package com.RPA.service;

import com.RPA.entity.InMemoryRobot;
import com.RPA.entity.Robot;
import com.RPA.entity.num.OperatingSystem;
import com.RPA.exception.ConflictException;
import com.RPA.exception.NotFoundException;
import com.RPA.repository.MemberRepository;
import com.RPA.repository.RobotRepository;
import com.RPA.request.CodeRequest;
import com.RPA.request.RobotInfoRequest;
import com.RPA.response.RobotResponse;
import com.RPA.response.VersionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class RobotService {
    @Value("${token.robot.secret}")
    private String robotAuthenticationKey;
    private List<InMemoryRobot> scriptRobots = new ArrayList<>();
    private List<InMemoryRobot> interactiveRobots = new ArrayList<>();
    private final RobotRepository robotRepository;
    private final MemberRepository memberRepository;

    synchronized public void releaseRobot(InMemoryRobot robot) {
        if (robot.isInteractive())
            interactiveRobots.add(robot);
        else
            scriptRobots.add(robot);
    }

    synchronized private InMemoryRobot searchRobot(List<InMemoryRobot> robots, Set<Long> groups,
                                                   String version, OperatingSystem os) {
        for (int i = 0; i < robots.size(); i++) {
            InMemoryRobot robot = robots.get(i);
            if (robot.os() == os && robot.versionFrom().compareTo(version) <= 0 && robot.groups().stream().anyMatch(groups::contains)) {
                return robots.remove(i);
            }
        }
        return null;
    }
    synchronized public InMemoryRobot receiveRobot(Set<Long> groups, String version, OperatingSystem os) {
        InMemoryRobot robot = searchRobot(scriptRobots, groups, version, os);
        if (robot == null)
            robot = searchRobot(interactiveRobots, groups, version, os);
        if (robot == null)
            throw new ConflictException("No available robots");
        return robot;
    }

    synchronized public InMemoryRobot getInteractiveRobot(Set<Long> groups, String version, OperatingSystem os) {
        for (int i = 0; i < interactiveRobots.size(); i++) {
            InMemoryRobot robot = interactiveRobots.get(i);
            if (robot.os() == os && robot.versionFrom().compareTo(version) == 0 && robot.groups().stream().anyMatch(groups::contains)) {
                return interactiveRobots.remove(i);
            }
        }
        throw new ConflictException("No available robots");
    }
    synchronized void addOrReplace(InMemoryRobot robot) {
        removeByHostPort(robot.hostPort());
        releaseRobot(robot);
    }

    synchronized void removeByHostPort(String hostPort) {
        final Predicate<InMemoryRobot> predicate = cur ->
                !Objects.equals(cur.hostPort(), hostPort);

        scriptRobots = scriptRobots.stream().filter(predicate).collect(Collectors.toList());
        interactiveRobots = interactiveRobots.stream().filter(predicate).collect(Collectors.toList());
    }


    @Async
    public void registerRobot(RobotInfoRequest request) {
        if (!Objects.equals(request.getSecret(), robotAuthenticationKey))
            return;

        if (!robotRepository.existsByVersion(request.getVersion())) {
            Robot robot = Robot.builder()
                    .version(request.getVersion())
                    .version_from(request.getVersionFrom())
                    .manual(request.getMan())
                    .build();
            robotRepository.save(robot);
        }

        InMemoryRobot robot = new InMemoryRobot(
                request.getOperatingSystem(),
                request.getVersion(),
                request.getVersionFrom(),
                request.getHostPort(),
                request.getGroups(),
                request.getInteractive()
        );
        addOrReplace(robot);
    }

    public void unregisterRobot(RobotInfoRequest request) {
        removeByHostPort(request.getHostPort());
    }
    public List<RobotResponse> getInteractiveRobots(Long uid) {
        Set<Long> groups = memberRepository.findGroupsByUserId(uid);
        return interactiveRobots.stream()
                .filter(robot -> robot.groups().stream().anyMatch(groups::contains))
                .map(RobotResponse::new)
                .collect(Collectors.toList());
    }

    public List<VersionResponse> getExistingVersions() {
        List<VersionResponse> response = new ArrayList<>();
        for (Robot robot : robotRepository.findAll())
            response.add(new VersionResponse(robot));
        return response;
    }

    public String getManual(Long id) {
        return robotRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Version not found"))
                .getManual();
    }

    private HttpRequest buildPostRequest(String uri, String request) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        return HttpRequest.newBuilder()
                .uri(java.net.URI.create(uri))
                .POST(HttpRequest.BodyPublishers.ofString(request))
                .headers(headers.entrySet().stream()
                        .flatMap(e -> e.getValue() == null ? null :
                                e.getValue().isBlank() ? null :
                                        Stream.of(e.getKey(), e.getValue()))
                        .toArray(String[]::new))
                .build();
    }

    public ResponseEntity executeScript(InMemoryRobot robot, CodeRequest request) {
        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(java.time.Duration.ofSeconds(600))
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String requestString = objectMapper.writeValueAsString(request);
            HttpRequest httpRequest = buildPostRequest("http://" + robot.hostPort() + "/session/execute", requestString);

            HttpResponse response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
            return ResponseEntity.status(response.statusCode()).headers(headers).body(response.body());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Server error");
        }
    }

    public ResponseEntity compileScript(InMemoryRobot robot, CodeRequest request) {
        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(java.time.Duration.ofSeconds(20))
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String requestString = objectMapper.writeValueAsString(request);
            HttpRequest httpRequest = buildPostRequest("http://" + robot.hostPort() + "/session/compile", requestString);
            HttpResponse response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
            return ResponseEntity.status(response.statusCode()).headers(headers).body(response.body());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Server error");
        }
    }

    public ResponseEntity clearState(InMemoryRobot robot) {
        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(java.time.Duration.ofSeconds(60))
                .build();

        try {
            HttpRequest httpRequest = buildPostRequest("http://" + robot.hostPort() + "/session/clear", "{}");
            HttpResponse response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
            return ResponseEntity.status(response.statusCode()).headers(headers).body(response.body());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Server error");
        }
    }
}

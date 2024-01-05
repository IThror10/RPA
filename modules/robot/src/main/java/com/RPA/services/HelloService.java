package com.RPA.services;

import com.RPA.requests.CreateUserRequest;
import com.RPA.requests.HelloRequest;
import com.RPA.responses.HelloResponse;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class HelloService {
    public HelloResponse sayHello(HelloRequest request) {
        if (Objects.equals(request.status(), "tutor"))
            throw new RuntimeException("Tutors are not welcome here");

        return new HelloResponse(
            request.name() + " " + request.status(),
            "We are glad to see you"
        );
    }

    public void createUser(CreateUserRequest request) {

    }
}

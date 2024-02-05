package com.RPA.service;

import com.RPA.entity.*;
import com.RPA.entity.id.GroupScriptId;
import com.RPA.entity.id.UserScriptId;
import com.RPA.exception.ConflictException;
import com.RPA.exception.ForbiddenException;
import com.RPA.exception.NotFoundException;
import com.RPA.repository.AuthorRepository;
import com.RPA.repository.ExecutorRepository;
import com.RPA.repository.ScriptRepository;
import com.RPA.request.CreateScriptRequest;
import com.RPA.request.UpdateScriptRequest;
import com.RPA.request.UsernameRequest;
import com.RPA.response.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScriptService {
    private final ScriptRepository scriptRepository;
    private final AuthorRepository authorRepository;
    private final ExecutorRepository executorRepository;
    private final UserService userService;

    @Transactional
    public ScriptResponse createScript(Long uid, CreateScriptRequest request) {
        if (scriptRepository.existsByName(request.name()))
            throw new ConflictException("Script with that name already exists");

        User author = User.builder().id(uid).build();
        Script script = Script.builder()
                .name(request.name())
                .creator(author)
                .code(request.code())
                .description(request.description())
                .inputData(request.inputData())
                .os(request.os())
                .version(request.version())
                .created(new Timestamp(System.currentTimeMillis()))
                .build();

        script = scriptRepository.save(script);
        UserScriptId id = new UserScriptId(uid, script.getId());
        authorRepository.save(new Author(id, author, script));
        return new ScriptResponse(script);
    }

    @Transactional
    public ScriptResponse updateScript(Long uid, Long sid, UpdateScriptRequest request) {
        Author author = authorRepository.findById(new UserScriptId(uid, sid))
                .orElseThrow(() -> new NotFoundException("Script not found"));

        Script script = author.getScript();
        if (request.description() != null)
            script.setDescription(request.description());
        if (request.os() != null)
            script.setOs(request.os());
        if (request.inputData() != null)
            script.setInputData(request.inputData());
        if (request.version() != null)
            script.setVersion(request.version());
        if (request.code() != null)
            script.setCode(request.code());
        return new ScriptResponse(scriptRepository.save(script));
    }

    @Transactional
    public void deleteScript(Long uid, Long sid) {
        Script script = scriptRepository.findById(sid)
                .orElseThrow(() -> new NotFoundException("Script not found"));

        if (script.getCreator().getId() != uid)
            throw new ForbiddenException("You can't delete that script");

        authorRepository.deleteAuthorsByScriptId(sid);
        executorRepository.deleteExecutorsByScriptId(sid);
        scriptRepository.delete(script);
    }

    @Transactional
    public void grantScriptToGroup(Long uid, Long sid, Long gid) {
        Script script = scriptRepository.findById(sid)
                .orElseThrow(() -> new NotFoundException("Script Not Found"));

        if (script.getCreator().getId() != uid)
            throw new ForbiddenException("You have no right to manage the groups");

        Executor executor = Executor.builder()
                .script(script)
                .group(Group.builder().id(gid).build())
                .id(new GroupScriptId(gid, sid))
                .build();
        executorRepository.save(executor);
    }

    @Transactional
    public void revokeScriptFromGroup(Long uid, Long sid, Long gid) {
        Script script = scriptRepository.findById(sid)
                .orElseThrow(() -> new NotFoundException("Script Not Found"));

        if (script.getCreator().getId() != uid)
            throw new ForbiddenException("You have no right to manage the groups");

        executorRepository.deleteById(new GroupScriptId(gid, sid));
    }

    @Transactional
    public void addAuthorToScript(Long uid, Long sid, UsernameRequest request) {
        User user = userService.getByUsername(request.getUsername());
        Script script = scriptRepository.findById(sid)
                .orElseThrow(() -> new NotFoundException("Script Not Found"));

        if (script.getCreator().getId() != uid)
            throw new ForbiddenException("You have no right to manage the groups");

        Author author = Author.builder()
                .script(script)
                .user(user)
                .id(new UserScriptId(user.getId(), sid))
                .build();
        authorRepository.save(author);
    }

    @Transactional
    public void removeAuthorFromScript(Long uid, Long sid, String username) {
        User user = userService.getByUsername(username);
        Script script = scriptRepository.findById(sid)
                .orElseThrow(() -> new NotFoundException("Script Not Found"));

        if (script.getCreator().getId() != uid)
            throw new ForbiddenException("You have no right to manage the groups");
        if (script.getCreator().getId() == user.getId())
            throw new ConflictException("You can't delete yourself from authors");

        UserScriptId id = new UserScriptId(user.getId(), sid);
        authorRepository.deleteById(id);
    }

    public List<ScriptWithRoleResponse> getManagingScripts(Long uid) {
        return authorRepository.findScriptsByUserId(uid).stream()
                .map(script -> new ScriptWithRoleResponse(
                        new ScriptResponse(script), script.getId() == uid)
                ).collect(Collectors.toList());
    }

    public List<ScriptResponse> getExecutingScripts(Long uid) {
        return executorRepository.findScriptsByUserInGroup(uid)
                .stream().map(ScriptResponse::new).collect(Collectors.toList());
    }

    public GroupsAndAuthorsResponse getScriptInfo(Long uid, Long sid) {
        Script script = scriptRepository.findById(sid)
                .orElseThrow(() -> new NotFoundException("Script not found"));

        if (script.getCreator().getId() != uid)
            throw new ForbiddenException("Private info");

        List<UserInfoResponse> users = authorRepository.findAuthorsByScriptId(sid).stream()
                .filter(user -> user.getId() != uid)
                .map(UserInfoResponse::new).collect(Collectors.toList());
        List<GroupResponse> groups = executorRepository.findGroupsByScriptId(sid).stream()
                .map(GroupResponse::new).collect(Collectors.toList());
        return new GroupsAndAuthorsResponse(groups, users);
    }
}

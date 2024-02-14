package com.RPA.service;

import com.RPA.entity.User;
import com.RPA.exception.ConflictException;
import com.RPA.exception.NotFoundException;
import com.RPA.repository.UserRepository;
import com.RPA.request.ChangeUserDataRequest;
import com.RPA.response.UserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public User create(User user) {
        if (repository.existsByUsername(user.getUsername()))
            throw new ConflictException("Username is already used. Try another one");
        else if (repository.existsByEmail(user.getEmail()))
            throw new ConflictException("Email is already used. Isn't it you?");
        else if (repository.existsByPhone(user.getPhone()))
            throw new ConflictException("Phone is already used. Isn't it you?");
        return repository.save(user);
    }

    public User getByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    public User getByUserId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    public List<UserInfoResponse> searchByUsername(String username) {
        List<User> users = repository.findFirst7ByUsernameStartsWithOrderByUsername(username);
        return users.stream().map(user -> new UserInfoResponse(user)).collect(Collectors.toList());
    }

    public UserInfoResponse updateUserInfo(Long userId, ChangeUserDataRequest request) {
        User user = repository.findById(userId).orElseThrow(() -> new RuntimeException("Unexpected error"));

        if (request.getEmail() != null && !Objects.equals(user.getEmail(), request.getEmail())) {
            if (repository.existsByEmail(request.getEmail()))
                throw new ConflictException("Email is already taken");
            user.setEmail(request.getEmail());
        }
        if (!Objects.equals(user.getPhone(), request.getPhone())) {
            if (repository.existsByPhone(request.getPhone()))
                throw new ConflictException("Phone is already taken");
            user.setPhone(request.getPhone());
        }
        return new UserInfoResponse(repository.save(user));
    }

    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    public User getCurrentUser() {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }
}

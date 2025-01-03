package com.security.vinclub.controller;

import com.security.vinclub.dto.request.users.UpdateUserRequest;
import com.security.vinclub.dto.request.users.UserSearchRequest;
import com.security.vinclub.exception.ServiceSecurityException;
import com.security.vinclub.service.UserService;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final Validator validator;

    @GetMapping("/{user_id}")
    public ResponseEntity<Object> getUserDetail(@PathVariable("user_id") String userId) {
        return ResponseEntity.ok(userService.getUserIdDetail(userId));
    }

    @PostMapping("/update")
    public ResponseEntity<Object> updateUserDetail(@RequestBody UpdateUserRequest request) {
        this.validateRequest(request);
        return ResponseEntity.ok(userService.updateUser(request));
    }

    @DeleteMapping("/admin/delete/{user_id}")
    public ResponseEntity<Object> deleteUser(@PathVariable("user_id") String userId) {
        return ResponseEntity.ok(userService.deleteUserById(userId));
    }

    @PatchMapping("/admin/activate/{user_id}")
    public ResponseEntity<Object> activateUser(@PathVariable("user_id") String userId) {
        return ResponseEntity.ok(userService.activateUserById(userId));
    }

    @PostMapping("/admin/all")
    public ResponseEntity<Object> getAllUser(@RequestBody UserSearchRequest request) {
        this.validateRequest(request);
        return ResponseEntity.ok(userService.getAllUsers(request));
    }

    private <T> void validateRequest(T request) {
        var violations = validator.validate(request);
        if (!violations.isEmpty()) throw new ServiceSecurityException(violations);
    }
}

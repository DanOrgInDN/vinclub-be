package com.security.duanspringboot.controller;

import com.security.duanspringboot.dto.request.users.UpdateUserRequest;
import com.security.duanspringboot.dto.request.users.UserSearchRequest;
import com.security.duanspringboot.exception.ServiceSecurityException;
import com.security.duanspringboot.service.UserService;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final Validator validator;

    @GetMapping("/un_auth/user/user_list")
    public ResponseEntity<Object> getAllUserDetail() {
        return ResponseEntity.ok(userService.getAllUserDetail());
    }

    @GetMapping("/un_auth/user/{user_id}")
    public ResponseEntity<Object> getUserDetail(@PathVariable("user_id") String userId) {
        return ResponseEntity.ok(userService.getUserIdDetail(userId));
    }

    @PostMapping("/un_auth/user/user_update")
    public ResponseEntity<Object> updateUserDetail(@RequestBody UpdateUserRequest request) {
        this.validateRequest(request);
        return ResponseEntity.ok(userService.updateUser(request));
    }

    @DeleteMapping("/admin/user/delete/{user_id}")
    public ResponseEntity<Object> deleteUser(@PathVariable("user_id") String userId) {
        return ResponseEntity.ok(userService.deleteUserById(userId));
    }

    @PostMapping("/admin/category/get_all_user")
    public ResponseEntity<Object> getAllUser(@RequestBody UserSearchRequest request) {
        this.validateRequest(request);
        return ResponseEntity.ok(userService.getAllUserPage(request));
    }

    private <T> void validateRequest(T request) {
        var violations = validator.validate(request);
        if (!violations.isEmpty()) throw new ServiceSecurityException(violations);
    }
}

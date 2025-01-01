package com.security.vinclub.service.impl;

import com.security.vinclub.core.response.ErrorData;
import com.security.vinclub.core.response.ResponseBody;
import com.security.vinclub.dto.request.authen.ChangePasswordRequest;
import com.security.vinclub.dto.request.authen.RefreshTokenRequest;
import com.security.vinclub.dto.request.authen.SignInRequest;
import com.security.vinclub.dto.request.authen.SignUpUserRequest;
import com.security.vinclub.dto.response.authen.JwtAuthenticationResponse;
import com.security.vinclub.entity.UsersModel;
import com.security.vinclub.exception.ServiceSecurityException;
import com.security.vinclub.repository.UsersRepository;
import com.security.vinclub.service.AuthenticationService;
import com.security.vinclub.service.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

import static com.security.vinclub.core.response.ResponseStatus.*;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UsersRepository usersRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JWTService jwtService;

    public ResponseBody<Object> registerUser(SignUpUserRequest signUpUserRequest) {
        UsersModel usersModel = new UsersModel();
        var existsEmail = usersRepository.existsByEmail(signUpUserRequest.getEmail());

        if (existsEmail) {
            var errorMapping = ErrorData.builder()
                    .errorKey1(EMAIL_EXIST.getCode())
                    .build();
            throw new ServiceSecurityException(HttpStatus.OK, EMAIL_EXIST, errorMapping);
        }

        usersModel.setUserId(UUID.randomUUID().toString().replaceAll("-", ""));
        usersModel.setEmail(signUpUserRequest.getEmail());
        usersModel.setFirstName(signUpUserRequest.getFirstName());
        usersModel.setLastName(signUpUserRequest.getLastName());
        usersModel.setRoleId(signUpUserRequest.getRoleId());
        usersModel.setPassword(passwordEncoder.encode(signUpUserRequest.getPassword()));
        usersModel.setCreateDate(LocalDateTime.now());
        usersRepository.save(usersModel);
        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, usersModel);
        return response;
    }

    public ResponseBody<Object> signIn(SignInRequest signInRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(signInRequest.getEmail(), signInRequest.getPassword())
            );
        } catch (AuthenticationException e) {
            var errorMapping = ErrorData.builder()
                    .errorKey2(INVALID_CREDENTIALS.getCode())
                    .build();
            throw new ServiceSecurityException(HttpStatus.UNAUTHORIZED, INVALID_CREDENTIALS, errorMapping);
        }
        var user = usersRepository.findByEmail(signInRequest.getEmail()).orElseThrow(() -> {
            var errorMapping = ErrorData.builder()
                    .errorKey2(INVALID_REQUEST_PARAMETER.getCode())
                    .build();
            return new ServiceSecurityException(HttpStatus.OK, INVALID_REQUEST_PARAMETER, errorMapping);
        });
        var jwt = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);

        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
        jwtAuthenticationResponse.setToken(jwt);
        jwtAuthenticationResponse.setRefreshToken(refreshToken);
        jwtAuthenticationResponse.setUserId(user.getUserId());
        jwtAuthenticationResponse.setRoleId(user.getRoleId());
        jwtAuthenticationResponse.setUsername(user.getUsername());

        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, jwtAuthenticationResponse);
        return response;
    }

    @Override
    public ResponseBody<Object> changePassword(ChangePasswordRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getOldPassword())
            );
        } catch (AuthenticationException e) {
            var errorMapping = ErrorData.builder()
                    .errorKey2(INVALID_CREDENTIALS.getCode())
                    .build();
            throw new ServiceSecurityException(HttpStatus.UNAUTHORIZED, INVALID_CREDENTIALS, errorMapping);
        }
        var user = usersRepository.findByEmail(request.getEmail()).orElseThrow(() -> {
            var errorMapping = ErrorData.builder()
                    .errorKey2(USER_NOT_FOUND.getCode())
                    .build();
            return new ServiceSecurityException(HttpStatus.OK, USER_NOT_FOUND, errorMapping);
        });

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setCreateDate(LocalDateTime.now());
        usersRepository.save(user);
        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, user);
        return response;
    }

    public ResponseBody<Object> refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String userEmail = jwtService.extractUsername(refreshTokenRequest.getToken());
        UsersModel usersModel = usersRepository.findByEmail(userEmail).orElseThrow();

        if (jwtService.isTokenValid(refreshTokenRequest.getToken(), usersModel)) {
            var errorMapping = ErrorData.builder()
                    .errorKey1(INVALID_REQUEST_PARAMETER.getCode())
                    .build();
            throw new ServiceSecurityException(HttpStatus.OK, INVALID_REQUEST_PARAMETER, errorMapping);
        }
        var jwt = jwtService.generateToken(usersModel);

        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
        jwtAuthenticationResponse.setToken(jwt);
        jwtAuthenticationResponse.setRefreshToken(refreshTokenRequest.getToken());
        jwtAuthenticationResponse.setUserId(usersModel.getUserId());

        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, jwtAuthenticationResponse);
        return response;
    }
}

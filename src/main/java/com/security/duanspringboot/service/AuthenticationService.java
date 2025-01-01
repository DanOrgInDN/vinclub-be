package com.security.duanspringboot.service;

import com.security.duanspringboot.core.response.ResponseBody;
import com.security.duanspringboot.dto.request.authen.ChangePasswordRequest;
import com.security.duanspringboot.dto.request.authen.RefreshTokenRequest;
import com.security.duanspringboot.dto.request.authen.SignInRequest;
import com.security.duanspringboot.dto.request.authen.SignUpUserRequest;

public interface AuthenticationService {

    ResponseBody<Object> registerUser(SignUpUserRequest signUpUserRequest);

    ResponseBody<Object> signIn(SignInRequest signInRequest);

    ResponseBody<Object> changePassword(ChangePasswordRequest changePasswordRequest);

    ResponseBody<Object> refreshToken(RefreshTokenRequest refreshTokenRequest);
}

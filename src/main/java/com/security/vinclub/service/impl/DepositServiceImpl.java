package com.security.vinclub.service.impl;

import com.security.vinclub.core.response.ErrorData;
import com.security.vinclub.core.response.ResponseBody;
import com.security.vinclub.dto.request.deposit.CreateDepositRequest;
import com.security.vinclub.entity.Deposit;
import com.security.vinclub.entity.User;
import com.security.vinclub.exception.ServiceSecurityException;
import com.security.vinclub.repository.DepositRepository;
import com.security.vinclub.repository.UserRepository;
import com.security.vinclub.service.DepositService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.security.vinclub.core.response.ResponseStatus.*;

@Service
@RequiredArgsConstructor
public class DepositServiceImpl implements DepositService {
    private final DepositRepository depositRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseBody<Object> createDepositTransaction(CreateDepositRequest request) {
        var isUserExisted = userRepository.existsById(request.getUserId());

        if (isUserExisted) {
            var errorMapping = ErrorData.builder()
                    .errorKey1(USER_NOT_FOUND.getCode())
                    .build();
            throw new ServiceSecurityException(HttpStatus.OK, USER_NOT_FOUND, errorMapping);
        }
        Deposit deposit = new Deposit();
        deposit.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        deposit.setUserId(request.getUserId());
        deposit.setAccountNumber(request.getAccountNumber());
        deposit.setAccountName(request.getAccountName());
        deposit.setBankName(request.getBankName());
        deposit.setAmount(request.getAmount());
        deposit.setCreatedDate(LocalDateTime.now());
        depositRepository.save(deposit);
        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, deposit);
        return response;
    }
}

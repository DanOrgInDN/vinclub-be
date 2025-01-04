package com.security.vinclub.service.impl;

import com.security.vinclub.core.response.ErrorData;
import com.security.vinclub.core.response.ResponseBody;
import com.security.vinclub.dto.request.withdraw.CreateWithdrawRequest;
import com.security.vinclub.entity.Withdraw;
import com.security.vinclub.exception.ServiceSecurityException;
import com.security.vinclub.repository.UserRepository;
import com.security.vinclub.repository.WithdrawRepository;
import com.security.vinclub.service.WithdrawService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.security.vinclub.core.response.ResponseStatus.SUCCESS;
import static com.security.vinclub.core.response.ResponseStatus.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class WithdrawServiceImpl implements WithdrawService {
    private final WithdrawRepository withdrawRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseBody<Object> createWithdrawTransaction(CreateWithdrawRequest request) {
        var isUserExisted = userRepository.existsById(request.getUserId());

        if (!isUserExisted) {
            var errorMapping = ErrorData.builder()
                    .errorKey1(USER_NOT_FOUND.getCode())
                    .build();
            throw new ServiceSecurityException(HttpStatus.OK, USER_NOT_FOUND, errorMapping);
        }
        Withdraw withdraw = new Withdraw();
        withdraw.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        withdraw.setUserId(request.getUserId());
        withdraw.setAccountNumber(request.getAccountNumber());
        withdraw.setAccountName(request.getAccountName());
        withdraw.setBankName(request.getBankName());
        withdraw.setAmount(request.getAmount());
        withdraw.setCreatedDate(LocalDateTime.now());
        withdrawRepository.save(withdraw);
        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, withdraw);
        return response;
    }
}

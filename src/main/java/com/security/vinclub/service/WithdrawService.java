package com.security.vinclub.service;

import com.security.vinclub.core.response.ResponseBody;
import com.security.vinclub.dto.request.deposit.CreateDepositRequest;
import com.security.vinclub.dto.request.withdraw.CreateWithdrawRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface WithdrawService {

    ResponseBody<Object> createWithdrawTransaction(CreateWithdrawRequest request);
}

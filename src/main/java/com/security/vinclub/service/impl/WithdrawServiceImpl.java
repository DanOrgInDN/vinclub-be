package com.security.vinclub.service.impl;

import com.security.vinclub.repository.WithdrawRepository;
import com.security.vinclub.service.WithdrawService;

public class WithdrawServiceImpl implements WithdrawService {
    private final WithdrawRepository withdrawRepository;

    public WithdrawServiceImpl(WithdrawRepository withdrawRepository) {
        this.withdrawRepository = withdrawRepository;
    }
}

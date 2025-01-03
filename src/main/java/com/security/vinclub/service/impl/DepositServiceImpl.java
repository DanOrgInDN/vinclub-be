package com.security.vinclub.service.impl;

import com.security.vinclub.repository.DepositRepository;
import com.security.vinclub.service.DepositService;

public class DepositServiceImpl implements DepositService {
    private final DepositRepository depositRepository;

    public DepositServiceImpl(DepositRepository depositRepository) {
        this.depositRepository = depositRepository;
    }
}

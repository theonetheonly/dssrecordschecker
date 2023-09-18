package com.sgasecurity.dssrecordschecker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepositService {
    @Autowired
    DepositRepository depositRepository;
    public List<Deposit> getAllDeposits()
    {
        List<Deposit> deposits = (List<Deposit>) depositRepository.findAll();
        return deposits;
    }

    public Deposit getDeposit(String systemCustomerNo) {
        return (Deposit) this.depositRepository.findBySystemCustomerNo(systemCustomerNo);
    }

    public Deposit saveDeposit(Deposit deposit)
    {
        return depositRepository.save(deposit);
    }

    public Deposit getLastDeposit() {
        return depositRepository.findFirstByOrderByIdDesc();
    }
}

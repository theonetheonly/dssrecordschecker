package com.sgasecurity.dssrecordschecker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthDataService {
    @Autowired
    AuthDataRepository authDataRepository;
    public AuthData getAuthDataByCustomerId(int customerId) {
        return this.authDataRepository.findByCustomerId(customerId);
    }

    public AuthData getAuthDataBySystemCustomerNo(String systemCustomerNo) {
        return this.authDataRepository.findBySystemCustomerNo(systemCustomerNo);
    }

    public AuthData saveAuthData(AuthData authData)
    {
        return authDataRepository.save(authData);
    }

    public List<AuthData> verifyOTP(String customerNo, String otp) {
        return authDataRepository.verifyOTP(customerNo, otp);
    }
}

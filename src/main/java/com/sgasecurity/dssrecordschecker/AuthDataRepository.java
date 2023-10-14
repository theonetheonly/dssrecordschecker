package com.sgasecurity.dssrecordschecker;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthDataRepository extends JpaRepository<AuthData, Long> {
    AuthData findByCustomerId(int customerId);

    AuthData findBySystemCustomerNo(String systemCustomerNo);

    @Query("SELECT a FROM AuthData a WHERE a.systemCustomerNo = :customerNo AND a.otp = :otp")
    List<AuthData> verifyOTP(String customerNo, String otp);
}

package com.sgasecurity.dssrecordschecker;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "auth_data")
public class AuthData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "customer_id")
    private int customerId;
    @Column(name = "password")
    private String password;
    @Column(name = "system_customer_no")
    private String systemCustomerNo;
    @Column(name = "otp")
    private String otp;
    @Column(name = "is_first_login")
    private String isFirstLogin;
    @Column(name = "timestamp")
    private Timestamp timestamp;

    public AuthData() {
    }

    public AuthData(long id, int customerId, String systemCustomerNo, String password, String otp, String isFirstLogin, Timestamp timestamp) {
        this.id = id;
        this.customerId = customerId;
        this.systemCustomerNo = systemCustomerNo;
        this.password = password;
        this.otp = otp;
        this.isFirstLogin = isFirstLogin;
        this.timestamp = timestamp;
    }

    public long getId()
    {
        return id;
    }
    public void setId(long id)
    {
        this.id = id;
    }
    public int getCustomerId()
    {
        return customerId;
    }
    public void setCustomerId(int customerId)
    {
        this.customerId = customerId;
    }

    public String getPassword()
    {
        return password;
    }
    public void setPassword(String password)
    {
        this.password = password;
    }
    public String getSystemCustomerNo()
    {
        return systemCustomerNo;
    }
    public void setSystemCustomerNo(String systemCustomerNo)
    {
        this.systemCustomerNo = systemCustomerNo;
    }
    public String getOtp()
    {
        return otp;
    }
    public void setOtp(String otp)
    {
        this.otp = otp;
    }
    public String getIsFirstLogin()
    {
        return isFirstLogin;
    }
    public void setIsFirstLogin(String isFirstLogin)
    {
        this.isFirstLogin = isFirstLogin;
    }
    public Timestamp getTimestamp()
    {
        return timestamp;
    }
    public void setTimestamp(Timestamp timestamp)
    {
        this.timestamp = timestamp;
    }
}
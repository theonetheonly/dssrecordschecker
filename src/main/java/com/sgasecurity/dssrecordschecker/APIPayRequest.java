package com.sgasecurity.dssrecordschecker;

public class APIPayRequest {
    private String sessionNo;
    private String customerNo;
    private Double monthlyCost;
    private Double deposit;
    private String transID;
    private String uniqueSiteId;

    private String customerId;

    public APIPayRequest() {
    }

    public APIPayRequest(String sessionNo, String customerNo, Double monthlyCost, Double deposit, String transID, String uniqueSiteId, String customerId) {
        this.sessionNo = sessionNo;
        this.customerNo = customerNo;
        this.monthlyCost = monthlyCost;
        this.deposit = deposit;
        this.transID = transID;
        this.uniqueSiteId = uniqueSiteId;
        this.customerId = customerId;
    }

    public String getCustomerId()
    {
        return customerId;
    }
    public void setCustomerId() {this.customerId = customerId;}
    public String getSessionNo()
    {
        return sessionNo;
    }
    public void setSessionNo(String sessionNo)
    {
        this.sessionNo = sessionNo;
    }

    public String getCustomerNo()
    {
        return customerNo;
    }
    public void setCustomerNo(String customerNo)
    {
        this.customerNo = customerNo;
    }

    public Double getMonthlyCost()
    {
        return monthlyCost;
    }
    public void setMonthlyCost(Double monthlyCost)
    {
        this.monthlyCost = monthlyCost;
    }

    public Double getDeposit()
    {
        return deposit;
    }
    public void setDeposit(Double deposit)
    {
        this.deposit = deposit;
    }
    public String getTransID()
    {
        return transID;
    }
    public void setTransID(String transID)
    {
        this.transID = transID;
    }

    public String getUniqueSiteId()
    {
        return uniqueSiteId;
    }
    public void setUniqueSiteId(String uniqueSiteId)
    {
        this.uniqueSiteId = uniqueSiteId;
    }
}

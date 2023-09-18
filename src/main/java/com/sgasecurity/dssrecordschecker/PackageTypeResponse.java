package com.sgasecurity.dssrecordschecker;

public class PackageTypeResponse {
    private String packageTypeName;
    private Double deposit;
    private Double monthlyCost;

    public PackageTypeResponse() {
    }

    public PackageTypeResponse(String packageTypeName, Double deposit, Double monthlyCost) {
        this.packageTypeName = packageTypeName;
        this.deposit = deposit;
        this.monthlyCost = monthlyCost;
    }

    public String getPackageTypeName()
    {
        return packageTypeName;
    }
    public void setPackageTypeName(String packageTypeName)
    {
        this.packageTypeName = packageTypeName;
    }

    public Double getDeposit()
    {
        return deposit;
    }
    public void setDeposit(Double deposit)
    {
        this.deposit = deposit;
    }

    public Double getMonthlyCost()
    {
        return monthlyCost;
    }
    public void setMonthlyCost(Double monthlyCost)
    {
        this.monthlyCost = monthlyCost;
    }
}

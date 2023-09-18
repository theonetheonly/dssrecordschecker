package com.sgasecurity.dssrecordschecker;

import javax.persistence.*;

@Entity
@Table(name = "package_types")
public class PackageType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "package_type_name")
    private String packageTypeName;
    @Column(name = "monthly_cost_exclusive")
    private Double monthlyCostExclusive;
    @Column(name = "monthly_cost_inclusive")
    private Double monthlyCostInclusive;
    @Column(name = "deposit_no_of_months")
    private Double depositNoOfMonths;
    @Column(name = "vat_rate")
    private Double vatRate;
    @Column(name = "deposit_amount")
    private Double depositAmount;
    @Column(name = "status")
    private String status;

    public PackageType() {
    }

    public PackageType(int id, String packageTypeName, Double monthlyCostExclusive, Double monthlyCostInclusive, Double depositNoOfMonths, Double vatRate, Double depositAmount, String status) {
        this.id = id;
        this.packageTypeName = packageTypeName;
        this.monthlyCostExclusive = monthlyCostExclusive;
        this.monthlyCostInclusive = monthlyCostInclusive;
        this.depositNoOfMonths = depositNoOfMonths;
        this.vatRate = vatRate;
        this.depositAmount = depositAmount;
        this.status = status;
    }

    public int getId()
    {
        return id;
    }
    public void setId(int id)
    {
        this.id = id;
    }

    public String getPackageTypeName()
    {
        return packageTypeName;
    }
    public void setPackageTypeName(String packageTypeName)
    {
        this.packageTypeName = packageTypeName;
    }

    public Double getMonthlyCostExclusive()
    {
        return monthlyCostExclusive;
    }
    public void setMonthlyCostExclusive(Double monthlyCostExclusive)
    {
        this.monthlyCostExclusive = monthlyCostExclusive;
    }

    public Double getMonthlyCostInclusive()
    {
        return monthlyCostInclusive;
    }
    public void setMonthlyCostInclusive(Double monthlyCostInclusive)
    {
        this.monthlyCostInclusive = monthlyCostInclusive;
    }
    public Double getDepositNoOfMonths()
    {
        return depositNoOfMonths;
    }
    public void setDepositNoOfMonths(Double depositNoOfMonths)
    {
        this.depositNoOfMonths = depositNoOfMonths;
    }
    public Double getVatRate()
    {
        return vatRate;
    }
    public void setVatRate(Double vatRate)
    {
        this.vatRate = vatRate;
    }
    public Double getDepositAmount()
    {
        return depositAmount;
    }
    public void setDepositAmount(Double depositAmount)
    {
        this.depositAmount = depositAmount;
    }
    public String getStatus()
    {
        return status;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }
}

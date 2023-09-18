package com.sgasecurity.dssrecordschecker;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@Table(name = "deposits")
public class Deposit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "amount")
    private double amount;
    @Column(name = "system_customer_no")
    private String systemCustomerNo;
    @Column(name = "dpo_to_omni_transactional_no")
    private String dpoToOmniTransactionalNo;
    @Column(name = "invoice_ref_no")
    private String invoiceRefNo;
    @Column(name = "job_no")
    private String jobNo;
    @Column(name = "installation_id")
    private long installationId;
    @Column(name = "package_type_id")
    private int packageTypeId;
    @Column(name = "date_paid")
    private LocalDate datePaid;
    @Column(name = "narrative")
    private String narrative;
    @Column(name = "status")
    private String status;
    @Column(name = "timestamp")
    private Timestamp timestamp;

    public Deposit() {
    }

    public Deposit(long id, double amount, String systemCustomerNo, String dpoToOmniTransactionalNo, String invoiceRefNo, String jobNo, long installationId, int packageTypeId, LocalDate datePaid, String narrative, String status, Timestamp timestamp) {
        this.id = id;
        this.amount = amount;
        this.systemCustomerNo = systemCustomerNo;
        this.dpoToOmniTransactionalNo = dpoToOmniTransactionalNo;
        this.invoiceRefNo = invoiceRefNo;
        this.jobNo = jobNo;
        this.installationId = installationId;
        this.packageTypeId = packageTypeId;
        this.datePaid = datePaid;
        this.narrative = narrative;
        this.status = status;
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
    public Double getAmount()
    {
        return amount;
    }
    public void setAmount(Double amount)
    {
        this.amount = amount;
    }
    public String getSystemCustomerNo()
    {
        return systemCustomerNo;
    }
    public void setSystemCustomerNo(String systemCustomerNo)
    {
        this.systemCustomerNo = systemCustomerNo;
    }
    public LocalDate getDatePaid()
    {
        return datePaid;
    }
    public void setDatePaid(LocalDate datePaid)
    {
        this.datePaid = datePaid;
    }
    public String getDpoToOmniTransactionalNo() {
        return dpoToOmniTransactionalNo;
    }
    public void setDpoToOmniTransactionalNo(String dpoToOmniTransactionalNo) {
        this.dpoToOmniTransactionalNo = dpoToOmniTransactionalNo;
    }
    public String getInvoiceRefNo() {
        return invoiceRefNo;
    }
    public void setInvoiceRefNo(String invoiceRefNo) {
        this.invoiceRefNo = invoiceRefNo;
    }
    public String getJobNo() {
        return jobNo;
    }
    public void setJobNo(String jobNo) {
        this.jobNo = jobNo;
    }
    public long getInstallationId() {
        return installationId;
    }

    public void setInstallationId(long installationId) {
        this.installationId = installationId;
    }

    public int getPackageTypeId() {
        return packageTypeId;
    }

    public void setPackageTypeId(int packageTypeId) {
        this.packageTypeId = packageTypeId;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getNarrative()
    {
        return narrative;
    }
    public void setNarrative(String narrative)
    {
        this.narrative = narrative;
    }
    public Timestamp getTimestamp()
    {
        return timestamp;
    }
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}

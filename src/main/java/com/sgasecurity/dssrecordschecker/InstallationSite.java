package com.sgasecurity.dssrecordschecker;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@Table(name = "installation_sites")
public class InstallationSite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "customer_id")
    private int customerId;
    @Column(name = "system_customer_no")
    private String systemCustomerNo;
    @Column(name = "session_no")
    private String sessionNo;
    @Column(name = "package_type_id")
    private int packageTypeId;
    @Column(name = "package_type_name")
    private String packageTypeName;
    @Column(name = "deposit_amount")
    private Double depositAmount;
    @Column(name = "gps_coordinate")
    private String gpsCoordinate;
    @Column(name = "w3w_coordinate")
    private String w3wCoordinate;
    @Column(name = "street")
    private String street;
    @Column(name = "hpp_site_id")
    private String hppSiteId;
    @Column(name = "unique_site_id")
    private String uniqueSiteId;
    @Column(name = "hik_formatted_customer_site_name")
    private String hikFormattedCustomerSiteName;
    @Column(name = "device_serial")
    private String deviceSerial;
    @Column(name = "house_door_no")
    private String houseDoorNo;
    @Column(name = "home_type")
    private String homeType;
    @Column(name = "postal_address")
    private String postalAddress;
    @Column(name = "postal_code")
    private String postalCode;
    @Column(name = "estate")
    private String estate;
    @Column(name = "household_number")
    private int householdNumber;
    @Column(name = "locality")
    private String locality;
    @Column(name = "installation_date")
    private LocalDate installationDate;
    @Column(name = "installation_status")
    private String installationStatus;
    @Column(name = "handover_status")
    private String handoverStatus;
    @Column(name = "handover_date")
    private LocalDate handoverDate;
    @Column(name = "last_payment_date")
    private LocalDate lastPaymentDate;
    @Column(name = "next_payment_date")
    private LocalDate nextPaymentDate;
    @Column(name = "payment_status")
    private int paymentStatus;
    @Column(name = "is_assigned_technician")
    private String isAssignedTechnician;
    @Column(name = "timestamp")
    private Timestamp timestamp;

    public InstallationSite() {
    }

    public InstallationSite(long id, String systemCustomerNo, String sessionNo, int customerId, int packageTypeId, String packageTypeName, Double depositAmount, String gpsCoordinate, String w3wCoordinate, String locality, String street, String hppSiteId, String uniqueSiteId, String hikFormattedCustomerSiteName, String deviceSerial, String houseDoorNo, String homeType, String postalAddress, String postalCode, String estate, int householdNumber, LocalDate installationDate, String installationStatus, String handoverStatus, LocalDate handoverDate, int paymentStatus, LocalDate lastPaymentDate, LocalDate nextPaymentDate, String isAssignedTechnician, Timestamp timestamp) {
        this.id = id;
        this.customerId = customerId;
        this.systemCustomerNo = systemCustomerNo;
        this.sessionNo = sessionNo;
        this.packageTypeId = packageTypeId;
        this.packageTypeName = packageTypeName;
        this.depositAmount = depositAmount;
        this.gpsCoordinate = gpsCoordinate;
        this.w3wCoordinate = w3wCoordinate;
        this.locality = locality;
        this.street = street;
        this.hppSiteId = hppSiteId;
        this.uniqueSiteId = uniqueSiteId;
        this.hikFormattedCustomerSiteName = hikFormattedCustomerSiteName;
        this.deviceSerial = deviceSerial;
        this.houseDoorNo = houseDoorNo;
        this.homeType = homeType;
        this.postalAddress = postalAddress;
        this.postalCode = postalCode;
        this.estate = estate;
        this.householdNumber = householdNumber;
        this.installationDate = installationDate;
        this.installationStatus = installationStatus;
        this.handoverStatus = handoverStatus;
        this.handoverDate = handoverDate;
        this.paymentStatus = paymentStatus;
        this.lastPaymentDate = lastPaymentDate;
        this.nextPaymentDate = nextPaymentDate;
        this.isAssignedTechnician = isAssignedTechnician;
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
    public String getSystemCustomerNo()
    {
        return systemCustomerNo;
    }
    public void setSystemCustomerNo(String systemCustomerNo)
    {
        this.systemCustomerNo = systemCustomerNo;
    }
    public String getSessionNo()
    {
        return sessionNo;
    }
    public void setSessionNo(String sessionNo)
    {
        this.sessionNo = sessionNo;
    }
    public int getPackageTypeId()
    {
        return packageTypeId;
    }
    public void setPackageTypeId(int packageTypeId)
    {
        this.packageTypeId = packageTypeId;
    }
    public String getPackageTypeName()
    {
        return packageTypeName;
    }
    public void setPackageTypeName(String packageTypeName)
    {
        this.packageTypeName = packageTypeName;
    }
    public Double getDepositAmount()
    {
        return depositAmount;
    }
    public void setDepositAmount(Double depositAmount)
    {
        this.depositAmount = depositAmount;
    }
    public String getGpsCoordinate(){ return gpsCoordinate; }
    public void setGpsCoordinate(String gpsCoordinate)
    {
        this.gpsCoordinate = gpsCoordinate;
    }

    public String getW3wCoordinate(){ return w3wCoordinate; }
    public void setW3wCoordinate(String w3wCoordinate)
    {
        this.w3wCoordinate = w3wCoordinate;
    }
    public String getHppSiteId(){ return hppSiteId; }
    public void setHppSiteId(String hppSiteId)
    {
        this.hppSiteId = hppSiteId;
    }
    public String getUniqueSiteId(){ return uniqueSiteId; }
    public void setUniqueSiteId(String uniqueSiteId)
    {
        this.uniqueSiteId = uniqueSiteId;
    }
    public String getHikFormattedCustomerSiteName(){ return hikFormattedCustomerSiteName; }
    public void setHikFormattedCustomerSiteName(String hikFormattedCustomerSiteName)
    {
        this.hikFormattedCustomerSiteName = hikFormattedCustomerSiteName;
    }
    public String getDeviceSerial(){ return deviceSerial; }
    public void setDeviceSerial(String deviceSerial)
    {
        this.deviceSerial = deviceSerial;
    }
    public String getHouseDoorNo(){ return houseDoorNo; }
    public void setHouseDoorNo(String houseDoorNo)
    {
        this.houseDoorNo = houseDoorNo;
    }
    public String getHomeType()
    {
        return homeType;
    }
    public void setHomeType(String homeType)
    {
        this.homeType = homeType;
    }
    public String getPostalAddress()
    {
        return postalAddress;
    }
    public void setPostalAddress(String postalAddress)
    {
        this.postalAddress = postalAddress;
    }
    public String getPostalCode()
    {
        return postalCode;
    }
    public void setPostalCode(String postalCode)
    {
        this.postalCode = postalCode;
    }
    public String getLocality()
    {
        return locality;
    }
    public void setLocality(String locality)
    {
        this.locality = locality;
    }
    public String getStreet()
    {
        return street;
    }
    public void setStreet(String street)
    {
        this.street = street;
    }
    public String getEstate()
    {
        return estate;
    }
    public void setEstate(String estate)
    {
        this.estate = estate;
    }
    public int getHouseholdNumber()
    {
        return householdNumber;
    }
    public void setHouseholdNumber(int householdNumber)
    {
        this.householdNumber = householdNumber;
    }
    public LocalDate getInstallationDate()
    {
        return installationDate;
    }
    public void setInstallationDate(LocalDate installationDate)
    {
        this.installationDate = installationDate;
    }
    public String getInstallationStatus()
    {
        return installationStatus;
    }
    public void setInstallationStatus(String installationStatus)
    {
        this.installationStatus = installationStatus;
    }
    public String getHandoverStatus()
    {
        return handoverStatus;
    }
    public void setHandoverStatus(String handoverStatus)
    {
        this.handoverStatus = handoverStatus;
    }
    public LocalDate getHandoverDate()
    {
        return handoverDate;
    }
    public void setHandoverDate(LocalDate handoverDate)
    {
        this.handoverDate = handoverDate;
    }
    public int getPaymentStatus()
    {
        return paymentStatus;
    }
    public void setPaymentStatus(int paymentStatus)
    {
        this.paymentStatus = paymentStatus;
    }
    public LocalDate getLastPaymentDate()
    {
        return lastPaymentDate;
    }
    public void setLastPaymentDate(LocalDate lastPaymentDate)
    {
        this.lastPaymentDate = lastPaymentDate;
    }
    public LocalDate getNextPaymentDate()
    {
        return nextPaymentDate;
    }
    public void setNextPaymentDate(LocalDate nextPaymentDate)
    {
        this.nextPaymentDate = nextPaymentDate;
    }
    public String getIsAssignedTechnician()
    {
        return isAssignedTechnician;
    }
    public void setIsAssignedTechnician(String isAssignedTechnician)
    {
        this.isAssignedTechnician = isAssignedTechnician;
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

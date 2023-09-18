package com.sgasecurity.dssrecordschecker;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "temp_package_and_calendar")
public class TempPackageAndCalendar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "timestamp", insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp timestamp;

    @Column(name="current_user_id", nullable = false)
    private int currentUserId;

    @Column(name="package_details", nullable = true)
    private String packageDetails;
    @Column(name="dates_bookings_details", nullable = true)
    private String datesBookingsDetails;


    public TempPackageAndCalendar()
    {

    }
    public TempPackageAndCalendar(int currentUserId, String packageDetails, String datesBookingsDetails) {
        this.currentUserId = currentUserId;
        this.packageDetails = packageDetails;
        this.datesBookingsDetails = datesBookingsDetails;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public int getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(int currentUserId) {
        this.currentUserId = currentUserId;
    }

    public String getPackageDetails() {
        return packageDetails;
    }

    public void setPackageDetails(String packageDetails) {
        this.packageDetails = packageDetails;
    }

    public String getDatesBookingsDetails() {
        return datesBookingsDetails;
    }

    public void setDatesBookingsDetails(String datesBookingsDetails) {
        this.datesBookingsDetails = datesBookingsDetails;
    }
}
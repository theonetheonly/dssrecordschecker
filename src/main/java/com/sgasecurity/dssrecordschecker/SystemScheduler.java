package com.sgasecurity.dssrecordschecker;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@Table(name = "system_scheduler")
public class SystemScheduler {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "schedule_date")
    private LocalDate scheduleDate;
    @Column(name = "safe_home_bookings_counter")
    private int safeHomeBookings;
    @Column(name = "safe_home_plus_bookings_counter")
    private int safeHomePlusBookings;
    @Column(name = "timestamp")
    private Timestamp timestamp;

    public SystemScheduler() {
    }

    public SystemScheduler(int id, LocalDate scheduleDate, int safeHomeBookings, int safeHomePlusBookings, Timestamp timestamp) {
        this.id = id;
        this.scheduleDate = scheduleDate;
        this.safeHomeBookings = safeHomeBookings;
        this.safeHomePlusBookings = safeHomePlusBookings;
        this.timestamp = timestamp;
    }

    public int getId()
    {
        return id;
    }
    public void setId(int id)
    {
        this.id = id;
    }

    public LocalDate getScheduleDate()
    {
        return scheduleDate;
    }
    public void setScheduleDate(LocalDate scheduleDate)
    {
        this.scheduleDate = scheduleDate;
    }
    public int getSafeHomeBookings()
    {
        return safeHomeBookings;
    }
    public void setSafeHomeBookings(int safeHomeBookings)
    {
        this.safeHomeBookings = safeHomeBookings;
    }
    public int getSafeHomePlusBookings()
    {
        return safeHomePlusBookings;
    }
    public void setSafeHomePlusBookings(int safeHomePlusBookings)
    {
        this.safeHomePlusBookings = safeHomePlusBookings;
    }
    public void getTimestamp(Timestamp timestamp)
    {
        this.timestamp = timestamp;
    }
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}

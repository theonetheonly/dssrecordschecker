package com.sgasecurity.dssrecordschecker;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface SystemSchedulerRepository extends JpaRepository<SystemScheduler, Integer> {
    SystemScheduler findByscheduleDate(LocalDate scheduleDate);
    boolean existsByScheduleDate(LocalDate scheduleDate);
    List<SystemScheduler> findBySafeHomeBookings(int safeHomeBookings);
    List<SystemScheduler> findBySafeHomePlusBookings(int safeHomePlusBookings);

    @Query("SELECT y FROM SystemScheduler y WHERE y.safeHomeBookings > 0")
    List<SystemScheduler> findAllSafeHomeBookingDates();

    @Query("SELECT y FROM SystemScheduler y WHERE y.safeHomePlusBookings > 0")
    List<SystemScheduler> findAllSafeHomePlusBookingDates();
}

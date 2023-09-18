package com.sgasecurity.dssrecordschecker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SystemSchedulerService {
    @Autowired
    SystemSchedulerRepository systemSchedulerRepository;
    public SystemScheduler saveSystemScheduler(SystemScheduler systemScheduler)
    {
        return systemSchedulerRepository.save(systemScheduler);
    }

    public SystemScheduler getByScheduleDate(LocalDate scheduleDate) {
        return systemSchedulerRepository.findByscheduleDate(scheduleDate);
    }

    public boolean isDateExisting(LocalDate scheduleDate) {
        return systemSchedulerRepository.existsByScheduleDate(scheduleDate);
    }

    public List<SystemScheduler> getSafeHomeBookings(int safeHomeBookings) {
        return systemSchedulerRepository.findBySafeHomeBookings(safeHomeBookings);
    }

    public List<SystemScheduler> getSafeHomePlusBookings(int safeHomePlusBookings) {
        return systemSchedulerRepository.findBySafeHomePlusBookings(safeHomePlusBookings);
    }

    public List<SystemScheduler> getAllSafeHomeBookings() {
        return systemSchedulerRepository.findAllSafeHomeBookingDates();
    }

    public List<SystemScheduler> getAllSafeHomePlusBookings() {
        return systemSchedulerRepository.findAllSafeHomePlusBookingDates();
    }
}

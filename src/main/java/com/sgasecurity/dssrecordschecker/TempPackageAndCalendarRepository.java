package com.sgasecurity.dssrecordschecker;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TempPackageAndCalendarRepository extends JpaRepository<TempPackageAndCalendar, Integer>
{

    @Query(value = "SELECT * FROM temp_package_and_calendar WHERE current_user_id=?1 ORDER BY id DESC LIMIT 1", nativeQuery = true)
    TempPackageAndCalendar getCurrentUserByCustomerID(String currentUserId);

}




package com.ualr.scheduler.repository;

import com.ualr.scheduler.model.Schedule;
import org.springframework.data.repository.CrudRepository;

public interface ScheduleRepository extends CrudRepository<Schedule,Long> {
    Schedule findByScheduleName(String scheduleName);
}

package com.ualr.scheduler.repository;

import com.ualr.scheduler.model.MeetingTimes;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository()
public interface MeetingtimeRepository extends CrudRepository<MeetingTimes,Long> {
     MeetingTimes findByMeetingId(Long meetingId);
}

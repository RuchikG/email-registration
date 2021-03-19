package com.ualr.scheduler.repository;

import com.ualr.scheduler.model.ReservedTime;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository()
public interface ReservedTimeRepository extends CrudRepository<ReservedTime,Long> {
    ReservedTime findByReservedtimeID(Long ReservedtimeID);
    List<ReservedTime> findByDayContaining(String day);
}

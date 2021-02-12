package com.ualr.scheduler.repository;

import com.ualr.scheduler.model.Courses;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository()
public interface CoursesRepository extends CrudRepository<Courses, Long> {
    Courses findByCourseNumber(Long courseNumber);
}

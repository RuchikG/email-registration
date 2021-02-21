package com.ualr.scheduler.repository;

import com.ualr.scheduler.model.Course;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository()
public interface CoursesRepository extends CrudRepository<Course, Long> {
    Course findByCourseNumber(Long courseNumber);
    List<Course> findAll();
    Course findByCourseid(Long courseId);
}

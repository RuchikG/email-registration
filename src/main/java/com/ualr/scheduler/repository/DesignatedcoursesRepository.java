package com.ualr.scheduler.repository;

import com.ualr.scheduler.model.DesignatedCourses;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository()
public interface DesignatedcoursesRepository extends CrudRepository<DesignatedCourses,Long> {
    DesignatedCourses findByDesignatedCourseid(Long designatedCourseId);
}


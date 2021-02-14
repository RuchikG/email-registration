package com.ualr.scheduler.model;


import org.hibernate.annotations.GeneratorType;

import javax.persistence.*;

@Entity
public class DesignatedCourses {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "designatedCourse_id")
    private Long designatedCourseid;

    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private Registration registration;

    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "course_number")
    private Course course;

    public DesignatedCourses(DesignatedCourses designatedCourses){
        this.designatedCourseid = designatedCourses.getDesignatedCourseid();
        this.course = designatedCourses.getCourse();
        this.registration = designatedCourses.getRegistration();
    }

    public DesignatedCourses(){

    }

    public Long getDesignatedCourseid() {
        return designatedCourseid;
    }

    public void setDesignatedCourseid(Long id) {
        this.designatedCourseid = id;
    }

    public Registration getRegistration() {
        return registration;
    }

    public void setRegistration(Registration registration) {
        this.registration = registration;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}

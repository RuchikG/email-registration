package com.ualr.scheduler.model;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "course_id")
    private Long courseid;

    @Column(name = "course_number")
    private Long courseNumber;

    @Column(name = "dept_id")
    private Long deptId;

    @Column(name = "course_title")
    private String courseTitle;

    @OneToMany(mappedBy = "course", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Section> sections;

    @ManyToMany(mappedBy = "designatedCourses",fetch = FetchType.EAGER)
    private Set<Registration> registrations;

    public Course() {
    }

    public Long getCourseid() {
        return courseid;
    }

    public void setCourseid(Long courseid) {
        this.courseid = courseid;
    }

    public Long getCourseNumber() {
        return courseNumber;
    }

    public void setCourseNumber(Long courseNumber) {
        this.courseNumber = courseNumber;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public Set<Section> getSections() {
        return sections;
    }

    public void setSections(Set<Section> sections) {
        this.sections = sections;
    }

    public Set<Registration> getRegistrations() {
        return registrations;
    }

    public void setRegistrations(Set<Registration> registrations) {
        this.registrations = registrations;
    }
}

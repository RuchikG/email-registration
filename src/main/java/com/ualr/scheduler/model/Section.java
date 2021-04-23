package com.ualr.scheduler.model;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Section implements Comparable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "section_id")
    private Long sectionid;

    @Column(name = "section_number")
    private String sectionNumber;

    @ManyToOne(fetch = FetchType.EAGER,optional = false)
    @JoinColumn(name = "course_number", nullable = false)
    private Course course;

    @Column(name = "instructor")
    private String instructor;

    @OneToMany(mappedBy = "section",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private Set<MeetingTimes> meetingTimes;

    public Section() {
    }

    public Section(String sectionNumber, Course course, String instructor) {
        this.sectionNumber = sectionNumber;
        this.course = course;
        this.instructor = instructor;
    }

    public Long getSectionid() {
        return sectionid;
    }

    public void setSectionid(Long sectionid) {
        this.sectionid = sectionid;
    }

    public String getSectionNumber() {
        return sectionNumber;
    }

    public void setSectionNumber(String sectionNumber) {
        this.sectionNumber = sectionNumber;
    }

    public Course getCourses() {
        return course;
    }

    public void setCourses(Course courses) {
        this.course = courses;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public Set<MeetingTimes> getMeetingTime() {
        return meetingTimes;
    }

    public void setMeetingTime(Set<MeetingTimes> meetingTimes) {
        this.meetingTimes = meetingTimes;
    }

    @Override
    public int compareTo(Object o) {
        int compareNum = Integer.parseInt(((Section) o).getSectionNumber());
        return Integer.parseInt(this.sectionNumber) - compareNum;
    }
}

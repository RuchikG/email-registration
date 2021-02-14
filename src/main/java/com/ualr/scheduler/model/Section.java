package com.ualr.scheduler.model;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "section_id")
    private Long sectionid;

    @Column(name = "section_number")
    private Long sectionNumber;

    @ManyToOne(fetch = FetchType.EAGER,optional = false)
    @JoinColumn(name = "course_number", nullable = false)
    private Course courses;

    @Column(name = "instructor")
    private String instructor;

    @OneToMany(mappedBy = "meetingTime", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<MeetingTimes> meetingTimes;

    public Section() {
    }

    public Section(Section sections){
        this.sectionid = sections.getSectionid();
        this.sectionNumber = sections.getSectionNumber();
        this.courses = sections.getCourses();
        this.instructor = sections.getInstructor();
        this.meetingTimes = sections.getMeetingTimes();
    }

    public Long getSectionid() {
        return sectionid;
    }

    public void setSectionid(Long sectionid) {
        this.sectionid = sectionid;
    }

    public Long getSectionNumber() {
        return sectionNumber;
    }

    public void setSectionNumber(Long sectionNumber) {
        this.sectionNumber = sectionNumber;
    }

    public Course getCourses() {
        return courses;
    }

    public void setCourses(Course courses) {
        this.courses = courses;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public Set<MeetingTimes> getMeetingTimes() {
        return meetingTimes;
    }

    public void setMeetingTimes(Set<MeetingTimes> meetingTimes) {
        this.meetingTimes = meetingTimes;
    }
}

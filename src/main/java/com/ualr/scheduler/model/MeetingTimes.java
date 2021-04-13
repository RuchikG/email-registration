package com.ualr.scheduler.model;
import javax.persistence.*;
import java.util.Set;

@Entity
public class MeetingTimes {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "meeting_id")
    private Long meetingId;

    private String day;

    private String startTime;

    private String endTime;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "section_id",nullable = false)
    private Section section;

    public MeetingTimes() {
    }

    public MeetingTimes(String day, String startTime, String endTime, Section section) {
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.section = section;
    }

    public Long getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(Long meetingId) {
        this.meetingId = meetingId;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Section getSections() {
        return section;
    }

    public void setSections(Section section) {
        this.section = section;
    }
}

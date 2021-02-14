package com.ualr.scheduler.model;
import javax.persistence.*;

@Entity
public class MeetingTimes {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "meeting_id")
    private Long meetingId;

    private String day;

    private String startTime;

    private String endTime;

    @ManyToOne(fetch = FetchType.EAGER,optional = false)
    @JoinColumn(name = "section_number", nullable = false)
    private Section sections;

    public MeetingTimes() {
    }

    public MeetingTimes(MeetingTimes meetingTimes){
        this.meetingId = meetingTimes.getMeetingId();
        this.day = meetingTimes.getDay();
        this.startTime = meetingTimes.getStartTime();
        this.endTime = meetingTimes.getEndTime();
        this.sections = meetingTimes.getSections();
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
        return sections;
    }

    public void setSections(Section sections) {
        this.sections = sections;
    }
}

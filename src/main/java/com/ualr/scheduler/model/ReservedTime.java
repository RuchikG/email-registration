package com.ualr.scheduler.model;

import javax.persistence.*;

@Entity
public class ReservedTime {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "reserved_timeid")
    private Long reservedtimeID;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id",nullable = false)
    private Registration registration;

    @Column(name = "day")
    private String day;

    @Column(name = "start_time")
    private String startTime;

    @Column(name = "end_time")
    private String endTime;

    private String description;

    public ReservedTime() {
    }


    public Long getReserved_timeID() {
        return reservedtimeID;
    }

    public void setReserved_timeID(Long reserved_timeID) {
        this.reservedtimeID = reserved_timeID;
    }

    public Registration getRegistration() {
        return registration;
    }

    public void setRegistration(Registration registration) {
        this.registration = registration;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

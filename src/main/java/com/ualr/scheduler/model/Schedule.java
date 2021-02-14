package com.ualr.scheduler.model;

import javax.persistence.*;

@Entity
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "scheduleId")
    private Long id;

    @Column(name = "scheduleName")
    private String scheduleName;

    @ManyToOne(fetch = FetchType.EAGER,optional = false)
    @JoinColumn(name = "user_id",nullable = false)
    private Registration registration;

    private String sections;

    public Schedule() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Registration getRegistration() {
        return registration;
    }

    public void setRegistration(Registration registration) {
        this.registration = registration;
    }

    public String getSections() {
        return sections;
    }

    public void setSections(String sections) {
        this.sections = sections;
    }

    public String getScheduleName() {
        return scheduleName;
    }

    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }
}

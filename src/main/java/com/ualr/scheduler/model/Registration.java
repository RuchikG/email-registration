package com.ualr.scheduler.model;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
public class Registration {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private long userid;

    private String username;

    private String emailId;

    private String password;

    private boolean isEnabled;

    private String roles;

    private String confirmationToken;

    private Date confirmationDate;

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.PERSIST)
    @JoinTable(name = "designatedCourses",
        joinColumns = {
            @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false,updatable = false)},
        inverseJoinColumns = {
            @JoinColumn(name = "course_id",referencedColumnName = "course_id",nullable = false,updatable = false)})
    private Set<Course> designatedCourses;

    @OneToMany(mappedBy = "registration",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private Set<ReservedTime> reservedTimes;

    public Registration(Registration registration) {
        this.userid = registration.getUserid();
        this.username = registration.getUsername();
        this.emailId = registration.getEmailId();
        this.password = registration.getPassword();
        this.isEnabled = registration.isEnabled();
        this.roles = registration.getRoles();
        this.confirmationToken = registration.getConfirmationToken();
        this.confirmationDate = registration.getConfirmationDate();
        this.designatedCourses = registration.getDesignatedCourses();
        this.reservedTimes = registration.getReservedTimes();
    }

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getConfirmationToken() {
        return confirmationToken;
    }

    public void setConfirmationToken(String confirmationToken) {
        this.confirmationToken = confirmationToken;
    }

    public Date getConfirmationDate() {
        return confirmationDate;
    }

    public void setConfirmationDate(Date confirmationDate) {
        this.confirmationDate = confirmationDate;
    }

    public Set<Course> getDesignatedCourses() {
        return designatedCourses;
    }

    public void setDesignatedCourses(Set<Course> designatedCourses) {
        this.designatedCourses = designatedCourses;
    }

    public Set<ReservedTime> getReservedTimes() {
        return reservedTimes;
    }

    public void setReservedTimes(Set<ReservedTime> reservedTimes) {
        this.reservedTimes = reservedTimes;
    }

    public Registration(){

    }
}

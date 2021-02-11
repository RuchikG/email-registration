package com.ualr.scheduler.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    public Registration(Registration registration) {
        this.userid = registration.getUserid();
        this.username = registration.getUsername();
        this.emailId = registration.getEmailId();
        this.password = registration.getPassword();
        this.isEnabled = registration.isEnabled();
        this.roles = registration.getRoles();
        this.confirmationToken = registration.getConfirmationToken();
        this.confirmationDate = registration.getConfirmationDate();
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

    public Registration(){

    }
}

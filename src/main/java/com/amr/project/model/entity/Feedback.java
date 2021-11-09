package com.amr.project.model.entity;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String reason;

    private String full_text;
    private LocalDateTime dateTime;
    private String username;



    public Feedback() {
    }

    public Feedback(Long id, String reason, String full_text, LocalDateTime dateTime, String username) {
        this.id = id;
        this.reason = reason;
        this.full_text = full_text;
        this.dateTime = dateTime;
        this.username = username;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getFull_text() {
        return full_text;
    }

    public void setFull_text(String full_text) {
        this.full_text = full_text;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = (LocalDateTime) LocalDateTime.now();;
    }


    public String getUsername() {
        return  username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "id=" + id +
                ", reason='" + reason + '\'' +
                ", full_text='" + full_text + '\'' +
                ", dateTime=" + dateTime +
                ", username='" + username + '\'' +
                '}';
    }
}

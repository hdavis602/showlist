package edu.csc435.showlist;

import jakarta.persistence.*;
import java.util.*;

@Entity
public class Show {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private UUID showId;
    @ManyToOne
    private User user;
    private String title;
    private Status status;
    private Integer rating;

    protected Show() {}

    public Show(User user, String title, Status status) {
        this.user = user;
        this.title = title;
        this.status = status;
    }

    public void setUser (User user) {this.user = user;}
    public void setStatus(String status) {this.status = Status.fromString(status);}
    public void setRating(Integer rating) {this.rating = rating;}

    public UUID getShowId() {return showId;}
    public User getUser() {return user;}
    public String getTitle() {return title;}
    public Status getStatus() {return status;}
    public Integer getRating() {return rating;}
}
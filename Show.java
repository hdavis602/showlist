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

    //public void setUser(User user) {this.user = user;} ?
    public void setTitle(String title) {this.title = title;}
    public void setStatus(String status) {this.status = Status.fromString(status);}
    public void setRating(Integer rating) {this.rating = rating;}

    public UUID showId () {return showId;}
    public User user() {return user;}
    public String title() {return title;}
    public Status status() {return status;}
    public Integer rating() {return rating;}
}

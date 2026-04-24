package edu.csc435.showlist;

import jakarta.persistence.*;
import java.util.*;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private UUID uid;
    private String username;
    private String passwordHash;
    @OneToMany
    private List<Show> shows;

    protected User() {}

    public User(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
    }

    public void setShows (List<Show> shows) {this.shows = shows;}

    public UUID uid() {return uid;}
    public String username() {return username;}
    public String password_hash() {return passwordHash;}
    public List<Show> shows() {return shows;}
}
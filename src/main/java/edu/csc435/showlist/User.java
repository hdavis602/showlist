package edu.csc435.showlist;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private UUID uid;
    private String username;
    @JsonIgnore
    private String passwordHash;
    @OneToMany(mappedBy = "user")
    private List<Show> shows;

    protected User() {}

    public User(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
    }

    public void setShows (List<Show> shows) {this.shows = shows;}

    public UUID getUid() {return uid;}
    public String getUsername() {return username;}
    public String getPasswordHash() {return passwordHash;}
    public List<Show> getShows() {return shows;}
}
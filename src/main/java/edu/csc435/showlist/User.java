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

    public void setUsername (String username) {
        this.username = username;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public UUID uid() {return uid;}
    public String username() {return username;}
    public String password_hash() {return passwordHash;}
}

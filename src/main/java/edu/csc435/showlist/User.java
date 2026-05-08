package edu.csc435.showlist;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = {"provider", "providerId"}))
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private UUID uid;

    @Column(nullable = false)
    private String provider;
    @Column(nullable = false)
    private String providerId;

    private String email;

    @OneToMany(mappedBy = "user")
    private List<Show> shows;

    protected User() {}

    public User(String provider, String providerId, String email) {
        this.provider = provider;
        this.providerId = providerId;
    }

    public void setShows (List<Show> shows) {this.shows = shows;}

    public UUID getUid() {return uid;}
    public String getProvider() {return provider;}
    public String getProviderId() {return providerId;}
    public String getEmail() {return email;}
    public List<Show> getShows() {return shows;}
}
package edu.csc435.showlist;

import jakarta.persistence.*;
import java.util.*;

@Entity
public class Show {
    @ManyToOne
    private User user;
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private UUID showid;

}

package edu.csc435.showlist.db;

import edu.csc435.showlist.User;

import org.springframework.data.repository.*;
import java.util.*;

public interface UserRepository extends PagingAndSortingRepository<User, UUID>, CrudRepository<User, UUID> {
    Optional<User> findByUsername (String username);
}
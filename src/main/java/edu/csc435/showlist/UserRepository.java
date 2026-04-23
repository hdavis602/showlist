package edu.csc435.showlist;

import java.util.*;
import org.springframework.data.repository.*;
//import org.springframework.data.rest.core.annotation.RepositoryRestResource;

public interface UserRepository extends CrudRepository<User, UUID> {
    User findByUsername (String username);
    User findByUid(UUID uid);
}
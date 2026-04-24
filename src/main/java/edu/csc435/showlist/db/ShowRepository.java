package edu.csc435.showlist.db;

import edu.csc435.showlist.*;

import org.springframework.data.repository.*;
import java.util.*;


public interface ShowRepository extends PagingAndSortingRepository<Show,UUID>, CrudRepository<Show,UUID> {
}

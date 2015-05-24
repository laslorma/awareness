package com.catwizard.awareness.repository;

import com.catwizard.awareness.domain.Habit;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Habit entity.
 */
public interface HabitRepository extends JpaRepository<Habit,Long> {

    @Query("select habit from Habit habit where habit.user.login = ?#{principal.username}")
    List<Habit> findAllForCurrentUser();

}

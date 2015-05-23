package com.catwizard.awareness.repository;

import com.catwizard.awareness.domain.Habbit;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Habbit entity.
 */
public interface HabbitRepository extends JpaRepository<Habbit,Long> {

    @Query("select habbit from Habbit habbit where habbit.user.login = ?#{principal.username}")
    List<Habbit> findAllForCurrentUser();

}

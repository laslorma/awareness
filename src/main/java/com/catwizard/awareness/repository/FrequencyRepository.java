package com.catwizard.awareness.repository;

import com.catwizard.awareness.domain.Frequency;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Frequency entity.
 */
public interface FrequencyRepository extends JpaRepository<Frequency,Long> {

}

package com.catwizard.awareness.repository;

import com.catwizard.awareness.domain.Measurement;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Measurement entity.
 */
public interface MeasurementRepository extends JpaRepository<Measurement,Long> {

}

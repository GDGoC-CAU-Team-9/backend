package com.gdg_team9.SafePlate.allergy.repository;

import com.gdg_team9.SafePlate.allergy.domain.Allergy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AllergyRepository extends JpaRepository<Allergy, Long> {
}

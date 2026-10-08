package bt.com.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import bt.com.entity.PatientEntity;

public interface PatientRepository extends JpaRepository<PatientEntity, Long> {
  
	List<PatientEntity> findByActiveTrue();
}


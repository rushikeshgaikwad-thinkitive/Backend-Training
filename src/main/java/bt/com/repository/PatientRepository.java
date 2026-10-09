package bt.com.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import bt.com.entity.PatientEntity;

public interface PatientRepository extends JpaRepository<PatientEntity, Long> {
  
	List<PatientEntity> findByActiveTrue();
	
	Optional<PatientEntity> findByUser_Id(Long userId);

	Optional<PatientEntity> findByUser_EmailIgnoreCase(String email);
	
	 boolean existsByEmailIgnoreCase(String email);
}


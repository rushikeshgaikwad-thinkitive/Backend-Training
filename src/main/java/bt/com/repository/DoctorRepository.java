package bt.com.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import bt.com.entity.DoctorEntity;

public interface DoctorRepository extends JpaRepository<DoctorEntity, Long> {

	    List<DoctorEntity> findByActiveTrue();
	    
	    Optional<DoctorEntity> findByUser_Id(Long userId);

	    Optional<DoctorEntity> findByUser_EmailIgnoreCase(String email);
	    
	    boolean existsByEmailIgnoreCase(String email);
}

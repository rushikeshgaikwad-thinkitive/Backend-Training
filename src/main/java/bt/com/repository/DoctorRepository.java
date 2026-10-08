package bt.com.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;

import bt.com.entity.DoctorEntity;

public interface DoctorRepository extends JpaRepository<DoctorEntity, Long> {

	    List<DoctorEntity> findByActiveTrue();
}

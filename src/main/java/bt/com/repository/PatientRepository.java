package bt.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import bt.com.entity.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {

}


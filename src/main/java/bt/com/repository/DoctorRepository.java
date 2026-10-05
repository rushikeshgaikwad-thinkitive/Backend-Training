package bt.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import bt.com.entity.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

}

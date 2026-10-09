package bt.com.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import bt.com.entity.AppointmentEntity;
import bt.com.enums.AppointmentStatus;

@Repository
public interface AppointmentRepository
        extends JpaRepository<AppointmentEntity, Long> {

    List<AppointmentEntity> findByPatient_Id(
            Long patientId
    );

    List<AppointmentEntity> findByDoctor_Id(
            Long doctorId
    );

    List<AppointmentEntity> findByAppointmentDate(
            LocalDate appointmentDate
    );

    List<AppointmentEntity> findByStatus(
            AppointmentStatus status
    );
   

    Optional<AppointmentEntity> findByIdAndPatient_Id(
            Long appointmentId, Long patientId);

    Optional<AppointmentEntity> findByIdAndDoctor_Id(
            Long appointmentId, Long doctorId);
}


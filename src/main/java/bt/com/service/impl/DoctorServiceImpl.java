package bt.com.service.impl;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bt.com.dto.constants.Messages;
import bt.com.dto.module.Doctor;
import bt.com.dto.projection.DoctorView;
import bt.com.entity.DoctorEntity;
import bt.com.exception.DoctorNotFoundException;
import bt.com.repository.DoctorRepository;
import bt.com.service.DoctorService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorServiceImpl(
            DoctorRepository doctorRepository) {

        this.doctorRepository = doctorRepository;
    }

    @Override
    public DoctorView createDoctor(Doctor doctor) {

        log.info(Messages.CREATING_DOCTOR);

        DoctorEntity doctorEntity =
                mapToEntity(doctor);

        DoctorEntity savedDoctor =
                doctorRepository.save(doctorEntity);

        log.info(
                Messages.DOCTOR_CREATED,
                savedDoctor.getId()
        );

        return mapToView(savedDoctor);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorView> getAllDoctors() {

        log.debug(Messages.FETCHING_ALL_DOCTORS);

        return doctorRepository
                .findAll()
                .stream()
                .sorted(
                        Comparator.comparing(
                                DoctorEntity::getName,
                                Comparator.nullsLast(
                                        Comparator.naturalOrder()
                                )
                        )
                )
                .map(this::mapToView)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public DoctorView getDoctorById(Long id) {

        log.debug(
                Messages.FETCHING_DOCTOR,
                id
        );

        return mapToView(
                getDoctor(id)
        );
    }

    @Override
    public DoctorView updateDoctor(
            Long id,
            Doctor doctor) {

        log.info(
                Messages.UPDATING_DOCTOR,
                id
        );

        DoctorEntity existingDoctor =
                getDoctor(id);

        existingDoctor.setName(
                doctor.getName()
        );

        existingDoctor.setSpecialization(
                doctor.getSpecialization()
        );

        existingDoctor.setPhone(
                doctor.getPhone()
        );

        existingDoctor.setEmail(
                doctor.getEmail()
        );

        existingDoctor.setActive(
                Boolean.TRUE.equals(
                        doctor.getActive()
                )
        );

        DoctorEntity updatedDoctor =
                doctorRepository.save(existingDoctor);

        log.info(
                Messages.DOCTOR_UPDATED,
                updatedDoctor.getId()
        );

        return mapToView(updatedDoctor);
    }

    @Override
    public void deleteDoctor(Long id) {

        log.info(
                Messages.DELETING_DOCTOR,
                id
        );

        DoctorEntity doctor =
                getDoctor(id);

        doctorRepository.delete(doctor);

        log.info(
                Messages.DOCTOR_DELETED,
                id
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorView> getActiveDoctors() {

        log.debug(Messages.FETCHING_ACTIVE_DOCTORS);

        return doctorRepository
                .findByActiveTrue()
                .stream()
                .sorted(
                        Comparator.comparing(
                                DoctorEntity::getName,
                                Comparator.nullsLast(
                                        Comparator.naturalOrder()
                                )
                        )
                )
                .map(this::mapToView)
                .toList();
    }

    private DoctorEntity getDoctor(Long id) {

        return doctorRepository
                .findById(id)
                .orElseThrow(
                        () -> new DoctorNotFoundException(
                                String.format(
                                        Messages.DOCTOR_NOT_FOUND,
                                        id
                                )
                        )
                );
    }

    private DoctorEntity mapToEntity(
            Doctor doctor) {

        return DoctorEntity.builder()
                .name(doctor.getName())
                .specialization(
                        doctor.getSpecialization()
                )
                .phone(doctor.getPhone())
                .email(doctor.getEmail())
                .active(
                        Boolean.TRUE.equals(
                                doctor.getActive()
                        )
                )
                .build();
    }

    private DoctorView mapToView(
            DoctorEntity doctor) {

        return DoctorView.builder()
                .id(doctor.getId())
                .name(doctor.getName())
                .specialization(
                        doctor.getSpecialization()
                )
                .phone(doctor.getPhone())
                .email(doctor.getEmail())
                .active(doctor.isActive())
                .createdAt(
                        doctor.getCreatedAt()
                )
                .updatedAt(
                        doctor.getUpdatedAt()
                )
                .build();
    }
}
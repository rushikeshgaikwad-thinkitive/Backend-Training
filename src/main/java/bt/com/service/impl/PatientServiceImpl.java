
package bt.com.service.impl;

import java.util.Comparator;
import java.util.List;


import org.springframework.stereotype.Service;

import bt.com.dto.constants.Messages;
import bt.com.dto.module.Patient;
import bt.com.dto.projection.PatientView;

import bt.com.entity.PatientEntity;
import bt.com.exception.PatientNotFoundException;

import bt.com.repository.PatientRepository;
import bt.com.service.PatientService;

import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    public PatientServiceImpl(
            PatientRepository patientRepository) {
        this.patientRepository = patientRepository;     
    }
    @Override
    public PatientView createPatient(Patient patient) {

        log.info(Messages.CREATING_PATIENT);

        PatientEntity patientEntity =
                mapToEntity(patient);

        PatientEntity savedPatient =
                patientRepository.save(patientEntity);

        log.info(
                Messages.PATIENT_CREATED,
                savedPatient.getId()
        );

        return mapToView(savedPatient);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PatientView> getAllPatients() {

        log.debug(Messages.FETCHING_ALL_PATIENTS);

        return patientRepository.findAll()
                .stream()
                .sorted(
                        Comparator.comparing(
                                PatientEntity::getName,
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
    public PatientView getPatientById(Long id) {

        log.debug(
                Messages.FETCHING_PATIENT,
                id
        );

        PatientEntity patient =
                patientRepository.findById(id)
                        .orElseThrow(
                                () -> new PatientNotFoundException(
                                        Messages.PATIENT_NOT_FOUND + id
                                )
                        );

        return mapToView(patient);
    }

    @Override
    public PatientView updatePatient(
            Long id,
            Patient patient) {

        log.info(
                Messages.UPDATING_PATIENT,
                id
        );

        PatientEntity existingPatient =
                patientRepository.findById(id)
                        .orElseThrow(
                                () -> new PatientNotFoundException(
                                        Messages.PATIENT_NOT_FOUND + id
                                )
                        );

        existingPatient.setName(
                patient.getName()
        );

        existingPatient.setAge(
                patient.getAge()
        );

        existingPatient.setGender(
                patient.getGender()
        );

        existingPatient.setPhone(
                patient.getPhone()
        );

        existingPatient.setEmail(
                patient.getEmail()
        );

        existingPatient.setActive(
                Boolean.TRUE.equals(
                        patient.getActive()
                )
        );

        PatientEntity updatedPatient =
                patientRepository.save(existingPatient);

        log.info(
                Messages.PATIENT_UPDATED,
                updatedPatient.getId()
        );

        return mapToView(updatedPatient);
    }

    @Override
    public void deletePatient(Long id) {

        log.info(
                Messages.DELETING_PATIENT,
                id
        );

        PatientEntity patient =
                patientRepository.findById(id)
                        .orElseThrow(
                                () -> new PatientNotFoundException(
                                        Messages.PATIENT_NOT_FOUND + id
                                )
                        );

        patientRepository.delete(patient);

        log.info(
                Messages.PATIENT_DELETED,
                id
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<PatientView> getActivePatients() {

        log.debug(
                Messages.FETCHING_ACTIVE_PATIENTS
        );

        return patientRepository.findByActiveTrue()
                .stream()
                .sorted(
                        Comparator.comparing(
                                PatientEntity::getName,
                                Comparator.nullsLast(
                                        Comparator.naturalOrder()
                                )
                        )
                )
                .map(this::mapToView)
                .toList();
    }

    private PatientEntity mapToEntity(
            Patient patient) {

        return PatientEntity.builder()
                .name(patient.getName())
                .age(patient.getAge())
                .gender(patient.getGender())
                .phone(patient.getPhone())
                .email(patient.getEmail())
                .active(
                        Boolean.TRUE.equals(
                                patient.getActive()
                        )
                )
                .build();
    }

    private PatientView mapToView(
            PatientEntity patient) {

        return PatientView.builder()
                .id(patient.getId())
                .name(patient.getName())
                .age(patient.getAge())
                .gender(patient.getGender())
                .phone(patient.getPhone())
                .email(patient.getEmail())
                .active(patient.isActive())
                .createdAt(patient.getCreatedAt())
                .updatedAt(patient.getUpdatedAt())
                .build();
    }
}
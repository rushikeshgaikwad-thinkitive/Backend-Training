package bt.com.service.impl;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bt.com.dto.constants.Messages;
import bt.com.dto.module.Appointment;
import bt.com.dto.module.AppointmentCancellation;
import bt.com.dto.projection.AppointmentView;
import bt.com.entity.AppointmentEntity;
import bt.com.entity.DoctorEntity;
import bt.com.entity.PatientEntity;
import bt.com.enums.AppointmentStatus;
import bt.com.enums.CancelledBy;
import bt.com.exception.AppointmentNotFoundException;
import bt.com.exception.DoctorNotFoundException;
import bt.com.exception.PatientNotFoundException;
import bt.com.repository.AppointmentRepository;
import bt.com.repository.DoctorRepository;
import bt.com.repository.PatientRepository;
import bt.com.service.AppointmentService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class AppointmentServiceImpl
        implements AppointmentService {

    private final AppointmentRepository appointmentRepository;

    private final PatientRepository patientRepository;

    private final DoctorRepository doctorRepository;

    public AppointmentServiceImpl(
            AppointmentRepository appointmentRepository,
            PatientRepository patientRepository,
            DoctorRepository doctorRepository) {

        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    @Override
    public AppointmentView createAppointment(
            Appointment appointment) {

        log.info(Messages.CREATING_APPOINTMENT);

        validateAppointmentDateTime(
                appointment.getAppointmentDate(),
                appointment.getAppointmentTime()
        );

        PatientEntity patient =
                getPatient(appointment.getPatientId());

        DoctorEntity doctor =
                getDoctor(appointment.getDoctorId());

        AppointmentEntity appointmentEntity =
                AppointmentEntity.builder()
                        .patient(patient)
                        .doctor(doctor)
                        .appointmentDate(
                                appointment.getAppointmentDate()
                        )
                        .appointmentTime(
                                appointment.getAppointmentTime()
                        )
                        .reason(
                                appointment.getReason()
                        )
                        .notes(
                                appointment.getNotes()
                        )
                        .status(
                                AppointmentStatus.SCHEDULED
                        )
                        .build();

        AppointmentEntity savedAppointment =
                appointmentRepository.save(
                        appointmentEntity
                );

        log.info(
                Messages.APPOINTMENT_CREATED,
                savedAppointment.getId()
        );

        return mapToView(savedAppointment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentView> getAllAppointments() {

        log.debug(Messages.FETCHING_ALL_APPOINTMENTS);

        return appointmentRepository.findAll()
                .stream()
                .sorted(appointmentComparator())
                .map(this::mapToView)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AppointmentView getAppointmentById(
            Long id) {

        log.debug(
                Messages.FETCHING_APPOINTMENT,
                id
        );

        AppointmentEntity appointment =
                getAppointment(id);

        return mapToView(appointment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentView> getAppointmentsByPatient(
            Long patientId) {

        log.debug(
                Messages.FETCHING_PATIENT_APPOINTMENTS,
                patientId
        );

        getPatient(patientId);

        return appointmentRepository
                .findByPatient_Id(patientId)
                .stream()
                .sorted(appointmentComparator())
                .map(this::mapToView)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentView> getAppointmentsByDoctor(
            Long doctorId) {

        log.debug(
                Messages.FETCHING_DOCTOR_APPOINTMENTS,
                doctorId
        );

        getDoctor(doctorId);

        return appointmentRepository
                .findByDoctor_Id(doctorId)
                .stream()
                .sorted(appointmentComparator())
                .map(this::mapToView)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentView> getAppointmentsByDate(
            LocalDate date) {

        log.debug(
                Messages.FETCHING_DATE_APPOINTMENTS,
                date
        );

        return appointmentRepository
                .findByAppointmentDate(date)
                .stream()
                .sorted(
                        Comparator.comparing(
                                AppointmentEntity::getAppointmentTime
                        )
                )
                .map(this::mapToView)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentView> getScheduledAppointments() {

        log.debug(
                Messages.FETCHING_SCHEDULED_APPOINTMENTS
        );

        return appointmentRepository
                .findByStatus(
                        AppointmentStatus.SCHEDULED
                )
                .stream()
                .sorted(appointmentComparator())
                .map(this::mapToView)
                .toList();
    }

    @Override
    public AppointmentView updateAppointment(
            Long id,
            Appointment appointment) {

        log.info(
                Messages.UPDATING_APPOINTMENT,
                id
        );

        AppointmentEntity existingAppointment =
                getAppointment(id);

        if (existingAppointment.getStatus()
                == AppointmentStatus.CANCELLED) {

            throw new IllegalStateException(
                    Messages.CANCELLED_APPOINTMENT_CANNOT_UPDATE
            );
        }

        validateAppointmentDateTime(
                appointment.getAppointmentDate(),
                appointment.getAppointmentTime()
        );

        PatientEntity patient =
                getPatient(appointment.getPatientId());

        DoctorEntity doctor =
                getDoctor(appointment.getDoctorId());

        existingAppointment.setPatient(patient);

        existingAppointment.setDoctor(doctor);

        existingAppointment.setAppointmentDate(
                appointment.getAppointmentDate()
        );

        existingAppointment.setAppointmentTime(
                appointment.getAppointmentTime()
        );

        existingAppointment.setReason(
                appointment.getReason()
        );

        existingAppointment.setNotes(
                appointment.getNotes()
        );

        AppointmentEntity updatedAppointment =
                appointmentRepository.save(
                        existingAppointment
                );

        log.info(
                Messages.APPOINTMENT_UPDATED,
                id
        );

        return mapToView(updatedAppointment);
    }

    @Override
    public void deleteAppointment(Long id) {

        log.info(
                Messages.DELETING_APPOINTMENT,
                id
        );

        AppointmentEntity appointment =
                getAppointment(id);

        appointmentRepository.delete(appointment);

        log.info(
                Messages.APPOINTMENT_DELETED,
                id
        );
    }

    @Override
    public AppointmentView completeAppointment(
            Long appointmentId) {

        log.info(
                Messages.COMPLETING_APPOINTMENT,
                appointmentId
        );

        AppointmentEntity appointment =
                getAppointment(appointmentId);

        if (appointment.getStatus()
                == AppointmentStatus.CANCELLED) {

            throw new IllegalStateException(
                    Messages.CANCELLED_APPOINTMENT_CANNOT_COMPLETE
            );
        }

        if (appointment.getStatus()
                == AppointmentStatus.COMPLETED) {

            throw new IllegalStateException(
                    Messages.APPOINTMENT_ALREADY_COMPLETED
            );
        }

        appointment.setStatus(
                AppointmentStatus.COMPLETED
        );

        AppointmentEntity updatedAppointment =
                appointmentRepository.save(
                        appointment
                );

        log.info(
                Messages.APPOINTMENT_COMPLETED,
                appointmentId
        );

        return mapToView(updatedAppointment);
    }

    @Override
    public AppointmentView cancelAppointmentByPatient(
            Long appointmentId,
            AppointmentCancellation cancellation) {

        return cancelAppointment(
                appointmentId,
                cancellation,
                CancelledBy.PATIENT
        );
    }

    @Override
    public AppointmentView cancelAppointmentByDoctor(
            Long appointmentId,
            AppointmentCancellation cancellation) {

        return cancelAppointment(
                appointmentId,
                cancellation,
                CancelledBy.DOCTOR
        );
    }

    private AppointmentView cancelAppointment(
            Long appointmentId,
            AppointmentCancellation cancellation,
            CancelledBy cancelledBy) {

        log.info(
                Messages.CANCELLING_APPOINTMENT,
                appointmentId
        );

        AppointmentEntity appointment =
                getAppointment(appointmentId);

        validateCanBeCancelled(appointment);

        appointment.setStatus(
                AppointmentStatus.CANCELLED
        );

        appointment.setCancelledBy(
                cancelledBy
        );

        appointment.setCancellationReason(
                cancellation.getCancellationReason()
        );

        appointment.setCancelledAt(
                Instant.now()
        );

        AppointmentEntity cancelledAppointment =
                appointmentRepository.save(
                        appointment
                );

        log.info(
                Messages.APPOINTMENT_CANCELLED,
                appointmentId
        );

        return mapToView(cancelledAppointment);
    }

    private AppointmentEntity getAppointment(
            Long id) {

        return appointmentRepository.findById(id)
                .orElseThrow(
                        () -> new AppointmentNotFoundException(
                                String.format(
                                        Messages.APPOINTMENT_NOT_FOUND,
                                        id
                                )
                        )
                );
    }

    private PatientEntity getPatient(
            Long patientId) {

        return patientRepository.findById(patientId)
                .orElseThrow(
                        () -> new PatientNotFoundException(
                                String.format(
                                        Messages.PATIENT_NOT_FOUND,
                                        patientId
                                )
                        )
                );
    }

    private DoctorEntity getDoctor(
            Long doctorId) {

        return doctorRepository.findById(doctorId)
                .orElseThrow(
                        () -> new DoctorNotFoundException(
                                String.format(
                                        Messages.DOCTOR_NOT_FOUND,
                                        doctorId
                                )
                        )
                );
    }

    private void validateCanBeCancelled(
            AppointmentEntity appointment) {

        if (appointment.getStatus()
                != AppointmentStatus.SCHEDULED) {

            throw new IllegalStateException(
                    Messages.ONLY_SCHEDULED_CAN_CANCEL
            );
        }
    }

    private void validateAppointmentDateTime(
            LocalDate appointmentDate,
            LocalTime appointmentTime) {

        LocalDate today = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        if (appointmentDate.isBefore(today)) {

            throw new IllegalArgumentException(
                    Messages.APPOINTMENT_DATE_PAST
            );
        }

        if (appointmentDate.equals(today)
                && appointmentTime.isBefore(currentTime)) {

            throw new IllegalArgumentException(
                    Messages.APPOINTMENT_TIME_PAST
            );
        }
    }

    private Comparator<AppointmentEntity>
    appointmentComparator() {

        return Comparator
                .comparing(
                        AppointmentEntity::getAppointmentDate
                )
                .thenComparing(
                        AppointmentEntity::getAppointmentTime
                );
    }

    private AppointmentView mapToView(
            AppointmentEntity appointment) {

        return AppointmentView.builder()
                .id(appointment.getId())

                .patientId(
                        appointment.getPatient().getId()
                )

                .patientName(
                        appointment.getPatient().getName()
                )

                .doctorId(
                        appointment.getDoctor().getId()
                )

                .doctorName(
                        appointment.getDoctor().getName()
                )

                .appointmentDate(
                        appointment.getAppointmentDate()
                )

                .appointmentTime(
                        appointment.getAppointmentTime()
                )

                .reason(
                        appointment.getReason()
                )

                .status(
                        appointment.getStatus()
                )

                .notes(
                        appointment.getNotes()
                )

                .cancelledBy(
                        appointment.getCancelledBy()
                )

                .cancellationReason(
                        appointment.getCancellationReason()
                )

                .cancelledAt(
                        appointment.getCancelledAt()
                )

                .createdAt(
                        appointment.getCreatedAt()
                )

                .updatedAt(
                        appointment.getUpdatedAt()
                )

                .build();
    }
}
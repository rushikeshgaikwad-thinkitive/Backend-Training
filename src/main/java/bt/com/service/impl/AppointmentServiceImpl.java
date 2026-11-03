package bt.com.service.impl;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.security.access.prepost.PreAuthorize;
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
import bt.com.enums.Role;
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
public class AppointmentServiceImpl  implements AppointmentService {

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
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasAuthority('APPOINTMENT_CREATE')")
    public AppointmentView createAppointmentForUser(
            Appointment appointment,
            String email,
            String role) {
        Role accountRole = parseRole(role);
        Appointment scopedAppointment = Appointment.builder()
                .id(appointment.getId())
                .patientId(appointment.getPatientId())
                .doctorId(appointment.getDoctorId())
                .appointmentDate(appointment.getAppointmentDate())
                .appointmentTime(appointment.getAppointmentTime())
                .reason(appointment.getReason())
                .notes(appointment.getNotes())
                .build();

        if (accountRole == Role.PATIENT) {
            PatientEntity patient = patientRepository
                    .findByUser_EmailIgnoreCase(email)
                    .orElseThrow(() -> new PatientNotFoundException(
                            "Patient profile not found for this account"));
            if (!patient.isActive()) {
                throw new IllegalStateException(
                        "Inactive patient profiles cannot book appointments");
            }
            scopedAppointment.setPatientId(patient.getId());
        } else if (accountRole == Role.DOCTOR) {
            DoctorEntity doctor = doctorRepository
                    .findByUser_EmailIgnoreCase(email)
                    .orElseThrow(() -> new DoctorNotFoundException(
                            "Doctor profile not found for this account"));
            if (!doctor.isActive()) {
                throw new IllegalStateException(
                        "Inactive doctors cannot schedule appointments");
            }
            scopedAppointment.setDoctorId(doctor.getId());
        }

        DoctorEntity selectedDoctor = getDoctor(scopedAppointment.getDoctorId());
        if (!selectedDoctor.isActive()) {
            throw new IllegalStateException(
                    "Appointments can only be scheduled with active doctors");
        }
        PatientEntity selectedPatient = getPatient(scopedAppointment.getPatientId());
        if (!selectedPatient.isActive()) {
            throw new IllegalStateException(
                    "Appointments can only be scheduled for active patients");
        }
        if (accountRole == Role.DOCTOR
                && !appointmentRepository.existsByDoctor_IdAndPatient_Id(
                        selectedDoctor.getId(), selectedPatient.getId())) {
            throw new PatientNotFoundException(
                    "Patient is not assigned to this doctor");
        }

        return createAppointment(scopedAppointment);
    }

    @Override
    @PreAuthorize("hasAuthority('APPOINTMENT_VIEW_ALL')")
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
    @PreAuthorize("hasAuthority('APPOINTMENT_VIEW_ASSIGNED')")
    @Transactional(readOnly = true)
    public List<AppointmentView> getAppointmentsByDoctorForUser(
            Long doctorId,
            String email) {
        DoctorEntity doctor = doctorRepository
                .findByUser_EmailIgnoreCase(email)
                .orElseThrow(() -> new DoctorNotFoundException(
                        "Doctor profile not found for this account"));
        if (!doctor.getId().equals(doctorId)) {
            throw new DoctorNotFoundException("Doctor profile not found");
        }
        return appointmentRepository.findByDoctor_Id(doctorId)
                .stream()
                .sorted(appointmentComparator())
                .map(this::mapToView)
                .toList();
    }

    @Override
    @PreAuthorize("hasAuthority('APPOINTMENT_VIEW_ALL')")
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
    @PreAuthorize("hasAuthority('APPOINTMENT_VIEW_ALL')")
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
    @PreAuthorize("hasAuthority('APPOINTMENT_VIEW_ALL')")
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
    @PreAuthorize("hasAuthority('APPOINTMENT_VIEW_ALL')")
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
    @PreAuthorize("hasAuthority('APPOINTMENT_VIEW_ALL')")
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
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasAnyAuthority('APPOINTMENT_UPDATE', 'APPOINTMENT_RESCHEDULE')")
    public AppointmentView updateAppointmentForUser(
            Long id,
            Appointment appointment,
            String email,
            String role) {
        Role accountRole = parseRole(role);
        if (accountRole == Role.ADMIN) {
            return updateAppointment(id, appointment);
        }

        AppointmentView current = getMyAppointment(id, email, role);
        Appointment scopedUpdate = Appointment.builder()
                .id(id)
                .patientId(current.getPatientId())
                .doctorId(current.getDoctorId())
                .appointmentDate(appointment.getAppointmentDate())
                .appointmentTime(appointment.getAppointmentTime())
                .reason(appointment.getReason())
                .notes(accountRole == Role.PATIENT
                        ? current.getNotes()
                        : appointment.getNotes())
                .build();
        return updateAppointment(id, scopedUpdate);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
    public AppointmentView cancelAppointmentByDoctor(
            Long appointmentId,
            AppointmentCancellation cancellation) {

        return cancelAppointment(
                appointmentId,
                cancellation,
                CancelledBy.DOCTOR
        );
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public AppointmentView cancelAppointmentByAdmin(
            Long appointmentId,
            AppointmentCancellation cancellation) {
        return cancelAppointment(
                appointmentId,
                cancellation,
                CancelledBy.ADMIN);
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
    @Override
    @PreAuthorize("hasAuthority('APPOINTMENT_VIEW_OWN')")
    @Transactional(readOnly = true)
    public List<AppointmentView> getMyPatientAppointments(String email) {

        PatientEntity patient = patientRepository
                .findByUser_EmailIgnoreCase(email)
                .orElseThrow(() -> new PatientNotFoundException(
                        "Patient profile not found for: " + email));

        return appointmentRepository.findByPatient_Id(patient.getId())
                .stream()
                .sorted(appointmentComparator())
                .map(this::mapToView)
                .toList();
    }

    @Override
    @PreAuthorize("hasAuthority('APPOINTMENT_VIEW_ASSIGNED')")
    @Transactional(readOnly = true)
    public List<AppointmentView> getMyDoctorAppointments(String email) {

        DoctorEntity doctor = doctorRepository
                .findByUser_EmailIgnoreCase(email)
                .orElseThrow(() -> new DoctorNotFoundException(
                        "Doctor profile not found for: " + email));

        return appointmentRepository.findByDoctor_Id(doctor.getId())
                .stream()
                .sorted(appointmentComparator())
                .map(this::mapToView)
                .toList();
    }

    @Override
    @PreAuthorize("hasAuthority('APPOINTMENT_VIEW_ASSIGNED')")
    @Transactional(readOnly = true)
    public List<AppointmentView> getAppointmentsByPatientForDoctor(
            Long patientId,
            String email) {
        DoctorEntity doctor = doctorRepository
                .findByUser_EmailIgnoreCase(email)
                .orElseThrow(() -> new DoctorNotFoundException(
                        "Doctor profile not found for this account"));
        return appointmentRepository
                .findByDoctor_IdAndPatient_Id(doctor.getId(), patientId)
                .stream()
                .sorted(appointmentComparator())
                .map(this::mapToView)
                .toList();
    }

    @Override
    @PreAuthorize("hasAnyAuthority('APPOINTMENT_VIEW_OWN', 'APPOINTMENT_VIEW_ASSIGNED')")
    @Transactional(readOnly = true)
    public AppointmentView getMyAppointment(
            Long appointmentId,
            String email,
            String role) {

        AppointmentEntity appointment;

        if ("PATIENT".equalsIgnoreCase(role)
                || "ROLE_PATIENT".equalsIgnoreCase(role)) {

            PatientEntity patient = patientRepository
                    .findByUser_EmailIgnoreCase(email)
                    .orElseThrow(() -> new PatientNotFoundException(
                            "Patient profile not found"));

            appointment = appointmentRepository
                    .findByIdAndPatient_Id(
                            appointmentId, patient.getId())
                    .orElseThrow(() -> new AppointmentNotFoundException(
                            "Appointment not found"));

        } else if ("DOCTOR".equalsIgnoreCase(role)
                || "ROLE_DOCTOR".equalsIgnoreCase(role)) {

            DoctorEntity doctor = doctorRepository
                    .findByUser_EmailIgnoreCase(email)
                    .orElseThrow(() -> new DoctorNotFoundException(
                            "Doctor profile not found"));

            appointment = appointmentRepository
                    .findByIdAndDoctor_Id(
                            appointmentId, doctor.getId())
                    .orElseThrow(() -> new AppointmentNotFoundException(
                            "Appointment not found"));

        } else {
            throw new IllegalArgumentException("Invalid user role");
        }

        return mapToView(appointment);
    }

    @Override
    @PreAuthorize("hasAuthority('APPOINTMENT_CANCEL')")
    public AppointmentView cancelMyPatientAppointment(
            Long appointmentId,
            AppointmentCancellation cancellation,
            String email) {

        PatientEntity patient = patientRepository
                .findByUser_EmailIgnoreCase(email)
                .orElseThrow(() -> new PatientNotFoundException(
                        "Patient profile not found"));

        AppointmentEntity appointment = appointmentRepository
                .findByIdAndPatient_Id(appointmentId, patient.getId())
                .orElseThrow(() -> new AppointmentNotFoundException(
                        "Appointment not found"));

        validateCanBeCancelled(appointment);

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointment.setCancelledBy(CancelledBy.PATIENT);
        appointment.setCancellationReason(
                cancellation.getCancellationReason());
        appointment.setCancelledAt(Instant.now());

        return mapToView(appointmentRepository.save(appointment));
    }
    @Override
    @PreAuthorize("hasAuthority('APPOINTMENT_CANCEL')")
    public AppointmentView cancelMyDoctorAppointment(
            Long appointmentId,
            AppointmentCancellation cancellation,
            String email) {

        DoctorEntity doctor = doctorRepository
                .findByUser_EmailIgnoreCase(email)
                .orElseThrow(() -> new DoctorNotFoundException(
                        "Doctor profile not found"));

        AppointmentEntity appointment = appointmentRepository
                .findByIdAndDoctor_Id(appointmentId, doctor.getId())
                .orElseThrow(() -> new AppointmentNotFoundException(
                        "Appointment not found"));

        validateCanBeCancelled(appointment);

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointment.setCancelledBy(CancelledBy.DOCTOR);
        appointment.setCancellationReason(
                cancellation.getCancellationReason());
        appointment.setCancelledAt(Instant.now());

        return mapToView(appointmentRepository.save(appointment));
    }
    @Override
    @PreAuthorize("hasAuthority('APPOINTMENT_UPDATE')")
    public AppointmentView completeMyDoctorAppointment(
            Long appointmentId,
            String email) {

        DoctorEntity doctor = doctorRepository
                .findByUser_EmailIgnoreCase(email)
                .orElseThrow(() -> new DoctorNotFoundException(
                        "Doctor profile not found"));

        AppointmentEntity appointment = appointmentRepository
                .findByIdAndDoctor_Id(appointmentId, doctor.getId())
                .orElseThrow(() -> new AppointmentNotFoundException(
                        "Appointment not found"));

        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new IllegalStateException(
                    Messages.CANCELLED_APPOINTMENT_CANNOT_COMPLETE);
        }

        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new IllegalStateException(
                    Messages.APPOINTMENT_ALREADY_COMPLETED);
        }

        appointment.setStatus(AppointmentStatus.COMPLETED);

        return mapToView(appointmentRepository.save(appointment));
    }

    private Role parseRole(String role) {
        try {
            return Role.valueOf(role);
        } catch (IllegalArgumentException | NullPointerException exception) {
            throw new IllegalArgumentException("Invalid user role");
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
package bt.com.service.impl;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.springframework.stereotype.Service;

import bt.com.dto.request.AppointmentRequest;
import bt.com.dto.response.AppointmentResponse;
import bt.com.entity.Appointment;
import bt.com.entity.Doctor;
import bt.com.entity.Patient;
import bt.com.enums.AppointmentStatus;
import bt.com.enums.CancelledBy;
import bt.com.exception.AppointmentNotFoundException;
import bt.com.exception.DoctorNotFoundException;
import bt.com.exception.PatientNotFoundException;
import bt.com.factory.AppointmentFactory;
import bt.com.mapper.AppointmentMapper;
import bt.com.repository.AppointmentRepository;
import bt.com.repository.DoctorRepository;
import bt.com.repository.PatientRepository;
import bt.com.service.AppointmentService;
import bt.com.validator.AppointmentValidator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    private final AppointmentMapper appointmentMapper;
    private final AppointmentFactory appointmentFactory;
    private final AppointmentValidator appointmentValidator;


    @Override
    public AppointmentResponse createAppointment(AppointmentRequest request) {

        appointmentValidator.validate(request);

        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(patientNotFoundException(request.getPatientId()));

       
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(doctorNotFoundException(request.getDoctorId()));

      

        Appointment appointment =
                appointmentFactory.createAppointment(
                        request,
                        patient,
                        doctor
                );

        Appointment savedAppointment =
                appointmentRepository.save(appointment);

  
        return appointmentMapper.toResponse(savedAppointment);
    }



    @Override
    public List<AppointmentResponse> getAllAppointments() {

        Function<Appointment, AppointmentResponse> mapper =
                appointmentMapper::toResponse;

        Comparator<Appointment> sortByDateTime =
                Comparator
                        .comparing(Appointment::getAppointmentDate)
                        .thenComparing(Appointment::getAppointmentTime);

        return appointmentRepository.findAll()
                .stream()
                .sorted(sortByDateTime)
                .map(mapper)
                .toList();
    }


    @Override
    public AppointmentResponse getAppointmentById(Long appointmentId) {

        Supplier<AppointmentNotFoundException> exceptionSupplier =
                () -> new AppointmentNotFoundException(
                        "Appointment not found with id: " + appointmentId
                );

        Appointment appointment =
                appointmentRepository.findById(appointmentId)
                        .orElseThrow(exceptionSupplier);

        return appointmentMapper.toResponse(appointment);
    }



    @Override
    public List<AppointmentResponse> getAppointmentsByPatient(
            Long patientId) {

        // Make sure patient exists
        patientRepository.findById(patientId)
                .orElseThrow(patientNotFoundException(patientId));

        return appointmentRepository.findByPatientId(patientId)
                .stream()
                .sorted(
                        Comparator
                                .comparing(Appointment::getAppointmentDate)
                                .thenComparing(Appointment::getAppointmentTime)
                )
                .map(appointmentMapper::toResponse)
                .toList();
    }




    @Override
    public List<AppointmentResponse> getAppointmentsByDoctor(
            Long doctorId) {

        // Make sure doctor exists
        doctorRepository.findById(doctorId)
                .orElseThrow(doctorNotFoundException(doctorId));

        return appointmentRepository.findByDoctorId(doctorId)
                .stream()
                .sorted(
                        Comparator
                                .comparing(Appointment::getAppointmentDate)
                                .thenComparing(Appointment::getAppointmentTime)
                )
                .map(appointmentMapper::toResponse)
                .toList();
    }



    @Override
    public List<AppointmentResponse> getAppointmentsByDate(
            LocalDate date) {

        return appointmentRepository.findByAppointmentDate(date)
                .stream()
                .sorted(
                        Comparator.comparing(
                                Appointment::getAppointmentTime
                        )
                )
                .map(appointmentMapper::toResponse)
                .toList();
    }


    @Override
    public AppointmentResponse updateAppointment(
            Long appointmentId,
            AppointmentRequest request) {

        // Validate request
        appointmentValidator.validate(request);

        // Find existing appointment
        Appointment appointment =
                appointmentRepository.findById(appointmentId)
                        .orElseThrow(
                                () -> new AppointmentNotFoundException(
                                        "Appointment not found with id: "
                                                + appointmentId
                                )
                        );

        // Do not allow update of cancelled appointment
        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new IllegalStateException(
                    "Cancelled appointment cannot be updated"
            );
        }

        // Find new patient
        Patient patient =
                patientRepository.findById(request.getPatientId())
                        .orElseThrow(
                                patientNotFoundException(
                                        request.getPatientId()
                                )
                        );

        // Find new doctor
        Doctor doctor =
                doctorRepository.findById(request.getDoctorId())
                        .orElseThrow(
                                doctorNotFoundException(
                                        request.getDoctorId()
                                )
                        );

        // Validate date/time
        appointmentValidator.validateAppointmentDateTime(
                request.getAppointmentDate(),
                request.getAppointmentTime()
        );

      

        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDate(
                request.getAppointmentDate()
        );
        appointment.setAppointmentTime(
                request.getAppointmentTime()
        );
        appointment.setReason(request.getReason());
        appointment.setNotes(request.getNotes());

        Appointment updatedAppointment =
                appointmentRepository.save(appointment);

        return appointmentMapper.toResponse(updatedAppointment);
    }



    @Override
    public void deleteAppointment(Long appointmentId) {

        if (!appointmentRepository.existsById(appointmentId)) {
            throw new AppointmentNotFoundException(
                    "Appointment not found with id: " + appointmentId
            );
        }

        appointmentRepository.deleteById(appointmentId);
    }


    @Override
    public AppointmentResponse completeAppointment(
            Long appointmentId) {

        Appointment appointment =
                appointmentRepository.findById(appointmentId)
                        .orElseThrow(
                                () -> new AppointmentNotFoundException(
                                        "Appointment not found with id: "
                                                + appointmentId
                                )
                        );

        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new IllegalStateException(
                    "Cancelled appointment cannot be completed"
            );
        }

        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new IllegalStateException(
                    "Appointment is already completed"
            );
        }

        appointment.setStatus(AppointmentStatus.COMPLETED);

        Appointment updatedAppointment =
                appointmentRepository.save(appointment);

        return appointmentMapper.toResponse(updatedAppointment);
    }



    @Override
    public AppointmentResponse cancelAppointmentByPatient(
            Long appointmentId,
            String reason) {

        Appointment appointment =
                getAppointmentEntity(appointmentId);

        validateCanBeCancelled(appointment);

        appointment.setStatus(AppointmentStatus.CANCELLED);

        appointment.setCancelledBy(
                CancelledBy.PATIENT
        );

        appointment.setCancellationReason(reason);

        appointment.setCancelledAt(
                Instant.now()
        );

        Appointment cancelledAppointment =
                appointmentRepository.save(appointment);

        return appointmentMapper.toResponse(
                cancelledAppointment
        );
    }


 

    @Override
    public AppointmentResponse cancelAppointmentByDoctor(
            Long appointmentId,
            String reason) {

        Appointment appointment =
                getAppointmentEntity(appointmentId);

        validateCanBeCancelled(appointment);

        appointment.setStatus(AppointmentStatus.CANCELLED);

        appointment.setCancelledBy(
                CancelledBy.DOCTOR
        );

        appointment.setCancellationReason(reason);

        appointment.setCancelledAt(
                Instant.now()
        );

        Appointment cancelledAppointment =
                appointmentRepository.save(appointment);

        return appointmentMapper.toResponse(
                cancelledAppointment
        );
    }


    @Override
    public List<AppointmentResponse> getScheduledAppointments() {

        Predicate<Appointment> isScheduled =
                appointment ->
                        appointment.getStatus()
                                == AppointmentStatus.SCHEDULED;

        return appointmentRepository.findAll()
                .stream()
                .filter(isScheduled)
                .sorted(
                        Comparator
                                .comparing(
                                        Appointment::getAppointmentDate
                                )
                                .thenComparing(
                                        Appointment::getAppointmentTime
                                )
                )
                .map(appointmentMapper::toResponse)
                .toList();
    }


    private Appointment getAppointmentEntity(
            Long appointmentId) {

        return appointmentRepository.findById(appointmentId)
                .orElseThrow(
                        () -> new AppointmentNotFoundException(
                                "Appointment not found with id: "
                                        + appointmentId
                        )
                );
    }


    private void validateCanBeCancelled(
            Appointment appointment) {

        if (appointment.getStatus()
                != AppointmentStatus.SCHEDULED) {

            throw new IllegalStateException(
                    "Only scheduled appointments can be cancelled"
            );
        }
    }


    private Supplier<PatientNotFoundException>
    patientNotFoundException(Long patientId) {

        return () -> new PatientNotFoundException(
                "Patient not found with id: " + patientId
        );
    }


    private Supplier<DoctorNotFoundException>
    doctorNotFoundException(Long doctorId) {

        return () -> new DoctorNotFoundException(
                "Doctor not found with id: " + doctorId
        );
    }
}
package com.backendTraining;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import bt.com.dto.module.Appointment;
import bt.com.entity.AppointmentEntity;
import bt.com.entity.DoctorEntity;
import bt.com.entity.PatientEntity;
import bt.com.exception.PatientNotFoundException;
import bt.com.repository.AppointmentRepository;
import bt.com.repository.DoctorRepository;
import bt.com.repository.PatientRepository;
import bt.com.service.impl.AppointmentServiceImpl;

@ExtendWith(MockitoExtension.class)
class AppointmentOwnershipTests {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    @Test
    void patientBookingUsesAuthenticatedPatientInsteadOfSubmittedId() {
        PatientEntity authenticatedPatient = PatientEntity.builder()
                .id(10L)
                .name("Patient")
                .active(true)
                .build();
        DoctorEntity selectedDoctor = DoctorEntity.builder()
                .id(20L)
                .name("Doctor")
                .active(true)
                .build();

        when(patientRepository.findByUser_EmailIgnoreCase("patient@example.test"))
                .thenReturn(Optional.of(authenticatedPatient));
        when(patientRepository.findById(10L))
                .thenReturn(Optional.of(authenticatedPatient));
        when(doctorRepository.findById(20L))
                .thenReturn(Optional.of(selectedDoctor));
        when(appointmentRepository.save(any(AppointmentEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Appointment request = Appointment.builder()
                .patientId(999L)
                .doctorId(20L)
                .appointmentDate(LocalDate.now().plusDays(1))
                .appointmentTime(LocalTime.of(9, 0))
                .reason("Consultation")
                .build();

        appointmentService.createAppointmentForUser(
                request, "patient@example.test", "PATIENT");

        ArgumentCaptor<AppointmentEntity> savedAppointment =
                ArgumentCaptor.forClass(AppointmentEntity.class);
        verify(appointmentRepository).save(savedAppointment.capture());
        assertEquals(10L, savedAppointment.getValue().getPatient().getId());
        assertEquals(20L, savedAppointment.getValue().getDoctor().getId());
    }

    @Test
    void doctorAppointmentLookupIsScopedToAuthenticatedDoctor() {
        DoctorEntity doctor = DoctorEntity.builder()
                .id(20L)
                .name("Doctor")
                .active(true)
                .build();
        when(doctorRepository.findByUser_EmailIgnoreCase("doctor@example.test"))
                .thenReturn(Optional.of(doctor));
        when(appointmentRepository.findByDoctor_IdAndPatient_Id(20L, 10L))
                .thenReturn(java.util.List.of());

        appointmentService.getAppointmentsByPatientForDoctor(
                10L, "doctor@example.test");

        verify(appointmentRepository)
                .findByDoctor_IdAndPatient_Id(20L, 10L);
    }

    @Test
    void doctorCannotCreateAppointmentForUnassignedPatient() {
        PatientEntity patient = PatientEntity.builder()
                .id(10L)
                .name("Patient")
                .active(true)
                .build();
        DoctorEntity doctor = DoctorEntity.builder()
                .id(20L)
                .name("Doctor")
                .active(true)
                .build();

        when(doctorRepository.findByUser_EmailIgnoreCase("doctor@example.test"))
                .thenReturn(Optional.of(doctor));
        when(doctorRepository.findById(20L)).thenReturn(Optional.of(doctor));
        when(patientRepository.findById(10L)).thenReturn(Optional.of(patient));

        Appointment request = Appointment.builder()
                .patientId(10L)
                .doctorId(999L)
                .appointmentDate(LocalDate.now().plusDays(1))
                .appointmentTime(LocalTime.of(9, 0))
                .reason("Consultation")
                .build();

        assertThrows(
                PatientNotFoundException.class,
                () -> appointmentService.createAppointmentForUser(
                        request, "doctor@example.test", "DOCTOR"));
        verify(appointmentRepository, never()).save(any(AppointmentEntity.class));
    }
}

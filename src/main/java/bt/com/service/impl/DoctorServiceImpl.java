package bt.com.service.impl;

import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.springframework.stereotype.Service;

import bt.com.dto.request.DoctorRequest;
import bt.com.dto.response.DoctorResponse;
import bt.com.entity.Doctor;
import bt.com.exception.DoctorNotFoundException;
import bt.com.repository.DoctorRepository;
import bt.com.service.DoctorService;

@Service
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorServiceImpl(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    // Create Doctor
    @Override
    public DoctorResponse createDoctor(DoctorRequest request) {

        Doctor doctor = new Doctor();

        doctor.setName(request.getName());
        doctor.setSpecialization(request.getSpecialization());
        doctor.setPhone(request.getPhone());
        doctor.setEmail(request.getEmail());
        doctor.setActive(request.isActive());

        Doctor savedDoctor = doctorRepository.save(doctor);

        return mapToResponse(savedDoctor);
    }

    // Get all Doctors
    @Override
    public List<DoctorResponse> getAllDoctors() {

        return doctorRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Doctor::getName))
                .map(this::mapToResponse)
                .toList();
    }

    // Get Doctor by ID
    @Override
    public DoctorResponse getDoctorById(Long id) {

        return doctorRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() ->
                        new DoctorNotFoundException(
                                "Doctor not found with id: " + id
                        ));
    }

    // Update Doctor
    @Override
    public DoctorResponse updateDoctor(
            Long id,
            DoctorRequest request) {

        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() ->
                        new DoctorNotFoundException(
                                "Doctor not found with id: " + id
                        ));

        doctor.setName(request.getName());
        doctor.setSpecialization(request.getSpecialization());
        doctor.setPhone(request.getPhone());
        doctor.setEmail(request.getEmail());
        doctor.setActive(request.isActive());

        Doctor updatedDoctor = doctorRepository.save(doctor);

        return mapToResponse(updatedDoctor);
    }

    // Delete Doctor
    @Override
    public void deleteDoctor(Long id) {

        doctorRepository.findById(id)
                .orElseThrow(() ->
                        new DoctorNotFoundException(
                                "Doctor not found with id: " + id
                        ));

        doctorRepository.deleteById(id);
    }

    // Get Active Doctors
    @Override
    public List<DoctorResponse> getActiveDoctors() {

        Predicate<Doctor> isActive = Doctor::isActive;

        return doctorRepository.findAll()
                .stream()
                .filter(isActive)
                .sorted(Comparator.comparing(Doctor::getName))
                .map(this::mapToResponse)
                .toList();
    }

    // Consumer
    public void logAllDoctors() {

        List<Doctor> doctors = doctorRepository.findAll();

        Consumer<Doctor> printDoctor =
                doctor -> System.out.println(
                        "Doctor ID: " + doctor.getId()
                        + ", Name: " + doctor.getName()
                        + ", Specialization: "
                        + doctor.getSpecialization()
                );

        doctors.forEach(printDoctor);
    }

    // Entity → Response DTO
    private DoctorResponse mapToResponse(Doctor doctor) {

        return DoctorResponse.builder()
                .id(doctor.getId())
                .name(doctor.getName())
                .specialization(doctor.getSpecialization())
                .phone(doctor.getPhone())
                .email(doctor.getEmail())
                .active(doctor.isActive())
                .build();
    }
}
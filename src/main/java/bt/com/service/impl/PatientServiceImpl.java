
package bt.com.service.impl;

import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.springframework.stereotype.Service;

import bt.com.dto.request.PatientRequest;
import bt.com.dto.response.PatientResponse;
import bt.com.entity.Patient;
import bt.com.exception.PatientNotFoundException;
import bt.com.factory.PatientFactory;
import bt.com.mapper.PatientMapper;
import bt.com.repository.PatientRepository;
import bt.com.service.PatientService;
import bt.com.validator.PatientValidator;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final PatientValidator patientValidator;
    private final PatientFactory patientFactory;

    public PatientServiceImpl(
            PatientRepository patientRepository,
            PatientMapper patientMapper,
            PatientValidator patientValidator,
            PatientFactory patientFactory) {

        this.patientRepository = patientRepository;
        this.patientMapper = patientMapper;
        this.patientValidator = patientValidator;
        this.patientFactory = patientFactory;
    }

    // Create patient
    @Override
    public PatientResponse createPatient(PatientRequest request) {


        patientValidator.validate(request);

        
        Patient patient = patientFactory.createPatient(request);

        Patient savedPatient = patientRepository.save(patient);

        return patientMapper.toResponse(savedPatient);
    }

    // Get all patients
    @Override
    public List<PatientResponse> getAllPatients() {

        List<Patient> patients = patientRepository.findAll();

        Function<Patient, PatientResponse> patientToResponse =
                patientMapper::toResponse;

        // Sort patients by name
        Comparator<Patient> byName =
                Comparator.comparing(
                        Patient::getName,
                        Comparator.nullsLast(Comparator.naturalOrder())
                );

        return patients.stream()
                .sorted(byName)
                .map(patientToResponse)
                .toList();
    }

    // Get patient by ID
    @Override
    public PatientResponse getPatientById(Long id) {

        Supplier<PatientNotFoundException> patientNotFound =
                () -> new PatientNotFoundException(
                        "Patient not found with id: " + id
                );

        Patient patient = patientRepository.findById(id)
                .orElseThrow(patientNotFound);

        return patientMapper.toResponse(patient);
    }

    // Update patient
    @Override
    public PatientResponse updatePatient(
            Long id,
            PatientRequest request) {

        patientValidator.validate(request);

        Patient patient = patientRepository.findById(id)
                .orElseThrow(() ->
                        new PatientNotFoundException(
                                "Patient not found with id: " + id
                        ));

        patient.setName(request.getName());
        patient.setDateOfBirth(request.getDateOfBirth());
        patient.setGender(request.getGender());
        patient.setPhone(request.getPhone());
        patient.setEmail(request.getEmail());
        patient.setActive(request.isActive());

        Patient updatedPatient =
                patientRepository.save(patient);

        return patientMapper.toResponse(updatedPatient);
    }

    // Delete patient
    @Override
    public void deletePatient(Long id) {

        if (!patientRepository.existsById(id)) {
            throw new PatientNotFoundException(
                    "Patient not found with id: " + id
            );
        }

        patientRepository.deleteById(id);
    }

    // Get all active patients
    @Override
    public List<PatientResponse> getActivePatients() {

        List<Patient> patients = patientRepository.findAll();

        Predicate<Patient> isActive =
                Patient::isActive;

        Comparator<Patient> byName =
                Comparator.comparing(
                        Patient::getName,
                        Comparator.nullsLast(Comparator.naturalOrder())
                );

        return patients.stream()
                .filter(isActive)
                .sorted(byName)
                .map(patientMapper::toResponse)
                .toList();
    }

    // Consumer function
    public void logAllPatients() {

        List<Patient> patients = patientRepository.findAll();

        Consumer<Patient> printPatient =
                patient -> System.out.println(
                        "Patient ID: " + patient.getId()
                                + ", Name: " + patient.getName()
                                + ", DOB: " + patient.getDateOfBirth()
                );

        patients.forEach(printPatient);
    }
}
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
import bt.com.repository.PatientRepository;
import bt.com.service.PatientService;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    // Create patient
    @Override
    public PatientResponse createPatient(PatientRequest request) {

    	
    	 if (!PatientService.isValidAge(request.getAge())) {
    	        throw new IllegalArgumentException(
    	                "Patient age must be between 1 and 120"
    	        );
    	 }
        Patient patient = new Patient();

        patient.setName(request.getName());
        patient.setAge(request.getAge());
        patient.setGender(request.getGender());
        patient.setPhone(request.getPhone());
        patient.setEmail(request.getEmail());
        patient.setActive(request.isActive());

        Patient savedPatient = patientRepository.save(patient);

        return mapToResponse(savedPatient);
    }

    // Get all patients
    @Override
    public List<PatientResponse> getAllPatients() {

        List<Patient> patients = patientRepository.findAll();

        Function<Patient, PatientResponse> patientToResponse =
                patient -> mapToResponse(patient);
           
                
                //use of Comparator to sort the patients in ascending order
         Comparator<Patient> byAge =
                  Comparator.comparing(Patient::getAge);
                  
//                  For descending order
//                             .reversed();
         

        return patients.stream()

        		    .sorted(byAge)
                .map(patientToResponse)
                .toList();
    }

    // Get patient by ID
    @Override
    public PatientResponse getPatientById(Long id) {

        //Supplier provides the exception only when the patient is not found.
        Supplier<PatientNotFoundException> patientNotFound =
                () -> new PatientNotFoundException(
                        "Patient not found with id: " + id
                );

        Patient patient = patientRepository.findById(id)
                .orElseThrow(patientNotFound);

        return mapToResponse(patient);
    }

    // Update patient
    @Override
    public PatientResponse updatePatient(
            Long id,
            PatientRequest request) {

        Patient patient = patientRepository.findById(id)
                .orElseThrow(() ->
                        new PatientNotFoundException(
                                "Patient not found with id: " + id
                        ));

        patient.setName(request.getName());
        patient.setAge(request.getAge());
        patient.setGender(request.getGender());
        patient.setPhone(request.getPhone());
        patient.setEmail(request.getEmail());
        patient.setActive(request.isActive());

        Patient updatedPatient = patientRepository.save(patient);

        return mapToResponse(updatedPatient);
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
                patient -> patient.isActive();
                
//              By Name
              Comparator<Patient> byName =
             	        Comparator.comparing(Patient::getName);

      return  patients.stream()
        		    .sorted(byName)
                .filter(isActive)
                .map(patient -> mapToResponse(patient))
                .toList();
      

                 
      
    }

    // Consumer function
    public void logAllPatients() {

        List<Patient> patients = patientRepository.findAll();

        Consumer<Patient> printPatient =
                patient -> System.out.println(
                        "Patient ID: " + patient.getId()
                        + ", Name: " + patient.getName()
                );

                patients.stream()
                .forEach(printPatient);
    }

    // Convert Patient entity to PatientResponse DTO
    private PatientResponse mapToResponse(Patient patient) {

        return PatientResponse.builder()
                .id(patient.getId())
                .name(patient.getName())
                .age(patient.getAge())
                .gender(patient.getGender())
                .phone(patient.getPhone())
                .email(patient.getEmail())
                .active(patient.isActive())
                .build();
    }
}
package bt.com.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import bt.com.dto.request.PatientRequest;
import bt.com.dto.response.PatientResponse;
import bt.com.entity.Patient;
import bt.com.repository.PatientRepository;
import bt.com.service.PatientService;
import java.util.function.Predicate;

@Service
public class PatientServiceImpl  implements PatientService {
      
	 private final PatientRepository patientRepository;

	    public PatientServiceImpl(PatientRepository patientRepository) {
	        this.patientRepository = patientRepository;
	    }

	    @Override
	    public PatientResponse createPatient(PatientRequest request) {

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

	    @Override
	    public List<PatientResponse> getAllPatients() {

	        List<Patient> patients = patientRepository.findAll();

	        return patients.stream()
	                .map(patient -> mapToResponse(patient))
	                .toList();
	    }

	    @Override
	    public PatientResponse getPatientById(Long id) {

	        Patient patient = patientRepository.findById(id)
	                .orElseThrow(() ->
	                        new RuntimeException("Patient not found"));

	        return mapToResponse(patient);
	    }

	    @Override
	    public PatientResponse updatePatient(Long id,
	                                         PatientRequest request) {

	        Patient patient = patientRepository.findById(id)
	                .orElseThrow(() ->
	                        new RuntimeException("Patient not found"));

	        patient.setName(request.getName());
	        patient.setAge(request.getAge());
	        patient.setGender(request.getGender());
	        patient.setPhone(request.getPhone());
	        patient.setEmail(request.getEmail());

	        Patient updatedPatient = patientRepository.save(patient);

	        return mapToResponse(updatedPatient);
	    }

	    @Override
	    public void deletePatient(Long id) {

	        if (!patientRepository.existsById(id)) {
	            throw new RuntimeException("Patient not found");
	        }

	        patientRepository.deleteById(id);
	    }

	    
	    @Override
	 public List<PatientResponse> getActivePatients(){
	    	
	    	List<Patient> patients = patientRepository.findAll();
	    	
	    Predicate<Patient> isActive = patient -> patient.isActive();
	    
	    return patients.stream()
	    		.filter(isActive)
	    		.map(patient -> mapToResponse(patient))
	    		.toList();
	    	
	    }
	    private PatientResponse mapToResponse(Patient patient) {

	        return new PatientResponse(
	                patient.getId(),
	                patient.getName(),
	                patient.getAge(),
	                patient.getGender(),
	                patient.getPhone(),
	                patient.getEmail(),
	                patient.isActive()
	        );
	    }
	}
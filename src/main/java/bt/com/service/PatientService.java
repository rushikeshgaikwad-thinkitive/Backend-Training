package bt.com.service;



import java.util.List;

import bt.com.dto.request.PatientRequest;
import bt.com.dto.response.PatientResponse;

public interface PatientService {
	PatientResponse createPatient(PatientRequest request);
	
	List<PatientResponse> getAllPatients();
	
	PatientResponse getPatientById(Long id);
	
	PatientResponse updatePatient(Long id , PatientRequest rquest);
	
	void deletePatient(Long id);
	List<PatientResponse> getActivePatients();
	
	
}

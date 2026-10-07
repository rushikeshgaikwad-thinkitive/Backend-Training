package bt.com.service;



import java.util.List;

import bt.com.dto.request.PatientRequest;
import bt.com.dto.response.PatientResponse;

public interface PatientService {
	PatientResponse createPatient(PatientRequest request);

    List<PatientResponse> getAllPatients();

    PatientResponse getPatientById(Long id);

    PatientResponse updatePatient(
            Long id,
            PatientRequest request
    );

    void deletePatient(Long id);

    List<PatientResponse> getActivePatients();
	

    // default method
    default void printServiceName() {
        System.out.println("Patient Service");
    }


    // static method to check valid age
    static boolean isValidAge(int age) {
        return age > 0 && age <= 120;
    }

	
	
}

package bt.com.service;



import java.util.List;

import bt.com.dto.module.Patient;
import bt.com.dto.projection.PatientView;
;

public interface PatientService {
	 PatientView createPatient(Patient patient);

	    List<PatientView> getAllPatients();

	    PatientView getPatientById(Long id);

	    PatientView updatePatient(Long id, Patient patient);

	    void deletePatient(Long id);

	    List<PatientView> getActivePatients();

	
	
}

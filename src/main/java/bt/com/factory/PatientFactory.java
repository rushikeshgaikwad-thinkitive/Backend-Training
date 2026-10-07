
package bt.com.factory;

import org.springframework.stereotype.Component;

import bt.com.dto.request.PatientRequest;
import bt.com.entity.Patient;

@Component
public class PatientFactory {

    public Patient createPatient(PatientRequest request) {

        return Patient.builder()
                .name(request.getName())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .phone(request.getPhone())
                .email(request.getEmail())
                .active(request.isActive())
                .build();
    }
}

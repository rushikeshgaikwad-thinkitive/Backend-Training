
package bt.com.validator;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import bt.com.dto.request.PatientRequest;

@Component
public class PatientValidator {

    public void validate(PatientRequest request) {

        if (request == null) {
            throw new IllegalArgumentException(
                    "Patient request cannot be null"
            );
        }

        if (request.getName() == null ||
                request.getName().isBlank()) {

            throw new IllegalArgumentException(
                    "Patient name is required"
            );
        }

        if (request.getDateOfBirth() == null) {

            throw new IllegalArgumentException(
                    "Date of birth is required"
            );
        }

        if (request.getDateOfBirth().isAfter(LocalDate.now())) {

            throw new IllegalArgumentException(
                    "Date of birth cannot be in the future"
            );
        }

        if (request.getEmail() == null ||
                request.getEmail().isBlank()) {

            throw new IllegalArgumentException(
                    "Email is required"
            );
        }

        if (request.getPhone() == null ||
                request.getPhone().isBlank()) {

            throw new IllegalArgumentException(
                    "Phone is required"
            );
        }
    }
}

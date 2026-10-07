package bt.com.validator;

import org.springframework.stereotype.Component;

import bt.com.dto.request.DoctorRequest;

@Component
public class DoctorValidator {

    public void validate(DoctorRequest request) {

        if (request == null) {
            throw new IllegalArgumentException(
                    "Doctor request cannot be null"
            );
        }

        if (request.getName() == null ||
                request.getName().isBlank()) {

            throw new IllegalArgumentException(
                    "Doctor name is required"
            );
        }

        if (request.getSpecialization() == null ||
                request.getSpecialization().isBlank()) {

            throw new IllegalArgumentException(
                    "Doctor specialization is required"
            );
        }

        if (request.getPhone() == null ||
                request.getPhone().isBlank()) {

            throw new IllegalArgumentException(
                    "Doctor phone is required"
            );
        }

        if (request.getEmail() == null ||
                request.getEmail().isBlank()) {

            throw new IllegalArgumentException(
                    "Doctor email is required"
            );
        }
    }
}
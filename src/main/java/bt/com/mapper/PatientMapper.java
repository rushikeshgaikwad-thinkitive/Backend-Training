package bt.com.mapper;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

import bt.com.dto.response.PatientResponse;
import bt.com.entity.Patient;

@Component
public class PatientMapper {

    private static final DateTimeFormatter DOB_FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public PatientResponse toResponse(Patient patient) {

        if (patient == null) {
            return null;
        }

        LocalDate dob = patient.getDateOfBirth();

        int age = 0;
        String formattedDob = null;

        if (dob != null) {

            age = Period.between(
                    dob,
                    LocalDate.now()
            ).getYears();

            formattedDob = dob.format(DOB_FORMATTER);
        }

        return PatientResponse.builder()
                .id(patient.getId())
                .name(patient.getName())
                .age(age)
                .gender(patient.getGender())
                .phone(patient.getPhone())
                .email(patient.getEmail())
                .active(patient.isActive())
                .createdAt(patient.getCreatedAt())
                .updatedAt(patient.getUpdatedAt())
                .dateOfBirth(dob)
                .formattedDob(formattedDob)
                .build();
    }
}

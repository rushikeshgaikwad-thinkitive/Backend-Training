package bt.com.mapper;

import org.springframework.stereotype.Component;

import bt.com.dto.request.DoctorRequest;
import bt.com.dto.response.DoctorResponse;
import bt.com.entity.Doctor;

@Component
public class DoctorMapper {

    // Entity → Response DTO
    public DoctorResponse toResponse(Doctor doctor) {

        return DoctorResponse.builder()
                .id(doctor.getId())
                .name(doctor.getName())
                .specialization(doctor.getSpecialization())
                .phone(doctor.getPhone())
                .email(doctor.getEmail())
                .active(doctor.isActive())
                .createdAt(doctor.getCreatedAt())
                .updatedAt(doctor.getUpdatedAt())
                .build();
    }


    // Request DTO → Entity
    public Doctor toEntity(DoctorRequest request) {

        return Doctor.builder()
                .name(request.getName())
                .specialization(request.getSpecialization())
                .phone(request.getPhone())
                .email(request.getEmail())
                .active(request.isActive())
                .build();
    }
}
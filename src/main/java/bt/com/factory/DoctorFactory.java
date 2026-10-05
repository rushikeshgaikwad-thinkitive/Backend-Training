package bt.com.factory;

import org.springframework.stereotype.Component;

import bt.com.dto.request.DoctorRequest;
import bt.com.entity.Doctor;

@Component
public class DoctorFactory {

    public Doctor createDoctor(DoctorRequest request) {

        return Doctor.builder()
                .name(request.getName())
                .specialization(request.getSpecialization())
                .phone(request.getPhone())
                .email(request.getEmail())
                .active(request.isActive())
                .build();
    }
}

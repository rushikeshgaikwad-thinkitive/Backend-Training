package bt.com.service;

import java.util.List;

import bt.com.dto.request.DoctorRequest;
import bt.com.dto.response.DoctorResponse;

public interface DoctorService {

    DoctorResponse createDoctor(DoctorRequest request);

    List<DoctorResponse> getAllDoctors();

    DoctorResponse getDoctorById(Long id);

    DoctorResponse updateDoctor(Long id, DoctorRequest request);

    void deleteDoctor(Long id);

    List<DoctorResponse> getActiveDoctors();
}
package bt.com.service;

import java.util.List;

import bt.com.dto.module.Doctor;
import bt.com.dto.module.DoctorProfileUpdate;
import bt.com.dto.projection.DoctorView;

public interface DoctorService {

    DoctorView createDoctor(Doctor doctor);

    List<DoctorView> getAllDoctors();

    DoctorView getDoctorById(Long id);

    DoctorView updateDoctor(Long id, Doctor doctor);

    void deleteDoctor(Long id);

    List<DoctorView> getActiveDoctors();

    DoctorView getMyDoctorProfile(String email);

    DoctorView updateMyDoctorProfile(
            String email,
            DoctorProfileUpdate update);
}
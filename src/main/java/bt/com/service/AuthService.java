package bt.com.service;


import bt.com.dto.module.DoctorRegistrationRequest;
import bt.com.dto.module.Login;
import bt.com.dto.module.PatientRegistrationRequest;
import bt.com.dto.projection.LoginResponse;
import bt.com.dto.projection.Registration;

public interface AuthService {

    LoginResponse login(Login request);
 

    Registration registerPatient(
            PatientRegistrationRequest request);

    Registration registerDoctor(
            DoctorRegistrationRequest request);

    void approveDoctor(Long userId);
}


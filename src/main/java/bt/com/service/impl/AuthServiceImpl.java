
package bt.com.service.impl;

import java.time.LocalDate;
import java.time.Period;
import java.util.Locale;

import bt.com.dto.module.DoctorRegistrationRequest;
import bt.com.dto.module.Login;
import bt.com.dto.module.PatientRegistrationRequest;
import bt.com.dto.projection.LoginResponse;
import bt.com.dto.projection.Registration;

import bt.com.entity.DoctorEntity;
import bt.com.entity.PatientEntity;
import bt.com.entity.UserEntity;

import bt.com.enums.Role;

import bt.com.repository.DoctorRepository;
import bt.com.repository.PatientRepository;
import bt.com.repository.UserRepository;

import bt.com.security.JwtService;
import bt.com.service.AuthService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    @Transactional(readOnly = true)
    public LoginResponse login(Login request) {

        String email = normalizeEmail(request.getEmail());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email,
                        request.getPassword()
                )
        );

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Invalid email or password"
                ));

        String token = jwtService.generateToken(user);

        return LoginResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .userId(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    @Override
    @Transactional
    public Registration registerPatient(
            PatientRegistrationRequest request) {

        String email = normalizeEmail(request.getEmail());

        ensureEmailAvailable(email);

        if (patientRepository.existsByEmailIgnoreCase(email)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "A patient profile already exists for this email. "
                    + "Contact an administrator to verify and link the existing profile."
            );
        }

        LocalDate dateOfBirth = request.getDateOfBirth();

        int age = Period.between(
                dateOfBirth,
                LocalDate.now()
        ).getYears();

        UserEntity user = userRepository.save(
                UserEntity.builder()
                        .email(email)
                        .password(passwordEncoder.encode(
                                request.getPassword()
                        ))
                        .role(Role.PATIENT)
                        .active(true)
                        .build()
        );

        PatientEntity patient = PatientEntity.builder()
                .user(user)
                .name(request.getName().trim())
                .gender(request.getGender().trim())
                .age(age)
                .active(true)
                .phone(request.getPhone().trim())
                .email(email)
                .dateOfBirth(dateOfBirth)
                .build();

        patientRepository.save(patient);

        return Registration.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .status("ACTIVE")
                .message(
                        "Patient account and profile created successfully. "
                        + "You can now log in."
                )
                .build();
    }

    @Override
    @Transactional
    public Registration registerDoctor(
            DoctorRegistrationRequest request) {

        String email = normalizeEmail(request.getEmail());

        ensureEmailAvailable(email);

        if (doctorRepository.existsByEmailIgnoreCase(email)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "A doctor profile already exists for this email. "
                    + "Contact an administrator to verify and link the existing profile."
            );
        }

        UserEntity user = userRepository.save(
                UserEntity.builder()
                        .email(email)
                        .password(passwordEncoder.encode(
                                request.getPassword()
                        ))
                        .role(Role.DOCTOR)
                        .active(false)
                        .build()
        );

        DoctorEntity doctor = DoctorEntity.builder()
                .user(user)
                .name(request.getName().trim())
                .specialization(request.getSpecialization().trim())
                .phone(request.getPhone().trim())
                .email(email)
                .active(false)
                .build();

        doctorRepository.save(doctor);

        return Registration.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .status("PENDING_APPROVAL")
                .message(
                        "Doctor registration submitted. "
                        + "An administrator must approve the account before login."
                )
                .build();
    }

    @Override
    @Transactional
    public void approveDoctor(Long userId) {

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User account not found"
                ));

        if (user.getRole() != Role.DOCTOR) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "This account is not a doctor"
            );
        }

        DoctorEntity doctor = doctorRepository.findByUser_Id(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Doctor profile not found for this account"
                ));

        user.setActive(true);
        doctor.setActive(true);

        userRepository.save(user);
        doctorRepository.save(doctor);
    }

    private void ensureEmailAvailable(String email) {

        if (userRepository.existsByEmail(email)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "An account with this email already exists"
            );
        }
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}

package bt.com.service.impl;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.springframework.stereotype.Service;

import bt.com.dto.request.DoctorRequest;
import bt.com.dto.response.DoctorResponse;
import bt.com.entity.Doctor;
import bt.com.exception.DoctorNotFoundException;
import bt.com.factory.DoctorFactory;
import bt.com.mapper.DoctorMapper;
import bt.com.repository.DoctorRepository;
import bt.com.service.DoctorService;
import bt.com.validator.DoctorValidator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;
    private final DoctorValidator doctorValidator;
    private final DoctorFactory doctorFactory;

    public DoctorServiceImpl(
            DoctorRepository doctorRepository,
            DoctorMapper doctorMapper,
            DoctorValidator doctorValidator,
            DoctorFactory doctorFactory) {

        this.doctorRepository = doctorRepository;
        this.doctorMapper = doctorMapper;
        this.doctorValidator = doctorValidator;
        this.doctorFactory = doctorFactory;
    }

    // Create Doctor
    @Override
    public DoctorResponse createDoctor(DoctorRequest request) {
 
    	log.info("Creating new doctor");


        doctorValidator.validate(request);

        Doctor doctor =
                doctorFactory.createDoctor(request);

        Doctor savedDoctor =
                doctorRepository.save(doctor);
        
        log.info("\"Doctor created successfully: id= {}" +
        savedDoctor.getId());

        return doctorMapper.toResponse(savedDoctor);
        
    }

    // Get all Doctors
    @Override
    public List<DoctorResponse> getAllDoctors() {
 
        log.debug("Fetching all doctors");
        List<Doctor> doctors =
                doctorRepository.findAll();

        Comparator<Doctor> byName =
                Comparator.comparing(
                        Doctor::getName,
                        Comparator.nullsLast(
                                Comparator.naturalOrder()
                        )
                );

        List<DoctorResponse> responses = 
        		doctors.stream()
                .sorted(byName)
                .map(doctorMapper::toResponse)
                .toList();
        
        log.info(
                "Fetched {} doctors",
                responses.size() );
        
        return responses;
    }

    // Get Doctor by ID
    @Override
    public DoctorResponse getDoctorById(Long id) {
    	
    	  log.debug(
                  "Fetching doctor: id={}",
                  id
          );

        Supplier<DoctorNotFoundException> doctorNotFound =
                () -> new DoctorNotFoundException(
                        "Doctor not found with id: " + id
                );

        Doctor doctor =
                doctorRepository.findById(id)
                        .orElseThrow(doctorNotFound);

         
        return doctorMapper.toResponse(doctor);
    }

    // Update Doctor
    @Override
    public DoctorResponse updateDoctor(
            Long id,
            DoctorRequest request) {
    	
    	 log.info(
                 "Updating doctor: id={}",
                 id
         );


        doctorValidator.validate(request);

        Doctor doctor =
                doctorRepository.findById(id)
                        .orElseThrow(() ->
                                new DoctorNotFoundException(
                                        "Doctor not found with id: " + id
                                ));

        doctor.setName(request.getName());
        doctor.setSpecialization(
                request.getSpecialization()
        );
        doctor.setPhone(request.getPhone());
        doctor.setEmail(request.getEmail());
        doctor.setActive(request.isActive());

        Doctor updatedDoctor =
                doctorRepository.save(doctor);
        
        
        log.info(
                "Doctor updated successfully: id={}",
                id
        );

        return doctorMapper.toResponse(updatedDoctor);
    }

    // Delete Doctor
    @Override
    public void deleteDoctor(Long id) {
 
    	 log.info(
                 "Deleting doctor: id={}",
                 id
         );
        if (!doctorRepository.existsById(id)) {

            throw new DoctorNotFoundException(
                    "Doctor not found with id: " + id
            );
        }

        doctorRepository.deleteById(id);
        
        log.info(
                "Doctor deleted successfully: id={}",
                id
        );
    }

    // Get Active Doctors
    @Override
    public List<DoctorResponse> getActiveDoctors() {

        List<Doctor> doctors =
                doctorRepository.findAll();

        Predicate<Doctor> isActive =
                Doctor::isActive;

        return doctors.stream()
                .filter(isActive)
                .sorted(
                        Comparator.comparing(
                                Doctor::getName,
                                Comparator.nullsLast(
                                        Comparator.naturalOrder()
                                )
                        )
                )
                .map(doctorMapper::toResponse)
                .toList();
    }

}
package bt.com.factory;

import org.springframework.stereotype.Component;

import bt.com.dto.request.AppointmentRequest;
import bt.com.entity.Appointment;
import bt.com.entity.Doctor;
import bt.com.entity.Patient;
import bt.com.enums.AppointmentStatus;

@Component
public class AppointmentFactory {

	public Appointment createAppointment(AppointmentRequest request, Patient patient , Doctor doctor) {
		  return Appointment.builder()
				  .patient(patient)
				  .doctor(doctor)
				  .appointmentDate(
						  request.getAppointmentDate()
				)
				  .appointmentTime(
						  request.getAppointmentTime()
			    )
				  .reason(request.getReason()
				)
				  .notes(request.getNotes()
				)
				  .status(AppointmentStatus.SCHEDULED)
				  .build();
		  
	}
}

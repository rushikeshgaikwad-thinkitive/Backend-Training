package bt.com.dto.constants;

public final class Messages {

    private Messages() {
    }

    // =========================================================
    // PATIENT VALIDATION
    // =========================================================

    public static final String PATIENT_NAME_REQUIRED =
            "Patient name is required";

    public static final String PATIENT_NAME_MAX_LENGTH =
            "Patient name must not exceed 100 characters";

    public static final String PATIENT_AGE_REQUIRED =
            "Patient age is required";

    public static final String PATIENT_AGE_NEGATIVE =
            "Patient age cannot be negative";

    public static final String PATIENT_AGE_MAX =
            "Patient age must not exceed 150";

    public static final String PATIENT_GENDER_REQUIRED =
            "Patient gender is required";

    public static final String PATIENT_GENDER_MAX_LENGTH =
            "Patient gender must not exceed 20 characters";

    public static final String PATIENT_PHONE_REQUIRED =
            "Patient phone is required";

    public static final String PATIENT_PHONE_INVALID =
            "Patient phone must contain exactly 10 digits";

    public static final String PATIENT_EMAIL_REQUIRED =
            "Patient email is required";

    public static final String PATIENT_EMAIL_INVALID =
            "Patient email must be valid";

    public static final String PATIENT_EMAIL_MAX_LENGTH =
            "Patient email must not exceed 150 characters";

    public static final String PATIENT_ACTIVE_REQUIRED =
            "Patient active status is required";

    // =========================================================
    // PATIENT
    // =========================================================

    public static final String PATIENT_NOT_FOUND =
            "Patient not found with id: %s";

    public static final String CREATING_PATIENT =
            "Creating new patient";

    public static final String PATIENT_CREATED =
            "Patient created successfully: id={}";

    public static final String FETCHING_PATIENT =
            "Fetching patient: id={}";

    public static final String FETCHING_ALL_PATIENTS =
            "Fetching all patients";

    public static final String FETCHING_ACTIVE_PATIENTS =
            "Fetching active patients";

    public static final String UPDATING_PATIENT =
            "Updating patient: id={}";

    public static final String PATIENT_UPDATED =
            "Patient updated successfully: id={}";

    public static final String DELETING_PATIENT =
            "Deleting patient: id={}";

    public static final String PATIENT_DELETED =
            "Patient deleted successfully: id={}";

    // =========================================================
    // DOCTOR VALIDATION
    // =========================================================

    public static final String DOCTOR_NAME_REQUIRED =
            "Doctor name is required";

    public static final String DOCTOR_NAME_MAX_LENGTH =
            "Doctor name must not exceed 100 characters";

    public static final String DOCTOR_SPECIALIZATION_REQUIRED =
            "Doctor specialization is required";

    public static final String DOCTOR_SPECIALIZATION_MAX_LENGTH =
            "Doctor specialization must not exceed 100 characters";

    public static final String DOCTOR_PHONE_REQUIRED =
            "Doctor phone is required";

    public static final String DOCTOR_PHONE_INVALID =
            "Doctor phone must contain exactly 10 digits";

    public static final String DOCTOR_EMAIL_REQUIRED =
            "Doctor email is required";

    public static final String DOCTOR_EMAIL_INVALID =
            "Doctor email must be valid";

    public static final String DOCTOR_EMAIL_MAX_LENGTH =
            "Doctor email must not exceed 150 characters";

    public static final String DOCTOR_ACTIVE_REQUIRED =
            "Doctor active status is required";

    // =========================================================
    // DOCTOR
    // =========================================================

    public static final String DOCTOR_NOT_FOUND =
            "Doctor not found with id: %s";

    public static final String CREATING_DOCTOR =
            "Creating new doctor";

    public static final String DOCTOR_CREATED =
            "Doctor created successfully: id={}";

    public static final String FETCHING_DOCTOR =
            "Fetching doctor: id={}";

    public static final String FETCHING_ALL_DOCTORS =
            "Fetching all doctors";

    public static final String FETCHING_ACTIVE_DOCTORS =
            "Fetching active doctors";

    public static final String UPDATING_DOCTOR =
            "Updating doctor: id={}";

    public static final String DOCTOR_UPDATED =
            "Doctor updated successfully: id={}";

    public static final String DELETING_DOCTOR =
            "Deleting doctor: id={}";

    public static final String DOCTOR_DELETED =
            "Doctor deleted successfully: id={}";

    // =========================================================
    // APPOINTMENT VALIDATION
    // =========================================================

    public static final String APPOINTMENT_PATIENT_REQUIRED =
            "Patient is required";

    public static final String APPOINTMENT_DOCTOR_REQUIRED =
            "Doctor is required";

    public static final String APPOINTMENT_DATE_REQUIRED =
            "Appointment date is required";

    public static final String APPOINTMENT_TIME_REQUIRED =
            "Appointment time is required";

    public static final String APPOINTMENT_REASON_REQUIRED =
            "Appointment reason is required";

    public static final String APPOINTMENT_REASON_MAX_LENGTH =
            "Appointment reason must not exceed 500 characters";

    public static final String APPOINTMENT_NOTES_MAX_LENGTH =
            "Appointment notes must not exceed 1000 characters";

    public static final String CANCELLATION_REASON_REQUIRED =
            "Cancellation reason is required";

    public static final String CANCELLATION_REASON_MAX_LENGTH =
            "Cancellation reason must not exceed 500 characters";

    // =========================================================
    // APPOINTMENT
    // =========================================================

    public static final String APPOINTMENT_NOT_FOUND =
            "Appointment not found with id: %s";

    public static final String CREATING_APPOINTMENT =
            "Creating new appointment";

    public static final String APPOINTMENT_CREATED =
            "Appointment created successfully: id={}";

    public static final String FETCHING_APPOINTMENT =
            "Fetching appointment: id={}";

    public static final String FETCHING_ALL_APPOINTMENTS =
            "Fetching all appointments";

    public static final String FETCHING_PATIENT_APPOINTMENTS =
            "Fetching appointments for patient: id={}";

    public static final String FETCHING_DOCTOR_APPOINTMENTS =
            "Fetching appointments for doctor: id={}";

    public static final String FETCHING_DATE_APPOINTMENTS =
            "Fetching appointments for date: {}";

    public static final String FETCHING_SCHEDULED_APPOINTMENTS =
            "Fetching scheduled appointments";

    public static final String UPDATING_APPOINTMENT =
            "Updating appointment: id={}";

    public static final String APPOINTMENT_UPDATED =
            "Appointment updated successfully: id={}";

    public static final String DELETING_APPOINTMENT =
            "Deleting appointment: id={}";

    public static final String APPOINTMENT_DELETED =
            "Appointment deleted successfully: id={}";

    public static final String CANCELLING_APPOINTMENT =
            "Cancelling appointment: id={}";

    public static final String APPOINTMENT_CANCELLED =
            "Appointment cancelled successfully: id={}";

    public static final String COMPLETING_APPOINTMENT =
            "Completing appointment: id={}";

    public static final String APPOINTMENT_COMPLETED =
            "Appointment completed successfully: id={}";

    // =========================================================
    // APPOINTMENT BUSINESS RULES
    // =========================================================

    public static final String APPOINTMENT_DATE_PAST =
            "Appointment date cannot be in the past";

    public static final String APPOINTMENT_TIME_PAST =
            "Appointment time cannot be in the past";

    public static final String CANCELLED_APPOINTMENT_CANNOT_UPDATE =
            "Cancelled appointment cannot be updated";

    public static final String COMPLETED_APPOINTMENT_CANNOT_UPDATE =
            "Completed appointment cannot be updated";

    public static final String CANCELLED_APPOINTMENT_CANNOT_COMPLETE =
            "Cancelled appointment cannot be completed";

    public static final String APPOINTMENT_ALREADY_COMPLETED =
            "Appointment is already completed";

    public static final String ONLY_SCHEDULED_CAN_CANCEL =
            "Only scheduled appointments can be cancelled";

    // =========================================================
    // EXCEPTION LOGGING
    // =========================================================

    public static final String PATIENT_NOT_FOUND_LOG =
            "Patient not found: path={}, message={}";

    public static final String DOCTOR_NOT_FOUND_LOG =
            "Doctor not found: path={}, message={}";

    public static final String APPOINTMENT_NOT_FOUND_LOG =
            "Appointment not found: path={}, message={}";

    public static final String INVALID_REQUEST_LOG =
            "Invalid request: path={}, message={}";

    public static final String INVALID_STATE_LOG =
            "Invalid business operation: path={}, message={}";

    public static final String UNEXPECTED_ERROR_LOG =
            "Unexpected application error: path={}";

    public static final String INVALID_REQUEST =
            "Invalid request";

    public static final String INTERNAL_SERVER_ERROR =
            "An unexpected error occurred";
    
     
    public static final String  USER_NOT_FOUND =  "User not found";
}
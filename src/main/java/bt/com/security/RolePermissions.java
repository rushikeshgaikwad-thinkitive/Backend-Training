package bt.com.security;

import java.util.EnumSet;
import java.util.Set;

import bt.com.enums.Permission;
import bt.com.enums.Role;

public final class RolePermissions {

    private RolePermissions() {
    }

    public static Set<Permission> forRole(String role) {
        Role parsedRole;
        try {
            parsedRole = Role.valueOf(role);
        } catch (IllegalArgumentException | NullPointerException exception) {
            return Set.of();
        }

        return switch (parsedRole) {
            case ADMIN -> EnumSet.of(
                    Permission.PATIENT_MANAGE,
                    Permission.DOCTOR_VIEW,
                    Permission.DOCTOR_APPROVE,
                    Permission.APPOINTMENT_VIEW_ALL,
                    Permission.APPOINTMENT_CREATE,
                    Permission.APPOINTMENT_UPDATE,
                    Permission.APPOINTMENT_CANCEL);
            case DOCTOR -> EnumSet.of(
                    Permission.PATIENT_VIEW_ASSIGNED,
                    Permission.PATIENT_REGISTER,
                    Permission.DOCTOR_VIEW,
                    Permission.DOCTOR_UPDATE_OWN,
                    Permission.APPOINTMENT_VIEW_ASSIGNED,
                    Permission.APPOINTMENT_CREATE,
                    Permission.APPOINTMENT_UPDATE,
                    Permission.APPOINTMENT_CANCEL);
            case PATIENT -> EnumSet.of(
                    Permission.PATIENT_VIEW_OWN,
                    Permission.PATIENT_UPDATE_OWN,
                    Permission.DOCTOR_VIEW,
                    Permission.APPOINTMENT_VIEW_OWN,
                    Permission.APPOINTMENT_CREATE,
                    Permission.APPOINTMENT_RESCHEDULE,
                    Permission.APPOINTMENT_CANCEL);
        };
    }
}

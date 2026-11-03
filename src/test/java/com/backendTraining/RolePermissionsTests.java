package com.backendTraining;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import bt.com.enums.Permission;
import bt.com.security.RolePermissions;

class RolePermissionsTests {

    @Test
    void patientsCannotViewAllAppointmentsOrManageProfiles() {
        var permissions = RolePermissions.forRole("PATIENT");

        assertTrue(permissions.contains(Permission.PATIENT_VIEW_OWN));
        assertTrue(permissions.contains(Permission.APPOINTMENT_VIEW_OWN));
        assertFalse(permissions.contains(Permission.APPOINTMENT_VIEW_ALL));
        assertFalse(permissions.contains(Permission.PATIENT_MANAGE));
    }

    @Test
    void doctorsCanViewAssignedPatientsButNotManageAllPatients() {
        var permissions = RolePermissions.forRole("DOCTOR");

        assertTrue(permissions.contains(Permission.PATIENT_VIEW_ASSIGNED));
        assertFalse(permissions.contains(Permission.PATIENT_MANAGE));
        assertFalse(permissions.contains(Permission.APPOINTMENT_VIEW_ALL));
    }
}

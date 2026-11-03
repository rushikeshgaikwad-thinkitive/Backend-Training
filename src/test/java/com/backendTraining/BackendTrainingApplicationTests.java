package com.backendTraining;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import bt.com.BackendTrainingApplication;

@SpringBootTest(classes = BackendTrainingApplication.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class BackendTrainingApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void contextLoads() {
	}

	@Test
	void registrationLoginAuthorizationAndAppointmentWorkflow() throws Exception {
		String suffix = UUID.randomUUID().toString();
		String patientEmail = "patient-" + suffix + "@example.test";
		String doctorEmail = "doctor-" + suffix + "@example.test";
		String password = "test-user-password-123";

		objectMapper.readTree(
				postJson("/api/auth/register/patient", Map.of(
						"name", "Test Patient",
						"email", patientEmail,
						"password", password,
						"phone", "+15551234567",
						"gender", "Other",
						"dateOfBirth", "1990-01-01"))
						.andExpect(status().isCreated())
						.andReturn().getResponse().getContentAsString());
		String patientToken = login(patientEmail, password);

		JsonNode patientProfile = objectMapper.readTree(mockMvc.perform(
				get("/api/patients/me")
						.header("Authorization", bearer(patientToken)))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString());
		long patientId = patientProfile.get("id").asLong();

		mockMvc.perform(get("/api/patients")
				.header("Authorization", bearer(patientToken)))
				.andExpect(status().isForbidden());

		JsonNode doctorRegistration = objectMapper.readTree(
				postJson("/api/auth/register/doctor", Map.of(
						"name", "Test Doctor",
						"email", doctorEmail,
						"password", password,
						"phone", "+15557654321",
						"specialization", "Family Medicine"))
						.andExpect(status().isCreated())
						.andReturn().getResponse().getContentAsString());
		assertEquals("PENDING_APPROVAL",
				doctorRegistration.get("status").asText());

		mockMvc.perform(post("/api/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json(Map.of(
						"email", doctorEmail,
						"password", password))))
				.andExpect(status().isUnauthorized());

		String adminToken = login(
				"admin@example.test", "test-admin-password-123");
		mockMvc.perform(get("/api/patients")
				.header("Authorization", bearer(adminToken)))
				.andExpect(status().isOk());
		mockMvc.perform(patch("/api/admin/doctors/{userId}/approve",
				doctorRegistration.get("userId").asLong())
				.header("Authorization", bearer(adminToken)))
				.andExpect(status().isNoContent());

		String doctorToken = login(doctorEmail, password);
		JsonNode doctorProfile = objectMapper.readTree(mockMvc.perform(
				get("/api/doctors/me")
						.header("Authorization", bearer(doctorToken)))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString());
		long doctorId = doctorProfile.get("id").asLong();

		mockMvc.perform(get("/api/patients")
				.header("Authorization", bearer(doctorToken)))
				.andExpect(status().isForbidden());
		mockMvc.perform(patch("/api/admin/doctors/{userId}/approve",
				doctorRegistration.get("userId").asLong())
				.header("Authorization", bearer(patientToken)))
				.andExpect(status().isForbidden());

		JsonNode appointment = objectMapper.readTree(
				postJson("/api/appointments", Map.of(
						"patientId", patientId + 1000,
						"doctorId", doctorId,
						"appointmentDate", LocalDate.now().plusDays(2).toString(),
						"appointmentTime", "09:30:00",
						"reason", "Integration test appointment"),
						patientToken)
						.andExpect(status().isCreated())
						.andReturn().getResponse().getContentAsString());
		assertEquals(patientId, appointment.get("patientId").asLong());

		JsonNode patientAppointments = objectMapper.readTree(mockMvc.perform(
				get("/api/appointments/me")
						.header("Authorization", bearer(patientToken)))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString());
		assertEquals(1, patientAppointments.size());
		assertEquals(appointment.get("id").asLong(),
				patientAppointments.get(0).get("id").asLong());
		mockMvc.perform(get("/api/appointments")
				.header("Authorization", bearer(adminToken)))
				.andExpect(status().isOk());
	}

	private ResultActions postJson(String path, Object body) throws Exception {
		return mockMvc.perform(post(path)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json(body)));
	}

	private ResultActions postJson(String path, Object body, String token)
			throws Exception {
		return mockMvc.perform(post(path)
				.header("Authorization", bearer(token))
				.contentType(MediaType.APPLICATION_JSON)
				.content(json(body)));
	}

	private String login(String email, String password) throws Exception {
		String response = mockMvc.perform(post("/api/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json(Map.of("email", email, "password", password))))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		return objectMapper.readTree(response).get("accessToken").asText();
	}

	private String json(Object value) throws Exception {
		return objectMapper.writeValueAsString(value);
	}

	private String bearer(String token) {
		return "Bearer " + token;
	}
}

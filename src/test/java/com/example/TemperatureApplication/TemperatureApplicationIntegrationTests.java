package com.example.TemperatureApplication;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

/*
		This file contains integration tests that verify the functionality of the API as implemented
		in the TemperatureController.java. This file tests individual api calls as well as consecutive
		api calls. (ex: POST followed by a DELETE).
 */
@SpringBootTest
@AutoConfigureMockMvc
public class TemperatureApplicationIntegrationTests {

	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testValidTemperature() throws Exception {
		String validData = "365951380:1640995229697:'Temperature':58.48256793121914";
		mockMvc.perform(MockMvcRequestBuilders.post("/temp")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"data\": \"" + validData + "\"}")
			)
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.overtemp").value(false));
	}

	@Test
	public void testTemperatureExceedsThreshold() throws Exception {
		String validData = "365951380:1640995229697:'Temperature':158.48256793121914";
		mockMvc.perform(MockMvcRequestBuilders.post("/temp")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"data\": \"" + validData + "\"}")
			)
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.overtemp").value(true));
	}

	@Test
	public void testInvalidTemperature() throws Exception {
		String invalidData = "365951380:1640995229697:'Temperature':invalid_temperature";
		mockMvc.perform(MockMvcRequestBuilders.post("/temp")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"data\": \"" + invalidData + "\"}")
			)
			.andExpect(status().isBadRequest())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.error").value("bad request"));

		//test for null request object
		invalidData = null;
		mockMvc.perform(MockMvcRequestBuilders.post("/temp")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"data\": \"" + invalidData + "\"}")
			)
			.andExpect(status().isBadRequest())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.error").value("bad request"));

		//test partially null request data
		invalidData = "365951380::'Temperature':58.48256793121914";
		mockMvc.perform(MockMvcRequestBuilders.post("/temp")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"data\": \"" + invalidData + "\"}")
			)
			.andExpect(status().isBadRequest())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.error").value("bad request"));

	}

	@Test
	public void testGetErrors() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/errors")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$.errors").isArray());
	}

	@Test
	public void testClearErrors() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/errors")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.content().string("Errors cleared"));
	}

	/*
			The following test will execute the POST, GET, and DELETE calls.
			This simulates the 4xx workflow where the client has made POST requests with
			invalid input.
	 */
	@Test
	public void testFullErrorHandling() throws Exception {
		String error1 = "Error 1";
		String error2 = "Error 2";

		mockMvc.perform(post("/temp")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"data\": \"" + error1 + "\"}")
			)
			.andExpect(status().isBadRequest());

		mockMvc.perform(post("/temp")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"data\": \"" + error2 + "\"}")
			)
			.andExpect(status().isBadRequest());

		// Perform GET request to retrieve errors
		mockMvc.perform(get("/errors"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.errors", hasSize(2)))
			.andExpect(jsonPath("$.errors", containsInAnyOrder(error1, error2)));

		// Perform DELETE request to clear errors
		mockMvc.perform(delete("/errors"))
			.andExpect(status().isOk());

		// Verify that the errors list is empty after deletion
		mockMvc.perform(get("/errors"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.errors", hasSize(0)));
	}
}

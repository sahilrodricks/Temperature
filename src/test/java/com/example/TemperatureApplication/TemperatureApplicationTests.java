package com.example.TemperatureApplication;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TemperatureApplicationTests {

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
	public void testInvalidTemperature() throws Exception {
		String invalidData = "365951380:1640995229697:'Temperature':invalid_temperature"; // Invalid temperature
		mockMvc.perform(MockMvcRequestBuilders.post("/temp")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"data\": \"" + invalidData + "\"}")
			)
			.andExpect(status().isBadRequest())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.error").value("bad request"));
	}
}

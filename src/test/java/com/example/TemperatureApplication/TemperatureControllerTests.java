package com.example.TemperatureApplication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


/*
    This file contains the Unite Tests for the TemperatureController.
 */
public class TemperatureControllerTests {

  private TemperatureController temperatureController;

  @Mock
  private ObjectMapper objectMapper;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    temperatureController = new TemperatureController();
  }

  @Test
  public void testPostDataOverTemp() throws IOException {
    JsonNode requestPayload = mock(JsonNode.class);
    when(requestPayload.get("data")).thenReturn(mock(JsonNode.class));
    when(requestPayload.get("data").asText()).thenReturn("12345:1633742400000:Temperature:92.5");

    TemperatureRequest temperatureRequest = new TemperatureRequest(requestPayload);
    when(objectMapper.readValue(requestPayload.toString(), TemperatureRequest.class)).thenReturn(temperatureRequest);

    ResponseEntity<?> responseEntity = temperatureController.postData(requestPayload);

    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertEquals(true, ((TemperatureResponse) responseEntity.getBody()).isOvertemp());
  }

  @Test
  public void testPostDataNotOverTemp() throws IOException {
    JsonNode requestPayload = mock(JsonNode.class);
    when(requestPayload.get("data")).thenReturn(mock(JsonNode.class));
    when(requestPayload.get("data").asText()).thenReturn("12345:1633742400000:Temperature:72.5");

    TemperatureRequest temperatureRequest = new TemperatureRequest(requestPayload);
    when(objectMapper.readValue(requestPayload.toString(), TemperatureRequest.class)).thenReturn(temperatureRequest);

    ResponseEntity<?> responseEntity = temperatureController.postData(requestPayload);

    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertEquals(false, ((TemperatureResponse) responseEntity.getBody()).isOvertemp());
  }

  @Test
  public void testPostDataNumberFormatException() throws JsonProcessingException {
    JsonNode requestPayload = mock(JsonNode.class);
    when(requestPayload.get("data")).thenReturn(mock(JsonNode.class));
    when(requestPayload.get("data").asText()).thenReturn("12345:invalidEpochMs:Temperature:72.5");

    when(objectMapper.readValue(requestPayload.toString(), TemperatureRequest.class))
      .thenThrow(NumberFormatException.class); // Throw NumberFormatException

    ResponseEntity<?> responseEntity = temperatureController.postData(requestPayload);

    assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
  }


  @Test
  public void testGetErrors() {
    ResponseEntity<ConcurrentHashMap<String, List<String>>> responseEntity = temperatureController.getErrors();
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
  }

  @Test
  public void testClearErrors() {
    ResponseEntity<String> responseEntity = temperatureController.clearErrors();
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
  }
}

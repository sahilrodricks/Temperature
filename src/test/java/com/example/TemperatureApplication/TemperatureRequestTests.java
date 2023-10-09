package com.example.TemperatureApplication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


/*
    This file contains unit tests for the TemperatureRequest class.
    This will test the basic functionality of the constructors, accessors, and mutator methods.
 */
public class TemperatureRequestTests {
  private TemperatureRequest temperatureRequest;

  @Before
  public void setUp() {
    String json = "{\"data\":\"12345:1633742400000:Temperature:72.5\"}";
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode jsonNode;
    try {
      jsonNode = objectMapper.readTree(json);
      temperatureRequest = new TemperatureRequest(jsonNode);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testGetData() {
    assertEquals("12345:1633742400000:Temperature:72.5", temperatureRequest.getData());
  }

  @Test
  public void testGetDeviceId() {
    assertEquals(12345, temperatureRequest.getDeviceId());
  }

  @Test
  public void testGetEpochMs() {
    assertEquals(1633742400000L, temperatureRequest.getEpochMs());
  }

  @Test
  public void testGetTemperature() {
    assertEquals(72.5, temperatureRequest.getTemperature(), 0.001); // delta for floating-point comparison
  }

  @Test
  public void testGetDataString() {
    String[] expectedDataString = {"12345", "1633742400000", "Temperature", "72.5"};
    assertArrayEquals(expectedDataString, temperatureRequest.getDataString());
  }

  @Test
  public void testIsOverTemp() {
    assertFalse(temperatureRequest.isOverTemp()); // 72.5 < 90
  }

  @Test
  public void testGetFormattedTime() {
    assertEquals("2021/10/08 18:20:00", temperatureRequest.getFormattedTime());
  }
}

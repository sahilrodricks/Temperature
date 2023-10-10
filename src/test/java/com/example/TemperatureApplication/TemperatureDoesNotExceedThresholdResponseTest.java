package com.example.TemperatureApplication;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import java.util.concurrent.ConcurrentHashMap;


/*
  This class provides unit test for the error response {"overtemp":false}
  returned when the temperature is below 90.
 */

public class TemperatureDoesNotExceedThresholdResponseTest {
  private TemperatureDoesNotExceedThresholdResponse response;

  @BeforeEach
  public void setUp() {
    response = new TemperatureDoesNotExceedThresholdResponse();
  }

  @Test
  public void testGetResponseMap() {
    ConcurrentHashMap<String, Boolean> responseMap = response.getResponseMap();
    assertTrue(responseMap.containsKey("overtemp"));
    assertFalse(responseMap.get("overtemp"));
  }

  @Test
  public void testGetIsOverThreshold() {
    assertFalse(response.getIsOverThreshold());
  }
}



package com.example.TemperatureApplication;

import org.junit.Test;
import static org.junit.Assert.*;


/*
  This file contains unit tests for the TemperatureResponse class.
  This will test the basic functionality of the constructors, accessors, and mutator methods.
  */
public class TemperatureResponseTests {

  @Test
  public void testIsOverTemp() {
    TemperatureResponse response1 = new TemperatureResponse(true, 123, "2023/10/09 18:20:00");
    assertTrue(response1.isOvertemp());

    TemperatureResponse response2 = new TemperatureResponse(false, 456, "2023/10/09 12:30:00");
    assertFalse(response2.isOvertemp());
  }

  @Test
  public void testGetDeviceId() {
    TemperatureResponse response = new TemperatureResponse(true, 789, "2023/10/09 15:45:00");
    assertEquals(789, response.getDeviceId());
  }

  @Test
  public void testGetFormattedTime() {
    TemperatureResponse response = new TemperatureResponse(true, 101, "2023/10/09 09:00:00");
    assertEquals("2023/10/09 09:00:00", response.getFormattedTime());
  }
}


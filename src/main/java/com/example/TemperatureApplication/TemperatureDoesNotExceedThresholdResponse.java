package com.example.TemperatureApplication;

import java.util.concurrent.ConcurrentHashMap;

/*
  This class will return the response body {"overtemp":false} whenever a valid
  data string does not exceed 90.
 */
public class TemperatureDoesNotExceedThresholdResponse {
  private ConcurrentHashMap<String, Boolean> responseMap = new ConcurrentHashMap<String, Boolean>() {{
    put("overtemp", false);
  }};

  public ConcurrentHashMap<String, Boolean> getResponseMap() {
    return responseMap;
  }

  public boolean getIsOverThreshold() {
    return responseMap.get("overtemp");
  }
}

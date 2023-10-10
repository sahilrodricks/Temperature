package com.example.TemperatureApplication;

import com.fasterxml.jackson.databind.JsonNode;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
   This class is used to map the data from the incoming JSON request.

   As per the instructions the JSON blob will have:
        {"data": data_string}
             -data is the key
             -data_string as a value is further broken down:
                  -deviceId (int32)
                  -epochMs timestamp (int64)
                  -'Temperature' static, free-formed text
                  -temperature (float64) value

   The TemperatureController will process the TemperatureRequest object on the POST call to execute
   the specified behaviors as per the instructions.
 */

public class TemperatureRequest {
  private String data;
  private int deviceId;
  private long epochMs;
  private double temperature;

  private String[] dataString;

  public TemperatureRequest(JsonNode jsonNode) {

    try {
      this.data = jsonNode.get("data").asText();
      if (!data.contains("'Temperature'")) {
        throw new IllegalArgumentException("Invalid 'data' field format");
      }
      String[] parts = data.split(":");
      this.dataString = parts;
      this.deviceId = Integer.parseInt(parts[0]);
      this.epochMs = Long.parseLong(parts[1]);
      this.temperature = Double.parseDouble(parts[3]);
    } catch (Exception e) {
      if(e instanceof NullPointerException) {
        throw e;
      }
      throw new IllegalArgumentException("Invalid Input" + e);
    }
  }

  public String getData() {
    return data;
  }

  public int getDeviceId() {
    return deviceId;
  }

  public long getEpochMs() {
    return epochMs;
  }

  public double getTemperature() {
    return temperature;
  }

  public String[] getDataString(){
    return dataString;
  }

  public boolean isOverTemp() {
    return temperature >= 90;
  }

  public String getFormattedTime() {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    return dateFormat.format(new Date(epochMs));
  }
}

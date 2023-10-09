package com.example.TemperatureApplication;

/*
This class will represent the response payload returned for all API interactions.
@param overTemp - true/false indicating if temperature threshold has been breached.
@param deviceId - int32 value of device id passed by client.
@param data - int64 epochMs timestamp.
 */

public class TemperatureResponse {
  private boolean overtemp;
  private int deviceId;
  private String formattedTime;

  public TemperatureResponse(boolean overtemp, int deviceId, String formattedTime) {
    this.overtemp = overtemp;
    this.deviceId = deviceId;
    this.formattedTime = formattedTime;
  }

  public boolean isOvertemp() {
    return overtemp;
  }

  public int getDeviceId() {
    return deviceId;
  }

  public String getFormattedTime() {
    return formattedTime;
  }
}

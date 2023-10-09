package com.example.TemperatureApplication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/*
    This is the main REST controller for the temperature api as defined by the instructions.
    Author: Sahil Rodricks
 */

@RestController
public class TemperatureController {
  private List<String> errorList = new ArrayList<>();
  private static final String BAD_REQUEST_ERROR = "{\"error\": \"bad request\"}";

  @PostMapping(value = "/temp", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> postData(@RequestBody JsonNode requestPayload) throws JsonProcessingException {
    System.out.println("Received data: " + requestPayload);

    try {
      TemperatureRequest request = new TemperatureRequest(requestPayload);

      int deviceId = request.getDeviceId();
      long epochMs = request.getEpochMs();
      double temperature = request.getTemperature();
      boolean isOverTemp = temperature >= 90;

      if (isOverTemp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String formattedTime = dateFormat.format(new Date(epochMs));
        return ResponseEntity.ok(
          new TemperatureResponse(true, deviceId, formattedTime)
        );
      } else {
        return ResponseEntity.ok(
          new TemperatureResponse(false, deviceId, null)
        );
      }

      /* Any exception when attempting to map client's "data string" input
         These are returned as bad requests while adding the data string to the error list
      */
    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
      errorList.add(requestPayload.get("data").asText());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(BAD_REQUEST_ERROR);
    } catch (IllegalArgumentException ie) {
      errorList.add(requestPayload.get("data").asText());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BAD_REQUEST_ERROR);
    }
  }

  @GetMapping("/errors")
  public ResponseEntity<ConcurrentHashMap<String, List<String>>> getErrors() {
    ConcurrentHashMap<String, List<String>> response = new ConcurrentHashMap<>();
    response.put("errors", errorList);
    System.out.println("Error Map: " + response);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/errors")
  public ResponseEntity<String> clearErrors() {
    errorList.clear();
    return ResponseEntity.ok("Errors cleared");
  }
}

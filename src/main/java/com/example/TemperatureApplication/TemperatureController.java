package com.example.TemperatureApplication;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/*
    This is the main REST controller for the temperature api as defined by the instructions.
    Author: Sahil Rodricks
 */

@RestController
public class TemperatureController {

  private List<String> errorList = new ArrayList<>();

  @PostMapping("/temp")
  public ResponseEntity<TemperatureResponse> postData(@RequestBody String data) {
    try {
      // Validate Client Request - returns http 400 for invalid input
      String[] parts = data.split(":");
      if (parts.length != 4 || !parts[2].equals("'Temperature'")) {
        errorList.add(data);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(new TemperatureResponse(false, 0, null));
      }

      int deviceId = Integer.parseInt(parts[0]);
      long epochMs = Long.parseLong(parts[1]);
      double temperature = Double.parseDouble(parts[3]);
      boolean overtemp = temperature >= 90;

      if (overtemp) {
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
    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
      errorList.add(data);
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new TemperatureResponse(false, 0, null)); // Custom response for bad request
    }
  }

  @GetMapping("/errors")
  public ResponseEntity<List<String>> getErrors() {
    return ResponseEntity.ok(errorList);
  }

  @DeleteMapping("/errors")
  public ResponseEntity<String> clearErrors() {
    errorList.clear();
    return ResponseEntity.ok("Errors cleared");
  }
}

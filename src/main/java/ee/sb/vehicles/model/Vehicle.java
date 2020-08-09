package ee.sb.vehicles.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class Vehicle {
  Long id;
  String plateNumber;
  Integer firstRegistration;
  Long purchasePrice;
  String producer;
  Long mileage;
  BigDecimal previousIndemnity;
}

package ee.sb.vehicles.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@Entity
@Table
@NoArgsConstructor
public class Vehicle {
  @Id
  Long id;
  String plateNumber;
  Integer firstRegistration;
  Long purchasePrice;
  String producer;
  Long mileage;
  BigDecimal previousIndemnity;
}

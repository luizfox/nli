package ee.sb.vehicles.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CoeffficientFactor {
  BigDecimal coefficientValue;
  BigDecimal instanceValue;

  public BigDecimal getFactorValue() {
    return coefficientValue.multiply(instanceValue);
  }
}

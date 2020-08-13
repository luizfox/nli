package ee.sb.vehicles.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CoeffficientFactor {
  public enum Label{ AGE, VALUE, PREVIOUS_INDEMNITY }
  BigDecimal coefficientValue;
  BigDecimal instanceValue;
  Label name;

  public BigDecimal getFactorValue() {
    return coefficientValue.multiply(instanceValue);
  }
}

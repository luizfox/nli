package ee.sb.vehicles.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class ResultOfCalculation {
  List<CoeffficientFactor.Label> usedCoefficients;
  BigDecimal yearlyFee;
  BigDecimal monthlyFee;
}

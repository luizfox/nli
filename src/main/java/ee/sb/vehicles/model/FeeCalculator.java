package ee.sb.vehicles.model;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Component
public class FeeCalculator {

  public ResultOfCalculation getResultOfCalculation(List<CoeffficientFactor> factors) {
    List<CoeffficientFactor.Label> coefficientNames = new ArrayList<>();
    factors.forEach( coeffficientFactor -> coefficientNames.add(coeffficientFactor.getName()));
    BigDecimal totalAnnualFee = getTotalAnnualFee(factors);
    return ResultOfCalculation.builder()
      .usedCoefficients(coefficientNames)
      .monthlyFee(getTotalMonthlyFee(totalAnnualFee))
      .yearlyFee(totalAnnualFee)
      .build();
  }

  private BigDecimal getTotalAnnualFee(List<CoeffficientFactor> listOfFactors) {
    return listOfFactors.stream()
      .map(CoeffficientFactor::getFactorValue)
      .reduce(BigDecimal::add)
      .get();
  }

  private BigDecimal getTotalMonthlyFee(BigDecimal totalAnnualFee) {
    return totalAnnualFee.divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);
  }
}

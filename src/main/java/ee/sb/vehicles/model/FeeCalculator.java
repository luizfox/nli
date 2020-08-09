package ee.sb.vehicles.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class FeeCalculator {
  private ArrayList<CoeffficientFactor> listOfFactors = new ArrayList<>();

  public void addFactor(CoeffficientFactor coeffficientFactor) {
    listOfFactors.add(coeffficientFactor);
  }

  public void addListOfFactors(List<CoeffficientFactor> listToAdd) {
    listOfFactors.addAll(listToAdd);
  }

  public BigDecimal getTotalAnnualFee() {
    return listOfFactors.stream()
      .map(CoeffficientFactor::getFactorValue)
      .reduce(BigDecimal::add)
      .get();
  }

  public BigDecimal getTotalMontlyFee() {
    return this.getTotalAnnualFee().divide(BigDecimal.valueOf(12));
  }

  public void clearList() {
    listOfFactors.clear();
  }
}

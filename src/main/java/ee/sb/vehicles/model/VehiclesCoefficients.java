package ee.sb.vehicles.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

@Data
@Component
public class VehiclesCoefficients {
  @JsonProperty("coefficients") Map<String, BigDecimal> coefficients;
  @JsonProperty("make_coefficients") Map<String, String> makeCoefficients;
  @JsonProperty("avg_purchase_price") Map<String, Long> averagePurchasePrice;

  public BigDecimal getVehicleAgeCoefficient() {
    return new BigDecimal(coefficients.get("vehicle_age").toString());
  }

  public BigDecimal getVehicleValueCoefficient() {
    return new BigDecimal(coefficients.get("vehicle_value").toString());
  }

  public BigDecimal getVehiclePreviousIndemnityCoefficient() {
    return new BigDecimal(coefficients.get("previous_indemnity").toString());
  }
}
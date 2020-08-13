package ee.sb.vehicles.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "CascoPayment")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CascoPayment {

  @Id
  Long id;

  @Column(name = "yearlyPayment")
  BigDecimal YearlyPayment;

  @Column(name = "montlyPayment")
  BigDecimal montlyPayment;
}

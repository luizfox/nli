package ee.sb.vehicles.repository;

import ee.sb.vehicles.model.CascoPayment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CascoPaymentRepository extends CrudRepository<CascoPayment, Long> {
}

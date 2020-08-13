package ee.sb.vehicles.csv;

import ee.sb.vehicles.model.Vehicle;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@Component
public class CSVParser {
  public Optional<Vehicle> csvRecordToVehicle(CSVRecord csvRecord){
    try {
      Vehicle vehicle = new Vehicle();
      vehicle.setId(Long.valueOf(csvRecord.get(CSVReader.Headers.id)));
      vehicle.setFirstRegistration(Integer.valueOf(csvRecord.get(CSVReader.Headers.first_registration)));
      vehicle.setMileage(Long.valueOf(csvRecord.get(CSVReader.Headers.mileage)));
      vehicle.setPlateNumber(csvRecord.get(CSVReader.Headers.plate_number));
      vehicle.setPreviousIndemnity(new BigDecimal(csvRecord.get(CSVReader.Headers.previous_indemnity)));
      vehicle.setPurchasePrice(Long.valueOf(csvRecord.get(CSVReader.Headers.purchase_price)));
      vehicle.setProducer(csvRecord.get(CSVReader.Headers.producer));
      return Optional.of(vehicle);
    } catch (Exception ex) {
      log.error("Unable to convert this record: {} : {}", csvRecord.toString(), ex.getMessage());
      return Optional.empty();
    }
  }
}

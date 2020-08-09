package ee.sb.vehicles.task1;

import ee.sb.vehicles.model.Vehicle;
import ee.sb.vehicles.model.VehiclesCoefficients;
import org.apache.commons.csv.CSVRecord;
import ee.sb.vehicles.readers.CSVReader;
import ee.sb.vehicles.readers.VehicleCoefficientsReader;
import ee.sb.vehicles.writer.CSVWriter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Optional;
import java.util.function.Consumer;

import static ee.sb.vehicles.readers.CSVReader.Headers;

public class Task1 {

  private VehiclesCoefficients vehiclesCoefficients;
  private CSVWriter csvWriter;

  Task1(String outputFile) {
    this.vehiclesCoefficients = VehicleCoefficientsReader.getVehiclesCoefficients().orElseThrow(() -> {
      throw new RuntimeException("Unable to load the vehicles coefficients");
    });
    this.csvWriter = new CSVWriter(outputFile);
  }


  void generateYearlyPayment(String sourceCsv) {
    readRecords(sourceCsv);
    finishFile();
  }

  private void readRecords(String sourceCsv) {
    Consumer<CSVRecord> csvRecordConsumer =  csvRecord -> {
      Optional<Vehicle> vehicle = csvRecordToVehicle(csvRecord);
      if (vehicle.isPresent()) {
        BigDecimal yearlyFee = calculateCascoPayment(vehicle.get());
        writeResults(yearlyFee, vehicle.get());
      }
    };
    CSVReader.readCsv(csvRecordConsumer, sourceCsv);
  }

  private void finishFile() {
    csvWriter.finishWriting();
  }

  private void writeResults(BigDecimal yearlyFee, Vehicle vehicle) {
    BigDecimal monthlyFee = yearlyFee.divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);
    csvWriter.writeRecord(vehicle.getId(), yearlyFee.setScale(2, RoundingMode.HALF_UP), monthlyFee);
  }

  private BigDecimal calculateCascoPayment(Vehicle vehicle ) {
    Integer currentAge = calculateVehicleAge(vehicle);
    BigDecimal vehicleRisk = calculateVehicleRisk(vehicle);
    Long price = vehicle.getPurchasePrice();
    return calculateWithFormula(currentAge, vehicleRisk, price);
  }

  private BigDecimal calculateWithFormula(Integer currentAge, BigDecimal vehicleRisk, Long price) {
    return vehicleRisk.multiply(
      vehiclesCoefficients.getVehicleAgeCoefficient().multiply(BigDecimal.valueOf(currentAge)).add(
        vehiclesCoefficients.getVehicleValueCoefficient().multiply(BigDecimal.valueOf(price)) ) );
  }

  private BigDecimal calculateVehicleRisk(Vehicle vehicle) {
    Optional<String> makeCoefficient = Optional.ofNullable(vehiclesCoefficients.getMakeCoefficients().get(
      vehicle.getProducer()));
    return makeCoefficient.map(BigDecimal::new).orElseGet(() -> new BigDecimal("1.00"));
  }

  private Integer calculateVehicleAge(Vehicle vehicle) {
    Integer currentYear = Calendar.getInstance().get(Calendar.YEAR);
    return currentYear - vehicle.getFirstRegistration();
  }

  private Optional<Vehicle> csvRecordToVehicle(CSVRecord csvRecord){
    try {
      return Optional.of(Vehicle.builder().
        firstRegistration(Integer.valueOf(csvRecord.get(Headers.first_registration)))
        .mileage(Long.valueOf(csvRecord.get(Headers.mileage)))
        .plateNumber(csvRecord.get(Headers.plate_number))
        .previousIndemnity(new BigDecimal(csvRecord.get(Headers.previous_indemnity)))
        .producer(csvRecord.get(Headers.producer))
        .purchasePrice(Long.valueOf(csvRecord.get(Headers.purchase_price)))
        .id(Long.valueOf(csvRecord.get(Headers.id)))
        .build());
    } catch (Exception ex) {
      System.out.println("Unable to convert this record:" + csvRecord.toString()+ " : " +ex.getMessage());
      return Optional.empty();
    }
  }

  public static void main(String[] args) {
    final String SOURCE_FILE = "src/main/resources/vehicles.csv";
    final String OUTPUT_FILE = "src/main/resources/CascoPayment.csv";
    Task1 task1 = new Task1(OUTPUT_FILE);
    task1.generateYearlyPayment(SOURCE_FILE);
  }
}
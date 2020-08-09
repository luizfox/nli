package ee.sb.vehicles.task;

import ee.sb.vehicles.model.CoeffficientFactor;
import ee.sb.vehicles.model.FeeCalculator;
import ee.sb.vehicles.model.Vehicle;
import ee.sb.vehicles.model.VehiclesCoefficients;
import org.apache.commons.csv.CSVRecord;
import ee.sb.vehicles.readers.CSVReader;
import ee.sb.vehicles.readers.VehicleCoefficientsReader;
import ee.sb.vehicles.writer.CSVWriter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static ee.sb.vehicles.readers.CSVReader.Headers;

public class Task {

  private FeeCalculator feeCalculator = new FeeCalculator();
  private VehiclesCoefficients vehiclesCoefficients;
  private CSVWriter csvWriter;

  Task(String outputFile) {
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
      Optional<Vehicle> vehicleOptional = csvRecordToVehicle(csvRecord);
      vehicleOptional.ifPresent( vehicle -> {
        BigDecimal yearlyFee = calculateCascoPayment(vehicle);
        writeResults(yearlyFee, vehicle);
      });
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
    CoeffficientFactor ageCoefficient = new CoeffficientFactor(vehiclesCoefficients.getVehicleAgeCoefficient(),
      BigDecimal.valueOf(calculateVehicleAge(vehicle)));
    CoeffficientFactor valueCoefficient = new CoeffficientFactor(vehiclesCoefficients.getVehicleValueCoefficient(),
      BigDecimal.valueOf(vehicle.getPurchasePrice()));
    CoeffficientFactor indemnityCoefficient = new CoeffficientFactor(vehiclesCoefficients.getVehiclePreviousIndemnityCoefficient(),
      vehicle.getPreviousIndemnity());
    BigDecimal vehicleRisk = getRiskCoefficient(vehicle);
    feeCalculator.addListOfFactors(List.of(ageCoefficient, valueCoefficient, indemnityCoefficient));
    BigDecimal result = calculateWithFormula(vehicleRisk);
    feeCalculator.clearList();
    return result;
  }

  private BigDecimal calculateWithFormula(BigDecimal vehicleRisk) {
    return vehicleRisk.multiply(feeCalculator.getTotalAnnualFee());
  }

  private BigDecimal getRiskCoefficient(Vehicle vehicle) {
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
    Task task = new Task(OUTPUT_FILE);
    task.generateYearlyPayment(SOURCE_FILE);
  }
}
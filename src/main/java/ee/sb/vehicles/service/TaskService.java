package ee.sb.vehicles.service;

import ee.sb.vehicles.csv.CSVParser;
import ee.sb.vehicles.model.*;
import ee.sb.vehicles.repository.CascoPaymentRepository;
import ee.sb.vehicles.repository.VehicleRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVRecord;
import ee.sb.vehicles.csv.CSVReader;
import ee.sb.vehicles.csv.VehicleCoefficientsReader;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Consumer;

@Slf4j
@Service
public class TaskService {
  private final String SOURCE_FILE = "src/main/resources/vehicles.csv";

  private final FeeCalculator feeCalculator;
  private final VehiclesCoefficients vehiclesCoefficients;
  private final CascoPaymentRepository cascoPaymentRepository;
  private final VehicleRepository vehicleRepository;
  private final CSVParser csvParser;

  TaskService(FeeCalculator feeCalculator, CascoPaymentRepository cascoPaymentRepository, VehicleRepository vehicleRepository, CSVParser csvParser) {
    this.feeCalculator = feeCalculator;
    this.cascoPaymentRepository = cascoPaymentRepository;
    this.vehicleRepository = vehicleRepository;
    this.csvParser = csvParser;
    this.vehiclesCoefficients = VehicleCoefficientsReader.getVehiclesCoefficients().orElseThrow(() -> {
      throw new RuntimeException("Unable to load the vehicles coefficients");
    });
  }

  @PostConstruct
  public void generateYearlyPayment() {
    generateYearlyPayment(SOURCE_FILE);
  }

  public void generateYearlyPayment(String source) {
    List<Vehicle> vehicleList = new ArrayList<>();
    List<CascoPayment> cascoPaymentList = new ArrayList<>();
    readRecords(source, vehicleList, cascoPaymentList);
    vehicleRepository.saveAll(vehicleList);
    cascoPaymentRepository.saveAll(cascoPaymentList);
  }

  public VehiclesCoefficients listCoefficients() {
    return vehiclesCoefficients;
  }

  public Iterable<Vehicle> listVehicles() {
     return vehicleRepository.findAll();
  }

  public Iterable<CascoPayment> listCascoPayments() {
    return cascoPaymentRepository.findAll();
  }

  public Map<String, BigDecimal> listUsedCoefficients() {
    return vehiclesCoefficients.getCoefficients();
  }

  private void readRecords(String sourceCsv, List<Vehicle> vehicleList, List<CascoPayment> cascoPaymentList) {
    Consumer<CSVRecord> csvRecordConsumer =  csvRecord -> {
      Optional<Vehicle> vehicleOptional = csvParser.csvRecordToVehicle(csvRecord);
      vehicleOptional.ifPresent( vehicle -> {
        BigDecimal yearlyFee = calculateCascoPayment(vehicle);
        writeResults(yearlyFee, vehicle, vehicleList, cascoPaymentList);
      });
    };
    CSVReader.readCsv(csvRecordConsumer, sourceCsv);
  }

  private void writeResults(BigDecimal yearlyFee, Vehicle vehicle, List<Vehicle> vehicleList, List<CascoPayment> cascoPaymentList) {
    BigDecimal monthlyFee = yearlyFee.divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);
    CascoPayment cascoPayment = new CascoPayment(vehicle.getId(), yearlyFee.setScale(2, RoundingMode.HALF_UP),
      monthlyFee);
    vehicleList.add(vehicle);
    cascoPaymentList.add(cascoPayment);
  }

  private BigDecimal calculateCascoPayment(Vehicle vehicle ) {
    CoeffficientFactor ageCoefficient = new CoeffficientFactor(vehiclesCoefficients.getVehicleAgeCoefficient(),
      BigDecimal.valueOf(calculateVehicleAge(vehicle)), CoeffficientFactor.Label.AGE);
    CoeffficientFactor valueCoefficient = new CoeffficientFactor(vehiclesCoefficients.getVehicleValueCoefficient(),
      BigDecimal.valueOf(vehicle.getPurchasePrice()), CoeffficientFactor.Label.VALUE);
    CoeffficientFactor indemnityCoefficient = new CoeffficientFactor(vehiclesCoefficients.getVehiclePreviousIndemnityCoefficient(),
      vehicle.getPreviousIndemnity(), CoeffficientFactor.Label.PREVIOUS_INDEMNITY);
    BigDecimal vehicleRisk = getRiskCoefficient(vehicle);
    ResultOfCalculation resultOfCalculation =
      feeCalculator.getResultOfCalculation(List.of(ageCoefficient, valueCoefficient, indemnityCoefficient));
    BigDecimal result = calculateWithFormula(vehicleRisk, resultOfCalculation);
    return result;
  }

  private BigDecimal calculateWithFormula(BigDecimal vehicleRisk, ResultOfCalculation resultOfCalculation) {
    return vehicleRisk.multiply(resultOfCalculation.getYearlyFee());
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
}
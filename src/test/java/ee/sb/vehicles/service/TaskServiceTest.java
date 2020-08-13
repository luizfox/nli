package ee.sb.vehicles.service;

import ee.sb.vehicles.csv.CSVParser;
import ee.sb.vehicles.model.FeeCalculator;
import ee.sb.vehicles.model.Vehicle;
import ee.sb.vehicles.model.VehiclesCoefficients;
import ee.sb.vehicles.repository.CascoPaymentRepository;
import ee.sb.vehicles.repository.VehicleRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.test.util.ReflectionTestUtils;



import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


class TaskServiceTest {

  @Spy FeeCalculator feeCalculator;
  @Mock VehiclesCoefficients vehiclesCoefficients;
  @Mock CascoPaymentRepository cascoPaymentRepository;
  @Mock VehicleRepository vehicleRepository;
  @Spy CSVParser csvParser;
  @InjectMocks TaskService taskService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
    when(vehicleRepository.findAll()).thenReturn(new ArrayList<>());
    when(cascoPaymentRepository.findAll()).thenReturn(new ArrayList<>());
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  void generateYearlyPayment() {
    assertDoesNotThrow(() -> taskService.generateYearlyPayment());
  }

  @Test
  void getAllVehicles() {
    assertNotNull(taskService.listVehicles());
  }

  @Test
  void listCoefficients() {
    VehiclesCoefficients coefficients = taskService.listCoefficients();
    assertNotNull(coefficients);
    assertEquals(3, coefficients.getCoefficients().size());
  }

  @Test
  void listCascoPayments() {
    assertNotNull(taskService.listCascoPayments());
  }

  @Test
  void listUsedCoefficients() {
    Map<String, BigDecimal> usedCoefficients =  taskService.listUsedCoefficients();
    assertNotNull(usedCoefficients);
    assertEquals(3, usedCoefficients.size());
  }

  @Test
  void calculateCascoPayment() {
    Vehicle vehicle = new Vehicle();
    vehicle.setProducer("FORD");
    vehicle.setPurchasePrice(123L);
    vehicle.setPreviousIndemnity(new BigDecimal("123.00"));
    vehicle.setPurchasePrice(321L);
    vehicle.setMileage(543L);
    vehicle.setFirstRegistration(2015);
    vehicle.setId(98L);
    BigDecimal result = ReflectionTestUtils.invokeMethod(taskService, "calculateCascoPayment", vehicle);
    assertEquals(37, result.intValue());
  }
}
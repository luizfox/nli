package ee.sb.vehicles.controller;

import ee.sb.vehicles.model.CascoPayment;
import ee.sb.vehicles.model.Vehicle;
import ee.sb.vehicles.model.VehiclesCoefficients;
import ee.sb.vehicles.service.TaskService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping
@CrossOrigin(origins = {"http://localhost:8080", "http://localhost:4200"}, maxAge = 3600)
public class TaskController {

  private final TaskService taskService;

  public TaskController(TaskService taskService) {
    this.taskService = taskService;
  }

  @GetMapping("/listCoefficients")
  VehiclesCoefficients listCoefficients () {
    return taskService.listCoefficients();
  }

  @GetMapping("/listVehicles")
  Iterable<Vehicle> listVehicles () {
    return taskService.listVehicles();
  }

  @GetMapping("/listCasco")
  Iterable<CascoPayment> listCascoPayments () {
    return taskService.listCascoPayments();
  }

  @GetMapping("/listUsedCoefficients")
  Map<String, BigDecimal> listUsedCoefficients () {
    return taskService.listUsedCoefficients();
  }



}

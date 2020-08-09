package ee.sb.vehicles.task;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

  @BeforeEach
  void setUp() {
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  void generateYearlyPayment() {
    final String OUTPUT_FILE = "src/test/resources/outputTest.csv";
    final String SOURCE_FILE = "src/test/resources/vehiclesTest.csv";
    Task task = new Task(OUTPUT_FILE);
    task.generateYearlyPayment(SOURCE_FILE);
    String output = readFile(OUTPUT_FILE);
    assertTrue(output.contains("100,4846.67,403.89"));
  }

  private String readFile(String filePath) {
    String data = null;
    try {
      File file = new File(filePath);
      Scanner scanner = new Scanner(file);
      while (scanner.hasNextLine()) {
        data = scanner.nextLine();
      }
      scanner.close();
    } catch (FileNotFoundException e) {
      System.out.println("File not found:" + e.getMessage());
    }
    return data;
  }
}
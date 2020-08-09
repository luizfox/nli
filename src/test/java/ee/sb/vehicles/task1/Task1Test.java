package ee.sb.vehicles.task1;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class Task1Test {

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
    Task1 task1 = new Task1(OUTPUT_FILE);
    task1.generateYearlyPayment(SOURCE_FILE);
    String output = readFile(OUTPUT_FILE);
    assertTrue(output.contains("100,3822.52,318.54"));
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
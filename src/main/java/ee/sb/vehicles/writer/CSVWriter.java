package ee.sb.vehicles.writer;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CSVWriter {
  private BufferedWriter writer;
  private CSVPrinter csvPrinter;

  public CSVWriter (String outputFile) {
    try {
      writer = Files.newBufferedWriter(Paths.get(outputFile));
      csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
        .withHeader("ID", "YearlyPayment", "MontlyPayment"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void writeRecord(Long id, BigDecimal yearlyFee, BigDecimal monthlyFee) {
    try {
      csvPrinter.printRecord(id, yearlyFee, monthlyFee);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void finishWriting() {
    try {
      csvPrinter.flush();
      csvPrinter.close();
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

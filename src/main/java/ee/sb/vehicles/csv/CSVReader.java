package ee.sb.vehicles.csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Consumer;

public class CSVReader {
  public enum Headers {
    id, plate_number, first_registration, purchase_price, producer, mileage, previous_indemnity
  }

  public static void readCsv(Consumer<CSVRecord> csvRecordConsumer, String filePath) {
    try (
      Reader reader = Files.newBufferedReader(Paths.get(filePath));
      CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
        .withFirstRecordAsHeader()
        .withHeader(Headers.class)
        .withIgnoreHeaderCase()
        .withTrim())){
      csvParser.getRecords().forEach(csvRecordConsumer::accept);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}



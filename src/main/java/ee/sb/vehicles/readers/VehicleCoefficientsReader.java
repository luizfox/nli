package ee.sb.vehicles.readers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import ee.sb.vehicles.model.VehiclesCoefficients;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;

public class VehicleCoefficientsReader {

  private static final String JSON_DATA_FILE = "src/main/resources/data.json";

  public static Optional<VehiclesCoefficients> getVehiclesCoefficients() {
    ObjectMapper mapper = new ObjectMapper();
    ObjectReader reader = mapper.readerFor(VehiclesCoefficients.class).withRootName("data");
    try {
      VehiclesCoefficients vehiclesCoefficients = reader.readValue(Paths.get(JSON_DATA_FILE).toFile());
      return Optional.ofNullable(vehiclesCoefficients);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return Optional.empty();
  }

}

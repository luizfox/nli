* Main stack used: Java 11 (although the project version is 9), Spring Boot and H2 Database;

* To execute the project, please run the ee.sb.vehicles.VehicleApplication;

* The hardware used was a 8GB RAM machine. I didn't have to change any JVM Heap parameters, but in case
you run out of Heap, please add the JVM initialization option "-Xmx1G" (without quotes). It should be enough;

* One can check the database at http://localhost:8080/h2-console/ It should be available after the application
startup process - in my case, it took me less than 10 seconds to boot;

* You can check the API endpoints accessing the following URLs:

http://localhost:8080/listCasco

http://localhost:8080/listVehicles

http://localhost:8080/listCoefficients

http://localhost:8080/listUsedCoefficients


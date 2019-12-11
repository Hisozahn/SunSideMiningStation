package SunSideMiningStation;

import SunSideMiningStation.Jersey.JerseyApplication;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;

import java.io.IOException;
import java.net.URI;

public class App {
    private static final URI BASE_URI = URI.create("http://localhost:8080/");

    public static void main(String[] args) {
        ServiceLocator locator = ServiceLocatorUtilities.createAndPopulateServiceLocator();
        HttpServer httpServer = GrizzlyHttpServerFactory.createHttpServer(BASE_URI, new JerseyApplication(), locator);

        try {
            httpServer.start();
            System.out.println("Jersey app started");
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
            httpServer.shutdownNow();
        }
    }
}

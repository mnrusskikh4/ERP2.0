package tests.authpagetests;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class SmokeE2ETests {

    private WireMockServer wireMockServer;

    @BeforeClass
    public void setup() {
        // Инициализация WireMockServer без указания порта
        wireMockServer = new WireMockServer();
        wireMockServer.start();

        // Настройка WireMock
        configureFor("localhost", wireMockServer.port()); // Используем автоматически назначенный порт
        stubFor(get(urlEqualTo("/path/to/your/endpoint"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBodyFile("path/to/your/file.stl"))); // Убедитесь, что файл находится в каталоге `resources` WireMock
    }

    @Test
    public void testSomething() {
        // Ваш E2E тест с использованием Selenium и TestNG
    }

    @AfterClass
    public void teardown() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }
}

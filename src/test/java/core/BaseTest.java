package core;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.FileDownloadMode;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import demoqa.MainPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.HttpCommandExecutor;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

import static config.Prop.PROP;


abstract public class BaseTest {
    MainPage mainPage;

    public static void setUp() {
        String remoteUrl = System.getProperty("remote.url", System.getenv("REMOTE_URL"));

        // Полностью блокируем автоматику Selenide
        System.setProperty("selenide.driverManagerEnabled", "false");
        Configuration.proxyEnabled = false;
        Configuration.fileDownload = FileDownloadMode.HTTPGET;

        if (remoteUrl != null && !remoteUrl.isEmpty()) {
            try {
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");
                options.addArguments("--remote-allow-origins=*");
                options.setCapability("se:cdp", false); // CDP часто ломает Docker-сессии

                // Используем прямой Executor, чтобы избежать DriverService
                HttpCommandExecutor executor = new HttpCommandExecutor(new URL(remoteUrl));
                RemoteWebDriver driver = new RemoteWebDriver(executor, options);

                WebDriverRunner.setWebDriver(driver);

                // Настройки Selenide после установки драйвера
                Configuration.timeout = 10000;

            } catch (Exception e) {
                throw new RuntimeException("CRITICAL: Connection to Grid failed", e);
            }
        }
    }

    @BeforeEach
    void setUp2() {
        mainPage = new MainPage(PROP.getURL());
    }

    @BeforeAll
    static void init() {
        setUp();
    }

    @AfterEach
    public void tearDown() {
        if (WebDriverRunner.hasWebDriverStarted()) {
            Selenide.closeWebDriver();
        }
    }
}

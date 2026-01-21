package core;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;


abstract public class BaseTest {

    public static void setUp(){
        Configuration.browser = "firefox";
        Configuration.headless = false;

    }

    @BeforeAll
    static void init() {
        setUp();
    }

    @AfterEach
    public void tearDown() {
        Selenide.closeWebDriver();
    }
}

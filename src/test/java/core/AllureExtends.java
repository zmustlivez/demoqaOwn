package core;

import com.codeborne.selenide.junit5.ScreenShooterExtension;
import org.junit.jupiter.api.extension.RegisterExtension;

public class AllureExtends {
    @RegisterExtension
    ScreenShooterExtension screenShooterExtension = new ScreenShooterExtension().to("target/selenide");
}
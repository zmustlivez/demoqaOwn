package demoqa.Elements;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$x;

public class RadioButtonPage {

    private final SelenideElement yesRadio = $x("//label[@for='yesRadio']");
    private final SelenideElement resultYesRadio = $x("//span[text()='Yes']");
    private final SelenideElement imperessiveRadio = $x("//label[@for='impressiveRadio']");
    private final SelenideElement resultImperessiveRadio = $x("//span[text()='Impressive']");

    public String markYesRadio() {
    yesRadio.scrollTo().click();
        return resultYesRadio.getText();
    }

    public String markImpressiveRadio() {
    imperessiveRadio.scrollTo().click();
        return resultImperessiveRadio.getText();
    }
}

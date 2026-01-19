package demoqa.Elements;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$x;

public class ButtonsPage {

    private final SelenideElement doubleClickButton = $x("//button[@id='doubleClickBtn']");
    private final SelenideElement resultDoubleClickButton = $x("//p[@id='doubleClickMessage']");

    private final SelenideElement rightClickButton = $x("//button[@id='rightClickBtn']");
    private final SelenideElement resultRightClickButton = $x("//p[@id='rightClickMessage']");

    private final SelenideElement clickMeButton = $x("//button[@class='btn btn-primary' and @type='button' and text()='Click Me']");
    private final SelenideElement resultClickMeButton = $x("//p[@id='dynamicClickMessage']");

    public String doubleClickButton() {
        doubleClickButton.doubleClick();
        return resultDoubleClickButton.getText();
    }

    public String rightClickButton() {
        rightClickButton.contextClick();
        return resultRightClickButton.getText();
    }

    public String clickMeButton() {
        clickMeButton.click();
        return resultClickMeButton.getText();
    }
}

package demoqa.Elements;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class ElementsPage {
//    private final SelenideElement textBoxElement = $x("//span[text()='Text Box']");
    private final SelenideElement textBoxElement = $("li#item-0 span.text");
//    private final SelenideElement checkBoxElement = $x("//span[text()='Check Box']");
    private final SelenideElement checkBoxElement = $("li#item-1 span.text");
//    private final SelenideElement radioButtonElement = $x("//span[text()='Radio Button']");
    private final SelenideElement radioButtonElement = $("li#item-2 span.text");
//    private final SelenideElement webTablesElement = $x("//span[text()='Web Tables']");
    private final SelenideElement webTablesElement = $("li#item-3 span.text");
//    private final SelenideElement buttonsElement = $x("//span[text()='Buttons']");
    private final SelenideElement buttonsElement = $("li#item-4 span.text");

    public TextBoxPage openTextBox() {

        textBoxElement.shouldHave(text("Text Box")).click();
        return new TextBoxPage();
    }

    public CheckBoxPage openCheckBox() {
        checkBoxElement.shouldHave(text("Check Box")).click();
        return new CheckBoxPage();
    }

    public RadioButtonPage openRadioButton() {
        radioButtonElement.shouldHave(text("Radio Button")).click();
        return new RadioButtonPage();
    }

    public WebTablesPage openWebTables() {
        webTablesElement.shouldHave(text("Web Tables")).click();
        return new WebTablesPage();
    }

    public ButtonsPage openButtons() {
        buttonsElement.shouldHave(text("Buttons")).click();
        return new ButtonsPage();
    }

}

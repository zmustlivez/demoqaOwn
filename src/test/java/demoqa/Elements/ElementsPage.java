package demoqa.Elements;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$x;

public class ElementsPage {
    private final SelenideElement textBoxElement = $x("//span[text()='Text Box']");
    private final SelenideElement checkBoxElement = $x("//span[text()='Check Box']");
    private final SelenideElement radioButtonElement = $x("//span[text()='Radio Button']");
    private final SelenideElement webTablesElement = $x("//span[text()='Web Tables']");
    private final SelenideElement buttonsElement = $x("//span[text()='Buttons']");

    public TextBoxPage openTextBox() {

        textBoxElement.click();
        return new TextBoxPage();
    }

    public CheckBoxPage openCheckBox() {
        checkBoxElement.click();
        return new CheckBoxPage();
    }

    public RadioButtonPage openRadioButton() {
        radioButtonElement.click();
        return new RadioButtonPage();
    }

    public WebTablesPage openWebTables() {
        webTablesElement.click();
        return new WebTablesPage();
    }

    public ButtonsPage openButtons() {
        buttonsElement.click();
        return new ButtonsPage();
    }

}

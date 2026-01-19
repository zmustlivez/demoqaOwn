package demoqa.Elements;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$x;

public class CheckBoxPage {

    private final SelenideElement markHome = $x("//span[text()='Home']");
    private final SelenideElement resultMarkHome = $x("//div[@id='result']");

    public String markHome (){
        markHome.click();
        String result = resultMarkHome.getText();
        return result;
    }
}

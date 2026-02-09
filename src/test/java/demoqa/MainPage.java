package demoqa;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import demoqa.Elements.ElementsPage;

import static com.codeborne.selenide.Selenide.$x;


public class MainPage {

    private final SelenideElement elements = $x("//h5[text()='Elements']");
    private final SelenideElement forms = $x("//h5[text()='Elements']");
    private final SelenideElement alerts_frame_windows = $x("//h5[text()='Alerts, Frame & Windows']");
    private final SelenideElement widgets = $x("//h5[text()='Widgets']");
    private final SelenideElement nteractions = $x("//h5[text()='Interactions']");
    private final SelenideElement book = $x("//h5[text()='Book Store Application']");

    public MainPage(String url) {
        if (url == null || url.isEmpty()) throw new RuntimeException("URL is empty!");
        Selenide.open(url);
    }

    public ElementsPage selectElements(){
        elements.scrollTo().click();
        return new ElementsPage();
    }

}

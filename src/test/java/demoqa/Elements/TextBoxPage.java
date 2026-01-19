package demoqa.Elements;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$x;

public class TextBoxPage {

    private final SelenideElement fullName = $x("//input[@id='userName']");
    private final SelenideElement userEmail = $x("//input[@id='userEmail']");
    private final SelenideElement usecCurrentAddress = $x("//textarea[@id='currentAddress']");
    private final SelenideElement userPermanentAddress = $x("//textarea[@id='permanentAddress']");
    private final SelenideElement submitButton = $x("//button[@id='submit']");
    private final SelenideElement resultName = $x("//p[@id='name']");
    private final SelenideElement resultEmail = $x("//p[@id='email']");
    private final SelenideElement resultCurrentAddress = $x("//p[@id='currentAddress']");
    private final SelenideElement resultPermanentAddress = $x("//p[@id='permanentAddress']");

    public String resultInputTextBox(String inputFullname,
                                     String inputEmail,
                                     String inputCurrentAddress,
                                     String inputPermanentAddress) {

        fullName.setValue(inputFullname);
        userEmail.setValue(inputEmail);
        usecCurrentAddress.setValue(inputCurrentAddress);
        userPermanentAddress.setValue(inputPermanentAddress);
        submitButton.scrollTo().click();
        String name = resultName.getText().substring(resultName.getText().indexOf(":") + 1);
        String email = resultEmail.getText().substring(resultEmail.getText().indexOf(":") + 1);
        String currentAddress = resultCurrentAddress.getText().substring(resultCurrentAddress.getText().indexOf(":") + 1);
        String permanentAddress = resultPermanentAddress.getText().substring(resultPermanentAddress.getText().indexOf(":") + 1);
        return String.format("%s %s %s %s", name, email, currentAddress, permanentAddress);
    }
}

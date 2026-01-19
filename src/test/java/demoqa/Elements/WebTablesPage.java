package demoqa.Elements;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;

public class WebTablesPage {

    private final SelenideElement firstNameModal = $x("//input[@id='firstName']");
    private final SelenideElement submitButton = $x("//button[@id='submit']");
    private final ElementsCollection rows = $$x("//div[@class='rt-tr-group']//div[@role='row' and not(contains(@class, '-padRow'))]");
    private final SelenideElement wait = $x("//div[@class='modal-content']");


    public String selectEditButton(String firstName, String newName) {
        int oldSize = rows.size();
        getEditButton(firstName).scrollTo().click();
        wait.shouldBe(Condition.visible);

        editName(newName);
        submitButton.click();
        int newSize = rows.size();
        if (oldSize == newSize && searchNewName(newName)) {
                return newName;
        }
        return firstName;
    }

    private SelenideElement getEditButton(String name) {
        return $x("//div[text()='" + name + "']/ancestor::div[@role='row']//span[@title='Edit']");
    }

    private void editName(String newName) {
        firstNameModal.setValue(newName);
    }

    private boolean searchNewName(String newName) {
        SelenideElement searchNewName = $x("//div[@class='rt-tr-group'][.//div[text()='" + newName + "']]");
        return searchNewName.getText().contains(newName);
    }

}

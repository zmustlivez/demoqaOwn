package core;

import demoqa.Elements.ButtonsPage;
import demoqa.Elements.RadioButtonPage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ElementsTest extends BaseTest {


    @Test
    public void checkTextBox() {
        String fullName = "Vasya";
        String email = "aa@bb.cc";
        String currentAddress = "123456 Current address";
        String permanentAddress = "123456 Permanent address";
        Assertions.assertEquals(fullName + " " + email + " " + currentAddress + " " + permanentAddress,mainPage
                        .selectElements()
                        .openTextBox()
                        .resultInputTextBox(fullName, email, currentAddress, permanentAddress)
                );
    }

    @Test
    public void checkCheckBox() {
        String checkString = """
                You have selected :
                home
                desktop
                notes
                commands
                documents
                workspace
                react
                angular
                veu
                office
                public
                private
                classified
                general
                downloads
                wordFile
                excelFile""";
        Assertions.assertTrue(mainPage
                .selectElements()
                .openCheckBox()
                .markHome().contains(checkString));
    }

    @Test
    public void checkRadioButton() {
        RadioButtonPage radioButtonPage = mainPage.selectElements().openRadioButton();
        Assertions.assertEquals("Yes", radioButtonPage.markYesRadio());

        Assertions.assertEquals("Impressive", radioButtonPage.markImpressiveRadio());
    }

    @Test
    public void checkWebTables() {
        String firstName = "Alden";
        String newName = "New_Alden";
        Assertions.assertEquals(newName, mainPage.selectElements().openWebTables().selectEditButton(firstName, newName));
    }

    @Test
    public void checkClicks(){
        ButtonsPage buttonsPage = mainPage.selectElements().openButtons();
        Assertions.assertEquals("You have done a double click", buttonsPage.doubleClickButton());
        Assertions.assertEquals("You have done a right click", buttonsPage.rightClickButton());
        Assertions.assertEquals("You have done a dynamic click", buttonsPage.clickMeButton());
    }
}

package com.trimble.test.pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.List;

/**
 * Page which takes care of login page elements and actions it
 */
public class LoginPage {

    public static AppiumDriver driver;


    public LoginPage(AppiumDriver drv) {
        driver = drv;
    }

    /**
     * Fill the login page with User credentials.
     * For now, hardcoding them, otherwise we should be loading it from a data file(json??)
     */
    public void inputUserCredentials() {
        List<WebElement> inputFlds = driver.findElements(By.className("android.view.View"));
        System.out.println("inputFlds is null=" + (inputFlds == null) + ", size=" + ((inputFlds != null) ? inputFlds.size() : 0));
        inputFlds.get(0).sendKeys("VenkataNutalapati");//Name
        inputFlds.get(1).sendKeys("Abcd@1234");         //Password
        inputFlds.get(2).sendKeys("45");                //age

        WebElement submitBtn = driver.findElement(By.className("android.widget.Button"));

        submitBtn.click();
    }

    public void validateLoginMessage() {
        //android.widget.TextView
        WebElement salutation = driver.findElement(By.className("android.widget.TextView"));
        Assert.assertEquals(salutation.getText().toString(), "welcome VenkataNutalapati");
    }
}

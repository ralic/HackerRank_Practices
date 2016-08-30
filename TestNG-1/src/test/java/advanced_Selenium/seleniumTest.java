package advanced_Selenium;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;

/**
 * Created by raliclo on 7/17/16. Project Name : TestNG-1
 */
public class seleniumTest {

    public WebDriver driver;

    // Information Reference :  https://gist.github.com/huangzhichong/3284966
    @Test
    public void main() {
        // Find the element that's ID attribute is 'account'(My Account)
        driver.findElement(By.id("lst-ib")).click();
        driver.findElement(By.id("lst-ib")).sendKeys("TestNG");
        driver.findElement(By.id("lst-ib")).sendKeys(Keys.RETURN);

        System.out.println();
        System.out.println("[Info] List out all Elements founded~!");
        System.out.println("--------------------------------------");
        List<WebElement> elist = driver.findElements(By.cssSelector("#rso > div > div:nth-child(1) > div > h3 > a"));
        System.out.println("--------------------------------------");
        System.out.println("Elements Amounts : " + elist.size());
        System.out.println("Element href attribute: " + elist.get(0).getAttribute("href"));
        System.out.println();

        // Click Google Rank #1's link
        String one = elist.get(0).getAttribute("href");
        driver.get(one);

        System.out.println();
        System.out.println("[Info] You shall be searching TestNG on google using Firefox or Chrome~!");
        System.out.println();
    }

    @BeforeMethod
    public void beforeMethod() {
        // Create a new instance of the Firefox driver
        driver = new FirefoxDriver();
        //Create a new instance of the Chrome Driver
        //[Note]Additional Setting Required for Chrome to work on Netbeans
        //      It's ok on IntelliJ or ok to use Firefox.
        //[Requirement] npm install -g chromedriver
//        System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");
//        driver = new ChromeDriver();
        //Put a Implicit wait, this means that any search for elements on the page could take the time the implicit wait is set for before throwing exception
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        //Launch the Google Search Website
        driver.get("https://www.google.com/");
    }

    @AfterMethod
    public void afterMethod() {
        // Close the driver
        driver.quit();
    }

}

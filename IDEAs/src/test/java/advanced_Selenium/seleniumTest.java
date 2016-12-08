package advanced_Selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by raliclo on 7/17/16. Project Name : TestNG-1
 */
public class seleniumTest {

    // Information Reference :  https://gist.github.com/huangzhichong/3284966
    @Test
    public void testChromeDriver() {
        WebDriver driver;

        /*  Chrome Driver Setup
            [Requirement] npm install -g chromedriver
            http://chromedriver.storage.googleapis.com/index.html?path=2.24/
            https://github.com/SeleniumHQ/selenium/wiki/ChromeDriver

            System.setProperty("webdriver.chrome.driver",
                    "/usr/local/bin/chromedriver");
        */


        driver = new ChromeDriver();
        // Put a Implicit wait, this means that any search for elements on the page
        // could take the time the implicit wait is set for before throwing exception
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

        // Launch the Google Search Website
        driver.get("https://www.google.com/");

        // Find the element that's ID attribute is 'account'(My Account)
        driver.findElement(By.id("lst-ib")).click();
        driver.findElement(By.id("lst-ib")).sendKeys("ChromeDriver");
        driver.findElement(By.id("lst-ib")).sendKeys(Keys.RETURN);

        System.out.println();
        System.out.println("[Info] List out all Elements founded~!");
        System.out.println("--------------------------------------");
        List<WebElement> elist = driver.findElements(
                By.cssSelector("#rso > div> div > div:nth-child(1) > div > h3 > a"));
        System.out.println("--------------------------------------");
        System.out.println("Elements Amounts : " + elist.size());
        System.out.println("Element href attribute: " +
                elist.get(0).getAttribute("href"));
        System.out.println();

        // Click Google Rank #1's link
        String one = elist.get(0).getAttribute("href");
        driver.get(one);

        System.out.println();
        System.out.println("[Info] You shall be searching ChromeDriver on google using Chrome~!");
        System.out.println();
        driver.quit();

    }

    @Test
    public void testFirefoxDriver() {
        WebDriver driver;

        /*  Firefox Driver Setup
            [Requirement] npm install -g geckodriver
            http://chromedriver.storage.googleapis.com/index.html?path=2.24/
            https://github.com/SeleniumHQ/selenium/wiki/FireFoxDriver

            System.setProperty("webdriver.gecko.driver",
                    "/usr/local/bin/geckodriver");
        */


        driver = new FirefoxDriver();
        // Put a Implicit wait, this means that any search for elements on the page
        // could take the time the implicit wait is set for before throwing exception
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

        // Launch the Google Search Website
        driver.get("https://www.google.com/");

        // Find the element that's ID attribute is 'account'(My Account)
        driver.findElement(By.id("lst-ib")).click();
        driver.findElement(By.id("lst-ib")).sendKeys("FireFoxDriver");
        driver.findElement(By.id("lst-ib")).sendKeys(Keys.RETURN);

        System.out.println();
        System.out.println("[Info] List out all Elements founded~!");
        System.out.println("--------------------------------------");
        List<WebElement> elist = driver.findElements(
                By.cssSelector("#rso >div>  div > div:nth-child(1) > div > h3 > a"));
        System.out.println("--------------------------------------");
        System.out.println("Elements Amounts : " + elist.size());
        System.out.println("Element href attribute: " + elist.get(0).getAttribute("href"));
        System.out.println();

        // Click Google Rank #1's link
        String one = elist.get(0).getAttribute("href");
        driver.get(one);

        System.out.println();
        System.out.println("[Info] You shall be searching FireFoxDriver on google using FireFox~!");
        System.out.println();
        driver.quit();

    }

    @BeforeMethod
    public void beforeMethod() {

    }

    @AfterMethod
    public void afterMethod() {
        // Close the driver
    }

}

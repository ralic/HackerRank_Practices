package advanced_Selenium;
/**
 * Created by raliclo on 9/20/16.
 * Project Name : TestNG-1
 */

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class phantomjsTest {

    /*
        https://github.com/qa/arquillian-phantom-driver
     */
    @Test
    public void testPhantomJSDriver() throws IOException {
//        String phantomjsPath = Paths.get("").toAbsolutePath() +
//                "/node_modules/phantomjs/bin/phantomjs";
//        File file = new File(phantomjsPath);
//        System.setProperty("phantomjs.binary.path", file.getAbsolutePath());

        WebDriver driver = new PhantomJSDriver();
        driver.get("http://www.google.com");
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        System.out.println(driver.getPageSource());
        driver.get("http://www.nthu.edu.tw");

        driver.findElement(By.id("lst-ib")).click();
        driver.findElement(By.id("lst-ib")).sendKeys("PhantomJS");
        driver.findElement(By.id("lst-ib")).sendKeys(Keys.RETURN);

        System.out.println();
        System.out.println("[Info] List out all Elements founded~!");
        System.out.println("--------------------------------------");
        List<WebElement> elist = driver.findElements(
                By.cssSelector("#rso > div > div:nth-child(1) > div > h3 > a"));
        System.out.println("--------------------------------------");
        System.out.println("Elements Amounts : " + elist.size());
        System.out.println("Element href attribute: " + elist.get(0).getAttribute("href"));
        System.out.println();

        // Click Google Rank #1's link
        String one = elist.get(0).getAttribute("href");
        driver.get(one);

        System.out.println();
        System.out.println("[Info] You shall be searching TestNG on google using PhantomJS~!");
        System.out.println();
    }
}
package advanced_Selenium;
/**
 * Created by raliclo on 9/20/16.
 * Project Name : TestNG-1
 */

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class phantomjsTest {
    WebDriver driver;

    /*
        https://github.com/qa/arquillian-phantom-driver
     */
    @Test
    public void testPhantomJSDriver() throws IOException {
//        String phantomjsPath = Paths.get("").toAbsolutePath() +
//                "/node_modules/phantomjs/bin/phantomjs";
//        File file = new File(phantomjsPath);
//        System.setProperty("phantomjs.binary.path", file.getAbsolutePath());

        driver = new PhantomJSDriver();

        driver.get("http://www.nthu.edu.tw");
        System.out.println(driver.getPageSource());
        System.out.println();

        driver.get("https://www.google.com");
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        System.out.println(driver.getPageSource());
        System.out.println();


        List<WebElement> pages = driver.findElements(By.cssSelector("body"));
        System.out.println(pages);

        /*
            Writing it as screen shot.
         */
        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(srcFile, new File("./selenium-shots/screenshot.png"));

        /*
            Writing it as base64 data.
         */
        String screennBase64data = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
        System.out.println("Base 64 String Length" + screennBase64data.length());
        FileUtils.writeStringToFile(new File("./selenium-shots/image-base-64.txt"), screennBase64data, "UTF-8", true);


        /*
            Search www.life-fund.org and click the link.
         */
        List<WebElement> elist;
        elist = driver.findElements(By.className("lst"));
        elist.get(0).sendKeys("www.life-fund.org");
        savePNG("./selenium-shots/screenshot_life_fund_1.png");

        elist = driver.findElements(By.className("lsb"));
        elist.get(0).click();
        savePNG("./selenium-shots/screenshot_life_fund_2.png");
    }

    public void savePNG(String filename) throws IOException {
        File target = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(target, new File(filename));
    }
}
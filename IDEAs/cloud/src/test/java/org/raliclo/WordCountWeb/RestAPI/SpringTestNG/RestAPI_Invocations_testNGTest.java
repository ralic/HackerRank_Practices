package org.raliclo.WordCountWeb.RestAPI.SpringTestNG;/**
 * Created by raliclo on 9/10/16.
 * Project Name : wcweb
 */

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.raliclo.WordCountWeb.RestAPI.Spring_WordCountSite;
import org.raliclo.WordCountWeb.RestAPI.dataApp.DataAppController;
import org.raliclo.WordCountWeb.RestAPI.dataApp.FreqResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/*
    [Info] Embedded predefined port is 8080, so tests are written for port 8080.
    [Info] The App shall be configured in port 9090 as default or others so tests can be isolated.
    [Info] It takes ~10 secs to initialize a container, so all test is written in the same file currently.
 */
@SpringBootTest(classes = {Spring_WordCountSite.class},
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext
public class RestAPI_Invocations_testNGTest extends AbstractTestNGSpringContextTests {


    int count;
    private DataAppController control = new DataAppController();
    private HashMap<String, FreqResponse> dictionary;
    private WebDriver driver;
    private ResponseEntity<String> entity;
    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeClass
    private void siteUP() throws Exception {
        driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.get("http://localhost:8080/wc?search=b");
        control.run();
        dictionary = control.ans;
        count = 1;
    }

    @AfterClass
    public void afterClass() {
        driver.close();
    }

    /*
       Print Each Entry Test
       - This is not a true test, just for validation of all dictionary in db.
     */
    @Test
    public void test01_printDictionaryTest() throws Exception {
        dictionary.forEach((k, v) -> {
                    System.out.println(k + " " + v.counted + " " + v.called);
                }
        );
    }

    /*
        Invocation Test
     */
    @Test(threadPoolSize = 1, invocationCount = 5, timeOut = 3000)
    public void test02_RestInvocationByTestNG() throws Exception {
        entity = this.restTemplate.getForEntity("/wc?search=b", String.class);
        System.out.println("[Test]Status Code is 200 ?" +
                assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK));

        count++;
        System.out.println("[Test]Search b again = " + count + " 183 ?" +
                assertThat(entity.getBody()).isEqualTo(count + " 183"));

        count++;
        entity = this.restTemplate.getForEntity("/wc?search=b", String.class);
        System.out.println("[Test]Search b again = " + count + " 183 ?" +
                assertThat(entity.getBody()).isEqualTo(count + " 183"));
        int i = 5;
        System.out.println(entity.getBody());
        while (i-- > 0) {
            count++;
            entity = this.restTemplate.getForEntity("/wc?search=b", String.class);
            count++;
            driver.get("http://localhost:8080/wc?search=b");
        }
        System.out.println(entity.getBody());
    }

    /*
           Test for /init and /recount
    */
    @Test(threadPoolSize = 1, invocationCount = 1, timeOut = 3000)
    public void test03_init_Test() throws Exception {
        entity = this.restTemplate.getForEntity("/init", String.class);
        System.out.println("[Test] Does read all db by using /init ?");
        assertThat(entity.getBody()).isEqualTo("Loaded Data from Avro File");
    }

    @Test(threadPoolSize = 1, invocationCount = 1, timeOut = Long.MAX_VALUE)
    public void test04_recount_Test() throws Exception {
        entity = this.restTemplate.getForEntity("/recount", String.class);
        System.out.println("[Test] Does recount works ?");
        assertThat(entity.getBody()).isEqualTo("Recount Completed.");
    }

    /*
        Sample Check Each Entry Test
        - Tried to check all but it takes too long
        - Instead, hashed the input string for sampling test
     */
    @Test(threadPoolSize = 10, invocationCount = 1, timeOut = Long.MAX_VALUE)
    public void test99_checkEachEntryTest() throws Exception {
        System.out.println("[Test] Dictionary Size is " + dictionary.size() +
                "\n[Note] Random Sample Size ~ 200, It may take a long time... please wait ...");
        int sampleSize = 200;
        int[] countEntry = new int[1];
        dictionary.forEach(
                (entryK, entryV) -> {
                    if (!entryK.equals("b") &&
                            entryK.hashCode() % (dictionary.size() / sampleSize + 1) == 0) {
                        driver.get("http://localhost:8080/wc?search=" + entryK);
                        System.out.println("Entry #" + (countEntry[0]++) + " Search:" + entryK);
                        ResponseEntity<String> entity = this.restTemplate.getForEntity("/wc?search=" + entryK, String.class);
                        assertThat(entity.getBody()).isEqualTo(
                                (entryV.called + 2) + " " + entryV.counted);
                    }
                }
        );
    }

}
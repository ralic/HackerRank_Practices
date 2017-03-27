package org.raliclo.WordCountWeb.RestAPI.SpringJunit5;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.raliclo.WordCountWeb.RestAPI.Spring_WordCountSite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Created by raliclo on 9/10/16.
 * Project Name : wcweb
 */

@ExtendWith(SpringExtension.class)

@SpringBootTest(classes = {Spring_WordCountSite.class},
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext
public class Spring_WordCountSite_JunitTest {

    private static WebDriver driver;
    ResponseEntity<String> entity;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeAll
    public static void driverStart() {
        // TODO Need the latest Firefox (v53)
        driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(30L, TimeUnit.SECONDS);
    }

    @AfterAll
    public static void driverClose() {
        driver.close();
    }

    @Test
    public void testRestByJunit() {

        driver.get("http://localhost:8080/wc?search=b");
        entity = this.restTemplate.getForEntity("/wc?search=b", String.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(entity.getBody()).isEqualTo("2 183");
        entity = this.restTemplate.getForEntity("/wc?search=b", String.class);
        assertThat(entity.getBody()).isEqualTo("3 183");
    }


}
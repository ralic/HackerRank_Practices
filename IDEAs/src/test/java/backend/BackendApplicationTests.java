package backend;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.raliclo.Spring_backend.BackendApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BackendApplication.class)
//@TestPropertySource("test/resources")
@WebAppConfiguration
public class BackendApplicationTests {

    @Test
    public void contextLoads() {
    }

}

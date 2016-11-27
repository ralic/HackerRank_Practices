package beginner;


import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class TestNG0 {

    @BeforeClass
    public void setUp() {
        System.out.println("This is setup phase");
    }

    @Test
    public void testAdd() {
        String str = "TestNG is working fine";
        assertEquals("TestNG is working fine", str);
        System.out.println("--> " + str);
    }
}

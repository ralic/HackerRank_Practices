package LeapMotion;


import com.leapmotion.leap.Controller;
import com.leapmotion.leap.DeviceList;


/**
 * Created by raliclo on 04/12/2016.
 * Project Name : IDEAs
 */

public class leapTest {

    static long speedX;

    public static void main(String[] args) {

        // Timer for Speed Test
        speedX = System.currentTimeMillis();
        // Code Starts here
        Controller controller = new Controller();
        DeviceList connectedLeaps = controller.devices();
        System.out.println("Controller" + controller);
        System.out.println("ConnectedLeaps" + connectedLeaps);
        // Keep this process running until Enter is pressed
        System.out.println("Sucessful Loading.");
//        try {
//            System.in.read();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        // Speed Testing
        System.out.println("Time spent :" + (System.currentTimeMillis() - speedX) + "msec");


    }

}

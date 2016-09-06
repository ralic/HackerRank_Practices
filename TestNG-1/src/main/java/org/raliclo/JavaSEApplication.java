/*
 * Copyright 2016 Ralic Lo<raliclo@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

/*
 * Info: Name=Lo,WeiShun 
 * Author: raliclo
 * Filename: Main.java
 * Date and Time: Jul 17, 2016 1:05:23 AM
 * Project Name: TestNG-1
 */

package org.raliclo;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
//import java.util.logging.Logger;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.*;

class JavaSEApplication {

//    static final Logger logger = LogManager.getLogger(JavaSEApplication.class.getName());
//    public static void doIt() {
//        logger.error("<msg> Crafted Error");
//    }

    public static void main(String[] args) {
//        doIt();
        // TODO code application logic here
        System.out.println("------Begin of Java SE application------");
        System.out.println("Printing out all network interfaces");
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    System.out.println(intf.getDisplayName() + " - "
                            + inetAddress.getHostAddress());
                }
            }
        } catch (SocketException ex) {
//            Logger.getLogger(JavaSEApplication.class.getName()).log(Level.SEVERE, null, ex);
//            logger.error("[Error] Error is " + ex);
        }

        System.out.println("------ End  of Java SE application------");

    }

}

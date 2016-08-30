/*
 * Info: Name=Lo,WeiShun 
 * Author: raliclo
 * Filename: Tester.java
 * Date and Time: Jul 16, 2016 10:42:38 PM
 * Project Name: Tester
 */
 /*
 * Copyright 2016 raliclo.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tester;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author raliclo
 */
public class Tester {

    public static String read(InputStream input) throws IOException {
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(input))) {
            return buffer.lines().collect(Collectors.joining("\n"));
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
        //        String[] arg = {"1", "2", "3", "4", "10", "11"};
        //        Scanner s = new Scanner(System.in);
//        try {
//            BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
//            String line = buffer.readLine();
//            String line2 = buffer.readLine();
//            args = line2.split(" ");
//            int ans = 0;
//            for (int i = 0; i < args.length; i++) {
//                ans += Integer.parseInt(args[i]);
//            }
//            System.out.println("sum" + ans);
//        } catch (Exception ex) {
//        }

        System.out.println("Jave Home:" + System.getProperty("java.home"));
        System.out.println(System.getProperty("java.net.preferIPv6Addresses"));
        System.setProperty("java.net.preferIPv6Addresses", "true");
        System.out.println(System.getProperty("java.net.preferIPv6Addresses"));

        try {
            BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter host name: ");
            String hostname = buffer.readLine();
            InetAddress[] addr = InetAddress.getAllByName(hostname);
            System.out.println("\nInternet Address for hostname:" + hostname + ",length" + addr.length);
            if (hostname == null) {
                hostname = "localhost";
            }
            for (InetAddress address : addr) {
                System.out.println(address);
                if (address instanceof Inet6Address) {
                    System.out.println("\nipv6 address is " + address.getHostAddress());
                } else {
                    System.out.println("\nipv4 address is " + address.getHostAddress());
                }
            }
        } catch (Exception ex) {
        }
    }
}

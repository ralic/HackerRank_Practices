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
package org.raliclo.RobotHand;/**
 * Created by raliclo on 9/15/16.
 * Project Name : TestNG-1
 */

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;

;

public class RobotHand {
    HashMap<Character, Integer> dict = new HashMap<>();

    RobotHand() {
        dict.put('0', KeyEvent.VK_0);
        dict.put('1', KeyEvent.VK_1);
        dict.put('2', KeyEvent.VK_2);
        dict.put('3', KeyEvent.VK_3);
        dict.put('4', KeyEvent.VK_4);
        dict.put('5', KeyEvent.VK_5);
        dict.put('6', KeyEvent.VK_6);
        dict.put('7', KeyEvent.VK_7);
        dict.put('8', KeyEvent.VK_8);
        dict.put('9', KeyEvent.VK_9);
    }

    public void robotSave(String xx) throws InterruptedException, AWTException {
        Robot robots = new Robot();
        Thread.sleep(4000);
        robots.keyPress(KeyEvent.VK_META);
        Thread.sleep(100);
        robots.keyRelease(KeyEvent.VK_META);
        robots.keyRelease(KeyEvent.VK_S);
        Thread.sleep(1000);
        robots.keyPress(KeyEvent.VK_LEFT);
        robots.keyRelease(KeyEvent.VK_LEFT);
        Thread.sleep(100);
        robots.keyPress(KeyEvent.VK_ENTER);
        robots.keyRelease(KeyEvent.VK_ENTER);
        Thread.sleep(3000);
        robots.keyPress(KeyEvent.VK_META);
        robots.keyPress(KeyEvent.VK_W);
        robots.keyRelease(KeyEvent.VK_W);
        robots.keyRelease(KeyEvent.VK_META);

    }

    public void clickNumbers(String x) throws AWTException, InterruptedException {
        int i;
        int n = x.length();
        for (i = 0; i < n; i++) {
            System.out.println(x.charAt(i));
            simuPress(x.charAt(i));
        }
    }

    public void simuPress(char c) throws AWTException, InterruptedException {
        Robot robotclick = new Robot();
        Thread.sleep(100);
        keyPR(c);
    }

    public void keyPR(char c) {
        if (dict.containsKey(c)) {
            Thread tmp = new Thread(() -> {
                try {
                    Robot robots = new Robot();
                    robots.keyPress(dict.get(c));
                    robots.keyRelease(dict.get(c));
                } catch (AWTException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}

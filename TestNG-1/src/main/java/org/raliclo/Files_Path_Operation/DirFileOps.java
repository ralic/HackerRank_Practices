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

package org.raliclo.Files_Path_Operation;/**
 * Created by raliclo on 9/9/16.
 * Project Name : TestNG-1
 */

import java.io.IOException;
import java.nio.file.*;

public class DirFileOps {
    public static DirectoryStream<Path> exec(Path dir, RunCommand cmd) {

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path file : stream) {
                if (file.toFile().isFile()) {
                    System.out.print("[File] ");
                    Object x = cmd.runexec(cmd.command + " " + file.toFile());
                    System.out.println(x);
                    x = null;
                }
            }
            return stream;
        } catch (IOException | DirectoryIteratorException ex) {
            System.err.println(ex);
        }
        return null;
    }

    public static void main(String args[]) {
        DirFileOps.exec(Paths.get(""), new RunCommand("ls"));
    }
}
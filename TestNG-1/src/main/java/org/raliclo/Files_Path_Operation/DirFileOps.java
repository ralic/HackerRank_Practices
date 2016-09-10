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
package org.raliclo.Files_Path_Operation;/**
 * Created by raliclo on 9/9/16.
 * Project Name : TestNG-1
 */

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

import static org.raliclo.WordCountWeb.src.WordCountAvro.getFileTree;

public class FileVisitorExample {

    public static void main(String[] args) {
        Path currentPath = Paths.get("");
        DirectoryStream<Path> fileTree = getFileTree(currentPath);
        ProcessFile fileOps = new ProcessFile();
        try {
            Files.walkFileTree(currentPath, fileOps);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Path fileInTree : fileTree) {
            if (fileInTree.toFile().isFile()) {
                System.out.println("[File]" + fileInTree.toFile());
            }
            if (fileInTree.toFile().isDirectory()) {
                System.out.println("[Directory]" + fileInTree.toFile());
            }
        }
    }

    private static final class ProcessFile extends SimpleFileVisitor<Path> {
        @Override
        public FileVisitResult visitFile(
                Path aFile, BasicFileAttributes aAttrs
        ) throws IOException {
            System.out.println("WalkingTo file     : " + aFile);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult preVisitDirectory(
                Path aDir, BasicFileAttributes aAttrs
        ) throws IOException {
            System.out.println("WalkingTo directory: " + aDir);
            return FileVisitResult.CONTINUE;
        }
    }
}

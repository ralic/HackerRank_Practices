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
import java.nio.file.attribute.BasicFileAttributes;

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

    /*
        getFileTree is to generate a fileList as Iterable stream.
     */
    public static DirectoryStream<Path> getFileTree(Path dir) {
        try {
            DirectoryStream<Path> stream = Files.newDirectoryStream(dir);
            return stream;
        } catch (Exception ex) {
            System.err.println(ex);
        }
        return null;
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

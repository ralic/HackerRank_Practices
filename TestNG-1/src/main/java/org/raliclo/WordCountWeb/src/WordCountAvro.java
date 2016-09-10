package org.raliclo.WordCountWeb.src;


import javafx.util.Pair;
import org.apache.avro.Schema;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumWriter;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by raliclo on 9/9/16.
 * Project Name : TestNG-1
 */

public class WordCountAvro {
    final private static int cpus = Runtime.getRuntime().availableProcessors();
    final private static long minMemory = 1024000;
    final private static Comparator<Pair<Path, Long>> sorter = (a, b) -> a.getValue().compareTo(b.getValue());
    final private static ArrayList<Pair<Path, Long>> fileList = new ArrayList();

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(cpus * 10);

        // Avro Setup
        Path avroDir = Paths.get("/Users/raliclo/work/@Netbeans/HackerRank_Practices/TestNG-1/src/main/java/org/raliclo/WordCountWeb/avroModel");
        File avroFile = new File(avroDir + "/wordCounts.avro");
        Schema avroSchema = new Schema.Parser().parse(new File(avroDir + "/wordCounts.avsc"));
        DatumWriter<GenericRecord> recordWriter = new GenericDatumWriter<>(avroSchema);
        DataFileWriter<GenericRecord> avroFileWriter = new DataFileWriter<>(recordWriter);
        avroFileWriter.create(avroSchema, avroFile);

        // FileTree Processing
        Path currentPath = Paths.get("/Users/raliclo/work/@Netbeans/HackerRank_Practices/TestNG-1/src/main/java/org/raliclo/WordCountWeb/files");
        DirectoryStream<Path> fileTree = getFileTree(currentPath);

        // Update Dictionary Index
        final SortedMap<String, BigInteger> finalIndex = new TreeMap<>();
        int[] filecounts = new int[1];
        for (Path fileInTree : fileTree) {
            Runnable worker = () -> {
                try {
                    /* For Printing Thread Counts
                    System.out.println(Thread.currentThread().getName() + " Start:" + filecounts[0]++);
                    */
                    if (memcheck()) {
                        finalIndexUpdate(fileInTree, finalIndex);
                    } else {
                        fileList.add(new Pair(fileInTree, fileInTree.toFile().length()));
                    }
                } catch (Exception ex) {
                }
            };
            new Thread(worker).run();
        }

        // Clean queue;

        /* For MultiThread Support
        1. Sort the filelist to get smallest file first.
        2. Clean up files in Queue if Memory is sufficient
        3. CPU resource maybe another factor for longer task
        System.out.println(SystemInfo.tops());
         */
        fileList.sort(sorter);
        while (fileList.size() != 0) {
            if (memcheck()) {
                Runnable queworker = () -> {
                    Path nextFile = fileList.remove(fileList.size()).getKey();
                    try {
                        finalIndexUpdate(nextFile, finalIndex);
                    } catch (Exception ex) {
                    }
                };
                new Thread(queworker).run();
            }
            Thread.currentThread().wait(1);
        }

        /* AvroFile Storage*/
        avroPut(finalIndex, avroFileWriter, avroSchema);
        System.out.println("All Files' wordcount data consolited in \n[File] " + avroFile);
        avroFileWriter.close();

        /* For Evaluation or Debug
        System.out.println(fileList);
        finalIndex.forEach((k, v) -> System.out.println(k + " " + v));
        */
    }

    public static DirectoryStream<Path> getFileTree(Path dir) {
        try {
            DirectoryStream<Path> stream = Files.newDirectoryStream(dir);
            return stream;
        } catch (Exception ex) {
            System.err.println(ex);
        }
        return null;
    }

    private static boolean memcheck() throws IOException {
        return Runtime.getRuntime().freeMemory() > minMemory ? true : false;
    }

    private static void avroPut(SortedMap<String, BigInteger> finalIndex,
                                DataFileWriter<GenericRecord> avroFileWriter,
                                Schema avroSchema) {
        finalIndex.forEach(
                (String k, BigInteger v) -> {
                    GenericRecord avroRecord = new GenericData.Record(avroSchema);
                    avroRecord.put("word", k);
                    avroRecord.put("count", v);
                    try {
                        avroFileWriter.append(avroRecord);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    private static HashMap<String, BigInteger> wordcountForPath(Path fileInTree) throws IOException {
        List<String> list = new ArrayList<>();
        HashMap<String, BigInteger> dictMap = new HashMap();
        try (BufferedReader br = Files.newBufferedReader(fileInTree)) {
            br.lines().forEach((m1) -> {
                        //{} /,._|~"`'?!*-\[\]()
                        String[] words = m1.split("[\\W {} /,._|~\"`'?!*-\\[\\]()]");
                        Arrays.stream(words).forEach((m2) -> {
                            if (dictMap.containsKey(m2)) {
                                dictMap.put(m2, dictMap.get(m2).add(BigInteger.ONE));
                            } else {
                                dictMap.put(m2, BigInteger.ONE);
                            }
                        });
                    }
            );
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dictMap;
    }

    public static void finalIndexUpdate(Path fileInTree, SortedMap<String, BigInteger> finalIndex) throws IOException {
        if (fileInTree.toFile().isFile() && !fileInTree.getFileName().toString().equals(".DS_Store")) {
            HashMap<String, BigInteger> localIndex = wordcountForPath(fileInTree);
            localIndex.forEach((k1, v1) -> {
//                System.out.println(k1 + " " + v1);
                if (k1.length() != 0) {
                    if (finalIndex.containsKey(k1)) {
                        finalIndex.put(k1, finalIndex.get(k1).add(v1));
                    } else {
                        finalIndex.put(k1, v1);
                    }
                }
            });
        }
    }
}

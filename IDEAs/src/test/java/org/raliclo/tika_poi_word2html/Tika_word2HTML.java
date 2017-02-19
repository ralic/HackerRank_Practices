package org.raliclo.tika_poi_word2html;/**
 * Created by raliclo on 30/11/2016.
 * Project Name : IDEAs
 */

import org.apache.tika.Tika;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.testng.annotations.Test;

import java.io.*;
import java.nio.file.Paths;

public class Tika_word2HTML {

    static long speedX;
    static String prefix = Paths.get("").toAbsolutePath().toString()
            .concat("/src/test/java/org/raliclo/tika_poi_word2html/");
    static String inputpath = prefix + "test.docx";

    @Test
    public static void main(String[] args) throws IOException {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */

        // Timer for Speed Test
        speedX = System.currentTimeMillis();
        // Code Starts here

        System.out.println(parseToStringExample());
        System.out.println(parseExample());

        System.out.println("Conversion Done");
        // Speed Testing
        System.out.println("Time spent :" + (System.currentTimeMillis() - speedX) + "msec");

    }
    //http://tika.apache.org/1.14/examples.html#Parsing_to_XHTML

    public static String parseToStringExample() {
        try {
            Tika tika = new Tika();
            try (InputStream stream = new FileInputStream(inputpath)) {
                System.out.println(stream);
                return tika.parseToString(stream);
            }
        } catch (Exception ex) {
            System.out.println(ex);
            return ex.toString();
        }

    }

    public static String parseExample() throws IOException {

        InputStream input = TikaInputStream.get(Paths.get(inputpath));

        try {
            AutoDetectParser parser = new AutoDetectParser();
            BodyContentHandler handler = new BodyContentHandler();
            Metadata metadata = new Metadata();
            try (InputStream stream = new FileInputStream(inputpath)) {
                parser.parse(stream, handler, metadata);
                return handler.toString();
            }
        } catch (Exception ex) {
            System.out.println(ex);
            return ex.toString();
        }
    }
}

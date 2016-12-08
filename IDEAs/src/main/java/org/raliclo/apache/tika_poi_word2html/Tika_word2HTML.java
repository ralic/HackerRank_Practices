package org.raliclo.apache.tika_poi_word2html;/**
 * Created by raliclo on 30/11/2016.
 * Project Name : IDEAs
 */

import org.apache.tika.Tika;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Tika_word2HTML {

    static long speedX;
    static String prefix = "/Users/raliclo/work/proj/HackerRanks/IDEAs/src/main/java/org/raliclo/apache/tika_poi_word2html/";
    static String inputpath = prefix + "test.docx";

    public static void main(String[] args) throws FileNotFoundException {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */

        // Timer for Speed Test
        speedX = System.currentTimeMillis();
        // Code Starts here

        System.out.println(parseToStringExample());
        System.out.println(parseExample());
        // Speed Testing
        System.out.println("Time spent :" + (System.currentTimeMillis() - speedX) + "msec");


    }
    //http://tika.apache.org/1.14/examples.html#Parsing_to_XHTML

    public static String parseToStringExample() {
        try {
            Tika tika = new Tika();
            try (InputStream stream = new FileInputStream(inputpath)) {
                return tika.parseToString(stream);
            }
        } catch (Exception ex) {
            System.out.println(ex);
            return ex.toString();
        }

    }

    public static String parseExample() throws FileNotFoundException {

        InputStream input = TikaInputStream.get(new File(inputpath));

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

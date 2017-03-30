package org.raliclo.tika_poi_word2html;
// * Created by raliclo on 30/11/2016.
// * Project Name : IDEAs
// */
//

import com.google.common.io.Files;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.ExpandedTitleContentHandler;
import org.testng.annotations.Test;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.file.Paths;

public class Tika_word2HTML_app {

    static long speedX;
    static String prefix = Paths.get("").toAbsolutePath().toString()
            .concat("/src/test/java/org/raliclo/tika_poi_word2html/");
    static String inputpath = prefix + "test.docx";

    @Test
    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */

        // Timer for Speed Test
        speedX = System.currentTimeMillis();
        // Code Starts here

        try {
            byte[] file = Files.toByteArray(new File(inputpath));

            InputStream in = new FileInputStream(new File(inputpath));

            XWPFDocument document = new XWPFDocument(in);
//            XHTMLOptions options = XHTMLOptions.create().URIResolver(
//                    new FileURIResolver(new File(inputpath)));

            OutputStream out = new ByteArrayOutputStream();
            AutoDetectParser tikaParser = new AutoDetectParser();
            InputStream input = TikaInputStream.get(Paths.get(inputpath));

            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            SAXTransformerFactory factory = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
            TransformerHandler handler = factory.newTransformerHandler();
            handler.getTransformer().setOutputProperty(OutputKeys.METHOD, "html");
            handler.getTransformer().setOutputProperty(OutputKeys.INDENT, "yes");
            handler.getTransformer().setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            handler.setResult(new StreamResult(out));
            ExpandedTitleContentHandler handler1 = new ExpandedTitleContentHandler(handler);

            tikaParser.parse(new ByteArrayInputStream(file), handler1, new Metadata());
            System.out.println(new String(bout.toByteArray(), "UTF-8"));
        } catch (Exception ex) {
            System.out.println(ex);
        }

        // Speed Testing
        System.out.println("Time spent :" + (System.currentTimeMillis() - speedX) + "msec");


    }

}



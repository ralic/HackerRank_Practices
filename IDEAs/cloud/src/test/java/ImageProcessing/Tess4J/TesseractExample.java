package ImageProcessing.Tess4J;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract1;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;
import java.nio.file.Paths;


/**
 * Created by raliclo on 23/02/2017.
 */
public class TesseractExample {


    public static void main(String[] args) {

        File imageFile = new File(Paths.get("").toAbsolutePath().toString()
                .concat("/src/test/java/ImageProcessing/Tess4J/eurotext.tif"));
//        ITesseract instance = new Tesseract();  // JNA Interface Mapping
         ITesseract instance = new Tesseract1(); // JNA Direct Mapping

        try {
            String result = instance.doOCR(imageFile);
            System.out.println(result);
        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }
    }
}
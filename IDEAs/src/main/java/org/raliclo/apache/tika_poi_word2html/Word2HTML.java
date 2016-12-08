//package org.raliclo.apache.tika_poi_word2html;/**
// * Created by raliclo on 30/11/2016.
// * Project Name : IDEAs
// */
//
//import org.apache.poi.
//import org.apache.poi.hwpf.converter.WordToHtmlConverter;
//import org.apache.poi.hwpf.usermodel.Picture;
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//
//import java.util.Base64;
//
//public class Word2HTML {
//
//    static long speedX;
//
//
//    public static void main(String[] args) {
//        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
//
//        // Timer for Speed Test
//        speedX = System.currentTimeMillis();
//        // Code Starts here
//
//        // Speed Testing
//        System.out.println("Time spent :" + (System.currentTimeMillis() - speedX) + "msec");
//
//
//    }
//
//}
//
//public class InlineImageWordToHtmlConverter extends WordToHtmlConverter {
//
//    public InlineImageWordToHtmlConverter(Document document) {
//        super(document);
//    }
//
//    @Override
//    protected void processImageWithoutPicturesManager(Element currentBlock,
//                                                      boolean inlined, Picture picture) {
//        Element imgNode = currentBlock.getOwnerDocument().createElement("img");
//        StringBuilder sb = new StringBuilder();
//        sb.append(Base64.getMimeEncoder().encodeToString(picture.getRawContent()));
//        sb.insert(0, "data:" + picture.getMimeType() + ";base64,");
//        imgNode.setAttribute("src", sb.toString());
//        currentBlock.appendChild(imgNode);
//    }
//
//}

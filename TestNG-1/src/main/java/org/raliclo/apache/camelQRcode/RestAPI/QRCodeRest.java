package org.raliclo.apache.camelQRcode.RestAPI;

import com.google.zxing.EncodeHintType;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.camel.spi.DataFormat;
import org.raliclo.apache.camelQRcode.ImageType;
import org.raliclo.apache.camelQRcode.QRCodeDataFormat;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.EnumMap;
import java.util.Map;

/**
 * Created by raliclo on 9/15/16.
 * Project Name : TestNG-1
 */

@SpringBootApplication
public class QRCodeRest {

    public static void main(String[] args) {
        SpringApplication.run(QRCodeRest.class, args);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/qrcode")
    public void qrRest(String url) {
        DataFormat qrformat0 = new QRCodeDataFormat(480, 480, ImageType.PNG, false);
        // create qr-code image
        Map<EncodeHintType, ErrorCorrectionLevel> hintMap = new EnumMap<EncodeHintType, ErrorCorrectionLevel>(EncodeHintType.class);
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);


    }

}

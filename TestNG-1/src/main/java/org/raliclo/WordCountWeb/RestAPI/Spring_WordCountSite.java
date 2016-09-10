package org.raliclo.WordCountWeb.RestAPI;/**
 * Created by raliclo on 9/10/16.
 * Project Name : TestNG-1
 */


import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileStream;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;


@SpringBootApplication
public class Spring_WordCountSite {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(Spring_WordCountSite.class, args);

         /*
         * Data Source
         * */
        String avroDir = "/Users/raliclo/work/@Netbeans/HackerRank_Practices/TestNG-1/src/main/java/org/raliclo/WordCountWeb/avroModel";
        File avroFile = new File(avroDir + "/wordCounts.avro");
        Schema avroSchema = new Schema.Parser().parse(new File(avroDir + "/wordCounts.avsc"));
        DatumReader<GenericRecord> datumReader = new GenericDatumReader<>(avroSchema);
        DataFileReader<GenericRecord> avroFileReader = new DataFileReader<>(avroFile, datumReader);
        DataFileStream x = null;


        // Deserialize users from disk
        GenericRecord avroRecord = null;
        while (avroFileReader.hasNext()) {
            // Reuse user object by passing it to next(). This saves us from
            // allocating and garbage collecting many objects for files with
            // many items.
            avroRecord = avroFileReader.next(avroRecord);
        }
    }


}

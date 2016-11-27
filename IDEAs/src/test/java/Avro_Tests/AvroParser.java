package Avro_Tests;/**
 * Created by raliclo on 8/26/16.
 * Project Name : TestNG-1
 */

import org.apache.avro.Schema;
import org.apache.avro.Schema.Parser;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;


public class AvroParser {

    public static void main(String[] args) throws IOException {

        // Test Schema methods
        String path = "/Users/raliclo/work/@Netbeans/HackerRank_Practices/TestNG-1/src/test/java/Avro_Tests/";
        File f = new File(path + "user.avsc");
        Parser p = new Parser();
        Schema x = p.parse(f);
        System.out.println(x);
        System.out.println(x.getFields());

        // Creating users and stored in avro
        File file = new File(path + "users.avro");
        Schema schema = new Schema.Parser().parse(new File(path + "user.avsc"));
        GenericRecord user1 = new GenericData.Record(schema);
        user1.put("name", "Alyssa");
        user1.put("favorite_number", 256);
        GenericRecord user2 = new GenericData.Record(schema);
        user2.put("name", "Ben");
        user2.put("favorite_number", 7);
        user2.put("favorite_color", "red");
        DatumWriter<GenericRecord> datumWriter = new GenericDatumWriter<>(schema);
        DataFileWriter<GenericRecord> dataFileWriter = new DataFileWriter<>(datumWriter);
        dataFileWriter.create(schema, file);
        dataFileWriter.append(user1);
        dataFileWriter.append(user2);
        dataFileWriter.close();

        // Deserialize users from disk
        DatumReader<GenericRecord> datumReader = new GenericDatumReader<>(schema);
        DataFileReader<GenericRecord> dataFileReader = new DataFileReader<>(file, datumReader);
        GenericRecord user = null;
        while (dataFileReader.hasNext()) {
            // Reuse user object by passing it to next(). This saves us from
            // allocating and garbage collecting many objects for files with
            // many items.
            user = dataFileReader.next(user);
            System.out.println(user);
        }

        // Read Avro File without Schema.
        File f2 = new File(path + "users.avro");
        FileReader rd = new FileReader(f2);
        BufferedReader reader = new BufferedReader(rd);
        ArrayList<String> input = reader.lines().collect(Collectors.toCollection(ArrayList::new));
        reader.close();
        System.out.println(input);

    }
}


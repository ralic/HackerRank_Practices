package org.raliclo.WordCountWeb.RestAPI.dataApp;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileStream;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.raliclo.WordCountWeb.RestAPI.impl.WordCountDBServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.HashMap;

/**
 * Created by raliclo on 9/10/16.
 * Project Name : TestNG-1
 */

@RestController
public class DataAppController {
    HashMap<String, FreqResponse> ans = new HashMap<>();

    @Autowired
    WordCountDBServiceImpl wordCountDBService;

    @RequestMapping("/init")
    public HashMap<String, FreqResponse> run(String... args) throws Exception {
        /*
         * Data Source
         * */
        String avroDir = "/Users/raliclo/work/@Netbeans/HackerRank_Practices/TestNG-1/src/main/java/org/raliclo/WordCountWeb/avroModel";
        File avroFile = new File(avroDir + "/wordCounts.avro");
        Schema avroSchema = new Schema.Parser().parse(new File(avroDir + "/wordCounts.avsc"));
        DatumReader<GenericRecord> datumReader = new GenericDatumReader<>(avroSchema);
        DataFileReader<GenericRecord> avroFileReader = new DataFileReader<>(avroFile, datumReader);
        DataFileStream x = null;

        /* Deserialize users from disk and load into DB.
           Note : It has to be after auto configuration is done.
         */
        GenericRecord avroRecord = null;

        while (avroFileReader.hasNext()) {
            // Reuse user object by passing it to next(). This saves us from
            // allocating and garbage collecting many objects for files with
            // many items.
            avroRecord = avroFileReader.next(avroRecord);

            /* This is to ensure that all word count data were properly written and read
                WordCountDB entity = new WordCountDB();
                entity.setWord(avroRecord.get("word").toString());
                entity.setCounts((Long) avroRecord.get("counts"));
                entity.setCreated(new Date());
                entity.setWordCountDBId(new Random().nextLong());
                TODO MySQL Database Persistence
                System.out.println(avroRecord);
                wordCountDBService.save(entity);
            */
            FreqResponse freq = new FreqResponse((long) 0, (Long) avroRecord.get("counts"));
            ans.put(avroRecord.get("word").toString(), freq);
        }
        return ans;
    }

    /*
        Get Data from Data Source
        1. if ans is not loaded, load it first
        2. if searched word is in HashMap, return Frequency Response
     */
    @RequestMapping(value = "/wc", method = RequestMethod.GET)
    public String addWordCountDB(
            @RequestParam(value = "search", required = false) String search) throws Exception {
        if (ans.size() == 0) {
            run();
        }
        System.out.println("[Info] Loaded DB in HashMap, size= " + ans.size());
        return ans.containsKey(search) ?
                (ans.get(search).called + " " + ans.get(search).counted) :
                (search + " is not in these files");
    }
}

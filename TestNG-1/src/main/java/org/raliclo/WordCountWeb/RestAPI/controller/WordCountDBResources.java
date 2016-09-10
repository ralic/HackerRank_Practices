package org.raliclo.WordCountWeb.RestAPI.controller;/**
 * Created by raliclo on 9/10/16.
 * Project Name : TestNG-1
 */

import org.raliclo.WordCountWeb.RestAPI.model.WordCountDB;
import org.raliclo.WordCountWeb.RestAPI.service.WordCountDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest")
public class WordCountDBResources {


    @Autowired
    private WordCountDBService wordCountDBService;

    // UPPDATE
    @RequestMapping(value = "/wordcountdb/save", method = RequestMethod.POST)
    public WordCountDB addWordCountDB(@RequestBody WordCountDB wordCountDB) {
        return wordCountDBService.save(wordCountDB);
    }
}
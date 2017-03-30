package org.raliclo.WordCountWeb.RestAPI.controller;/**
 * Created by raliclo on 9/10/16.
 * Project Name : TestNG-1
 */

import org.raliclo.WordCountWeb.RestAPI.model.WordCountDB;
import org.raliclo.WordCountWeb.RestAPI.service.WordCountDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/wordcount")
public class WordCountDBController {

    @Autowired
    private WordCountDBService wordCountDBService;

    @RequestMapping("/countall")
    public List<WordCountDB> getAllWordCountDBs() {
        return wordCountDBService.findAll();
    }

}

package org.raliclo.WordCountWeb.RestAPI.service;/**
 * Created by raliclo on 9/10/16.
 * Project Name : TestNG-1
 */

import org.raliclo.WordCountWeb.RestAPI.model.WordCountDB;

import java.math.BigInteger;
import java.util.List;

public interface WordCountDBService {
    WordCountDB save(WordCountDB photo);

    List<WordCountDB> findByWordCountDB(WordCountDB wordCountDB);

    WordCountDB findByWordCountDBId(BigInteger wordCountDBId);

    List<WordCountDB> findAll();
}

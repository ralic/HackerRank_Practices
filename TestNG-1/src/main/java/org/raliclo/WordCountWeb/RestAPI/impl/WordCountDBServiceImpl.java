package org.raliclo.WordCountWeb.RestAPI.impl;

import org.raliclo.WordCountWeb.RestAPI.dao.WordCountDBDao;
import org.raliclo.WordCountWeb.RestAPI.model.WordCountDB;
import org.raliclo.WordCountWeb.RestAPI.service.WordCountDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class WordCountDBServiceImpl implements WordCountDBService {

    @Autowired
    private WordCountDBDao wordCountDBDao;

    public WordCountDB save(WordCountDB wordCountDB) {
        return wordCountDBDao.save(wordCountDB);
    }

    @Override
    public List<WordCountDB> findByWordCountDB(WordCountDB wordCountDBDao) {
        return null;
    }

    public WordCountDB findByWordCountDBId(BigInteger wordCountDBId) {
        return wordCountDBDao.findByWordCountDBId(wordCountDBId);
    }

    public List<WordCountDB> findAll() {
        return wordCountDBDao.findAll();
    }
}

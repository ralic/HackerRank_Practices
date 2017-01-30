package org.raliclo.WordCountWeb.RestAPI.dao;/**
 * Created by raliclo on 9/10/16.
 * Project Name : TestNG-1
 */

import org.raliclo.WordCountWeb.RestAPI.model.WordCountDB;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface WordCountDBDao extends CrudRepository<WordCountDB, Long> {
    WordCountDB save(WordCountDB wordCountDB);

    WordCountDB findByWordCountDBId(Long wordCountDBId);

    List<WordCountDB> findAll();

}

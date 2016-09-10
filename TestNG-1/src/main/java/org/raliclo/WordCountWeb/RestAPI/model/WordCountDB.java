package org.raliclo.WordCountWeb.RestAPI.model;/**
 * Created by raliclo on 9/10/16.
 * Project Name : TestNG-1
 */

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigInteger;
import java.util.Date;


@Entity
public class WordCountDB {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String word;
    private BigInteger count;
    private BigInteger wordCountDBId;
    @CreationTimestamp
    private Date created;

    public BigInteger getCount() {
        return count;
    }

    public void setCount(BigInteger count) {
        this.count = count;
    }

    public BigInteger getWordCountDBId() {
        return wordCountDBId;
    }

    public void setWordCountDBId(BigInteger wordCountDBId) {
        this.wordCountDBId = wordCountDBId;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

}

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
    private BigInteger wordcountId;

    @CreationTimestamp
    private Date created;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public BigInteger getCount() {
        return count;
    }

    public void setCount(BigInteger count) {
        this.count = count;
    }

    public BigInteger findByWordCountDBid() {
        return wordcountId;
    }

    public void setWordcountId(BigInteger wordcountId) {
        this.wordcountId = wordcountId;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

}

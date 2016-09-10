package org.raliclo.WordCountWeb.RestAPI.model;/**
 * Created by raliclo on 9/10/16.
 * Project Name : TestNG-1
 */

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;


@Entity
public class WordCountDB {

    private String word;
    private Long counts;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long wordCountDBId;

    @CreationTimestamp
    private Date created;

    public Long getCounts() {
        return counts;
    }

    public void setCounts(Long count) {
        this.counts = count;
    }

    public Long getWordCountDBId() {
        return wordCountDBId;
    }

    public void setWordCountDBId(Long wordCountDBId) {
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

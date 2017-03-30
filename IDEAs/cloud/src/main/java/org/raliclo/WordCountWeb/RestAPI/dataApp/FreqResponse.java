package org.raliclo.WordCountWeb.RestAPI.dataApp;

/**
 * Created by raliclo on 9/10/16.
 * Project Name : TestNG-1
 */

public class FreqResponse {
    public Long called;
    public Long counted;

    FreqResponse(Long called, Long counted) {
        this.called = called;
        this.counted = counted;
    }
}

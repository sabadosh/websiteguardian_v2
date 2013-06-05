package com.androidbrew.WebSiteGuardian.DAO;


public class HttpClient {

    int id;

    String response;
    String timestamp;

    public HttpClient() {
    }

    public HttpClient(int id, String response, String timestamp) {
        this.id = id;
        this.response = response;
        this.timestamp = timestamp;
    }

    public HttpClient(String response, String timestamp) {
        this.response = response;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}

package com.sarafanka.team.sarafanka.server.socialSenders;

import com.sarafanka.team.sarafanka.server.Constants;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class WhatappSender implements SocialSender {
    @Override
    public String send(String text, String phoneNumber) throws IOException {
        //send message to whatsapp

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost("https://eu24.chat-api.com/instance17301/message?token=" + Constants.Social.whatsappToken);

        JSONObject obj = new JSONObject();
        obj.put("phone", phoneNumber);
        obj.put("body", text);
        request.setEntity(new StringEntity(obj.toString(), "UTF-8"));

        request.setHeader("Content-Type", "application/json");

        //Execute and get the response.
        HttpResponse response = httpClient.execute(request);
        HttpEntity entity = response.getEntity();
      //  JSONObject json = new JSONObject(entity.getContent().toString());

        BufferedReader bR = new BufferedReader(  new InputStreamReader(entity.getContent()));
        String line = "";
        StringBuilder responseStrBuilder = new StringBuilder();
        while((line =  bR.readLine()) != null){
            responseStrBuilder.append(line);
        }
        entity.getContent().close();

        JSONObject result= new JSONObject(responseStrBuilder.toString());
        return String.valueOf(result.getBoolean("sent"));

    }
}

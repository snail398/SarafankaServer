package com.sarafanka.team.sarafanka.server.socialSenders;

import com.sarafanka.team.sarafanka.server.Constants;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Base64;

public class SmsSender implements SocialSender {
    @Override
    public void send(String text, String phoneNumber) {
        //send sms to user

            try {
                String name = "snail398";
                String password = "Xamovniki398";

                String sender = Constants.Social.smsSignature;

                String authString = name + ":" + password;
                String authStringEnc = Base64.getEncoder().encodeToString(authString.getBytes());

                URL url = new URL("http", "api.smsfeedback.ru", 80, "/messages/v2/send/?phone=%2B" + phoneNumber + "&text=" + URLEncoder.encode(text, "UTF-8") + "&sender=" + sender);
                URLConnection urlConnection = url.openConnection();
                urlConnection.setRequestProperty("Authorization", authStringEnc);
                InputStream is = urlConnection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);

                int numCharsRead;
                char[] charArray = new char[1024];
                StringBuffer sb = new StringBuffer();
                while ((numCharsRead = isr.read(charArray)) > 0) {
                    sb.append(charArray, 0, numCharsRead);
                }
                String result = sb.toString();

                System.out.println("*** BEGIN ***");
                System.out.println(result);
                System.out.println("*** END ***");

            } catch (MalformedURLException ex) {
                System.out.println(ex.toString());
            } catch (IOException ex) {
                System.out.println(ex.toString());
            }
        }

}

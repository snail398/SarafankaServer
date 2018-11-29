package com.sarafanka.team.sarafanka.server.socialSenders;

import java.io.IOException;

public interface SocialSender {
    String send(String text, String phoneNumber) throws IOException;
}

package com.project.devgram.chat.model;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginInfo {

    private final String name;
    private final String token;

    @Builder
    public LoginInfo(String name, String token) {
        this.name = name;
        this.token = token;
    }
}

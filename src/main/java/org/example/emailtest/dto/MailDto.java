package org.example.emailtest.dto;


import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MailDto {
    private String from;
    private String[] address;
//    private String[] ccAddress;
    private String title;
    private String content;
}
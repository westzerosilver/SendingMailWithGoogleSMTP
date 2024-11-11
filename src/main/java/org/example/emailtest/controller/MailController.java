package org.example.emailtest.controller;

import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.example.emailtest.dto.MailDto;
import org.example.emailtest.service.MailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.sendgrid.*;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class MailController {
    private final MailService mailService;

    @PostMapping("/mail")
    public ResponseEntity<String> sendMail(@RequestPart(name = "mailDto") MailDto mailDto,
                                           MultipartFile file) {
        try {
            mailService.sendMultipleMessage(mailDto, file);
            return ResponseEntity.ok("mail success");
        } catch (MessagingException messagingException){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messagingException.getMessage());
        } catch (IOException ioException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ioexception");
        } catch (Error e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}

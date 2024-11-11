package org.example.emailtest.service;

import ch.qos.logback.core.util.StringUtil;
import jakarta.activation.DataHandler;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.internet.MimeUtility;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.example.emailtest.dto.MailDto;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.standard.expression.MessageExpression;
import java.net.URLEncoder;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender emailSender;

    public void sendMultipleMessage(MailDto mailDto, MultipartFile file) throws MessagingException, IOException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        System.out.println(mailDto.getAddress());
        System.out.println(mailDto.getContent());
        System.out.println(mailDto.getTitle());
        System.out.println(mailDto.getFrom());

        // 메일 제목 설정
        helper.setSubject(mailDto.getTitle());
        helper.setFrom(mailDto.getFrom());
        helper.setReplyTo(mailDto.getFrom());
        helper.setTo(mailDto.getAddress());

        // 본문 내용 추가
        helper.setText(mailDto.getContent(), true);

        // 첨부파일 설정
        if (file != null && !file.isEmpty()) {
            MimeBodyPart attachmentPart = new MimeBodyPart();

            String originalFileName = file.getOriginalFilename();

            // 파일 이름 UTF-8 인코딩 (RFC 2231 방식 사용)
            String encodedFileName = URLEncoder.encode(originalFileName, "UTF-8").replaceAll("\\+", "%20");

            ByteArrayDataSource dataSource = new ByteArrayDataSource(file.getBytes(), file.getContentType());
            attachmentPart.setDataHandler(new DataHandler(dataSource));

            // RFC 2231 방식으로 Content-Disposition 설정
            attachmentPart.setHeader("Content-Disposition",
                    "attachment; filename=\"" + originalFileName + "\"; filename*=UTF-8''" + encodedFileName);

            // MimeMultipart에 첨부파일 추가
            MimeMultipart multipart = new MimeMultipart("mixed"); // "mixed"는 본문과 첨부파일을 동시에 보낼 때 사용
            multipart.addBodyPart(attachmentPart);

            // 메일에 Multipart 추가
            message.setContent(multipart);
        }

        // 메일 전송
        emailSender.send(message);
        System.out.println("mail send complete");
    }

//
//
//
//
//    public void sendMultipleMessage(MailDto mailDto, MultipartFile file) throws MessagingException, IOException {
//        MimeMessage message = emailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
//
//        System.out.println(mailDto.getAddress());
//        System.out.println(mailDto.getContent());
//        System.out.println(mailDto.getTitle());
//        System.out.println(mailDto.getFrom());
//        // 메일 제목 설정
//        helper.setSubject(mailDto.getTitle());
//
//        // 참조자 설정
////        helper.setCc(mailDto.getCcAddress());
//        helper.setText(mailDto.getContent());
//        helper.setFrom(mailDto.getFrom());
//        helper.setReplyTo(mailDto.getFrom());
//        System.out.println("2");
//
//        // 첨부파일 설정
//        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
//        System.out.println("3");
//        helper.addAttachment(MimeUtility.encodeText(fileName, "UTF-8", "B"), new ByteArrayResource(IOUtils.toByteArray(file.getInputStream())));
//
//        System.out.println("4");
//        // 전송
//        helper.setTo(mailDto.getAddress());
//        emailSender.send(message);
//        System.out.println("mail send complete");
//
//
//    }

}

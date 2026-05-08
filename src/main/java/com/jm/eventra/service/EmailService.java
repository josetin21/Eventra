package com.jm.eventra.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    private static final String FROM = "Eventra <onboarding@resend.dev>";
    private static final String RESEND_URL = "https://api.resend.com/emails";

    @Value("${resend.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    @Async
    public void sendRegistrationConfirmation(String toEmail, String studentName,
                                             String eventTitle, String eventVenue,
                                             String eventDate) {
        String body =
                "Hi " + studentName + ",\n\n" +
                        "Your registration for " + eventTitle + " has been confirmed!\n\n" +
                        "Event Details:\n" +
                        "Title: " + eventTitle + "\n" +
                        "Venue: " + eventVenue + "\n" +
                        "Date: " + eventDate + "\n\n" +
                        "Please keep your QR code ready for attendance.\n\n" +
                        "Regards,\n Eventra Team";
        send(toEmail, "Registration Confirmed - " + eventTitle, body);
    }

    @Async
    public void sendEventUpdateNotification(String toEmail, String studentName,
                                            String eventTitle,
                                            String oldVenue, String newVenue,
                                            String oldEventDate, String newEventDate) {
        String body=
                "Hi " + studentName + ",\n\n" +
                        "An event you registered for has been updated.\n\n" +
                        "Event: " + eventTitle + "\n\n" +
                        "Updated Details:\n" +
                        "Venue: " + oldVenue + " → " + newVenue + "\n" +
                        "Date: " + oldEventDate + " → " + newEventDate + "\n\n" +
                        "Please check the app for the latest details.\n\n" +
                        "Regards,\n Eventra Team";
        send(toEmail, "Event Updated - " + eventTitle, body);
    }

    @Async
    public void sendPasswordResetOtp(String toEmail, String otp) {
        String body=
                "Hi,\n\n" +
                        "Your OTP to reset your Eventra password is:\n\n" +
                        otp + "\n\n" +
                        "This OTP will expire in 3 minutes.\n\n" +
                        "If you didn't request this, ignore this email.\n\n" +
                        "Regards,\n Eventra Team";
        send(toEmail, "Eventra Password Reset OTP", body);
    }

    @Async
    public void sendPasswordResetSuccess(String toEmail) {
        String body =
                "Hi,\n\n" +
                        "Your Eventra account password was changed successfully.\n\n" +
                        "If you did not perform this action, please reset your password " +
                        "immediately and contact support.\n\n" +
                        "Regards,\n Eventra Team";
        send(toEmail, "Eventra Password Changed Successfully", body);
    }

    private void send(String to, String subject, String text){
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            Map<String, Object> payload = Map.of(
                    "from", FROM,
                    "to", new String[]{to},
                    "subject", subject,
                    "text", text
            );

            HttpEntity<Map<String,Object>> request = new HttpEntity<>(payload, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(RESEND_URL, request, String.class);
            logger.info("Email sent to {}: {}", to,response.getStatusCode());
        } catch (Exception e){
            logger.info("Failed to send email to {}: {}", to , e.getMessage(), e);
        }
    }
}
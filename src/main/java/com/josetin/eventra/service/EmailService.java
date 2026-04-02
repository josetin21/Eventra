package com.josetin.eventra.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendRegistrationConfirmation(String toEmail, String studentName,
                                             String eventTitle, String eventVenue,
                                             String eventDate){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Registration Confirmed - " + eventTitle);
        message.setText(
                        "Hi " + studentName + ",\n\n" +
                        "Your registration for " + eventTitle + " has been confirmed!\n\n" +
                        "Event Details:\n" +
                        "Title: " + eventTitle + "\n" +
                        "Venue: " + eventVenue + "\n" +
                        "Date: " + eventDate + "\n\n" +
                        "Please keep your QR code ready for attendance.\n\n" +
                        "regards,\n" +
                        "Eventra Team"
        );
        mailSender.send(message);
    }

    public void sendEventUpdateNotification(String toEmail, String studentName,
                                            String eventTitle,
                                            String oldVenue, String newVenue,
                                            String oldEventDate, String newEventDate){

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Event Updated - " + eventTitle);
        message.setText(
                "Hi " + studentName + ",\n\n" +
                        "An event you registered for has been updated.\n\n" +
                        "Event: " + eventTitle + "\n\n" +
                        "Updated Details:\n" +
                        "Venue: " + oldVenue + " → " + newVenue + "\n" +
                        "Date: " + oldEventDate + " → " + newEventDate + "\n\n" +
                        "Please check the app for the latest details.\n\n" +
                        "regards,\n" +
                        "Eventra Team"
        );
        mailSender.send(message);
    }
}

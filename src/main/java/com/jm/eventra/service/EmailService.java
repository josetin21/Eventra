package com.jm.eventra.service;

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

    public void sendPasswordResetOtp(String toEmail, String otp){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Eventra Password Reset OTP");
        message.setText(
                "Hi,\n\n" +
                        "Your OTP to reset your Eventra password is:\n\n" +
                        otp + "\n\n" +
                        "This OTP will expire in 3 minutes.\n\n" +
                        "If you didn't request this, ignore this email.\n\n" +
                        "regards,\n" +
                        "Eventra Team"
        );
        mailSender.send(message);
    }

    public void sendPasswordResetSuccess(String toEmail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Eventra Password Changed Successfully");
        message.setText(
                "Hi,\n\n" +
                        "Your Eventra account password was changed successfully.\n\n" +
                        "If you did not perform this action, please reset your password immediately and contact support.\n\n" +
                        "Regards,\n" +
                        "Eventra Team"
        );
        mailSender.send(message);
    }
}

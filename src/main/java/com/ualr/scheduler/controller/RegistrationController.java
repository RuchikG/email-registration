package com.ualr.scheduler.controller;

import com.ualr.scheduler.model.Registration;
import com.ualr.scheduler.repository.RegistrationRepository;
import com.ualr.scheduler.service.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.security.SecureRandom;
import java.util.Date;
import java.util.UUID;

@Controller
public class RegistrationController {

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailSenderService emailSenderService;

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView displayRegistration(ModelAndView modelAndView, Registration registration)
    {
        modelAndView.addObject("registration", registration);
        modelAndView.setViewName("register");
        return modelAndView;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView registerUser(ModelAndView modelAndView, Registration registration)
    {
        Registration existingRegistration = registrationRepository.findByUsernameIgnoreCase(registration.getUsername());
        if(existingRegistration != null){
            modelAndView.addObject("message","This username already exists! If that is you please login");
            modelAndView.setViewName("error");
        } else {
            registration.setConfirmationToken(UUID.randomUUID().toString());
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(registration.getEmailId());
            mailMessage.setSubject("Complete Registration!");
            mailMessage.setFrom("ruchikgabha@gmail.com");
            mailMessage.setText("To confirm your account, please click here: " + "https://email-registration.herokuapp.com/confirm-account?token="+registration.getConfirmationToken());
            mailMessage.setReplyTo("ruchikgabha@gmail.com");

            emailSenderService.sendEmail(mailMessage);

            SecureRandom random = new SecureRandom();
            SecureRandom secureRandom = new SecureRandom(random.generateSeed(10));
            String password = new BCryptPasswordEncoder(4,secureRandom).encode(registration.getPassword());
            registration.setPassword(password);
            registrationRepository.save(registration);

            modelAndView.addObject("emailId",registration.getEmailId());
            modelAndView.setViewName("successfulRegistration");
        }
        return modelAndView;
    }

    @RequestMapping(value = "/confirm-account", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView confirmUserAccount(ModelAndView modelAndView, @RequestParam("token")String confirmationToken){
        Registration token = registrationRepository.findByConfirmationToken(confirmationToken);

        if(token != null){
            token.setEnabled(true);
            token.setConfirmationDate(new Date());
            registrationRepository.save(token);
            modelAndView.setViewName("accountVerified");
        } else {
            modelAndView.addObject("message","The link is invalid or broken");
            modelAndView.setViewName("error");
        }
        return modelAndView;
    }

    public RegistrationRepository getRegistrationRepository() {
        return registrationRepository;
    }

    public void setRegistrationRepository(RegistrationRepository registrationRepository) {
        this.registrationRepository = registrationRepository;
    }

    public EmailSenderService getEmailSenderService() {
        return emailSenderService;
    }

    public void setEmailSenderService(EmailSenderService emailSenderService) {
        this.emailSenderService = emailSenderService;
    }
}

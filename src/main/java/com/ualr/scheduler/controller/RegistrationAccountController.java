package com.ualr.scheduler.controller;

import com.ualr.scheduler.model.ConfirmationToken;
import com.ualr.scheduler.model.Registration;
import com.ualr.scheduler.repository.ConfirmationTokenRepository;
import com.ualr.scheduler.repository.RegistrationRepository;
import com.ualr.scheduler.service.EmailSenderService;
import org.dom4j.rule.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.security.SecureRandom;

@Controller
public class RegistrationAccountController {

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

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
            SecureRandom random = new SecureRandom();
            SecureRandom secureRandom = new SecureRandom(random.generateSeed(10));

            registration.setPassword(new BCryptPasswordEncoder(4,secureRandom).encode(registration.getPassword()));
            registrationRepository.save(registration);

            ConfirmationToken confirmationToken = new ConfirmationToken(registration);

            confirmationTokenRepository.save(confirmationToken);

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(registration.getEmailId());
            mailMessage.setSubject("Complete Registration!");
            mailMessage.setFrom("ruchikgabhawala@gmail.com");
            mailMessage.setText("To confirm your account, please click here: " + "https://email-registration.herokuapp.com/confirm-account?token="+confirmationToken.getConfirmationToken());
            mailMessage.setReplyTo("ruchikgabhawala@gmail.com");

            emailSenderService.sendEmail(mailMessage);

            modelAndView.addObject("emailId",registration.getEmailId());
            modelAndView.setViewName("successfulRegistration");
        }
        return modelAndView;
    }

    @RequestMapping(value = "/confirm-account", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView confirmUserAccount(ModelAndView modelAndView, @RequestParam("token")String confirmationToken){
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);

        if(token != null){
            Registration registration = registrationRepository.findByUsernameIgnoreCase(token.getRegistration().getUsername());
            registration.setEnabled(true);
            registrationRepository.save(registration);
            modelAndView.setViewName("accountVerified");
        } else {
            modelAndView.addObject("message","The link is invalid or broken");
            modelAndView.setViewName("error");
        }
        return modelAndView;
    }

    @RequestMapping(value = "/student",method = RequestMethod.GET)
    public ModelAndView studentPage(ModelAndView modelAndView){
        modelAndView.setViewName("student");
        return modelAndView;
    }

    public RegistrationRepository getRegistrationRepository() {
        return registrationRepository;
    }

    public void setRegistrationRepository(RegistrationRepository registrationRepository) {
        this.registrationRepository = registrationRepository;
    }

    public ConfirmationTokenRepository getConfirmationTokenRepository() {
        return confirmationTokenRepository;
    }

    public void setConfirmationTokenRepository(ConfirmationTokenRepository confirmationTokenRepository) {
        this.confirmationTokenRepository = confirmationTokenRepository;
    }

    public EmailSenderService getEmailSenderService() {
        return emailSenderService;
    }

    public void setEmailSenderService(EmailSenderService emailSenderService) {
        this.emailSenderService = emailSenderService;
    }
}

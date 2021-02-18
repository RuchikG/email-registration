package com.ualr.scheduler.controller;

import com.ualr.scheduler.repository.RegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AdminController {

    @Autowired
    private RegistrationRepository registrationRepository;

    //@PreAuthorize("hasAnyRole('ADMIN')")
    @RequestMapping(value = "/admin",method = RequestMethod.GET)
    public ModelAndView adminUserPage(ModelAndView modelAndView){
        modelAndView.setViewName("admin");
        return modelAndView;
    }
}

package com.ualr.scheduler.controller;

import com.ualr.scheduler.model.Course;
import com.ualr.scheduler.model.Registration;
import com.ualr.scheduler.repository.CoursesRepository;
import com.ualr.scheduler.repository.RegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class RootController {

    @Autowired
    private CoursesRepository coursesRepository;

    @Autowired
    private RegistrationRepository registrationRepository;

    @PreAuthorize("hasAnyRole('ROOT')")
    @RequestMapping(value = "/root",method = RequestMethod.GET)
    public ModelAndView rootUserPage(ModelAndView modelAndView){
        List<Course> courses = coursesRepository.findAll();
        modelAndView.addObject("courses",courses);
        modelAndView.addObject("requests",registrationRepository.findAllByisEnabledAndRolesNot(false,"STUDENT"));
        modelAndView.addObject("register", new Registration());
        modelAndView.setViewName("root");
        return modelAndView;
    }
}

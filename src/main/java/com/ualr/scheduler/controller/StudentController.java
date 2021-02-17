package com.ualr.scheduler.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class StudentController {

    //@PreAuthorize("hasAnyRole('STUDENT')")
    @RequestMapping(value = "/student",method = RequestMethod.GET)
    public ModelAndView studentPage(ModelAndView modelAndView){
        modelAndView.setViewName("student");
        return modelAndView;
    }
}

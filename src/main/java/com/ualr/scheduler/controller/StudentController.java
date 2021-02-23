package com.ualr.scheduler.controller;

import com.ualr.scheduler.model.Course;
import com.ualr.scheduler.model.Registration;
import com.ualr.scheduler.repository.CoursesRepository;
import com.ualr.scheduler.repository.RegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Set;

@Controller
public class StudentController {

    @Autowired
    CoursesRepository coursesRepository;

    @Autowired
    RegistrationRepository registrationRepository;

    //@PreAuthorize("hasAnyRole('STUDENT')")
    @RequestMapping(value = "/student",method = RequestMethod.GET)
    public ModelAndView studentPage(ModelAndView modelAndView){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Registration registration = registrationRepository.findByUsernameIgnoreCase(username);
        Set<Course> designatedCourses = registration.getDesignatedCourses();
        List<Course> courses = coursesRepository.findAll();
        modelAndView.addObject("courses",courses);
        modelAndView.addObject("designatedCourses",designatedCourses);
        modelAndView.setViewName("student");
        return modelAndView;
    }

    //@PreAuthorize("hasAnyRole('STUDENT')")
    @RequestMapping(value = "/addDCourse",method = {RequestMethod.POST,RequestMethod.GET})
    public ModelAndView addDesignatedCourse(ModelAndView modelAndView, @RequestParam("courseNum")String courseNum){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Registration registration = registrationRepository.findByUsernameIgnoreCase(username);
        Set<Course> designatedCourses = registration.getDesignatedCourses();
        String[] courseNums = courseNum.split(",");
        for (String courseNo:courseNums) {
            Course course = coursesRepository.findByCourseNumber(Long.decode(courseNo));
            designatedCourses.add(course);
        }
        modelAndView.addObject("message","The courses have been added to the designated courses list");
        modelAndView.setViewName("courseRequest");
        registrationRepository.save(registration);
        return modelAndView;
    }

    //@PreAuthorize("hasAnyRole('STUDENT')")
    @RequestMapping(value = "/deleteDCourse",method = {RequestMethod.GET,RequestMethod.POST})
    public ModelAndView deleteDesignatedCourse(ModelAndView modelAndView, @RequestParam("courseNum")String courseNum){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Registration registration = registrationRepository.findByUsernameIgnoreCase(username);
        Set<Course> designatedCourses = registration.getDesignatedCourses();
        Course course = coursesRepository.findByCourseNumber(Long.decode(courseNum));
        designatedCourses.remove(course);
        modelAndView.addObject("message","The course has been removed from the designated courses list");
        modelAndView.setViewName("courseRequest");
        registrationRepository.save(registration);
        return modelAndView;
    }
}

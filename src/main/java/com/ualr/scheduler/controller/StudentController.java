package com.ualr.scheduler.controller;

import com.ualr.scheduler.model.*;
import com.ualr.scheduler.repository.CoursesRepository;
import com.ualr.scheduler.repository.RegistrationRepository;
import com.ualr.scheduler.repository.ReservedTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
public class StudentController {

    @Autowired
    CoursesRepository coursesRepository;

    @Autowired
    RegistrationRepository registrationRepository;

    @Autowired
    ReservedTimeRepository reservedTimeRepository;

    //@PreAuthorize("hasAnyRole('STUDENT')")
    @RequestMapping(value = "/student",method = RequestMethod.GET)
    public ModelAndView studentPage(ModelAndView modelAndView){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Registration registration = registrationRepository.findByUsernameIgnoreCase(username);
        Set<Course> designatedCourses = registration.getDesignatedCourses();
        Set<Course> possibleCourses = registration.getPossibleCourses();
        Set<ReservedTime> reservedTimes = registration.getReservedTimes();
        List<Course> courses = coursesRepository.findAll();
        modelAndView.addObject("courses",courses);
        modelAndView.addObject("possibleCourses",possibleCourses);
        modelAndView.addObject("designatedCourses",designatedCourses);
        modelAndView.addObject("reserveTimes",reservedTimes);
        modelAndView.setViewName("student");
        return modelAndView;
    }

    @RequestMapping(value = "/addPCourse",method = {RequestMethod.POST,RequestMethod.GET})
    public ModelAndView addPossibleCourse(ModelAndView modelAndView, @RequestParam("courseNum")String courseNum){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Registration registration = registrationRepository.findByUsernameIgnoreCase(username);
        Set<Course> possibleCourses = registration.getPossibleCourses();
        String[] courseNums = courseNum.split(",");
        for (String courseNo:courseNums) {
            Course course = coursesRepository.findByCourseNumber(Long.decode(courseNo));
            possibleCourses.add(course);
        }
        modelAndView.addObject("message","The courses have been added to the possible courses list");
        modelAndView.setViewName("courseRequest");
        registrationRepository.save(registration);
        return modelAndView;
    }

    //@PreAuthorize("hasAnyRole('STUDENT')")
    @RequestMapping(value = "/deletePCourse",method = {RequestMethod.GET,RequestMethod.POST})
    public ModelAndView deletePossibleCourse(ModelAndView modelAndView, @RequestParam("courseNum")String courseNum){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Registration registration = registrationRepository.findByUsernameIgnoreCase(username);
        Set<Course> possibleCourses = registration.getPossibleCourses();
        Course course = coursesRepository.findByCourseNumber(Long.decode(courseNum));
        possibleCourses.remove(course);
        modelAndView.addObject("message","The course has been removed from the possible courses list");
        modelAndView.setViewName("courseRequest");
        registrationRepository.save(registration);
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

    //@PreAuthorize("hasAnyRole('STUDENT')")
    @RequestMapping(value = "/addReserve", method = RequestMethod.GET)
    public ModelAndView addingReserveTime(ModelAndView modelAndView, ReservedTime reservedTime){
        modelAndView.setViewName("addReserveTime");
        modelAndView.addObject("reserveTime",reservedTime);
        return modelAndView;
    }

    //@PreAuthorize("hasAnyRole('STUDENT')")
    @RequestMapping(value = "/addReserve", method = RequestMethod.POST)
    public ModelAndView addReserveTime(ModelAndView modelAndView, ReservedTime reservedTime){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Registration registration = registrationRepository.findByUsernameIgnoreCase(username);
        Set<ReservedTime> reservedTimes = registration.getReservedTimes();
        reservedTime.setRegistration(registration);
        reservedTimes.add(reservedTime);
        modelAndView.addObject("message","The reserved time has been added to the user");
        modelAndView.setViewName("courseRequest");
        registrationRepository.save(registration);
        return modelAndView;
    }

    //@PreAuthorize("hasAnyRole('STUDENT')")
    @RequestMapping(value = "/editReserve",method = RequestMethod.GET)
    public ModelAndView displayEditReserveTime(ModelAndView modelAndView, @RequestParam("id")String id){
        ReservedTime reservedTime = reservedTimeRepository.findByReservedtimeID(Long.decode(id));
        modelAndView.addObject("reserveTime",reservedTime);
        modelAndView.setViewName("editReserveTime");
        return modelAndView;
    }

    //@PreAuthorize("hasAnyRole('STUDENT')")
    @RequestMapping(value = "/editReserve",method = RequestMethod.POST)
    public ModelAndView editReserveTime(ModelAndView modelAndView, ReservedTime reservedTime){
        ReservedTime check = reservedTimeRepository.findByReservedtimeID(reservedTime.getReserved_timeID());
        check.setDay(reservedTime.getDay());
        check.setStartTime(reservedTime.getStartTime());
        check.setEndTime(reservedTime.getEndTime());
        check.setDescription(reservedTime.getDescription());
        modelAndView.addObject("message","The course " + check.getReserved_timeID() + " has been updated successfully!");
        modelAndView.setViewName("courseRequest");
        reservedTimeRepository.save(check);
        return modelAndView;
    }

    //@PreAuthorize("hasAnyRole('STUDENT')")
    @RequestMapping(value = "/deleteReserve", method = {RequestMethod.GET,RequestMethod.POST})
    public ModelAndView deleteReserve(ModelAndView modelAndView, @RequestParam("id")String id){
        ReservedTime reservedTime = reservedTimeRepository.findByReservedtimeID(Long.decode(id));
        modelAndView.addObject("message","The reserved time " + reservedTime.getReserved_timeID() + " has been deleted successfully!");
        modelAndView.setViewName("courseRequest");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Registration registration = registrationRepository.findByUsernameIgnoreCase(auth.getName());
        Set<ReservedTime> reservedTimeSet = registration.getReservedTimes();
        reservedTimeSet.remove(reservedTime);
        registrationRepository.save(registration);
        reservedTimeRepository.delete(reservedTime);
        return modelAndView;
    }

    /*@PreAuthorize("hasAnyRole('STUDENT')")
    @RequestMapping(value = "/generateSchedules", method = RequestMethod.GET)
    public ModelAndView generatingSchedules(ModelAndView modelAndView){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Registration registration = registrationRepository.findByUsernameIgnoreCase(username);
        ArrayList<Section> possibleSections = new ArrayList<>();
        Map<String, Set<ReservedTime>> reservedTimes;
        Set<ReservedTime> reservedTimeSet = registration.getReservedTimes();
        for (ReservedTime reservedTime: reservedTimeSet){

        }
        Set<Course> designatedCourses = registration.getDesignatedCourses();
        for (Course course: designatedCourses){
            for (Section section: course.getSections()){
               Boolean available = true;
               for (MeetingTimes meetingTimes: section.getMeetingTime()){
                   switch (meetingTimes.getDay()){
                       case "M": break;
                       case "T": break;
                       case "W": break;

                   }
               }
            }
        }
        return modelAndView;
    }*/
}

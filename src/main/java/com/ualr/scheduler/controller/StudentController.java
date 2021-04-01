package com.ualr.scheduler.controller;

import com.ualr.scheduler.model.*;
import com.ualr.scheduler.repository.*;
import org.apache.tomcat.jni.Local;
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

import java.time.LocalTime;
import java.util.*;

@Controller
public class StudentController {
    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    CoursesRepository coursesRepository;

    @Autowired
    RegistrationRepository registrationRepository;

    @Autowired
    ReservedTimeRepository reservedTimeRepository;

    @Autowired
    SectionRepository sectionRepository;

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
        modelAndView.addObject("message","The reserve time " + check.getReserved_timeID() + " has been updated successfully!");
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
    @RequestMapping("/test")
    public ModelAndView test(ModelAndView modelAndView){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Registration registration = registrationRepository.findByUsernameIgnoreCase(username);
        Set<Schedule> schedules = new HashSet<>();
        registration.setSchedules(schedules);
        registrationRepository.save(registration);
        scheduleRepository.deleteAll();
        modelAndView.addObject("message","Schedules are cleared");
        modelAndView.setViewName("courseRequest");
        return modelAndView;
    }

    @PreAuthorize("hasAnyRole('STUDENT')")
    @RequestMapping(value = "/generateSchedules", method = RequestMethod.GET)
    public ModelAndView generatingSchedules(ModelAndView modelAndView){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Registration registration = registrationRepository.findByUsernameIgnoreCase(username);
        ArrayList<Section> designatedSections = new ArrayList<>();
        Set<Course> designatedCourses = registration.getDesignatedCourses();
        Set<ReservedTime> reservedTimes = registration.getReservedTimes();
        for (Course course: designatedCourses) {
            for (Section section: course.getSections()){
                designatedSections.add(section);
            }
        }
        Set<Schedule> schedules = registration.getSchedules();
        int k = 0;
        for (int i = 0;i<designatedSections.size();i++){
            Schedule schedule = new Schedule();
            schedule.setScheduleName("schedule" + Integer.toString(k+1+schedules.size()));
            schedule.setRegistration(registration);
            String sections = scheduling(designatedSections,designatedSections.get(i),reservedTimes);
            boolean newSchedule = true;
            if (schedules.size()>0) {
                for (Schedule schedule1 : schedules) {
                    if (schedule1.getSections().equals(sections.substring(0, sections.length() - 2))) {
                        newSchedule = false;
                    }
                }
            } else {
                schedule.setSections(sections.substring(0, sections.length() - 2));
                schedules.add(schedule);
            }
            if (newSchedule){
                schedule.setSections(sections.substring(0, sections.length() - 2));
                schedules.add(schedule);
                k++;
            }
        }
        registration.setSchedules(schedules);
        registrationRepository.save(registration);
        modelAndView.addObject("message","Schedules are generated");
        modelAndView.setViewName("courseRequest");
        return modelAndView;
    }

    public String scheduling(ArrayList<Section> sections,Section section, Set<ReservedTime> reservedTimes){
        String schedule = "";
        boolean sectionTime = true;
        for (MeetingTimes meetingTimes: section.getMeetingTime()) {
            for (int i = 0; i < meetingTimes.getDay().length(); i++) {
                for (ReservedTime reservedTime : reservedTimes) {
                    if ((reservedTime.getDay().indexOf(meetingTimes.getDay().substring(i, i + 1)) != -1) && (sectionTime)){
                        LocalTime sStartTime = LocalTime.parse(meetingTimes.getStartTime());
                        LocalTime sEndTime = LocalTime.parse(meetingTimes.getEndTime());
                        LocalTime rStartTime = LocalTime.parse(reservedTime.getStartTime());
                        LocalTime rEndTime = LocalTime.parse(reservedTime.getEndTime());
                        if (!(sStartTime.isAfter(rEndTime) || sEndTime.isBefore(rStartTime))) {
                            sectionTime = false;
                        }
                    }
                }
            }
        }
        if (sectionTime){
            schedule += (section.getSectionNumber().toString() + ", ");
        }
        for (Section possibleSection: sections){
            Set<Section> scheduledSections = new HashSet<>();
            for (int i =0;i<schedule.length();i+=5){
                scheduledSections.add(sectionRepository.findBySectionNumber(Long.parseLong(schedule.substring(i,i+3))));
            }
            if (schedule.indexOf(possibleSection.getSectionNumber().toString()) == -1){
                boolean courseNotIn = true;
                for (Section ssection: scheduledSections){
                    if (possibleSection.getCourses().getCourseNumber() == ssection.getCourses().getCourseNumber()){
                        courseNotIn = false;
                    }

                }
                if (courseNotIn){
                    sectionTime = true;
                    for (MeetingTimes meetingTimes: possibleSection.getMeetingTime()){
                        for (int i=0;i<meetingTimes.getDay().length();i++){
                            for (ReservedTime reservedTime: reservedTimes){
                                if ((reservedTime.getDay().indexOf(meetingTimes.getDay().substring(i,i+1)) != -1) && (sectionTime)){
                                    LocalTime sStartTime = LocalTime.parse(meetingTimes.getStartTime());
                                    LocalTime sEndTime = LocalTime.parse(meetingTimes.getEndTime());
                                    LocalTime rStartTime = LocalTime.parse(reservedTime.getStartTime());
                                    LocalTime rEndTime = LocalTime.parse(reservedTime.getEndTime());
                                    if (!(sStartTime.isAfter(rEndTime) || sEndTime.isBefore(rStartTime))){
                                        sectionTime = false;
                                    }
                                }
                            }
                            for (Section sSection: scheduledSections){
                                for (MeetingTimes sMeetingTimes: sSection.getMeetingTime()){
                                    if ((sMeetingTimes.getDay().indexOf(meetingTimes.getDay().substring(i,i+1)) != -1) && (sectionTime)){
                                        LocalTime sStartTime = LocalTime.parse(meetingTimes.getStartTime());
                                        LocalTime sEndTime = LocalTime.parse(meetingTimes.getEndTime());
                                        LocalTime rStartTime = LocalTime.parse(sMeetingTimes.getStartTime());
                                        LocalTime rEndTime = LocalTime.parse(sMeetingTimes.getEndTime());
                                        if (!(sStartTime.isAfter(rEndTime) || sEndTime.isBefore(rStartTime))){
                                            sectionTime = false;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (sectionTime) {
                        schedule+=(possibleSection.getSectionNumber().toString()+", ");
                    }
                }
            }
        }
        return schedule;
    }
}

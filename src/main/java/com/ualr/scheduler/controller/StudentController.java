package com.ualr.scheduler.controller;

import com.ualr.scheduler.model.*;
import com.ualr.scheduler.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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

    @PreAuthorize("hasAnyRole('STUDENT')")
    @RequestMapping(value = "/student",method = RequestMethod.GET)
    public ModelAndView studentPage(ModelAndView modelAndView){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Registration registration = registrationRepository.findByUsernameIgnoreCase(username);
        Set<Course> designatedCourses = registration.getDesignatedCourses();
        Set<Course> possibleCourses = registration.getPossibleCourses();
        Set<ReservedTime> reservedTimes = registration.getReservedTimes();
        List<Course> courses = coursesRepository.findAll();
        Map<String,ArrayList<Section>> scheduling = new LinkedHashMap<>();
        int j = 1;
        for (Schedule schedule: registration.getSchedules()){
            ArrayList<Section> sections = new ArrayList<>();
            String[] scheduledSections = schedule.getSections().split(",");
            for(String s: scheduledSections){
                sections.add(sectionRepository.findBySectionid(Long.decode(s.trim())));
            }
            scheduling.put(("Schedule " + (j)),sections);
            j++;
        }
        modelAndView.addObject("courses",courses);
        modelAndView.addObject("possibleCourses",possibleCourses);
        modelAndView.addObject("designatedCourses",designatedCourses);
        modelAndView.addObject("reserveTimes",reservedTimes);
        modelAndView.addObject("scheduling",scheduling);
        modelAndView.setViewName("student");
        return modelAndView;
    }

    @PreAuthorize("hasAnyRole('STUDENT')")
    @RequestMapping(value = "/addPCourse",method = {RequestMethod.POST,RequestMethod.GET})
    public ModelAndView addPossibleCourse(ModelAndView modelAndView, @RequestParam("courseId")String courseId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Registration registration = registrationRepository.findByUsernameIgnoreCase(username);
        Set<Course> possibleCourses = registration.getPossibleCourses();
        String[] courseIds = courseId.split(",");
        for (String courseID:courseIds) {
            Course course = coursesRepository.findByCourseid(Long.decode(courseID));
            possibleCourses.add(course);
        }
        modelAndView.addObject("message","The courses have been added to the possible courses list");
        modelAndView.setViewName("courseRequest");
        registrationRepository.save(registration);
        return modelAndView;
    }

    @PreAuthorize("hasAnyRole('STUDENT')")
    @RequestMapping(value = "/deletePCourse",method = {RequestMethod.GET,RequestMethod.POST})
    public ModelAndView deletePossibleCourse(ModelAndView modelAndView, @RequestParam("courseId")String courseId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Registration registration = registrationRepository.findByUsernameIgnoreCase(username);
        Set<Course> possibleCourses = registration.getPossibleCourses();
        Course course = coursesRepository.findByCourseid(Long.decode(courseId));
        possibleCourses.remove(course);
        modelAndView.addObject("message","The course has been removed from the possible courses list");
        modelAndView.setViewName("courseRequest");
        registrationRepository.save(registration);
        return modelAndView;
    }

    @PreAuthorize("hasAnyRole('STUDENT')")
    @RequestMapping(value = "/addDCourse",method = {RequestMethod.POST,RequestMethod.GET})
    public ModelAndView addDesignatedCourse(ModelAndView modelAndView, @RequestParam("courseId")String courseId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Registration registration = registrationRepository.findByUsernameIgnoreCase(username);
        Set<Course> designatedCourses = registration.getDesignatedCourses();
        String[] courseIds = courseId.split(",");
        for (String courseID:courseIds) {
            Course course = coursesRepository.findByCourseid(Long.decode(courseID));
            designatedCourses.add(course);
        }
        modelAndView.addObject("message","The courses have been added to the designated courses list");
        modelAndView.setViewName("courseRequest");
        registrationRepository.save(registration);
        return modelAndView;
    }

    @PreAuthorize("hasAnyRole('STUDENT')")
    @RequestMapping(value = "/deleteDCourse",method = {RequestMethod.GET,RequestMethod.POST})
    public ModelAndView deleteDesignatedCourse(ModelAndView modelAndView, @RequestParam("courseId")String courseId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Registration registration = registrationRepository.findByUsernameIgnoreCase(username);
        Set<Course> designatedCourses = registration.getDesignatedCourses();
        Course course = coursesRepository.findByCourseid(Long.decode(courseId));
        designatedCourses.remove(course);
        modelAndView.addObject("message","The course has been removed from the designated courses list");
        modelAndView.setViewName("courseRequest");
        registrationRepository.save(registration);
        return modelAndView;
    }

    @PreAuthorize("hasAnyRole('STUDENT')")
    @RequestMapping(value = "/addReserve", method = RequestMethod.GET)
    public ModelAndView addingReserveTime(ModelAndView modelAndView, ReservedTime reservedTime){
        modelAndView.setViewName("addReserveTime");
        modelAndView.addObject("reserveTime",reservedTime);
        return modelAndView;
    }

    @PreAuthorize("hasAnyRole('STUDENT')")
    @RequestMapping(value = "/addReserve", method = RequestMethod.POST)
    public ModelAndView addReserveTime(ModelAndView modelAndView, ReservedTime reservedTime){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Registration registration = registrationRepository.findByUsernameIgnoreCase(username);
        Set<ReservedTime> reservedTimes = registration.getReservedTimes();
        reservedTime.setRegistration(registration);
        boolean adding = true;
        for (int i =0; i<reservedTime.getDay().length();i++) {
            for (ReservedTime rTime : reservedTimes) {
                if (rTime.getDay().indexOf(reservedTime.getDay().substring(i,i+1)) != -1){
                   LocalTime rstime = LocalTime.parse(rTime.getStartTime().toUpperCase(), DateTimeFormatter.ofPattern("hh:mma"));
                   LocalTime retime = LocalTime.parse(rTime.getEndTime().toUpperCase(), DateTimeFormatter.ofPattern("hh:mma"));
                   LocalTime reserveStart = LocalTime.parse(reservedTime.getStartTime().toUpperCase(), DateTimeFormatter.ofPattern("hh:mma"));
                   LocalTime reserveEnd = LocalTime.parse(reservedTime.getEndTime().toUpperCase(), DateTimeFormatter.ofPattern("hh:mma"));
                   if (!(reserveStart.isAfter(retime) || reserveEnd.isBefore(rstime))){
                       adding = false;
                       break;
                   }
                }
            }
        }
        if (adding) {
            reservedTimes.add(reservedTime);
            modelAndView.addObject("message", "The reserved time has been added to the user");
            modelAndView.setViewName("courseRequest");
            registrationRepository.save(registration);
        } else {
            modelAndView.addObject("message", "The reserved time has conflict with another reserve time");
            modelAndView.setViewName("courseRequest");
        }
        return modelAndView;
    }

    @PreAuthorize("hasAnyRole('STUDENT')")
    @RequestMapping(value = "/editReserve",method = RequestMethod.GET)
    public ModelAndView displayEditReserveTime(ModelAndView modelAndView, @RequestParam("id")String id){
        ReservedTime reservedTime = reservedTimeRepository.findByReservedtimeID(Long.decode(id));
        modelAndView.addObject("reserveTime",reservedTime);
        modelAndView.setViewName("editReserveTime");
        return modelAndView;
    }

    @PreAuthorize("hasAnyRole('STUDENT')")
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

    @PreAuthorize("hasAnyRole('STUDENT')")
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

    @PreAuthorize("hasAnyRole('STUDENT')")
    @RequestMapping("/test")
    public ModelAndView test(ModelAndView modelAndView){
        Registration registration = registrationRepository.findByUsernameIgnoreCase("root");
        registration.setEnabled(true);
        registrationRepository.save(registration);
        modelAndView.addObject("message","Root is enabled");
        modelAndView.setViewName("courseRequest");
        return modelAndView;
    }

    @PreAuthorize("hasAnyRole('STUDENT')")
    @RequestMapping(value = "/generateSchedules", method = RequestMethod.GET)
    public ModelAndView generatingSchedules(ModelAndView modelAndView){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Registration registration = registrationRepository.findByUsernameIgnoreCase(username);
        Set<Schedule> schedules = registration.getSchedules();
        scheduleRepository.deleteAll();
        schedules.clear();
        ArrayList<Section> designatedSections = new ArrayList<>();
        Set<Course> designatedCourses = registration.getDesignatedCourses();
        Set<ReservedTime> reservedTimes = registration.getReservedTimes();
        for (Course course: designatedCourses) {
            for (Section section: course.getSections()){
                designatedSections.add(section);
            }
        }
        int k = 0;
        for (int i = 0;i<designatedSections.size();i++){
            Schedule schedule = new Schedule();
            schedule.setScheduleName("schedule" + Integer.toString(k+1));
            schedule.setRegistration(registration);
            String sections = scheduling(designatedSections,designatedSections.get(i),reservedTimes);
            boolean newSchedule = true;
            if (schedules.size()>0) {
                for (Schedule schedule1 : schedules) {
                    if (schedule1.getSections().equals(sections.substring(0, sections.length() - 2))) {
                        newSchedule = false;
                    }
                }
            }
            if (newSchedule){
                schedule.setSections(sections.substring(0, sections.length() - 2));
                schedules.add(schedule);
                k++;
            }
        }
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
                        LocalTime sStartTime = LocalTime.parse(meetingTimes.getStartTime().toUpperCase(),DateTimeFormatter.ofPattern("hh:mma"));
                        LocalTime sEndTime = LocalTime.parse(meetingTimes.getEndTime().toUpperCase(),DateTimeFormatter.ofPattern("hh:mma"));
                        LocalTime rStartTime = LocalTime.parse(reservedTime.getStartTime().toUpperCase(), DateTimeFormatter.ofPattern("hh:mma"));
                        LocalTime rEndTime = LocalTime.parse(reservedTime.getEndTime().toUpperCase(), DateTimeFormatter.ofPattern("hh:mma"));
                        if (!(sStartTime.isAfter(rEndTime) || sEndTime.isBefore(rStartTime))) {
                            sectionTime = false;
                        }
                    }
                }
            }
        }
        if (sectionTime){
            schedule += (section.getSectionid().toString() + ", ");
        }
        for (Section possibleSection: sections){
            Set<Section> scheduledSections = new HashSet<>();
            String[] tokens = schedule.split(",");
            for (String t: tokens){
                if (t.trim().length() > 0) {
                    scheduledSections.add(sectionRepository.findBySectionid(Long.decode(t.trim())));
                }
            }
            if (schedule.indexOf(possibleSection.getSectionid().toString()) == -1){
                boolean courseNotIn = true;
                for (Section ssection: scheduledSections){
                    if (possibleSection.getCourses().getCourseid() == ssection.getCourses().getCourseid()){
                        courseNotIn = false;
                    }
                }
                if (courseNotIn){
                    sectionTime = true;
                    for (MeetingTimes meetingTimes: possibleSection.getMeetingTime()){
                        for (int i=0;i<meetingTimes.getDay().length();i++){
                            for (ReservedTime reservedTime: reservedTimes){
                                if ((reservedTime.getDay().indexOf(meetingTimes.getDay().substring(i,i+1)) != -1) && (sectionTime)){
                                    LocalTime sStartTime = LocalTime.parse(meetingTimes.getStartTime().toUpperCase(),DateTimeFormatter.ofPattern("hh:mma"));
                                    LocalTime sEndTime = LocalTime.parse(meetingTimes.getEndTime().toUpperCase(),DateTimeFormatter.ofPattern("hh:mma"));
                                    LocalTime rStartTime = LocalTime.parse(reservedTime.getStartTime().toUpperCase(),DateTimeFormatter.ofPattern("hh:mma"));
                                    LocalTime rEndTime = LocalTime.parse(reservedTime.getEndTime().toUpperCase(),DateTimeFormatter.ofPattern("hh:mma"));
                                    if (!(sStartTime.isAfter(rEndTime) || sEndTime.isBefore(rStartTime))){
                                        sectionTime = false;
                                    }
                                }
                            }
                            for (Section sSection: scheduledSections){
                                for (MeetingTimes sMeetingTimes: sSection.getMeetingTime()){
                                    if ((sMeetingTimes.getDay().indexOf(meetingTimes.getDay().substring(i,i+1)) != -1) && (sectionTime)){
                                        LocalTime sStartTime = LocalTime.parse(meetingTimes.getStartTime().toUpperCase(),DateTimeFormatter.ofPattern("hh:mma"));
                                        LocalTime sEndTime = LocalTime.parse(meetingTimes.getEndTime().toUpperCase(),DateTimeFormatter.ofPattern("hh:mma"));
                                        LocalTime rStartTime = LocalTime.parse(sMeetingTimes.getStartTime().toUpperCase(),DateTimeFormatter.ofPattern("hh:mma"));
                                        LocalTime rEndTime = LocalTime.parse(sMeetingTimes.getEndTime().toUpperCase(),DateTimeFormatter.ofPattern("hh:mma"));
                                        if (!(sStartTime.isAfter(rEndTime) || sEndTime.isBefore(rStartTime))){
                                            sectionTime = false;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (sectionTime) {
                        schedule+=(possibleSection.getSectionid().toString()+", ");
                    }
                }
            }
        }
        return schedule;
    }
}

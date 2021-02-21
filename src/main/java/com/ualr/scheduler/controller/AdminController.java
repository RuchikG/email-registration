package com.ualr.scheduler.controller;

import com.ualr.scheduler.model.Course;
import com.ualr.scheduler.model.Section;
import com.ualr.scheduler.repository.CoursesRepository;
import com.ualr.scheduler.repository.RegistrationRepository;
import com.ualr.scheduler.repository.SectionRepository;
import org.dom4j.rule.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Set;

@Controller
public class AdminController {

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private CoursesRepository coursesRepository;

    @Autowired
    private SectionRepository sectionRepository;

    //@PreAuthorize("hasAnyRole('ADMIN')")
    @RequestMapping(value = "/admin",method = RequestMethod.GET)
    public ModelAndView adminUserPage(ModelAndView modelAndView){
        List<Course> courses = coursesRepository.findAll();
        modelAndView.addObject("courses",courses);
        modelAndView.setViewName("admin");
        return modelAndView;
    }

    //@PreAuthorize("hasAnyRole('ADMIN')")
    @RequestMapping(value = "/addCourse",method = RequestMethod.GET)
    public ModelAndView displayAddCourse(ModelAndView modelAndView, Course course){
        modelAndView.addObject("course",course);
        modelAndView.setViewName("addCourse");
        return modelAndView;
    }

    //@PreAuthorize("hasAnyRole('ADMIN')")
    @RequestMapping(value = "/addCourse",method = RequestMethod.POST)
    public ModelAndView addCourse(ModelAndView modelAndView, Course course){
        coursesRepository.save(course);
        modelAndView.addObject("message","The course has been added successfully!");
        modelAndView.setViewName("courseRequest");
        return modelAndView;
    }

    //@PreAuthorize("hasAnyRole('ADMIN')")
    @RequestMapping(value = "/editCourse",method = RequestMethod.GET)
    public ModelAndView displayEditCourse(ModelAndView modelAndView, @RequestParam("courseNum")String courseNum){
        Course course = coursesRepository.findByCourseNumber(Long.decode(courseNum));
        modelAndView.addObject("course",course);
        modelAndView.setViewName("editCourse");
        return modelAndView;
    }

    //@PreAuthorize("hasAnyRole('ADMIN')")
    @RequestMapping(value = "/editCourse",method = RequestMethod.POST)
    public ModelAndView editCourse(ModelAndView modelAndView, Course course){
        Course check = coursesRepository.findByCourseid(course.getCourseid());
        check.setCourseNumber(course.getCourseNumber());
        check.setCourseTitle(course.getCourseTitle());
        check.setDeptId(course.getDeptId());
        modelAndView.addObject("message","The course " + check.getCourseTitle() + " has been updated successfully!");
        modelAndView.setViewName("courseRequest");
        coursesRepository.save(check);
        return modelAndView;
    }

    //@PreAuthorize("hasAnyRole('ADMIN')")
    @RequestMapping(value = "/deleteCourse", method = {RequestMethod.GET,RequestMethod.POST})
    public ModelAndView deleteCourse(ModelAndView modelAndView, @RequestParam("courseNum")String courseNum){
        Course course = coursesRepository.findByCourseNumber(Long.decode(courseNum));
        modelAndView.addObject("message","The course " + course.getCourseTitle() + " has been deleted successfully!");
        modelAndView.setViewName("courseRequest");
        coursesRepository.delete(course);
        return modelAndView;
    }

    //@PreAuthorize("hasAnyRole('ADMIN')")
    @RequestMapping(value = "/course", method = RequestMethod.GET)
    public ModelAndView viewCourse(ModelAndView modelAndView, @RequestParam("courseNum")String courseNum){
        Course course = coursesRepository.findByCourseNumber(Long.decode(courseNum));
        Set<Section> sections = course.getSections();
        modelAndView.addObject("course",course);
        modelAndView.addObject("sections",sections);
        modelAndView.setViewName("course");
        return modelAndView;
    }

    //@PreAuthorize("hasAnyRole('ADMIN')")
    @RequestMapping(value = "/addSection",method = RequestMethod.GET)
    public ModelAndView displayAddSection(ModelAndView modelAndView, Section section){
        modelAndView.addObject("section",section);
        modelAndView.setViewName("addSection");
        return modelAndView;
    }

    //@PreAuthorize("hasAnyRole('ADMIN')")
    @RequestMapping(value = "/addSection",method = RequestMethod.POST)
    public ModelAndView addSection(ModelAndView modelAndView, Section section){
        Long courseNum = section.getCourses().getCourseNumber();
        Course course = coursesRepository.findByCourseNumber(courseNum);
        section.setCourses(course);
        modelAndView.addObject("message","The section has been added successfully!");
        modelAndView.setViewName("courseRequest");
        sectionRepository.save(section);
        return modelAndView;
    }

    /*//@PreAuthorize("hasAnyRole('ADMIN')")
    @RequestMapping(value = "/editCourse",method = RequestMethod.GET)
    public ModelAndView displayEditCourse(ModelAndView modelAndView, @RequestParam("courseNum")String courseNum){
        Course course = coursesRepository.findByCourseNumber(Long.decode(courseNum));
        modelAndView.addObject("course",course);
        modelAndView.setViewName("editCourse");
        return modelAndView;
    }

    //@PreAuthorize("hasAnyRole('ADMIN')")
    @RequestMapping(value = "/editCourse",method = RequestMethod.POST)
    public ModelAndView editCourse(ModelAndView modelAndView, Course course){
        Course check = coursesRepository.findByCourseid(course.getCourseid());
        check.setCourseNumber(course.getCourseNumber());
        check.setCourseTitle(course.getCourseTitle());
        check.setDeptId(course.getDeptId());
        modelAndView.addObject("message","The course " + check.getCourseTitle() + " has been updated successfully!");
        modelAndView.setViewName("courseRequest");
        coursesRepository.save(check);
        return modelAndView;
    }

    //@PreAuthorize("hasAnyRole('ADMIN')")
    @RequestMapping(value = "/deleteCourse", method = {RequestMethod.GET,RequestMethod.POST})
    public ModelAndView deleteCourse(ModelAndView modelAndView, @RequestParam("courseNum")String courseNum){
        Course course = coursesRepository.findByCourseNumber(Long.decode(courseNum));
        modelAndView.addObject("message","The course " + course.getCourseTitle() + " has been deleted successfully!");
        modelAndView.setViewName("courseRequest");
        coursesRepository.delete(course);
        return modelAndView;
    } */

}

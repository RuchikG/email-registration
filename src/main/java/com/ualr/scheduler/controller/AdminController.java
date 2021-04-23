package com.ualr.scheduler.controller;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.ualr.scheduler.model.Course;
import com.ualr.scheduler.model.MeetingTimes;
import com.ualr.scheduler.model.Section;
import com.ualr.scheduler.repository.CoursesRepository;
import com.ualr.scheduler.repository.MeetingtimeRepository;
import com.ualr.scheduler.repository.SectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Controller
public class AdminController {

    @Autowired
    private MeetingtimeRepository meetingtimeRepository;

    @Autowired
    private CoursesRepository coursesRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @RequestMapping(value = "/admin",method = RequestMethod.GET)
    public ModelAndView adminUserPage(ModelAndView modelAndView){
        List<Course> courses = coursesRepository.findAll();
        modelAndView.addObject("courses",courses);
        modelAndView.setViewName("admin");
        return modelAndView;
    }

    @PreAuthorize("hasAnyRole('ADMIN','ROOT')")
    @RequestMapping(value = "/addCourse",method = RequestMethod.GET)
    public ModelAndView displayAddCourse(ModelAndView modelAndView, Course course){
        modelAndView.addObject("course",course);
        modelAndView.setViewName("addCourse");
        return modelAndView;
    }

    @PreAuthorize("hasAnyRole('ADMIN','ROOT')")
    @RequestMapping(value = "/addCourse",method = RequestMethod.POST)
    public ModelAndView addCourse(ModelAndView modelAndView, Course course){
        coursesRepository.save(course);
        modelAndView.addObject("message","The course has been added successfully!");
        modelAndView.setViewName("courseRequest");
        return modelAndView;
    }

    @PreAuthorize("hasAnyRole('ADMIN','ROOT')")
    @RequestMapping(value = "/editCourse",method = RequestMethod.GET)
    public ModelAndView displayEditCourse(ModelAndView modelAndView, @RequestParam("courseId")String courseId){
        Course course = coursesRepository.findByCourseid(Long.decode(courseId));
        modelAndView.addObject("course",course);
        modelAndView.setViewName("editCourse");
        return modelAndView;
    }

    @PreAuthorize("hasAnyRole('ADMIN','ROOT')")
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

    @PreAuthorize("hasAnyRole('ADMIN','ROOT')")
    @RequestMapping(value = "/deleteCourse", method = {RequestMethod.GET,RequestMethod.POST})
    public ModelAndView deleteCourse(ModelAndView modelAndView, @RequestParam("courseId")String courseId){
        Course course = coursesRepository.findByCourseid(Long.decode(courseId));
        modelAndView.addObject("message","The course " + course.getCourseTitle() + " has been deleted successfully!");
        modelAndView.setViewName("courseRequest");
        coursesRepository.delete(course);
        return modelAndView;
    }

    @PreAuthorize("hasAnyRole('ADMIN','ROOT','STUDENT')")
    @RequestMapping(value = "/course", method = RequestMethod.GET)
    public ModelAndView viewCourse(ModelAndView modelAndView, @RequestParam("courseId")String courseId){
        Course course = coursesRepository.findByCourseid(Long.decode(courseId));
        ArrayList<Section> sections = new ArrayList<>(course.getSections());
        Collections.sort(sections);
       /* int j = 0;
        while(sections.size() != course.getSections().size()) {
            for (Section s: course.getSections()) {
                if (Integer.parseInt(s.getSectionNumber()) == (j+1)){
                    sections.add(s);
                    j++;
                }
            }
        } */
        modelAndView.addObject("course",course);
        modelAndView.addObject("sections",sections);
        modelAndView.setViewName("course");
        return modelAndView;
    }

    @PreAuthorize("hasAnyRole('ADMIN','ROOT')")
    @RequestMapping(value = "/addSection",method = RequestMethod.GET)
    public ModelAndView displayAddSection(ModelAndView modelAndView, @RequestParam("courseId")String courseId,Section section){
        section.setCourses(new Course());
        section.getCourses().setCourseid(Long.parseLong(courseId));
        modelAndView.addObject("section",section);
        //modelAndView.addObject("courseNumber",Long.parseLong(courseNum));
        modelAndView.setViewName("addSection");
        return modelAndView;
    }

    @PreAuthorize("hasAnyRole('ADMIN','ROOT')")
    @RequestMapping(value = "/addSection",method = RequestMethod.POST)
    public ModelAndView addSection(ModelAndView modelAndView, Section section){
        Long courseId = section.getCourses().getCourseid();
        Course course = coursesRepository.findByCourseid(courseId);
        section.setCourses(course);
        modelAndView.addObject("message","The section has been added successfully!");
        modelAndView.setViewName("courseRequest");
        sectionRepository.save(section);
        return modelAndView;
    }

    @PreAuthorize("hasAnyRole('ADMIN','ROOT')")
    @RequestMapping(value = "/editSection",method = RequestMethod.GET)
    public ModelAndView displayEditSection(ModelAndView modelAndView, @RequestParam("sectionId")String sectionId){
        Section section = sectionRepository.findBySectionid(Long.decode(sectionId));
        modelAndView.addObject("section",section);
        modelAndView.setViewName("editSection");
        return modelAndView;
    }

    @PreAuthorize("hasAnyRole('ADMIN','ROOT')")
    @RequestMapping(value = "/editSection",method = RequestMethod.POST)
    public ModelAndView editSection(ModelAndView modelAndView, Section section){
        Section check = sectionRepository.findBySectionid(section.getSectionid());
        check.setSectionNumber(section.getSectionNumber());
        check.setInstructor(section.getInstructor());
        modelAndView.addObject("message","The section has been updated successfully!");
        modelAndView.setViewName("courseRequest");
        sectionRepository.save(check);
        return modelAndView;
    }

    @PreAuthorize("hasAnyRole('ADMIN','ROOT')")
    @RequestMapping(value = "/deleteSection", method = {RequestMethod.GET,RequestMethod.POST})
    public ModelAndView deleteSection(ModelAndView modelAndView, @RequestParam("sectionId")String sectionId){
        Section section = sectionRepository.findBySectionid(Long.decode(sectionId));
        modelAndView.addObject("message","The section has been deleted successfully!");
        modelAndView.setViewName("courseRequest");
        section.getCourses().getSections().remove(section);
        sectionRepository.delete(section);
        return modelAndView;
    }

    @PreAuthorize("hasAnyRole('ADMIN','ROOT','STUDENT')")
    @RequestMapping(value = "/section", method = RequestMethod.GET)
    public ModelAndView viewSection(ModelAndView modelAndView, @RequestParam("sectionId")String sectionId){
        Section section = sectionRepository.findBySectionid(Long.decode(sectionId));
        Set<MeetingTimes> meetingTimes = section.getMeetingTime();
        modelAndView.addObject("section",section);
        modelAndView.addObject("meetingTimes",meetingTimes);
        modelAndView.setViewName("section");
        return modelAndView;
    }

    @PreAuthorize("hasAnyRole('ADMIN','ROOT')")
    @RequestMapping(value = "/addMeetingtime",method = RequestMethod.GET)
    public ModelAndView displayAddMeetingTime(ModelAndView modelAndView,@RequestParam("sectionId")String sectionId, MeetingTimes meetingTimes){
        meetingTimes.setSections(new Section());
        meetingTimes.getSections().setSectionid(Long.decode(sectionId));
        modelAndView.addObject("meetingtime",meetingTimes);
        modelAndView.setViewName("addMeetingtime");
        return modelAndView;
    }

    @PreAuthorize("hasAnyRole('ADMIN','ROOT')")
    @RequestMapping(value = "/addMeetingtime",method = RequestMethod.POST)
    public ModelAndView addMeetingTime(ModelAndView modelAndView, MeetingTimes meetingTimes){
        Long sectionid = meetingTimes.getSections().getSectionid();
        Section section = sectionRepository.findBySectionid(sectionid);
        meetingTimes.setSections(section);
        meetingTimes.setStartTime(meetingTimes.getStartTime());
        meetingTimes.setEndTime(meetingTimes.getEndTime());
        modelAndView.addObject("message","The meeting time has been added successfully!");
        modelAndView.setViewName("courseRequest");
        meetingtimeRepository.save(meetingTimes);
        return modelAndView;
    }

    @PreAuthorize("hasAnyRole('ADMIN','ROOT')")
    @RequestMapping(value = "/editMeetingtime",method = RequestMethod.GET)
    public ModelAndView displayEditMeetingTime(ModelAndView modelAndView, @RequestParam("meetingId")String meetingId){
        MeetingTimes meetingTimes = meetingtimeRepository.findByMeetingId(Long.decode(meetingId));
        modelAndView.addObject("meetingtime",meetingTimes);
        modelAndView.setViewName("editMeetingtime");
        return modelAndView;
    }

    @PreAuthorize("hasAnyRole('ADMIN','ROOT')")
    @RequestMapping(value = "/editMeetingtime",method = RequestMethod.POST)
    public ModelAndView editMeetingTime(ModelAndView modelAndView, MeetingTimes meetingTimes){
        MeetingTimes check = meetingtimeRepository.findByMeetingId(meetingTimes.getMeetingId());
        check.setDay(meetingTimes.getDay());
        check.setStartTime(meetingTimes.getStartTime());
        check.setEndTime(meetingTimes.getEndTime());
        modelAndView.addObject("message","The meeting time has been updated successfully!");
        modelAndView.setViewName("courseRequest");
        meetingtimeRepository.save(check);
        return modelAndView;
    }

    @PreAuthorize("hasAnyRole('ADMIN','ROOT')")
    @RequestMapping(value = "/deleteMeetingtime", method = {RequestMethod.GET,RequestMethod.POST})
    public ModelAndView deleteMeetingTime(ModelAndView modelAndView, @RequestParam("meetingId")String meetingId){
        MeetingTimes meetingTimes = meetingtimeRepository.findByMeetingId(Long.decode(meetingId));
        modelAndView.addObject("message","The meeting time has been deleted successfully!");
        modelAndView.setViewName("courseRequest");
        meetingTimes.getSections().getMeetingTime().remove(meetingTimes);
        meetingtimeRepository.delete(meetingTimes);
        return modelAndView;
    }

    @PreAuthorize("hasAnyRole('ADMIN','ROOT')")
    @RequestMapping(value = "/uploadCourseFile", method = RequestMethod.GET)
    public ModelAndView displayFileUpload(ModelAndView modelAndView){
        modelAndView.setViewName("courseFileUpload");
        return modelAndView;
    }

    @PreAuthorize("hasAnyRole('ADMIN','ROOT')")
    @RequestMapping(value = "/uploadCourseFile", method = RequestMethod.POST)
    public ModelAndView fileUploadCourse(ModelAndView modelAndView, @RequestParam("file") MultipartFile file){
        if (file.isEmpty()){
            modelAndView.addObject("message", "Please select a CSV file to upload.");
            modelAndView.setViewName("courseRequest");
        } else {
            try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                CsvToBean<Course> csvToBean = new CsvToBeanBuilder(reader)
                                                    .withType(Course.class)
                                                    .withIgnoreLeadingWhiteSpace(true)
                                                    .build();
                List<Course> courses = csvToBean.parse();
                coursesRepository.saveAll(courses);
                modelAndView.addObject("message","All the courses have been added to the catalog");
                modelAndView.setViewName("courseRequest");
            } catch (Exception ex){
                modelAndView.addObject("message", "An error occurred while processing the CSV file");
                modelAndView.setViewName("courseRequest");
            }
        }

        return modelAndView;
    }

    @PreAuthorize("hasAnyRole('ADMIN','ROOT')")
    @RequestMapping(value = "/uploadSectionFile", method = RequestMethod.GET)
    public ModelAndView displaySectionUpload(ModelAndView modelAndView){
        modelAndView.setViewName("sectionFileUpload");
        return modelAndView;
    }

    @PreAuthorize("hasAnyRole('ADMIN','ROOT')")
    @RequestMapping(value = "/uploadSectionFile", method = RequestMethod.POST)
    public ModelAndView fileUploadSection(ModelAndView modelAndView, @RequestParam("file") MultipartFile file){
        if (file.isEmpty()){
            modelAndView.addObject("message", "Please select a CSV file to upload.");
            modelAndView.setViewName("courseRequest");
        } else {
            try (CSVReader reader = new CSVReaderBuilder(new InputStreamReader(file.getInputStream())).withSkipLines(1).build()){
                String[] record;
                List<Section> sections = new ArrayList<>();
                List<MeetingTimes> meetingTimes = new ArrayList<>();
                while ((record = reader.readNext()) != null){
                    Course course = coursesRepository.findByCourseNumberAndDeptIdAndCourseTitle(Long.decode(record[1]),record[0],record[2]);
                    Section section = new Section(record[3],course,record[4]);
                    sections.add(section);
                    if (record[5].indexOf(",") != -1){
                        String[] period = record[5].split(",");
                        for (int i = 0; i < period.length; i++){
                            String t = period[i];
                            if (i > 0){
                                t = t.substring(1);
                            }
                            String[] time = t.split(" ");
                            String[] timing = time[1].split("-");
                            MeetingTimes m = new MeetingTimes(time[0],timing[0],timing[1],section);
                            meetingTimes.add(m);
                        }
                    } else {
                        String[] time = record[5].split(" ");
                        String[] timing = time[1].split("-");
                        MeetingTimes m = new MeetingTimes(time[0],timing[0],timing[1],section);
                        meetingTimes.add(m);
                    }
                }
                sectionRepository.saveAll(sections);
                meetingtimeRepository.saveAll(meetingTimes);
                modelAndView.addObject("message","All the sections have been added to the catalog");
                modelAndView.setViewName("courseRequest");
            }catch (Exception ex){
                modelAndView.addObject("message", "An error occurred while processing the CSV file");
                modelAndView.setViewName("courseRequest");
            }
        }
        return modelAndView;
    }
}

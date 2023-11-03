package com.chatbot.chatbot.controllers;

import com.chatbot.chatbot.models.Course;
import com.chatbot.chatbot.models.Subject;
import com.chatbot.chatbot.repositories.CourseDao;
import com.chatbot.chatbot.services.PythonService;
import com.chatbot.chatbot.services.SeleniumService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController()
@RequestMapping("/courses")
public class CourseController {
    private final SeleniumService seleniumService;
    private final PythonService pythonService;
    private final CourseDao courseDao;

    @PostMapping("/populate")
    public List<Course> populateCourses() {
        List<Course> courses = seleniumService.getAllCourses();

        if (!courses.isEmpty()) {
            log.info("Successfully scraped {} courses from site. Deleting old ones and inserting them into database", courses.size());
            courseDao.deleteAll();
            courseDao.saveAll(courses);
        } else {
            log.error("Endpoint /courses/populate would delete all courses, but wouldn't insert any");
        }

        return courses;
    }

    @GetMapping("/all")
    public List<Subject> getAllSubjects() throws JSONException {
        return pythonService.getAllSubjects();
    }

}

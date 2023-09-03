package com.chatbot.chatbot.implementations;

import com.chatbot.chatbot.models.Employee;
import com.chatbot.chatbot.models.Course;
import com.chatbot.chatbot.services.SeleniumService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class SeleniumServiceImpl implements SeleniumService {

    private static final String EMPLOYEE_URL = "https://imi.pmf.kg.ac.rs/nastavno-osoblje";
    private static final String MOODLE_URL = "https://imi.pmf.kg.ac.rs/moodle/";
    private static final String EMPLOYEE_ROW_CLASS = "nastavnici_row";
    private static final String EMPLOYEE_CELL_NAME = "ime_nastavnika";
    private static final String EMPLOYEE_CELL_TITLE = "zvanje_nastavnika";
    private static final String EMPLOYEE_CELL_CABINET = "kabinet_nastavnika";
    private static final String EMPLOYEE_CELL_LOCALE = "lokal_nastavnika";
    private static final String EMPLOYEE_CELL_EMAIL = "mail_nastavnika";
    private static final String EMPLOYEE_CELL_CONSULTATION = "konsultacije_nastavnika";
    private static final List<String> OAS_LIST = Arrays.asList("OAS MATEMATIKE", "OAS INFORMATIKE");
    private static final List<String> MAS_LIST = Arrays.asList("MAS MATEMATIKE", "MAS INFORMATIKE");

    private void setUp() {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();
        webDriver = new FirefoxDriver(options);
    }

    private WebDriver webDriver;

    @Override
    @Cacheable("employees")
    public List<Employee> getAllEmployees() {

        List<Employee> employees = new ArrayList<>();

        setUp();
        webDriver.get(EMPLOYEE_URL);

        List<WebElement> employeeRows = webDriver.findElements(By.className(EMPLOYEE_ROW_CLASS));

        for (WebElement employeeRow : employeeRows) {
            employees.add(
                    Employee.builder().name(getCellPropertyByClassName(employeeRow, EMPLOYEE_CELL_NAME))
                    .title(getCellPropertyByClassName(employeeRow, EMPLOYEE_CELL_TITLE))
                    .cabinet(getCellPropertyByClassName(employeeRow, EMPLOYEE_CELL_CABINET))
                    .locale(getCellPropertyByClassName(employeeRow, EMPLOYEE_CELL_LOCALE))
                    .email(getCellPropertyByClassName(employeeRow, EMPLOYEE_CELL_EMAIL))
                    .consultation(getCellPropertyByClassName(employeeRow, EMPLOYEE_CELL_CONSULTATION))
                    .build()
            );
        }
        webDriver.quit();

        return employees;
    }

    @Override
    @Cacheable("courses")
    public List<Course> getAllCourses() {

        List<Course> courses = new ArrayList<>();

        List<String> elementaryUrls = new ArrayList<>();
        List<String> masterUrls = new ArrayList<>();
        List<String> elementaryYearsUrls = new ArrayList<>();
        List<String> elementarySemesterUrls = new ArrayList<>();
        List<String> elementaryClassUrls = new ArrayList<>();

        setUp();
        webDriver.get(MOODLE_URL);

        for(String oas : OAS_LIST){
            WebElement course = webDriver.findElement(By.linkText(oas));
            elementaryUrls.add(course.getAttribute("href"));
        }

        for(String mas : MAS_LIST){
            WebElement course = webDriver.findElement(By.linkText(mas));
            masterUrls.add(course.getAttribute("href"));
        }

        for(String elementaryUrl : elementaryUrls){
            webDriver.get(elementaryUrl);

            List<WebElement> years = webDriver.findElements(By.partialLinkText(" godina"));
            for(WebElement year : years){
                elementaryYearsUrls.add(year.getAttribute("href"));
            }
        }

        for (String yearUrl : elementaryYearsUrls){
            webDriver.get(yearUrl);

            List<WebElement> semesters = webDriver.findElements(By.partialLinkText(" semestar"));
            for(WebElement semester : semesters){
                elementarySemesterUrls.add(semester.getAttribute("href"));
            }
        }

        for(String elementarySemesterUrl : elementarySemesterUrls){
            webDriver.get(elementarySemesterUrl);

            List<WebElement> semesterCourses = webDriver.findElements(By.className("aalink"));

            for(WebElement course : semesterCourses){
                courses.add(
                        Course.builder()
                                .name(course.getText())
                                .url(course.getAttribute("href"))
                                .build()
                );
            }
        }

        for (Course course : courses){
            populateCourseProfessor(course);
        }

        webDriver.quit();


        return courses;
    }

    private void populateCourseProfessor(Course course){
        webDriver.get(course.getUrl());
        if(isElementPresent(webDriver, By.xpath(".//*[text()[contains(.,\"Predmetni nastavn\")]]"))){
            WebElement element = webDriver.findElement(By.xpath(".//*[text()[contains(.,\"Predmetni nastavn\")]]"));
            course.setProfessor(element.getText());

            if (element.getText().equals("Predmetni nastavnik:")) {
                if(isElementPresent(webDriver, By.xpath(".//*[text()[contains(.,\"Predmetni nastavn\")]]/following-sibling::*[1]"))) {
                    element = webDriver.findElement(By.xpath(".//*[text()[contains(.,\"Predmetni nastavn\")]]/following-sibling::*[1]"));
                    course.setProfessor(element.getText());
                }
            }

            if (element.getText().equals("Predmetni nastavnik:")) {
                System.out.println(element.getAttribute("innerHTML"));
                if(isElementPresent(webDriver, By.xpath(".//*[text()[contains(.,\"Predmetni nastavn\")]]/following-sibling::b[1]/a"))) {
                    element = webDriver.findElement(By.xpath(".//*[text()[contains(.,\"Predmetni nastavn\")]]/following-sibling::b[1]/a"));
                    System.out.println(element.getText());
                }
            }
        }
    }

    private String getCellPropertyByClassName(WebElement employeeRow, String className) {
        WebElement cell = null;

        if (isElementPresent(employeeRow, By.className(className))){
            cell = employeeRow.findElement(By.className(className));
        }

        return cell != null ? cell.getText() : null;
    }

    private boolean isElementPresent(WebElement element, By by) {
        try {
            element.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private boolean isElementPresent(WebDriver webDriver, By by) {
        try{
            webDriver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }
}

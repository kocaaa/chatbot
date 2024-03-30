package com.chatbot.chatbot.implementations;

import com.chatbot.chatbot.models.Course;
import com.chatbot.chatbot.models.Employee;
import com.chatbot.chatbot.models.ExaminationPeriod;
import com.chatbot.chatbot.services.SeleniumService;
import com.chatbot.chatbot.utils.LatinUtil;
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
    public static final String DIV_CUSTOM_PANEL = "div.custom-panel";
    public static final String ROW_SELECTOR = "div.row:not(div.row > div.row)";
    private static final String EMPLOYEE_URL = "https://imi.pmf.kg.ac.rs/nastavno-osoblje";
    private static final String MOODLE_URL = "https://imi.pmf.kg.ac.rs/moodle/";
    private static final String EXAMINATION_PERIODS_URL = "https://www.pmf.kg.ac.rs/?id=527";
    private static final String REGISTRATION_STRING = "ПРИЈАВЉИВАЊЕ ИСПИТА ЗА";
    private static final String EMPLOYEE_ROW_CLASS = "nastavnici_row";
    private static final String EMPLOYEE_CELL_NAME = "ime_nastavnika";
    private static final String EMPLOYEE_CELL_TITLE = "zvanje_nastavnika";
    private static final String EMPLOYEE_CELL_CABINET = "kabinet_nastavnika";
    private static final String EMPLOYEE_CELL_LOCALE = "lokal_nastavnika";
    private static final String EMPLOYEE_CELL_EMAIL = "mail_nastavnika";
    private static final String EMPLOYEE_CELL_CONSULTATION = "konsultacije_nastavnika";
    private static final String PARTIAL_LINK_YEAR = " godina";
    private static final String PARTIAL_LINK_SEMESTER = " semestar";
    private static final String HREF_TAG = "href";
    private static final List<String> OAS_LIST = Arrays.asList("OAS MATEMATIKE", "OAS INFORMATIKE");
    private static final List<String> MAS_LIST = Arrays.asList("MAS MATEMATIKE", "MAS INFORMATIKE");
    private WebDriver webDriver;

    private void setUp(String url) {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();
        webDriver = new FirefoxDriver(options);
        webDriver.get(url);
    }

    @Override
    @Cacheable("employees")
    public List<Employee> getAllEmployees() {
        setUp(EMPLOYEE_URL);
        List<WebElement> employeeRows = webDriver.findElements(By.className(EMPLOYEE_ROW_CLASS));
        return extractEmployees(employeeRows);
    }

    @Override
    public List<ExaminationPeriod> getAllExaminationPeriods() {
        setUp(EXAMINATION_PERIODS_URL);

        List<ExaminationPeriod> examinationPeriods = new ArrayList<>();

        List<WebElement> examinationPeriodElements = webDriver.findElements(By.cssSelector(DIV_CUSTOM_PANEL))
                .get(0)
                .findElements(By.cssSelector(ROW_SELECTOR));


        return extractExaminationPeriods(examinationPeriodElements);
    }

    private List<ExaminationPeriod> extractExaminationPeriods(List<WebElement> examinationPeriodElements) {
        List<ExaminationPeriod> examinationPeriods = new ArrayList<>();

        for (WebElement element : examinationPeriodElements) {
            List<String> strings = List.of(element.getText().split("\n"));

            examinationPeriods.add(
                    ExaminationPeriod.builder()
                            .month(LatinUtil.convertCyrillicToLatin(strings.get(0)).toLowerCase())
                            .date(LatinUtil.convertCyrillicToLatin(getExaminationPeriodDate(strings)).toLowerCase())
                            .build()
            );
        }

        webDriver.quit();

        return examinationPeriods;
    }

    private String getExaminationPeriodDate(List<String> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).startsWith(REGISTRATION_STRING) && i + 1 < list.size()) {
                return new StringBuilder(
                        new StringBuilder(list.get(i + 1).replaceFirst("\\.\\s", "."))
                                .reverse()
                                .toString()
                                .replaceFirst("\\s\\.", ".")
                                .replaceFirst("\\s\\.", ".")
                ).reverse().toString();
            }
        }

        return "invalid value";
    }

    @Deprecated
    @Override
    @Cacheable("courses")
    public List<Course> getAllCourses() {
        List<String> elementaryClassUrls = new ArrayList<>();

        setUp(MOODLE_URL);

        List<String> elementaryUrls = getDepartmentUrls(OAS_LIST);
        List<String> masterUrls = getDepartmentUrls(MAS_LIST);

        List<String> elementaryYearsUrls = getUrlsByPartialLinkText(elementaryUrls, PARTIAL_LINK_YEAR);
        List<String> elementarySemesterUrls = getUrlsByPartialLinkText(elementaryYearsUrls, PARTIAL_LINK_SEMESTER);

        List<Course> courses = extractCourses(elementarySemesterUrls);

        for (Course course : courses) {
            populateCourseProfessor(course);
        }

        webDriver.quit();


        return courses;
    }

    private List<Course> extractCourses(List<String> elementarySemesterUrls) {
        List<Course> courses = new ArrayList<>();

        for (String elementarySemesterUrl : elementarySemesterUrls) {
            webDriver.get(elementarySemesterUrl);

            List<WebElement> semesterCourses = webDriver.findElements(By.className("aalink"));

            for (WebElement course : semesterCourses) {
                courses.add(
                        Course.builder()
                                .name(course.getText())
                                .url(course.getAttribute(HREF_TAG))
                                .build()
                );
            }
        }

        return courses;
    }

    private List<String> getUrlsByPartialLinkText(List<String> elementaryUrls, String partialLinkText) {
        List<String> urls = new ArrayList<>();

        for (String elementaryUrl : elementaryUrls) {
            webDriver.get(elementaryUrl);

            List<WebElement> years = webDriver.findElements(By.partialLinkText(partialLinkText));
            for (WebElement year : years) {
                urls.add(year.getAttribute(HREF_TAG));
            }
        }

        return urls;
    }

    private List<String> getDepartmentUrls(List<String> departments) {
        List<String> urls = new ArrayList<>();

        for (String department : departments) {
            WebElement course = webDriver.findElement(By.linkText(department));
            urls.add(course.getAttribute(HREF_TAG));
        }

        return urls;
    }

    private List<Employee> extractEmployees(List<WebElement> employeeRows) {
        List<Employee> employees = new ArrayList<>();

        for (WebElement employeeRow : employeeRows) {
            employees.add(
                    Employee.builder()
                            .name(getCellPropertyByClassName(employeeRow, EMPLOYEE_CELL_NAME))
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

    private void populateCourseProfessor(Course course) {
        webDriver.get(course.getUrl());
        if (isElementPresent(By.xpath(".//*[text()[contains(.,\"Predmetni nastavn\")]]"))) {
            WebElement element = webDriver.findElement(By.xpath(".//*[text()[contains(.,\"Predmetni nastavn\")]]"));
            course.setProfessor(element.getText());

            if (element.getText().equals("Predmetni nastavnik:")) {
                if (isElementPresent(By.xpath(".//*[text()[contains(.,\"Predmetni nastavn\")]]/following-sibling::*[1]"))) {
                    element = webDriver.findElement(By.xpath(".//*[text()[contains(.,\"Predmetni nastavn\")]]/following-sibling::*[1]"));
                    course.setProfessor(element.getText());
                }
            }

            if (element.getText().equals("Predmetni nastavnik:")) {
                if (isElementPresent(By.xpath(".//*[text()[contains(.,\"Predmetni nastavn\")]]/following-sibling::b[1]/a"))) {
                    element = webDriver.findElement(By.xpath(".//*[text()[contains(.,\"Predmetni nastavn\")]]/following-sibling::b[1]/a"));
                }
            }
        }
    }

    private String getCellPropertyByClassName(WebElement employeeRow, String className) {
        WebElement cell = isElementPresent(employeeRow, By.className(className)) ? employeeRow.findElement(By.className(className)) : null;
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

    private boolean isElementPresent(By by) {
        try {
            webDriver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }
}

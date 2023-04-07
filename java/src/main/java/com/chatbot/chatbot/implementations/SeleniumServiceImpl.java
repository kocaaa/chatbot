package com.chatbot.chatbot.implementations;

import com.chatbot.chatbot.models.Employee;
import com.chatbot.chatbot.services.SeleniumService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SeleniumServiceImpl implements SeleniumService {

    private static final String EMPLOYEE_URL = "https://imi.pmf.kg.ac.rs/nastavno-osoblje";
    private static final String EMPLOYEE_ROW_CLASS = "nastavnici_row";
    private static final String EMPLOYEE_CELL_NAME = "ime_nastavnika";
    private static final String EMPLOYEE_CELL_TITLE = "zvanje_nastavnika";
    private static final String EMPLOYEE_CELL_CABINET = "ime_nastavnika";
    private static final String EMPLOYEE_CELL_LOCALE = "lokal_nastavnika";
    private static final String EMPLOYEE_CELL_EMAIL = "mail_nastavnika";
    private static final String EMPLOYEE_CELL_CONSULTATION = "konsultacije_nastavnika";


    private void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        webDriver = new ChromeDriver(options);
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
}

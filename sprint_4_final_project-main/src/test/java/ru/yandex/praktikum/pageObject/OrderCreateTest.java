package ru.yandex.praktikum.pageObject;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static ru.yandex.praktikum.pageObject.constants.CreateOrderButton.BASEMENT_BUTTON;
import static ru.yandex.praktikum.pageObject.constants.CreateOrderButton.CAP_BUTTON;
import static ru.yandex.praktikum.pageObject.constants.RentDurationConstants.*;
import static ru.yandex.praktikum.pageObject.constants.ScooterColours.*;

@RunWith(Parameterized.class)
public class OrderCreateTest {
    private WebDriver driver;
    private final String site = "https://qa-scooter.praktikum-services.ru/";
    private final String name;
    private final String surname;
    private final String address;
    private final int stateMetroNumber;
    private final String telephoneNumber;
    private final String date;
    private final String duration;
    private final Enum colour;
    private final String comment;
    private final String expectedHeader = "Заказ оформлен";
    private final Enum button;

    public OrderCreateTest(Enum button, String name, String surname, String address, int stateMetroNumber, String telephoneNumber,
                           String date, String duration, Enum colour, String comment) {
        this.button = button;
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.stateMetroNumber = stateMetroNumber;
        this.telephoneNumber = telephoneNumber;
        this.date = date;
        this.duration = duration;
        this.colour = colour;
        this.comment = comment;
    }

    @Parameterized.Parameters
    public static Object[][] getParameters() {
        return new Object[][]{
                {CAP_BUTTON, "Петр", "Петров", "Ленина 1", 1, "79880276622", "14.01.2024", DAYS_4, BLACK_PEARLS, "Оставить у двери"},
                {CAP_BUTTON, "Иван", "Иванов", "Карбышева 2", 2, "79880276623", "14.01.2024", DAY_1, BLACK_PEARLS, "Позвонить перед приездом"},
                {CAP_BUTTON, "Сергей", "Сергеев", "Энгельса 3", 3, "79880276624", "14.01.2024", DAYS_6, GREY_DESPAIR, "Не звонить"},
                {BASEMENT_BUTTON, "Мария", "Марьина", "Логинова 4", 4, "79880276625", "14.01.2024", DAYS_7, GREY_DESPAIR, "Взять размен"},
                {BASEMENT_BUTTON, "Семен", "Семенов", "Набережная 5", 5, "79880276626", "14.01.2024", DAYS_2, GREY_DESPAIR, "Во второй половине дня"},
                {BASEMENT_BUTTON, "Григорий", "Григорьев", "Королева 6", 6, "79880276627", "14.01.2024", DAYS_7, BLACK_PEARLS, "Накачать колеса"},
        };
    }

    @Before
    public void startUp() {
        WebDriverManager.firefoxdriver().setup();
        driver = new FirefoxDriver();
        driver.get(site);
    }

    @After
    public void teardown() {
        driver.quit();
    }

    @Test
    public void testCreateOrderWithUpButton() {
        new HomePage(driver)
                .waitForLoadHomePage()
                .clickCreateOrderButton(button);

        new AboutRenter(driver)
                .waitForLoadOrderPage()
                .inputName(name)
                .inputSurname(surname)
                .inputAddress(address)
                .changeStateMetro(stateMetroNumber)
                .inputTelephone(telephoneNumber)
                .clickNextButton();

        new AboutScooter(driver)
                .waitAboutRentHeader()
                .inputDate(date)
                .inputDuration(duration)
                .changeColour(colour)
                .inputComment(comment)
                .clickButtonCreateOrder();

        PopUpWindow popUpWindow = new PopUpWindow(driver);
                popUpWindow.clickButtonYes();

        assertTrue(popUpWindow.getHeaderAfterCreateOrder().contains(expectedHeader));
    }
}

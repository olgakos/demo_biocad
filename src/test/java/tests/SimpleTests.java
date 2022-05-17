package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import helpers.Attach;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

@DisplayName("Страницы Контакты")
//https://biocad.ru/contacts

public class SimpleTests {

    /*
    @BeforeAll
    static void openPage() {
        Configuration.browserSize = "1920x1080";
        open("https://biocad.ru");
    }

     */
    @BeforeEach
    public void beforeEach() {
        String browser = System.getProperty("browser", "chrome");
        String size = System.getProperty("size", "1920x1080");


        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
        Configuration.browserSize = size;
        Configuration.browser = browser;
        //Configuration.remote = "https://user1:1234@selenoid.autotests.cloud/wd/hub";

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("enableVNC", true);
        capabilities.setCapability("enableVideo", true);
        Configuration.browserCapabilities = capabilities;


        Attach.attachAsText("Browser: ", browser);
        Attach.attachAsText("Size: ", size);

        clearBrowserCookies();
        Configuration.baseUrl = "https://biocad.ru";
        open("");

    }

    @AfterEach
    @DisplayName("Формирование артефактов тестирования")
    void addAttachment() {
        Attach.screenshotAs("Last screenshot");
        Attach.addVideo();
        Attach.pageSource();
        Attach.browserConsoleLogs();
    }

    /*
    @AfterEach
    void closeBrowser() {
        closeWebDriver();
    }
     */

    @AfterAll
    public static void afterAll() {
        closeWebDriver();
    }


    @Tag("homepageTests")
    @DisplayName("Проверка текстов на странице HomePage")
    @Test
    void searchTextElement() {
        open("");
        //open("https://biocad.ru/");
        $(".content h2").shouldHave(text("Добро пожаловать в BIOCAD — международную инновационную биотехнологическую компанию полного цикла"));
        $(".logo[href*='/']").click(); // логотип
        $(".nav-toggle").click(); //сэндвич-меню
        $(".menu a[href*='/we']").shouldBe(visible); // "О компании"
        $("span.root").shouldHave(text("Продукты")); //текст на ссылке
        $("span.root").click();
        $(".menu a[href*='/products']").shouldBe(visible); //"Все продукты"
        //$("span.root").shouldHave(text("Наука"));
        //$("span.root").click();
        $(By.linkText("Производство")).shouldBe(visible);
        $(By.linkText("Карьера")).shouldBe(visible);
        $(By.linkText("Устойчивое развитие")).shouldBe(visible);
        $(By.linkText("Пресс-центр")).shouldBe(visible);
        $(By.linkText("Контакты")).shouldBe(visible);
        $(By.linkText("Поставщикам")).shouldBe(visible);
        $(By.linkText("Eng")).shouldBe(visible);
        $(".blue").click(); // кнопка "О КОМПАНИИ"
    }

    @Tag("contactsTests")
    @DisplayName("Проверка текстов на странице Контакты")
    @Test
    void expectTextTest() {
        open("/contacts");
        $(".form-contact h2").shouldHave(text("ОТПРАВИТЬ СООБЩЕНИЕ"));
        $(byText("ПОЛЯ ОТМЕЧЕННЫЕ * ОБЯЗАТЕЛЬНЫ")).isDisplayed();
        $$(".requisites").find(text("Полное наименование:")).shouldBe(visible, Duration.ofSeconds(10));
        $(By.linkText("Загрузить файл")).isDisplayed();
    }

    @Tag("contactsTests")
    @DisplayName("Отправки формы (заполнены не все поля)")
    @Test
    void fillContactFormTests() {
        open("/contacts");
       // $("input[placeholder='Enter your phone number']").setValue("+7921781хххх"); //example
        $("input[aria-activedescendant='role_list_0']").click();
        $(byText("Медицинский/Фармацевтический работник")).isDisplayed();
        $(byText("Медицинский/Фармацевтический работник")).click();
        $("#topic").click();
        $(byText("Отдел организации спортивных мероприятий")).isDisplayed();
        $(byText("Отдел организации спортивных мероприятий")).click();
        $(".name").setValue("Kos");
        $(".email").setValue("ok@dex.ru");
        $(".company").setValue("Название организации");
        $(".position").setValue("Должность");
        $(".phone").setValue("Контактный телефон"); // todo: Нет ограничений на формат ввода int
        //$("#rc_select_2").click(); //Препарат
        $("#product").click(); //Препарат
        $(byText("Фортека")).isDisplayed();
        $(byText("Фортека")).click();
        $(".msg").isDisplayed(); // ! намеренно не заполняю обязательное поле СООБЩЕНИЕ*
        $(byText("Загрузить файл")).isDisplayed();
        $(".row-agree_service  .ant-checkbox-input").click(); // чекбокс=да
        $(".row-agree_personal .ant-checkbox-input").click(); // чекбокс=да
        $("button.green").click(); // отправить
        //Expect (окно предупреждения) :
        $$(".ant-notification-bottomRight").find(text("Поля отмеченные * обязательны для заполнения")).shouldBe(visible, Duration.ofSeconds(10));
        //$$(".ant-notification-bottomRight").find(text("Тра ля ля")).shouldBe(visible, Duration.ofSeconds(10)); //negative test
        //sleep(Long.parseLong("5000"));
        }
}

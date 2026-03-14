package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

import java.time.Duration;

@Tag("SMOKE")
public class PracticeFormMultipleUsersTest {

    @BeforeAll
    static void setUp() {
        Configuration.browserSize = "1920x1080";
        Configuration.pageLoadStrategy = "eager";
        Configuration.baseUrl = "https://demoqa.com/";
        Configuration.timeout = 10000;
    }

    @BeforeEach
    void openPracticeForm() {
        open("");
        executeJavaScript("""
                document.getElementById('fixedban')?.remove();
                document.querySelector('footer')?.remove();
                """);

        SelenideElement formsElement = $(byText("Forms"));
        formsElement.shouldBe(visible, Duration.ofSeconds(10));
        formsElement.click();
        $$(".router-link").findBy(text("Practice Form")).click();
    }

    @DisplayName("Проверка формы на разных значениях")
    @ParameterizedTest
    @CsvSource({
            "Oleg, Razumov, razumov@mail.ru, Male, 9997776655, 14 January 1977, English, Music, Ulitsa Lenina 5, Uttar Pradesh, Merrut",
            "Anna, Ivanova, anna@mail.ru, Female, 8885552233, 25 December 1990, Economics, Sports, Ulitsa Mira 10, NCR, Delhi",
            "Petr, Sidorov, petr@mail.ru, Male, 7774441111, 03 March 1985, English, Reading, Ulitsa Dobra 15, Rajasthan, Jaipur"
    })
    void testFormSubmissionWithDifferentUsers(
            String firstName, String lastName, String email, String gender,
            String mobile, String birthDate, String subject, String hobby,
            String address, String state, String city) {

        // Заполнение формы
        $("#firstName").setValue(firstName);
        $("#lastName").setValue(lastName);
        $("#userEmail").setValue(email);

        // Выбор пола (динамический)
        $(byText(gender)).click();

        $("#userNumber").setValue(mobile);

        // Выбор даты рождения (упрощенно - разбиваем параметр)
        String[] birthParts = birthDate.split(" ");
        String day = birthParts[0];
        String month = birthParts[1];
        String year = birthParts[2];

        $("#dateOfBirthInput").click();
        $(".react-datepicker__month-select").selectOption(month);
        $(".react-datepicker__year-select").selectOption(year);
        $(".react-datepicker__day--0" + (day.length() == 1 ? "0" + day : day) +
                ":not(.react-datepicker__day--outside-month)").shouldBe(visible).click();

        // Предмет и хобби
        $("#subjectsInput").setValue(subject.substring(0, 2)).pressEnter();
        $(byText(hobby)).click();

        // Адрес
        $("#currentAddress").setValue(address);

        // Штат и город
        $("#state").click();
        $(byText(state)).click();
        $("#city").click();
        $(byText(city)).click();

        // Отправка
        $("#submit").click();

        // Проверки
        $(".modal-content").shouldBe(visible);

        // Проверяем все введенные данные
        $(".table-responsive").$(byText("Student Name")).parent()
                .shouldHave(text(firstName + " " + lastName));
        $(".table-responsive").$(byText("Student Email")).parent()
                .shouldHave(text(email));
        $(".table-responsive").$(byText("Gender")).parent()
                .shouldHave(text(gender));
        $(".table-responsive").$(byText("Mobile")).parent()
                .shouldHave(text(mobile));
        $(".table-responsive").$(byText("Date of Birth")).parent()
                .shouldHave(text(day + " " + month + "," + year));
        $(".table-responsive").$(byText("Subjects")).parent()
                .shouldHave(text(subject));
        $(".table-responsive").$(byText("Hobbies")).parent()
                .shouldHave(text(hobby));
        $(".table-responsive").$(byText("Address")).parent()
                .shouldHave(text(address));
        $(".table-responsive").$(byText("State and City")).parent()
                .shouldHave(text(state + " " + city));
    }
}

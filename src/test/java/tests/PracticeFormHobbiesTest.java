package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

import java.time.Duration;


@Tag("SMOKE")
public class PracticeFormHobbiesTest {

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

    @DisplayName("Проверка разных значений в поле hobby")
    @ParameterizedTest
    @ValueSource(strings = {"Sports", "Reading", "Music"})
    void testDifferentHobbiesSelection(String hobby) {
        $("#firstName").setValue("Oleg");
        $("#lastName").setValue("Razumov");
        $("#gender-radio-1").parent().click();
        $("#userNumber").setValue("9997776655");

        // Выбираем хобби из параметра
        $(byText(hobby)).click();

        $("#submit").click();

        $(".modal-content").shouldBe(visible);
        $(".table-responsive").$(byText("Hobbies")).parent().shouldHave(text(hobby));
    }
}

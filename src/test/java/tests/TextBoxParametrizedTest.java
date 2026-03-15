package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

@Tag("REGRESS")
public class TextBoxParametrizedTest {

    SelenideElement fullNameInput = $("#userName");
    SelenideElement emailInput = $("#userEmail");
    SelenideElement currentAddressInput = $("#currentAddress");
    SelenideElement permanentAddressInput = $("#permanentAddress");
    SelenideElement submitButton = $("#submit");

    @BeforeAll
    public static void setUp() {
        Configuration.browser = "chrome";
    }

    // Источник данных для параметризированного теста
    static Stream<Arguments> provideUserDataForTextBox() {
        return Stream.of(
                Arguments.of(
                        "Олег Разумов",
                        "oleg.razumov@mail.ru",
                        "ул. Юбилейная, д. 24, кв. 45, Кострома",
                        "ул. Строителей, д. 20, кв. 30, Москва"
                ),
                Arguments.of(
                        "Иван Петров",
                        "ivan.petrov@mail.ru",
                        "ул. Ленина, д. 10, кв. 5, Москва",
                        "ул. Гагарина, д. 22, кв. 100, Москва"
                ),
                Arguments.of(
                        "Анна Смирнова",
                        "anna.smirnova@mail.ru",
                        "пр. Мира, 15, Санкт-Петербург",
                        "наб. реки Фонтанки, 30, Санкт-Петербург"
                )
        );
    }

    @DisplayName("Тест обычной текстовой формы")
    @ParameterizedTest(name = "Тест с данными: {0}, {1}")
    @MethodSource("provideUserDataForTextBox")
    public void fillTextBoxAndCheckResult(String name, String email, String currentAddress, String permanentAddress) {

        open("https://demoqa.com/text-box");

        fullNameInput.setValue(name);
        emailInput.setValue(email);
        currentAddressInput.setValue(currentAddress);
        permanentAddressInput.setValue(permanentAddress);

        submitButton.click();

        SelenideElement outputDiv = $("#output");

        outputDiv.shouldHave(
                text("Name:" + name),
                text("Email:" + email),
                text("Current Address :" + currentAddress),
                text("Permananet Address :" + permanentAddress) // Обратите внимание на опечатку "Permananet" на странице
        );
    }
}

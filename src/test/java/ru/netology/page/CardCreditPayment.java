package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class CardCreditPayment {
    private SelenideElement inputCardNumberField = $("input[type=\"text\"][placeholder=\"0000 0000 0000 0000\"]");
    private SelenideElement inputMonthField= $("input[type=\"text\"][placeholder=\"08\"]");
    private SelenideElement inputYearField= $("input[type=\"text\"][placeholder=\"22\"]");
    private SelenideElement inputOwnerField = $$(".input").find(exactText("Владелец")).$(".input__control");
    private SelenideElement inputCVCField = $("input[type=\"text\"][placeholder=\"999\"]");
    private SelenideElement buyContinueButton = $$(".button").find(exactText("Продолжить"));

    private SelenideElement checkApprovedMessage = $$(".notification__title").find(exactText("Успешно"));
    private SelenideElement checkDeclinedMessage = $$(".notification__title").find(exactText("Ошибка"));

    private SelenideElement checkErrorMessageCard = $$(".input__top").find(exactText("Номер карты")).parent().
            $$(".input__sub").find(exactText("Неверный формат"));
    private SelenideElement checkErrorMessageMonth = $$(".input__top").find(exactText("Месяц")).parent().
            $$(".input__sub").find(exactText("Неверный формат"));
    private SelenideElement checkErrorMessageExpiredMonth = $$(".input__top").find(exactText("Месяц")).parent().
            $$(".input__sub").find(exactText("Неверно указан срок действия карты"));
    private SelenideElement checkErrorMessageYear = $$(".input__top").find(exactText("Год")).parent().
            $$(".input__sub").find(exactText("Неверный формат"));
    private SelenideElement checkErrorMessageYearExpired = $$(".input__top").find(exactText("Год")).parent().
            $$(".input__sub").find(exactText("Истёк срок действия карты"));
    private SelenideElement checkErrorMessageOwner = $$(".input__top").find(exactText("Владелец")).parent().
            $$(".input__sub").find(exactText("Поле обязательно для заполнения"));
    private SelenideElement checkErrorMessageOwnerSimbol = $$(".input__top").find(exactText("Владелец")).parent().
            $$(".input__sub").find(exactText("Неверный формат"));
    private SelenideElement checkErrorMessageCVC = $$(".input__top").find(exactText("CVC/CVV")).parent().
            $$(".input__sub").find(exactText("Неверный формат"));

    public CardCreditPayment pageFieldInfo(DataHelper.CardInfo info) {
        inputCardNumberField.setValue(info.getNumber());
        inputMonthField.setValue(info.getMonth());
        inputYearField.setValue(info.getYear());
        inputOwnerField.setValue(info.getOwner());
        inputCVCField.setValue(info.getCvc());
        buyContinueButton.click();
        return new CardCreditPayment();
    }


    public void checkApprovedMessage() {
        checkApprovedMessage.waitUntil(visible, 15000);
    }

    public void checkDeclinedMessage() {
        checkDeclinedMessage.waitUntil(visible, 15000);
    }

    public void checkErrorMessageCard() {
        checkErrorMessageCard.shouldBe(visible);
    }

    public void checkErrorMessageMonth() {
        checkErrorMessageMonth.shouldBe(visible);
    }

    public void checkErrorMessageYear() {
        checkErrorMessageYear.shouldBe(visible);
    }

    public void checkErrorMessageYearExpired() {
        checkErrorMessageYearExpired.shouldBe(visible);
    }

    public void checkErrorMessageExpiredMonth() {
        checkErrorMessageExpiredMonth.shouldBe(visible);
    }

    public void checkErrorMessageOwner() {
        checkErrorMessageOwner.shouldBe(visible);
    }

    public void checkErrorMessageOwnerSimbol() {
        checkErrorMessageOwnerSimbol.shouldBe(visible);
    }

    public void checkErrorMessageCVC() {
        checkErrorMessageCVC.shouldBe(visible);
    }

}





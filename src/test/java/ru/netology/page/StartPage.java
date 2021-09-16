package ru.netology.page;

import com.codeborne.selenide.SelenideElement;


import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class StartPage {
    private final SelenideElement buyTourButton = $$(".button").find(exactText("Купить"));
    private final SelenideElement buyTourInCreditButton = $$(".button").find(exactText("Купить в кредит"));

    public CardPayment debitPurchase() {
        buyTourButton.click();
        return new CardPayment();
    }

    public CardCreditPayment creditPurchase() {
        buyTourInCreditButton.click();
        return new CardCreditPayment();
    }
}

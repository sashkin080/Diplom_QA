package ru.netology.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class StartPage {
    private SelenideElement buyTourButton = $$(".button").find(exactText("Купить"));
    private SelenideElement buyTourInCreditButton = $$(".button").find(exactText("Купить в кредит"));

    public void debitPurchase() {
        buyTourButton.click();
    }

    public void creditPurchase() {
        buyTourInCreditButton.click();
    }
}

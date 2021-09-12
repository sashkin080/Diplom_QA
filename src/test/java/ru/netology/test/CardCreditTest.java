package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.val;
import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.data.DBHelper;
import ru.netology.page.CardCreditPayment;

import java.sql.SQLException;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CardCreditTest {
    private CardCreditPayment cardCreditPayment = new CardCreditPayment();

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void shouldOpen() {
        open("http://localhost:8080");
    }

    @AfterEach
    void clearAll()  {
        DBHelper.clearDBTables();
    }

    @Test
    void shouldBeApproved() throws SQLException {
        val cardValidNumber = DataHelper.getCardInfo(DataHelper.cardAPPROVED(), DataHelper.getValidMoth(),
                DataHelper.getValidYear(1), DataHelper.getValidOwner(), DataHelper.getValidCvc());
        cardCreditPayment.creditPurchase();
        cardCreditPayment.pageFieldInfo(cardValidNumber);
        cardCreditPayment.checkApprovedMessage();
        assertEquals(DataHelper.statusAPPROVED(), DBHelper.getStatusFromPaymentEntity());
        assertEquals(DBHelper.getBankIdFromCreditRequestEntity(), DBHelper.getCreditIdFromOrderEntity());
    }

    @Test
    void shouldBeDeclinedToBlockedCard()throws SQLException {
        val cardInValidNumber = DataHelper.getCardInfo(DataHelper.cardDECLINED(), DataHelper.getValidMoth(),
                DataHelper.getValidYear(1), DataHelper.getValidOwner(), DataHelper.getValidCvc());
        cardCreditPayment.creditPurchase();
        cardCreditPayment.pageFieldInfo(cardInValidNumber);
        cardCreditPayment.checkDeclinedMessage();
        assertEquals(DataHelper.statusDECLINED(), DBHelper.getStatusFromPaymentEntity());
        assertEquals(DBHelper.getBankIdFromCreditRequestEntity(), DBHelper.getCreditIdFromOrderEntity());
    }

    @Test
    void shouldBeDeclinedInvalidCard() {
        val cardInValidNumber = DataHelper.getCardInfo(DataHelper.getInvalidNumberCard(), DataHelper.getValidMoth(),
                DataHelper.getValidYear(1), DataHelper.getValidOwner(), DataHelper.getValidCvc());
        cardCreditPayment.creditPurchase();
        cardCreditPayment.pageFieldInfo(cardInValidNumber);
        cardCreditPayment.checkErrorMessageCard();
    }

    @Test
    void shouldBeDeclinedEmptyNumberCard() {
        val cardInValidNumber = DataHelper.getCardInfo(DataHelper.getEmptyNumberCard(), DataHelper.getValidMoth(),
                DataHelper.getValidYear(1), DataHelper.getValidOwner(), DataHelper.getValidCvc());
        cardCreditPayment.creditPurchase();
        cardCreditPayment.pageFieldInfo(cardInValidNumber);
        cardCreditPayment.checkErrorMessageCard();
    }

    @Test
    void shouldBeDeclinedInValidMonth() {
        val cardInValidNumber = DataHelper.getCardInfo(DataHelper.cardAPPROVED(), DataHelper.getInValidMoth(),
                DataHelper.getValidYear(1), DataHelper.getValidOwner(), DataHelper.getValidCvc());
        cardCreditPayment.creditPurchase();
        cardCreditPayment.pageFieldInfo(cardInValidNumber);
        cardCreditPayment.checkErrorMessageExpiredMonth();
    }

    @Test
    void shouldBeDeclinedExpiredShelfLifeCard() {
        val cardInValidNumber = DataHelper.getCardInfo(DataHelper.cardAPPROVED(), DataHelper.getExpiredShelfLifeCard(),
                DataHelper.getValidYear(1), DataHelper.getValidOwner(), DataHelper.getValidCvc());
        cardCreditPayment.creditPurchase();
        cardCreditPayment.pageFieldInfo(cardInValidNumber);
        cardCreditPayment.checkErrorMessageExpiredMonth();
    }

    @Test
    void shouldBeDeclinedEmptyMonth() {
        val cardInValidNumber = DataHelper.getCardInfo(DataHelper.cardAPPROVED(), DataHelper.getEmptyMonth(),
                DataHelper.getValidYear(1), DataHelper.getValidOwner(), DataHelper.getValidCvc());
        cardCreditPayment.creditPurchase();
        cardCreditPayment.pageFieldInfo(cardInValidNumber);
        cardCreditPayment.checkErrorMessageMonth();
    }

    @Test
    void shouldBeDeclinedEmptyYear() {
        val cardInValidNumber = DataHelper.getCardInfo(DataHelper.cardAPPROVED(), DataHelper.getValidMoth(),
                DataHelper.getEmptyYear(), DataHelper.getValidOwner(), DataHelper.getValidCvc());
        cardCreditPayment.creditPurchase();
        cardCreditPayment.pageFieldInfo(cardInValidNumber);
        cardCreditPayment.checkErrorMessageYear();
    }

    @Test
    void shouldBeDeclinedExpiredYear() {
        val cardInValidNumber = DataHelper.getCardInfo(DataHelper.cardAPPROVED(), DataHelper.getValidMoth(),
                DataHelper.getLastYear(), DataHelper.getValidOwner(), DataHelper.getValidCvc());
        cardCreditPayment.creditPurchase();
        cardCreditPayment.pageFieldInfo(cardInValidNumber);
        cardCreditPayment.checkErrorMessageYearExpired();
    }

    @Test
    void shouldBeDeclinedInValidOwner() {
        val cardInValidNumber = DataHelper.getCardInfo(DataHelper.cardAPPROVED(), DataHelper.getValidMoth(),
                DataHelper.getValidYear(1), DataHelper.getInValidOwner(), DataHelper.getValidCvc());
        cardCreditPayment.creditPurchase();
        cardCreditPayment.pageFieldInfo(cardInValidNumber);
        cardCreditPayment.checkErrorMessageOwnerSimbol();
    }

    @Test
    void shouldBeDeclinedInValidOwnerRus() {
        val cardInValidNumber = DataHelper.getCardInfo(DataHelper.cardAPPROVED(), DataHelper.getValidMoth(),
                DataHelper.getValidYear(1), DataHelper.getInValidOwnerRus(), DataHelper.getValidCvc());
        cardCreditPayment.creditPurchase();
        cardCreditPayment.pageFieldInfo(cardInValidNumber);
        cardCreditPayment.checkErrorMessageOwnerSimbol();
    }

    @Test
    void shouldBeDeclinedEmptyOwner() {
        val cardInValidNumber = DataHelper.getCardInfo(DataHelper.cardAPPROVED(), DataHelper.getValidMoth(),
                DataHelper.getValidYear(1), DataHelper.getEmptyOwner(), DataHelper.getValidCvc());
        cardCreditPayment.creditPurchase();
        cardCreditPayment.pageFieldInfo(cardInValidNumber);
        cardCreditPayment.checkErrorMessageOwner();
    }

    @Test
    void shouldBeDeclinedInValidCvc() {
        val cardInValidNumber = DataHelper.getCardInfo(DataHelper.cardAPPROVED(), DataHelper.getValidMoth(),
                DataHelper.getValidYear(1), DataHelper.getValidOwner(), DataHelper.getInValidCvc());
        cardCreditPayment.creditPurchase();
        cardCreditPayment.pageFieldInfo(cardInValidNumber);
        cardCreditPayment.checkErrorMessageCVC();
    }

    @Test
    void shouldBeDeclinedEmptyCvc() {
        val cardInValidNumber = DataHelper.getCardInfo(DataHelper.cardAPPROVED(), DataHelper.getValidMoth(),
                DataHelper.getValidYear(1), DataHelper.getValidOwner(), DataHelper.getEmptyCvc());
        cardCreditPayment.creditPurchase();
        cardCreditPayment.pageFieldInfo(cardInValidNumber);
        cardCreditPayment.checkErrorMessageCVC();
    }
}
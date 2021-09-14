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
    void shouldBeApproved(){
        val cardValidNumber = DataHelper.getCardInfo(DataHelper.cardAPPROVED(), DataHelper.getValidMoth(),
                DataHelper.getValidYear(1), DataHelper.getValidOwner(), DataHelper.getValidCvc());
        cardCreditPayment.creditPurchase();
        cardCreditPayment.pageFieldInfo(cardValidNumber);
        cardCreditPayment.checkApprovedMessage();
        assertEquals(DataHelper.statusAPPROVED(), DBHelper.getStatusFromPaymentEntity());
        assertEquals(DBHelper.getBankIdFromCreditRequestEntity(), DBHelper.getCreditIdFromOrderEntity());
        assertEquals("1", DBHelper.getNumberOfOrders());
    }

    @Test
    void shouldBeDeclinedToBlockedCard() {
        val cardInValidNumber = DataHelper.getCardInfo(DataHelper.cardDECLINED(), DataHelper.getValidMoth(),
                DataHelper.getValidYear(1), DataHelper.getValidOwner(), DataHelper.getValidCvc());
        cardCreditPayment.creditPurchase();
        cardCreditPayment.pageFieldInfo(cardInValidNumber);
        cardCreditPayment.checkDeclinedMessage();
        assertEquals(DataHelper.statusDECLINED(), DBHelper.getStatusFromPaymentEntity());
        assertEquals(DBHelper.getBankIdFromCreditRequestEntity(), DBHelper.getCreditIdFromOrderEntity());
        assertEquals("0", DBHelper.getNumberOfOrders());
    }

    @Test
    void shouldBeDeclinedInvalidCard() {
        val cardInValidNumber = DataHelper.getCardInfo(DataHelper.getInvalidNumberCard(), DataHelper.getValidMoth(),
                DataHelper.getValidYear(1), DataHelper.getValidOwner(), DataHelper.getValidCvc());
        cardCreditPayment.creditPurchase();
        cardCreditPayment.pageFieldInfo(cardInValidNumber);
        cardCreditPayment.checkErrorMessageCard();
        assertEquals("0", DBHelper.getNumberOfOrders());
    }

    @Test
    void shouldBeDeclinedEmptyNumberCard() {
        val cardInValidNumber = DataHelper.getCardInfo(DataHelper.getEmptyNumberCard(), DataHelper.getValidMoth(),
                DataHelper.getValidYear(1), DataHelper.getValidOwner(), DataHelper.getValidCvc());
        cardCreditPayment.creditPurchase();
        cardCreditPayment.pageFieldInfo(cardInValidNumber);
        cardCreditPayment.checkErrorMessageCard();
        assertEquals("0", DBHelper.getNumberOfOrders());
    }

    @Test
    void shouldBeDeclinedInValidMonth() {
        val cardInValidNumber = DataHelper.getCardInfo(DataHelper.cardAPPROVED(), DataHelper.getInValidMoth(),
                DataHelper.getValidYear(1), DataHelper.getValidOwner(), DataHelper.getValidCvc());
        cardCreditPayment.creditPurchase();
        cardCreditPayment.pageFieldInfo(cardInValidNumber);
        cardCreditPayment.checkErrorMessageExpiredMonth();
        assertEquals("0", DBHelper.getNumberOfOrders());
    }

    @Test
    void shouldBeDeclinedExpiredShelfLifeCard() {
        val cardInValidNumber = DataHelper.getCardInfo(DataHelper.cardAPPROVED(), DataHelper.getExpiredShelfLifeCard(),
                DataHelper.getValidYear(1), DataHelper.getValidOwner(), DataHelper.getValidCvc());
        cardCreditPayment.creditPurchase();
        cardCreditPayment.pageFieldInfo(cardInValidNumber);
        cardCreditPayment.checkErrorMessageExpiredMonth();
        assertEquals("0", DBHelper.getNumberOfOrders());
    }

    @Test
    void shouldBeDeclinedEmptyMonth() {
        val cardInValidNumber = DataHelper.getCardInfo(DataHelper.cardAPPROVED(), DataHelper.getEmptyMonth(),
                DataHelper.getValidYear(1), DataHelper.getValidOwner(), DataHelper.getValidCvc());
        cardCreditPayment.creditPurchase();
        cardCreditPayment.pageFieldInfo(cardInValidNumber);
        cardCreditPayment.checkErrorMessageMonth();
        assertEquals("0", DBHelper.getNumberOfOrders());
    }

    @Test
    void shouldBeDeclinedEmptyYear() {
        val cardInValidNumber = DataHelper.getCardInfo(DataHelper.cardAPPROVED(), DataHelper.getValidMoth(),
                DataHelper.getEmptyYear(), DataHelper.getValidOwner(), DataHelper.getValidCvc());
        cardCreditPayment.creditPurchase();
        cardCreditPayment.pageFieldInfo(cardInValidNumber);
        cardCreditPayment.checkErrorMessageYear();
        assertEquals("0", DBHelper.getNumberOfOrders());
    }

    @Test
    void shouldBeDeclinedExpiredYear() {
        val cardInValidNumber = DataHelper.getCardInfo(DataHelper.cardAPPROVED(), DataHelper.getValidMoth(),
                DataHelper.getLastYear(), DataHelper.getValidOwner(), DataHelper.getValidCvc());
        cardCreditPayment.creditPurchase();
        cardCreditPayment.pageFieldInfo(cardInValidNumber);
        cardCreditPayment.checkErrorMessageYearExpired();
        assertEquals("0", DBHelper.getNumberOfOrders());
    }

    @Test
    void shouldBeDeclinedInValidOwner() {
        val cardInValidNumber = DataHelper.getCardInfo(DataHelper.cardAPPROVED(), DataHelper.getValidMoth(),
                DataHelper.getValidYear(1), DataHelper.getInValidOwner(), DataHelper.getValidCvc());
        cardCreditPayment.creditPurchase();
        cardCreditPayment.pageFieldInfo(cardInValidNumber);
        cardCreditPayment.checkErrorMessageOwnerSimbol();
        assertEquals("0", DBHelper.getNumberOfOrders());
    }

    @Test
    void shouldBeDeclinedInValidOwnerRus() {
        val cardInValidNumber = DataHelper.getCardInfo(DataHelper.cardAPPROVED(), DataHelper.getValidMoth(),
                DataHelper.getValidYear(1), DataHelper.getInValidOwnerRus(), DataHelper.getValidCvc());
        cardCreditPayment.creditPurchase();
        cardCreditPayment.pageFieldInfo(cardInValidNumber);
        cardCreditPayment.checkErrorMessageOwnerSimbol();
        assertEquals("0", DBHelper.getNumberOfOrders());
    }

    @Test
    void shouldBeDeclinedEmptyOwner() {
        val cardInValidNumber = DataHelper.getCardInfo(DataHelper.cardAPPROVED(), DataHelper.getValidMoth(),
                DataHelper.getValidYear(1), DataHelper.getEmptyOwner(), DataHelper.getValidCvc());
        cardCreditPayment.creditPurchase();
        cardCreditPayment.pageFieldInfo(cardInValidNumber);
        cardCreditPayment.checkErrorMessageOwner();
        assertEquals("0", DBHelper.getNumberOfOrders());
    }

    @Test
    void shouldBeDeclinedInValidCvc() {
        val cardInValidNumber = DataHelper.getCardInfo(DataHelper.cardAPPROVED(), DataHelper.getValidMoth(),
                DataHelper.getValidYear(1), DataHelper.getValidOwner(), DataHelper.getInValidCvc());
        cardCreditPayment.creditPurchase();
        cardCreditPayment.pageFieldInfo(cardInValidNumber);
        cardCreditPayment.checkErrorMessageCVC();
        assertEquals("0", DBHelper.getNumberOfOrders());
    }

    @Test
    void shouldBeDeclinedEmptyCvc() {
        val cardInValidNumber = DataHelper.getCardInfo(DataHelper.cardAPPROVED(), DataHelper.getValidMoth(),
                DataHelper.getValidYear(1), DataHelper.getValidOwner(), DataHelper.getEmptyCvc());
        cardCreditPayment.creditPurchase();
        cardCreditPayment.pageFieldInfo(cardInValidNumber);
        cardCreditPayment.checkErrorMessageCVC();
        assertEquals("0", DBHelper.getNumberOfOrders());
    }
}
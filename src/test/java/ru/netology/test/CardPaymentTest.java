package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.val;
import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.data.DBHelper;
import ru.netology.page.CardPayment;

import java.sql.SQLException;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CardPaymentTest {
    private CardPayment cardPayment = new CardPayment();

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
        DBHelper.clearDBTables();
    }

    @Test
    void shouldBeApproved() throws SQLException {
        val cardValidNumber = DataHelper.getCardInfo(DataHelper.cardAPPROVED(), DataHelper.getValidMoth(),
                DataHelper.getValidYear(1), DataHelper.getValidOwner(), DataHelper.getValidCvc());
        cardPayment.debitPurchase();
        cardPayment.pageFieldInfo(cardValidNumber);
        cardPayment.checkApprovedMessage();
        assertEquals(DataHelper.statusAPPROVED(), DBHelper.getStatusFromPaymentEntity());
        assertEquals(DBHelper.getTransactionIdFromPaymentEntity(), DBHelper.getPaymentIdFromOrderEntity());
        assertEquals("1", DBHelper.getNumberOfOrders());
    }

    @Test
    void shouldBeDeclinedToBlockedCard() throws SQLException {
        val cardInValidNumber = DataHelper.getCardInfo(DataHelper.cardDECLINED(), DataHelper.getValidMoth(),
                DataHelper.getValidYear(1), DataHelper.getValidOwner(), DataHelper.getValidCvc());
        cardPayment.debitPurchase();
        cardPayment.pageFieldInfo(cardInValidNumber);
        cardPayment.checkDeclinedMessage();
        assertEquals(DataHelper.statusDECLINED(), DBHelper.getStatusFromPaymentEntity());
        assertEquals(DBHelper.getTransactionIdFromPaymentEntity(), DBHelper.getPaymentIdFromOrderEntity());
        assertEquals("0", DBHelper.getNumberOfOrders());
//failed
    }

    @Test
    void shouldBeDeclinedInvalidCard() {
        val cardInValidNumber = DataHelper.getCardInfo(DataHelper.getInvalidNumberCard(), DataHelper.getValidMoth(),
                DataHelper.getValidYear(1), DataHelper.getValidOwner(), DataHelper.getValidCvc());
        cardPayment.debitPurchase();
        cardPayment.pageFieldInfo(cardInValidNumber);
        cardPayment.checkErrorMessageCard();
        assertEquals("0", DBHelper.getNumberOfOrders());
    }

    @Test
    void shouldBeDeclinedEmptyNumberCard() {
        val cardInValidNumber = DataHelper.getCardInfo(DataHelper.getEmptyNumberCard(), DataHelper.getValidMoth(),
                DataHelper.getValidYear(1), DataHelper.getValidOwner(), DataHelper.getValidCvc());
        cardPayment.debitPurchase();
        cardPayment.pageFieldInfo(cardInValidNumber);
        cardPayment.checkErrorMessageCard();
        assertEquals("0", DBHelper.getNumberOfOrders());
    }

    @Test
    void shouldBeDeclinedInValidMonth() {
        val cardInValidNumber = DataHelper.getCardInfo(DataHelper.cardAPPROVED(), DataHelper.getInValidMoth(),
                DataHelper.getValidYear(1), DataHelper.getValidOwner(), DataHelper.getValidCvc());
        cardPayment.debitPurchase();
        cardPayment.pageFieldInfo(cardInValidNumber);
        cardPayment.checkErrorMessageExpiredMonth();
        assertEquals("0", DBHelper.getNumberOfOrders());
    }

    @Test
    void shouldBeDeclinedExpiredShelfLifeCard() {
        val cardInValidNumber = DataHelper.getCardInfo(DataHelper.cardAPPROVED(), DataHelper.getExpiredShelfLifeCard(),
                DataHelper.getValidYear(1), DataHelper.getValidOwner(), DataHelper.getValidCvc());
        cardPayment.debitPurchase();
        cardPayment.pageFieldInfo(cardInValidNumber);
        cardPayment.checkErrorMessageExpiredMonth();
        assertEquals("0", DBHelper.getNumberOfOrders());
    }

    @Test
    void shouldBeDeclinedEmptyMonth() {
        val cardInValidNumber = DataHelper.getCardInfo(DataHelper.cardAPPROVED(), DataHelper.getEmptyMonth(),
                DataHelper.getValidYear(1), DataHelper.getValidOwner(), DataHelper.getValidCvc());
        cardPayment.debitPurchase();
        cardPayment.pageFieldInfo(cardInValidNumber);
        cardPayment.checkErrorMessageMonth();
        assertEquals("0", DBHelper.getNumberOfOrders());
    }

    @Test
    void shouldBeDeclinedEmptyYear() {
        val cardInValidNumber = DataHelper.getCardInfo(DataHelper.cardAPPROVED(), DataHelper.getValidMoth(),
                DataHelper.getEmptyYear(), DataHelper.getValidOwner(), DataHelper.getValidCvc());
        cardPayment.debitPurchase();
        cardPayment.pageFieldInfo(cardInValidNumber);
        cardPayment.checkErrorMessageYear();
        assertEquals("0", DBHelper.getNumberOfOrders());
    }

    @Test
    void shouldBeDeclinedExpiredYear() {
        val cardInValidNumber = DataHelper.getCardInfo(DataHelper.cardAPPROVED(), DataHelper.getValidMoth(),
                DataHelper.getLastYear(), DataHelper.getValidOwner(), DataHelper.getValidCvc());
        cardPayment.debitPurchase();
        cardPayment.pageFieldInfo(cardInValidNumber);
        cardPayment.checkErrorMessageYearExpired();
        assertEquals("0", DBHelper.getNumberOfOrders());
    }

    @Test
    void shouldBeDeclinedInValidOwner() {
        val cardInValidNumber = DataHelper.getCardInfo(DataHelper.cardAPPROVED(), DataHelper.getValidMoth(),
                DataHelper.getValidYear(1), DataHelper.getInValidOwner(), DataHelper.getValidCvc());
        cardPayment.debitPurchase();
        cardPayment.pageFieldInfo(cardInValidNumber);
        cardPayment.checkErrorMessageOwnerSimbol();
        assertEquals("0", DBHelper.getNumberOfOrders());
        //failing
    }

    @Test
    void shouldBeDeclinedInValidOwnerRus() {
        val cardInValidNumber = DataHelper.getCardInfo(DataHelper.cardAPPROVED(), DataHelper.getValidMoth(),
                DataHelper.getValidYear(1), DataHelper.getInValidOwnerRus(), DataHelper.getValidCvc());
        cardPayment.debitPurchase();
        cardPayment.pageFieldInfo(cardInValidNumber);
        cardPayment.checkErrorMessageOwnerSimbol();
        assertEquals("0", DBHelper.getNumberOfOrders());
        //failing
    }

    @Test
    void shouldBeDeclinedEmptyOwner() {
        val cardInValidNumber = DataHelper.getCardInfo(DataHelper.cardAPPROVED(), DataHelper.getValidMoth(),
                DataHelper.getValidYear(1), DataHelper.getEmptyOwner(), DataHelper.getValidCvc());
        cardPayment.debitPurchase();
        cardPayment.pageFieldInfo(cardInValidNumber);
        cardPayment.checkErrorMessageOwner();
        assertEquals("0", DBHelper.getNumberOfOrders());
    }

    @Test
    void shouldBeDeclinedInValidCvc() {
        val cardInValidNumber = DataHelper.getCardInfo(DataHelper.cardAPPROVED(), DataHelper.getValidMoth(),
                DataHelper.getValidYear(1), DataHelper.getValidOwner(), DataHelper.getInValidCvc());
        cardPayment.debitPurchase();
        cardPayment.pageFieldInfo(cardInValidNumber);
        cardPayment.checkErrorMessageCVC();
        assertEquals("0", DBHelper.getNumberOfOrders());
    }

    @Test
    void shouldBeDeclinedEmptyCvc() {
        val cardInValidNumber = DataHelper.getCardInfo(DataHelper.cardAPPROVED(), DataHelper.getValidMoth(),
                DataHelper.getValidYear(1), DataHelper.getValidOwner(), DataHelper.getEmptyCvc());
        cardPayment.debitPurchase();
        cardPayment.pageFieldInfo(cardInValidNumber);
        cardPayment.checkErrorMessageCVC();
        assertEquals("0", DBHelper.getNumberOfOrders());
        //failing
    }
}
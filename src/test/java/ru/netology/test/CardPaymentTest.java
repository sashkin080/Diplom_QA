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

    private final static String statusAPPROVED = "APPROVED";
    private final static String statusDECLINED = "DECLINED";
    private final static String cardAPPROVED = "4444444444444441";
    private final static String cardDECLINED = "4444444444444442";


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
    void clearAll() throws SQLException {
    }

    @Test
    void shouldBeApproved() throws SQLException {
        val cardNumber = DataHelper.getCardInfo(cardAPPROVED, DataHelper.getValidMoth(),
                DataHelper.getValidYear(), DataHelper.getValidOwner(), DataHelper.getValidCvc());
        cardPayment.debitPurchase();
        cardPayment.pageFieldInfo(cardNumber);
        cardPayment.checkApprovedMessage();
        assertEquals(statusAPPROVED, DBHelper.getStatusFromPaymentEntity());
        assertEquals(DBHelper.getTransactionIdFromPaymentEntity(), DBHelper.getPaymentIdFromOrderEntity());
    }

    @Test
    void ShouldBeDeclinedToBlockedCard() throws SQLException {
        val cardNumber = DataHelper.getCardInfo(cardDECLINED, DataHelper.getValidMoth(),
                DataHelper.getValidYear(), DataHelper.getValidOwner(), DataHelper.getValidCvc());
        cardPayment.debitPurchase();
        cardPayment.pageFieldInfo(cardNumber);
        cardPayment.checkDeclinedMessage();
        assertEquals(statusDECLINED, DBHelper.getStatusFromPaymentEntity());
        assertEquals(DBHelper.getTransactionIdFromPaymentEntity(), DBHelper.getPaymentIdFromOrderEntity());
//filed
    }

    @Test
    void ShouldBeDeclinedInvalidCard() {
        val cardNumber = DataHelper.getCardInfo(DataHelper.getInvalidNumberCard(), DataHelper.getValidMoth(),
                DataHelper.getValidYear(), DataHelper.getValidOwner(), DataHelper.getValidCvc());
        cardPayment.debitPurchase();
        cardPayment.pageFieldInfo(cardNumber);
        cardPayment.checkErrorMessageCard();
    }

    @Test
    void ShouldBeDeclinedEmptyNumberCard() {
        val cardNumber = DataHelper.getCardInfo(DataHelper.getEmptyNumberCard(), DataHelper.getValidMoth(),
                DataHelper.getValidYear(), DataHelper.getValidOwner(), DataHelper.getValidCvc());
        cardPayment.debitPurchase();
        cardPayment.pageFieldInfo(cardNumber);
        cardPayment.checkErrorMessageCard();
    }

    @Test
    void ShouldBeDeclinedInValidMonth() {
        val cardNumber = DataHelper.getCardInfo(cardAPPROVED, DataHelper.getInValidMoth(),
                DataHelper.getValidYear(), DataHelper.getValidOwner(), DataHelper.getValidCvc());
        cardPayment.debitPurchase();
        cardPayment.pageFieldInfo(cardNumber);
        cardPayment.checkErrorMessageExpiredMonth();
    }

    @Test
    void ShouldBeDeclinedExpiredShelfLifeCard() {
        val cardNumber = DataHelper.getCardInfo(cardAPPROVED, DataHelper.getExpiredShelfLifeCard(),
                DataHelper.getValidYear(), DataHelper.getValidOwner(), DataHelper.getValidCvc());
        cardPayment.debitPurchase();
        cardPayment.pageFieldInfo(cardNumber);
        cardPayment.checkErrorMessageExpiredMonth();
    }

    @Test
    void ShouldBeDeclinedEmptyMonth() {
        val cardNumber = DataHelper.getCardInfo(cardAPPROVED, DataHelper.getEmptyMonth(),
                DataHelper.getValidYear(), DataHelper.getValidOwner(), DataHelper.getValidCvc());
        cardPayment.debitPurchase();
        cardPayment.pageFieldInfo(cardNumber);
        cardPayment.checkErrorMessageMonth();
    }

    @Test
    void ShouldBeDeclinedEmptyYear() {
        val cardNumber = DataHelper.getCardInfo(cardAPPROVED, DataHelper.getValidMoth(),
                DataHelper.getEmptyYear(), DataHelper.getValidOwner(), DataHelper.getValidCvc());
        cardPayment.debitPurchase();
        cardPayment.pageFieldInfo(cardNumber);
        cardPayment.checkErrorMessageYear();
    }

    @Test
    void ShouldBeDeclinedExpiredYear() {
        val cardNumber = DataHelper.getCardInfo(cardAPPROVED, DataHelper.getValidMoth(),
                DataHelper.getLastYear(), DataHelper.getValidOwner(), DataHelper.getValidCvc());
        cardPayment.debitPurchase();
        cardPayment.pageFieldInfo(cardNumber);
        cardPayment.checkErrorMessageYearExpired();
    }

    @Test
    void ShouldBeDeclinedInValidOwner() {
        val cardNumber = DataHelper.getCardInfo(cardAPPROVED, DataHelper.getValidMoth(),
                DataHelper.getValidYear(), DataHelper.getInValidOwner(), DataHelper.getValidCvc());
        cardPayment.debitPurchase();
        cardPayment.pageFieldInfo(cardNumber);
        cardPayment.checkErrorMessageOwnerSimbol();
        //filed
    }

    @Test
    void ShouldBeDeclinedInValidOwnerRus() {
        val cardNumber = DataHelper.getCardInfo(cardAPPROVED, DataHelper.getValidMoth(),
                DataHelper.getValidYear(), DataHelper.getInValidOwnerRus(), DataHelper.getValidCvc());
        cardPayment.debitPurchase();
        cardPayment.pageFieldInfo(cardNumber);
        cardPayment.checkErrorMessageOwnerSimbol();
        //filed
    }

    @Test
    void ShouldBeDeclinedEmptyOwner()  {
            val cardNumber = DataHelper.getCardInfo(cardAPPROVED, DataHelper.getValidMoth(),
                    DataHelper.getValidYear(), DataHelper.getEmptyOwner(), DataHelper.getValidCvc());
            cardPayment.debitPurchase();
            cardPayment.pageFieldInfo(cardNumber);
            cardPayment.checkErrorMessageOwner();
        }

    @Test
    void ShouldBeDeclinedInValidCvc()  {
        val cardNumber = DataHelper.getCardInfo(cardAPPROVED, DataHelper.getValidMoth(),
                DataHelper.getValidYear(), DataHelper.getValidOwner(), DataHelper.getInValidCvc());
        cardPayment.debitPurchase();
        cardPayment.pageFieldInfo(cardNumber);
        cardPayment.checkErrorMessageCVC();
    }

    @Test
    void ShouldBeDeclinedEmptyCvc()  {
        val cardNumber = DataHelper.getCardInfo(cardAPPROVED, DataHelper.getValidMoth(),
                DataHelper.getValidYear(), DataHelper.getValidOwner(), DataHelper.getEmptyCvc());
        cardPayment.debitPurchase();
        cardPayment.pageFieldInfo(cardNumber);
        cardPayment.checkErrorMessageCVC();
        //filed
    }

}
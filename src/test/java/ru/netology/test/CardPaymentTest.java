package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.val;
import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.data.DBHelper;
import ru.netology.page.CardPayment;
import ru.netology.page.StartPage;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CardPaymentTest {

    private CardPayment cardPayment = new CardPayment();
    private StartPage startPage;

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
        startPage = open("http://localhost:8080",StartPage.class);
        DBHelper.clearDBTables();
    }


    @Test
    void shouldBeApproved() {
        val validCard = DataHelper.getCardInfo(DataHelper.cardAPPROVED(), DataHelper.getValidMoth(),
                DataHelper.getValidYear(1), DataHelper.getValidOwner(), DataHelper.getValidCvc());
        CardPayment cardPayment = startPage.debitPurchase();
        cardPayment.pageFieldInfo(validCard);
        cardPayment.checkApprovedMessage();
        assertEquals(DataHelper.statusAPPROVED(), DBHelper.getStatusFromPaymentEntity());
        assertEquals(DBHelper.getTransactionIdFromPaymentEntity(), DBHelper.getPaymentIdFromOrderEntity());
        assertEquals("1", DBHelper.getNumberOfOrders());
    }

    @Test
    void shouldBeDeclinedToBlockedCard() {
        val invalidCard = DataHelper.getCardInfo(DataHelper.cardDECLINED(), DataHelper.getValidMoth(),
                DataHelper.getValidYear(1), DataHelper.getValidOwner(), DataHelper.getValidCvc());
        CardPayment cardPayment = startPage.debitPurchase();
        cardPayment.pageFieldInfo(invalidCard);
        cardPayment.checkDeclinedMessage();
        assertEquals(DataHelper.statusDECLINED(), DBHelper.getStatusFromPaymentEntity());
        assertEquals(DBHelper.getTransactionIdFromPaymentEntity(), DBHelper.getPaymentIdFromOrderEntity());
        assertEquals("0", DBHelper.getNumberOfOrders());
//failed
    }

    @Test
    void shouldBeDeclinedInvalidCard() {
        val invalidCard = DataHelper.getCardInfo(DataHelper.getInvalidNumberCard(), DataHelper.getValidMoth(),
                DataHelper.getValidYear(1), DataHelper.getValidOwner(), DataHelper.getValidCvc());
        CardPayment cardPayment = startPage.debitPurchase();
        cardPayment.pageFieldInfo(invalidCard);
        cardPayment.checkErrorMessageCard();
        assertEquals("0", DBHelper.getNumberOfOrders());
    }

    @Test
    void shouldBeDeclinedEmptyNumberCard() {
        val invalidCard = DataHelper.getCardInfo(DataHelper.getEmptyNumberCard(), DataHelper.getValidMoth(),
                DataHelper.getValidYear(1), DataHelper.getValidOwner(), DataHelper.getValidCvc());
        CardPayment cardPayment = startPage.debitPurchase();
        cardPayment.pageFieldInfo(invalidCard);
        cardPayment.checkErrorMessageCard();
        assertEquals("0", DBHelper.getNumberOfOrders());
    }

    @Test
    void shouldBeDeclinedInValidMonth() {
        val invalidCard = DataHelper.getCardInfo(DataHelper.cardAPPROVED(), DataHelper.getInValidMoth(),
                DataHelper.getValidYear(1), DataHelper.getValidOwner(), DataHelper.getValidCvc());
        CardPayment cardPayment = startPage.debitPurchase();
        cardPayment.pageFieldInfo(invalidCard);
        cardPayment.checkErrorMessageExpiredMonth();
        assertEquals("0", DBHelper.getNumberOfOrders());
    }

    @Test
    void shouldBeDeclinedExpiredShelfLifeCard() {
        val invalidCard = DataHelper.getCardInfo(DataHelper.cardAPPROVED(), DataHelper.getExpiredShelfLifeCard(),
                DataHelper.getValidYear(0), DataHelper.getValidOwner(), DataHelper.getValidCvc());
        CardPayment cardPayment = startPage.debitPurchase();
        cardPayment.pageFieldInfo(invalidCard);
        cardPayment.checkErrorMessageExpiredMonth();
        assertEquals("0", DBHelper.getNumberOfOrders());
    }

    @Test
    void shouldBeDeclinedEmptyMonth() {
        val invalidCard = DataHelper.getCardInfo(DataHelper.cardAPPROVED(), DataHelper.getEmptyMonth(),
                DataHelper.getValidYear(1), DataHelper.getValidOwner(), DataHelper.getValidCvc());
        CardPayment cardPayment = startPage.debitPurchase();
        cardPayment.pageFieldInfo(invalidCard);
        cardPayment.checkErrorMessageMonth();
        assertEquals("0", DBHelper.getNumberOfOrders());
    }

    @Test
    void shouldBeDeclinedEmptyYear() {
        val invalidCard = DataHelper.getCardInfo(DataHelper.cardAPPROVED(), DataHelper.getValidMoth(),
                DataHelper.getEmptyYear(), DataHelper.getValidOwner(), DataHelper.getValidCvc());
        CardPayment cardPayment = startPage.debitPurchase();
        cardPayment.pageFieldInfo(invalidCard);
        cardPayment.checkErrorMessageYear();
        assertEquals("0", DBHelper.getNumberOfOrders());
    }

    @Test
    void shouldBeDeclinedExpiredYear() {
        val invalidCard = DataHelper.getCardInfo(DataHelper.cardAPPROVED(), DataHelper.getValidMoth(),
                DataHelper.getLastYear(), DataHelper.getValidOwner(), DataHelper.getValidCvc());
        CardPayment cardPayment = startPage.debitPurchase();
        cardPayment.pageFieldInfo(invalidCard);
        cardPayment.checkErrorMessageYearExpired();
        assertEquals("0", DBHelper.getNumberOfOrders());
    }

    @Test
    void shouldBeDeclinedInValidOwner() {
        val invalidCard = DataHelper.getCardInfo(DataHelper.cardAPPROVED(), DataHelper.getValidMoth(),
                DataHelper.getValidYear(1), DataHelper.getInValidOwner(), DataHelper.getValidCvc());
        CardPayment cardPayment = startPage.debitPurchase();
        cardPayment.pageFieldInfo(invalidCard);
        cardPayment.checkErrorMessageOwnerSimbol();
        assertEquals("0", DBHelper.getNumberOfOrders());
        //failing
    }

    @Test
    void shouldBeDeclinedInValidOwnerRus() {
        val invalidCard = DataHelper.getCardInfo(DataHelper.cardAPPROVED(), DataHelper.getValidMoth(),
                DataHelper.getValidYear(1), DataHelper.getInValidOwnerRus(), DataHelper.getValidCvc());
        CardPayment cardPayment = startPage.debitPurchase();
        cardPayment.pageFieldInfo(invalidCard);
        cardPayment.checkErrorMessageOwnerSimbol();
        assertEquals("0", DBHelper.getNumberOfOrders());
        //failing
    }

    @Test
    void shouldBeDeclinedEmptyOwner() {
        val invalidCard = DataHelper.getCardInfo(DataHelper.cardAPPROVED(), DataHelper.getValidMoth(),
                DataHelper.getValidYear(1), DataHelper.getEmptyOwner(), DataHelper.getValidCvc());
        CardPayment cardPayment = startPage.debitPurchase();
        cardPayment.pageFieldInfo(invalidCard);
        cardPayment.checkErrorMessageOwner();
        assertEquals("0", DBHelper.getNumberOfOrders());
    }

    @Test
    void shouldBeDeclinedInValidCvc() {
        val invalidCard = DataHelper.getCardInfo(DataHelper.cardAPPROVED(), DataHelper.getValidMoth(),
                DataHelper.getValidYear(1), DataHelper.getValidOwner(), DataHelper.getInValidCvc());
        CardPayment cardPayment = startPage.debitPurchase();
        cardPayment.pageFieldInfo(invalidCard);
        cardPayment.checkErrorMessageCVC();
        assertEquals("0", DBHelper.getNumberOfOrders());
    }

    @Test
    void shouldBeDeclinedEmptyCvc() {
        val invalidCard = DataHelper.getCardInfo(DataHelper.cardAPPROVED(), DataHelper.getValidMoth(),
                DataHelper.getValidYear(1), DataHelper.getValidOwner(), DataHelper.getEmptyCvc());
        CardPayment cardPayment = startPage.debitPurchase();
        cardPayment.pageFieldInfo(invalidCard);
        cardPayment.checkErrorMessageCVC();
        assertEquals("0", DBHelper.getNumberOfOrders());
        //failing
    }
}
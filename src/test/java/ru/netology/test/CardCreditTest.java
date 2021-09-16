package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.val;
import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.data.DBHelper;
import ru.netology.page.CardCreditPayment;
import ru.netology.page.CardPayment;
import ru.netology.page.StartPage;


import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CardCreditTest {

    private StartPage startPage;
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
        startPage = open("http://localhost:8080",StartPage.class);
        DBHelper.clearDBTables();
    }


    @Test
    void shouldBeApproved(){
        val validCard = DataHelper.getCardInfo(DataHelper.cardAPPROVED(), DataHelper.getValidMoth(),
                DataHelper.getValidYear(1), DataHelper.getValidOwner(), DataHelper.getValidCvc());
        CardCreditPayment cardCreditPayment = startPage.creditPurchase();
        cardCreditPayment.pageFieldInfo(validCard);
        cardCreditPayment.checkApprovedMessage();
        assertEquals(DataHelper.statusAPPROVED(), DBHelper.getStatusFromCreditRequestEntity());
        assertEquals(DBHelper.getBankIdFromCreditRequestEntity(), DBHelper.getCreditIdFromOrderEntity());
        assertEquals("1", DBHelper.getNumberOfOrders());
    }

    @Test
    void shouldBeDeclinedToBlockedCard() {
        val invalidCard = DataHelper.getCardInfo(DataHelper.cardDECLINED(), DataHelper.getValidMoth(),
                DataHelper.getValidYear(1), DataHelper.getValidOwner(), DataHelper.getValidCvc());
        CardCreditPayment cardCreditPayment = startPage.creditPurchase();
        cardCreditPayment.pageFieldInfo(invalidCard);
        cardCreditPayment.checkDeclinedMessage();
        assertEquals(DataHelper.statusDECLINED(), DBHelper.getStatusFromPaymentEntity());
        assertEquals(DBHelper.getBankIdFromCreditRequestEntity(), DBHelper.getCreditIdFromOrderEntity());
        assertEquals("0", DBHelper.getNumberOfOrders());
    }

    @Test
    void shouldBeDeclinedInvalidCard() {
        val invalidCard = DataHelper.getCardInfo(DataHelper.getInvalidNumberCard(), DataHelper.getValidMoth(),
                DataHelper.getValidYear(1), DataHelper.getValidOwner(), DataHelper.getValidCvc());
        CardCreditPayment cardCreditPayment = startPage.creditPurchase();
        cardCreditPayment.pageFieldInfo(invalidCard);
        cardCreditPayment.checkErrorMessageCard();
        assertEquals("0", DBHelper.getNumberOfOrders());
    }

    @Test
    void shouldBeDeclinedEmptyNumberCard() {
        val invalidCard = DataHelper.getCardInfo(DataHelper.getEmptyNumberCard(), DataHelper.getValidMoth(),
                DataHelper.getValidYear(1), DataHelper.getValidOwner(), DataHelper.getValidCvc());
        CardCreditPayment cardCreditPayment = startPage.creditPurchase();
        cardCreditPayment.pageFieldInfo(invalidCard);
        cardCreditPayment.checkErrorMessageCard();
        assertEquals("0", DBHelper.getNumberOfOrders());
    }

    @Test
    void shouldBeDeclinedInValidMonth() {
        val invalidCard = DataHelper.getCardInfo(DataHelper.cardAPPROVED(), DataHelper.getInValidMoth(),
                DataHelper.getValidYear(1), DataHelper.getValidOwner(), DataHelper.getValidCvc());
        CardCreditPayment cardCreditPayment = startPage.creditPurchase();
        cardCreditPayment.pageFieldInfo(invalidCard);
        cardCreditPayment.checkErrorMessageExpiredMonth();
        assertEquals("0", DBHelper.getNumberOfOrders());
    }

    @Test
    void shouldBeDeclinedExpiredShelfLifeCard() {
        val invalidCard = DataHelper.getCardInfo(DataHelper.cardAPPROVED(), DataHelper.getExpiredShelfLifeCard(),
                DataHelper.getValidYear(0), DataHelper.getValidOwner(), DataHelper.getValidCvc());
        CardCreditPayment cardCreditPayment = startPage.creditPurchase();
        cardCreditPayment.pageFieldInfo(invalidCard);
        cardCreditPayment.checkErrorMessageExpiredMonth();
        assertEquals("0", DBHelper.getNumberOfOrders());
    }

    @Test
    void shouldBeDeclinedEmptyMonth() {
        val invalidCard = DataHelper.getCardInfo(DataHelper.cardAPPROVED(), DataHelper.getEmptyMonth(),
                DataHelper.getValidYear(1), DataHelper.getValidOwner(), DataHelper.getValidCvc());
        CardCreditPayment cardCreditPayment = startPage.creditPurchase();
        cardCreditPayment.pageFieldInfo(invalidCard);
        cardCreditPayment.checkErrorMessageMonth();
        assertEquals("0", DBHelper.getNumberOfOrders());
    }

    @Test
    void shouldBeDeclinedEmptyYear() {
        val invalidCard = DataHelper.getCardInfo(DataHelper.cardAPPROVED(), DataHelper.getValidMoth(),
                DataHelper.getEmptyYear(), DataHelper.getValidOwner(), DataHelper.getValidCvc());
        CardCreditPayment cardCreditPayment = startPage.creditPurchase();
        cardCreditPayment.pageFieldInfo(invalidCard);
        cardCreditPayment.checkErrorMessageYear();
        assertEquals("0", DBHelper.getNumberOfOrders());
    }

    @Test
    void shouldBeDeclinedExpiredYear() {
        val invalidCard = DataHelper.getCardInfo(DataHelper.cardAPPROVED(), DataHelper.getValidMoth(),
                DataHelper.getLastYear(), DataHelper.getValidOwner(), DataHelper.getValidCvc());
        CardCreditPayment cardCreditPayment = startPage.creditPurchase();
        cardCreditPayment.pageFieldInfo(invalidCard);
        cardCreditPayment.checkErrorMessageYearExpired();
        assertEquals("0", DBHelper.getNumberOfOrders());
    }

    @Test
    void shouldBeDeclinedInValidOwner() {
        val invalidCard = DataHelper.getCardInfo(DataHelper.cardAPPROVED(), DataHelper.getValidMoth(),
                DataHelper.getValidYear(1), DataHelper.getInValidOwner(), DataHelper.getValidCvc());
        CardCreditPayment cardCreditPayment = startPage.creditPurchase();
        cardCreditPayment.pageFieldInfo(invalidCard);
        cardCreditPayment.checkErrorMessageOwnerSimbol();
        assertEquals("0", DBHelper.getNumberOfOrders());
    }

    @Test
    void shouldBeDeclinedInValidOwnerRus() {
        val invalidCard = DataHelper.getCardInfo(DataHelper.cardAPPROVED(), DataHelper.getValidMoth(),
                DataHelper.getValidYear(1), DataHelper.getInValidOwnerRus(), DataHelper.getValidCvc());
        CardCreditPayment cardCreditPayment = startPage.creditPurchase();
        cardCreditPayment.pageFieldInfo(invalidCard);
        cardCreditPayment.checkErrorMessageOwnerSimbol();
        assertEquals("0", DBHelper.getNumberOfOrders());
    }

    @Test
    void shouldBeDeclinedEmptyOwner() {
        val invalidCard = DataHelper.getCardInfo(DataHelper.cardAPPROVED(), DataHelper.getValidMoth(),
                DataHelper.getValidYear(1), DataHelper.getEmptyOwner(), DataHelper.getValidCvc());
        CardCreditPayment cardCreditPayment = startPage.creditPurchase();
        cardCreditPayment.pageFieldInfo(invalidCard);
        cardCreditPayment.checkErrorMessageOwner();
        assertEquals("0", DBHelper.getNumberOfOrders());
    }

    @Test
    void shouldBeDeclinedInValidCvc() {
        val invalidCard = DataHelper.getCardInfo(DataHelper.cardAPPROVED(), DataHelper.getValidMoth(),
                DataHelper.getValidYear(1), DataHelper.getValidOwner(), DataHelper.getInValidCvc());
        CardCreditPayment cardCreditPayment = startPage.creditPurchase();
        cardCreditPayment.pageFieldInfo(invalidCard);
        cardCreditPayment.checkErrorMessageCVC();
        assertEquals("0", DBHelper.getNumberOfOrders());
    }

    @Test
    void shouldBeDeclinedEmptyCvc() {
        val invalidCard = DataHelper.getCardInfo(DataHelper.cardAPPROVED(), DataHelper.getValidMoth(),
                DataHelper.getValidYear(1), DataHelper.getValidOwner(), DataHelper.getEmptyCvc());
        CardCreditPayment cardCreditPayment = startPage.creditPurchase();
        cardCreditPayment.pageFieldInfo(invalidCard);
        cardCreditPayment.checkErrorMessageCVC();
        assertEquals("0", DBHelper.getNumberOfOrders());
    }
}
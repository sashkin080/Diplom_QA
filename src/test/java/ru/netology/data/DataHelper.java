package ru.netology.data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;


public class DataHelper {
    private DataHelper() {
    }

    @Value
    public static class CardInfo {
        public String number;
        public String month;
        public String year;
        public String owner;
        public String cvc;
    }

    public static CardInfo getCardInfo(String number, String month, String year, String owner, String cvc) {
        return new CardInfo(number, month, year, owner, cvc);
    }

    static Faker fakerRus = new Faker(new Locale("ru"));
    static Faker fakerEng = new Faker(new Locale("en"));
    static Calendar calendar = new GregorianCalendar();



    public static String cardAPPROVED() {
        return "4444444444444441";
    }
    public static String cardDECLINED() {
        return "4444444444444442";
    }
    public static String statusAPPROVED() {
        return "APPROVED";
    }
    public static String statusDECLINED() {
        return "DECLINED";
    }
    public static String getInvalidNumberCard() {
        return "444444444444444";
    }
    public static String getEmptyNumberCard() {
        return "";
    }

    public static String getValidOwner() {

        return "Ivan Ivanov";
    }

    public static String getInValidOwner() {
        return "Ivanov-Ivan 22";
    }

    public static String getInValidOwnerRus() {
        return fakerRus.name().lastName().firstName();
    }

    public static String getEmptyOwner() {
        return "";
    }

    public static String getValidMoth() {
        return String.format("%02d",calendar.get(Calendar.MONTH));
    }

    public static String getInValidMoth() {
        return "15";
    }

    public static String getExpiredShelfLifeCard() {
        return "05";
    }

    public static String getEmptyMonth() {
        return "";
    }

    public static String getValidYear(int shift) {
        return String.valueOf(calendar.get(Calendar.YEAR) + shift).substring(2);
    }

    public static String getLastYear() { return "20";}

    public static String getEmptyYear() {
        return "";
    }

    public static String getValidCvc() {
        return String.valueOf(fakerEng.number().numberBetween(100, 999));
    }

    public static String getInValidCvc() {
        return String.valueOf(fakerEng.number().numberBetween(10, 99));
    }

    public static String getEmptyCvc() {
        return "";
    }
}

package ru.netology.data;

import lombok.val;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.openqa.selenium.support.ui.Select;

import java.sql.DriverManager;
import java.sql.SQLException;


public class DBHelper {

    private static final String url = System.getProperty("db.url");
    private static final String user = "app";
    private static final String password = "pass";


    public static String getScalarFromTable(String column, String tableName) {
        QueryRunner runner = new QueryRunner();
        String scalar = new String();
        try (val conn = DriverManager.getConnection(url, user, password)
        ) {
            val info = "SELECT " + column + " FROM " + tableName + " ORDER BY created DESC LIMIT 1;";
            val result = runner.query(conn, info, new ScalarHandler<String>());
            scalar = String.valueOf(result);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return scalar;
    }


    public static String getStatusFromPaymentEntity() {
        return getScalarFromTable("status", "payment_entity");
    }

    public static String getTransactionIdFromPaymentEntity() {
        return getScalarFromTable("transaction_id", "payment_entity");
    }

    public static String getPaymentIdFromOrderEntity() {
        return getScalarFromTable("payment_id", "order_entity");
    }

    public static String getCreditIdFromOrderEntity() {
        return getScalarFromTable("payment_id", "order_entity");
    }

    public static String getBankIdFromCreditRequestEntity() {
        return getScalarFromTable("bank_id", "credit_request_entity");
    }

    public static String getNumberOfOrders(String column, String tableName)   {
        QueryRunner runner = new QueryRunner( );
        String numberOfOrders = new String();
        try (val conn = DriverManager.getConnection(url, user, password)
        ) {
            val info = "SELECT " + column + "FROM" + tableName + "count(*)";
            val result = runner.query(conn, info, new ScalarHandler<String>());
            numberOfOrders = String.valueOf(result);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return numberOfOrders;
    }

    public static String getNumberOfOrders1() {
        return getNumberOfOrders("payment_id", "payment_entity");
    }

    public static void clearDBTables() {
        val runner = new QueryRunner();
        try (val conn = DriverManager.getConnection(url, user, password)
        ) {
            runner.update(conn, "DELETE  FROM credit_request_entity;");
            runner.update(conn, "DELETE  FROM payment_entity;");
            runner.update(conn, "DELETE  FROM order_entity;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

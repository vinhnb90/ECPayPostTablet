package org.ecpay.client.rsa;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Logger;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;

/**
 * Created by Joe on 8/7/2016.
 */
public class Utils {

    private static final Logger LOGGER = Logger.getLogger("eStoreCashierLogger");

    public static final String TIMESTAMP_FORMAT = "yyyyMMddHHmmssSSS";
    public static final String DATETIME_FORMAT = "yyyyMMddHHmmss";
    public static final String DATE_FORMAT = "yyyyMMdd";
    public static final String TIME_FORMAT = "HHmmss";
    public static final String UTF_8 = "UTF-8";

    public static Date string2Date(String value, String format) {
        if (value == null) {
            return null;
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            try {
                return sdf.parse(value);
            } catch (ParseException e) {
                LOGGER.error("Exception when converting string to date", e);
                return null;
            }
        }
    }

    public static String date2String(Date date, String format) {
        if (date == null) {
            return null;
        } else {
            Format formater = new SimpleDateFormat(format);
            return formater.format(date);
        }
    }

    public static String toStringBase64(byte[] byteArray) {
        return (new BASE64Encoder().encodeBuffer(byteArray)).replaceAll(String.valueOf((char) 0xd), "");
    }

    public static byte[] toByteArrayBase64(String string) throws IOException {
        return new BASE64Decoder().decodeBuffer(string);
    }

    public static String stringArray2Json(String[] arr) {
        ObjectWriter ow = new ObjectMapper().writer();
        try {
            return ow.writeValueAsString(arr);
        } catch (JsonProcessingException e) {
            LOGGER.error("Exception when converting string to array json", e);
            return "[]";
        }
    }

   /* public static String stringArray2Json(Customer[] arr) {
        ObjectWriter ow = new ObjectMapper().writer();
        try {
            return ow.writeValueAsString(arr);
        } catch (JsonProcessingException e) {
            LOGGER.error("Exception when converting string to array json", e);
            return "[]";
        }
    }*/

    public static String stringArray2Json(Object[] arr) {
        ObjectWriter ow = new ObjectMapper().writer();
        try {
            return ow.writeValueAsString(arr);
        } catch (JsonProcessingException e) {
            LOGGER.error("Exception when converting string to array json", e);
            return "[]";
        }
    }

    public static void putArray(JsonNode jsonNode, String fileName, String[] array) {
        if (array != null && array.length > 0) {
            ArrayNode arrayNode = ((ObjectNode) jsonNode).putArray(fileName);
            for (int i = 0; i < array.length; i++) {
                arrayNode.insert(i, array[i]);
            }
        }
    }

    public static String ifNull(Object str, String def) {
        return str == null ? def : str.toString();
    }
/*
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        InputBO ip = new InputBO();
        Class.forName("oracle.jdbc.driver.OracleDriver");
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(
                    "jdbc:oracle:thin:@192.168.130.8:1521:db11g", "estore", "estore");
            ip.setConnection(connection);
            BaseStoreDAO baseStoreDAO = new BaseStoreDAO();
            baseStoreDAO.loadParamsConfig(connection);
            ip.setPcCode("PD0600");
            ip.setCustomerName("Nguyen Quang Truong");
//            String[] arrCus = new String[2];
//            arrCus[0] = "PD06000062193";
//            arrCus[1] = "PD06000044778";
//            ip.setArrCustomerCode(arrCus);
            EcpayStoreService ecpayStoreService = new EcpayStoreService();
            ResultBO resultBO = ecpayStoreService.getListCustomer(ip);
            LOGGER.info(CommonUtils.convertObjects2Json(resultBO.getListCustomer()));
            LOGGER.info(CommonUtils.convertObjects2Json(resultBO));

        } catch (SQLException e) {
            LOGGER.error("Exception: ", e);
        } finally {
            BaseDAO.close(connection);
        }
    }*/
}

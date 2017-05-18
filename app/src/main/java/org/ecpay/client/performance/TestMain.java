package org.ecpay.client.performance;

import org.ecpay.client.Partner;
import org.ecpay.client.eDongClient;

import java.text.ParseException;

/**
 * Created by Joe on 12/18/2015.
 */
public class TestMain {
  public static int TOTAL = 0;
  public static final String expression = "30 06 14 * * ? *";
  public static final eDongClient client = new eDongClient();

  public static void main(String[] args) throws InterruptedException {
    client.setHost("10.2.20.43");
    client.setPort(1800);
    client.setSync(false);
    client.setTimeout(1000 * 180);
//    client.setPartner(new Partner("DT0605", "SDc1ny4Mvp0ugITsRaox", "YDRVvyoYLDOTg38sZsvI0syLnUcfaeHBC3sbagUw"));
    client.setPartner(new Partner("DT0586", "7WONLmwAJTfiz5KlasgF", "ToRw3KVjTK1VEoMnkDJwOsXD1JumfpN9TJmr4p05"));
    client.init();

//    transfer2Purse();
//    billing();
    query();
  }

  public static void transfer2Purse() {
    String srcPhone = "0966923885";
    Long amount = 500000L;
    String destPhone = "0923638384";
    for (int i = 0; i < 31; i++) {
      try {
        Transfer2PurseRunnable transfer = new Transfer2PurseRunnable(expression, srcPhone, destPhone, amount);
        transfer.setIndex(i + 1);
        transfer.start();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public static void billing() {
    String phone = "0966923885";
    Long amount = 155000L;
    String customerCode = "PA25VY00000";
    String billId = "15126738";
    String providerCode = "NCC0483";
    for (int i = 0; i < 31; i++) {
      try {
        BillingRunnable billing = new BillingRunnable(expression, phone, customerCode + i, amount, billId, providerCode);
        billing.setIndex(i + 1);
        billing.start();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public static void query(){
    String phone = "0966923885";
    String providerCode = "NCC0468";
    String customerCode = "PD0100T27524";
    for(int i = 0; i < 100; i++){
      try {
        QueryRunnable query = new QueryRunnable(expression, phone, customerCode, providerCode);
        query.setIndex(i + 1);
        query.start();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public synchronized static void growthPlus() {
    TOTAL++;
    System.out.println("TOTAL : " + TOTAL);
  }
}

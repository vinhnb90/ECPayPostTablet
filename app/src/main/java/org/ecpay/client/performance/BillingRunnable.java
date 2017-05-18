package org.ecpay.client.performance;

import org.ecpay.client.Utils;
import org.jpos.iso.ISOMsg;

import java.text.ParseException;

/**
 * Created by Joe on 12/30/2015.
 */
public class BillingRunnable extends CronThread {
  private String phone;
  private String customerCode;
  private Long amount;
  private String billId;
  private String providerCode;
  private int index;

  static final String session = "11111111-2222-3333-4444-555555555555";

  private BillingRunnable(String pattern) throws ParseException {
    super(pattern);
  }

  public BillingRunnable(String pattern, String phone, String customerCode, Long amount, String billId, String providerCode) throws ParseException {
    this(pattern);
    this.phone = phone;
    this.customerCode = customerCode;
    this.amount = amount;
    this.billId = billId;
    this.providerCode = providerCode;
  }

  @Override
  protected void doInterval() {
    try {
      long start = System.currentTimeMillis();
      ISOMsg msg = TestMain.client.billing(phone, amount, session, null, providerCode, (long) (Math.random() * 10E8), customerCode, billId);
      long end = System.currentTimeMillis();
      System.out.println("result : " + index + " : " + (end - start) + " milliseconds");
      System.out.println(Utils.toString(msg));
      TestMain.growthPlus();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void setIndex(int index) {
    this.index = index;
  }
}

package org.ecpay.client.performance;

import org.ecpay.client.Utils;
import org.jpos.iso.ISOMsg;

import java.text.ParseException;

/**
 * Created by Joe on 3/2/2016.
 */
public class QueryRunnable extends CronThread {
  private String phone;
  private String customerCode;
  private String providerCode;
  private int index;

  static final String session = "11111111-2222-3333-4444-555555555555";

  private QueryRunnable(String pattern) throws ParseException {
    super(pattern);
  }

  public QueryRunnable(String pattern, String phone, String customerCode, String providerCode) throws ParseException {
    this(pattern);
    this.phone = phone;
    this.customerCode = customerCode;
    this.providerCode = providerCode;
  }

  @Override
  protected void doInterval() {
    try {
      long start = System.currentTimeMillis();
      ISOMsg msg = TestMain.client.queryCustomer(phone, session, providerCode, (long) (Math.random() * 10E8), customerCode);
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

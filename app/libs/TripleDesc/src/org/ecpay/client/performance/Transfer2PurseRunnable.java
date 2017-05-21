package org.ecpay.client.performance;

import java.text.ParseException;

import org.ecpay.client.Utils;
import org.jpos.iso.ISOMsg;

/**
 * Created by Joe on 12/18/2015.
 */
public class Transfer2PurseRunnable extends CronThread {
  private String srcPhone;
  private String destPhone;
  private Long amount;
  private int index;

  private Transfer2PurseRunnable(String pattern) throws ParseException {
    super(pattern);
  }

  public Transfer2PurseRunnable(String pattern, String srcPhone, String destPhone, Long amount) throws ParseException {
    this(pattern);
    this.srcPhone = srcPhone;
    this.destPhone = destPhone;
    this.amount = amount;
  }

  @Override
  protected void doInterval() {
    try {
      long start = System.currentTimeMillis();
      ISOMsg msg = TestMain.client.transferMoney(srcPhone, destPhone, amount, null, null, "performance test", (long) (Math.random() * 10E8));
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

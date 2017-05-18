package org.ecpay.client.performance;

import org.util.convert.TotalConverter;
import org.util.schedule.CronExpression;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: NguyenNhat
 * Date: 7/6/11
 * Time: 4:06 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class CronThread implements Runnable {
  private boolean active = false;
  private Thread runner;
  private CronExpression cronExpression = null;
  private Date nextTime;
  protected String name = "CronThread";

  public CronThread(CronExpression cronExpression) {
    this.cronExpression = cronExpression;
  }

  public CronThread(String pattern) throws ParseException {
    cronExpression = new CronExpression(pattern);
  }

  public void start() throws Exception {
    active = true;
    if (runner == null && cronExpression != null) {
      nextTime = cronExpression.getNextValidTimeAfter(new Date());
      System.out.println("nextTime : " + TotalConverter.toString(nextTime, "yyyy/MM/dd HH:mm:ss"));
      runner = new Thread(this);
      runner.start();
      System.out.println(name + " is starting");
    } else {
      throw new Exception("CronExpression is null");
    }
  }

  @Override
  public void run() {
    Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
    while (active) {
      try {
        Date date = new Date();
        if (date.getTime() / 1000 == nextTime.getTime() / 1000) {
          System.out.println("......(^_^)......");
          nextTime = cronExpression.getNextValidTimeAfter(date);
          doInterval();
        }
        Thread.sleep(500);
      } catch (InterruptedException e) {
        System.out.println(e);
      }
    }
  }

  public void stop() throws Exception {
    active = false;
    nextTime = null;
    System.out.println(name + " is stopped");
  }

  protected abstract void doInterval();

  public Date getNextTime() {
    return nextTime;
  }

  public CronExpression getCronExpression() {
    return cronExpression;
  }

  public void setCronExpression(CronExpression cronExpression) {
    this.cronExpression = cronExpression;
  }
}

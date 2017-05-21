package org.ecpay.client;

import org.ecpay.client.base.ASCIIClient;
import org.ecpay.client.base.BaseClient;
import org.ecpay.client.base.MuxClient;
import org.jpos.iso.ISOBasePackager;
import org.jpos.iso.ISOMsg;
import org.util.convert.TotalConverter;

import java.util.Date;

/**
 * Created by NhatNV on Dec 03, 2014 11:34.
 */
public class eDongClient {
  public static final ISOBasePackager packager = new ECPayPackager();
  public static final String DATETIME_FORMAT = "yyyyMMddHHmmss";
  public static final String DATE_FORMAT = "yyyyMMdd";

  /*Dang ky tai khoan vi eDong*/
  public ISOMsg registerAccount(String phone, String name, String idNumber, String idNumberType, String pin, String pinRetype, long auditNumber) throws Exception {
    ISOMsg msg = new ISOMsg();
    Date current = new Date();
    msg.setPackager(packager);
    msg.setMTI("1100");
    msg.set(2, phone);
    msg.set(3, "600000");
    msg.set(11, String.valueOf(auditNumber));
    msg.set(12, Utils.toString(current, DATETIME_FORMAT));
    msg.set(16, new String(Utils.encodeHex(name.getBytes(Utils.UTF8))));
    msg.set(17, idNumber);
    //TruongNQ 20160421 Bo xung thong tin Id ho chieu hoac the can cuoc khi dang ky eDong (cho cac kenh)
    msg.set(25, idNumberType);
    msg.set(19, pin);
    if (pinRetype != null && !"".equals(pinRetype.trim())) {
      msg.set(20, pinRetype);
    }
    msg.set(32, partner.getCode());
    msg.set(38, String.valueOf(timeout));
    String dataSign = Utils.getDataSign(msg);
    dataSign = Utils.encryptMD5(dataSign + partner.getPrivateKey());
    dataSign = Utils.encryptMD5(dataSign + partner.getPublicKey());
    msg.set(50, dataSign);

    return client.request(msg, timeout);
  }

  /*Login tai khoan vi eDong*/
  public ISOMsg login(String phone, String pin, long auditNumber) throws Exception {
    long t1 = System.currentTimeMillis();
    ISOMsg msg = new ISOMsg();
    Date current = new Date();
    msg.setPackager(packager);
    msg.setMTI("1100");
    msg.set(2, phone);
    msg.set(3, "300000");
    msg.set(11, String.valueOf(auditNumber));
    msg.set(12, Utils.toString(current, DATETIME_FORMAT));
    msg.set(18, pin);
    msg.set(32, partner.getCode());
    msg.set(38, String.valueOf(timeout));
    String dataSign = Utils.getDataSign(msg);
    dataSign = Utils.encryptMD5(dataSign + partner.getPrivateKey());
    dataSign = Utils.encryptMD5(dataSign + partner.getPublicKey());
    msg.set(50, dataSign);
    long t2 = System.currentTimeMillis();
    System.out.println("before request : " + (t2 - t1) + " milliseconds");
    return client.request(msg, timeout);
  }

  public ISOMsg logout(String phone, String session, long auditNumber) throws Exception {
    long t1 = System.currentTimeMillis();
    ISOMsg msg = new ISOMsg();
    Date current = new Date();
    msg.setPackager(packager);
    msg.setMTI("1100");
    msg.set(2, phone);
    msg.set(3, "900000");
    msg.set(11, String.valueOf(auditNumber));
    msg.set(12, Utils.toString(current, DATETIME_FORMAT));
    if (session != null)
      msg.set(30, session);
    msg.set(32, partner.getCode());
    msg.set(38, String.valueOf(timeout));
    String dataSign = Utils.getDataSign(msg);
    dataSign = Utils.encryptMD5(dataSign + partner.getPrivateKey());
    dataSign = Utils.encryptMD5(dataSign + partner.getPublicKey());
    msg.set(50, dataSign);
    long t2 = System.currentTimeMillis();
    System.out.println("before request : " + (t2 - t1) + " milliseconds");
    return client.request(msg, timeout);
  }

  public ISOMsg retryToLogin(String phone, String pin, long auditNumber) throws Exception {
    long t1 = System.currentTimeMillis();
    ISOMsg msg = new ISOMsg();
    Date current = new Date();
    msg.setPackager(packager);
    msg.setMTI("1100");
    msg.set(2, phone);
    msg.set(3, "110000");
    msg.set(11, String.valueOf(auditNumber));
    msg.set(12, Utils.toString(current, DATETIME_FORMAT));
    msg.set(18, pin);
    msg.set(32, partner.getCode());
    msg.set(38, String.valueOf(timeout));
    String dataSign = Utils.getDataSign(msg);
    dataSign = Utils.encryptMD5(dataSign + partner.getPrivateKey());
    dataSign = Utils.encryptMD5(dataSign + partner.getPublicKey());
    msg.set(50, dataSign);
    long t2 = System.currentTimeMillis();
    System.out.println("before request : " + (t2 - t1) + " milliseconds");
    return client.request(msg, timeout);
  }

  /*Lay thong tin tai khoan vi*/
  public ISOMsg getAccountInfo(String phone, long auditNumber) throws Exception {
    ISOMsg msg = new ISOMsg();
    Date current = new Date();
    msg.setPackager(packager);
    msg.setMTI("1100");
    msg.set(2, phone);
    msg.set(3, "800000");
    msg.set(11, String.valueOf(auditNumber));
    msg.set(12, Utils.toString(current, DATETIME_FORMAT));
    msg.set(32, partner.getCode());
    msg.set(38, String.valueOf(timeout));
    String dataSign = Utils.getDataSign(msg);
    dataSign = Utils.encryptMD5(dataSign + partner.getPrivateKey());
    dataSign = Utils.encryptMD5(dataSign + partner.getPublicKey());
    msg.set(50, dataSign);

    return client.request(msg, timeout);
  }

  /*Doi mat khau vi eDong*/
  public ISOMsg changePIN(String phone, String oldPIN, String newPIN, String retypePIN, String session, String otp, long auditNumber) throws Exception {
    ISOMsg msg = new ISOMsg();
    Date current = new Date();
    msg.setPackager(packager);
    msg.setMTI("1100");
    msg.set(2, phone);
    msg.set(3, "700000");
    msg.set(11, String.valueOf(auditNumber));
    msg.set(12, Utils.toString(current, DATETIME_FORMAT));
    msg.set(18, oldPIN);
    msg.set(19, newPIN);
    if (retypePIN != null)
      msg.set(20, retypePIN);
    if (otp != null)
      msg.set(29, otp);
    if (session != null)
      msg.set(30, session);
    msg.set(32, partner.getCode());
    msg.set(38, String.valueOf(timeout));
    String dataSign = Utils.getDataSign(msg);
    dataSign = Utils.encryptMD5(dataSign + partner.getPrivateKey());
    dataSign = Utils.encryptMD5(dataSign + partner.getPublicKey());
    msg.set(50, dataSign);

    return client.request(msg, timeout);
  }

  /*Lay thong tin so du tai khoan vi*/
  public ISOMsg getBalance(String phone, String session, long auditNumber) throws Exception {
    ISOMsg msg = new ISOMsg();
    Date current = new Date();
    msg.setPackager(packager);
    msg.setMTI("1100");
    msg.set(2, phone);
    msg.set(3, "100000");
    msg.set(11, String.valueOf(auditNumber));
    msg.set(12, Utils.toString(current, DATETIME_FORMAT));
    if (session != null)
      msg.set(30, session);
    msg.set(32, partner.getCode());
    msg.set(38, String.valueOf(timeout));
    String dataSign = Utils.getDataSign(msg);
    dataSign = Utils.encryptMD5(dataSign + partner.getPrivateKey());
    dataSign = Utils.encryptMD5(dataSign + partner.getPublicKey());
    msg.set(50, dataSign);

    return client.request(msg, timeout);
  }

  /*Liet ke giao dich SIZE*/
  public ISOMsg listTransactionSize(String phone, String session, int size, long auditNumber) throws Exception {
    ISOMsg msg = new ISOMsg();
    Date current = new Date();
    msg.setPackager(packager);
    msg.setMTI("1100");
    msg.set(2, phone);
    msg.set(3, "400000");
    msg.set(11, String.valueOf(auditNumber));
    msg.set(12, Utils.toString(current, DATETIME_FORMAT));
    if (session != null)
      msg.set(30, session);
    msg.set(32, partner.getCode());
    msg.set(38, String.valueOf(timeout));
    msg.set(111, String.valueOf(size));
    String dataSign = Utils.getDataSign(msg);
    dataSign = Utils.encryptMD5(dataSign + partner.getPrivateKey());
    dataSign = Utils.encryptMD5(dataSign + partner.getPublicKey());
    msg.set(50, dataSign);

    return client.request(msg, timeout);
  }

  /*Liet ke giao dich PAGING*/
  public ISOMsg listTransactionPaging(String phone, String session, Date fromDate, Date toDate,
                                      int pageStart, int pageSize, long auditNumber) throws Exception {
    ISOMsg msg = new ISOMsg();
    Date current = new Date();
    msg.setPackager(packager);
    msg.setMTI("1100");
    msg.set(2, phone);
    msg.set(3, "500000");
    msg.set(11, String.valueOf(auditNumber));
    msg.set(12, Utils.toString(current, DATETIME_FORMAT));
    if (session != null)
      msg.set(30, session);
    msg.set(32, partner.getCode());
    msg.set(38, String.valueOf(timeout));
    msg.set(111, TotalConverter.toString(fromDate, DATETIME_FORMAT));
    msg.set(112, TotalConverter.toString(toDate, DATETIME_FORMAT));
    msg.set(113, String.valueOf(pageStart));
    msg.set(114, String.valueOf(pageSize));
    String dataSign = Utils.getDataSign(msg);
    dataSign = Utils.encryptMD5(dataSign + partner.getPrivateKey());
    dataSign = Utils.encryptMD5(dataSign + partner.getPublicKey());
    msg.set(50, dataSign);

    return client.request(msg, timeout);
  }

  /*Nap tien dien tu doi tac eDong*/
  public ISOMsg loadCashBank(String phone, long amount, String agent, String desc, long auditNumber) throws Exception {
    ISOMsg msg = new ISOMsg();
    Date current = new Date();
    msg.setPackager(packager);
    msg.setMTI("1200");
    msg.set(2, phone);
    msg.set(3, "700000");
    msg.set(4, String.valueOf(amount));
    msg.set(11, String.valueOf(auditNumber));
    msg.set(12, Utils.toString(current, DATETIME_FORMAT));
    msg.set(32, partner.getCode());
    if (desc != null && !desc.matches("^[ ]*$"))
      msg.set(36, new String(Utils.encodeHex(desc.getBytes(Utils.UTF8))));
    msg.set(38, String.valueOf(timeout));
    msg.set(111, agent);

    String dataSign = Utils.getDataSign(msg);
    dataSign = Utils.encryptMD5(dataSign + partner.getPrivateKey());
    dataSign = Utils.encryptMD5(dataSign + partner.getPublicKey());
    msg.set(50, dataSign);

    return client.request(msg, timeout);
  }

  /*Nap tien dien tu tai diem thanh toan*/
  public ISOMsg loadCashPOS(String phone1, long amount, String phone2, String session, long auditNumber) throws Exception {
    ISOMsg msg = new ISOMsg();
    Date current = new Date();
    msg.setPackager(packager);
    msg.setMTI("1200");
    msg.set(2, phone1);
    msg.set(3, "800000");
    msg.set(4, String.valueOf(amount));
    msg.set(11, String.valueOf(auditNumber));
    msg.set(12, Utils.toString(current, DATETIME_FORMAT));
    if (session != null)
      msg.set(30, session);
    msg.set(32, partner.getCode());
    msg.set(38, String.valueOf(timeout));
    msg.set(111, phone2);
    String dataSign = Utils.getDataSign(msg);
    dataSign = Utils.encryptMD5(dataSign + partner.getPrivateKey());
    dataSign = Utils.encryptMD5(dataSign + partner.getPublicKey());
    msg.set(50, dataSign);

    return client.request(msg, timeout);
  }

  /*OTP giao dich chuyen tien*/
  public ISOMsg otpTransferMoney(String srcPhone, String destPhone, long amount, String session, long auditNumber) throws Exception {
    ISOMsg msg = new ISOMsg();
    Date current = new Date();
    msg.setPackager(packager);
    msg.setMTI("1200");
    msg.set(2, srcPhone);
    msg.set(3, "510000");
    msg.set(4, String.valueOf(amount));
    msg.set(11, String.valueOf(auditNumber));
    msg.set(12, Utils.toString(current, DATETIME_FORMAT));
    if (session != null)
      msg.set(30, session);
    msg.set(32, partner.getCode());
    msg.set(38, String.valueOf(timeout));
    msg.set(111, destPhone);

    String dataSign = Utils.getDataSign(msg);
    dataSign = Utils.encryptMD5(dataSign + partner.getPrivateKey());
    dataSign = Utils.encryptMD5(dataSign + partner.getPublicKey());
    msg.set(50, dataSign);

    return client.request(msg, timeout);
  }

  /*Chuyen tien*/
  public ISOMsg transferMoney(String srcPhone, String destPhone, long amount, String session, String otp, String desc, long auditNumber) throws Exception {
    ISOMsg msg = new ISOMsg();
    Date current = new Date();
    msg.setPackager(packager);
    msg.setMTI("1200");
    msg.set(2, srcPhone);
    msg.set(3, "500000");
    msg.set(4, String.valueOf(amount));
    msg.set(11, String.valueOf(auditNumber));
    msg.set(12, Utils.toString(current, DATETIME_FORMAT));
    if (otp != null)
      msg.set(29, otp);
    if (session != null)
      msg.set(30, session);
    msg.set(32, partner.getCode());
    if (desc != null && !desc.matches("^[ ]*$"))
      msg.set(36, new String(Utils.encodeHex(desc.getBytes(Utils.UTF8))));
    msg.set(38, String.valueOf(timeout));
    msg.set(111, destPhone);

    String dataSign = Utils.getDataSign(msg);
    dataSign = Utils.encryptMD5(dataSign + partner.getPrivateKey());
    dataSign = Utils.encryptMD5(dataSign + partner.getPublicKey());
    msg.set(50, dataSign);

    return client.request(msg, timeout);
  }

  /*Lay thong tin no khach hang*/
  public ISOMsg queryCustomer(String phone, String session, String providerCode, long auditNumber, String... reserved) throws Exception {
    ISOMsg msg = new ISOMsg();
    Date current = new Date();
    msg.setPackager(packager);
    msg.setMTI("1100");
    msg.set(2, phone);
    msg.set(3, "200000");
    msg.set(11, String.valueOf(auditNumber));
    msg.set(12, Utils.toString(current, DATETIME_FORMAT));
    if (session != null)
      msg.set(30, session);
    msg.set(32, partner.getCode());
    msg.set(33, providerCode);
    msg.set(38, String.valueOf(timeout));
    if (reserved != null && reserved.length > 0)
      for (int i = 0; i < reserved.length; i++) {
        msg.set(111 + i, reserved[i]);
      }

    String dataSign = Utils.getDataSign(msg);
    dataSign = Utils.encryptMD5(dataSign + partner.getPrivateKey());
    dataSign = Utils.encryptMD5(dataSign + partner.getPublicKey());
    msg.set(50, dataSign);

    return client.request(msg, timeout);
  }

  /*OTP thanh toan hoa don*/
  public ISOMsg otpBilling(String phone, long amount, String session, String providerCode, long auditNumber, String... reserved) throws Exception {
    ISOMsg msg = new ISOMsg();
    Date current = new Date();
    msg.setPackager(packager);
    msg.setMTI("1200");
    msg.set(2, phone);
    msg.set(3, "110000");
    msg.set(4, String.valueOf(amount));
    msg.set(11, String.valueOf(auditNumber));
    msg.set(12, Utils.toString(current, DATETIME_FORMAT));
    if (session != null)
      msg.set(30, session);
    msg.set(32, partner.getCode());
    msg.set(33, providerCode);
    msg.set(38, String.valueOf(timeout));
    if (reserved != null && reserved.length > 0)
      for (int i = 0; i < reserved.length; i++) {
        if (reserved[i] != null && !reserved[i].matches("^[ ]*$"))
          msg.set(111 + i, reserved[i]);
      }

    String dataSign = Utils.getDataSign(msg);
    dataSign = Utils.encryptMD5(dataSign + partner.getPrivateKey());
    dataSign = Utils.encryptMD5(dataSign + partner.getPublicKey());
    msg.set(50, dataSign);

    return client.request(msg, timeout);
  }

  /*Thanh toan hoa don*/
  public ISOMsg billing(String phone, long amount, String session, String otp, String providerCode, long auditNumber, String... reserved) throws Exception {
    ISOMsg msg = new ISOMsg();
    Date current = new Date();
    msg.setPackager(packager);
    msg.setMTI("1200");
    if (phone != null)
      msg.set(2, phone);
    msg.set(3, "100000");
    msg.set(4, String.valueOf(amount));
    msg.set(11, String.valueOf(auditNumber));
    msg.set(12, Utils.toString(current, DATETIME_FORMAT));
    if (otp != null)
      msg.set(29, otp);

    if (session != null)
      msg.set(30, session);
    msg.set(32, partner.getCode());
    msg.set(33, providerCode);
    msg.set(38, String.valueOf(timeout));
    if (reserved != null && reserved.length > 0)
      for (int i = 0; i < reserved.length; i++) {
        if (reserved[i] != null && !reserved[i].matches("^[ ]*$"))
          msg.set(111 + i, reserved[i]);
      }

    String dataSign = Utils.getDataSign(msg);
    dataSign = Utils.encryptMD5(dataSign + partner.getPrivateKey());
    dataSign = Utils.encryptMD5(dataSign + partner.getPublicKey());
    msg.set(50, dataSign);

    return client.request(msg, timeout);
  }

  /*OTP mua ma the*/
  public ISOMsg otpBuyCard(String phone, long amount, String session, String providerCode, long auditNumber) throws Exception {
    ISOMsg msg = new ISOMsg();
    Date current = new Date();
    msg.setPackager(packager);
    msg.setMTI("1200");
    msg.set(2, phone);
    msg.set(3, "310000");
    msg.set(4, String.valueOf(amount));
    msg.set(11, String.valueOf(auditNumber));
    msg.set(12, Utils.toString(current, DATETIME_FORMAT));
    if (session != null)
      msg.set(30, session);
    msg.set(32, partner.getCode());
    msg.set(33, providerCode);
    msg.set(38, String.valueOf(timeout));
    String dataSign = Utils.getDataSign(msg);
    dataSign = Utils.encryptMD5(dataSign + partner.getPrivateKey());
    dataSign = Utils.encryptMD5(dataSign + partner.getPublicKey());
    msg.set(50, dataSign);

    return client.request(msg, timeout);
  }

  /*Mua ma the*/
  public ISOMsg buyCard(String phone, long amount, String session, String otp, String providerCode, long auditNumber) throws Exception {
    ISOMsg msg = new ISOMsg();
    Date current = new Date();
    msg.setPackager(packager);
    msg.setMTI("1200");
    msg.set(2, phone);
    msg.set(3, "300000");
    msg.set(4, String.valueOf(amount));
    msg.set(11, String.valueOf(auditNumber));
    msg.set(12, Utils.toString(current, DATETIME_FORMAT));
    if (otp != null)
      msg.set(29, otp);
    if (session != null)
      msg.set(30, session);
    msg.set(32, partner.getCode());
    msg.set(33, providerCode);
    msg.set(38, String.valueOf(timeout));
    msg.set(111, String.valueOf(amount));
    String dataSign = Utils.getDataSign(msg);
    dataSign = Utils.encryptMD5(dataSign + partner.getPrivateKey());
    dataSign = Utils.encryptMD5(dataSign + partner.getPublicKey());
    msg.set(50, dataSign);

    return client.request(msg, timeout);
  }

  /*OTP nap tien tra truoc*/
  public ISOMsg otpTopupPrepaid(String phone, long amount, String session, String providerCode, long auditNumber, String topupPhone) throws Exception {
    ISOMsg msg = new ISOMsg();
    Date current = new Date();
    msg.setPackager(packager);
    msg.setMTI("1200");
    msg.set(2, phone);
    msg.set(3, "210000");
    msg.set(4, String.valueOf(amount));
    msg.set(11, String.valueOf(auditNumber));
    msg.set(12, Utils.toString(current, DATETIME_FORMAT));
    if (session != null)
      msg.set(30, session);
    msg.set(32, partner.getCode());
    msg.set(33, providerCode);
    msg.set(38, String.valueOf(timeout));
    msg.set(111, topupPhone);
    String dataSign = Utils.getDataSign(msg);
    dataSign = Utils.encryptMD5(dataSign + partner.getPrivateKey());
    dataSign = Utils.encryptMD5(dataSign + partner.getPublicKey());
    msg.set(50, dataSign);

    return client.request(msg, timeout);
  }

  /*Nap tien tra truoc*/
  public ISOMsg topupPrepaid(String phone, long amount, String session, String otp, String providerCode, long auditNumber, String ...reserved) throws Exception {
    ISOMsg msg = new ISOMsg();
    Date current = new Date();
    msg.setPackager(packager);
    msg.setMTI("1200");
    msg.set(2, phone);
    msg.set(3, "200000");
    msg.set(4, String.valueOf(amount));
    msg.set(11, String.valueOf(auditNumber));
    msg.set(12, Utils.toString(current, DATETIME_FORMAT));
    if (otp != null)
      msg.set(29, otp);
    if (session != null)
      msg.set(30, session);
    msg.set(32, partner.getCode());
    msg.set(33, providerCode);
    msg.set(38, String.valueOf(timeout));
//    msg.set(111, topupPhone);
//    msg.set(112, String.valueOf(amount));
    if (reserved != null && reserved.length > 0)
      for (int i = 0; i < reserved.length; i++) {
        if (reserved[i] != null && !reserved[i].matches("^[ ]*$"))
          msg.set(111 + i, reserved[i]);
      }
    String dataSign = Utils.getDataSign(msg);
    dataSign = Utils.encryptMD5(dataSign + partner.getPrivateKey());
    dataSign = Utils.encryptMD5(dataSign + partner.getPublicKey());
    msg.set(50, dataSign);

    return client.request(msg, timeout);
  }


  /*OTP nap tien tra sau*/
  public ISOMsg otpTopupPostpaid(String phone, long amount, String session, String providerCode, long auditNumber, String topupPhone) throws Exception {
    ISOMsg msg = new ISOMsg();
    Date current = new Date();
    msg.setPackager(packager);
    msg.setMTI("1200");
    msg.set(2, phone);
    msg.set(3, "610000");
    msg.set(4, String.valueOf(amount));
    msg.set(11, String.valueOf(auditNumber));
    msg.set(12, Utils.toString(current, DATETIME_FORMAT));
    if (session != null)
      msg.set(30, session);
    msg.set(32, partner.getCode());
    msg.set(33, providerCode);
    msg.set(38, String.valueOf(timeout));
    msg.set(111, topupPhone);
    String dataSign = Utils.getDataSign(msg);
    dataSign = Utils.encryptMD5(dataSign + partner.getPrivateKey());
    dataSign = Utils.encryptMD5(dataSign + partner.getPublicKey());
    msg.set(50, dataSign);

    return client.request(msg, timeout);
  }

  /*Nap tien tra sau*/
  public ISOMsg topupPostpaid(String phone, long amount, String session, String otp, String providerCode, long auditNumber, String topupPhone) throws Exception {
    ISOMsg msg = new ISOMsg();
    Date current = new Date();
    msg.setPackager(packager);
    msg.setMTI("1200");
    msg.set(2, phone);
    msg.set(3, "600000");
    msg.set(4, String.valueOf(amount));
    msg.set(11, String.valueOf(auditNumber));
    msg.set(12, Utils.toString(current, DATETIME_FORMAT));
    if (otp != null)
      msg.set(29, otp);
    if (session != null)
      msg.set(30, session);
    msg.set(32, partner.getCode());
    msg.set(33, providerCode);
    msg.set(38, String.valueOf(timeout));
    msg.set(111, topupPhone);
    msg.set(112, String.valueOf(amount));
    String dataSign = Utils.getDataSign(msg);
    dataSign = Utils.encryptMD5(dataSign + partner.getPrivateKey());
    dataSign = Utils.encryptMD5(dataSign + partner.getPublicKey());
    msg.set(50, dataSign);

    return client.request(msg, timeout);
  }

  /*OTP rut tien ngan hang*/
  public ISOMsg otpWithdrawBank(String phone, long amount, String session, long auditNumber, String... reserved) throws Exception {
    ISOMsg msg = new ISOMsg();
    Date current = new Date();
    msg.setPackager(packager);
    msg.setMTI("1400");
    msg.set(2, phone);
    msg.set(3, "310000");
    msg.set(4, String.valueOf(amount));
    msg.set(11, String.valueOf(auditNumber));
    msg.set(12, Utils.toString(current, DATETIME_FORMAT));
    if (session != null)
      msg.set(30, session);
    msg.set(32, partner.getCode());
    msg.set(38, String.valueOf(timeout));
    if (reserved != null && reserved.length > 0)
      for (int i = 0; i < reserved.length; i++) {
        if (reserved[i] != null && !reserved[i].matches("^[ ]*$"))
          msg.set(111 + i, reserved[i]);
      }

    String dataSign = Utils.getDataSign(msg);
    dataSign = Utils.encryptMD5(dataSign + partner.getPrivateKey());
    dataSign = Utils.encryptMD5(dataSign + partner.getPublicKey());
    msg.set(50, dataSign);

    return client.request(msg, timeout);
  }

  /*Rut tien ngan hang*/
  public ISOMsg withdrawBank(String phone, long amount, String session, String otp, long auditNumber, String... reserved) throws Exception {
    ISOMsg msg = new ISOMsg();
    Date current = new Date();
    msg.setPackager(packager);
    msg.setMTI("1400");
    msg.set(2, phone);
    msg.set(3, "300000");
    msg.set(4, String.valueOf(amount));
    msg.set(11, String.valueOf(auditNumber));
    msg.set(12, Utils.toString(current, DATETIME_FORMAT));
    if (otp != null)
      msg.set(29, otp);
    if (session != null)
      msg.set(30, session);
    msg.set(32, partner.getCode());
    msg.set(38, String.valueOf(timeout));
    if (reserved != null && reserved.length > 0)
      for (int i = 0; i < reserved.length; i++) {
        if (reserved[i] != null && !reserved[i].matches("^[ ]*$"))
          msg.set(111 + i, reserved[i]);
      }

    String dataSign = Utils.getDataSign(msg);
    dataSign = Utils.encryptMD5(dataSign + partner.getPrivateKey());
    dataSign = Utils.encryptMD5(dataSign + partner.getPublicKey());
    msg.set(50, dataSign);

    return client.request(msg, timeout);
  }

  public ISOMsg refund(long transId, Date transDate, String lockRef, String providerCode, long auditNumber) throws Exception {
    ISOMsg msg = new ISOMsg();
    Date current = new Date();
    msg.setPackager(packager);
    msg.setMTI("1400");
    msg.set(3, "200000");
    msg.set(11, String.valueOf(auditNumber));
    msg.set(12, Utils.toString(current, DATETIME_FORMAT));
    msg.set(32, partner.getCode());
    msg.set(38, String.valueOf(timeout));
    msg.set(111, String.valueOf(transId));
    msg.set(112, TotalConverter.toString(transDate, DATE_FORMAT));
    if (lockRef != null)
      msg.set(113, lockRef);
    if (providerCode != null)
      msg.set(33, providerCode);
    String dataSign = Utils.getDataSign(msg);
    dataSign = Utils.encryptMD5(dataSign + partner.getPrivateKey());
    dataSign = Utils.encryptMD5(dataSign + partner.getPublicKey());
    msg.set(50, dataSign);

    return client.request(msg, timeout);
  }

  public ISOMsg reloadCheckTime(long auditNumber) throws Exception {
    ISOMsg msg = new ISOMsg();
    Date current = new Date();
    msg.setPackager(packager);
    msg.setMTI("1800");
    msg.set(3, "100000");
    msg.set(11, String.valueOf(auditNumber));
    msg.set(12, Utils.toString(current, DATETIME_FORMAT));
    msg.set(32, partner.getCode());
    msg.set(38, String.valueOf(timeout));
    String dataSign = Utils.getDataSign(msg);
    dataSign = Utils.encryptMD5(dataSign + partner.getPrivateKey());
    dataSign = Utils.encryptMD5(dataSign + partner.getPublicKey());
    msg.set(50, dataSign);
    return client.request(msg, timeout);
  }

  BaseClient client;

  public void init() {
    if (!sync) {
      client = new MuxClient();
    } else {
      client = new ASCIIClient();
    }
    client.setServerPort(port);
    client.setServerHost(host);
    client.setPackager(packager);
    client.init();
  }

  public void destroy() {
    client.terminal();
  }

  private String host;
  private int port;

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }

  private boolean sync;

  public boolean isSync() {
    return sync;
  }

  public void setSync(boolean sync) {
    this.sync = sync;
  }

  private int timeout;

  public int getTimeout() {
    return timeout;
  }

  public void setTimeout(int timeout) {
    this.timeout = timeout;
  }

  private Partner partner;

  public Partner getPartner() {
    return partner;
  }

  public void setPartner(Partner partner) {
    this.partner = partner;
  }

}

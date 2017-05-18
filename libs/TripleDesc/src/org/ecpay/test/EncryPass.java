package org.ecpay.test;

import org.ecpay.client.Partner;
import org.ecpay.client.Utils;
import org.ecpay.client.eDongClient;
import org.ecpay.client.jce.Crypto;
import org.ecpay.client.jce.TripleDesCBC;
import org.jpos.iso.ISOMsg;

public class EncryPass {

	  public static void main(String[] args) throws Exception {
	    eDongClient client = new eDongClient();
	    client.setHost("127.0.0.1");
	    client.setPort(1800);
	    client.setSync(true);
	    client.setTimeout(1000 * 180);
//	    eInternet
	    client.setPartner(new Partner("DT0602", "Y8oqRyNJnnefR99G8DjX", "idNwOldSOYFWRpZqDTPJTCyap830mrPucY8SJzHN"));
//	    eDongABank
//	    client.setPartner(new Partner("DT0559", "9F3H0bUr9xtDeHb6EREP", "f75lJA1aBAAd9blAZO14UVyahKxSXcj183MDm5zp"));
//	    eInternet
//	    client.setPartner(new Partner("DT0602", "Y8oqRyNJnnefR99G8DjX", "idNwOldSOYFWRpZqDTPJTCyap830mrPucY8SJzHN"));
	    //ecsystem
//	    client.setPartner(new Partner("DT0603", "Y8oqRyNJnnefR99G8DjX", "idNwOldSOYFWRpZqDTPJTCyap830mrPucY8SJzHN"));
	    client.init();
	    ISOMsg msg = null;
	    long start = System.currentTimeMillis();
	    Crypto crypto = new TripleDesCBC(client.getPartner().getKEY().getBytes(), client.getPartner().getIV().getBytes(), false);
//	    String pin1 = new String(Utils.encodeHex(crypto.encrypt("  123456".getBytes())));
//	    String pin2 = new String(Utils.encodeHex(crypto.encrypt("  123456".getBytes())));
//	     msg = client.registerAccount("0977557078", "Ä�Ã o Thá»‹ Thu", "111823333", pin1, pin2, (long) (Math.random() * 10E8));
	    String pin = new String(Utils.encodeHex(crypto.encrypt("  123456".getBytes())));
	    System.out.println("Pin: " + pin);
	    try {
	    	msg = client.login("0963015389", pin, (long) (Math.random() * 10E8));
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }

//	    msg = client.logout("0966923885", "6fdee244-cbe8-4019-95de-5066b2cec334", (long) (Math.random() * 10E8));

//	    msg = client.retryToLogin("0966923885", pin, (long) (Math.random() * 10E8));

//	    msg = client.getAccountInfo("0966923885", (long) (Math.random() * 10E8));

//	    String pin1 = new String(Utils.encodeHex(crypto.encrypt("  123456".getBytes())));
//	    String pin2 = new String(Utils.encodeHex(crypto.encrypt("  654321".getBytes())));
//	    msg = client.changePIN("0966923885", pin1, pin2, pin2, "6fdee244-cbe8-4019-95de-5066b2cec334", null, (long) (Math.random() * 10E8));

//	     msg = client.getBalance("0966923885", "2eaefbff-8b7a-404b-982b-3304c2dca47f", (long) (Math.random() * 10E8));

//	    msg = client.listTransactionSize("0966923885", "88fdee7a-d8f0-44ec-a240-7a1d7d5864eb", 5, (long) (Math.random() * 10E8));

//	    msg = client.listTransactionPaging("0966923885", "172d04d9-7917-4b4d-8aea-11cc7d71d814", DateUtil.getBeginDay(new Date()), DateUtil.getEndDay(new Date()), 0, 20, (long) (Math.random() * 10E8));
	//
//	    msg = client.loadCashBank("01689285664", 5000000L, "BANK", "nap tien", (long) (Math.random() * 10E8));

//	    msg = client.otpTransferMoney("0988310386", "01688162007", 12000L, "5de65cfe-adc3-4fea-9e6f-406eb239d9c1", (long) (Math.random() * 10E8));

//	    msg = client.transferMoney("0988310386", "0972172849", 120000L, "a29ae2de-61bb-4032-9b9a-3d65682406e9", "638244", "", (long) (Math.random() * 10E8));

//	     msg = client.queryCustomer("0966923885","44026d3d-a9e2-4aa0-b853-15f99423b8a3","NCC0468",(long) (Math.random() * 10E8),"PD13000122884");
//	    msg = client.queryCustomer("0966923885", null, "NCC0468", (long) (Math.random() * 10E8), "PD0100T27524");
//	    msg = client.queryCustomer("0966923885", null, "NCC0466", (long) (Math.random() * 10E8), "PE09000012100");
//	    msg = client.queryCustomer("0966923885", null, "NCC0483", (long) (Math.random() * 10E8), "PA24HN0029810");
//	    msg = client.queryCustomer("0966923885", null, "NCC0499", (long) (Math.random() * 10E8), "PC12CC0438490");
//	    msg = client.queryCustomer("0966923885", null, "NCC0498", (long) (Math.random() * 10E8), "PB06030050039");
//	    msg = client.queryCustomer("0966923885", null, "NCC0500", (long) (Math.random() * 10E8), "PU03000099947");

//	    msg = client.otpBilling("0966923885", 12836,"d606d2e8-837b-46db-9d95-5ba998b685f4", "NCC0501", (long) (Math.random() * 10E8), "PE06000121241");

//	    msg = client.billing("0966923885", 12836, "d606d2e8-837b-46db-9d95-5ba998b685f4", "315135", "NCC0466", (long) (Math.random() * 10E8), "PE06000121241", "150230", "01/15(1)");
//	    msg = client.billing("0988310386", 12836, null, null, "NCC0501", (long) (Math.random() * 10E8), "5474827", "12281161");
//	    msg = client.billing("0988310386", 12836, "756c48da-a2b6-4f6d-bd2a-72f01dcc624e", "896091", "NCC0466", (long) (Math.random() * 10E8), "PE06000121242", "150230");
//	    msg = client.billing("0966923885", 12836, null, null, "NCC0483", (long) (Math.random() * 10E8), "PA25VY0121242", "150230");
//	    msg = client.billing("0966923885", 12836, null, null, "NCC0499", (long) (Math.random() * 10E8), "PC12CC0121242", "150230");
//	    msg = client.billing(null, 12836, null, null, "NCC0498", (long) (Math.random() * 10E8), "PB06030121241", "150230");
//	    msg = client.billing("0966923885", 12836, null, null, "NCC0500", (long) (Math.random() * 10E8), "PE06000121241", "150230");


//	    msg = client.otpTopupPrepaid("0988310386", 10000L, "649173da-06f0-4e69-98f2-42ef1929f280", "NCC0469", (long) (Math.random() * 10E8), "0988310386");

	    //msg = client.topupPrepaid("0988310386", 363750, "c443edc3-c58a-48b9-81a1-7e2556356e8b", "972516", "NCC0501", (long) (Math.random() * 10E8), "5438290","3","166");

//	    msg = client.topupPrepaid("0988310386", 10001L, "756c48da-a2b6-4f6d-bd2a-72f01dcc624e", "896091", "NCC0469", (long) (Math.random() * 10E8),"0988310386");

//	    msg = client.otpTopupPostpaid("0966923885", 90000L, "6b3180b7-3caf-4f31-8543-ac9eafb37f16", "NCC0469", (long) (Math.random() * 10E8), "0975973277");

//	    msg = client.topupPostpaid("0988310386", 50000L, "756c48da-a2b6-4f6d-bd2a-72f01dcc624e", "896091", "NCC0470", (long) (Math.random() * 10E8), "0904310386");

//	    msg = client.otpBuyCard("0988310386", 100000L, "f7f9fdfb-36cd-4463-9add-04715ebbfb79", "NCC0469", (long) (Math.random() * 10E8));

//	    msg = client.buyCard("0988310386", 100000L, "f7f9fdfb-36cd-4463-9add-04715ebbfb79", "136182", "NCC0469", (long) (Math.random() * 10E8));

//	    msg = client.otpWithdrawBank("0966923885", 1500000, "291ee962-cbdb-44e5-981b-2123322fd1f6", (long) (Math.random() * 10E8), "970406");

//	    msg = client.withdrawBank("0966923885", 15000000, "291ee962-cbdb-44e5-981b-2123322fd1f6","623195", (long) (Math.random() * 10E8), "970406");

//	    msg = client.refund(-16429905L, new Date(), null, "NCC0466",(long) (Math.random() * 10E8));

//	    msg = client.reloadCheckTime((long) (Math.random() * 10E8));
	    long end = System.currentTimeMillis();
	    System.out.println((end - start) + " milliseconds");
	    System.out.println(Utils.toString(msg));
	 /*   OracleDataSource ds = new OracleDataSource();
	    ds.setURL("jdbc:oracle:thin:@192.168.130.3:1521:orcl");
	    ds.setUser("eDong");
	    ds.setPassword("eDong");

	    JdbcTemplate jdbc = new JdbcTemplate(ds);
	    Map out = jdbc.callFunction("GET__MESSAGE__ID", null);
	    System.out.println(out); */
	  }
}

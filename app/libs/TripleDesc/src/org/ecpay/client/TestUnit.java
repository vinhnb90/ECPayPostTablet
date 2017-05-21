package org.ecpay.client;

/**
 * Created by Joe on 10/9/2015.
 */
public class TestUnit {
  public static void main(String[] args) throws Exception {
//    Partner partner = new Partner("DT0605","SDc1ny4Mvp0ugITsRaox","YDRVvyoYLDOTg38sZsvI0syLnUcfaeHBC3sbagUw");
//    System.out.println(partner.getKEY());
//    System.out.println(partner.getIV());
//    TripleDesCBC cbc = new TripleDesCBC(partner.getKEY().getBytes(), partner.getIV().getBytes(),false);
//    System.out.println(new String(Utils.encodeHex(cbc.encrypt("  159357159357  ".getBytes()))));
    String s;
//    s = "0000000000000";
    s = "000000-123456";
//    s = "000000123456";
    if (s.matches("^0+$")) {
      s = "0";
    }else{
      s = s.replaceFirst("^0+","");
    }
    System.out.println(s);


//    System.out.println(StringUtils.addRight(s,'A',mod));
//    String s = "ECPay thong bao: Tien dien T11/2015 cua Ma KH PD13000122876, la 389.523VND. Truy cap www.ecpay.vn hoac lien he 1900561230 de biet them chi tiet.";
//    Pattern pattern = Pattern.compile("^(.+)[ ](\\d+([.]\\d+)*)+VND[.](.+)$");
//    Matcher matcher = pattern.matcher(s);
//    if(matcher.find()){
//      System.out.println(matcher.group(2));
//    }
  }
}

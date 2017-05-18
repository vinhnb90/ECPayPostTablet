package org.ecpay.test;

import org.ecpay.client.Partner;
import org.ecpay.client.Utils;
import org.ecpay.client.jce.Crypto;
import org.ecpay.client.jce.TripleDesCBC;

public class Test {

	public static void main(String[] args) throws Exception {
		
		//eInternet
		//Partner p = new Partner("DT0602", "Y8oqRyNJnnefR99G8DjX", "idNwOldSOYFWRpZqDTPJTCyap830mrPucY8SJzHN");
		
		//ePos
		//Partner p = new Partner("DT0605", "SDc1ny4Mvp0ugITsRaox", "YDRVvyoYLDOTg38sZsvI0syLnUcfaeHBC3sbagUw");
		
		//Mobile
		/*ecpay.core.partner.code=DT0800
				ecpay.core.private.key=40cXOSxlrFpKxStluA44
				ecpay.core.public.key=0WJhe98o4JY4NkBLOfiFwkTizgBWbh4307ZlGnF3*/
		
		Partner p = new Partner("DT0809", "NuZqoLIILFs27FBeCmxV", "zgdZWXlNDs8yhec1P39BxCqWmJhPrwInfiSUgngw");
		
		/*ecpay.core.partner.code=DT0809
				ecpay.core.private.key=NuZqoLIILFs27FBeCmxV
				ecpay.core.public.key=zgdZWXlNDs8yhec1P39BxCqWmJhPrwInfiSUgngw*/
		
		Crypto crypto = new TripleDesCBC(p.getKEY().getBytes(), p.getIV().getBytes(), false);
	    String pin = new String(Utils.encodeHex(crypto.encrypt("  123456".getBytes())));
	    System.out.println("Pin: " + pin);
	}

}

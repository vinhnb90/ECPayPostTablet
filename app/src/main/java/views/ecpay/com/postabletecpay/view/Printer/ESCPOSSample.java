package views.ecpay.com.postabletecpay.view.Printer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import com.sewoo.jpos.command.ESCPOS;
import com.sewoo.jpos.printer.ESCPOSPrinter;
import com.sewoo.jpos.printer.LKPrint;

import java.io.IOException;

import views.ecpay.com.postabletecpay.R;
import views.ecpay.com.postabletecpay.model.adapter.PayAdapter;
import views.ecpay.com.postabletecpay.util.entities.sqlite.Bill;

//import java.io.UnsupportedEncodingException;
//import android.util.Log;

public class ESCPOSSample
{
	private ESCPOSPrinter posPtr;
	// 0x1B
	private final char ESC = ESCPOS.ESC;
	
	public ESCPOSSample()
	{
		posPtr = new ESCPOSPrinter();
	}
	
//	private final String TAG = "PrinterStsChecker";
	private int rtn;
	private String demtext(int t, String text){
		String space = "";
		int length = t - text.length();
		for(int i = 1; i < length; i ++){
			space = space + " ";
		}
		return space;
	}
	
    public int sample1(Context activity) throws InterruptedException {
		try
		{
			rtn = posPtr.printerSts();
			// Do not check the paper near empty.
			// if( (rtn != 0) && (rtn != ESCPOSConst.STS_PAPERNEAREMPTY)) return rtn;
			// check the paper near empty.
			if( rtn != 0 )  return rtn;
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return rtn;
		}
		int DNTT = 261;

		int dongia1 = 1484;
		int dongia2 = 1533;
		int dongia3 = 1786;
		int dongia4 = 2242;
		int dongia5 = 2453;
		int dongia6 = 2535;

		int tinhlan1 = 50;
		int tinhlan2 = 50;
		int tinhlan3 = 100;
		int tinhlan4 = 100;
		int tinhlan5 = 100;
		int tinhlan6  = DNTT - tinhlan1 - tinhlan2 - tinhlan3 - tinhlan4 - tinhlan5;

		String nameCustom = "Vuong Van Phuong", customId = "123456789", address = "Dong Quang - Quoc Oai - Ha Noi",
				soCongto = "123456",soHo = "987456", idHopDong = "0976956559", textHTTT= "tien mat",textCSDK ="phuong",
				textCSKH = "ECPay",textMoney = "100.000.000đ",textThue = "10.000đ",textSum = "100.010.000đ",nameNV = "Ngoc Linh";


		try {
            posPtr.printBitmap(drawableToBitmap(activity.getResources().getDrawable(R.drawable.ic_evn_hanoi_ecpay_logo)),1,8);
			posPtr.printNormal("\n\n");
			posPtr.printText("Cong ty dien luc Ha Noi" +"\n", LKPrint.LK_ALIGNMENT_CENTER, LKPrint.LK_FNT_BOLD, LKPrint.LK_TXT_1WIDTH);
			posPtr.printText("BIEN NHAN" +"\n", LKPrint.LK_ALIGNMENT_CENTER, LKPrint.LK_FNT_BOLD, LKPrint.LK_TXT_2WIDTH);
			posPtr.printText("THANH TOAN TIEN DIEN" +"\n", LKPrint.LK_ALIGNMENT_CENTER, LKPrint.LK_FNT_BOLD, LKPrint.LK_TXT_1WIDTH);

//            posPtr.printNormal("Ten KH: "+billObj.getName()+"\n");
            posPtr.printNormal("Ten KH: "+ nameCustom +"\n");
            posPtr.printNormal("Đia chi: "+address +"\n");
            posPtr.printNormal("Ma KH: "+customId+"\n");
            posPtr.printNormal("So cong to: "+soCongto +"   Số hộ: "+soHo+"\n");
            posPtr.printNormal("Seri HDDT: "+idHopDong+"\n");
            posPtr.printNormal("Hinh thuc thanh toan: "+textHTTT+"\n");
            posPtr.printNormal("Noi dung thanh toan tien dien: kỳ " + "thang 6"+"\n");
            posPtr.printNormal("Ngay: "+"14/07/2017"+"\n");
            posPtr.printNormal("CSDK: "+textCSDK+"\n");
            posPtr.printNormal("CSCK: "+textCSKH+"\n");
            posPtr.printNormal("--------------------------------");
            posPtr.printNormal("| ĐNTT | Đon gia |   Thanh tien|");
			posPtr.printNormal("--------------------------------");
			if (DNTT < 50){
				posPtr.printText(demtext(7,DNTT + "")+ DNTT +"kwh|\n", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
				posPtr.printText(demtext(10,dongia1 + "")+ dongia1 +"|\n", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
				posPtr.printText(demtext(14,DNTT * dongia1 + "")+ dongia1 +"|\n", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
			}
			if (DNTT < 100){
				posPtr.printText(demtext(7,50 + "")+ 50 +"|", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
				posPtr.printText(demtext(10,dongia1 + "")+ dongia1 +"|", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
				posPtr.printText(demtext(14,DNTT * dongia1 + "")+ dongia1 +"|\n", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);

				posPtr.printText(demtext(7,DNTT-50 + "")+ (DNTT-50) +"|", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
				posPtr.printText(demtext(10,dongia2 + "")+ dongia2 +"|", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
				posPtr.printText(demtext(14, (DNTT - 50)* dongia2 + "")+ (DNTT - 50)* dongia2 +"|\n", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
			}

			if (DNTT < 300){
				posPtr.printText("|" + demtext(7,50 + "")+ 50 +"|", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
				posPtr.printText(demtext(10,dongia1 + "")+ dongia1 +"|", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
				posPtr.printText(demtext(14,50 * dongia1 + "")+ 50*dongia1 +"|\n", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);

				posPtr.printText("|" + demtext(7,50 + "")+ 50 +"|", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
				posPtr.printText(demtext(10,dongia2 + "")+ dongia2 +"|", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
				posPtr.printText(demtext(14, 50* dongia2 + "")+ 50* dongia2 +"|\n", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);

				posPtr.printText("|" + demtext(7,100 + "")+ 100 +"|", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
				posPtr.printText(demtext(10,dongia3 + "")+ dongia3 +"|", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
				posPtr.printText(demtext(14, 100* dongia3 + "")+ 100* dongia3 +"|\n", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);

				posPtr.printText("|" + demtext(7,DNTT - 200 + "")+ (DNTT - 200) +"|", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
				posPtr.printText(demtext(10,dongia4 + "")+ dongia4 +"|", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
				posPtr.printText(demtext(14, (DNTT - 200)* dongia4 + "")+ (DNTT - 200)* dongia4 +"|\n", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
			}
			posPtr.printNormal("--------------------------------");
			posPtr.printNormal("|  "+ DNTT+"kwh"+demtext(12,DNTT + "") +"|");
			posPtr.printText(demtext(14,"466211")+ 466211 +"|\n", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
            posPtr.printNormal("| Thue GTGT      |");
			posPtr.printText(demtext(14,"46621")+ 46621 +"|\n", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
			posPtr.printNormal("--------------------------------");
			posPtr.printNormal("| Tong cong      |");
			posPtr.printText(demtext(14,"512833")+ 512833 +"|\n", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
			posPtr.printNormal("--------------------------------");
			posPtr.printNormal("\n\n");
			posPtr.printNormal("Bang chu:................\n");
			posPtr.printNormal("Ngay:13/07/2017\n");
			posPtr.printText("Nhan vien" +"\n", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
			posPtr.printText(nameNV +"\n", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
			posPtr.printText("0976956559" +"\n", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
			posPtr.printNormal("Khach hang vui long giu lai bien nhan sau khi thanh toan.\n");
			posPtr.printText("Xin cam on!" +"\n", LKPrint.LK_ALIGNMENT_CENTER, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
			posPtr.printText("Luu y:" +"\n\n", LKPrint.LK_ALIGNMENT_LEFT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
			posPtr.printText("ĐT sua chua: 0976208447" +"\n", LKPrint.LK_ALIGNMENT_CENTER, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
			posPtr.printText("--/lan in (2)/--"+"\n", LKPrint.LK_ALIGNMENT_CENTER, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
            posPtr.lineFeed(4);
            posPtr.cutPaper();
        } catch (IOException e) {
            e.printStackTrace();
        }
		return 0;
    }

	public int sample2(Context activity, PayAdapter.BillEntityAdapter billObj) throws InterruptedException {
		try
		{
			rtn = posPtr.printerSts();
			// Do not check the paper near empty.
			// if( (rtn != 0) && (rtn != ESCPOSConst.STS_PAPERNEAREMPTY)) return rtn;
			// check the paper near empty.
			if( rtn != 0 )  return rtn;
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return rtn;
		}
		int DNTT = 261;

		int dongia1 = 1484;
		int dongia2 = 1533;
		int dongia3 = 1786;
		int dongia4 = 2242;
		int dongia5 = 2453;
		int dongia6 = 2535;

		int tinhlan1 = 50;
		int tinhlan2 = 50;
		int tinhlan3 = 100;
		int tinhlan4 = 100;
		int tinhlan5 = 100;
		int tinhlan6  = DNTT - tinhlan1 - tinhlan2 - tinhlan3 - tinhlan4 - tinhlan5;

		String nameCustom = "Vương Văn Phương", customId = "123456789", address = "Đồng Quang - Quốc Oai - Hà Nội",
				soCongto = "123456",soHo = "987456", idHopDong = "0976956559", textHTTT= "tiền mặt",textCSDK ="phương",
				textCSKH = "ECPay",textMoney = "100.000.000đ",textThue = "10.000đ",textSum = "100.010.000đ",nameNV = "Ngọc Linh";


		try {
			posPtr.printBitmap(drawableToBitmap(activity.getResources().getDrawable(R.drawable.ic_evn_hanoi_ecpay_logo)),1,8);
			posPtr.printNormal("\n\n");
			posPtr.printText("Công ty điện lực Hà nội" +"\n", LKPrint.LK_ALIGNMENT_CENTER, LKPrint.LK_FNT_BOLD, LKPrint.LK_TXT_1WIDTH);
			posPtr.printText("BIÊN NHẬN" +"\n", LKPrint.LK_ALIGNMENT_CENTER, LKPrint.LK_FNT_BOLD, LKPrint.LK_TXT_2WIDTH);
			posPtr.printText("THANH TOÁN TIỀN ĐIỆN" +"\n", LKPrint.LK_ALIGNMENT_CENTER, LKPrint.LK_FNT_BOLD, LKPrint.LK_TXT_1WIDTH);

			posPtr.printNormal("Tên khách hàng: "+billObj.getTEN_KHACH_HANG()+"\n");
			posPtr.printNormal("Địa chỉ: "+billObj.getDIA_CHI() +"\n");
			posPtr.printNormal("Mã KH: "+billObj.getMA_KHACH_HANG()+"\n");
			posPtr.printNormal("Số công tơ: "+billObj.getSO_CONG_TO() +"   Số hộ: "+billObj.getSO_HO()+"\n");
			posPtr.printNormal("Seri HĐĐT: "+billObj.getMA_HOA_DON()+"\n");
			posPtr.printNormal("Hình thức thanh toán: "+textHTTT+"\n");
			posPtr.printNormal("Nội dung: thanh toán hóa đơn tiền điện: kỳ " + billObj.getTHANG_THANH_TOAN()+"\n");
			posPtr.printNormal("Ngày: "+billObj.getRequestDate()+"\n");
			posPtr.printNormal("CSDK: "+billObj.getCSDK()+"\n");
			posPtr.printNormal("CSCK: "+billObj.getCSCK()+"\n");
			posPtr.printNormal("--------------------------------");
			posPtr.printNormal("|"+ demtext(16 - billObj.getDNTT().length(),billObj.getDNTT() + "")+"|");
			posPtr.printText(demtext(14,billObj.getTONG_TIEN_CHUA_THUE()+"")+ billObj.getTONG_TIEN_CHUA_THUE() +"|\n", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
			posPtr.printNormal("| Thuế GTGT      |");
			posPtr.printText(demtext(14,billObj.getTONG_TIEN_THUE() + "")+ billObj.getTONG_TIEN_THUE() +"|\n", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
			posPtr.printNormal("--------------------------------");
			posPtr.printNormal("| Tổng cộng      |");
			posPtr.printText(demtext(14,billObj.getTIEN_THANH_TOAN() +"")+ billObj.getTIEN_THANH_TOAN() +"|\n", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
			posPtr.printNormal("--------------------------------");
			posPtr.printNormal("\n\n");
			posPtr.printNormal("Bằng chữ:................\n");
			posPtr.printNormal("Ngày:13/07/2017\n");
			posPtr.printText("Nhân viên" +"\n", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
			posPtr.printText(nameNV +"\n", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
			posPtr.printText("0976956559" +"\n", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
			posPtr.printNormal("Khach hang vui long giu lai bien nhan sau khi thanh toan.\n");
			posPtr.printText("Xin cảm ơn!" +"\n", LKPrint.LK_ALIGNMENT_CENTER, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
			posPtr.printText("ECPAY kinh moi quy khach hang vui long: " +"\n", LKPrint.LK_ALIGNMENT_LEFT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
			posPtr.printText("+ Truy cap trang web http://cskh.hcmpc.com.vn đe nhan hoa don dien tu " +"\n", LKPrint.LK_ALIGNMENT_LEFT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
			posPtr.printText("+ Kiem tra cac thong tin phia tren, neu co van de can giai dap lien he Trung tam cham soc khach hang 1900561230*" +"\n", LKPrint.LK_ALIGNMENT_LEFT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
			posPtr.printText("ĐT sua chua: 0976208447" +"\n", LKPrint.LK_ALIGNMENT_CENTER, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
			posPtr.printText("--/lần in (2)/--"+"\n", LKPrint.LK_ALIGNMENT_CENTER, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
			posPtr.lineFeed(4);
			posPtr.cutPaper();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static Bitmap drawableToBitmap (Drawable drawable) {
		Bitmap bitmap = null;

		if (drawable instanceof BitmapDrawable) {
			BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
			if(bitmapDrawable.getBitmap() != null) {
				return bitmapDrawable.getBitmap();
			}
		}

		if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
			bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
		} else {
			bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
		}

		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	public int Thongbao(Context activity) throws InterruptedException {
		try
		{
			rtn = posPtr.printerSts();
			// Do not check the paper near empty.
			// if( (rtn != 0) && (rtn != ESCPOSConst.STS_PAPERNEAREMPTY)) return rtn;
			// check the paper near empty.
			if( rtn != 0 )  return rtn;
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return rtn;
		}
		int DNTT = 261;

		int dongia1 = 1484;
		int dongia2 = 1533;
		int dongia3 = 1786;
		int dongia4 = 2242;
		int dongia5 = 2453;
		int dongia6 = 2535;

		int tinhlan1 = 50;
		int tinhlan2 = 50;
		int tinhlan3 = 100;
		int tinhlan4 = 100;
		int tinhlan5 = 100;
		int tinhlan6  = DNTT - tinhlan1 - tinhlan2 - tinhlan3 - tinhlan4 - tinhlan5;

		String nameCustom = "Vuong Van Phuong", customId = "123456789", address = "Dong Quang - Quoc Oai - Ha Noi",
				soCongto = "123456",soHo = "987456", idHopDong = "0976956559", textHTTT= "tien mat",textCSDK ="phuong",
				textCSKH = "ECPay",textMoney = "100.000.000đ",textThue = "10.000đ",textSum = "100.010.000đ",nameNV = "Ngoc Linh";


		try {
			posPtr.printBitmap(drawableToBitmap(activity.getResources().getDrawable(R.drawable.ic_evn_hanoi_ecpay_logo)),1,8);
			posPtr.printNormal("\n\n");
			posPtr.printText("Cong ty dien luc Ha Noi" +"\n", LKPrint.LK_ALIGNMENT_CENTER, LKPrint.LK_FNT_BOLD, LKPrint.LK_TXT_1WIDTH);
			posPtr.printText("THONG BAO TIEN DIEN" +"\n", LKPrint.LK_ALIGNMENT_CENTER, LKPrint.LK_FNT_BOLD, LKPrint.LK_TXT_1WIDTH);
			posPtr.printText("Khong co gia tri thanh toan" +"\n", LKPrint.LK_ALIGNMENT_CENTER, LKPrint.LK_FNT_BOLD, LKPrint.LK_TXT_1WIDTH);

//            posPtr.printNormal("Ten KH: "+billObj.getName()+"\n");
			posPtr.printNormal("Ten KH: "+ nameCustom +"\n");
			posPtr.printNormal("Đia chi: "+address +"\n");
			posPtr.printNormal("Ma KH: "+customId+"\n");
			posPtr.printNormal("So cong to: "+soCongto +"   Số hộ: "+soHo+"\n");
			posPtr.printNormal("Seri HDDT: "+idHopDong+"\n");
			posPtr.printNormal("Hinh thuc thanh toan: "+textHTTT+"\n");
			posPtr.printNormal("Noi dung thanh toan tien dien: kỳ " + "thang 6"+"\n");
			posPtr.printNormal("Ngay: "+"14/07/2017"+"\n");
			posPtr.printNormal("CSDK: "+textCSDK+"\n");
			posPtr.printNormal("CSCK: "+textCSKH+"\n");
			posPtr.printNormal("--------------------------------");
			posPtr.printNormal("| ĐNTT | Đon gia |   Thanh tien|");
			posPtr.printNormal("--------------------------------");
			if (DNTT < 50){
				posPtr.printText(demtext(7,DNTT + "")+ DNTT +"kwh|\n", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
				posPtr.printText(demtext(10,dongia1 + "")+ dongia1 +"|\n", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
				posPtr.printText(demtext(14,DNTT * dongia1 + "")+ dongia1 +"|\n", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
			}
			if (DNTT < 100){
				posPtr.printText(demtext(7,50 + "")+ 50 +"|", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
				posPtr.printText(demtext(10,dongia1 + "")+ dongia1 +"|", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
				posPtr.printText(demtext(14,DNTT * dongia1 + "")+ dongia1 +"|\n", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);

				posPtr.printText(demtext(7,DNTT-50 + "")+ (DNTT-50) +"|", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
				posPtr.printText(demtext(10,dongia2 + "")+ dongia2 +"|", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
				posPtr.printText(demtext(14, (DNTT - 50)* dongia2 + "")+ (DNTT - 50)* dongia2 +"|\n", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
			}

			if (DNTT < 300){
				posPtr.printText("|" + demtext(7,50 + "")+ 50 +"|", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
				posPtr.printText(demtext(10,dongia1 + "")+ dongia1 +"|", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
				posPtr.printText(demtext(14,50 * dongia1 + "")+ 50*dongia1 +"|\n", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);

				posPtr.printText("|" + demtext(7,50 + "")+ 50 +"|", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
				posPtr.printText(demtext(10,dongia2 + "")+ dongia2 +"|", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
				posPtr.printText(demtext(14, 50* dongia2 + "")+ 50* dongia2 +"|\n", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);

				posPtr.printText("|" + demtext(7,100 + "")+ 100 +"|", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
				posPtr.printText(demtext(10,dongia3 + "")+ dongia3 +"|", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
				posPtr.printText(demtext(14, 100* dongia3 + "")+ 100* dongia3 +"|\n", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);

				posPtr.printText("|" + demtext(7,DNTT - 200 + "")+ (DNTT - 200) +"|", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
				posPtr.printText(demtext(10,dongia4 + "")+ dongia4 +"|", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
				posPtr.printText(demtext(14, (DNTT - 200)* dongia4 + "")+ (DNTT - 200)* dongia4 +"|\n", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
			}
			posPtr.printNormal("--------------------------------");
			posPtr.printNormal("|  "+ demtext(12,DNTT + "")+ DNTT +"kwh|");
			posPtr.printText(demtext(14,"466211")+ 466211 +"|\n", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
			posPtr.printNormal("| Thue GTGT      |");
			posPtr.printText(demtext(14,"46621")+ 46621 +"|\n", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
			posPtr.printNormal("--------------------------------");
			posPtr.printNormal("| Tong cong      |");
			posPtr.printText(demtext(14,"512833")+ 512833 +"|\n", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
			posPtr.printNormal("--------------------------------");
			posPtr.printNormal("\n\n");
			posPtr.printNormal("Ngay thong bao:13/07/2017\n");
			posPtr.printText("Nhan vien" +"\n", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
			posPtr.printText(nameNV +"\n", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
			posPtr.printText("0976956559" +"\n", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
			posPtr.printNormal("De nghi thanh toan truoc ngay: 20/07/2017.\n");
			posPtr.printText("--/lan in (2)/--"+"\n", LKPrint.LK_ALIGNMENT_CENTER, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
			posPtr.lineFeed(4);
			posPtr.cutPaper();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}
}
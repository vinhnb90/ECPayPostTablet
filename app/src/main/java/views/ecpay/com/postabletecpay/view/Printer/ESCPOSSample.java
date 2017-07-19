package views.ecpay.com.postabletecpay.view.Printer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.google.zxing.aztec.encoder.Encoder;
import com.google.zxing.common.StringUtils;
import com.sewoo.jpos.command.ESCPOS;
import com.sewoo.jpos.printer.ESCPOSPrinter;
import com.sewoo.jpos.printer.LKPrint;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.StringTokenizer;

import views.ecpay.com.postabletecpay.R;
import views.ecpay.com.postabletecpay.model.adapter.PayAdapter;
import views.ecpay.com.postabletecpay.util.commons.Common;
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
	
    public int sample1(Context activity,PayAdapter.BillEntityAdapter billObj) throws InterruptedException {
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
        String MCS[] = billObj.getCHI_TIET_MCS().split(";");
        String TIEN[] = billObj.getCHI_TIET_TIEN_MCS().split(";");
        String DG[] = billObj.getCHI_TIET_KG().split(";");
        ArrayList<TienTheoChiSo> tienTheoChiSos = new ArrayList<>();
        for(int i=0; i<MCS.length; i++){
            TienTheoChiSo tienTheoChiSo = new TienTheoChiSo();
            tienTheoChiSo.setChiSo(MCS[i]);
            tienTheoChiSo.setDonGia(DG[i]);
            tienTheoChiSo.setTienTheoChiSo(TIEN[i]);
            tienTheoChiSos.add(tienTheoChiSo);
        }

		String textHTTT= "tien mat",nameNV = "Ngoc Linh";


		try {
            posPtr.printBitmap(drawableToBitmap(activity.getResources().getDrawable(R.drawable.ic_evn_hanoi_ecpay_logo)),1,8);
			posPtr.printNormal("\n\n");
			posPtr.printText(Common.unAccent(Common.PROVIDER_CODE.findCodeMessage(billObj.getMA_DIEN_LUC()).getMessage()) +"\n", LKPrint.LK_ALIGNMENT_CENTER, LKPrint.LK_FNT_BOLD, LKPrint.LK_TXT_1WIDTH);
			posPtr.printText("BIEN NHAN" +"\n", LKPrint.LK_ALIGNMENT_CENTER, LKPrint.LK_FNT_BOLD, LKPrint.LK_TXT_2WIDTH);
			posPtr.printText("THANH TOAN TIEN DIEN" +"\n", LKPrint.LK_ALIGNMENT_CENTER, LKPrint.LK_FNT_BOLD, LKPrint.LK_TXT_1WIDTH);

//            posPtr.printNormal("Ten KH: "+billObj.getName()+"\n");
            posPtr.printNormal("Ten KH: "+ Common.unAccent(billObj.getTEN_KHACH_HANG()) +"\n");
            posPtr.printNormal("Dia chi: "+ Common.unAccent(billObj.getDIA_CHI()) +"\n");
            posPtr.printNormal("Ma KH: "+billObj.getMA_KHACH_HANG()+"\n");
            posPtr.printNormal("So cong to: "+billObj.getSO_CONG_TO() +"   So ho: "+billObj.getSO_HO()+"\n");
            posPtr.printNormal("Seri HDDT: "+billObj.getMA_HOA_DON()+"\n");
            posPtr.printNormal("Hinh thuc thanh toan: "+textHTTT+"\n");
            posPtr.printNormal("Noi dung thanh toan tien dien: ky " + billObj.getTHANG_THANH_TOAN()+"\n\n");
            posPtr.printNormal("CSDK: "+billObj.getCSDK()+"\n");
            posPtr.printNormal("CSCK: "+billObj.getCSCK()+"\n");
            posPtr.printNormal("--------------------------------");
            posPtr.printNormal("| DNTT | Don gia |   Thanh tien|");
			posPtr.printNormal("--------------------------------");
            for (int i = 0; i <= tienTheoChiSos.size();i++){
                posPtr.printText(demtext(7,tienTheoChiSos.get(i).getChiSo() + "")+ tienTheoChiSos.get(i).getChiSo() +"kwh|\n", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
                posPtr.printText(demtext(10,tienTheoChiSos.get(i).getDonGia() + "")+ tienTheoChiSos.get(i).getDonGia() +"|\n", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
                posPtr.printText(demtext(14,tienTheoChiSos.get(i).getTienTheoChiSo() + "")+ tienTheoChiSos.get(i).getTienTheoChiSo() +"|\n", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);

            }
			posPtr.printNormal("--------------------------------");
            posPtr.printNormal("|  "+  billObj.getDNTT() + demtext(12,billObj.getDNTT() + "")+"kwh|");
            posPtr.printText(demtext(14,billObj.getTONG_TIEN_CHUA_THUE())+ billObj.getTONG_TIEN_CHUA_THUE() +"|\n", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
            posPtr.printNormal("| Thue GTGT      |");
            posPtr.printText(demtext(14,billObj.getTONG_TIEN_THUE() + "")+ billObj.getTONG_TIEN_THUE() +"|\n", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
            posPtr.printNormal("--------------------------------");
            posPtr.printNormal("| Tong cong      |");
            posPtr.printText(demtext(14,billObj.getTIEN_THANH_TOAN()+"")+ billObj.getTIEN_THANH_TOAN() +"|\n", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
            posPtr.printNormal("--------------------------------");
			posPtr.printNormal("\n\n");
			posPtr.printNormal("Bang chu:................\n");
			posPtr.printNormal("Ngay: "+ billObj.getRequestDate()+"\n");
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

		String nameNV = "Ngọc Linh";

		try {
			posPtr.printBitmap(drawableToBitmap(activity.getResources().getDrawable(R.drawable.ic_evn_hanoi_ecpay_logo)),1,8);
			posPtr.printNormal("\n\n");
			posPtr.printText(Common.unAccent(Common.PROVIDER_CODE.findCodeMessage(billObj.getMA_DIEN_LUC()).getMessage()) +"\n", LKPrint.LK_ALIGNMENT_CENTER, LKPrint.LK_FNT_BOLD, LKPrint.LK_TXT_1WIDTH);
			posPtr.printText("BIEN NHAN" +"\n", LKPrint.LK_ALIGNMENT_CENTER, LKPrint.LK_FNT_BOLD, LKPrint.LK_TXT_2WIDTH);
			posPtr.printText("THANH TOAN TIEN ĐIEN" +"\n", LKPrint.LK_ALIGNMENT_CENTER, LKPrint.LK_FNT_BOLD, LKPrint.LK_TXT_1WIDTH);

			posPtr.printNormal("Ten KH: "+Common.unAccent(billObj.getTEN_KHACH_HANG())+"\n");
			posPtr.printNormal("Dia chi: "+Common.unAccent(billObj.getDIA_CHI()) +"\n");
			posPtr.printNormal("Ma KH: "+billObj.getMA_KHACH_HANG()+"\n");
			posPtr.printNormal("So GCS: "+billObj.getSO_GCS()+"\n");
			posPtr.printNormal("So cong to: "+billObj.getSO_CONG_TO() +"   Số hộ: "+billObj.getSO_HO()+"\n");
			posPtr.printNormal("Hình thức thanh toán: tien mat\n");
			posPtr.printNormal("Nội dung: thanh toán hóa đơn tiền điện: kỳ " + billObj.getTHANG_THANH_TOAN()+"\n");

			posPtr.printNormal("--------------------------------");
			posPtr.printNormal("|"+ demtext(16,billObj.getDNTT() + "")+ billObj.getDNTT()+"|");
            posPtr.printText(demtext(14,billObj.getTONG_TIEN_CHUA_THUE()+"")+ billObj.getTONG_TIEN_CHUA_THUE() +"|\n", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
            posPtr.printNormal("| Thanh tien     |");
            posPtr.printText(demtext(14,billObj.getTONG_TIEN_CHUA_THUE())+ billObj.getTONG_TIEN_CHUA_THUE() +"|\n", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
            posPtr.printNormal("--------------------------------");
            posPtr.printNormal("| Thuế GTGT      |");
			posPtr.printText(demtext(14,billObj.getTONG_TIEN_THUE() + "")+ billObj.getTONG_TIEN_THUE() +"|\n", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
			posPtr.printNormal("--------------------------------");
			posPtr.printNormal("| Tổng cộng      |");
			posPtr.printText(demtext(14,billObj.getTIEN_THANH_TOAN() +"")+ billObj.getTIEN_THANH_TOAN() +"|\n", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
			posPtr.printNormal("--------------------------------");
			posPtr.printNormal("\n\n");
			posPtr.printNormal("Bằng chữ:................\n");
			posPtr.printNormal("Ngày: "+ billObj.getRequestDate()+"\n");
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

	public int Thongbao(Context activity, PayAdapter.BillEntityAdapter billEntityAdapter) throws InterruptedException {
		try
		{
			rtn = posPtr.printerSts();
			if( rtn != 0 )  return rtn;
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return rtn;
		}
		String MCS[] = billEntityAdapter.getCHI_TIET_MCS().split(";");
		String TIEN[] = billEntityAdapter.getCHI_TIET_TIEN_MCS().split(";");
		String DG[] = billEntityAdapter.getCHI_TIET_KG().split(";");
		ArrayList<TienTheoChiSo> tienTheoChiSos = new ArrayList<>();
		for(int i=0; i<MCS.length; i++){
			TienTheoChiSo tienTheoChiSo = new TienTheoChiSo();
			tienTheoChiSo.setChiSo(MCS[i]);
			tienTheoChiSo.setDonGia(DG[i]);
			tienTheoChiSo.setTienTheoChiSo(TIEN[i]);
			tienTheoChiSos.add(tienTheoChiSo);
		}

		String textHTTT= "tien mat",nameNV = "Ngoc Linh";

		try {
			posPtr.printBitmap(drawableToBitmap(activity.getResources().getDrawable(R.drawable.ic_evn_hanoi_ecpay_logo)),1,8);
			posPtr.printNormal("\n\n");
			posPtr.printText(Common.unAccent(billEntityAdapter.getMA_DL_MO_RONG()) +"\n", LKPrint.LK_ALIGNMENT_CENTER, LKPrint.LK_FNT_BOLD, LKPrint.LK_TXT_1WIDTH);
			posPtr.printText(Common.unAccent("THÔNG BÁO TIỀN ĐIỆN") +"\n", LKPrint.LK_ALIGNMENT_CENTER, LKPrint.LK_FNT_BOLD, LKPrint.LK_TXT_1WIDTH);
			posPtr.printText(Common.unAccent("(Không có giá trị thanh toán)") +"\n", LKPrint.LK_ALIGNMENT_CENTER,
					LKPrint.LK_FNT_BOLD, LKPrint.LK_TXT_1WIDTH);
			posPtr.printNormal("Ten KH: "+ Common.unAccent(billEntityAdapter.getTEN_KHACH_HANG()) +"\n");
			posPtr.printNormal("Dia chi: "+ Common.unAccent(billEntityAdapter.getDIA_CHI()) +"\n");
			posPtr.printNormal("Ma KH: "+billEntityAdapter.getMA_KHACH_HANG()+"\n");
			posPtr.printNormal("So cong to: "+billEntityAdapter.getSO_CONG_TO() +"   So ho: "+billEntityAdapter.getSO_HO()+"\n");
			posPtr.printNormal("Seri HDDT: "+billEntityAdapter.getMA_HOA_DON()+"\n");
			posPtr.printNormal("Hinh thuc thanh toan: "+textHTTT+"\n");
			posPtr.printNormal("Noi dung thanh toan tien dien: ky " + billEntityAdapter.getTHANG_THANH_TOAN()+"\n");
			posPtr.printNormal("Ngay: "+"14/07/2017"+"\n");
			posPtr.printNormal("CSDK: "+billEntityAdapter.getCSDK()+"\n");
			posPtr.printNormal("CSCK: "+billEntityAdapter.getCSCK()+"\n");
			posPtr.printNormal("--------------------------------");
			posPtr.printNormal("| DNTT | Don gia |   Thanh tien|");
			posPtr.printNormal("--------------------------------");
			for (int i = 0; i <= tienTheoChiSos.size();i++){
				posPtr.printText(demtext(7,tienTheoChiSos.get(i).getChiSo() + "")+ tienTheoChiSos.get(i).getChiSo() +"kwh|\n", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
				posPtr.printText(demtext(10,tienTheoChiSos.get(i).getDonGia() + "")+ tienTheoChiSos.get(i).getDonGia() +"|\n", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
				posPtr.printText(demtext(14,tienTheoChiSos.get(i).getTienTheoChiSo() + "")+ tienTheoChiSos.get(i).getTienTheoChiSo() +"|\n", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);

			}
			posPtr.printNormal("--------------------------------");
			posPtr.printNormal("|  "+  billEntityAdapter.getDNTT() + demtext(12,billEntityAdapter.getDNTT() + "")+"kwh|");
			posPtr.printText(demtext(14,billEntityAdapter.getTONG_TIEN_CHUA_THUE())+ billEntityAdapter.getTONG_TIEN_CHUA_THUE() +"|\n", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
			posPtr.printNormal("| Thue GTGT      |");
			posPtr.printText(demtext(14,billEntityAdapter.getTONG_TIEN_THUE())+ billEntityAdapter.getTONG_TIEN_THUE() +"|\n", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
			posPtr.printNormal("--------------------------------");
			posPtr.printNormal("| Tong cong      |");
			posPtr.printText(demtext(14,billEntityAdapter.getTIEN_THANH_TOAN()+"")+ billEntityAdapter.getTIEN_THANH_TOAN() +"|\n", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
			posPtr.printNormal("--------------------------------");
			posPtr.printNormal("\n\n");
			posPtr.printNormal("Ngay thong bao: "+billEntityAdapter.getRequestDate()+"\n");
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

	private class TienTheoChiSo{
		private String chiSo;
		private String tienTheoChiSo;
		private String donGia;

		String getChiSo() {
			return chiSo;
		}

		void setChiSo(String chiSo) {
			this.chiSo = chiSo;
		}

		String getTienTheoChiSo() {
			return tienTheoChiSo;
		}

		void setTienTheoChiSo(String tienTheoChiSo) {
			this.tienTheoChiSo = tienTheoChiSo;
		}

		String getDonGia() {
			return donGia;
		}

		void setDonGia(String donGia) {
			this.donGia = donGia;
		}
	}
}
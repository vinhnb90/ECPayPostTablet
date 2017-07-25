package views.ecpay.com.postabletecpay.view.Printer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.bbpos.simplyprint.SimplyPrintController;
import com.sewoo.jpos.printer.LKPrint;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import views.ecpay.com.postabletecpay.R;
import views.ecpay.com.postabletecpay.model.adapter.PayAdapter;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.util.entities.sqlite.Account;

import static android.content.ContentValues.TAG;

public class ReceiptUtility {

    private static byte[] INIT = {0x1B, 0x40};
    private static byte[] POWER_ON = {0x1B, 0x3D, 0x01};
    private static byte[] POWER_OFF = {0x1B, 0x3D, 0x02};
    private static byte[] NEW_LINE = {0x0A};
    private static byte[] ALIGN_LEFT = {0x1B, 0x61, 0x00};
    private static byte[] ALIGN_CENTER = {0x1B, 0x61, 0x01};
    private static byte[] ALIGN_RIGHT = {0x1B, 0x61, 0x02};
    private static byte[] EMPHASIZE_ON = {0x1B, 0x45, 0x01};
    private static byte[] EMPHASIZE_OFF = {0x1B, 0x45, 0x00};
    private static byte[] FONT_5X8 = {0x1B, 0x4D, 0x00};
    private static byte[] FONT_5X12 = {0x1B, 0x4D, 0x01};
    private static byte[] FONT_8X12 = {0x1B, 0x4D, 0x02};
    private static byte[] FONT_10X18 = {0x1B, 0x4D, 0x03};
    private static byte[] FONT_SIZE_0 = {0x1D, 0x21, 0x00};
    private static byte[] FONT_SIZE_1 = {0x1D, 0x21, 0x11};
    private static byte[] CHAR_SPACING_0 = {0x1B, 0x20, 0x00};
    private static byte[] CHAR_SPACING_1 = {0x1B, 0x20, 0x01};

    private static enum TYPE_TEXT {
        UNICODE_2_COLLUMN(14),
//        UNICODE_3_COLLUMN(14),
        UNICODE_3_COLLUMN(6),
        UNICODE_4_COLLUMN(16),
        ENGLISH(11);

        private int fontSize;

        TYPE_TEXT(int fontSize) {
            this.fontSize = fontSize;
        }

        public int getFontSize() {
            return fontSize;
        }
    }

    private static byte[] hexToByteArray(String s) {
        if (s == null) {
            s = "";
        }
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        for (int i = 0; i < s.length() - 1; i += 2) {
            String data = s.substring(i, i + 2);
            bout.write(Integer.parseInt(data, 16));
        }
        return bout.toByteArray();
    }

    private static byte[] convertBitmap(Bitmap bitmap, int targetWidth, int threshold) {
        int targetHeight = (int) Math.round((double) targetWidth / (double) bitmap.getWidth() * (double) bitmap.getHeight());

        byte[] pixels = new byte[targetWidth * targetHeight];
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, false);
        for (int j = 0; j < scaledBitmap.getHeight(); ++j) {
            for (int i = 0; i < scaledBitmap.getWidth(); ++i) {
                int pixel = scaledBitmap.getPixel(i, j);
                int alpha = (pixel >> 24) & 0xFF;
                int r = (pixel >> 16) & 0xFF;
                int g = (pixel >> 8) & 0xFF;
                int b = pixel & 0xFF;
                if (alpha < 50) {
                    pixels[i + j * scaledBitmap.getWidth()] = 0;
                } else if ((r + g + b) / 3 >= threshold) {
                    pixels[i + j * scaledBitmap.getWidth()] = 0;
                } else {
                    pixels[i + j * scaledBitmap.getWidth()] = 1;
                }
            }
        }

        byte[] output = new byte[scaledBitmap.getWidth() * (int) Math.ceil((double) scaledBitmap.getHeight() / (double) 8)];

        for (int i = 0; i < scaledBitmap.getWidth(); ++i) {
            for (int j = 0; j < (int) Math.ceil((double) scaledBitmap.getHeight() / (double) 8); ++j) {
                for (int n = 0; n < 8; ++n) {
                    if (j * 8 + n < scaledBitmap.getHeight()) {
                        output[i + j * scaledBitmap.getWidth()] |= pixels[i + (j * 8 + n) * scaledBitmap.getWidth()] << (7 - n);
                    }
                }
            }
        }

        return output;
    }

    private static byte[] convertBarcode(Bitmap bitmap, int targetWidth, int threshold) {
        int targetHeight = (int) Math.round((double) targetWidth / (double) bitmap.getWidth() * (double) bitmap.getHeight());

        byte[] pixels = new byte[targetWidth];
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, false);
        for (int i = 0; i < scaledBitmap.getWidth(); ++i) {
            int pixel = scaledBitmap.getPixel(i, scaledBitmap.getHeight() / 2);
            int alpha = (pixel >> 24) & 0xFF;
            int r = (pixel >> 16) & 0xFF;
            int g = (pixel >> 8) & 0xFF;
            int b = pixel & 0xFF;
            if (alpha < 50) {
                pixels[i] = 0;
            } else if ((r + g + b) / 3 >= threshold) {
                pixels[i] = 0;
            } else {
                pixels[i] = 1;
            }
        }

        byte[] output = new byte[(int) Math.ceil((double) scaledBitmap.getWidth() / 8.0)];

        for (int i = 0; i < scaledBitmap.getWidth(); ++i) {
            output[i / 8] |= pixels[i] << (7 - (i % 8));
        }

        return output;
    }

    public static byte[] genReceiptTest(Context context, PayAdapter.BillEntityAdapter billObj) {
        int lineWidth = 384;
        int size0NoEmphasizeLineWidth = 384 / 8; //line width / font width
        int size0NoEmphasizeText = 43; //line width / font width
        String singleLine = "";
        for (int i = 0; i < size0NoEmphasizeLineWidth; ++i) {
            singleLine += "-";
        }
        String doubleLine = "";
        for (int i = 0; i < size0NoEmphasizeLineWidth; ++i) {
            doubleLine += "=";
        }


        int logoTargetWidth = 300;
        byte[] d1 = convertBitmap(checkLogo(billObj,context), logoTargetWidth, 150);


        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(INIT);
            baos.write(POWER_ON);
            baos.write(NEW_LINE);
            baos.write(ALIGN_CENTER);

            for (int j = 0; j < d1.length / logoTargetWidth; ++j) {
                baos.write(hexToByteArray("1B2A00"));
                baos.write((byte) logoTargetWidth);
                baos.write((byte) (logoTargetWidth >> 8));
                byte[] temp = new byte[logoTargetWidth];
                System.arraycopy(d1, j * logoTargetWidth, temp, 0, temp.length);
                baos.write(temp);
                baos.write(NEW_LINE);
            }

            baos.write(NEW_LINE);
            baos.write(FONT_SIZE_0);
            baos.write(EMPHASIZE_ON);
            baos.write(CHAR_SPACING_0);
            baos.write(FONT_5X12);
            baos.write(SimplyPrintController.getUnicodeCommand(billObj.getTEN_DIEN_LUC()));
            baos.write(NEW_LINE);
            baos.write(SimplyPrintController.getUnicodeCommand("BIÊN NHẬN"));
            baos.write(NEW_LINE);
            baos.write(SimplyPrintController.getUnicodeCommand("THANH TOÁN TIỀN ĐIỆN"));
            baos.write(NEW_LINE);


            baos.write(ALIGN_LEFT);

            String textKH = "Tên KH: "+ billObj.getTEN_KHACH_HANG();
            writeLongText(baos, textKH, size0NoEmphasizeLineWidth);
            String textDiaChi = "Địa chỉ:"+ billObj.getDIA_CHI();
            writeLongText(baos, textDiaChi, size0NoEmphasizeLineWidth);
            String maKH = "Mã KH: "+billObj.getMA_KHACH_HANG();
            writeLongText(baos, maKH, size0NoEmphasizeLineWidth);
            String s1 = "Số Công tơ: "+ billObj.getSO_CONG_TO() + "      Số hộ: "+ billObj.getSO_HO();
            writeLongText(baos, s1, size0NoEmphasizeLineWidth);
            writeLongText(baos, "Seri HĐTT: "+ billObj.getBillId(), size0NoEmphasizeLineWidth);
            writeLongText(baos, "Hình thức thanh toán: Tiền mặt", size0NoEmphasizeLineWidth);
            String noiDung = "Nội dung: thanh toán hóa đơn tiền điện kỳ "+ Common.parse(billObj.getTHANG_THANH_TOAN(),Common.DATE_TIME_TYPE.MMyyyy.toString())+" từ ngày "+ billObj.getTU_NGAY()+" đến ngày " + billObj.getDEN_NGAY();
            writeLongText(baos, noiDung, size0NoEmphasizeLineWidth);
            baos.write(NEW_LINE);
            baos.write(SimplyPrintController.getUnicodeCommand("CSĐK: "+ billObj.getCSDK()));
            baos.write(NEW_LINE);
            baos.write(SimplyPrintController.getUnicodeCommand("CSCK: "+ billObj.getCSCK()));
            baos.write(NEW_LINE);
            baos.write(NEW_LINE);

            doubleLine = getSingleLine(lineWidth, TYPE_TEXT.UNICODE_3_COLLUMN);
            baos.write(doubleLine.getBytes());
            writeLongText(baos,"|ĐNTT | Đơn giá | Thành tiền|",size0NoEmphasizeText);
            baos.write(doubleLine.getBytes());
            String MCS[] = billObj.getCHI_TIET_MCS().split(";");
            String TIEN[] = billObj.getCHI_TIET_TIEN_MCS().split(";");
            String DG[] = billObj.getCHI_TIET_KG().split(";");
            ArrayList<ESCPOSSample.TienTheoChiSo> tienTheoChiSos = new ArrayList<>();
            for(int i=0; i<MCS.length; i++){
                ESCPOSSample.TienTheoChiSo tienTheoChiSo = new ESCPOSSample.TienTheoChiSo();
                tienTheoChiSo.setChiSo(MCS[i]);
                tienTheoChiSo.setDonGia(DG[i]);
                tienTheoChiSo.setTienTheoChiSo(TIEN[i]);
                tienTheoChiSos.add(tienTheoChiSo);

            }
            for (int i = 0; i < tienTheoChiSos.size();i++){
                String dntt = "|"+ESCPOSSample.demtext(6,tienTheoChiSos.get(i).getChiSo() + "")+ tienTheoChiSos.get(i).getChiSo() +"|"
                        + ESCPOSSample.demtext(10,tienTheoChiSos.get(i).getDonGia() + "")+ tienTheoChiSos.get(i).getDonGia() +"|" +
                        ESCPOSSample.demtext(12,tienTheoChiSos.get(i).getTienTheoChiSo() + "")+ tienTheoChiSos.get(i).getTienTheoChiSo() +"|";
                writeLongText(baos,dntt,size0NoEmphasizeText);
                singleLine = getSingleLine(lineWidth, TYPE_TEXT.UNICODE_3_COLLUMN);
                baos.write(singleLine.getBytes());
            }
            baos.write(NEW_LINE);
            String tiendiennang = "|"+  billObj.getDNTT() + "kwh" + ESCPOSSample.demtext(13,billObj.getDNTT() + "")+"|" +
                    ESCPOSSample.demtext(12,billObj.getTONG_TIEN_CHUA_THUE())+ billObj.getTONG_TIEN_CHUA_THUE() +"|";
            writeLongText(baos,tiendiennang,size0NoEmphasizeText);
            baos.write(singleLine.getBytes());
            String thue = "| Thue GTGT     |" + ESCPOSSample.demtext(12,billObj.getTONG_TIEN_THUE() + "")+ billObj.getTONG_TIEN_THUE() +"|";
            writeLongText(baos,thue,size0NoEmphasizeText);
            baos.write(singleLine.getBytes());
            String tongtien = "| Tong cong     |" + ESCPOSSample.demtext(12,billObj.getTIEN_THANH_TOAN()+"")+ billObj.getTIEN_THANH_TOAN() +"|";
            writeLongText(baos,tongtien,size0NoEmphasizeText);
            baos.write(singleLine.getBytes());
            baos.write(NEW_LINE);
            baos.write(NEW_LINE);

            String tienChu = "Bằng chữ: " + ReadNumber.numberToString((double) billObj.getTIEN_THANH_TOAN())+"";
            writeLongText(baos, tienChu, size0NoEmphasizeLineWidth);
            baos.write(NEW_LINE);
            baos.write(NEW_LINE);
            baos.write(NEW_LINE);
            baos.write(ALIGN_RIGHT);
            baos.write(SimplyPrintController.getUnicodeCommand("Hà nội, ngày " + ESCPOSSample.dateCurent()));
            baos.write(NEW_LINE);
            baos.write(SimplyPrintController.getUnicodeCommand("Người thu tiền"));
            baos.write(NEW_LINE);
            baos.write(SimplyPrintController.getUnicodeCommand("Nguyễn Bá Vinh"));
            baos.write(NEW_LINE);
            baos.write(SimplyPrintController.getUnicodeCommand("01214500702"));
            baos.write(NEW_LINE);
            baos.write(ALIGN_CENTER);
            String loiChao = "Khách hàng vui lòng giữ lại biên nhận sau khi thanh toán. Xin cảm ơn";
            writeLongText(baos, loiChao, size0NoEmphasizeLineWidth);
            baos.write(NEW_LINE);
            baos.write(NEW_LINE);
            baos.write(ALIGN_RIGHT);
            baos.write(SimplyPrintController.getUnicodeCommand("-- /Lần in 1/ --"));
            baos.write(NEW_LINE);
            baos.write(NEW_LINE);
            baos.write(NEW_LINE);
            baos.write(NEW_LINE);
            baos.write(POWER_OFF);

            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public static byte[] BiennhanHcmc(Context context, PayAdapter.BillEntityAdapter billObj) {
        int lineWidth = 384;
        int size0NoEmphasizeLineWidth = 384 / 8; //line width / font width
        int size0NoEmphasizeText = 43; //line width / font width
        String singleLine = "";
        for (int i = 0; i < size0NoEmphasizeLineWidth; ++i) {
            singleLine += "-";
        }
        int logoTargetWidth = 300;
        byte[] d1 = convertBitmap(checkLogo(billObj,context), logoTargetWidth, 150);


        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(INIT);
            baos.write(POWER_ON);
            baos.write(NEW_LINE);
            baos.write(ALIGN_CENTER);

            for (int j = 0; j < d1.length / logoTargetWidth; ++j) {
                baos.write(hexToByteArray("1B2A00"));
                baos.write((byte) logoTargetWidth);
                baos.write((byte) (logoTargetWidth >> 8));
                byte[] temp = new byte[logoTargetWidth];
                System.arraycopy(d1, j * logoTargetWidth, temp, 0, temp.length);
                baos.write(temp);
                baos.write(NEW_LINE);
            }

            baos.write(NEW_LINE);
            baos.write(FONT_SIZE_0);
            baos.write(EMPHASIZE_ON);
            baos.write(CHAR_SPACING_0);
            baos.write(FONT_5X12);
            baos.write(SimplyPrintController.getUnicodeCommand(billObj.getTEN_DIEN_LUC()));
            baos.write(NEW_LINE);
            baos.write(SimplyPrintController.getUnicodeCommand("BIÊN NHẬN"));
            baos.write(NEW_LINE);
            baos.write(SimplyPrintController.getUnicodeCommand("THANH TOÁN TIỀN ĐIỆN"));
            baos.write(NEW_LINE);
            baos.write(ALIGN_LEFT);

            String textKH = "Tên KH: "+ billObj.getTEN_KHACH_HANG();
            writeLongText(baos, textKH, size0NoEmphasizeLineWidth);
            String textDiaChi = "Địa chỉ:"+ billObj.getDIA_CHI();
            writeLongText(baos, textDiaChi, size0NoEmphasizeLineWidth);
            String maKH = "Mã KH: "+billObj.getMA_KHACH_HANG();
            writeLongText(baos, maKH, size0NoEmphasizeLineWidth);
            writeLongText(baos, "Sổ GCS " + billObj.getSO_GCS(), size0NoEmphasizeLineWidth);
            if (Character.toString(billObj.getMA_DIEN_LUC().charAt(1)).equals("E")){
                String s1 = "Số Công tơ: "+ billObj.getSO_CONG_TO() + "     Số hộ: "+ billObj.getSO_HO();
                writeLongText(baos, s1, size0NoEmphasizeLineWidth);
            }

            writeLongText(baos, "Hình thức thanh toán: Tiền mặt", size0NoEmphasizeLineWidth);
            String noiDung = "Nội dung: thanh toán hóa đơn tiền điện kỳ "+ Common.parse(billObj.getTHANG_THANH_TOAN(),Common.DATE_TIME_TYPE.MMyyyy.toString())+" từ ngày "+ billObj.getTU_NGAY()+" đến ngày " + billObj.getDEN_NGAY();
            writeLongText(baos, noiDung, size0NoEmphasizeLineWidth);
            baos.write(NEW_LINE);
            baos.write(NEW_LINE);
            baos.write(singleLine.getBytes());
            String tiendiennang = "|"+  billObj.getDNTT() + "kwh" + ESCPOSSample.demtext(13,billObj.getDNTT() + "")+"|" +
                    ESCPOSSample.demtext(12,billObj.getTONG_TIEN_CHUA_THUE())+ billObj.getTONG_TIEN_CHUA_THUE() +"|";
            writeLongText(baos,tiendiennang,size0NoEmphasizeText);
            baos.write(singleLine.getBytes());
            String thanhtien = "| Thành tiền    |" + ESCPOSSample.demtext(12,billObj.getTONG_TIEN_THUE() + "")+ billObj.getTONG_TIEN_THUE() +"|";
            writeLongText(baos,thanhtien,size0NoEmphasizeText);
            String thue = "| Thue GTGT     |" + ESCPOSSample.demtext(12,billObj.getTONG_TIEN_THUE() + "")+ billObj.getTONG_TIEN_THUE() +"|";
            writeLongText(baos,thue,size0NoEmphasizeText);
            baos.write(singleLine.getBytes());
            String tongtien = "| Tong cong     |" + ESCPOSSample.demtext(12,billObj.getTIEN_THANH_TOAN()+"")+ billObj.getTIEN_THANH_TOAN() +"|";
            writeLongText(baos,tongtien,size0NoEmphasizeText);
            baos.write(singleLine.getBytes());
            baos.write(NEW_LINE);
            baos.write(NEW_LINE);

            String tienChu = "Bằng chữ: " + ReadNumber.numberToString((double) billObj.getTIEN_THANH_TOAN())+"";
            writeLongText(baos, tienChu, size0NoEmphasizeLineWidth);
            baos.write(NEW_LINE);
            baos.write(NEW_LINE);
            baos.write(ALIGN_RIGHT);
            baos.write(SimplyPrintController.getUnicodeCommand("ngày " + ESCPOSSample.dateCurent()));
            baos.write(NEW_LINE);
            baos.write(SimplyPrintController.getUnicodeCommand("Người thu tiền"));
            baos.write(NEW_LINE);
            baos.write(SimplyPrintController.getUnicodeCommand("Nguyễn Bá Vinh"));
            baos.write(NEW_LINE);
            baos.write(SimplyPrintController.getUnicodeCommand("01214500702"));
            baos.write(NEW_LINE);
            baos.write(ALIGN_CENTER);
            writeLongText(baos, "Khach hang vui long giu lai bien nhan sau khi thanh toan.", size0NoEmphasizeLineWidth);
            writeLongText(baos, "Xin cam on!", size0NoEmphasizeLineWidth);
            writeLongText(baos, "ECPAY kinh moi quy khach hang vui long: ", size0NoEmphasizeLineWidth);
            String webCSKH = null;
            if (Character.toString(billObj.getMA_DIEN_LUC().charAt(1)).equals("E")){
                webCSKH = "http://cskh.hcmpc.com.vn";
            }else if (Character.toString(billObj.getMA_DIEN_LUC().charAt(1)).equals("B")){
                webCSKH = "http://cskh.evnspc.com.vn";
            }
            String cskh = "+ Truy cap trang web " + webCSKH  + " de nhan hoa don dien tu ";
            writeLongText(baos, cskh, size0NoEmphasizeLineWidth);
            writeLongText(baos, "+ Kiem tra cac thong tin phia tren, neu co van de can giai dap lien he Trung tam cham soc khach hang 1900561230*", size0NoEmphasizeLineWidth);
            writeLongText(baos, "ĐT sua chua: 0976208447", size0NoEmphasizeLineWidth);
            baos.write(NEW_LINE);
            baos.write(NEW_LINE);
            baos.write(ALIGN_RIGHT);
            baos.write(SimplyPrintController.getUnicodeCommand("-- /Lần in 1/ --"));
            baos.write(NEW_LINE);
            baos.write(NEW_LINE);
            baos.write(NEW_LINE);
            baos.write(NEW_LINE);
            baos.write(POWER_OFF);

            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    static byte[] baoCao(Context context, Account account, int hdGiao, long tienGiao, int hdThu, long tienThu, int hdVangLai, long tienVangLai, int hdTraKH, long tienTraKHt) throws InterruptedException {
        int size0NoEmphasizeLineWidth = 384 / 8; //line width / font width
        String singleLine = "";
        for (int i = 0; i < size0NoEmphasizeLineWidth; ++i) {
            singleLine += "-";
        }
        int logoTargetWidth = 300;
        Bitmap logoBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_evn_npc);
        byte[] d1 = convertBitmap(logoBitmap, logoTargetWidth, 150);

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(INIT);
            baos.write(POWER_ON);
            baos.write(NEW_LINE);
            baos.write(ALIGN_CENTER);

            for (int j = 0; j < d1.length / logoTargetWidth; ++j) {
                baos.write(hexToByteArray("1B2A00"));
                baos.write((byte) logoTargetWidth);
                baos.write((byte) (logoTargetWidth >> 8));
                byte[] temp = new byte[logoTargetWidth];
                System.arraycopy(d1, j * logoTargetWidth, temp, 0, temp.length);
                baos.write(temp);
                baos.write(NEW_LINE);
            }
            baos.write(NEW_LINE);
            baos.write(FONT_SIZE_0);
            baos.write(EMPHASIZE_ON);
            baos.write(CHAR_SPACING_0);
            baos.write(FONT_5X12);
            writeLongText(baos, "TỔNG HỢP GIAO THU", size0NoEmphasizeLineWidth);
            baos.write(NEW_LINE);
            baos.write(NEW_LINE);
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            writeLongText(baos,"Ngay in: "+ dateFormat.format(new Date()), size0NoEmphasizeLineWidth);
            baos.write(NEW_LINE);
            writeLongText(baos,"Thu ngan vien: "+ account.getName(), size0NoEmphasizeLineWidth);
            baos.write(NEW_LINE);
            baos.write(singleLine.getBytes());
            writeLongText(baos,"|        | So HD |   Tong Tien |", size0NoEmphasizeLineWidth);
            baos.write(singleLine.getBytes());
            String giao = "|  "+ "Giao" + ESCPOSSample.demtext(7,"Giao")+"|" + ESCPOSSample.demtext(8,hdThu+"")+ hdThu +"|" + ESCPOSSample.demtext(14,tienGiao+"")+ tienGiao +"|";
            String Thu = "|  "+ "Thu" + ESCPOSSample.demtext(7,"Thu")+"|" + ESCPOSSample.demtext(8,hdGiao+"")+ hdGiao +"|" + ESCPOSSample.demtext(14,tienThu+"")+ tienThu +"|";
            String ton = "|  "+ "Tồn" + ESCPOSSample.demtext(7,"Tồn")+"|" + ESCPOSSample.demtext(8,String.valueOf(hdGiao - (hdThu - hdVangLai))+"")+ String.valueOf(hdGiao - (hdThu - hdVangLai)) +
                    "|" + ESCPOSSample.demtext(14,Common.convertLongToMoney(tienGiao - (tienThu - tienVangLai))+"")+ Common.convertLongToMoney(tienGiao - (tienThu - tienVangLai)) +"|";
            writeLongText(baos,giao, size0NoEmphasizeLineWidth);
            baos.write(singleLine.getBytes());
            writeLongText(baos,Thu, size0NoEmphasizeLineWidth);
            baos.write(singleLine.getBytes());
            writeLongText(baos,ton, size0NoEmphasizeLineWidth);
            baos.write(singleLine.getBytes());
            baos.write(NEW_LINE);
            writeLongText(baos,"Trong do: ", size0NoEmphasizeLineWidth);
            baos.write(NEW_LINE);
            String vanglai = "Vãng lai" + ESCPOSSample.demtext(11,"Vang lai")+"|" + ESCPOSSample.demtext(8,hdVangLai +"")+ hdVangLai +"|" + ESCPOSSample.demtext(13,tienVangLai+"")+ tienVangLai +"|";
            String hoantra = "Hoàn trả" + ESCPOSSample.demtext(11,"Hoan tra")+"|" + ESCPOSSample.demtext(8,hdTraKH +"")+ hdTraKH +"|" + ESCPOSSample.demtext(13,tienTraKHt+"")+ tienTraKHt +"|";
            writeLongText(baos,vanglai, size0NoEmphasizeLineWidth);
            baos.write(NEW_LINE);
            writeLongText(baos,hoantra, size0NoEmphasizeLineWidth);
            baos.write(NEW_LINE);
            baos.write(NEW_LINE);
            baos.write(NEW_LINE);
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    static byte[] thongbao(Context context, PayAdapter.BillEntityAdapter billObj){
        int lineWidth = 384;
        int size0NoEmphasizeLineWidth = 384 / 8; //line width / font width
        int size0NoEmphasizeText = 43; //line width / font width
        String singleLine = "";
        for (int i = 0; i < size0NoEmphasizeLineWidth; ++i) {
            singleLine += "-";
        }


        int logoTargetWidth = 300;
        byte[] d1 = convertBitmap(checkLogo(billObj,context), logoTargetWidth, 150);
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(INIT);
            baos.write(POWER_ON);
            baos.write(NEW_LINE);
            baos.write(ALIGN_CENTER);
            writeLongText(baos, "Tong cong ty dien luc", size0NoEmphasizeLineWidth);

            for (int j = 0; j < d1.length / logoTargetWidth; ++j) {
                baos.write(hexToByteArray("1B2A00"));
                baos.write((byte) logoTargetWidth);
                baos.write((byte) (logoTargetWidth >> 8));
                byte[] temp = new byte[logoTargetWidth];
                System.arraycopy(d1, j * logoTargetWidth, temp, 0, temp.length);
                baos.write(temp);
                baos.write(NEW_LINE);
            }
            baos.write(NEW_LINE);
            baos.write(FONT_SIZE_0);
            baos.write(EMPHASIZE_ON);
            baos.write(CHAR_SPACING_0);
            baos.write(FONT_5X12);
            baos.write(SimplyPrintController.getUnicodeCommand(billObj.getTEN_DIEN_LUC()));
            baos.write(NEW_LINE);
            baos.write(SimplyPrintController.getUnicodeCommand("THÔNG BÁO TIỀN ĐIỆN"));
            baos.write(SimplyPrintController.getUnicodeCommand("(Không có giá trị thanh toán)"));
            writeLongText(baos, "Ky hoa don: "+ Common.parse(billObj.getTHANG_THANH_TOAN(), Common.DATE_TIME_TYPE.MMyyyy.toString()), size0NoEmphasizeLineWidth);

            baos.write(NEW_LINE);
            if (Character.toString(billObj.getMA_DIEN_LUC().charAt(1)).equals("E")
                    ||Character.toString(billObj.getMA_DIEN_LUC().charAt(1)).equals("C")){
                writeLongText(baos,"Tu: "+billObj.getTU_NGAY() +" den: "+billObj.getDEN_NGAY(), size0NoEmphasizeLineWidth);
            }

            baos.write(ALIGN_LEFT);

            String textKH = "Tên KH: "+ billObj.getTEN_KHACH_HANG();
            writeLongText(baos, textKH, size0NoEmphasizeLineWidth);
            String textDiaChi = "Địa chỉ:"+ billObj.getDIA_CHI();
            writeLongText(baos, textDiaChi, size0NoEmphasizeLineWidth);
            String maKH = "Mã KH: "+billObj.getMA_KHACH_HANG();
            writeLongText(baos, maKH, size0NoEmphasizeLineWidth);
            String s1 = "Số Công tơ: "+ billObj.getSO_CONG_TO() + "      Số hộ: "+ billObj.getSO_HO();
            writeLongText(baos, s1, size0NoEmphasizeLineWidth);
            if (Character.toString(billObj.getMA_DIEN_LUC().charAt(1)).equals("B")){
                writeLongText(baos,"So GCS: "+ billObj.getSO_GCS(), size0NoEmphasizeLineWidth);
            }
            writeLongText(baos,"ID hoa don: "+ billObj.getMA_HOA_DON(), size0NoEmphasizeLineWidth);
            if (Character.toString(billObj.getMA_DIEN_LUC().charAt(1)).equals("E")
                    ||Character.toString(billObj.getMA_DIEN_LUC().charAt(2)).equals("C")){
                writeLongText(baos,"CSDK: "+billObj.getCSDK(), size0NoEmphasizeLineWidth);
                writeLongText(baos,"CSCK: "+billObj.getCSCK(), size0NoEmphasizeLineWidth);
            }
            baos.write(singleLine.getBytes());
            writeLongText(baos,"|ĐNTT | Đơn giá | Thành tiền|",size0NoEmphasizeText);
            baos.write(singleLine.getBytes());
            if (billObj.getCHI_TIET_MCS().equals("") || billObj.getCHI_TIET_MCS() == null){
                ArrayList<ESCPOSSample.TienTheoChiSo> tienTheoChiSos = new ArrayList<>();
                String MCS[] = billObj.getCHI_TIET_MCS().split(";");
                String TIEN[] = billObj.getCHI_TIET_TIEN_MCS().split(";");
                String DG[] = billObj.getCHI_TIET_KG().split(";");
                for(int i=0; i<MCS.length; i++){
                    ESCPOSSample.TienTheoChiSo tienTheoChiSo = new ESCPOSSample.TienTheoChiSo();
                    tienTheoChiSo.setChiSo(MCS[i]);
                    tienTheoChiSo.setDonGia(DG[i]);
                    tienTheoChiSo.setTienTheoChiSo(TIEN[i]);
                    tienTheoChiSos.add(tienTheoChiSo);
                }
                for (int i = 0; i < tienTheoChiSos.size();i++){
                    String dntt = "|"+ESCPOSSample.demtext(6,tienTheoChiSos.get(i).getChiSo() + "")+ tienTheoChiSos.get(i).getChiSo() +"|"
                            + ESCPOSSample.demtext(10,tienTheoChiSos.get(i).getDonGia() + "")+ tienTheoChiSos.get(i).getDonGia() +"|" +
                            ESCPOSSample.demtext(12,tienTheoChiSos.get(i).getTienTheoChiSo() + "")+ tienTheoChiSos.get(i).getTienTheoChiSo() +"|";
                    writeLongText(baos,dntt,size0NoEmphasizeText);
                    singleLine = getSingleLine(lineWidth, TYPE_TEXT.UNICODE_3_COLLUMN);
                    baos.write(singleLine.getBytes());
                }
            }

            baos.write(NEW_LINE);
            String tiendiennang = "|"+  billObj.getDNTT() + "kwh" + ESCPOSSample.demtext(13,billObj.getDNTT() + "")+"|" +
                    ESCPOSSample.demtext(12,billObj.getTONG_TIEN_CHUA_THUE())+ billObj.getTONG_TIEN_CHUA_THUE() +"|";
            writeLongText(baos,tiendiennang,size0NoEmphasizeText);
            baos.write(singleLine.getBytes());
            String thue = "| Thue GTGT     |" + ESCPOSSample.demtext(12,billObj.getTONG_TIEN_THUE() + "")+ billObj.getTONG_TIEN_THUE() +"|";
            writeLongText(baos,thue,size0NoEmphasizeText);
            baos.write(singleLine.getBytes());
            String tongtien = "| Tong cong     |" + ESCPOSSample.demtext(12,billObj.getTIEN_THANH_TOAN()+"")+ billObj.getTIEN_THANH_TOAN() +"|";
            writeLongText(baos,tongtien,size0NoEmphasizeText);
            baos.write(singleLine.getBytes());
            baos.write(NEW_LINE);
            baos.write(NEW_LINE);
            baos.write(ALIGN_RIGHT);
            baos.write(SimplyPrintController.getUnicodeCommand("ngày thông báo " + ESCPOSSample.dateCurent()));
            baos.write(NEW_LINE);
            baos.write(NEW_LINE);
            baos.write(SimplyPrintController.getUnicodeCommand("Người thu tiền"));
            baos.write(NEW_LINE);
            baos.write(SimplyPrintController.getUnicodeCommand("Nguyễn Bá Vinh"));
            baos.write(NEW_LINE);
            baos.write(SimplyPrintController.getUnicodeCommand("01214500702"));
            baos.write(NEW_LINE);
            baos.write(ALIGN_CENTER);
            String loiChao = "De nghi thanh toan truoc ngay: 20/07/2017.";
            writeLongText(baos, loiChao, size0NoEmphasizeLineWidth);
            baos.write(NEW_LINE);
            baos.write(NEW_LINE);
            baos.write(ALIGN_RIGHT);
            baos.write(SimplyPrintController.getUnicodeCommand("-- /Lần in 1/ --"));
            baos.write(NEW_LINE);
            baos.write(NEW_LINE);
            baos.write(NEW_LINE);
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getSingleLine(int lineWidth, TYPE_TEXT type) {
        String result = "";

        int numOfCharacterPerLine = (int) lineWidth / type.getFontSize();
        if (numOfCharacterPerLine % 2 != 0)
            numOfCharacterPerLine++;

        for (int index = 0; index < numOfCharacterPerLine; index++) {
            result += "-";
        }
        return result;
    }

    private static void writeLongText(final ByteArrayOutputStream baos, String text, int size0NoEmphasizeLineWidth) throws Exception {
        try {
            int count = 0;
            String noiDungInDong = "";
            baos.write(NEW_LINE);
            do {
                if (text.length() - count > size0NoEmphasizeLineWidth) {
                    noiDungInDong = text.substring(count, count + size0NoEmphasizeLineWidth);
                    baos.write(SimplyPrintController.getUnicodeCommand(noiDungInDong));
                    count = count + size0NoEmphasizeLineWidth;
                } else {
                    noiDungInDong = text.substring(count, text.length());
                    baos.write(ALIGN_LEFT);
                    baos.write(SimplyPrintController.getUnicodeCommand(noiDungInDong));
                    count = text.length();
                    baos.write(NEW_LINE);
                }

            } while (count != text.length());

        } catch (IOException e) {
            throw e;
        }

    }
    private static Bitmap checkLogo(PayAdapter.BillEntityAdapter bill, Context context){
        Bitmap logoBitmap = null;
        if (Character.toString(bill.getMA_DIEN_LUC().charAt(1)).equals("H")||Character.toString(bill.getMA_DIEN_LUC().charAt(1)).equals("A")){
            logoBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_evn_npc);
        }else if (Character.toString(bill.getMA_DIEN_LUC().charAt(1)).equals("C")){
            logoBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_evn_cpc);
        }else if (Character.toString(bill.getMA_DIEN_LUC().charAt(1)).equals("B")){
            logoBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_evn_spc);
        }else if (Character.toString(bill.getMA_DIEN_LUC().charAt(1)).equals("D")){
            logoBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_evn_hanoi);
        }else if (Character.toString(bill.getMA_DIEN_LUC().charAt(1)).equals("E")){
            logoBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_evn_hcmc);
        }else {
            logoBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.logoecpay2);
        }
        return logoBitmap;
    }

}

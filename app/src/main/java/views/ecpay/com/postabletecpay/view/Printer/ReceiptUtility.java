package views.ecpay.com.postabletecpay.view.Printer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.bbpos.simplyprint.SimplyPrintController;
import com.sewoo.jpos.printer.LKPrint;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import views.ecpay.com.postabletecpay.R;
import views.ecpay.com.postabletecpay.model.adapter.PayAdapter;
import views.ecpay.com.postabletecpay.util.commons.Common;

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
        UNICODE_3_COLLUMN(14),
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


            /**
             * Loại 2 dòng unicode
             */
            String s1 = "Số Công tơ: "+ billObj.getSO_CONG_TO();
            String s2 = "Số hộ: "+ billObj.getSO_HO();

            List<String> mutilText_2 = new ArrayList<>();
            mutilText_2.add(s1);
            mutilText_2.add(s2);
            int padding = 4;
            writeLongTextMultiCollum(baos, lineWidth, padding, TYPE_TEXT.UNICODE_2_COLLUMN, mutilText_2);

            /**
             * Loại 3 dòng unicode
             */

            baos.write(NEW_LINE);
            baos.write(NEW_LINE);
            writeLongText(baos, "Seri HĐTT: "+ billObj.getBillId(), size0NoEmphasizeLineWidth);
            baos.write(NEW_LINE);
            writeLongText(baos, "Hình thức thanh toán: Tiền mặt", size0NoEmphasizeLineWidth);
            baos.write(NEW_LINE);
            String noiDung = "Nội dung: thanh toán hóa đơn tiền điện kỳ "+ Common.parse(billObj.getTHANG_THANH_TOAN(),Common.DATE_TIME_TYPE.MMyyyy.toString())+" từ ngày "+ billObj.getTU_NGAY()+" đến ngày " + billObj.getDEN_NGAY();
            writeLongText(baos, noiDung, size0NoEmphasizeLineWidth);
            baos.write(NEW_LINE);
            baos.write(SimplyPrintController.getUnicodeCommand("CSĐK: "+ billObj.getCSDK()));
            baos.write(NEW_LINE);
            baos.write(SimplyPrintController.getUnicodeCommand("CSCK: "+ billObj.getCSCK()));
            baos.write(NEW_LINE);
            baos.write(NEW_LINE);

            doubleLine = getDoubleLine(lineWidth, TYPE_TEXT.UNICODE_3_COLLUMN);
            baos.write(doubleLine.getBytes());

            List<String> mutilText_3 = new ArrayList<>();
            mutilText_3.add("ĐNTT");
            mutilText_3.add("Đơn giá");
            mutilText_3.add("Thành tiền");
            padding = 2;
            writeLongTextMultiCollum(baos, lineWidth, padding, TYPE_TEXT.UNICODE_3_COLLUMN, mutilText_3);
            baos.write(NEW_LINE);
            baos.write(doubleLine.getBytes());
            baos.write(NEW_LINE);
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
                mutilText_3.clear();
                mutilText_3.add(tienTheoChiSos.get(i).getChiSo());
                mutilText_3.add(tienTheoChiSos.get(i).getDonGia());
                mutilText_3.add(tienTheoChiSos.get(i).getTienTheoChiSo());
                padding = 2;
                writeLongTextMultiCollum(baos, lineWidth, padding, TYPE_TEXT.UNICODE_3_COLLUMN, mutilText_3);
                baos.write(NEW_LINE);
            }

            baos.write(singleLine.getBytes());
            baos.write(NEW_LINE);

            mutilText_2.clear();
            mutilText_2.add(billObj.getDNTT() + " KWh");
            mutilText_2.add(billObj.getTONG_TIEN_CHUA_THUE());
            padding = 4;
            writeLongTextMultiCollum(baos, lineWidth, padding, TYPE_TEXT.UNICODE_2_COLLUMN, mutilText_2);
            baos.write(NEW_LINE);

            mutilText_2.clear();
            mutilText_2.add("Thuế GTGT");
            mutilText_2.add(billObj.getTONG_TIEN_THUE());
            padding = 4;
            writeLongTextMultiCollum(baos, lineWidth, padding, TYPE_TEXT.UNICODE_2_COLLUMN, mutilText_2);
            baos.write(NEW_LINE);

            mutilText_2.clear();
            mutilText_2.add("Tổng tiền");
            mutilText_2.add(billObj.getTIEN_THANH_TOAN() + "");
            padding = 4;
            writeLongTextMultiCollum(baos, lineWidth, padding, TYPE_TEXT.UNICODE_2_COLLUMN, mutilText_2);
            baos.write(NEW_LINE);

            String tienChu = "Bằng chữ: " + Common.unAccent(billObj.getTIEN_THANH_TOAN()+"");
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
            baos.write(SimplyPrintController.getUnicodeCommand("-- /Lần in 1 --"));

            baos.write(POWER_OFF);

            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String getDoubleLine(int lineWidth, TYPE_TEXT type) {
        String result = "";

        int numOfCharacterPerLine = (int) lineWidth / type.getFontSize();
        if (numOfCharacterPerLine % 2 != 0)
            numOfCharacterPerLine++;

        for (int index = 0; index < numOfCharacterPerLine; index++) {
            result += "=";
        }
        return result;
    }

    private static void writeLongTextMultiCollum(final ByteArrayOutputStream baos, int lineWidth, int padding, TYPE_TEXT type, final List<String> mutilText) throws Exception {
        try {
            int collumn = mutilText.size();

            int numOfCharacterPerLine = (int) lineWidth / type.getFontSize();

            if (numOfCharacterPerLine % 2 != 0)
                numOfCharacterPerLine++;
            if (padding % 2 != 0)
                padding++;
            int halfPadding = padding / 2;
            int numMaxOfEveryText = (int) numOfCharacterPerLine / collumn;


            int soCotHetKyTuText = 0;

            do {
                String noiDungInDong = "";
                for (int cot = 0; cot < collumn; cot++) {
                    Boolean cotTextHetKiTu = false;
                    //thêm kí tự trắng nếu in ko đủ cột này
                    if (mutilText.get(cot).length() <= numMaxOfEveryText - halfPadding) {
                        noiDungInDong += mutilText.get(cot);
                        for (int viTrikyTuThem = 0; viTrikyTuThem < numMaxOfEveryText - mutilText.get(cot).length() - halfPadding; viTrikyTuThem++) {
                            noiDungInDong += " ";
                        }

                        //tạo 1 cờ tránh việc tăng soCotHetKyTuText trường hợp cả một dòng của cột text này toàn kí tự trắng
                        if (numMaxOfEveryText - mutilText.get(cot).length() == numMaxOfEveryText)
                            cotTextHetKiTu = true;

                        //xóa bớt kí tự ban đầu
                        mutilText.set(cot, "");
                    } else {
                        noiDungInDong += mutilText.get(cot).substring(0, numMaxOfEveryText - halfPadding);
                        //xóa bớt kí tự ban đầu
                        String sS = mutilText.get(cot).substring(numMaxOfEveryText - halfPadding, mutilText.get(cot).length());
                        mutilText.set(cot, sS);
                    }

                    if (cot < collumn - 1) {
                        //thêm kí tự trắng padding
                        for (int viTrikyTuThem = 0; viTrikyTuThem < padding; viTrikyTuThem++) {
                            noiDungInDong += " ";
                        }
                    }

                    //nếu cột text đó đã hết hoặc không phải trường hợp in toàn kí tự trắng cột đó
                    if (cotTextHetKiTu) {

                    } else if (mutilText.get(cot).length() == 0) {
                        soCotHetKyTuText++;
                    }
                }

                //Khi có kí tự thì in
                if (noiDungInDong.length() > numOfCharacterPerLine) {
                    Log.d(TAG, "writeLongTextMultiCollum: ");
                }

                baos.write(ALIGN_LEFT);
                baos.write(SimplyPrintController.getUnicodeCommand(noiDungInDong));
                baos.write(NEW_LINE);

            }
            while (soCotHetKyTuText != collumn);

        } catch (Exception e) {
            throw e;
        }

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

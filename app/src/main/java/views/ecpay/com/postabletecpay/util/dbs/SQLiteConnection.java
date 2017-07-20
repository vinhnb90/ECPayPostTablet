package views.ecpay.com.postabletecpay.util.dbs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDiskIOException;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import views.ecpay.com.postabletecpay.model.ReportModel;
import views.ecpay.com.postabletecpay.model.adapter.PayAdapter;
import views.ecpay.com.postabletecpay.model.adapter.ReportLichSuThanhToanAdapter;
import views.ecpay.com.postabletecpay.presenter.PayPresenter;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.util.entities.EntityDanhSachThu;
import views.ecpay.com.postabletecpay.util.entities.EntityHoaDonNo;
import views.ecpay.com.postabletecpay.util.entities.EntityHoaDonThu;
import views.ecpay.com.postabletecpay.util.entities.EntityKhachHang;
import views.ecpay.com.postabletecpay.util.entities.EntityLichSuThanhToan;
import views.ecpay.com.postabletecpay.util.entities.request.EntityPostBill.TransactionOffItem;
import views.ecpay.com.postabletecpay.util.entities.response.EntityBill.BillResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityCustomer.CustomerResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityEVN.ListBookCmisResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityEVN.ListEvnPCResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityFileGen.BodyBillResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityFileGen.BodyCustomerResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityFileGen.FooterCustomerResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityFileGen.ListBillResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityFileGen.ListCustomerResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntitySearchOnline.BillInsideCustomer;
import views.ecpay.com.postabletecpay.util.entities.sqlite.Account;
import views.ecpay.com.postabletecpay.util.entities.sqlite.Bill;
import views.ecpay.com.postabletecpay.util.entities.sqlite.Customer;
import views.ecpay.com.postabletecpay.util.entities.sqlite.EvnPC;
import views.ecpay.com.postabletecpay.view.Main.MainActivity;
import views.ecpay.com.postabletecpay.view.ThanhToan.PayFragment;

import static android.content.ContentValues.TAG;
import static views.ecpay.com.postabletecpay.util.commons.Common.DATE_TIME_TYPE.FULL;
import static views.ecpay.com.postabletecpay.util.commons.Common.DATE_TIME_TYPE.yyyyMMddHHmmssSSS;
import static views.ecpay.com.postabletecpay.util.commons.Common.ONE;
import static views.ecpay.com.postabletecpay.util.commons.Common.PATH_FOLDER_CONFIG;
import static views.ecpay.com.postabletecpay.util.commons.Common.PATH_FOLDER_DB;
import static views.ecpay.com.postabletecpay.util.commons.Common.PATH_FOLDER_ROOT;
import static views.ecpay.com.postabletecpay.util.commons.Common.ZERO;
import static views.ecpay.com.postabletecpay.util.commons.Common.intConvertNull;
import static views.ecpay.com.postabletecpay.util.commons.Common.longConvertNull;
import static views.ecpay.com.postabletecpay.util.commons.Common.stringConvertNull;

/**
 * Created by TungNV on 5/5/17.
 */

public class SQLiteConnection extends SQLiteOpenHelper {
    private static SQLiteConnection instance;
    private SQLiteDatabase database;

    private static String databaseName = "PosTablet.s3db";
    private static int DATABASE_VERSION = 1;
    public static int ERROR_OCCUR = -1;

    private String TABLE_NAME_ACCOUNT = "TBL_ACCOUNT";
    private String TABLE_NAME_EVN_PC = "TBL_EVN_PC";
    private String TABLE_NAME_BOOK_CMIS = "TBL_BOOK_CMIS";
    private String TABLE_NAME_CUSTOMER = "TBL_CUSTOMER";
    private String TABLE_NAME_BILL = "TBL_BILL";
    private String TABLE_NAME_DEBT_COLLECTION = "TBL_DEBT_COLLECTION";
    private String TABLE_NAME_HISTORY_PAY = "TBL_LICH_SU_TTOAN";

    private String CREATE_TABLE_ACCOUNT = "CREATE TABLE `" + TABLE_NAME_ACCOUNT + "` (`edong` TEXT NOT NULL PRIMARY KEY, `name` TEXT, " +
            "`address` TEXT, `phone` TEXT, `email` TEXT, `birthday` TEXT, `session` TEXT, `balance` NUMERIC, `lockMoney` NUMERIC, " +
            "`changePIN` INTEGER, `verified` INTEGER, `mac` TEXT, `ip` TEXT, `strLoginTime` TEXT, `strLogoutTime` TEXT, `type` INTEGER, " +
            "`status` TEXT, `idNumber` TEXT, `idNumberDate` TEXT, `idNumberPlace` TEXT, `parentEdong` TEXT, `notYetPushMoney` INTEGER DEFAULT 0)";


    private String CREATE_TABLE_EVN_PC = "CREATE TABLE " + TABLE_NAME_EVN_PC + " ( pcId INTEGER NOT NULL PRIMARY KEY, edong TEXT NOT NULL, strPcId TEXT, parentId INTEGER, " +
            "strParentId TEXT, " +
            "code TEXT, ext TEXT, fullName TEXT, shortName TEXT, address TEXT, taxCode TEXT, phone1 TEXT, phone2 TEXT, " +
            "fax TEXT, level INTEGER, " +
            "strLevel TEXT, mailTo TEXT, mailCc TEXT, status INTEGER, strStatus , dateCreated DATE, strDateCreated TEXT, " +
            "idChanged INTEGER, dateChanged DATE, strDateChanged TEXT, regionId INTEGER, parentPcCode TEXT, cardPrefix TEXT)";

    private String CREATE_TABLE_BOOK_CMIS = "CREATE TABLE " + TABLE_NAME_BOOK_CMIS + "(bookCmis TEXT, pcCode TEXT, pcCodeExt TEXT, inningDate TEXT, email TEXT, status INTEGER, strStatus TEXT, strCreateDate TEXT, strChangeDate TEXT, idChanged INTEGER, id INTEGER, parentPcCode TEXT, countBill INTEGER, countBillPaid INTEGER, countCustomer INTEGER, listCustomer TEXT, listBillUnpaid TEXT, listBillPaid TEXT)";

    private String CREATE_TABLE_CUSTOMER = "CREATE TABLE `" + TABLE_NAME_CUSTOMER + "` ( `ID` INTEGER PRIMARY KEY AUTOINCREMENT, `MA_KHANG` TEXT," +
            " `MA_THE` TEXT, `E_DONG` TEXT, `TEN_KHANG` TEXT, " +
            "`DIA_CHI` TEXT, `PHIEN_TTOAN` TEXT, `LO_TRINH` TEXT,`SO_GCS` TEXT, `DIEN_LUC` TEXT, `SO_HO` TEXT, `SDT_ECPAY` TEXT, " +
            "`SDT_EVN` TEXT, `GIAO_THU` TEXT, `NGAY_GIAO_THU` DATE)";

    //add new field requestDate: date bill paying online success from tablet
    /* + Em Phuong
        - Chi tiết khung giá (CHI_TIET_KG)
        - Chi tiết từng mức chỉ số (CHI_TIET_MCS)
        - Chi tiết số tiền từng mức chỉ sô (CHI_TIET_TIEN_MCS)
        - Số điện năng tiêu thụ (DNTT)
        - Tổng tiền chưa thuế (TONG_TIEN_CHUA_THUE)
        - Tổng tiền thuế (TIEN_THUE)
 */
    private String CREATE_TABLE_BILL = "CREATE TABLE `" + TABLE_NAME_BILL + "` ( `ID` INTEGER PRIMARY KEY AUTOINCREMENT , `E_DONG` TEXT, `MA_HOA_DON` TEXT, `SERI_HDON` TEXT, `MA_KHANG` TEXT, " +
            "`MA_THE` TEXT, `TEN_KHANG` TEXT, `DIA_CHI` TEXT, `THANG_TTOAN` DATE, `PHIEN_TTOAN` TEXT, `SO_TIEN_TTOAN` INTEGER, " +
            "`SO_GCS` TEXT, `DIEN_LUC` TEXT, `SO_HO` TEXT, `SO_DAU_KY` TEXT, `SO_CUOI_KY` TEXT, `SO_CTO` TEXT, `SDT_ECPAY` TEXT, " +
            "`SDT_EVN` TEXT, `GIAO_THU` TEXT, `NGAY_GIAO_THU` DATE, `TRANG_THAI_TTOAN` TEXT, `VI_TTOAN` TEXT, `CHI_TIET_KG` TEXT, `CHI_TIET_MCS` TEXT, `CHI_TIET_TIEN_MCS` TEXT, `DNTT` TEXT,"+
            "`TONG_TIEN_CHUA_THUE` INTEGER, `TIEN_THUE` INTEGER)";

    private String CREATE_TABLE_DEBT_COLLECTION = "CREATE TABLE `" + TABLE_NAME_DEBT_COLLECTION + "` ( `ID` INTEGER PRIMARY KEY AUTOINCREMENT , `E_DONG` TEXT, `MA_HOA_DON` TEXT, `SERI_HDON` TEXT, `MA_KHANG` TEXT, " +
            "`MA_THE` TEXT, `TEN_KHANG` TEXT, `DIA_CHI` TEXT, `THANG_TTOAN` DATE, `PHIEN_TTOAN` TEXT, `SO_TIEN_TTOAN` INTEGER, " +
            "`SO_GCS` TEXT, `DIEN_LUC` TEXT, `SO_HO` TEXT, `SO_DAU_KY` TEXT, `SO_CUOI_KY` TEXT, `SO_CTO` TEXT, `SDT_ECPAY` TEXT, " +
            "`SDT_EVN` TEXT, `GIAO_THU` TEXT, `NGAY_GIAO_THU` DATE, `TRANG_THAI_TTOAN` TEXT, `VI_TTOAN` TEXT, `HINH_THUC_TT` TEXT, " +
            "`TRANG_THAI_CHAM_NO` TEXT, `TRANG_THAI_HUY` TEXT, `TRANG_THAI_HOAN_TRA` TEXT, `TRANG_THAI_XU_LY_NGHI_NGO` TEXT, " +
            "`TRANG_THAI_DAY_CHAM_NO` TEXT, `NGAY_DAY` DATE, `NGAY_THU` DATE, `SO_LAN_IN_BIEN_NHAN` INTEGER, `IN_THONG_BAO_DIEN` TEXT)";

    private String CREATE_TABLE_HISTORY_PAY = "CREATE TABLE `" + TABLE_NAME_HISTORY_PAY + "` ( `ID` INTEGER PRIMARY KEY AUTOINCREMENT , `E_DONG` TEXT, `MA_HOA_DON` TEXT, " +
            "`SERI_HDON` TEXT, `MA_KHANG` TEXT, " +
            "`MA_THE` TEXT, `TEN_KHANG` TEXT, `DIA_CHI` TEXT, `THANG_TTOAN` DATE, `PHIEN_TTOAN` TEXT, `SO_TIEN_TTOAN` INTEGER, " +
            "`SO_GCS` TEXT, `DIEN_LUC` TEXT, `SO_HO` TEXT, `SO_DAU_KY` TEXT, `SO_CUOI_KY` TEXT, `SO_CTO` TEXT, `SDT_ECPAY` TEXT, " +
            "`SDT_EVN` TEXT, `GIAO_THU` TEXT, `NGAY_GIAO_THU` DATE, `TRANG_THAI_TTOAN` TEXT, `VI_TTOAN` TEXT, `HINH_THUC_TT` TEXT, " +
            "`TRANG_THAI_CHAM_NO` TEXT, `TRANG_THAI_HUY` TEXT, `TRANG_THAI_NGHI_NGO` TEXT, `SO_IN_BIEN_NHAN` INTERGER, `IN_THONG_BAO_DIEN` TEXT, " +
            "`NGAY_PHAT_SINH` DATE, `MA_GIAO_DICH` TEXT)";

    private SQLiteConnection(Context context) {
        super(context, Common.PATH_FOLDER_DB + databaseName, null, DATABASE_VERSION);
        this.checkFileDBExist();
        SQLiteDatabase.openOrCreateDatabase(Common.PATH_FOLDER_DB + databaseName, null);
    }

    private void checkFileDBExist() {
        File programDirectory = new File(PATH_FOLDER_ROOT);
        if (!programDirectory.exists()) {
            programDirectory.mkdirs();
        }
        File programDbDirectory = new File(PATH_FOLDER_DB);
        if (!programDbDirectory.exists()) {
            programDbDirectory.mkdirs();
        }
        File programConfigDirectory = new File(PATH_FOLDER_CONFIG);
        if (!programConfigDirectory.exists()) {
            programConfigDirectory.mkdirs();
        }
    }

    public static synchronized SQLiteConnection getInstance(Context context) {
        if (instance == null)
            instance = new SQLiteConnection(context.getApplicationContext());
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE_ACCOUNT);
            db.execSQL(CREATE_TABLE_EVN_PC);
            db.execSQL(CREATE_TABLE_BOOK_CMIS);
            db.execSQL(CREATE_TABLE_BILL);
            db.execSQL(CREATE_TABLE_DEBT_COLLECTION);
            db.execSQL(CREATE_TABLE_CUSTOMER);
            db.execSQL(CREATE_TABLE_HISTORY_PAY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_ACCOUNT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_EVN_PC);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_BOOK_CMIS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_BILL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_DEBT_COLLECTION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_CUSTOMER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_HISTORY_PAY);
        onCreate(db);
    }

    @Override
    public synchronized void close() {
        if (database != null) {
            database.close();
            database = null;
        }
        super.close();
    }


    public void deleteAllData()
    {
        try
        {
            database = getReadableDatabase();
//            String query = "DELETE FROM " + TABLE_NAME_BILL;
//            database.execSQL(query, null);
//
//            query = "DELETE FROM " + TABLE_NAME_DEBT_COLLECTION;
//            database.execSQL(query, null);
//
//
//            query = "DELETE FROM " + TABLE_NAME_HISTORY_PAY;
//            database.execSQL(query, null);
            database.delete(TABLE_NAME_BILL, null, null);
            database.delete(TABLE_NAME_DEBT_COLLECTION, null, null);
            database.delete(TABLE_NAME_HISTORY_PAY, null, null);

        }catch (Exception e)
        {
            e.printStackTrace();
        }


    }

    //region sqlite
    public void insertOrUpdateAccount(Account account) {
        try {
            if (account == null)
                return;


            ContentValues initialValues = new ContentValues();
            initialValues.put("edong", account.getEdong());
            initialValues.put("name", account.getName());
            initialValues.put("address", account.getAddress());
            initialValues.put("phone", account.getPhone());
            initialValues.put("email", account.getEmail());
            initialValues.put("birthday", account.getBirthday());
            initialValues.put("session", account.getSession());
            initialValues.put("balance", account.getBalance());
            initialValues.put("lockMoney", account.getLockMoney());
            initialValues.put("changePIN", (account.isChangePIN() == true) ? "1" : "0");
            initialValues.put("verified", account.getVerified());
            initialValues.put("mac", account.getMac());
            initialValues.put("ip", account.getIp());
            initialValues.put("strLoginTime", account.getStrLoginTime());
            initialValues.put("strLogoutTime", account.getStrLogoutTime());
            initialValues.put("type", account.getType());
            initialValues.put("status", account.getStatus());
            initialValues.put("idNumber", account.getIdNumber());
            initialValues.put("idNumberDate", account.getIdNumberDate());
            initialValues.put("idNumberPlace", account.getIdNumberPlace());
            initialValues.put("parentEdong", account.getParentEdong());

            database = getWritableDatabase();
            int rowAffect = (int) database.insertWithOnConflict(TABLE_NAME_ACCOUNT, null, initialValues, SQLiteDatabase.CONFLICT_IGNORE);
            if (rowAffect == ERROR_OCCUR) {
                rowAffect = database.update(TABLE_NAME_ACCOUNT, initialValues, "edong=?", new String[]{account.getEdong()});  // number 1 is the _id here, update to variable for your code
            }
            if (rowAffect == ERROR_OCCUR) {
                Log.e(TAG, "insertOrUpdateAccount: cannot update or insert data to account table");
            }
        }catch (Exception e)
        {
            Log.e(TAG, "database : " +e.getMessage() );
        }
    }

    public double selectBalance() {
        database = getReadableDatabase();
        String query = "SELECT balance FROM " + TABLE_NAME_ACCOUNT;
        Cursor c = database.rawQuery(query, null);
        if (c.moveToFirst()) {
            return c.getDouble(0);
        }
        return 0;
    }

    public Account selectAccount(String edong) throws SQLiteException {
        if (edong == null)
            return null;

        database = getReadableDatabase();
        Cursor mCursor =
                database.query(true, TABLE_NAME_ACCOUNT, new String[]{
                                "edong",
                                "name",
                                "address",
                                "phone",
                                "email",
                                "birthday",
                                "session",
                                "balance",
                                "lockMoney",
                                "changePIN",
                                "verified",
                                "mac",
                                "ip",
                                "strLoginTime",
                                "strLogoutTime",
                                "type",
                                "status",
                                "idNumber",
                                "idNumberDate",
                                "idNumberPlace",
                                "parentEdong",
                                "notYetPushMoney"
                        },
                        "edong" + "=?",
                        new String[]{edong},
                        null, null, null, null);

        Account account = null;
        if (mCursor.moveToFirst()) {

            account = new Account(
                    stringConvertNull(mCursor.getString(mCursor.getColumnIndex("edong"))),
                    stringConvertNull(mCursor.getString(mCursor.getColumnIndex("name"))),
                    stringConvertNull(mCursor.getString(mCursor.getColumnIndex("address"))),
                    stringConvertNull(mCursor.getString(mCursor.getColumnIndex("phone"))),
                    stringConvertNull(mCursor.getString(mCursor.getColumnIndex("email"))),
                    stringConvertNull(mCursor.getString(mCursor.getColumnIndex("birthday"))),
                    stringConvertNull(mCursor.getString(mCursor.getColumnIndex("session"))),
                    longConvertNull(mCursor.getLong(mCursor.getColumnIndex("balance"))),
                    intConvertNull(mCursor.getInt(mCursor.getColumnIndex("lockMoney"))),
                    (mCursor.getInt(mCursor.getColumnIndex("changePIN")) == 1) ? true : false,
                    intConvertNull(mCursor.getInt(mCursor.getColumnIndex("verified"))),
                    stringConvertNull(mCursor.getString(mCursor.getColumnIndex("mac"))),
                    stringConvertNull(mCursor.getString(mCursor.getColumnIndex("ip"))),
                    stringConvertNull(mCursor.getString(mCursor.getColumnIndex("strLoginTime"))),
                    stringConvertNull(mCursor.getString(mCursor.getColumnIndex("strLogoutTime"))),
                    intConvertNull(mCursor.getInt(mCursor.getColumnIndex("type"))),
                    stringConvertNull(mCursor.getString(mCursor.getColumnIndex("status"))),
                    stringConvertNull(mCursor.getString(mCursor.getColumnIndex("idNumber"))),
                    stringConvertNull(mCursor.getString(mCursor.getColumnIndex("idNumberDate"))),
                    stringConvertNull(mCursor.getString(mCursor.getColumnIndex("idNumberPlace"))),
                    stringConvertNull(mCursor.getString(mCursor.getColumnIndex("parentEdong")))
//                    ,  intConvertNull(mCursor.getInt(mCursor.getColumnIndex("notYetPushMoney")))
            );
        }

        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }
        return account;
    }


    public ReportModel.BillInfo countBillDuocGiao(String edong) {
        ReportModel.BillInfo bill = new ReportModel.BillInfo();

        String query = "SELECT coalesce(SUM(SO_TIEN_TTOAN), 0) AS SUM, COUNT(*) AS COUNT FROM (SELECT * FROM " + TABLE_NAME_BILL + " WHERE E_DONG = '" + edong + "' and GIAO_THU = '" + Common.TRANG_THAI_GIAO_THU.GIAO_THU.getCode() + "') AS BILL "
                + " JOIN (SELECT * FROM " + TABLE_NAME_CUSTOMER+ " WHERE E_DONG = '" + edong + "') AS CUSTOMER ON BILL.MA_KHANG = CUSTOMER.MA_KHANG";
        Cursor mCursor = database.rawQuery(query, null);

        if (mCursor.getCount() != ZERO && mCursor.moveToFirst()) {
            bill.setAmount(Long.parseLong(mCursor.getString(mCursor.getColumnIndex("SUM"))));
            bill.setCount(Integer.parseInt(mCursor.getString(mCursor.getColumnIndex("COUNT"))));
        }

        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }

        return bill;

    }


    public ReportModel.BillInfo countBillDaThu(String edong) {
        ReportModel.BillInfo bill = new ReportModel.BillInfo();
        String query = "SELECT coalesce(SUM(SO_TIEN_TTOAN), 0) AS SUM, COUNT(*) AS COUNT FROM ( SELECT * FROM " + TABLE_NAME_DEBT_COLLECTION + " WHERE E_DONG = '" + edong + "') AS BILL"
                + " JOIN (SELECT * FROM " + TABLE_NAME_CUSTOMER+ " WHERE E_DONG = '" + edong + "') AS CUSTOMER ON BILL.MA_KHANG = CUSTOMER.MA_KHANG";
        Cursor mCursor = database.rawQuery(query, null);

        if (mCursor.getCount() != ZERO && mCursor.moveToFirst()) {
            bill.setAmount(Long.parseLong(mCursor.getString(mCursor.getColumnIndex("SUM"))));
            bill.setCount(Integer.parseInt(mCursor.getString(mCursor.getColumnIndex("COUNT"))));
        }

        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }

        return bill;

    }


    public ReportModel.BillInfo countBillHoanTra(String edong) {
        ReportModel.BillInfo bill = new ReportModel.BillInfo();
        String query = "SELECT coalesce(SUM(SO_TIEN_TTOAN), 0) AS SUM, COUNT(*) AS COUNT FROM ( SELECT * FROM " + TABLE_NAME_DEBT_COLLECTION + " WHERE E_DONG = '" + edong + "' and TRANG_THAI_HOAN_TRA = '" + Common.TRANG_THAI_HOAN_TRA.CHUA_TRA.getCode() + "') AS BILL"
                + " JOIN (SELECT * FROM " + TABLE_NAME_CUSTOMER+ " WHERE E_DONG = '" + edong + "') AS CUSTOMER ON BILL.MA_KHANG = CUSTOMER.MA_KHANG";
        Cursor mCursor = database.rawQuery(query, null);

        if (mCursor.getCount() != ZERO && mCursor.moveToFirst()) {
            bill.setAmount(Long.parseLong(mCursor.getString(mCursor.getColumnIndex("SUM"))));
            bill.setCount(Integer.parseInt(mCursor.getString(mCursor.getColumnIndex("COUNT"))));
        }

        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }

        return bill;

    }


    public ReportModel.BillInfo countBillVangLai(String edong) {
        ReportModel.BillInfo bill = new ReportModel.BillInfo();
        String query = "SELECT coalesce(SUM(SO_TIEN_TTOAN), 0) AS SUM, COUNT(*) AS COUNT FROM ( SELECT * FROM " + TABLE_NAME_DEBT_COLLECTION + " bill WHERE bill.E_DONG = '" + edong +
                "' and GIAO_THU = '" + Common.TRANG_THAI_GIAO_THU.VANG_LAI.getCode() + "' ) AS BILL"
                + " JOIN (SELECT * FROM " + TABLE_NAME_CUSTOMER+ " WHERE E_DONG = '" + edong + "') AS CUSTOMER ON BILL.MA_KHANG = CUSTOMER.MA_KHANG";
//                "                  FROM employees" +
//                "                  WHERE departments.department_id = employees.department_id)";
        Cursor mCursor = database.rawQuery(query, null);

        if (mCursor.getCount() != ZERO && mCursor.moveToFirst()) {
            bill.setAmount(Long.parseLong(mCursor.getString(mCursor.getColumnIndex("SUM"))));
            bill.setCount(Integer.parseInt(mCursor.getString(mCursor.getColumnIndex("COUNT"))));
        }

        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }

        return bill;

    }


    public List<EntityHoaDonThu> getBillThuByCodeAndDate(String edong, boolean isMaKH, String customerCode, Calendar dateFrom, Calendar dateTo) {
        List<EntityHoaDonThu> lst = new ArrayList<>();


        String query;

        String queryDateFrom = "";
        String queryDateTo = "";
        if (dateFrom != null) {
            queryDateFrom = " and date(NGAY_THU) >= date('" + Common.parse(dateFrom.getTime(), Common.DATE_TIME_TYPE.FULL.toString()) + "')";
        }

        if (dateTo != null) {
            queryDateTo = " and date(NGAY_THU) <= date('" + Common.parse(dateTo.getTime(), Common.DATE_TIME_TYPE.FULL.toString()) + "')";
        }

        if (isMaKH) {
            query = "SELECT * FROM " + TABLE_NAME_DEBT_COLLECTION + " WHERE E_DONG = '" + edong +
                    "' and MA_KHANG like '%" + customerCode + "%' " + queryDateFrom + queryDateTo + " ORDER BY date(NGAY_THU) DESC";
        } else {
            query = "SELECT * FROM " + TABLE_NAME_DEBT_COLLECTION + " WHERE E_DONG = '" + edong +
                    "' and TEN_KHANG like '%" + customerCode + "%'" + queryDateFrom + queryDateTo + " ORDER BY date(NGAY_THU) DESC";
        }


        query = "SELECT * FROM " + TABLE_NAME_CUSTOMER + " A INNER JOIN ( " + query + ") B ON A.MA_KHANG = B.MA_KHANG";

        Cursor mCursor = database.rawQuery(query, null);

        if (mCursor != null && mCursor.moveToFirst()) {
            int count = mCursor.getCount();
            do {

                EntityHoaDonThu hoaDonThu = new EntityHoaDonThu();


                hoaDonThu.setE_DONG(mCursor.getString(mCursor.getColumnIndex("E_DONG")));
                hoaDonThu.setSERI_HDON(mCursor.getString(mCursor.getColumnIndex("SERI_HDON")));
                hoaDonThu.setMA_HOA_DON(mCursor.getString(mCursor.getColumnIndex("MA_HOA_DON")));
                hoaDonThu.setMA_KHANG(mCursor.getString(mCursor.getColumnIndex("MA_KHANG")));
                hoaDonThu.setMA_THE(mCursor.getString(mCursor.getColumnIndex("MA_THE")));
                hoaDonThu.setTEN_KHANG(mCursor.getString(4));
                hoaDonThu.setDIA_CHI(mCursor.getString(mCursor.getColumnIndex("DIA_CHI")));
                hoaDonThu.setTHANG_TTOAN(Common.parseDate(mCursor.getString(mCursor.getColumnIndex("THANG_TTOAN")), Common.DATE_TIME_TYPE.FULL.toString()));
                hoaDonThu.setPHIEN_TTOAN(mCursor.getInt(mCursor.getColumnIndex("PHIEN_TTOAN")));
                hoaDonThu.setSO_TIEN_TTOAN(mCursor.getInt(mCursor.getColumnIndex("SO_TIEN_TTOAN")));
                hoaDonThu.setSO_GCS(mCursor.getString(mCursor.getColumnIndex("SO_GCS")));
                hoaDonThu.setDIEN_LUC(mCursor.getString(mCursor.getColumnIndex("DIEN_LUC")));
                hoaDonThu.setSO_HO(mCursor.getString(mCursor.getColumnIndex("SO_HO")));
                hoaDonThu.setSO_DAU_KY(mCursor.getString(mCursor.getColumnIndex("SO_DAU_KY")));
                hoaDonThu.setSO_CUOI_KY(mCursor.getString(mCursor.getColumnIndex("SO_CUOI_KY")));
                hoaDonThu.setSO_CTO(mCursor.getString(mCursor.getColumnIndex("SO_CTO")));
                hoaDonThu.setSDT_ECPAY(mCursor.getString(mCursor.getColumnIndex("SDT_ECPAY")));
                hoaDonThu.setSDT_EVN(mCursor.getString(mCursor.getColumnIndex("SDT_EVN")));
                hoaDonThu.setGIAO_THU(mCursor.getString(mCursor.getColumnIndex("GIAO_THU")));
                hoaDonThu.setNGAY_GIAO_THU(Common.parseDate(mCursor.getString(mCursor.getColumnIndex("NGAY_GIAO_THU")), Common.DATE_TIME_TYPE.FULL.toString()));
                hoaDonThu.setTRANG_THAI_TTOAN(mCursor.getString(mCursor.getColumnIndex("TRANG_THAI_TTOAN")));
                hoaDonThu.setVI_TTOAN(mCursor.getString(mCursor.getColumnIndex("VI_TTOAN")));
                hoaDonThu.setNGAY_THU(Common.parseDate(mCursor.getString(mCursor.getColumnIndex("NGAY_THU")), Common.DATE_TIME_TYPE.FULL.toString()));
                hoaDonThu.setHINH_THUC_TT(mCursor.getString(mCursor.getColumnIndex("HINH_THUC_TT")));
                hoaDonThu.setTRANG_THAI_CHAM_NO(mCursor.getString(mCursor.getColumnIndex("TRANG_THAI_CHAM_NO")));
                hoaDonThu.setTRANG_THAI_HUY(mCursor.getString(mCursor.getColumnIndex("TRANG_THAI_HUY")));
                hoaDonThu.setTRANG_THAI_HOAN_TRA(mCursor.getString(mCursor.getColumnIndex("TRANG_THAI_HOAN_TRA")));
                hoaDonThu.setTRANG_THAI_XU_LY_NGHI_NGO(mCursor.getString(mCursor.getColumnIndex("TRANG_THAI_XU_LY_NGHI_NGO")));
                hoaDonThu.setTRANG_THAI_DAY_CHAM_NO(mCursor.getString(mCursor.getColumnIndex("TRANG_THAI_DAY_CHAM_NO")));

                hoaDonThu.setNGAY_DAY(Common.parseDate(mCursor.getString(mCursor.getColumnIndex("NGAY_DAY")), Common.DATE_TIME_TYPE.FULL.toString()));
                hoaDonThu.setSO_LAN_IN_BIEN_NHAN(mCursor.getInt(mCursor.getColumnIndex("SO_LAN_IN_BIEN_NHAN")));
                hoaDonThu.setIN_THONG_BAO_DIEN(mCursor.getString(mCursor.getColumnIndex("IN_THONG_BAO_DIEN")));

                lst.add(hoaDonThu);

            }
            while (mCursor.moveToNext());

            mCursor.close();
        }

        if (lst.size() > 1) {
//            Collections.sort(lst, new Comparator<Bill>() {
//                @Override
//                public int compare(Bill bill, Bill t1) {
//                    if (bill.getRequestDateCal().before(t1.getRequestDateCal()))
//                        return -1;
//                    return 1;
//                }
//            });
        }

        return lst;
    }


    private ReportLichSuThanhToanAdapter.LichSuThanhToanData find(String maKH, List<ReportLichSuThanhToanAdapter.LichSuThanhToanData> lst) {
        for (int i = 0, n = lst.size(); i < n; i++) {
            if (lst.get(i).getMA_KHACH_HANG().equalsIgnoreCase(maKH)) {
                return lst.get(i);
            }
        }

        return null;
    }

    String getTenKhachHangByMaKhachHang(String edong, String MA_KHACH_HANG) {
        Cursor mCursor = database.rawQuery("SELECT TEN_KHANG FROM " + TABLE_NAME_CUSTOMER + " WHERE MA_KHANG = '" + MA_KHACH_HANG + "' and E_DONG = '" + edong + "'", null);
        if (mCursor != null) {
            if (mCursor.moveToFirst()) {
                return mCursor.getString(mCursor.getColumnIndex("TEN_KHANG"));
            }
            mCursor.close();
        }
        return "";
    }


    public List<ReportLichSuThanhToanAdapter.LichSuThanhToanData> getLichSuThanhToan(String edong, boolean isMaKH, String customerCode) {
        List<ReportLichSuThanhToanAdapter.LichSuThanhToanData> lst = new ArrayList<>();


        String query;
        if (isMaKH) {
            query = "SELECT * FROM " + TABLE_NAME_HISTORY_PAY + " WHERE E_DONG = '" + edong +
                    "' and MA_KHANG like '%" + customerCode + "%' " + " ORDER BY date(NGAY_PHAT_SINH) DESC";
        } else {
            query = "SELECT * FROM " + TABLE_NAME_HISTORY_PAY + " WHERE E_DONG = '" + edong +
                    "' and TEN_KHANG like '%" + customerCode + "%' " + " ORDER BY date(NGAY_PHAT_SINH) DESC";
        }

        Cursor mCursor = database.rawQuery(query, null);

        if (mCursor != null && mCursor.moveToFirst()) {
            do {

                ReportLichSuThanhToanAdapter.LichSuThanhToanData data = this.find(mCursor.getString(mCursor.getColumnIndex("MA_KHANG")), lst);

                String name, code;

                if (data == null) {

                    name = mCursor.getString(mCursor.getColumnIndex("TEN_KHANG"));
                    code = mCursor.getString(mCursor.getColumnIndex("MA_KHANG"));

                    if (name == null || name.length() == 0) {
                        name = getTenKhachHangByMaKhachHang(MainActivity.mEdong, code);
                    }

                    data = new ReportLichSuThanhToanAdapter.LichSuThanhToanData(code, name);
                    lst.add(data);
                } else {
                    name = data.getTEN_KHACH_HANG();
                    code = data.getMA_KHACH_HANG();
                }

                EntityLichSuThanhToan lichsu = new EntityLichSuThanhToan();


                lichsu.setE_DONG(mCursor.getString(mCursor.getColumnIndex("E_DONG")));
                lichsu.setSERI_HDON(mCursor.getString(mCursor.getColumnIndex("SERI_HDON")));
                lichsu.setMA_HOA_DON(mCursor.getString(mCursor.getColumnIndex("MA_HOA_DON")));
                lichsu.setMA_KHANG(code);
                lichsu.setMA_THE(mCursor.getString(mCursor.getColumnIndex("MA_THE")));
                lichsu.setTEN_KHANG(name);
                lichsu.setDIA_CHI(mCursor.getString(mCursor.getColumnIndex("DIA_CHI")));
                lichsu.setTHANG_TTOAN(Common.parseDate(mCursor.getString(mCursor.getColumnIndex("THANG_TTOAN")), Common.DATE_TIME_TYPE.FULL.toString()));
                lichsu.setPHIEN_TTOAN(mCursor.getInt(mCursor.getColumnIndex("PHIEN_TTOAN")));
                lichsu.setSO_TIEN_TTOAN(mCursor.getInt(mCursor.getColumnIndex("SO_TIEN_TTOAN")));
                lichsu.setSO_GCS(mCursor.getString(mCursor.getColumnIndex("SO_GCS")));
                lichsu.setDIEN_LUC(mCursor.getString(mCursor.getColumnIndex("DIEN_LUC")));
                lichsu.setSO_HO(mCursor.getString(mCursor.getColumnIndex("SO_HO")));
                lichsu.setSO_DAU_KY(mCursor.getString(mCursor.getColumnIndex("SO_DAU_KY")));
                lichsu.setSO_CUOI_KY(mCursor.getString(mCursor.getColumnIndex("SO_CUOI_KY")));
                lichsu.setSO_CTO(mCursor.getString(mCursor.getColumnIndex("SO_CTO")));
                lichsu.setSDT_ECPAY(mCursor.getString(mCursor.getColumnIndex("SDT_ECPAY")));
                lichsu.setSDT_EVN(mCursor.getString(mCursor.getColumnIndex("SDT_EVN")));
                lichsu.setGIAO_THU(mCursor.getString(mCursor.getColumnIndex("GIAO_THU")));
                lichsu.setNGAY_GIAO_THU(Common.parseDate(mCursor.getString(mCursor.getColumnIndex("NGAY_GIAO_THU")), Common.DATE_TIME_TYPE.FULL.toString()));
                lichsu.setTRANG_THAI_TTOAN(mCursor.getString(mCursor.getColumnIndex("TRANG_THAI_TTOAN")));
                lichsu.setVI_TTOAN(mCursor.getString(mCursor.getColumnIndex("VI_TTOAN")));


                lichsu.setHINH_THUC_TT(mCursor.getString(mCursor.getColumnIndex("HINH_THUC_TT")));
                lichsu.setTRANG_THAI_CHAM_NO(mCursor.getString(mCursor.getColumnIndex("TRANG_THAI_CHAM_NO")));
                lichsu.setTRANG_THAI_HUY(mCursor.getString(mCursor.getColumnIndex("TRANG_THAI_HUY")));
                lichsu.setTRANG_THAI_NGHI_NGO(mCursor.getString(mCursor.getColumnIndex("TRANG_THAI_NGHI_NGO")));
                lichsu.setSO_IN_BIEN_NHAN(mCursor.getInt(mCursor.getColumnIndex("SO_IN_BIEN_NHAN")));
                lichsu.setIN_THONG_BAO_DIEN(mCursor.getString(mCursor.getColumnIndex("IN_THONG_BAO_DIEN")));
                lichsu.setNGAY_PHAT_SINH(Common.parseDate(mCursor.getString(mCursor.getColumnIndex("NGAY_PHAT_SINH")), Common.DATE_TIME_TYPE.FULL.toString()));
                lichsu.setMA_GIAO_DICH(mCursor.getString(mCursor.getColumnIndex("MA_GIAO_DICH")));

                data.getLichSu().add(lichsu);
            }
            while (mCursor.moveToNext());

            mCursor.close();
        }


        return lst;
    }


    public List<EntityHoaDonThu> getBillHoanTraByCodeAndDate(String edong, boolean isMaKH, String customerCode, Calendar dateFrom, Calendar dateTo) {
        List<EntityHoaDonThu> lst = new ArrayList<>();

        String query;

        String queryDateFrom = "";
        String queryDateTo = "";
        if (dateFrom != null) {
            queryDateFrom = " and date(NGAY_THU) >= date('" + Common.parse(dateFrom.getTime(), Common.DATE_TIME_TYPE.FULL.toString()) + "')";
        }

        if (dateTo != null) {
            queryDateTo = " and date(NGAY_THU) <= date('" + Common.parse(dateTo.getTime(), Common.DATE_TIME_TYPE.FULL.toString()) + "')";
        }

        if (isMaKH) {
            query = "SELECT * FROM " + TABLE_NAME_DEBT_COLLECTION + " WHERE E_DONG = '" + edong +
                    "' and MA_KHANG like '%" + customerCode + "%' and TRANG_THAI_HOAN_TRA = '" + Common.TRANG_THAI_HOAN_TRA.CHUA_TRA.getCode() + "' " + queryDateFrom + queryDateTo + " ORDER BY date(NGAY_THU) DESC";
        } else {
            query = "SELECT * FROM " + TABLE_NAME_DEBT_COLLECTION + " WHERE E_DONG = '" + edong +
                    "' and TEN_KHANG like '%" + customerCode + "%' and TRANG_THAI_HOAN_TRA = '" + Common.TRANG_THAI_HOAN_TRA.CHUA_TRA.getCode() + "' " + queryDateFrom + queryDateTo + " ORDER BY date(NGAY_THU) DESC";
        }

        query = "SELECT * FROM " + TABLE_NAME_CUSTOMER + " A INNER JOIN ( " + query + ") B ON A.MA_KHANG = B.MA_KHANG";

        Cursor mCursor = database.rawQuery(query, null);

        if (mCursor != null && mCursor.moveToFirst()) {
            int count = mCursor.getCount();
            do {

                EntityHoaDonThu hoaDonThu = new EntityHoaDonThu();


                hoaDonThu.setE_DONG(mCursor.getString(mCursor.getColumnIndex("E_DONG")));
                hoaDonThu.setSERI_HDON(mCursor.getString(mCursor.getColumnIndex("SERI_HDON")));
                hoaDonThu.setMA_HOA_DON(mCursor.getString(mCursor.getColumnIndex("MA_HOA_DON")));
                hoaDonThu.setMA_KHANG(mCursor.getString(mCursor.getColumnIndex("MA_KHANG")));
                hoaDonThu.setMA_THE(mCursor.getString(mCursor.getColumnIndex("MA_THE")));
                hoaDonThu.setTEN_KHANG(mCursor.getString(4));
                hoaDonThu.setDIA_CHI(mCursor.getString(mCursor.getColumnIndex("DIA_CHI")));
                hoaDonThu.setTHANG_TTOAN(Common.parseDate(mCursor.getString(mCursor.getColumnIndex("THANG_TTOAN")), Common.DATE_TIME_TYPE.FULL.toString()));
                hoaDonThu.setPHIEN_TTOAN(mCursor.getInt(mCursor.getColumnIndex("PHIEN_TTOAN")));
                hoaDonThu.setSO_TIEN_TTOAN(mCursor.getInt(mCursor.getColumnIndex("SO_TIEN_TTOAN")));
                hoaDonThu.setSO_GCS(mCursor.getString(mCursor.getColumnIndex("SO_GCS")));
                hoaDonThu.setDIEN_LUC(mCursor.getString(mCursor.getColumnIndex("DIEN_LUC")));
                hoaDonThu.setSO_HO(mCursor.getString(mCursor.getColumnIndex("SO_HO")));
                hoaDonThu.setSO_DAU_KY(mCursor.getString(mCursor.getColumnIndex("SO_DAU_KY")));
                hoaDonThu.setSO_CUOI_KY(mCursor.getString(mCursor.getColumnIndex("SO_CUOI_KY")));
                hoaDonThu.setSO_CTO(mCursor.getString(mCursor.getColumnIndex("SO_CTO")));
                hoaDonThu.setSDT_ECPAY(mCursor.getString(mCursor.getColumnIndex("SDT_ECPAY")));
                hoaDonThu.setSDT_EVN(mCursor.getString(mCursor.getColumnIndex("SDT_EVN")));
                hoaDonThu.setGIAO_THU(mCursor.getString(mCursor.getColumnIndex("GIAO_THU")));
                hoaDonThu.setNGAY_GIAO_THU(Common.parseDate(mCursor.getString(mCursor.getColumnIndex("NGAY_GIAO_THU")), Common.DATE_TIME_TYPE.FULL.toString()));
                hoaDonThu.setTRANG_THAI_TTOAN(mCursor.getString(mCursor.getColumnIndex("TRANG_THAI_TTOAN")));
                hoaDonThu.setVI_TTOAN(mCursor.getString(mCursor.getColumnIndex("VI_TTOAN")));
                hoaDonThu.setNGAY_THU(Common.parseDate(mCursor.getString(mCursor.getColumnIndex("NGAY_THU")), Common.DATE_TIME_TYPE.FULL.toString()));
                hoaDonThu.setHINH_THUC_TT(mCursor.getString(mCursor.getColumnIndex("HINH_THUC_TT")));
                hoaDonThu.setTRANG_THAI_CHAM_NO(mCursor.getString(mCursor.getColumnIndex("TRANG_THAI_CHAM_NO")));
                hoaDonThu.setTRANG_THAI_HUY(mCursor.getString(mCursor.getColumnIndex("TRANG_THAI_HUY")));
                hoaDonThu.setTRANG_THAI_HOAN_TRA(mCursor.getString(mCursor.getColumnIndex("TRANG_THAI_HOAN_TRA")));
                hoaDonThu.setTRANG_THAI_XU_LY_NGHI_NGO(mCursor.getString(mCursor.getColumnIndex("TRANG_THAI_XU_LY_NGHI_NGO")));
                hoaDonThu.setTRANG_THAI_DAY_CHAM_NO(mCursor.getString(mCursor.getColumnIndex("TRANG_THAI_DAY_CHAM_NO")));

                hoaDonThu.setNGAY_DAY(Common.parseDate(mCursor.getString(mCursor.getColumnIndex("NGAY_DAY")), Common.DATE_TIME_TYPE.FULL.toString()));
                hoaDonThu.setSO_LAN_IN_BIEN_NHAN(mCursor.getInt(mCursor.getColumnIndex("SO_LAN_IN_BIEN_NHAN")));
                hoaDonThu.setIN_THONG_BAO_DIEN(mCursor.getString(mCursor.getColumnIndex("IN_THONG_BAO_DIEN")));

                lst.add(hoaDonThu);

            }
            while (mCursor.moveToNext());

            mCursor.close();
        }

        return lst;
    }

    public int countBill(String edong) {
        if (edong == null)
            return 0;

        database = this.getReadableDatabase();

//        String query = "SELECT COUNT(*) AS COUNT FROM " + TABLE_NAME_BILL + " WHERE E_DONG = '" + edong + "' and GIAO_THU = '" + Common.TRANG_THAI_GIAO_THU.GIAO_THU.getCode() + "' " +
//                " and TRANG_THAI_TTOAN = '" + Common.TRANG_THAI_TTOAN.CHUA_TTOAN.getCode() + "'";// + "' and status = " + ZERO;

        String query = "SELECT COUNT(*) AS COUNT FROM(SELECT * FROM " + TABLE_NAME_BILL + " WHERE E_DONG = '" + edong + "' and GIAO_THU ='" + Common.TRANG_THAI_GIAO_THU.GIAO_THU.getCode() + "'  and TRANG_THAI_TTOAN ='" + Common.TRANG_THAI_TTOAN.CHUA_TTOAN.getCode() + "' ) AS BILL " +
                "JOIN (SELECT * FROM " + TABLE_NAME_CUSTOMER + " WHERE E_DONG = '" + edong + "') AS CUSTOMER ON BILL.MA_KHANG = CUSTOMER.MA_KHANG";

        Cursor mCursor = database.rawQuery(query, null);
        int count = mCursor.getCount();
        if (count != ZERO) {
            mCursor.moveToFirst();
            count = Integer.parseInt(mCursor.getString(mCursor.getColumnIndex("COUNT")));
        }
        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }
        return count;
    }


    public List<PayAdapter.BillEntityAdapter> selectOfflineBill() {
        database = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_DEBT_COLLECTION + " WHERE E_DONG = '" + MainActivity.mEdong + "' and HINH_THUC_TT = '" + Common.HINH_THUC_TTOAN.OFFLINE.getCode() + "' and TRANG_THAI_DAY_CHAM_NO = '" + Common.TRANG_THAI_DAY_CHAM_NO.CHUA_DAY.getCode() + "'";
        Cursor mCursor = database.rawQuery(query, null);

        List<PayAdapter.BillEntityAdapter> lst = new ArrayList<>();

        if (mCursor != null && mCursor.moveToFirst()) {
            do {

                int billId = intConvertNull(mCursor.getInt(mCursor.getColumnIndex("MA_HOA_DON")));
                int amount = intConvertNull(mCursor.getInt(mCursor.getColumnIndex("SO_TIEN_TTOAN")));

                PayAdapter.BillEntityAdapter bill = new PayAdapter.BillEntityAdapter();
                bill.setBillId(billId);
                bill.setTIEN_THANH_TOAN(amount);
                bill.setMA_KHACH_HANG(mCursor.getString(mCursor.getColumnIndex("MA_KHANG")));

                lst.add(bill);


            } while (mCursor.moveToNext());
        }


        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }
        return lst;
    }


    public void updateHoaDonThu(String MA_HOA_DON, Common.TRANG_THAI_TTOAN TRANG_THAI_TTOAN, String TRANG_THAI_CHAM_NO,
                                String TRANG_THAI_DAY_CHAM_NO, String NGAY_DAY, String TRANG_THAI_HOAN_TRA) {
        database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("TRANG_THAI_TTOAN", TRANG_THAI_TTOAN.getCode());
        contentValues.put("TRANG_THAI_CHAM_NO", TRANG_THAI_CHAM_NO);
        contentValues.put("TRANG_THAI_DAY_CHAM_NO", TRANG_THAI_DAY_CHAM_NO);
        contentValues.put("NGAY_DAY", NGAY_DAY);
        contentValues.put("TRANG_THAI_HOAN_TRA", TRANG_THAI_HOAN_TRA);
        database.update(TABLE_NAME_DEBT_COLLECTION, contentValues, "MA_HOA_DON = ?", new String[]{MA_HOA_DON});
    }


    public void updateHoaDonThu(String MA_HOA_DON, String VI_TTOAN, Common.TRANG_THAI_TTOAN TRANG_THAI_TTOAN,
                                String TRANG_THAI_CHAM_NO, String TRANG_THAI_DAY_CHAM_NO, String NGAY_DAY, String TRANG_THAI_HOAN_TRA) {
        database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("VI_TTOAN", VI_TTOAN);
        contentValues.put("TRANG_THAI_TTOAN", TRANG_THAI_TTOAN.getCode());
        contentValues.put("TRANG_THAI_CHAM_NO", TRANG_THAI_CHAM_NO);
        contentValues.put("TRANG_THAI_DAY_CHAM_NO", TRANG_THAI_DAY_CHAM_NO);
        contentValues.put("NGAY_DAY", NGAY_DAY);
        contentValues.put("TRANG_THAI_HOAN_TRA", TRANG_THAI_HOAN_TRA);
        database.update(TABLE_NAME_DEBT_COLLECTION, contentValues, "MA_HOA_DON = ?", new String[]{MA_HOA_DON});
    }


    public void updateHoaDonThu(String MA_HOA_DON, Common.TRANG_THAI_HUY TRANG_THAI_HUY, String LyDo) {
        database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("TRANG_THAI_HUY", TRANG_THAI_HUY.getCode());
        database.update(TABLE_NAME_DEBT_COLLECTION, contentValues, "MA_HOA_DON = ?", new String[]{MA_HOA_DON});
    }


    public Common.TRANG_THAI_TTOAN getTrangThaiThanhToanHoaDonNo(String MA_HOA_DON) {
        database = this.getReadableDatabase();
        String query = "SELECT TRANG_THAI_TTOAN FROM " + TABLE_NAME_BILL + " WHERE E_DONG = '" + MainActivity.mEdong + "' and MA_HOA_DON = '" + MA_HOA_DON + "'";
        Cursor mCursor = database.rawQuery(query, null);
        if (mCursor != null && mCursor.moveToFirst()) {
            Common.TRANG_THAI_TTOAN result = Common.TRANG_THAI_TTOAN.findCodeMessage(mCursor.getString(mCursor.getColumnIndex("TRANG_THAI_TTOAN")));
            if (mCursor != null && !mCursor.isClosed()) {
                mCursor.close();
            }
            return result;
        }


        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }

        return Common.TRANG_THAI_TTOAN.NULL;
    }


    public long updateHoaDonNo(long billID, String status, String edong) {
        database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("TRANG_THAI_TTOAN", status);
        contentValues.put("VI_TTOAN", edong);
        return database.update(TABLE_NAME_BILL, contentValues, "MA_HOA_DON = ?", new String[]{String.valueOf(billID)});
    }

    public long updateHoaDonNo(long billID, String edong) {
        database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("VI_TTOAN", edong);
        return database.update(TABLE_NAME_BILL, contentValues, "MA_HOA_DON = ?", new String[]{String.valueOf(billID)});
    }

    public EntityHoaDonNo getHoaDonNo(long billID) {
            EntityHoaDonNo entityHoaDonNo = new EntityHoaDonNo();
            database = this.getReadableDatabase();
            String query = "SELECT * FROM " + TABLE_NAME_BILL + " WHERE MA_HOA_DON ='" + billID + "'";
            Cursor c = database.rawQuery(query, null);

            if (c.moveToFirst()) {
                entityHoaDonNo.setE_DONG(c.getString(c.getColumnIndex("E_DONG")));
                entityHoaDonNo.setMA_HOA_DON(c.getString(c.getColumnIndex("MA_HOA_DON")));
                entityHoaDonNo.setSERI_HDON(c.getString(c.getColumnIndex("SERI_HDON")));
                entityHoaDonNo.setMA_KHANG(c.getString(c.getColumnIndex("MA_KHANG")));
                entityHoaDonNo.setMA_THE(c.getString(c.getColumnIndex("MA_THE")));
                entityHoaDonNo.setTEN_KHANG(c.getString(c.getColumnIndex("TEN_KHANG")));
                entityHoaDonNo.setDIA_CHI(c.getString(c.getColumnIndex("DIA_CHI")));
                entityHoaDonNo.setTHANG_TTOAN(Common.parseDate(c.getString(c.getColumnIndex("THANG_TTOAN")), Common.DATE_TIME_TYPE.FULL.toString()));
                entityHoaDonNo.setPHIEN_TTOAN(c.getInt(c.getColumnIndex("PHIEN_TTOAN")));
                entityHoaDonNo.setSO_TIEN_TTOAN(c.getInt(c.getColumnIndex("SO_TIEN_TTOAN")));
                entityHoaDonNo.setSO_GCS(c.getString(c.getColumnIndex("SO_GCS")));
                entityHoaDonNo.setDIEN_LUC(c.getString(c.getColumnIndex("DIEN_LUC")));
                entityHoaDonNo.setSO_HO(c.getString(c.getColumnIndex("SO_HO")));
                entityHoaDonNo.setSO_DAU_KY(c.getString(c.getColumnIndex("SO_DAU_KY")));
                entityHoaDonNo.setSO_CUOI_KY(c.getString(c.getColumnIndex("SO_CUOI_KY")));
                entityHoaDonNo.setSO_CTO(c.getString(c.getColumnIndex("SO_CTO")));
                entityHoaDonNo.setSDT_ECPAY(c.getString(c.getColumnIndex("SDT_ECPAY")));
                entityHoaDonNo.setSDT_EVN(c.getString(c.getColumnIndex("SDT_EVN")));
                entityHoaDonNo.setGIAO_THU(c.getString(c.getColumnIndex("GIAO_THU")));
                entityHoaDonNo.setNGAY_GIAO_THU(Common.parseDate(c.getString(c.getColumnIndex("NGAY_GIAO_THU")), Common.DATE_TIME_TYPE.FULL.toString()));
                entityHoaDonNo.setTRANG_THAI_TTOAN(c.getString(c.getColumnIndex("TRANG_THAI_TTOAN")));
                entityHoaDonNo.setVI_TTOAN(c.getString(c.getColumnIndex("VI_TTOAN")));

            }


            if (c != null && !c.isClosed()) {
                c.close();
            }
            return entityHoaDonNo;
    }


    public EntityHoaDonThu getHoaDonThu(long billID) {
        EntityHoaDonThu hoaDonThu = new EntityHoaDonThu();

        database = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_DEBT_COLLECTION + " WHERE MA_HOA_DON ='" + billID + "'";
        Cursor mCursor = database.rawQuery(query, null);

        if (mCursor.moveToFirst()) {




            hoaDonThu.setE_DONG(mCursor.getString(mCursor.getColumnIndex("E_DONG")));
            hoaDonThu.setSERI_HDON(mCursor.getString(mCursor.getColumnIndex("SERI_HDON")));
            hoaDonThu.setMA_HOA_DON(mCursor.getString(mCursor.getColumnIndex("MA_HOA_DON")));
            hoaDonThu.setMA_KHANG(mCursor.getString(mCursor.getColumnIndex("MA_KHANG")));
            hoaDonThu.setMA_THE(mCursor.getString(mCursor.getColumnIndex("MA_THE")));
            hoaDonThu.setTEN_KHANG(mCursor.getString(4));
            hoaDonThu.setDIA_CHI(mCursor.getString(mCursor.getColumnIndex("DIA_CHI")));
            hoaDonThu.setTHANG_TTOAN(Common.parseDate(mCursor.getString(mCursor.getColumnIndex("THANG_TTOAN")), Common.DATE_TIME_TYPE.FULL.toString()));
            hoaDonThu.setPHIEN_TTOAN(mCursor.getInt(mCursor.getColumnIndex("PHIEN_TTOAN")));
            hoaDonThu.setSO_TIEN_TTOAN(mCursor.getInt(mCursor.getColumnIndex("SO_TIEN_TTOAN")));
            hoaDonThu.setSO_GCS(mCursor.getString(mCursor.getColumnIndex("SO_GCS")));
            hoaDonThu.setDIEN_LUC(mCursor.getString(mCursor.getColumnIndex("DIEN_LUC")));
            hoaDonThu.setSO_HO(mCursor.getString(mCursor.getColumnIndex("SO_HO")));
            hoaDonThu.setSO_DAU_KY(mCursor.getString(mCursor.getColumnIndex("SO_DAU_KY")));
            hoaDonThu.setSO_CUOI_KY(mCursor.getString(mCursor.getColumnIndex("SO_CUOI_KY")));
            hoaDonThu.setSO_CTO(mCursor.getString(mCursor.getColumnIndex("SO_CTO")));
            hoaDonThu.setSDT_ECPAY(mCursor.getString(mCursor.getColumnIndex("SDT_ECPAY")));
            hoaDonThu.setSDT_EVN(mCursor.getString(mCursor.getColumnIndex("SDT_EVN")));
            hoaDonThu.setGIAO_THU(mCursor.getString(mCursor.getColumnIndex("GIAO_THU")));
            hoaDonThu.setNGAY_GIAO_THU(Common.parseDate(mCursor.getString(mCursor.getColumnIndex("NGAY_GIAO_THU")), Common.DATE_TIME_TYPE.FULL.toString()));
            hoaDonThu.setTRANG_THAI_TTOAN(mCursor.getString(mCursor.getColumnIndex("TRANG_THAI_TTOAN")));
            hoaDonThu.setVI_TTOAN(mCursor.getString(mCursor.getColumnIndex("VI_TTOAN")));
            hoaDonThu.setNGAY_THU(Common.parseDate(mCursor.getString(mCursor.getColumnIndex("NGAY_THU")), Common.DATE_TIME_TYPE.FULL.toString()));
            hoaDonThu.setHINH_THUC_TT(mCursor.getString(mCursor.getColumnIndex("HINH_THUC_TT")));
            hoaDonThu.setTRANG_THAI_CHAM_NO(mCursor.getString(mCursor.getColumnIndex("TRANG_THAI_CHAM_NO")));
            hoaDonThu.setTRANG_THAI_HUY(mCursor.getString(mCursor.getColumnIndex("TRANG_THAI_HUY")));
            hoaDonThu.setTRANG_THAI_HOAN_TRA(mCursor.getString(mCursor.getColumnIndex("TRANG_THAI_HOAN_TRA")));
            hoaDonThu.setTRANG_THAI_XU_LY_NGHI_NGO(mCursor.getString(mCursor.getColumnIndex("TRANG_THAI_XU_LY_NGHI_NGO")));
            hoaDonThu.setTRANG_THAI_DAY_CHAM_NO(mCursor.getString(mCursor.getColumnIndex("TRANG_THAI_DAY_CHAM_NO")));

            hoaDonThu.setNGAY_DAY(Common.parseDate(mCursor.getString(mCursor.getColumnIndex("NGAY_DAY")), Common.DATE_TIME_TYPE.FULL.toString()));
            hoaDonThu.setSO_LAN_IN_BIEN_NHAN(mCursor.getInt(mCursor.getColumnIndex("SO_LAN_IN_BIEN_NHAN")));
            hoaDonThu.setIN_THONG_BAO_DIEN(mCursor.getString(mCursor.getColumnIndex("IN_THONG_BAO_DIEN")));


        }


        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }
        return hoaDonThu;
    }

    public Pair<List<EntityKhachHang>, Integer> selectAllCustomerFitterBy(String edong, int startIndex, Common.TYPE_SEARCH typeSearch, String infoSearch) {

        List<EntityKhachHang> customerList = new ArrayList<>();
        int totalRow = 0;
        boolean fail = TextUtils.isEmpty(edong);
        if (fail)
            return new Pair<>(customerList, totalRow);

        String query = "";
        String whereQuery1 = "";
        String whereQuery2 = "";
        switch (typeSearch.getPosition()) {
            case 0:
                whereQuery1 =  " t1.E_DONG = '" + edong + "'";
                whereQuery2 =  " t2.E_DONG = '" + edong + "'";
                break;
            case 1:
                whereQuery1 =  " t1.E_DONG = '" + edong + "' and t1.MA_KHANG like '%" + infoSearch + "%'";
                whereQuery2 =  " t2.E_DONG = '" + edong + "' and t2.MA_KHANG like '%" + infoSearch + "%'";
                break;
            case 2:
                whereQuery1 =  " t1.E_DONG = '" + edong + "' and t1.TEN_KHANG like '%" + infoSearch + "%'";
                whereQuery2 =  " t2.E_DONG = '" + edong + "' and t2.TEN_KHANG like '%" + infoSearch + "%'";
                break;
            case 3:
                whereQuery1 =  " t1.E_DONG = '" + edong + "' and t1.SDT_EVN like '%" + infoSearch + "%'";
                whereQuery2 =  " t2.E_DONG = '" + edong + "' and t2.SDT_EVN like '%" + infoSearch + "%'";
                break;
            case 4:
                whereQuery1 =  " t1.E_DONG = '" + edong + "' and t1.DIA_CHI like '%" + infoSearch + "%'";
                whereQuery2 =  " t2.E_DONG = '" + edong + "' and t2.DIA_CHI like '%" + infoSearch + "%'";
                break;
            case 5:
                whereQuery1 =  " t1.E_DONG = '" + edong + "' and t1.SO_GCS like '%" + infoSearch + "%'";
                whereQuery2 =  " t2.E_DONG = '" + edong + "' and t2.SO_GCS like '%" + infoSearch + "%'";
                break;
            case 6:
                whereQuery1 =  " t1.E_DONG = '" + edong + "' and t1.LO_TRINH like '%" + infoSearch + "%'";
                whereQuery2 =  " t2.E_DONG = '" + edong + "' and t2.LO_TRINH like '%" + infoSearch + "%'";
                break;
            default:
                whereQuery1 = " t1.E_DONG = '" + edong + "'";
                whereQuery2 = " t2.E_DONG = '" + edong + "'";
        }


        query = "SELECT * FROM (select (select COUNT(*) " +
                "                from " + TABLE_NAME_CUSTOMER + " t1 " +
                "                where t1.ID <= t2.ID and " + whereQuery1 +
                "                ) as Row_Number, (select count(*) from " + TABLE_NAME_CUSTOMER + " t1 WHERE  " + whereQuery1 + ") as TONG_ROW, * from " + TABLE_NAME_CUSTOMER + " t2 WHERE " + whereQuery2 + " ORDER BY ID) " +
                "                  WHERE Row_Number BETWEEN " + ((startIndex - 1) * PayFragment.ROWS_ON_PAGE + 1) + " AND " + ((startIndex) * PayFragment.ROWS_ON_PAGE);

        database = this.getWritableDatabase();
        Cursor c = database.rawQuery(query, null);

        if (c != null && c.moveToFirst()) {
            int count = c.getCount();
            do {

                totalRow = c.getInt(c.getColumnIndex("TONG_ROW"));
                EntityKhachHang khachHang = new EntityKhachHang();
                khachHang.setE_DONG(c.getString(c.getColumnIndex("E_DONG")));
                khachHang.setMA_KHANG(c.getString(c.getColumnIndex("MA_KHANG")));
                khachHang.setMA_THE(c.getString(c.getColumnIndex("MA_THE")));
                khachHang.setTEN_KHANG(c.getString(c.getColumnIndex("TEN_KHANG")));
                khachHang.setDIA_CHI(c.getString(c.getColumnIndex("DIA_CHI")));
                khachHang.setPHIEN_TTOAN(c.getString(c.getColumnIndex("PHIEN_TTOAN")));
                khachHang.setLO_TRINH(c.getString(c.getColumnIndex("LO_TRINH")));
                khachHang.setSO_GCS(c.getString(c.getColumnIndex("SO_GCS")));
                khachHang.setDIEN_LUC(c.getString(c.getColumnIndex("DIEN_LUC")));
                khachHang.setSO_HO(c.getString(c.getColumnIndex("SO_HO")));
                khachHang.setSDT_ECPAY(c.getString(c.getColumnIndex("SDT_ECPAY")));
                khachHang.setSDT_EVN(c.getString(c.getColumnIndex("SDT_EVN")));
                khachHang.setGIAO_THU(c.getString(c.getColumnIndex("GIAO_THU")));
                khachHang.setNGAY_GIAO_THU(Common.parseDate(c.getString(c.getColumnIndex("NGAY_GIAO_THU")), Common.DATE_TIME_TYPE.FULL.toString()));

                customerList.add(khachHang);
            }
            while (c.moveToNext());

        }

        if(c != null)
        {
            c.close();
        }
        return new Pair<>(customerList, totalRow);
    }


    public List<EntityKhachHang> selectAllCustomerFitter(String _maKH, String _name, String _address, String _phone, String _bookCmis) {
        List<EntityKhachHang> customerList = new ArrayList<>();


        String query = "SELECT * FROM " + TABLE_NAME_CUSTOMER + " WHERE ";

        boolean hasWhere = false;

        if (_maKH.length() != 0) {
            hasWhere = true;
            query += "(MA_KHANG like '%" + _maKH + "%' or ";
            query += "MA_THE like '%" + _maKH + "%') ";
        }

        if (_name.length() > 0) {
            query += (hasWhere ? "and " : "") + "TEN_KHANG like '%" + _name + "%' ";
            hasWhere = true;
        }

        if (_address.length() > 0) {
            query += (hasWhere ? "and " : "") + "DIA_CHI like '%" + _address + "%' ";
            hasWhere = true;
        }

        if (_phone.length() > 0) {
            query += (hasWhere ? "and " : "") + "SDT_ECPAY like '%" + _phone + "%' ";
            hasWhere = true;
        }

        if (_bookCmis.length() > 0) {
            query += (hasWhere ? "and " : "") + "SO_GCS like '%" + _bookCmis + "%' ";
            hasWhere = true;
        }

        database = this.getWritableDatabase();
        Cursor mCursor = database.rawQuery(query, null);

        if (mCursor != null && mCursor.moveToFirst()) {
            int count = mCursor.getCount();
            do {
                EntityKhachHang customer = new EntityKhachHang();
                customer.setMA_KHANG(stringConvertNull(mCursor.getString(mCursor.getColumnIndex("MA_KHANG"))));
                customer.setE_DONG(stringConvertNull(mCursor.getString(mCursor.getColumnIndex("E_DONG"))));
                customer.setMA_THE(stringConvertNull(mCursor.getString(mCursor.getColumnIndex("MA_THE"))));
                customer.setTEN_KHANG(stringConvertNull(mCursor.getString(mCursor.getColumnIndex("TEN_KHANG"))));
                customer.setDIA_CHI(stringConvertNull(mCursor.getString(mCursor.getColumnIndex("DIA_CHI"))));
                customer.setPHIEN_TTOAN(stringConvertNull(mCursor.getString(mCursor.getColumnIndex("PHIEN_TTOAN"))));
                customer.setLO_TRINH(stringConvertNull(mCursor.getString(mCursor.getColumnIndex("LO_TRINH"))));
                customer.setSO_GCS(stringConvertNull(mCursor.getString(mCursor.getColumnIndex("SO_GCS"))));
                customer.setDIEN_LUC(stringConvertNull(mCursor.getString(mCursor.getColumnIndex("DIEN_LUC"))));
                customer.setSO_HO(stringConvertNull(mCursor.getString(mCursor.getColumnIndex("SO_HO"))));
                customer.setSDT_ECPAY(stringConvertNull(mCursor.getString(mCursor.getColumnIndex("SDT_ECPAY"))));
                customer.setSDT_EVN(stringConvertNull(mCursor.getString(mCursor.getColumnIndex("SDT_EVN"))));
                customer.setGIAO_THU(stringConvertNull(mCursor.getString(mCursor.getColumnIndex("GIAO_THU"))));
                customer.setNGAY_GIAO_THU(Common.parseDate(stringConvertNull(mCursor.getString(mCursor.getColumnIndex("NGAY_GIAO_THU"))), Common.DATE_TIME_TYPE.FULL.toString()));


                customerList.add(customer);
            }
            while (mCursor.moveToNext());

        }

        if(mCursor != null)
        {
            mCursor.close();
        }
        return customerList;
    }


    public  List<PayAdapter.BillEntityAdapter> getBillSelectedToPay(String [] billids)
    {
        List<PayAdapter.BillEntityAdapter> billList = new ArrayList<>();

        if(billids.length > 0)
        {
            database = this.getReadableDatabase();
            String query = "SELECT * FROM " + TABLE_NAME_BILL + " WHERE E_DONG = '" + MainActivity.mEdong + "' and TRANG_THAI_TTOAN = '" + Common.TRANG_THAI_TTOAN.CHUA_TTOAN.getCode() + "' and (";

            for (int i = 0, n = billids.length; i < n; i ++)
            {
                query += (i == 0 ? " MA_HOA_DON = '" : " or MA_HOA_DON = '") + billids[i] + "' ";
            }

            query += ")";

            query = "SELECT * FROM " + TABLE_NAME_CUSTOMER + " A INNER JOIN ( " + query + ") B ON A.MA_KHANG = B.MA_KHANG";

            Cursor mCursor = database.rawQuery(query, null);
            if (mCursor.getCount() == 0)
            {
                mCursor.close();
                return billList;
            }
            if (mCursor.moveToFirst()) {
                do {
                    String viThanhToan = mCursor.getString(mCursor.getColumnIndex("VI_TTOAN"));
                    int billId = intConvertNull(mCursor.getInt(mCursor.getColumnIndex("MA_HOA_DON")));
                    int amount = intConvertNull(mCursor.getInt(mCursor.getColumnIndex("SO_TIEN_TTOAN")));
                    String status = mCursor.getString(mCursor.getColumnIndex("TRANG_THAI_TTOAN"));

                    String dateRequest = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("NGAY_GIAO_THU")));

                    PayAdapter.BillEntityAdapter bill = new PayAdapter.BillEntityAdapter();
                    bill.setBillId(billId);
                    bill.setVI_TTOAN(viThanhToan);
                    bill.setTIEN_THANH_TOAN(amount);
                    bill.setTEN_KHACH_HANG(mCursor.getString(4));
                    bill.setTHANG_THANH_TOAN(Common.parseDate(mCursor.getString(mCursor.getColumnIndex("THANG_TTOAN")), Common.DATE_TIME_TYPE.FULL.toString()));
                    bill.setTRANG_THAI_TT(status);
                    bill.setMA_DIEN_LUC(mCursor.getString(mCursor.getColumnIndex("DIEN_LUC")));
                    bill.setChecked(true);
                    bill.setMA_KHACH_HANG(mCursor.getString(mCursor.getColumnIndex("MA_KHANG")));

                    bill.setCheckEnable(!bill.getTRANG_THAI_TT().equalsIgnoreCase(Common.STATUS_BILLING.DA_THANH_TOAN.getCode()));

                    if (bill.getTRANG_THAI_TT().equalsIgnoreCase(Common.STATUS_BILLING.DA_THANH_TOAN.getCode()) && MainActivity.mEdong.equalsIgnoreCase(viThanhToan))
                        bill.setPrintEnable(true);
                    else
                        bill.setPrintEnable(false);

                    bill.setRequestDate(dateRequest);

                    billList.add(bill);
                }
                while (mCursor.moveToNext());
            }


            if (mCursor != null && !mCursor.isClosed()) {
                mCursor.close();
            }
        }

        return billList;
    }

    public Pair<List<PayAdapter.BillEntityAdapter>, Long> selectInfoBillOfCustomerToRecycler(String edong, String code) {

        List<PayAdapter.BillEntityAdapter> billList = new ArrayList<>();

        database = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_BILL + " WHERE E_DONG = '" + edong + "' and MA_KHANG ='" + code + "' ORDER BY THANG_TTOAN DESC";
        Cursor mCursor = database.rawQuery(query, null);

        long total = 0;
        if (mCursor.getCount() == 0)
        {
            mCursor.close();
            return new Pair<>(billList, total);
        }
        if (mCursor.moveToFirst()) {
            do {;
                String viThanhToan = mCursor.getString(mCursor.getColumnIndex("VI_TTOAN"));
                String chiTietKhungGia = mCursor.getString(mCursor.getColumnIndex("CHI_TIET_KG"));
                String chiTietMucChiSo = mCursor.getString(mCursor.getColumnIndex("CHI_TIET_MCS"));
                String chiTietTienTheoChiSo = mCursor.getString(mCursor.getColumnIndex("CHI_TIET_TIEN_MCS"));
                String dienNangTieuThu = mCursor.getString(mCursor.getColumnIndex("DNTT"));
                String soHo = mCursor.getString(mCursor.getColumnIndex("SO_HO"));
                String soDauKy = mCursor.getString(mCursor.getColumnIndex("SO_DAU_KY"));
                String soCuoiKy = mCursor.getString(mCursor.getColumnIndex("SO_CUOI_KY"));
                String soCto = mCursor.getString(mCursor.getColumnIndex("SO_CTO"));
                int billId = intConvertNull(mCursor.getInt(mCursor.getColumnIndex("MA_HOA_DON")));
                int amount = intConvertNull(mCursor.getInt(mCursor.getColumnIndex("SO_TIEN_TTOAN")));
                int tienChuaThue = intConvertNull(mCursor.getInt(mCursor.getColumnIndex("TONG_TIEN_CHUA_THUE")));
                int tienThue = intConvertNull(mCursor.getInt(mCursor.getColumnIndex("TIEN_THUE")));
                String status = mCursor.getString(mCursor.getColumnIndex("TRANG_THAI_TTOAN"));

                total += amount;

                String dateRequest = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("NGAY_GIAO_THU")));

                PayAdapter.BillEntityAdapter bill = new PayAdapter.BillEntityAdapter();
                bill.setBillId(billId);
                bill.setVI_TTOAN(viThanhToan);
                bill.setTIEN_THANH_TOAN(amount);
                bill.setTHANG_THANH_TOAN(Common.parseDate(mCursor.getString(mCursor.getColumnIndex("THANG_TTOAN")), Common.DATE_TIME_TYPE.FULL.toString()));
                bill.setTRANG_THAI_TT(status);
                bill.setCHI_TIET_KG(chiTietKhungGia);
                bill.setCHI_TIET_MCS(chiTietMucChiSo);
                bill.setCHI_TIET_TIEN_MCS(chiTietTienTheoChiSo);
                bill.setDNTT(dienNangTieuThu);
                bill.setTONG_TIEN_CHUA_THUE(tienChuaThue+"");
                bill.setTONG_TIEN_THUE(tienThue+"");
                bill.setCSDK(soDauKy);
                bill.setCSCK(soCuoiKy);
                bill.setSO_CONG_TO(soCto);
                bill.setSO_HO(soHo);
                bill.setMA_DIEN_LUC(mCursor.getString(mCursor.getColumnIndex("DIEN_LUC")));
                bill.setChecked(false);
                bill.setMA_KHACH_HANG(mCursor.getString(mCursor.getColumnIndex("MA_KHANG")));

                bill.setCheckEnable(!bill.getTRANG_THAI_TT().equalsIgnoreCase(Common.STATUS_BILLING.DA_THANH_TOAN.getCode()));

                if (bill.getTRANG_THAI_TT().equalsIgnoreCase(Common.STATUS_BILLING.DA_THANH_TOAN.getCode()) && MainActivity.mEdong.equalsIgnoreCase(viThanhToan))
                    bill.setPrintEnable(true);
                else
                    bill.setPrintEnable(false);

                bill.setRequestDate(dateRequest);

                billList.add(bill);
            }
            while (mCursor.moveToNext());
        }


        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }
        return new Pair<>(billList, total);
    }

    public int countMoneyAllBill(String edong) {
        database = this.getReadableDatabase();
//        String query = "SELECT  FROM " + TABLE_NAME_BILL + " WHERE E_DONG = '" + edong + "' and GIAO_THU = '" + Common.TRANG_THAI_GIAO_THU.GIAO_THU.getCode() + "' " +
//                " and TRANG_THAI_TTOAN = '" + Common.TRANG_THAI_TTOAN.CHUA_TTOAN.getCode() + "'";// and status = " + ZERO;

        String query = "SELECT SUM(SO_TIEN_TTOAN) FROM(SELECT * FROM " + TABLE_NAME_BILL + " WHERE E_DONG = '" + edong + "' and GIAO_THU ='" + Common.TRANG_THAI_GIAO_THU.GIAO_THU.getCode() + "'  and TRANG_THAI_TTOAN ='" + Common.TRANG_THAI_TTOAN.CHUA_TTOAN.getCode() + "' ) AS BILL " +
                "JOIN (SELECT * FROM " + TABLE_NAME_CUSTOMER + " WHERE E_DONG = '" + edong + "') AS CUSTOMER ON BILL.MA_KHANG = CUSTOMER.MA_KHANG";


        Cursor mCursor = database.rawQuery(query, null);
        int totalMoney = ERROR_OCCUR;
        if (mCursor.moveToFirst())
            totalMoney = mCursor.getInt(0);
        else
            totalMoney = ERROR_OCCUR;

        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }
        return totalMoney;
    }

    public void insertOrUpdateCustomerFromSearchOnline(EntityKhachHang customer) {

        ContentValues initialValues = new ContentValues();

        initialValues.put("MA_KHANG", customer.getMA_KHANG());
        initialValues.put("TEN_KHANG", customer.getTEN_KHANG());
        initialValues.put("MA_THE", customer.getMA_THE());
        initialValues.put("DIA_CHI", customer.getDIA_CHI());
        initialValues.put("PHIEN_TTOAN", customer.getPHIEN_TTOAN());
        initialValues.put("LO_TRINH", customer.getLO_TRINH());
        initialValues.put("SO_GCS", customer.getSO_GCS());
        initialValues.put("DIEN_LUC", customer.getDIEN_LUC());
        initialValues.put("SO_HO", "");
        initialValues.put("E_DONG", customer.getE_DONG());
        initialValues.put("SDT_ECPAY", customer.getSDT_ECPAY());
        initialValues.put("SDT_EVN", customer.getSDT_EVN());
        initialValues.put("NGAY_GIAO_THU", Common.parse(customer.getNGAY_GIAO_THU(), Common.DATE_TIME_TYPE.FULL.toString()));
        database = getWritableDatabase();

        String query = "SELECT * FROM " + TABLE_NAME_CUSTOMER + " WHERE MA_KHANG = '" + customer.getMA_KHANG() + "'";
        Cursor mCursor = database.rawQuery(query, null);
        if (mCursor.moveToFirst()) {
            database.update(TABLE_NAME_CUSTOMER, initialValues, "MA_KHANG=?", new String[]{String.valueOf(customer.getMA_KHANG())});
        } else {

            initialValues.put("GIAO_THU", customer.getGIAO_THU());
            database.insert(TABLE_NAME_CUSTOMER, null, initialValues);
        }


        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }
    }

    public void insertOrUpdateBillSearchOnline(String edong, BillInsideCustomer bodyBillResponse) {

        ContentValues initialValues = new ContentValues();

        initialValues.put("MA_KHANG", bodyBillResponse.getCustomerCode());
        initialValues.put("E_DONG", edong);
        initialValues.put("MA_HOA_DON", bodyBillResponse.getBillId());
        initialValues.put("SERI_HDON", bodyBillResponse.getSeri());


        initialValues.put("MA_THE", bodyBillResponse.getCardNo());
        initialValues.put("TEN_KHANG", bodyBillResponse.getName());
        initialValues.put("DIA_CHI", bodyBillResponse.getAddress());

        initialValues.put("THANG_TTOAN", Common.parse(Common.parseDate(Common.convertToDate(bodyBillResponse.getTerm()), Common.DATE_TIME_TYPE.yyyyMMddHHmmssSSS.toString()), Common.DATE_TIME_TYPE.FULL.toString()));
        initialValues.put("PHIEN_TTOAN", Common.parseInt(bodyBillResponse.getPeriod()));
        initialValues.put("SO_TIEN_TTOAN", bodyBillResponse.getAmount());
        initialValues.put("SO_GCS", bodyBillResponse.getBookCmis());
        initialValues.put("DIEN_LUC", bodyBillResponse.getPcCode());
        initialValues.put("SO_HO", bodyBillResponse.getHome());

        initialValues.put("SO_DAU_KY", bodyBillResponse.getOldIndex());
        initialValues.put("SO_CUOI_KY", bodyBillResponse.getNewIndex());
        initialValues.put("SO_CTO", bodyBillResponse.getElectricityMeter());
        initialValues.put("SDT_ECPAY", bodyBillResponse.getPhoneByecp());
        initialValues.put("SDT_EVN", bodyBillResponse.getPhoneByevn());
        initialValues.put("NGAY_GIAO_THU", Common.getDateTimeNow(Common.DATE_TIME_TYPE.FULL));
        initialValues.put("TRANG_THAI_TTOAN", Common.TRANG_THAI_TTOAN.CHUA_TTOAN.getCode());
        initialValues.put("VI_TTOAN", "");
        initialValues.put("CHI_TIET_KG", bodyBillResponse.getPriceDetails());
        initialValues.put("CHI_TIET_MCS", bodyBillResponse.getNumeDetails());
        initialValues.put("CHI_TIET_TIEN_MCS", bodyBillResponse.getAmountDetails());
        initialValues.put("DNTT", bodyBillResponse.getNume());
        initialValues.put("TONG_TIEN_CHUA_THUE", bodyBillResponse.getAmountNotTax());
        initialValues.put("TIEN_THUE",bodyBillResponse.getAmountTax());

        database = getWritableDatabase();


        String query = "SELECT * FROM " + TABLE_NAME_BILL + " WHERE MA_HOA_DON = '" + bodyBillResponse.getBillId() + "'";
        Cursor mCursor = database.rawQuery(query, null);
        if (mCursor.moveToFirst()) {
            database.update(TABLE_NAME_BILL, initialValues, "MA_HOA_DON=?", new String[]{String.valueOf(bodyBillResponse.getBillId())});
        } else {
            initialValues.put("GIAO_THU", Common.TRANG_THAI_GIAO_THU.VANG_LAI.getCode());
            database.insert(TABLE_NAME_BILL, null, initialValues);
        }


        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }

    }


    public String selectSessionAccount(String edong) {
        if (TextUtils.isEmpty(edong))
            return null;

        String query = "SELECT session FROM " + TABLE_NAME_ACCOUNT + " WHERE edong = '" + edong + "'";
        String session = "";

        database = this.getReadableDatabase();
        Cursor mCursor = database.rawQuery(query, null);
        mCursor.moveToFirst();

        int count = mCursor.getCount();
        //get first value
        if (count != Common.ZERO)
            session = mCursor.getString(mCursor.getColumnIndex("session"));

        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }
        return session;
    }


    //region EVN
    public void insertOrUpdateEvnPcFromLoginReponse(EvnPC evnPC, String edong) {
        if (evnPC == null)
            return;

        ContentValues initialValues = new ContentValues();
        initialValues.put("pcId", evnPC.getPcId());
        initialValues.put("parentId", evnPC.getParentId());
        initialValues.put("code", evnPC.getCode());
        initialValues.put("ext", evnPC.getExt());
        initialValues.put("fullName", evnPC.getFullName());
        initialValues.put("shortName", evnPC.getShortName());
        initialValues.put("address", evnPC.getAddress());
        initialValues.put("taxCode", evnPC.getTaxCode());
        initialValues.put("phone1", evnPC.getPhone1());
        initialValues.put("phone2", evnPC.getPhone2());
        initialValues.put("fax", evnPC.getFax());
        initialValues.put("level", evnPC.getLevel());
        initialValues.put("edong", edong);

        database = getWritableDatabase();
        int id = (int) database.insertWithOnConflict(TABLE_NAME_EVN_PC, null, initialValues, SQLiteDatabase.CONFLICT_IGNORE);
        if (id == -1) {
            database.update(TABLE_NAME_EVN_PC, initialValues, "pcId=? and edong=?", new String[]{String.valueOf(evnPC.getPcId()), edong});
        }
    }

    public long insertEvnPC(ListEvnPCResponse listEvnPCResponse, String edong) {
        ContentValues initialValues = new ContentValues();
        initialValues.put("parentId", String.valueOf(listEvnPCResponse.getParentId()));
        initialValues.put("strParentId", listEvnPCResponse.getStrParentId());
        initialValues.put("pcId", listEvnPCResponse.getPcId());
        initialValues.put("strPcId", listEvnPCResponse.getStrPcId() + "");
        initialValues.put("code", listEvnPCResponse.getCode());
        initialValues.put("ext", listEvnPCResponse.getExt());
        initialValues.put("fullName", listEvnPCResponse.getFullName());
        initialValues.put("shortName", listEvnPCResponse.getShortName());
        initialValues.put("address", listEvnPCResponse.getAddress());
        initialValues.put("taxCode", listEvnPCResponse.getTaxCode());
        initialValues.put("phone1", listEvnPCResponse.getPhone1());
        initialValues.put("phone2", listEvnPCResponse.getPhone2());
        initialValues.put("fax", listEvnPCResponse.getFax());
        initialValues.put("level", listEvnPCResponse.getLevel());
        initialValues.put("strLevel", listEvnPCResponse.getStrLevel());
        initialValues.put("mailTo", listEvnPCResponse.getMailTo());
        initialValues.put("mailCc", listEvnPCResponse.getMailCc());
        initialValues.put("status", listEvnPCResponse.getStatus());
        initialValues.put("strStatus", listEvnPCResponse.getStrStatus());
        initialValues.put("dateCreated", listEvnPCResponse.getDateCreated());
        initialValues.put("strDateCreated", listEvnPCResponse.getStrDateCreated());
        initialValues.put("idChanged", listEvnPCResponse.getIdChanged());
        initialValues.put("dateChanged", listEvnPCResponse.getDateChanged());
        initialValues.put("strDateChanged", listEvnPCResponse.getStrDateChanged());
        initialValues.put("regionId", listEvnPCResponse.getRegionId());
        initialValues.put("parentPcCode", listEvnPCResponse.getParentPcCode());
        initialValues.put("cardPrefix", listEvnPCResponse.getCardPrefix() + "");
        initialValues.put("edong", edong);
        int rowAffect = -1;
        database = getWritableDatabase();
        try {
            rowAffect = (int) database.insertOrThrow(TABLE_NAME_EVN_PC, null, initialValues);
        } catch (Exception e) {
            Log.e(TAG, "insertEvnPC: " + e.getMessage());
        }
        return rowAffect;
    }

    public long updateEvnPC(ListEvnPCResponse listEvnPCResponse, String edong) {
        ContentValues initialValues = new ContentValues();

        initialValues.put("parentId", listEvnPCResponse.getParentId());
        initialValues.put("strParentId", listEvnPCResponse.getStrParentId());
        initialValues.put("pcId", listEvnPCResponse.getPcId());
        initialValues.put("strPcId", listEvnPCResponse.getStrPcId());
        initialValues.put("code", listEvnPCResponse.getCode());
        initialValues.put("ext", listEvnPCResponse.getExt());
        initialValues.put("fullName", listEvnPCResponse.getFullName());
        initialValues.put("shortName", listEvnPCResponse.getShortName());
        initialValues.put("address", listEvnPCResponse.getAddress());
        initialValues.put("taxCode", listEvnPCResponse.getTaxCode());
        initialValues.put("phone1", listEvnPCResponse.getPhone1());
        initialValues.put("phone2", listEvnPCResponse.getPhone2());
        initialValues.put("fax", listEvnPCResponse.getFax());
        initialValues.put("level", listEvnPCResponse.getLevel());
        initialValues.put("strLevel", listEvnPCResponse.getStrLevel());
        initialValues.put("mailTo", listEvnPCResponse.getMailTo());
        initialValues.put("mailCc", listEvnPCResponse.getMailCc());
        initialValues.put("status", listEvnPCResponse.getStatus());
        initialValues.put("strStatus", listEvnPCResponse.getStrStatus());
        initialValues.put("dateCreated", listEvnPCResponse.getDateCreated());
        initialValues.put("strDateCreated", listEvnPCResponse.getStrDateCreated());
        initialValues.put("idChanged", listEvnPCResponse.getIdChanged());
        initialValues.put("dateChanged", listEvnPCResponse.getDateChanged());
        initialValues.put("strDateChanged", listEvnPCResponse.getStrDateChanged());
        initialValues.put("regionId", listEvnPCResponse.getRegionId());
        initialValues.put("parentPcCode", listEvnPCResponse.getParentPcCode());
        initialValues.put("cardPrefix", listEvnPCResponse.getCardPrefix());
        initialValues.put("edong", edong);
        database = getWritableDatabase();
        int rowAffect = (int) database.update(TABLE_NAME_EVN_PC, initialValues, "pcId=? and edong=?", new String[]{String.valueOf(listEvnPCResponse.getPcId()), edong});
        return rowAffect;
    }

    public long deleteAllPC(String edong) {
        database = this.getWritableDatabase();
        return database.delete(TABLE_NAME_EVN_PC, "edong=?", new String[]{edong});
    }

    public long checkEvnPCExist(int pcId, String edong) {
        database = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + TABLE_NAME_EVN_PC + " WHERE pcId = " + pcId + " and edong = " + edong;
        Cursor mCursor = database.rawQuery(query, null);
        if (mCursor.moveToFirst())
            return mCursor.getInt(0);
        return 0;
    }

    public String getPcCode(String edong) {
        database = this.getReadableDatabase();
        String query = "SELECT ext FROM " + TABLE_NAME_EVN_PC + " Where edong = " + edong;
        Cursor c = database.rawQuery(query, null);
        if (c.moveToFirst()) {
            return c.getString(0);
        }
        return "";
    }

    public List<String> getPcCodes(String edong) {

        List<String> result = new ArrayList<>();

        database = this.getReadableDatabase();
        String query = "SELECT ext FROM " + TABLE_NAME_EVN_PC + " where edong =" + edong;
        Cursor c = database.rawQuery(query, null);
        if (c.moveToFirst()) {
            do {
                result.add(c.getString(0));
            } while (c.moveToNext());
        }
        return result;
    }
    //endregion

    //region BOOK CMIS
    public long insertBookCmis(ListBookCmisResponse listBookCmisResponse) {
        ContentValues initialValues = new ContentValues();

        initialValues.put("bookCmis", listBookCmisResponse.getBookCmis());
        initialValues.put("pcCode", listBookCmisResponse.getPcCode());
        initialValues.put("pcCodeExt", listBookCmisResponse.getPcCodeExt());
        initialValues.put("inningDate", listBookCmisResponse.getInningDate());
        initialValues.put("email", listBookCmisResponse.getEmail());
        initialValues.put("status", listBookCmisResponse.getStatus());
        initialValues.put("strStatus", listBookCmisResponse.getStrStatus());
        initialValues.put("strCreateDate", listBookCmisResponse.getStrCreateDate());
        initialValues.put("strChangeDate", listBookCmisResponse.getStrChangeDate());
        initialValues.put("idChanged", listBookCmisResponse.getIdChanged());
        initialValues.put("id", listBookCmisResponse.getId());
        initialValues.put("parentPcCode", listBookCmisResponse.getParentPcCode());
        initialValues.put("countBill", listBookCmisResponse.getCountBill());
        initialValues.put("countBillPaid", listBookCmisResponse.getCountBillPaid());
        initialValues.put("countCustomer", listBookCmisResponse.getCountCustomer());
        initialValues.put("listCustomer", listBookCmisResponse.getListCustomer());
        initialValues.put("listBillUnpaid", listBookCmisResponse.getListBillUnpaid());
        initialValues.put("listBillPaid", listBookCmisResponse.getListBillPaid());

        database = getWritableDatabase();
        int rowAffect = (int) database.insert(TABLE_NAME_BOOK_CMIS, null, initialValues);
        return rowAffect;
    }

    public long deleteAllBookCmis() {
        database = this.getWritableDatabase();
        return database.delete(TABLE_NAME_BOOK_CMIS, null, null);
    }

    public long checkBookCmisExist(String bookCmis) {
        database = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + TABLE_NAME_BOOK_CMIS + " WHERE bookCmis = '" + bookCmis + "'";
        Cursor mCursor = database.rawQuery(query, null);
        if (mCursor.moveToFirst())
            return mCursor.getInt(0);
        return 0;
    }

    public Cursor getAllBookCmis() {
        database = this.getReadableDatabase();
        String query = "SELECT bookCmis, pcCodeExt FROM " + TABLE_NAME_BOOK_CMIS;
        return database.rawQuery(query, null);
    }
    //endregion

    //region CUSTOMER
    public long insertCustomer(ListCustomerResponse listCustomerResponse) {
        ContentValues initialValues = new ContentValues();

        BodyCustomerResponse bodyCustomerResponse = listCustomerResponse.getBodyCustomerResponse();
        FooterCustomerResponse footerCustomerResponse = listCustomerResponse.getFooterCustomerResponse();

        initialValues.put("MA_KHANG", bodyCustomerResponse.getCustomerCode());
        initialValues.put("TEN_KHANG", bodyCustomerResponse.getName());
        initialValues.put("MA_THE", bodyCustomerResponse.getCardNo());
        initialValues.put("DIA_CHI", bodyCustomerResponse.getAddress());
        initialValues.put("PHIEN_TTOAN", bodyCustomerResponse.getInning());
        initialValues.put("LO_TRINH", bodyCustomerResponse.getRoad());
        initialValues.put("SO_GCS", bodyCustomerResponse.getBookCmis());
        initialValues.put("DIEN_LUC", bodyCustomerResponse.getPcCode());
        initialValues.put("SO_HO", "");
        initialValues.put("E_DONG", MainActivity.mEdong);
        initialValues.put("SDT_ECPAY", bodyCustomerResponse.getPhoneByECP());
        initialValues.put("SDT_EVN", bodyCustomerResponse.getPhoneByEVN());
        initialValues.put("GIAO_THU", Common.TRANG_THAI_GIAO_THU.GIAO_THU.getCode());
        initialValues.put("NGAY_GIAO_THU", Common.getDateTimeNow(Common.DATE_TIME_TYPE.FULL));
        database = getWritableDatabase();
        int rowAffect = (int) database.insert(TABLE_NAME_CUSTOMER, null, initialValues);
        return rowAffect;
    }

    public long insertCustomer(CustomerResponse customerResponse) {
        ContentValues initialValues = new ContentValues();

        views.ecpay.com.postabletecpay.util.entities.response.EntityCustomer.BodyCustomerResponse bodyCustomerResponse = customerResponse.getBodyCustomerResponse();

        initialValues.put("MA_KHANG", bodyCustomerResponse.getCustomerCode());
        initialValues.put("TEN_KHANG", bodyCustomerResponse.getName());
        initialValues.put("MA_THE", bodyCustomerResponse.getCardNo());
        initialValues.put("DIA_CHI", bodyCustomerResponse.getAddress());
        initialValues.put("PHIEN_TTOAN", bodyCustomerResponse.getInning());
        initialValues.put("LO_TRINH", bodyCustomerResponse.getRoad());
        initialValues.put("SO_GCS", bodyCustomerResponse.getBookCmis());
        initialValues.put("DIEN_LUC", bodyCustomerResponse.getPcCode());
        initialValues.put("SO_HO", "");
        initialValues.put("E_DONG", MainActivity.mEdong);
        initialValues.put("SDT_ECPAY", bodyCustomerResponse.getPhoneByECP());
        initialValues.put("SDT_EVN", bodyCustomerResponse.getPhoneByEVN());
        initialValues.put("GIAO_THU", Common.TRANG_THAI_GIAO_THU.GIAO_THU.getCode());
        initialValues.put("NGAY_GIAO_THU", Common.getDateTimeNow(Common.DATE_TIME_TYPE.FULL));

        database = getWritableDatabase();
        int rowAffect = (int) database.insert(TABLE_NAME_CUSTOMER, null, initialValues);
        return rowAffect;
    }


    public long updateCustomer(CustomerResponse customerResponse) {
        ContentValues initialValues = new ContentValues();

        views.ecpay.com.postabletecpay.util.entities.response.EntityCustomer.BodyCustomerResponse bodyCustomerResponse = customerResponse.getBodyCustomerResponse();
        views.ecpay.com.postabletecpay.util.entities.response.EntityCustomer.FooterCustomerResponse footerCustomerResponse = customerResponse.getFooterCustomerResponse();

        initialValues.put("TEN_KHANG", bodyCustomerResponse.getName());
        initialValues.put("MA_THE", bodyCustomerResponse.getCardNo());
        initialValues.put("DIA_CHI", bodyCustomerResponse.getAddress());
        initialValues.put("PHIEN_TTOAN", bodyCustomerResponse.getInning());
        initialValues.put("LO_TRINH", bodyCustomerResponse.getRoad());
        initialValues.put("SO_GCS", bodyCustomerResponse.getBookCmis());
        initialValues.put("DIEN_LUC", bodyCustomerResponse.getPcCode());
        initialValues.put("SO_HO", "");
        initialValues.put("E_DONG", MainActivity.mEdong);
        initialValues.put("SDT_ECPAY", bodyCustomerResponse.getPhoneByECP());
        initialValues.put("SDT_EVN", bodyCustomerResponse.getPhoneByEVN());

        database = getWritableDatabase();
        int rowAffect = (int) database.update(TABLE_NAME_CUSTOMER, initialValues, "MA_KHANG = ?", new String[]{bodyCustomerResponse.getCustomerCode()});
        return rowAffect;
    }

    public long updateCustomer(EntityKhachHang customer) {
        ContentValues initialValues = new ContentValues();

        initialValues.put("MA_KHANG", customer.getMA_KHANG());
        initialValues.put("TEN_KHANG", customer.getTEN_KHANG());
        initialValues.put("MA_THE", customer.getMA_THE());
        initialValues.put("DIA_CHI", customer.getDIA_CHI());
        initialValues.put("PHIEN_TTOAN", customer.getPHIEN_TTOAN());
        initialValues.put("LO_TRINH", customer.getLO_TRINH());
        initialValues.put("SO_GCS", customer.getSO_GCS());
        initialValues.put("DIEN_LUC", customer.getDIEN_LUC());
        initialValues.put("SO_HO", "");
        initialValues.put("E_DONG", customer.getE_DONG());
        initialValues.put("SDT_ECPAY", customer.getSDT_ECPAY());
        initialValues.put("SDT_EVN", customer.getSDT_EVN());


        database = getWritableDatabase();
        int rowAffect = (int) database.update(TABLE_NAME_CUSTOMER, initialValues, "MA_KHANG = ?", new String[]{customer.getMA_KHANG()});
        return rowAffect;
    }

    public long checkCustomerExist(String code) {
        database = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + TABLE_NAME_CUSTOMER + " WHERE MA_KHANG = '" + code + "'";
        Cursor mCursor = database.rawQuery(query, null);
        if (mCursor.moveToFirst())
            return mCursor.getInt(0);
        return 0;
    }

    public EntityKhachHang getCustomer(String code) {
        database = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_CUSTOMER + " WHERE MA_KHANG = '" + code + "'";

        Cursor c = database.rawQuery(query, null);
        if (c.moveToFirst()) {
            EntityKhachHang khachHang = new EntityKhachHang();
            khachHang.setE_DONG(c.getString(c.getColumnIndex("E_DONG")));
            khachHang.setMA_KHANG(c.getString(c.getColumnIndex("MA_KHANG")));
            khachHang.setMA_THE(c.getString(c.getColumnIndex("MA_THE")));
            khachHang.setTEN_KHANG(c.getString(c.getColumnIndex("TEN_KHANG")));
            khachHang.setDIA_CHI(c.getString(c.getColumnIndex("DIA_CHI")));
            khachHang.setPHIEN_TTOAN(c.getString(c.getColumnIndex("PHIEN_TTOAN")));
            khachHang.setLO_TRINH(c.getString(c.getColumnIndex("LO_TRINH")));
            khachHang.setSO_GCS(c.getString(c.getColumnIndex("SO_GCS")));
            khachHang.setDIEN_LUC(c.getString(c.getColumnIndex("DIEN_LUC")));
            khachHang.setSO_HO(c.getString(c.getColumnIndex("SO_HO")));
            khachHang.setSDT_ECPAY(c.getString(c.getColumnIndex("SDT_ECPAY")));
            khachHang.setSDT_EVN(c.getString(c.getColumnIndex("SDT_EVN")));
            khachHang.setGIAO_THU(c.getString(c.getColumnIndex("GIAO_THU")));
            khachHang.setNGAY_GIAO_THU(Common.parseDate(c.getString(c.getColumnIndex("NGAY_GIAO_THU")), Common.DATE_TIME_TYPE.FULL.toString()));

            return khachHang;
        }
        return null;
    }
    //endregion

    //region BILL Danh sách hóa dơn nợ
    public long insertBill(ListBillResponse listBillResponse) {


        ContentValues initialValues = new ContentValues();

        BodyBillResponse bodyBillResponse = listBillResponse.getBodyBillResponse();

        initialValues.put("MA_KHANG", bodyBillResponse.getCustomerCode());
        initialValues.put("E_DONG", bodyBillResponse.getEdong());
        initialValues.put("MA_HOA_DON", bodyBillResponse.getBillId());
        initialValues.put("SERI_HDON", bodyBillResponse.getSeri());


        initialValues.put("MA_THE", bodyBillResponse.getCardNo());
        initialValues.put("TEN_KHANG", bodyBillResponse.getName());
        initialValues.put("DIA_CHI", bodyBillResponse.getAddress());

        initialValues.put("THANG_TTOAN", Common.parse(Common.parseDate(Common.convertToDate(bodyBillResponse.getTerm()), Common.DATE_TIME_TYPE.yyyyMMddHHmmssSSS.toString()), Common.DATE_TIME_TYPE.FULL.toString()));
        initialValues.put("PHIEN_TTOAN", Common.parseInt(bodyBillResponse.getPeriod()));
        initialValues.put("SO_TIEN_TTOAN", Common.parseInt(bodyBillResponse.getAmount()));
        initialValues.put("SO_GCS", bodyBillResponse.getBookCmis());
        initialValues.put("DIEN_LUC", bodyBillResponse.getPcCode());
        initialValues.put("SO_HO", bodyBillResponse.getHome());

        initialValues.put("SO_DAU_KY", bodyBillResponse.getOldIndex());
        initialValues.put("SO_CUOI_KY", bodyBillResponse.getNewIndex());
        initialValues.put("SO_CTO", bodyBillResponse.getElectricityMeter());
        initialValues.put("SDT_ECPAY", bodyBillResponse.getPhoneByecp());
        initialValues.put("SDT_EVN", bodyBillResponse.getPhoneByevn());
        initialValues.put("GIAO_THU", Common.TRANG_THAI_GIAO_THU.GIAO_THU.getCode());
        initialValues.put("NGAY_GIAO_THU", Common.getDateTimeNow(Common.DATE_TIME_TYPE.FULL));
        initialValues.put("TRANG_THAI_TTOAN", Common.TRANG_THAI_TTOAN.CHUA_TTOAN.getCode());
        initialValues.put("VI_TTOAN", "");
        initialValues.put("CHI_TIET_KG", bodyBillResponse.getPriceDetail());
        initialValues.put("CHI_TIET_MCS", bodyBillResponse.getNumeDetail());
        initialValues.put("CHI_TIET_TIEN_MCS", bodyBillResponse.getAmountDetail());
        initialValues.put("DNTT", bodyBillResponse.getNume());
        initialValues.put("TONG_TIEN_CHUA_THUE", Common.parseInt(bodyBillResponse.getAmountNotTax()));
        initialValues.put("TIEN_THUE",Common.parseInt(bodyBillResponse.getAmountTax()));
                database = getWritableDatabase();
        int rowAffect = (int) database.insert(TABLE_NAME_BILL, null, initialValues);
        return rowAffect;
    }

    public long insertBill(BillResponse listBillResponse) {
        ContentValues initialValues = new ContentValues();

        views.ecpay.com.postabletecpay.util.entities.response.EntityBill.BodyBillResponse bodyBillResponse = listBillResponse.getBodyBillResponse();

        initialValues.put("MA_KHANG", bodyBillResponse.getCustomerCode());
        initialValues.put("E_DONG", bodyBillResponse.getEdong());
        initialValues.put("MA_HOA_DON", bodyBillResponse.getBillId());
        initialValues.put("SERI_HDON", bodyBillResponse.getSeri());


        initialValues.put("MA_THE", bodyBillResponse.getCardNo());
        initialValues.put("TEN_KHANG", bodyBillResponse.getName());
        initialValues.put("DIA_CHI", bodyBillResponse.getAddress());

        initialValues.put("THANG_TTOAN", Common.parse(Common.parseDate(Common.convertToDate(bodyBillResponse.getTerm()), Common.DATE_TIME_TYPE.yyyyMMddHHmmssSSS.toString()), Common.DATE_TIME_TYPE.FULL.toString()));
        initialValues.put("PHIEN_TTOAN", Common.parseInt(bodyBillResponse.getPeriod()));
        initialValues.put("SO_TIEN_TTOAN", Common.parseInt(bodyBillResponse.getAmount()));
        initialValues.put("SO_GCS", bodyBillResponse.getBookCmis());
        initialValues.put("DIEN_LUC", bodyBillResponse.getPcCode());
        initialValues.put("SO_HO", bodyBillResponse.getHome());

        initialValues.put("SO_DAU_KY", bodyBillResponse.getOldIndex());
        initialValues.put("SO_CUOI_KY", bodyBillResponse.getNewIndex());
        initialValues.put("SO_CTO", bodyBillResponse.getElectricityMeter());
        initialValues.put("SDT_ECPAY", bodyBillResponse.getPhoneByecp());
        initialValues.put("SDT_EVN", bodyBillResponse.getPhoneByevn());
        initialValues.put("GIAO_THU", Common.TRANG_THAI_GIAO_THU.GIAO_THU.getCode());
        initialValues.put("NGAY_GIAO_THU", Common.getDateTimeNow(Common.DATE_TIME_TYPE.FULL));
        initialValues.put("TRANG_THAI_TTOAN", Common.TRANG_THAI_TTOAN.CHUA_TTOAN.getCode());
        initialValues.put("VI_TTOAN", "");
        initialValues.put("CHI_TIET_KG", bodyBillResponse.getPriceDetail());
        initialValues.put("CHI_TIET_MCS", bodyBillResponse.getNumeDetail());
        initialValues.put("CHI_TIET_TIEN_MCS", bodyBillResponse.getAmountDetail());
        initialValues.put("DNTT", bodyBillResponse.getNume());
        initialValues.put("TONG_TIEN_CHUA_THUE", Common.parseInt(bodyBillResponse.getAmountNotTax()));
        initialValues.put("TIEN_THUE",Common.parseInt(bodyBillResponse.getAmountTax()));

        database = getWritableDatabase();
        int rowAffect = (int) database.insert(TABLE_NAME_BILL, null, initialValues);
        return rowAffect;
    }

    public long insertLichSuThanhToan(views.ecpay.com.postabletecpay.util.entities.response.EntityBill.BodyBillResponse bodyBillResponse, String HINH_THUC_TT, String TRANG_THAI_TT, String TRANG_THAI_CHAM_NO, String TRANG_THAI_HUY,
                                      String TRANG_THAI_NGHI_NGO, int SO_IN_BIEN_NHAN, String IN_THONG_BAO_DIEN, String NGAY_PHAT_SINH,
                                      String MA_GIAO_DICH) {
        ContentValues initialValues = new ContentValues();


        initialValues.put("MA_KHANG", bodyBillResponse.getCustomerCode());
        initialValues.put("E_DONG", bodyBillResponse.getEdong());
        initialValues.put("MA_HOA_DON", bodyBillResponse.getBillId());
        initialValues.put("SERI_HDON", bodyBillResponse.getSeri());


        initialValues.put("MA_THE", bodyBillResponse.getCardNo());
        initialValues.put("TEN_KHANG", bodyBillResponse.getName());
        initialValues.put("DIA_CHI", bodyBillResponse.getAddress());

        initialValues.put("THANG_TTOAN", Common.parse(Common.parseDate(Common.convertToDate(bodyBillResponse.getTerm()), Common.DATE_TIME_TYPE.yyyyMMddHHmmssSSS.toString()), Common.DATE_TIME_TYPE.FULL.toString()));
        initialValues.put("PHIEN_TTOAN", Common.parseInt(bodyBillResponse.getPeriod()));
        initialValues.put("SO_TIEN_TTOAN", Common.parseInt(bodyBillResponse.getAmount()));
        initialValues.put("SO_GCS", bodyBillResponse.getBookCmis());
        initialValues.put("DIEN_LUC", bodyBillResponse.getPcCode());
        initialValues.put("SO_HO", bodyBillResponse.getHome());

        initialValues.put("SO_DAU_KY", bodyBillResponse.getOldIndex());
        initialValues.put("SO_CUOI_KY", bodyBillResponse.getNewIndex());
        initialValues.put("SO_CTO", bodyBillResponse.getElectricityMeter());
        initialValues.put("SDT_ECPAY", bodyBillResponse.getPhoneByecp());
        initialValues.put("SDT_EVN", bodyBillResponse.getPhoneByevn());
        initialValues.put("GIAO_THU", Common.TRANG_THAI_GIAO_THU.GIAO_THU.getCode());
        initialValues.put("NGAY_GIAO_THU", Common.getDateTimeNow(Common.DATE_TIME_TYPE.FULL));
        initialValues.put("VI_TTOAN", bodyBillResponse.getBillingBy());


        initialValues.put("HINH_THUC_TT", HINH_THUC_TT);
        initialValues.put("TRANG_THAI_TTOAN", TRANG_THAI_TT);
        initialValues.put("TRANG_THAI_CHAM_NO", TRANG_THAI_CHAM_NO);
        initialValues.put("TRANG_THAI_HUY", TRANG_THAI_HUY);
        initialValues.put("TRANG_THAI_NGHI_NGO", TRANG_THAI_NGHI_NGO);
        initialValues.put("SO_IN_BIEN_NHAN", SO_IN_BIEN_NHAN);
        initialValues.put("IN_THONG_BAO_DIEN", IN_THONG_BAO_DIEN);
        initialValues.put("NGAY_PHAT_SINH", NGAY_PHAT_SINH);
        initialValues.put("MA_GIAO_DICH", MA_GIAO_DICH);


        database = getWritableDatabase();
        int rowAffect = (int) database.insert(TABLE_NAME_HISTORY_PAY, null, initialValues);
        return rowAffect;
    }

    public void insertLichSuThanhToan(String MA_HOA_DON, String NGAY_PHAT_SINH, String MA_GIAO_DICH) {
        database = getWritableDatabase();

        String query = "SELECT * FROM " + TABLE_NAME_DEBT_COLLECTION + " WHERE MA_HOA_DON = " + MA_HOA_DON;
        Cursor hoaDonThu = database.rawQuery(query, null);
        if (hoaDonThu == null || !hoaDonThu.moveToFirst())
            return;

        ContentValues initialValues = new ContentValues();

        initialValues.put("MA_KHANG", hoaDonThu.getString(hoaDonThu.getColumnIndex("MA_KHANG")));
        initialValues.put("E_DONG", hoaDonThu.getString(hoaDonThu.getColumnIndex("E_DONG")));
        initialValues.put("MA_HOA_DON", hoaDonThu.getString(hoaDonThu.getColumnIndex("MA_HOA_DON")));
        initialValues.put("SERI_HDON", hoaDonThu.getString(hoaDonThu.getColumnIndex("SERI_HDON")));


        initialValues.put("MA_THE", hoaDonThu.getString(hoaDonThu.getColumnIndex("MA_THE")));
        initialValues.put("TEN_KHANG", hoaDonThu.getString(hoaDonThu.getColumnIndex("TEN_KHANG")));
        initialValues.put("DIA_CHI", hoaDonThu.getString(hoaDonThu.getColumnIndex("DIA_CHI")));

        initialValues.put("THANG_TTOAN", hoaDonThu.getString(hoaDonThu.getColumnIndex("THANG_TTOAN")));
        initialValues.put("PHIEN_TTOAN", hoaDonThu.getString(hoaDonThu.getColumnIndex("PHIEN_TTOAN")));
        initialValues.put("SO_TIEN_TTOAN", hoaDonThu.getString(hoaDonThu.getColumnIndex("SO_TIEN_TTOAN")));
        initialValues.put("SO_GCS", hoaDonThu.getString(hoaDonThu.getColumnIndex("SO_GCS")));
        initialValues.put("DIEN_LUC", hoaDonThu.getString(hoaDonThu.getColumnIndex("DIEN_LUC")));
        initialValues.put("SO_HO", hoaDonThu.getString(hoaDonThu.getColumnIndex("SO_HO")));

        initialValues.put("SO_DAU_KY", hoaDonThu.getString(hoaDonThu.getColumnIndex("SO_DAU_KY")));
        initialValues.put("SO_CUOI_KY", hoaDonThu.getString(hoaDonThu.getColumnIndex("SO_CUOI_KY")));
        initialValues.put("SO_CTO", hoaDonThu.getString(hoaDonThu.getColumnIndex("SO_CTO")));
        initialValues.put("SDT_ECPAY", hoaDonThu.getString(hoaDonThu.getColumnIndex("SDT_ECPAY")));
        initialValues.put("SDT_EVN", hoaDonThu.getString(hoaDonThu.getColumnIndex("SDT_EVN")));
        initialValues.put("GIAO_THU", hoaDonThu.getString(hoaDonThu.getColumnIndex("GIAO_THU")));
        initialValues.put("NGAY_GIAO_THU", hoaDonThu.getString(hoaDonThu.getColumnIndex("NGAY_GIAO_THU")));
        initialValues.put("VI_TTOAN", hoaDonThu.getString(hoaDonThu.getColumnIndex("VI_TTOAN")));


        initialValues.put("HINH_THUC_TT", hoaDonThu.getString(hoaDonThu.getColumnIndex("HINH_THUC_TT")));
        initialValues.put("TRANG_THAI_TTOAN", hoaDonThu.getString(hoaDonThu.getColumnIndex("TRANG_THAI_TTOAN")));
        initialValues.put("TRANG_THAI_CHAM_NO", hoaDonThu.getString(hoaDonThu.getColumnIndex("TRANG_THAI_CHAM_NO")));
        initialValues.put("TRANG_THAI_HUY", hoaDonThu.getString(hoaDonThu.getColumnIndex("TRANG_THAI_HUY")));
        initialValues.put("TRANG_THAI_NGHI_NGO", hoaDonThu.getString(hoaDonThu.getColumnIndex("TRANG_THAI_NGHI_NGO")));
        initialValues.put("SO_IN_BIEN_NHAN", hoaDonThu.getString(hoaDonThu.getColumnIndex("SO_IN_BIEN_NHAN")));
        initialValues.put("IN_THONG_BAO_DIEN", hoaDonThu.getString(hoaDonThu.getColumnIndex("IN_THONG_BAO_DIEN")));
        initialValues.put("NGAY_PHAT_SINH", NGAY_PHAT_SINH);
        initialValues.put("MA_GIAO_DICH", MA_GIAO_DICH);

        database.insert(TABLE_NAME_HISTORY_PAY, null, initialValues);


        if (hoaDonThu != null && !hoaDonThu.isClosed()) {
            hoaDonThu.close();
        }
    }

    public void insertLichSuThanhToan(String MA_HOA_DON, Common.TRANG_THAI_HUY TRANG_THAI_THUY, String MA_GIAO_DICH, String LyDo) {
        database = getWritableDatabase();

        String query = "SELECT * FROM " + TABLE_NAME_DEBT_COLLECTION + " WHERE MA_HOA_DON = " + MA_HOA_DON;
        Cursor hoaDonThu = database.rawQuery(query, null);
        if (hoaDonThu == null || !hoaDonThu.moveToFirst())
            return;

        ContentValues initialValues = new ContentValues();

        initialValues.put("MA_KHANG", hoaDonThu.getString(hoaDonThu.getColumnIndex("MA_KHANG")));
        initialValues.put("E_DONG", hoaDonThu.getString(hoaDonThu.getColumnIndex("E_DONG")));
        initialValues.put("MA_HOA_DON", hoaDonThu.getString(hoaDonThu.getColumnIndex("MA_HOA_DON")));
        initialValues.put("SERI_HDON", hoaDonThu.getString(hoaDonThu.getColumnIndex("SERI_HDON")));


        initialValues.put("MA_THE", hoaDonThu.getString(hoaDonThu.getColumnIndex("MA_THE")));
        initialValues.put("TEN_KHANG", hoaDonThu.getString(hoaDonThu.getColumnIndex("TEN_KHANG")));
        initialValues.put("DIA_CHI", hoaDonThu.getString(hoaDonThu.getColumnIndex("DIA_CHI")));

        initialValues.put("THANG_TTOAN", hoaDonThu.getString(hoaDonThu.getColumnIndex("THANG_TTOAN")));
        initialValues.put("PHIEN_TTOAN", hoaDonThu.getString(hoaDonThu.getColumnIndex("PHIEN_TTOAN")));
        initialValues.put("SO_TIEN_TTOAN", hoaDonThu.getString(hoaDonThu.getColumnIndex("SO_TIEN_TTOAN")));
        initialValues.put("SO_GCS", hoaDonThu.getString(hoaDonThu.getColumnIndex("SO_GCS")));
        initialValues.put("DIEN_LUC", hoaDonThu.getString(hoaDonThu.getColumnIndex("DIEN_LUC")));
        initialValues.put("SO_HO", hoaDonThu.getString(hoaDonThu.getColumnIndex("SO_HO")));

        initialValues.put("SO_DAU_KY", hoaDonThu.getString(hoaDonThu.getColumnIndex("SO_DAU_KY")));
        initialValues.put("SO_CUOI_KY", hoaDonThu.getString(hoaDonThu.getColumnIndex("SO_CUOI_KY")));
        initialValues.put("SO_CTO", hoaDonThu.getString(hoaDonThu.getColumnIndex("SO_CTO")));
        initialValues.put("SDT_ECPAY", hoaDonThu.getString(hoaDonThu.getColumnIndex("SDT_ECPAY")));
        initialValues.put("SDT_EVN", hoaDonThu.getString(hoaDonThu.getColumnIndex("SDT_EVN")));
        initialValues.put("GIAO_THU", hoaDonThu.getString(hoaDonThu.getColumnIndex("GIAO_THU")));
        initialValues.put("NGAY_GIAO_THU", hoaDonThu.getString(hoaDonThu.getColumnIndex("NGAY_GIAO_THU")));
        initialValues.put("VI_TTOAN", hoaDonThu.getString(hoaDonThu.getColumnIndex("VI_TTOAN")));


        initialValues.put("HINH_THUC_TT", hoaDonThu.getString(hoaDonThu.getColumnIndex("HINH_THUC_TT")));
        initialValues.put("TRANG_THAI_TTOAN", hoaDonThu.getString(hoaDonThu.getColumnIndex("TRANG_THAI_TTOAN")));
        initialValues.put("TRANG_THAI_CHAM_NO", hoaDonThu.getString(hoaDonThu.getColumnIndex("TRANG_THAI_CHAM_NO")));
        initialValues.put("TRANG_THAI_HUY", TRANG_THAI_THUY.getCode());
        initialValues.put("TRANG_THAI_NGHI_NGO", hoaDonThu.getString(hoaDonThu.getColumnIndex("TRANG_THAI_NGHI_NGO")));
        initialValues.put("SO_IN_BIEN_NHAN", hoaDonThu.getString(hoaDonThu.getColumnIndex("SO_IN_BIEN_NHAN")));
        initialValues.put("IN_THONG_BAO_DIEN", hoaDonThu.getString(hoaDonThu.getColumnIndex("IN_THONG_BAO_DIEN")));
        initialValues.put("NGAY_PHAT_SINH", Common.getDateTimeNow(Common.DATE_TIME_TYPE.FULL));
        initialValues.put("MA_GIAO_DICH", MA_GIAO_DICH);

        database.insert(TABLE_NAME_HISTORY_PAY, null, initialValues);


        if (hoaDonThu != null && !hoaDonThu.isClosed()) {
            hoaDonThu.close();
        }
    }

    public long insertLichSuThanhToan(EntityLichSuThanhToan lichSuThanhToan) {
        ContentValues initialValues = new ContentValues();


        initialValues.put("MA_KHANG", lichSuThanhToan.getMA_KHANG());
        initialValues.put("E_DONG", lichSuThanhToan.getE_DONG());
        initialValues.put("MA_HOA_DON", lichSuThanhToan.getMA_HOA_DON());
        initialValues.put("SERI_HDON", lichSuThanhToan.getSERI_HDON());


        initialValues.put("MA_THE", lichSuThanhToan.getMA_THE());
        initialValues.put("TEN_KHANG", lichSuThanhToan.getTEN_KHANG());
        initialValues.put("DIA_CHI", lichSuThanhToan.getDIA_CHI());

        initialValues.put("THANG_TTOAN", Common.parse(lichSuThanhToan.getTHANG_TTOAN(), Common.DATE_TIME_TYPE.FULL.toString()));
        initialValues.put("PHIEN_TTOAN", lichSuThanhToan.getPHIEN_TTOAN());
        initialValues.put("SO_TIEN_TTOAN", lichSuThanhToan.getSO_TIEN_TTOAN());
        initialValues.put("SO_GCS", lichSuThanhToan.getSO_GCS());
        initialValues.put("DIEN_LUC", lichSuThanhToan.getDIEN_LUC());
        initialValues.put("SO_HO", lichSuThanhToan.getSO_HO());

        initialValues.put("SO_DAU_KY", lichSuThanhToan.getSO_DAU_KY());
        initialValues.put("SO_CUOI_KY", lichSuThanhToan.getSO_CUOI_KY());
        initialValues.put("SO_CTO", lichSuThanhToan.getSO_CTO());
        initialValues.put("SDT_ECPAY", lichSuThanhToan.getSDT_ECPAY());
        initialValues.put("SDT_EVN", lichSuThanhToan.getSDT_EVN());
        initialValues.put("GIAO_THU", lichSuThanhToan.getGIAO_THU());
        initialValues.put("NGAY_GIAO_THU", Common.parse(lichSuThanhToan.getNGAY_GIAO_THU(), Common.DATE_TIME_TYPE.FULL.toString()));
        initialValues.put("VI_TTOAN", lichSuThanhToan.getVI_TTOAN());


        initialValues.put("HINH_THUC_TT", lichSuThanhToan.getHINH_THUC_TT());
        initialValues.put("TRANG_THAI_TTOAN", lichSuThanhToan.getTRANG_THAI_TTOAN());
        initialValues.put("TRANG_THAI_CHAM_NO", lichSuThanhToan.getTRANG_THAI_CHAM_NO());
        initialValues.put("TRANG_THAI_HUY", lichSuThanhToan.getTRANG_THAI_HUY());
        initialValues.put("TRANG_THAI_NGHI_NGO", lichSuThanhToan.getTRANG_THAI_NGHI_NGO());
        initialValues.put("SO_IN_BIEN_NHAN", lichSuThanhToan.getSO_IN_BIEN_NHAN());
        initialValues.put("IN_THONG_BAO_DIEN", lichSuThanhToan.getIN_THONG_BAO_DIEN());
        initialValues.put("NGAY_PHAT_SINH", Common.parse(lichSuThanhToan.getNGAY_PHAT_SINH(), Common.DATE_TIME_TYPE.FULL.toString()));
        initialValues.put("MA_GIAO_DICH", lichSuThanhToan.getMA_GIAO_DICH());


        database = getWritableDatabase();
        int rowAffect = (int) database.insert(TABLE_NAME_HISTORY_PAY, null, initialValues);
        return rowAffect;
    }

    public void insertHoaDonThu(views.ecpay.com.postabletecpay.util.entities.response.EntityBill.BodyBillResponse bodyBillResponse, String HINH_THUC_TT,
                                String TRANG_THAI_TT, String TRANG_THAI_CHAM_NO, String TRANG_THAI_HUY,
                                String TRANG_THAI_HOAN_TRA, String TRANG_THAI_XU_LY_NGHI_NGO, String TRANG_THAI_DAY_CHAM_NO, int SO_LAN_IN_BIEN_NHAN,
                                String NGAY_DAY, String IN_THONG_BAO_DIEN) {

        database = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_DEBT_COLLECTION + " WHERE MA_HOA_DON = '" + bodyBillResponse.getBillId() + "'";
        Cursor mCursor = database.rawQuery(query, null);
        if (mCursor.moveToFirst()) {
            mCursor.close();
            ContentValues initialValues = new ContentValues();


            initialValues.put("MA_KHANG", bodyBillResponse.getCustomerCode());
            initialValues.put("E_DONG", bodyBillResponse.getEdong());
            initialValues.put("MA_HOA_DON", bodyBillResponse.getBillId());
            initialValues.put("SERI_HDON", bodyBillResponse.getSeri());


            initialValues.put("MA_THE", bodyBillResponse.getCardNo());
            initialValues.put("TEN_KHANG", bodyBillResponse.getName());
            initialValues.put("DIA_CHI", bodyBillResponse.getAddress());

            initialValues.put("THANG_TTOAN", Common.parse(Common.parseDate(Common.convertToDate(bodyBillResponse.getTerm()), Common.DATE_TIME_TYPE.yyyyMMddHHmmssSSS.toString()), Common.DATE_TIME_TYPE.FULL.toString()));
            initialValues.put("PHIEN_TTOAN", Common.parseInt(bodyBillResponse.getPeriod()));
            initialValues.put("SO_TIEN_TTOAN", Common.parseInt(bodyBillResponse.getAmount()));
            initialValues.put("SO_GCS", bodyBillResponse.getBookCmis());
            initialValues.put("DIEN_LUC", bodyBillResponse.getPcCode());
            initialValues.put("SO_HO", bodyBillResponse.getHome());

            initialValues.put("SO_DAU_KY", bodyBillResponse.getOldIndex());
            initialValues.put("SO_CUOI_KY", bodyBillResponse.getNewIndex());
            initialValues.put("SO_CTO", bodyBillResponse.getElectricityMeter());
            initialValues.put("SDT_ECPAY", bodyBillResponse.getPhoneByecp());
            initialValues.put("SDT_EVN", bodyBillResponse.getPhoneByevn());
            initialValues.put("GIAO_THU", Common.TRANG_THAI_GIAO_THU.GIAO_THU.getCode());
            initialValues.put("NGAY_GIAO_THU", Common.getDateTimeNow(Common.DATE_TIME_TYPE.FULL));
            initialValues.put("VI_TTOAN", bodyBillResponse.getBillingBy());


            initialValues.put("HINH_THUC_TT", HINH_THUC_TT);
            initialValues.put("TRANG_THAI_TTOAN", TRANG_THAI_TT);
            initialValues.put("TRANG_THAI_CHAM_NO", TRANG_THAI_CHAM_NO);
            initialValues.put("TRANG_THAI_HUY", TRANG_THAI_HUY);
            initialValues.put("TRANG_THAI_HOAN_TRA", TRANG_THAI_HOAN_TRA);
            initialValues.put("TRANG_THAI_XU_LY_NGHI_NGO", TRANG_THAI_XU_LY_NGHI_NGO);
            initialValues.put("IN_THONG_BAO_DIEN", IN_THONG_BAO_DIEN);
            initialValues.put("TRANG_THAI_DAY_CHAM_NO", TRANG_THAI_DAY_CHAM_NO);
            initialValues.put("SO_LAN_IN_BIEN_NHAN", SO_LAN_IN_BIEN_NHAN);
            initialValues.put("NGAY_DAY", NGAY_DAY);

            if (bodyBillResponse.getRequestDate() != null) {
                initialValues.put("NGAY_THU", Common.parse(Common.parseDate(bodyBillResponse.getRequestDate(), Common.DATE_TIME_TYPE.yyyyMMddHHmmssSSS.toString()), Common.DATE_TIME_TYPE.FULL.toString()));
            }

            database.update(TABLE_NAME_DEBT_COLLECTION, initialValues, "MA_HOA_DON=?", new String[]{String.valueOf(bodyBillResponse.getBillId())});
        } else {

            ContentValues initialValues = new ContentValues();


            initialValues.put("MA_KHANG", bodyBillResponse.getCustomerCode());
            initialValues.put("E_DONG", bodyBillResponse.getEdong());
            initialValues.put("MA_HOA_DON", bodyBillResponse.getBillId());
            initialValues.put("SERI_HDON", bodyBillResponse.getSeri());


            initialValues.put("MA_THE", bodyBillResponse.getCardNo());
            initialValues.put("TEN_KHANG", bodyBillResponse.getName());
            initialValues.put("DIA_CHI", bodyBillResponse.getAddress());

            initialValues.put("THANG_TTOAN", Common.parse(Common.parseDate(Common.convertToDate(bodyBillResponse.getTerm()), Common.DATE_TIME_TYPE.yyyyMMddHHmmssSSS.toString()), Common.DATE_TIME_TYPE.FULL.toString()));
            initialValues.put("PHIEN_TTOAN", Common.parseInt(bodyBillResponse.getPeriod()));
            initialValues.put("SO_TIEN_TTOAN", Common.parseInt(bodyBillResponse.getAmount()));
            initialValues.put("SO_GCS", bodyBillResponse.getBookCmis());
            initialValues.put("DIEN_LUC", bodyBillResponse.getPcCode());
            initialValues.put("SO_HO", bodyBillResponse.getHome());

            initialValues.put("SO_DAU_KY", bodyBillResponse.getOldIndex());
            initialValues.put("SO_CUOI_KY", bodyBillResponse.getNewIndex());
            initialValues.put("SO_CTO", bodyBillResponse.getElectricityMeter());
            initialValues.put("SDT_ECPAY", bodyBillResponse.getPhoneByecp());
            initialValues.put("SDT_EVN", bodyBillResponse.getPhoneByevn());
            initialValues.put("GIAO_THU", Common.TRANG_THAI_GIAO_THU.GIAO_THU.getCode());
            initialValues.put("NGAY_GIAO_THU", Common.getDateTimeNow(Common.DATE_TIME_TYPE.FULL));
            initialValues.put("VI_TTOAN", bodyBillResponse.getBillingBy());


            initialValues.put("HINH_THUC_TT", HINH_THUC_TT);
            initialValues.put("TRANG_THAI_TTOAN", TRANG_THAI_TT);
            initialValues.put("TRANG_THAI_CHAM_NO", TRANG_THAI_CHAM_NO);
            initialValues.put("TRANG_THAI_HUY", TRANG_THAI_HUY);
            initialValues.put("TRANG_THAI_HOAN_TRA", TRANG_THAI_HOAN_TRA);
            initialValues.put("TRANG_THAI_XU_LY_NGHI_NGO", TRANG_THAI_XU_LY_NGHI_NGO);
            initialValues.put("IN_THONG_BAO_DIEN", IN_THONG_BAO_DIEN);
            initialValues.put("TRANG_THAI_DAY_CHAM_NO", TRANG_THAI_DAY_CHAM_NO);
            initialValues.put("SO_LAN_IN_BIEN_NHAN", SO_LAN_IN_BIEN_NHAN);
            initialValues.put("NGAY_DAY", NGAY_DAY);


            if (bodyBillResponse.getRequestDate() != null) {
                initialValues.put("NGAY_THU", Common.parse(Common.parseDate(bodyBillResponse.getRequestDate(), Common.DATE_TIME_TYPE.yyyyMMddHHmmssSSS.toString()), Common.DATE_TIME_TYPE.FULL.toString()));
            } else {
                initialValues.put("NGAY_THU", Common.getDateTimeNow(Common.DATE_TIME_TYPE.FULL));
            }

            int rowAffect = (int) database.insert(TABLE_NAME_DEBT_COLLECTION, null, initialValues);
        }
    }


    public void insertHoaDonThu(EntityHoaDonThu entityHoaDonThu) {

        database = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_DEBT_COLLECTION + " WHERE MA_HOA_DON = '" + entityHoaDonThu.getMA_HOA_DON() + "'";
        Cursor mCursor = database.rawQuery(query, null);
        if (mCursor.moveToFirst()) {
            ContentValues initialValues = new ContentValues();


            initialValues.put("MA_KHANG", entityHoaDonThu.getMA_KHANG());
            initialValues.put("E_DONG", entityHoaDonThu.getE_DONG());
            initialValues.put("MA_HOA_DON", entityHoaDonThu.getMA_HOA_DON());
            initialValues.put("SERI_HDON", entityHoaDonThu.getSERI_HDON());


            initialValues.put("MA_THE", entityHoaDonThu.getMA_THE());
            initialValues.put("TEN_KHANG", entityHoaDonThu.getTEN_KHANG());
            initialValues.put("DIA_CHI", entityHoaDonThu.getDIA_CHI());

            initialValues.put("THANG_TTOAN", Common.parse(entityHoaDonThu.getTHANG_TTOAN(), Common.DATE_TIME_TYPE.FULL.toString()));
            initialValues.put("PHIEN_TTOAN", entityHoaDonThu.getPHIEN_TTOAN());
            initialValues.put("SO_TIEN_TTOAN", entityHoaDonThu.getSO_TIEN_TTOAN());
            initialValues.put("SO_GCS", entityHoaDonThu.getSO_GCS());
            initialValues.put("DIEN_LUC", entityHoaDonThu.getDIEN_LUC());
            initialValues.put("SO_HO", entityHoaDonThu.getSO_HO());

            initialValues.put("SO_DAU_KY", entityHoaDonThu.getSO_DAU_KY());
            initialValues.put("SO_CUOI_KY", entityHoaDonThu.getSO_CUOI_KY());
            initialValues.put("SO_CTO", entityHoaDonThu.getSO_CTO());
            initialValues.put("SDT_ECPAY", entityHoaDonThu.getSDT_ECPAY());
            initialValues.put("SDT_EVN", entityHoaDonThu.getSDT_EVN());
            initialValues.put("GIAO_THU", entityHoaDonThu.getGIAO_THU());
            initialValues.put("NGAY_GIAO_THU", Common.parse(entityHoaDonThu.getNGAY_GIAO_THU(), Common.DATE_TIME_TYPE.FULL.toString()));
            initialValues.put("VI_TTOAN", entityHoaDonThu.getVI_TTOAN());


            initialValues.put("HINH_THUC_TT", entityHoaDonThu.getHINH_THUC_TT());
            initialValues.put("TRANG_THAI_TTOAN", entityHoaDonThu.getTRANG_THAI_TTOAN());
            initialValues.put("TRANG_THAI_CHAM_NO", entityHoaDonThu.getTRANG_THAI_CHAM_NO());
            initialValues.put("TRANG_THAI_HUY", entityHoaDonThu.getTRANG_THAI_HUY());
            initialValues.put("TRANG_THAI_HOAN_TRA", entityHoaDonThu.getTRANG_THAI_HOAN_TRA());
            initialValues.put("TRANG_THAI_XU_LY_NGHI_NGO", entityHoaDonThu.getTRANG_THAI_XU_LY_NGHI_NGO());
            initialValues.put("IN_THONG_BAO_DIEN", entityHoaDonThu.getIN_THONG_BAO_DIEN());
            initialValues.put("TRANG_THAI_DAY_CHAM_NO", entityHoaDonThu.getTRANG_THAI_DAY_CHAM_NO());
            initialValues.put("SO_LAN_IN_BIEN_NHAN", entityHoaDonThu.getSO_LAN_IN_BIEN_NHAN());
            initialValues.put("NGAY_DAY", Common.parse(entityHoaDonThu.getNGAY_DAY(), Common.DATE_TIME_TYPE.FULL.toString()));

            database.update(TABLE_NAME_DEBT_COLLECTION, initialValues, "MA_HOA_DON=?", new String[]{String.valueOf(entityHoaDonThu.getMA_HOA_DON())});
        } else {

            ContentValues initialValues = new ContentValues();

            initialValues.put("MA_KHANG", entityHoaDonThu.getMA_KHANG());
            initialValues.put("E_DONG", entityHoaDonThu.getE_DONG());
            initialValues.put("MA_HOA_DON", entityHoaDonThu.getMA_HOA_DON());
            initialValues.put("SERI_HDON", entityHoaDonThu.getSERI_HDON());


            initialValues.put("MA_THE", entityHoaDonThu.getMA_THE());
            initialValues.put("TEN_KHANG", entityHoaDonThu.getTEN_KHANG());
            initialValues.put("DIA_CHI", entityHoaDonThu.getDIA_CHI());

            initialValues.put("THANG_TTOAN", Common.parse(entityHoaDonThu.getTHANG_TTOAN(), Common.DATE_TIME_TYPE.FULL.toString()));
            initialValues.put("PHIEN_TTOAN", entityHoaDonThu.getPHIEN_TTOAN());
            initialValues.put("SO_TIEN_TTOAN", entityHoaDonThu.getSO_TIEN_TTOAN());
            initialValues.put("SO_GCS", entityHoaDonThu.getSO_GCS());
            initialValues.put("DIEN_LUC", entityHoaDonThu.getDIEN_LUC());
            initialValues.put("SO_HO", entityHoaDonThu.getSO_HO());

            initialValues.put("SO_DAU_KY", entityHoaDonThu.getSO_DAU_KY());
            initialValues.put("SO_CUOI_KY", entityHoaDonThu.getSO_CUOI_KY());
            initialValues.put("SO_CTO", entityHoaDonThu.getSO_CTO());
            initialValues.put("SDT_ECPAY", entityHoaDonThu.getSDT_ECPAY());
            initialValues.put("SDT_EVN", entityHoaDonThu.getSDT_EVN());
            initialValues.put("GIAO_THU", entityHoaDonThu.getGIAO_THU());
            initialValues.put("NGAY_GIAO_THU", Common.parse(entityHoaDonThu.getNGAY_GIAO_THU(), Common.DATE_TIME_TYPE.FULL.toString()));
            initialValues.put("VI_TTOAN", entityHoaDonThu.getVI_TTOAN());


            initialValues.put("HINH_THUC_TT", entityHoaDonThu.getHINH_THUC_TT());
            initialValues.put("TRANG_THAI_TTOAN", entityHoaDonThu.getTRANG_THAI_TTOAN());
            initialValues.put("TRANG_THAI_CHAM_NO", entityHoaDonThu.getTRANG_THAI_CHAM_NO());
            initialValues.put("TRANG_THAI_HUY", entityHoaDonThu.getTRANG_THAI_HUY());
            initialValues.put("TRANG_THAI_HOAN_TRA", entityHoaDonThu.getTRANG_THAI_HOAN_TRA());
            initialValues.put("TRANG_THAI_XU_LY_NGHI_NGO", entityHoaDonThu.getTRANG_THAI_XU_LY_NGHI_NGO());
            initialValues.put("IN_THONG_BAO_DIEN", entityHoaDonThu.getIN_THONG_BAO_DIEN());
            initialValues.put("TRANG_THAI_DAY_CHAM_NO", entityHoaDonThu.getTRANG_THAI_DAY_CHAM_NO());
            initialValues.put("SO_LAN_IN_BIEN_NHAN", entityHoaDonThu.getSO_LAN_IN_BIEN_NHAN());
            initialValues.put("NGAY_DAY", Common.parse(entityHoaDonThu.getNGAY_DAY(), Common.DATE_TIME_TYPE.FULL.toString()));

            initialValues.put("NGAY_THU", Common.getDateTimeNow(Common.DATE_TIME_TYPE.FULL));

            int rowAffect = (int) database.insert(TABLE_NAME_DEBT_COLLECTION, null, initialValues);
        }
    }

    public long updateBill(BillResponse listBillResponse) {
        ContentValues initialValues = new ContentValues();

        views.ecpay.com.postabletecpay.util.entities.response.EntityBill.BodyBillResponse bodyBillResponse = listBillResponse.getBodyBillResponse();

        int status = Common.parseInt(bodyBillResponse.getStatus());
        /*
        Hoá đơn được thanh toán từ nguồn khác:
         */

        if (status == 1 && bodyBillResponse.getBillingType().equalsIgnoreCase("SOURCE_OTHER")) //Hoá đơn được thanh toán từ nguồn khác:
        {
            initialValues.put("TRANG_THAI_TTOAN", Common.TRANG_THAI_TTOAN.DA_TTOAN.getCode());
            this.insertLichSuThanhToan(bodyBillResponse, "", "03", "", "", "", 0, "", Common.getDateTimeNow(Common.DATE_TIME_TYPE.FULL), "02");
        } else if (status == 1 && bodyBillResponse.getBillingType().equalsIgnoreCase("EDONG_OTHER")) //Hoá đơn được thanh toán bởi số ví khác:
        {
            initialValues.put("TRANG_THAI_TTOAN", Common.TRANG_THAI_TTOAN.DA_TTOAN.getCode());
            initialValues.put("VI_TTOAN", bodyBillResponse.getBillingBy());

            this.insertLichSuThanhToan(bodyBillResponse, "", "04", "", "", "", 0, "", Common.getDateTimeNow(Common.DATE_TIME_TYPE.FULL), "02");

            //Chấm nợ thành công
        } else if (status == 1 && bodyBillResponse.getBillingType().equalsIgnoreCase("BILLING") && bodyBillResponse.getBillingBy().equalsIgnoreCase(MainActivity.mEdong)) {
            initialValues.put("TRANG_THAI_TTOAN", Common.TRANG_THAI_TTOAN.DA_TTOAN.getCode());

            this.insertLichSuThanhToan(bodyBillResponse, "", "02", "02", "", "", 0, "", Common.getDateTimeNow(Common.DATE_TIME_TYPE.FULL), "07");
            this.insertHoaDonThu(bodyBillResponse, "", "02", "02", "", "", "", "", 0, "", "");

            //Chấm nợ không thành công, thanh toan bởi nguồn khác
        } else if (status != 1 && bodyBillResponse.getBillingType().equalsIgnoreCase("SOURCE_OTHER") && bodyBillResponse.getBillingBy().equalsIgnoreCase(MainActivity.mEdong)) {
            initialValues.put("TRANG_THAI_TTOAN", Common.TRANG_THAI_TTOAN.DA_TTOAN.getCode());

            this.insertLichSuThanhToan(bodyBillResponse, "", "03", "05", "", "", 0, "", Common.getDateTimeNow(Common.DATE_TIME_TYPE.FULL), "07");
            this.insertHoaDonThu(bodyBillResponse, "", "03", "05", "", "01", "", "", 0, "", "");

            //Chấm nợ không thành công, thanh toan bởi ví khác
        } else if (status != 1 && bodyBillResponse.getBillingType().equalsIgnoreCase("EDONG_OTHER")) {
            initialValues.put("TRANG_THAI_TTOAN", Common.TRANG_THAI_TTOAN.DA_TTOAN.getCode());
            initialValues.put("VI_TTOAN", bodyBillResponse.getBillingBy());

            this.insertLichSuThanhToan(bodyBillResponse, "", "04", "05", "", "", 0, "", Common.getDateTimeNow(Common.DATE_TIME_TYPE.FULL), "07");
            this.insertHoaDonThu(bodyBillResponse, "", "04", "05", "", "01", "", "", 0, "", "");

            //Chấm nợ không thành công, chấm
        } else if (status != 1 && bodyBillResponse.getBillingType().equalsIgnoreCase("ERROR") && bodyBillResponse.getBillingBy().equalsIgnoreCase(MainActivity.mEdong)) {
            initialValues.put("TRANG_THAI_TTOAN", Common.TRANG_THAI_TTOAN.CHUA_TTOAN.getCode());

            this.insertLichSuThanhToan(bodyBillResponse, "", "02", "04", "", "", 0, "", Common.getDateTimeNow(Common.DATE_TIME_TYPE.FULL), "07");
            this.insertHoaDonThu(bodyBillResponse, "", "02", "04", "", "", "", "", 0, "", "");

            //Huỷ hoá đơn thành
        } else if (bodyBillResponse.getBillingType().equalsIgnoreCase("REVERT")) {
            if (bodyBillResponse.getBillingBy().equalsIgnoreCase(MainActivity.mEdong)) {
                initialValues.put("TRANG_THAI_TTOAN", Common.TRANG_THAI_TTOAN.CHUA_TTOAN.getCode());
                initialValues.put("VI_TTOAN", "");

                this.insertLichSuThanhToan(bodyBillResponse, "", "01", "", "01", "", 0, "", Common.getDateTimeNow(Common.DATE_TIME_TYPE.FULL), "08");
                this.insertHoaDonThu(bodyBillResponse, "", "", "", "01", "01", "", "", 0, "", "");

            } else {//Huỷ hoá đơn thành, tk != tk dang nhap
                initialValues.put("TRANG_THAI_TTOAN", Common.TRANG_THAI_TTOAN.CHUA_TTOAN.getCode());
                initialValues.put("VI_TTOAN", "");

                this.insertLichSuThanhToan(bodyBillResponse, "", "01", "", "01", "", 0, "", Common.getDateTimeNow(Common.DATE_TIME_TYPE.FULL), "08");
            }
        } else if (status != 1 && bodyBillResponse.getBillingType().equalsIgnoreCase("TIMEOUT")) {
            initialValues.put("TRANG_THAI_TTOAN", Common.TRANG_THAI_TTOAN.CHUA_TTOAN.getCode());
            initialValues.put("VI_TTOAN", "");
            this.insertLichSuThanhToan(bodyBillResponse, "", "01", "", "", "01", 0, "", Common.getDateTimeNow(Common.DATE_TIME_TYPE.FULL), "09");
            if (bodyBillResponse.getBillingBy().equalsIgnoreCase(MainActivity.mEdong)) {
                this.insertHoaDonThu(bodyBillResponse, "", "", "", "", "01", "01", "", 0, "", "");
            }

        } else if (bodyBillResponse.getBillingType().equalsIgnoreCase("TIMEOUT")) {
            initialValues.put("TRANG_THAI_TTOAN", Common.TRANG_THAI_TTOAN.DA_TTOAN.getCode());
            initialValues.put("VI_TTOAN", bodyBillResponse.getBillingBy());
            this.insertLichSuThanhToan(bodyBillResponse, "", "02", "02", "", "02", 0, "", Common.getDateTimeNow(Common.DATE_TIME_TYPE.FULL), "09");
            if (bodyBillResponse.getBillingBy().equalsIgnoreCase(MainActivity.mEdong)) {
                this.insertHoaDonThu(bodyBillResponse, "", "", "", "", "", "02", "", 0, "", "");
            }
        } else {
            return -1;
        }


        database = getWritableDatabase();
        int rowAffect = (int) database.update(TABLE_NAME_BILL, initialValues, "MA_HOA_DON=?", new String[]{String.valueOf(bodyBillResponse.getBillId())});
        return rowAffect;
    }


    public long checkBillExist(String billId) {
        database = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + TABLE_NAME_BILL + " WHERE MA_HOA_DON = '" + billId + "'";
        Cursor mCursor = database.rawQuery(query, null);
        if (mCursor.moveToFirst())
            return mCursor.getInt(0);
        return 0;
    }


    public int updateBalance(String edong, long balance, long lockmoney) {
        database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("balance", balance);
        contentValues.put("lockMoney", lockmoney);
        return database.update(TABLE_NAME_ACCOUNT, contentValues, "edong = ?", new String[]{String.valueOf(edong)});
    }

    //endregion
}

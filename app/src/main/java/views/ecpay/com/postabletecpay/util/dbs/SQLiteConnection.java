package views.ecpay.com.postabletecpay.util.dbs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import java.util.List;

import views.ecpay.com.postabletecpay.model.ReportModel;
import views.ecpay.com.postabletecpay.model.adapter.PayAdapter;
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

import static android.content.ContentValues.TAG;
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


    private String CREATE_TABLE_EVN_PC = "CREATE TABLE " + TABLE_NAME_EVN_PC + " ( pcId NOT NULL PRIMARY KEY, strPcId TEXT, parentId INTEGER, " +
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
    private String CREATE_TABLE_BILL = "CREATE TABLE `" + TABLE_NAME_BILL + "` ( `ID` INTEGER PRIMARY KEY AUTOINCREMENT , `E_DONG` TEXT, `MA_HOA_DON` TEXT, `SERI_HDON` TEXT, `MA_KHANG` TEXT, " +
            "`MA_THE` TEXT, `TEN_KHANG` TEXT, `DIA_CHI` TEXT, `THANG_TTOAN` DATE, `PHIEN_TTOAN` TEXT, `SO_TIEN_TTOAN` INTEGER, " +
            "`SO_GCS` TEXT, `DIEN_LUC` TEXT, `SO_HO` TEXT, `SO_DAU_KY` TEXT, `SO_CUOI_KY` TEXT, `SO_CTO` TEXT, `SDT_ECPAY` TEXT, " +
            "`SDT_EVN` TEXT, `GIAO_THU` TEXT, `NGAY_GIAO_THU` DATE, `TRANG_THAI_TTOAN` TEXT, `VI_TTOAN` TEXT)";

    private String CREATE_TABLE_DEBT_COLLECTION = "CREATE TABLE `" + TABLE_NAME_DEBT_COLLECTION + "` ( `ID` INTEGER PRIMARY KEY AUTOINCREMENT , `E_DONG` TEXT, `MA_HOA_DON` TEXT, `SERI_HDON` TEXT, `MA_KHANG` TEXT, " +
            "`MA_THE` TEXT, `TEN_KHANG` TEXT, `DIA_CHI` TEXT, `THANG_TTOAN` DATE, `PHIEN_TTOAN` TEXT, `SO_TIEN_TTOAN` INTEGER, " +
            "`SO_GCS` TEXT, `DIEN_LUC` TEXT, `SO_HO` TEXT, `SO_DAU_KY` TEXT, `SO_CUOI_KY` TEXT, `SO_CTO` TEXT, `SDT_ECPAY` TEXT, " +
            "`SDT_EVN` TEXT, `GIAO_THU` TEXT, `NGAY_GIAO_THU` DATE, `TRANG_THAI_TTOAN` TEXT, `VI_TTOAN` TEXT, `HINH_THUC_TT` TEXT, " +
            "`TRANG_THAI_CHAM_NO` TEXT, `TRANG_THAI_HUY` TEXT, `TRANG_THAI_HOAN_TRA` TEXT, `TRANG_THAI_XU_LY_NGHI_NGO` TEXT, " +
            "`TRANG_THAI_DAY_CHAM_NO` TEXT, `NGAY_DAY` DATE, `SO_LAN_IN_BIEN_NHAN` INTEGER, `IN_THONG_BAO_DIEN` TEXT)";

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

    //region sqlite
    public void insertOrUpdateAccount(Account account) {
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
            );
        }

        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }
        return account;
    }



    public ReportModel.BillInfo countBillDuocGiao(String edong) {
        ReportModel.BillInfo bill = new ReportModel.BillInfo();
//        String query = "SELECT coalesce(SUM(amount), 0) AS SUM, COUNT(*) AS COUNT FROM " + TABLE_NAME_BILL + " WHERE edongKey = '" + edong + "'";
//        Cursor mCursor = database.rawQuery(query, null);
//
//        if(mCursor.getCount() != ZERO && mCursor.moveToFirst())
//        {
//            bill.setAmount(Long.parseLong(mCursor.getString(mCursor.getColumnIndex("SUM"))));
//            bill.setCount(Integer.parseInt(mCursor.getString(mCursor.getColumnIndex("COUNT"))));
//        }
//
//        if (mCursor != null && !mCursor.isClosed()) {
//            mCursor.close();
//        }

        return bill;

    }



    public ReportModel.BillInfo countBillDaThu(String edong) {
        ReportModel.BillInfo bill = new ReportModel.BillInfo();
//        String query = "SELECT coalesce(SUM(amount), 0) AS SUM, COUNT(*) AS COUNT FROM " + TABLE_NAME_BILL + " WHERE edongKey = '" + edong + "' and status != 0";
//        Cursor mCursor = database.rawQuery(query, null);
//
//        if(mCursor.getCount() != ZERO && mCursor.moveToFirst())
//        {
//            bill.setAmount(Long.parseLong(mCursor.getString(mCursor.getColumnIndex("SUM"))));
//            bill.setCount(Integer.parseInt(mCursor.getString(mCursor.getColumnIndex("COUNT"))));
//        }
//
//        if (mCursor != null && !mCursor.isClosed()) {
//            mCursor.close();
//        }

        return bill;

    }



    public ReportModel.BillInfo countBillHoanTra(String edong) {
        ReportModel.BillInfo bill = new ReportModel.BillInfo();
//        String query = "SELECT coalesce(SUM(amount), 0) AS SUM, COUNT(*) AS COUNT FROM " + TABLE_NAME_BILL + " WHERE edongKey = '" + edong + "' and status != 0 and billingType IN ('EDONG_OTHER', 'SOURCE_OTHER', 'TIMEOUT', 'REVERT')";
//        Cursor mCursor = database.rawQuery(query, null);
//
//        if(mCursor.getCount() != ZERO && mCursor.moveToFirst())
//        {
//            bill.setAmount(Long.parseLong(mCursor.getString(mCursor.getColumnIndex("SUM"))));
//            bill.setCount(Integer.parseInt(mCursor.getString(mCursor.getColumnIndex("COUNT"))));
//        }
//
//        if (mCursor != null && !mCursor.isClosed()) {
//            mCursor.close();
//        }

        return bill;

    }


    public ReportModel.BillInfo countBillVangLai(String edong) {
        ReportModel.BillInfo bill = new ReportModel.BillInfo();
//        String query = "SELECT coalesce(SUM(amount), 0) AS SUM, COUNT(*) AS COUNT FROM " + TABLE_NAME_BILL + " bill WHERE bill.edongKey = '" + edong +
//                "' and bill.status != 0 and  NOT EXISTS (SELECT * FROM " + TABLE_NAME_CUSTOMER + " customer WHERE bill.customerCode = customer.code)";
////                "                  FROM employees" +
////                "                  WHERE departments.department_id = employees.department_id)";
//        Cursor mCursor = database.rawQuery(query, null);
//
//        if(mCursor.getCount() != ZERO && mCursor.moveToFirst())
//        {
//            bill.setAmount(Long.parseLong(mCursor.getString(mCursor.getColumnIndex("SUM"))));
//            bill.setCount(Integer.parseInt(mCursor.getString(mCursor.getColumnIndex("COUNT"))));
//        }
//
//        if (mCursor != null && !mCursor.isClosed()) {
//            mCursor.close();
//        }

        return bill;

    }


    public List<Bill> getBillThuByCodeAndDate(String edong, boolean isMaKH, String customerCode, Calendar dateFrom, Calendar dateTo)
    {
        List<Bill> lst = new ArrayList<>();


//        String query;
//
//        if(isMaKH)
//        {
//            query = "SELECT * FROM " + TABLE_NAME_BILL + " WHERE edongKey = '" + edong +
//                    "' and status != 0 and customerCode like '%" + customerCode + "%'";
//        }else
//        {
//            query = "SELECT * FROM " + TABLE_NAME_BILL + " WHERE edongKey = '" + edong +
//                    "' and status != 0 and name like '%" + customerCode + "%'";
//        }
//
//        Cursor mCursor = database.rawQuery(query, null);
//
//        if (mCursor != null && mCursor.moveToFirst()) {
//            int count = mCursor.getCount();
//            do {
//                String _requestDate = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("requestDate")));
//                if(_requestDate.length() == 0)
//                {
//                    continue;
//                }
//
//                String[] arr = _requestDate.split("/");
//                if (arr.length != 3)
//                {
//                    continue;
//                }
//                Calendar calendar = Calendar.getInstance();
//                calendar.set(intConvertNull(Integer.parseInt(arr[2])), intConvertNull(Integer.parseInt(arr[1])) - 1, intConvertNull(Integer.parseInt(arr[0])));
//
//                if((dateFrom != null && calendar.before(dateFrom)) || (dateTo != null && calendar.after(dateTo)))
//                {
//                    continue;
//                }
//
//
//
//                String _customerCode = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("customerCode")));
//                String _customerPayCode = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("customerPayCode")));
//                String _billId = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("billId")));
//                String _term = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("term")));
//                int _amount = intConvertNull(mCursor.getInt(mCursor.getColumnIndex("amount")));
//                String _period = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("period")));
//                String _issueDate = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("issueDate")));
//                String _strIssueDate = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("strIssueDate")));
//                int _status = intConvertNull(mCursor.getInt(mCursor.getColumnIndex("status")));
//                String _seri = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("seri")));
//                String _pcCode = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("pcCode")));
//                String _handoverCode = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("handoverCode")));
//                String _cashierCode = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("cashierCode")));
//                String _bookCmis = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("bookCmis")));
//                String _fromDate = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("fromDate")));
//                String _toDate = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("toDate")));
//                String _strFromDate = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("strFromDate")));
//                String _strToDate = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("strToDate")));
//                String _home = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("home")));
//                String _tax = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("tax")));
//                String _billNum = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("billNum")));
//                String _currency = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("currency")));
//                String _priceDetails = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("priceDetails")));
//                String _numeDetails = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("numeDetails")));
//                String _amountDetails = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("amountDetails")));
//                String _oldIndex = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("oldIndex")));
//                String _newIndex = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("newIndex")));
//                String _nume = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("nume")));
//                int _amountNotTax = intConvertNull(mCursor.getInt(mCursor.getColumnIndex("amountNotTax")));
//                int _amountTax = intConvertNull(mCursor.getInt(mCursor.getColumnIndex("amountTax")));
//                String _multiple = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("multiple")));
//                String _billType = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("billType")));
//                String _typeIndex = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("typeIndex")));
//                String _groupTypeIndex = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("groupTypeIndex")));
//                String _createdDate = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("createdDate")));
//                int _idChanged = intConvertNull(mCursor.getInt(mCursor.getColumnIndex("idChanged")));
//                String _dateChanged = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("dateChanged")));
//                String _edong = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("edong")));
//                String _pcCodeExt = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("pcCodeExt")));
//                String _code = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("code")));
//                String _name = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("name")));
//                String _nameNosign = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("nameNosign")));
//                String _phoneByevn = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("phoneByevn")));
//                String _phoneByecp = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("phoneByecp")));
//                String _electricityMeter = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("electricityMeter")));
//                String _inning = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("inning")));
//                String _road = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("road")));
//                String _station = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("station")));
//                String _taxCode = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("taxCode")));
//                String _trade = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("trade")));
//                String _countPeriod = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("countPeriod")));
//                String _team = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("team")));
//                int _type = intConvertNull(mCursor.getInt(mCursor.getColumnIndex("type")));
//                String _lastQuery = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("lastQuery")));
//                int _groupType = intConvertNull(mCursor.getInt(mCursor.getColumnIndex("groupType")));
//                String _billingChannel = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("billingChannel")));
//                String _billingType = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("billingType")));
//                String _billingBy = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("billingBy")));
//                String _cashierPay = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("cashierPay")));
//                String _edongKey = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("edongKey")));
//                int _isChecked = intConvertNull(mCursor.getInt(mCursor.getColumnIndex("isChecked")));
//                long _traceNumber = longConvertNull(mCursor.getLong(mCursor.getColumnIndex("customerPayCode")));
//
//                Bill bill = new Bill(_customerCode, _customerPayCode, _billId, _term, _amount, _period, _issueDate, _strIssueDate, _status, _seri, _pcCode, _handoverCode, _cashierCode, _bookCmis, _fromDate, _toDate, _strFromDate, _strToDate, _home, _tax, _billNum, _currency, _priceDetails, _numeDetails, _amountDetails, _oldIndex, _newIndex, _nume, _amountNotTax, _amountTax, _multiple, _billType, _typeIndex, _groupTypeIndex, _createdDate, _idChanged, _dateChanged, _edong, _pcCodeExt, _code, _name, _nameNosign, _phoneByevn, _phoneByecp, _electricityMeter, _inning, _road, _station, _taxCode, _trade, _countPeriod, _team, _type, _lastQuery, _groupType, _billingChannel, _billingType, _billingBy, _cashierPay, _requestDate, _edongKey, _isChecked, _traceNumber);
//                bill.setRequestDateCal(calendar);
//                lst.add(bill);
//
//            }
//            while (mCursor.moveToNext());
//
//            mCursor.close();
//        }


        if (lst.size() > 1)
        {
            Collections.sort(lst, new Comparator<Bill>() {
                @Override
                public int compare(Bill bill, Bill t1) {
                    if(bill.getRequestDateCal().before(t1.getRequestDateCal()))
                        return -1;
                    return 1;
                }
            });
        }

        return lst;
    }


    public List<Bill> getBillHoanTraByCodeAndDate(String edong, boolean isMaKH, String customerCode, Calendar dateFrom, Calendar dateTo)
    {
        List<Bill> lst = new ArrayList<>();


//        String query;
//
//        if(isMaKH)
//        {
//            query = "SELECT * FROM " + TABLE_NAME_BILL + " WHERE edongKey = '" + edong +
//                    "' and status != 0 and customerCode like '%" + customerCode + "%' and billingType IN ('EDONG_OTHER', 'SOURCE_OTHER', 'TIMEOUT', 'REVERT')";
//        }else
//        {
//            query = "SELECT * FROM " + TABLE_NAME_BILL + " WHERE edongKey = '" + edong +
//                    "' and status != 0 and name like '%" + customerCode + "%' and billingType IN ('EDONG_OTHER', 'SOURCE_OTHER', 'TIMEOUT', 'REVERT')";
//        }
//
//        Cursor mCursor = database.rawQuery(query, null);
//
//        if (mCursor != null && mCursor.moveToFirst()) {
//            int count = mCursor.getCount();
//            do {
//                String _requestDate = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("requestDate")));
//                if(_requestDate.length() == 0)
//                {
//                    continue;
//                }
//
//                String[] arr = _requestDate.split("/");
//                if (arr.length != 3)
//                {
//                    continue;
//                }
//                Calendar calendar = Calendar.getInstance();
//                calendar.set(intConvertNull(Integer.parseInt(arr[2])), intConvertNull(Integer.parseInt(arr[1])) - 1, intConvertNull(Integer.parseInt(arr[0])));
//
//                if((dateFrom != null && calendar.before(dateFrom)) || (dateTo != null && calendar.after(dateTo)))
//                {
//                    continue;
//                }
//
//
//
//                String _customerCode = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("customerCode")));
//                String _customerPayCode = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("customerPayCode")));
//                String _billId = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("billId")));
//                String _term = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("term")));
//                int _amount = intConvertNull(mCursor.getInt(mCursor.getColumnIndex("amount")));
//                String _period = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("period")));
//                String _issueDate = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("issueDate")));
//                String _strIssueDate = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("strIssueDate")));
//                int _status = intConvertNull(mCursor.getInt(mCursor.getColumnIndex("status")));
//                String _seri = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("seri")));
//                String _pcCode = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("pcCode")));
//                String _handoverCode = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("handoverCode")));
//                String _cashierCode = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("cashierCode")));
//                String _bookCmis = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("bookCmis")));
//                String _fromDate = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("fromDate")));
//                String _toDate = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("toDate")));
//                String _strFromDate = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("strFromDate")));
//                String _strToDate = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("strToDate")));
//                String _home = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("home")));
//                String _tax = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("tax")));
//                String _billNum = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("billNum")));
//                String _currency = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("currency")));
//                String _priceDetails = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("priceDetails")));
//                String _numeDetails = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("numeDetails")));
//                String _amountDetails = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("amountDetails")));
//                String _oldIndex = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("oldIndex")));
//                String _newIndex = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("newIndex")));
//                String _nume = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("nume")));
//                int _amountNotTax = intConvertNull(mCursor.getInt(mCursor.getColumnIndex("amountNotTax")));
//                int _amountTax = intConvertNull(mCursor.getInt(mCursor.getColumnIndex("amountTax")));
//                String _multiple = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("multiple")));
//                String _billType = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("billType")));
//                String _typeIndex = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("typeIndex")));
//                String _groupTypeIndex = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("groupTypeIndex")));
//                String _createdDate = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("createdDate")));
//                int _idChanged = intConvertNull(mCursor.getInt(mCursor.getColumnIndex("idChanged")));
//                String _dateChanged = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("dateChanged")));
//                String _edong = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("edong")));
//                String _pcCodeExt = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("pcCodeExt")));
//                String _code = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("code")));
//                String _name = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("name")));
//                String _nameNosign = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("nameNosign")));
//                String _phoneByevn = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("phoneByevn")));
//                String _phoneByecp = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("phoneByecp")));
//                String _electricityMeter = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("electricityMeter")));
//                String _inning = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("inning")));
//                String _road = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("road")));
//                String _station = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("station")));
//                String _taxCode = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("taxCode")));
//                String _trade = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("trade")));
//                String _countPeriod = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("countPeriod")));
//                String _team = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("team")));
//                int _type = intConvertNull(mCursor.getInt(mCursor.getColumnIndex("type")));
//                String _lastQuery = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("lastQuery")));
//                int _groupType = intConvertNull(mCursor.getInt(mCursor.getColumnIndex("groupType")));
//                String _billingChannel = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("billingChannel")));
//                String _billingType = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("billingType")));
//                String _billingBy = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("billingBy")));
//                String _cashierPay = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("cashierPay")));
//                String _edongKey = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("edongKey")));
//                int _isChecked = intConvertNull(mCursor.getInt(mCursor.getColumnIndex("isChecked")));
//                long _traceNumber = longConvertNull(mCursor.getLong(mCursor.getColumnIndex("customerPayCode")));
//
//                Bill bill = new Bill(_customerCode, _customerPayCode, _billId, _term, _amount, _period, _issueDate, _strIssueDate, _status, _seri, _pcCode, _handoverCode, _cashierCode, _bookCmis, _fromDate, _toDate, _strFromDate, _strToDate, _home, _tax, _billNum, _currency, _priceDetails, _numeDetails, _amountDetails, _oldIndex, _newIndex, _nume, _amountNotTax, _amountTax, _multiple, _billType, _typeIndex, _groupTypeIndex, _createdDate, _idChanged, _dateChanged, _edong, _pcCodeExt, _code, _name, _nameNosign, _phoneByevn, _phoneByecp, _electricityMeter, _inning, _road, _station, _taxCode, _trade, _countPeriod, _team, _type, _lastQuery, _groupType, _billingChannel, _billingType, _billingBy, _cashierPay, _requestDate, _edongKey, _isChecked, _traceNumber);
//                bill.setRequestDateCal(calendar);
//                lst.add(bill);
//
//            }
//            while (mCursor.moveToNext());
//
//            mCursor.close();
//        }


        if (lst.size() > 1)
        {
            Collections.sort(lst, new Comparator<Bill>() {
                @Override
                public int compare(Bill bill, Bill t1) {
                    if(bill.getRequestDateCal().before(t1.getRequestDateCal()))
                        return -1;
                    return 1;
                }
            });
        }

        return lst;
    }

    public int countBill(String edong) {
        if (edong == null)
            return 0;

        database = this.getReadableDatabase();

//        String query = "SELECT COUNT(*) AS COUNT FROM " + TABLE_NAME_BILL + " WHERE E_DONG = '" + edong + "'";// + "' and status = " + ZERO;
        String query = "SELECT COUNT(*) AS COUNT FROM " + TABLE_NAME_BILL + " WHERE edongKey = '" + edong + "' and status = " + ZERO;
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


    public List<TransactionOffItem> selectOfflineBill() {
        database = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_DEBT_COLLECTION + " WHERE E_DONG = '" + MainActivity.mEdong + "' and HINH_THUC_TT = '" + Common.HINH_THUC_TTOAN.OFFLINE.getCode() + "' and TRANG_THAI_DAY_CHAM_NO = '" + Common.TRANG_THAI_DAY_CHAM_NO.CHUA_DAY.getCode() + "'";
        Cursor mCursor = database.rawQuery(query, null);

        List<TransactionOffItem> lst = new ArrayList<>();

        if(mCursor != null && mCursor.moveToFirst())
        {
            do {

                String MA_HOA_DON = mCursor.getString(mCursor.getColumnIndex("MA_HOA_DON"));
                String currentDate = Common.getDateTimeNow(Common.DATE_TIME_TYPE.yyyyMMddHHmmssSSS);

                Common.TRANG_THAI_TTOAN trangThaiTtoan = getTrangThaiThanhToanHoaDonNo(MA_HOA_DON);
                if (trangThaiTtoan.equal(Common.TRANG_THAI_TTOAN.NULL))
                    continue;;

                if (trangThaiTtoan.equal(Common.TRANG_THAI_TTOAN.TTOAN_BOI_NGUON_KHAC))
                {

                    this.updateHoaDonThu(MA_HOA_DON, Common.TRANG_THAI_TTOAN.TTOAN_BOI_NGUON_KHAC, "", Common.TRANG_THAI_DAY_CHAM_NO.KHONG_THANH_CONG.getCode(),
                            currentDate, Common.TRANG_THAI_HOAN_TRA.CHUA_TRA.getCode());


                    this.insertLichSuThanhToan(MA_HOA_DON, currentDate, Common.MA_GIAO_DICH.DAY_CHAM_NO.getCode());
                    continue;
                }
                if (trangThaiTtoan.equal(Common.TRANG_THAI_TTOAN.TTOAN_BOI_VI_KHAC))
                {

                    this.updateHoaDonThu(MA_HOA_DON, Common.TRANG_THAI_TTOAN.TTOAN_BOI_VI_KHAC, "", Common.TRANG_THAI_DAY_CHAM_NO.KHONG_THANH_CONG.getCode(),
                            currentDate, Common.TRANG_THAI_HOAN_TRA.CHUA_TRA.getCode());


                    this.insertLichSuThanhToan(MA_HOA_DON, currentDate, Common.MA_GIAO_DICH.DAY_CHAM_NO.getCode());
                    continue;
                }

                if (trangThaiTtoan.equal(Common.TRANG_THAI_TTOAN.CHUA_TTOAN))
                {

                    TransactionOffItem item = new TransactionOffItem();
                    item.setCustomer_code(mCursor.getString(mCursor.getColumnIndex("MA_KHANG")));
                    item.setAmount(mCursor.getLong(mCursor.getColumnIndex("SO_TIEN_TTOAN")));
                    item.setBill_id(Long.parseLong(mCursor.getString(mCursor.getColumnIndex("MA_HOA_DON"))));
                    item.setEdong(mCursor.getString(mCursor.getColumnIndex("E_DONG")));
                    lst.add(item);
                }


            }while (mCursor.moveToNext());
        }


        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }
        return  lst;
    }


    public  void updateHoaDonThu(String MA_HOA_DON, Common.TRANG_THAI_TTOAN TRANG_THAI_TTOAN, String TRANG_THAI_CHAM_NO,
                                 String TRANG_THAI_DAY_CHAM_NO, String NGAY_DAY, String TRANG_THAI_HOAN_TRA)
    {
        database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("TRANG_THAI_TTOAN", TRANG_THAI_TTOAN.getCode());
        contentValues.put("TRANG_THAI_CHAM_NO", TRANG_THAI_CHAM_NO);
        contentValues.put("TRANG_THAI_DAY_CHAM_NO", TRANG_THAI_DAY_CHAM_NO);
        contentValues.put("NGAY_DAY", NGAY_DAY);
        contentValues.put("TRANG_THAI_HOAN_TRA", TRANG_THAI_HOAN_TRA);
        database.update(TABLE_NAME_BILL, contentValues, "MA_HOA_DON = ?", new String[]{MA_HOA_DON});
    }


    public Common.TRANG_THAI_TTOAN getTrangThaiThanhToanHoaDonNo(String MA_HOA_DON)
    {
        database = this.getReadableDatabase();
        String query = "SELECT TRANG_THAI_TTOAN FROM " + TABLE_NAME_BILL + " WHERE E_DONG = '" + MainActivity.mEdong + "' and MA_HOA_DON = '" + MA_HOA_DON + "'";
        Cursor mCursor = database.rawQuery(query, null);
        if(mCursor != null && mCursor.moveToFirst())
        {

            if (mCursor != null && !mCursor.isClosed()) {
                mCursor.close();
            }
            return Common.TRANG_THAI_TTOAN.valueOf(mCursor.getString(mCursor.getColumnIndex("TRANG_THAI_TTOAN")));
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

    public EntityHoaDonNo getHoaDonNo(long billID)
    {
        EntityHoaDonNo entityHoaDonNo = new EntityHoaDonNo();
        database = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_BILL + " WHERE MA_HOA_DON ='" + billID + "'";
        Cursor c = database.rawQuery(query, null);

        if(c.moveToFirst())
        {
            entityHoaDonNo.setE_DONG(c.getString(c.getColumnIndex("E_DONG")));
            entityHoaDonNo.setMA_HOA_DON(c.getString(c.getColumnIndex("MA_HOA_DON")));
            entityHoaDonNo.setSERI_HDON(c.getString(c.getColumnIndex("SERI_HDON")));
            entityHoaDonNo.setMA_KHANG(c.getString(c.getColumnIndex("MA_KHANG")));
            entityHoaDonNo.setMA_THE(c.getString(c.getColumnIndex("MA_THE")));
            entityHoaDonNo.setTEN_KHANG(c.getString(c.getColumnIndex("TEN_KHANG")));
            entityHoaDonNo.setDIA_CHI(c.getString(c.getColumnIndex("DIA_CHI")));
            entityHoaDonNo.setTHANG_TTOAN(Common.parseDate(c.getString(c.getColumnIndex("THANG_TTOAN")), Common.DATE_TIME_TYPE.yyyyMMddHHmmssSSS.toString()));
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
            entityHoaDonNo.setNGAY_GIAO_THU(Common.parseDate(c.getString(c.getColumnIndex("NGAY_GIAO_THU")), Common.DATE_TIME_TYPE.yyyyMMddHHmmssSSS.toString()));
            entityHoaDonNo.setTRANG_THAI_TTOAN(c.getString(c.getColumnIndex("TRANG_THAI_TTOAN")));
            entityHoaDonNo.setVI_TTOAN(c.getString(c.getColumnIndex("VI_TTOAN")));

        }


        if (c != null && !c.isClosed()) {
            c.close();
        }
        return entityHoaDonNo;
    }

    public List<EntityKhachHang> selectAllCustomerFitterBy(String edong, Common.TYPE_SEARCH typeSearch, String infoSearch) {
        List<EntityKhachHang> customerList = new ArrayList<>();

        boolean fail = TextUtils.isEmpty(edong) || TextUtils.isEmpty(infoSearch) && typeSearch.getPosition() != Common.TYPE_SEARCH.ALL.getPosition();
        if (fail)
            return customerList;

        String query = null;
        switch (typeSearch.getPosition()) {
            case 0:
                query = "SELECT * FROM " + TABLE_NAME_CUSTOMER + " WHERE E_DONG = '" + edong + "'";
                break;
            case 1:
                query = "SELECT * FROM " + TABLE_NAME_CUSTOMER + " WHERE E_DONG = '" + edong + "' and MA_KHANG like '%" + infoSearch + "%'";
                break;
            case 2:
                query = "SELECT * FROM " + TABLE_NAME_CUSTOMER + " WHERE E_DONG = '" + edong + "' and TEN_KHANG like '%" + infoSearch + "%'";
                break;
            case 3:
                query = "SELECT * FROM " + TABLE_NAME_CUSTOMER + " WHERE E_DONG = '" + edong + "' and SDT_EVN like '%" + infoSearch + "%'";
                break;
            case 4:
                query = "SELECT * FROM " + TABLE_NAME_CUSTOMER + " WHERE E_DONG = '" + edong + "' and DIA_CHI like '%" + infoSearch + "%'";
                break;
            case 5:
                query = "SELECT * FROM " + TABLE_NAME_CUSTOMER + " WHERE E_DONG = '" + edong + "' and SO_GCS like '%" + infoSearch + "%'";
                break;
            case 6:
                query = "SELECT * FROM " + TABLE_NAME_CUSTOMER + " WHERE E_DONG = '" + edong + "' and LO_TRINH like '%" + infoSearch + "%'";
                break;

          /*  case 7:
                query = "SELECT * FROM " + TABLE_NAME_CUSTOMER + " WHERE E_DONG = '" + edong
                        + "' and code like '%" + infoSearch
                        + "%' or name like '%" + infoSearch
                        + "%' or phoneByevn like '%" + infoSearch
                        + "%' or address like '%" + infoSearch
                        + "%' or bookCmis like '%" + infoSearch
                        + "%' or road like '%" + infoSearch
                        + "%' ";
                break;*/
        }

        database = this.getWritableDatabase();
        Cursor c = database.rawQuery(query, null);

        if (c != null && c.moveToFirst()) {
            int count = c.getCount();
            do {


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
                khachHang.setNGAY_GIAO_THU(Common.parseDate(c.getString(c.getColumnIndex("NGAY_GIAO_THU")), Common.DATE_TIME_TYPE.yyyyMMddHHmmssSSS.toString()));

                customerList.add(khachHang);
            }
            while (c.moveToNext());

            c.close();
        }
        return customerList;
    }


    public List<EntityKhachHang> selectAllCustomerFitter(String _maKH, String _name, String _address, String _phone, String _bookCmis) {
        List<EntityKhachHang> customerList = new ArrayList<>();


        String query = "SELECT * FROM " + TABLE_NAME_CUSTOMER + " WHERE ";
        if(_maKH.length() != 0)
        {
            query += "MA_KHANG like '%" + _maKH + "%' or ";
            query += "MA_THE like '%" + _maKH + "%' ";
        }else
        {
            boolean hasWhere = false;

            if(_name.length() > 0)
            {
                query += (hasWhere ? "and "  : "") + "TEN_KHANG like '%" + _name + "%' ";
                hasWhere = true;
            }

            if(_address.length() > 0)
            {
                query += (hasWhere ? "and "  : "") + "DIA_CHI like '%" + _address + "%' ";
                hasWhere = true;
            }

            if(_phone.length() > 0)
            {
                query += (hasWhere ? "and "  : "") + "SDT_ECPAY like '%" + _phone + "%' ";
                hasWhere = true;
            }

            if(_bookCmis.length() > 0)
            {
                query += (hasWhere ? "and "  : "") + "SO_GCS like '%" + _bookCmis + "%' ";
                hasWhere = true;
            }
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
                customer.setNGAY_GIAO_THU(Common.parseDate(stringConvertNull(mCursor.getString(mCursor.getColumnIndex("NGAY_GIAO_THU"))), Common.DATE_TIME_TYPE.yyyyMMddHHmmssSSS.toString()));


                customerList.add(customer);
            }
            while (mCursor.moveToNext());

            mCursor.close();
        }
        return customerList;
    }

    public Pair<List<PayAdapter.BillEntityAdapter>, Long> selectInfoBillOfCustomerToRecycler(String edong, String code) {

        List<PayAdapter.BillEntityAdapter> billList = new ArrayList<>();

        database = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_BILL + " WHERE E_DONG = '" + edong + "' and MA_KHANG ='" + code + "' ORDER BY THANG_TTOAN DESC";
        Cursor mCursor = database.rawQuery(query, null);

        long total = 0;
        if (mCursor.getCount() == 0)
            return new Pair<>(billList, total);
        if(mCursor.moveToFirst())
        {
            do {
                String viThanhToan = mCursor.getString(mCursor.getColumnIndex("VI_TTOAN"));
                int billId = intConvertNull(mCursor.getInt(mCursor.getColumnIndex("MA_HOA_DON")));
                int amount = intConvertNull(mCursor.getInt(mCursor.getColumnIndex("SO_TIEN_TTOAN")));
                String status = mCursor.getString(mCursor.getColumnIndex("TRANG_THAI_TTOAN"));

                total += amount;

                String term = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("THANG_TTOAN")));
                term = Common.convertDateToDate(term, yyyyMMddHHmmssSSS, Common.DATE_TIME_TYPE.MMyyyy);

                String dateRequest = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("NGAY_GIAO_THU")));

                PayAdapter.BillEntityAdapter bill = new PayAdapter.BillEntityAdapter();
                bill.setBillId(billId);
                bill.setVI_TTOAN(viThanhToan);
                bill.setTIEN_THANH_TOAN(amount);
                bill.setTHANG_THANH_TOAN(term);
                bill.setTRANG_THAI_TT(status);
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
//        String query = "SELECT SUM(SO_TIEN_TTOAN) FROM " + TABLE_NAME_BILL + " WHERE E_DONG = '" + edong + "'";// and status = " + ZERO;
        String query = "SELECT SUM(amount) FROM " + TABLE_NAME_BILL + " WHERE edongKey = '" + edong + "' and status = " + ZERO;
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
        initialValues.put("NGAY_GIAO_THU", Common.parse(customer.getNGAY_GIAO_THU(), Common.DATE_TIME_TYPE.yyyyMMddHHmmssSSS.toString()));
        database = getWritableDatabase();

        String query = "SELECT * FROM " + TABLE_NAME_CUSTOMER + " WHERE MA_KHANG = '" + customer.getMA_KHANG() + "'";
        Cursor mCursor = database.rawQuery(query, null);
        if (mCursor.moveToFirst()) {
            database.update(TABLE_NAME_CUSTOMER, initialValues, "MA_KHANG=?", new String[]{String.valueOf(customer.getMA_KHANG())});
        }else
        {

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

        initialValues.put("THANG_TTOAN", bodyBillResponse.getTerm());
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
        initialValues.put("NGAY_GIAO_THU", Common.getDateTimeNow(Common.DATE_TIME_TYPE.yyyyMMddHHmmssSSS));
        initialValues.put("TRANG_THAI_TTOAN", Common.TRANG_THAI_TTOAN.CHUA_TTOAN.getCode());
        initialValues.put("VI_TTOAN", "");

        database = getWritableDatabase();


        String query = "SELECT * FROM " + TABLE_NAME_BILL + " WHERE MA_HOA_DON = '" + bodyBillResponse.getBillId() + "'";
        Cursor mCursor = database.rawQuery(query, null);
        if (mCursor.moveToFirst()) {
            database.update(TABLE_NAME_BILL, initialValues, "MA_HOA_DON=?", new String[]{String.valueOf(bodyBillResponse.getBillId())});
        }else
        {
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
    public void insertOrUpdateEvnPcFromLoginReponse(EvnPC evnPC) {
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

        database = getWritableDatabase();
        int id = (int) database.insertWithOnConflict(TABLE_NAME_EVN_PC, null, initialValues, SQLiteDatabase.CONFLICT_IGNORE);
        if (id == -1) {
            database.update(TABLE_NAME_EVN_PC, initialValues, "pcId=?", new String[]{String.valueOf(evnPC.getPcId())});
        }
    }

    public long insertEvnPC(ListEvnPCResponse listEvnPCResponse) {
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

        database = getWritableDatabase();
        int rowAffect = (int) database.insert(TABLE_NAME_EVN_PC, null, initialValues);
        return rowAffect;
    }

    public long deleteAllPC() {
        database = this.getWritableDatabase();
        return database.delete(TABLE_NAME_EVN_PC, null, null);
    }

    public long checkEvnPCExist(int pcId) {
        database = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + TABLE_NAME_EVN_PC + " WHERE pcId = " + pcId;
        Cursor mCursor = database.rawQuery(query, null);
        if (mCursor.moveToFirst())
            return mCursor.getInt(0);
        return 0;
    }

    public String getPcCode() {
        database = this.getReadableDatabase();
        String query = "SELECT ext FROM " + TABLE_NAME_EVN_PC;
        Cursor c = database.rawQuery(query, null);
        if (c.moveToFirst()) {
            return c.getString(0);
        }
        return "";
    }

    public List<String> getPcCodes() {

        List<String> result = new ArrayList<>();

        database = this.getReadableDatabase();
        String query = "SELECT ext FROM " + TABLE_NAME_EVN_PC;
        Cursor c = database.rawQuery(query, null);
        if (c.moveToFirst()) {
            do {
                result.add(c.getString(0));
            }while (c.moveToNext());
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
        initialValues.put("NGAY_GIAO_THU", Common.getDateTimeNow(Common.DATE_TIME_TYPE.yyyyMMddHHmmssSSS));
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
        initialValues.put("NGAY_GIAO_THU", Common.getDateTimeNow(Common.DATE_TIME_TYPE.yyyyMMddHHmmssSSS));

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

    public long updateCustomer(Customer customer) {
        ContentValues initialValues = new ContentValues();

        initialValues.put("name", customer.getName());
        initialValues.put("address", customer.getAddress());
        initialValues.put("pcCode", customer.getPcCode());
        initialValues.put("cardNo", customer.getCardNo());
        initialValues.put("pcCodeExt", customer.getPcCodeExt());
        initialValues.put("phoneByevn", customer.getPhoneByevn());
        initialValues.put("phoneByecp", customer.getPhoneByecp());
        initialValues.put("bookCmis", customer.getBookCmis());
        initialValues.put("electricityMeter", customer.getElectricityMeter());
        initialValues.put("inning", customer.getInning());
        initialValues.put("status", customer.getStatus());
        initialValues.put("bankAccount", "");
        initialValues.put("idNumber", customer.getIdNumber());
        initialValues.put("bankName", "");
        initialValues.put("isShowBill", 0);
        initialValues.put("idChanged", customer.getIdChanged());
        initialValues.put("dateChanged", customer.getDateChanged());

        database = getWritableDatabase();
        int rowAffect = (int) database.update(TABLE_NAME_CUSTOMER, initialValues, "code = ?", new String[]{customer.getCode()});
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
            khachHang.setNGAY_GIAO_THU(Common.parseDate(c.getString(c.getColumnIndex("NGAY_GIAO_THU")), Common.DATE_TIME_TYPE.yyyyMMddHHmmssSSS.toString()));

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

        initialValues.put("THANG_TTOAN", bodyBillResponse.getTerm());
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
        initialValues.put("NGAY_GIAO_THU", Common.getDateTimeNow(Common.DATE_TIME_TYPE.yyyyMMddHHmmssSSS));
        initialValues.put("TRANG_THAI_TTOAN", Common.TRANG_THAI_TTOAN.CHUA_TTOAN.getCode());
        initialValues.put("VI_TTOAN", "");
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

        initialValues.put("THANG_TTOAN", bodyBillResponse.getTerm());
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
        initialValues.put("NGAY_GIAO_THU", Common.getDateTimeNow(Common.DATE_TIME_TYPE.yyyyMMddHHmmssSSS));
        initialValues.put("TRANG_THAI_TTOAN", Common.TRANG_THAI_TTOAN.CHUA_TTOAN.getCode());
        initialValues.put("VI_TTOAN", "");

        database = getWritableDatabase();
        int rowAffect = (int) database.insert(TABLE_NAME_BILL, null, initialValues);
        return rowAffect;
    }

    public long insertLichSuThanhToan( views.ecpay.com.postabletecpay.util.entities.response.EntityBill.BodyBillResponse bodyBillResponse, String HINH_THUC_TT, String TRANG_THAI_TT, String TRANG_THAI_CHAM_NO, String TRANG_THAI_HUY,
                                       String TRANG_THAI_NGHI_NGO, int SO_IN_BIEN_NHAN, String IN_THONG_BAO_DIEN, String NGAY_PHAT_SINH,
                                       String MA_GIAO_DICH)
    {
        ContentValues initialValues = new ContentValues();


        initialValues.put("MA_KHANG", bodyBillResponse.getCustomerCode());
        initialValues.put("E_DONG", bodyBillResponse.getEdong());
        initialValues.put("MA_HOA_DON", bodyBillResponse.getBillId());
        initialValues.put("SERI_HDON", bodyBillResponse.getSeri());


        initialValues.put("MA_THE", bodyBillResponse.getCardNo());
        initialValues.put("TEN_KHANG", bodyBillResponse.getName());
        initialValues.put("DIA_CHI", bodyBillResponse.getAddress());

        initialValues.put("THANG_TTOAN", bodyBillResponse.getTerm());
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
        initialValues.put("NGAY_GIAO_THU", Common.getDateTimeNow(Common.DATE_TIME_TYPE.yyyyMMddHHmmssSSS));
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

    public void insertLichSuThanhToan(String  MA_HOA_DON, String NGAY_PHAT_SINH, String MA_GIAO_DICH)
    {
        database = getWritableDatabase();

        String query = "SELECT * FROM " + TABLE_NAME_DEBT_COLLECTION + " WHERE MA_HOA_DON = " + MA_HOA_DON;
        Cursor hoaDonThu = database.rawQuery(query, null);
        if(hoaDonThu == null || !hoaDonThu.moveToFirst())
            return;

        ContentValues initialValues = new ContentValues();

        initialValues.put("MA_KHANG", hoaDonThu.getString(hoaDonThu.getColumnIndex("MA_KHANG")));
        initialValues.put("E_DONG", hoaDonThu.getString(hoaDonThu.getColumnIndex("E_DONG")));
        initialValues.put("MA_HOA_DON",hoaDonThu.getString(hoaDonThu.getColumnIndex("MA_HOA_DON")));
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

    public long insertLichSuThanhToan( EntityLichSuThanhToan lichSuThanhToan)
    {
        ContentValues initialValues = new ContentValues();


        initialValues.put("MA_KHANG", lichSuThanhToan.getMA_KHANG());
        initialValues.put("E_DONG", lichSuThanhToan.getE_DONG());
        initialValues.put("MA_HOA_DON", lichSuThanhToan.getMA_HOA_DON());
        initialValues.put("SERI_HDON", lichSuThanhToan.getSERI_HDON());


        initialValues.put("MA_THE", lichSuThanhToan.getMA_THE());
        initialValues.put("TEN_KHANG", lichSuThanhToan.getTEN_KHANG());
        initialValues.put("DIA_CHI", lichSuThanhToan.getDIA_CHI());

        initialValues.put("THANG_TTOAN", Common.parse(lichSuThanhToan.getTHANG_TTOAN(), Common.DATE_TIME_TYPE.yyyyMMddHHmmssSSS.toString()));
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
        initialValues.put("NGAY_GIAO_THU", Common.parse(lichSuThanhToan.getNGAY_GIAO_THU(), Common.DATE_TIME_TYPE.yyyyMMddHHmmssSSS.toString()));
        initialValues.put("VI_TTOAN", lichSuThanhToan.getVI_TTOAN());



        initialValues.put("HINH_THUC_TT", lichSuThanhToan.getHINH_THUC_TT());
        initialValues.put("TRANG_THAI_TTOAN", lichSuThanhToan.getTRANG_THAI_TTOAN());
        initialValues.put("TRANG_THAI_CHAM_NO", lichSuThanhToan.getTRANG_THAI_CHAM_NO());
        initialValues.put("TRANG_THAI_HUY", lichSuThanhToan.getTRANG_THAI_HUY());
        initialValues.put("TRANG_THAI_NGHI_NGO", lichSuThanhToan.getTRANG_THAI_NGHI_NGO());
        initialValues.put("SO_IN_BIEN_NHAN", lichSuThanhToan.getSO_IN_BIEN_NHAN());
        initialValues.put("IN_THONG_BAO_DIEN", lichSuThanhToan.getIN_THONG_BAO_DIEN());
        initialValues.put("NGAY_PHAT_SINH", Common.parse(lichSuThanhToan.getNGAY_PHAT_SINH(), Common.DATE_TIME_TYPE.yyyyMMddHHmmssSSS.toString()));
        initialValues.put("MA_GIAO_DICH", lichSuThanhToan.getMA_GIAO_DICH());


        database = getWritableDatabase();
        int rowAffect = (int) database.insert(TABLE_NAME_HISTORY_PAY, null, initialValues);
        return rowAffect;
    }

    public void insertHoaDonThu( views.ecpay.com.postabletecpay.util.entities.response.EntityBill.BodyBillResponse bodyBillResponse, String HINH_THUC_TT,
                                 String TRANG_THAI_TT, String TRANG_THAI_CHAM_NO, String TRANG_THAI_HUY,
                                       String TRANG_THAI_HOAN_TRA,String TRANG_THAI_XU_LY_NGHI_NGO, String TRANG_THAI_DAY_CHAM_NO, int SO_LAN_IN_BIEN_NHAN,
                                        String NGAY_DAY, String IN_THONG_BAO_DIEN)
    {

        database = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_DEBT_COLLECTION + " WHERE MA_HOA_DON = '" + bodyBillResponse.getBillId() + "'";
        Cursor mCursor = database.rawQuery(query, null);
        if (mCursor.moveToFirst())
        {
            ContentValues initialValues = new ContentValues();


            initialValues.put("MA_KHANG", bodyBillResponse.getCustomerCode());
            initialValues.put("E_DONG", bodyBillResponse.getEdong());
            initialValues.put("MA_HOA_DON", bodyBillResponse.getBillId());
            initialValues.put("SERI_HDON", bodyBillResponse.getSeri());


            initialValues.put("MA_THE", bodyBillResponse.getCardNo());
            initialValues.put("TEN_KHANG", bodyBillResponse.getName());
            initialValues.put("DIA_CHI", bodyBillResponse.getAddress());

            initialValues.put("THANG_TTOAN", bodyBillResponse.getTerm());
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
            initialValues.put("NGAY_GIAO_THU", Common.getDateTimeNow(Common.DATE_TIME_TYPE.yyyyMMddHHmmssSSS));
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

            database.update(TABLE_NAME_DEBT_COLLECTION, initialValues, "MA_HOA_DON=?", new String[]{String.valueOf(bodyBillResponse.getBillId())});
        }else
        {

            ContentValues initialValues = new ContentValues();


            initialValues.put("MA_KHANG", bodyBillResponse.getCustomerCode());
            initialValues.put("E_DONG", bodyBillResponse.getEdong());
            initialValues.put("MA_HOA_DON", bodyBillResponse.getBillId());
            initialValues.put("SERI_HDON", bodyBillResponse.getSeri());


            initialValues.put("MA_THE", bodyBillResponse.getCardNo());
            initialValues.put("TEN_KHANG", bodyBillResponse.getName());
            initialValues.put("DIA_CHI", bodyBillResponse.getAddress());

            initialValues.put("THANG_TTOAN", bodyBillResponse.getTerm());
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
            initialValues.put("NGAY_GIAO_THU", Common.getDateTimeNow(Common.DATE_TIME_TYPE.yyyyMMddHHmmssSSS));
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

            int rowAffect = (int) database.insert(TABLE_NAME_DEBT_COLLECTION, null, initialValues);
        }
    }


    public void insertHoaDonThu(EntityHoaDonThu entityHoaDonThu)
    {

        database = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_DEBT_COLLECTION + " WHERE MA_HOA_DON = '" + entityHoaDonThu.getMA_HOA_DON() + "'";
        Cursor mCursor = database.rawQuery(query, null);
        if (mCursor.moveToFirst())
        {
            ContentValues initialValues = new ContentValues();


            initialValues.put("MA_KHANG", entityHoaDonThu.getMA_KHANG());
            initialValues.put("E_DONG", entityHoaDonThu.getE_DONG());
            initialValues.put("MA_HOA_DON", entityHoaDonThu.getMA_HOA_DON());
            initialValues.put("SERI_HDON", entityHoaDonThu.getSERI_HDON());


            initialValues.put("MA_THE", entityHoaDonThu.getMA_THE());
            initialValues.put("TEN_KHANG", entityHoaDonThu.getTEN_KHANG());
            initialValues.put("DIA_CHI", entityHoaDonThu.getDIA_CHI());

            initialValues.put("THANG_TTOAN", Common.parse(entityHoaDonThu.getTHANG_TTOAN(), Common.DATE_TIME_TYPE.yyyyMMddHHmmssSSS.toString()));
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
            initialValues.put("NGAY_GIAO_THU", Common.parse(entityHoaDonThu.getNGAY_GIAO_THU(), Common.DATE_TIME_TYPE.yyyyMMddHHmmssSSS.toString()));
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
            initialValues.put("NGAY_DAY", Common.parse(entityHoaDonThu.getNGAY_DAY(), Common.DATE_TIME_TYPE.yyyyMMddHHmmssSSS.toString()));

            database.update(TABLE_NAME_DEBT_COLLECTION, initialValues, "MA_HOA_DON=?", new String[]{String.valueOf(entityHoaDonThu.getMA_HOA_DON())});
        }else
        {

            ContentValues initialValues = new ContentValues();

            initialValues.put("MA_KHANG", entityHoaDonThu.getMA_KHANG());
            initialValues.put("E_DONG", entityHoaDonThu.getE_DONG());
            initialValues.put("MA_HOA_DON", entityHoaDonThu.getMA_HOA_DON());
            initialValues.put("SERI_HDON", entityHoaDonThu.getSERI_HDON());


            initialValues.put("MA_THE", entityHoaDonThu.getMA_THE());
            initialValues.put("TEN_KHANG", entityHoaDonThu.getTEN_KHANG());
            initialValues.put("DIA_CHI", entityHoaDonThu.getDIA_CHI());

            initialValues.put("THANG_TTOAN", Common.parse(entityHoaDonThu.getTHANG_TTOAN(), Common.DATE_TIME_TYPE.yyyyMMddHHmmssSSS.toString()));
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
            initialValues.put("NGAY_GIAO_THU", Common.parse(entityHoaDonThu.getNGAY_GIAO_THU(), Common.DATE_TIME_TYPE.yyyyMMddHHmmssSSS.toString()));
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
            initialValues.put("NGAY_DAY", Common.parse(entityHoaDonThu.getNGAY_DAY(), Common.DATE_TIME_TYPE.yyyyMMddHHmmssSSS.toString()));


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

        if(status == 1 && bodyBillResponse.getBillingType().equalsIgnoreCase("SOURCE_OTHER")) //Hoá đơn được thanh toán từ nguồn khác:
        {
            initialValues.put("TRANG_THAI_TTOAN", Common.TRANG_THAI_TTOAN.DA_TTOAN.getCode());
            this.insertLichSuThanhToan(bodyBillResponse, "", "03", "", "", "", 0, "", Common.getDateTimeNow(Common.DATE_TIME_TYPE.yyyyMMddHHmmssSSS), "02");
        }else if(status == 1 && bodyBillResponse.getBillingType().equalsIgnoreCase("EDONG_OTHER")) //Hoá đơn được thanh toán bởi số ví khác:
        {
            initialValues.put("TRANG_THAI_TTOAN", Common.TRANG_THAI_TTOAN.DA_TTOAN.getCode());
            initialValues.put("VI_TTOAN", bodyBillResponse.getBillingBy());

            this.insertLichSuThanhToan(bodyBillResponse, "", "04", "", "", "", 0, "", Common.getDateTimeNow(Common.DATE_TIME_TYPE.yyyyMMddHHmmssSSS), "02");

            //Chấm nợ thành công
        }else if(status == 1 && bodyBillResponse.getBillingType().equalsIgnoreCase("BILLING") && bodyBillResponse.getBillingBy().equalsIgnoreCase(MainActivity.mEdong))
        {
            initialValues.put("TRANG_THAI_TTOAN", Common.TRANG_THAI_TTOAN.DA_TTOAN.getCode());

            this.insertLichSuThanhToan(bodyBillResponse, "", "02", "02", "", "", 0, "", Common.getDateTimeNow(Common.DATE_TIME_TYPE.yyyyMMddHHmmssSSS), "07");
            this.insertHoaDonThu(bodyBillResponse, "", "02", "02", "", "", "", "", 0, "", "");

            //Chấm nợ không thành công, thanh toan bởi nguồn khác
        }else if(status != 1 && bodyBillResponse.getBillingType().equalsIgnoreCase("SOURCE_OTHER") && bodyBillResponse.getBillingBy().equalsIgnoreCase(MainActivity.mEdong))
        {
            initialValues.put("TRANG_THAI_TTOAN", Common.TRANG_THAI_TTOAN.DA_TTOAN.getCode());

            this.insertLichSuThanhToan(bodyBillResponse, "", "03", "05", "", "", 0, "", Common.getDateTimeNow(Common.DATE_TIME_TYPE.yyyyMMddHHmmssSSS), "07");
            this.insertHoaDonThu(bodyBillResponse, "", "03", "05", "", "01", "", "", 0, "", "");

            //Chấm nợ không thành công, thanh toan bởi ví khác
        }else if(status != 1 && bodyBillResponse.getBillingType().equalsIgnoreCase("EDONG_OTHER"))
        {
            initialValues.put("TRANG_THAI_TTOAN", Common.TRANG_THAI_TTOAN.DA_TTOAN.getCode());
            initialValues.put("VI_TTOAN", bodyBillResponse.getBillingBy());

            this.insertLichSuThanhToan(bodyBillResponse, "", "04", "05", "", "", 0, "", Common.getDateTimeNow(Common.DATE_TIME_TYPE.yyyyMMddHHmmssSSS), "07");
            this.insertHoaDonThu(bodyBillResponse, "", "04", "05", "", "01", "", "", 0, "", "");

            //Chấm nợ không thành công, chấm
        }else if(status != 1 && bodyBillResponse.getBillingType().equalsIgnoreCase("ERROR") && bodyBillResponse.getBillingBy().equalsIgnoreCase(MainActivity.mEdong))
        {
            initialValues.put("TRANG_THAI_TTOAN", Common.TRANG_THAI_TTOAN.CHUA_TTOAN.getCode());

            this.insertLichSuThanhToan(bodyBillResponse, "", "02", "04", "", "", 0, "", Common.getDateTimeNow(Common.DATE_TIME_TYPE.yyyyMMddHHmmssSSS), "07");
            this.insertHoaDonThu(bodyBillResponse, "", "02", "04", "", "", "", "", 0, "", "");

            //Huỷ hoá đơn thành
        }else if(bodyBillResponse.getBillingType().equalsIgnoreCase("REVERT")) {
            if (bodyBillResponse.getBillingBy().equalsIgnoreCase(MainActivity.mEdong)) {
                initialValues.put("TRANG_THAI_TTOAN", Common.TRANG_THAI_TTOAN.CHUA_TTOAN.getCode());
                initialValues.put("VI_TTOAN", "");

                this.insertLichSuThanhToan(bodyBillResponse, "", "01", "", "01", "", 0, "", Common.getDateTimeNow(Common.DATE_TIME_TYPE.yyyyMMddHHmmssSSS), "08");
                this.insertHoaDonThu(bodyBillResponse, "", "", "", "01", "01", "", "", 0, "", "");

            } else {//Huỷ hoá đơn thành, tk != tk dang nhap
                initialValues.put("TRANG_THAI_TTOAN", Common.TRANG_THAI_TTOAN.CHUA_TTOAN.getCode());
                initialValues.put("VI_TTOAN", "");

                this.insertLichSuThanhToan(bodyBillResponse, "", "01", "", "01", "", 0, "", Common.getDateTimeNow(Common.DATE_TIME_TYPE.yyyyMMddHHmmssSSS), "08");
            }
        }else if(status != 1 && bodyBillResponse.getBillingType().equalsIgnoreCase("TIMEOUT"))
        {
            initialValues.put("TRANG_THAI_TTOAN", Common.TRANG_THAI_TTOAN.CHUA_TTOAN.getCode());
            initialValues.put("VI_TTOAN", "");
            this.insertLichSuThanhToan(bodyBillResponse, "", "01", "", "", "01", 0, "", Common.getDateTimeNow(Common.DATE_TIME_TYPE.yyyyMMddHHmmssSSS), "09");
            if(bodyBillResponse.getBillingBy().equalsIgnoreCase(MainActivity.mEdong))
            {
                this.insertHoaDonThu(bodyBillResponse, "", "", "", "", "01", "01", "", 0, "", "");
            }

        }else if(bodyBillResponse.getBillingType().equalsIgnoreCase("TIMEOUT"))
        {
            initialValues.put("TRANG_THAI_TTOAN", Common.TRANG_THAI_TTOAN.DA_TTOAN.getCode());
            initialValues.put("VI_TTOAN", bodyBillResponse.getBillingBy());
            this.insertLichSuThanhToan(bodyBillResponse, "", "02", "02", "", "02", 0, "", Common.getDateTimeNow(Common.DATE_TIME_TYPE.yyyyMMddHHmmssSSS), "09");
            if(bodyBillResponse.getBillingBy().equalsIgnoreCase(MainActivity.mEdong))
            {
                this.insertHoaDonThu(bodyBillResponse, "", "", "", "", "", "02", "", 0, "", "");
            }
        }else
        {
            return  -1;
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

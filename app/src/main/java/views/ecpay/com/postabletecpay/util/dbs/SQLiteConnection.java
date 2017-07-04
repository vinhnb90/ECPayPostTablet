package views.ecpay.com.postabletecpay.util.dbs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import views.ecpay.com.postabletecpay.model.ReportModel;
import views.ecpay.com.postabletecpay.model.adapter.PayAdapter;
import views.ecpay.com.postabletecpay.model.adapter.PayBillsDialogAdapter;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.util.entities.EntityDanhSachThu;
import views.ecpay.com.postabletecpay.util.entities.EntityLichSuThanhToan;
import views.ecpay.com.postabletecpay.util.entities.response.EntityBill.BillResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityCustomer.CustomerResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityEVN.ListBookCmisResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityEVN.ListEvnPCResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityFileGen.BodyBillResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityFileGen.BodyCustomerResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityFileGen.FooterBillResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityFileGen.FooterCustomerResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityFileGen.ListBillResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityFileGen.ListCustomerResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntitySearchOnline.BillInsideCustomer;
import views.ecpay.com.postabletecpay.util.entities.response.EntitySearchOnline.CustomerInsideBody;
import views.ecpay.com.postabletecpay.util.entities.sqlite.Account;
import views.ecpay.com.postabletecpay.util.entities.sqlite.Bill;
import views.ecpay.com.postabletecpay.util.entities.sqlite.Customer;
import views.ecpay.com.postabletecpay.util.entities.sqlite.EvnPC;
import views.ecpay.com.postabletecpay.view.Main.MainActivity;

import static android.content.ContentValues.TAG;
import static views.ecpay.com.postabletecpay.util.commons.Common.DATE_TIME_TYPE.yyyyMMddHHmmssSSS;
import static views.ecpay.com.postabletecpay.util.commons.Common.DATE_TIME_TYPE.yyyyMMdd;
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

    private String CREATE_TABLE_CUSTOMER = "CREATE TABLE `" + TABLE_NAME_CUSTOMER + "` ( `code` TEXT NOT NULL PRIMARY KEY, `name` TEXT, `cardNo` TEXT, " +
            "`address` TEXT, `pcCode` TEXT, `pcCodeExt` TEXT, `phoneByevn` TEXT, `phoneByecp` TEXT, `bookCmis` TEXT, " +
            "`electricityMeter` TEXT, `inning` TEXT, `status` TEXT, `bankAccount` TEXT, `idNumber` TEXT, `bankName` TEXT , " +
            "`edongKey` TEXT NOT NULL, `isShowBill` INTEGER DEFAULT 0, " +
            "idChanged TEXT, dateChanged TEXT)";

    //add new field requestDate: date bill paying online success from tablet
    private String CREATE_TABLE_BILL = "CREATE TABLE `" + TABLE_NAME_BILL + "` ( `customerCode` TEXT, `customerPayCode` TEXT, " +
            "`billId` INTEGER NOT NULL PRIMARY KEY, `term` TEXT, `strTerm` TEXT, `amount` INTEGER, `period` TEXT, `issueDate` TEXT, " +
            "`strIssueDate` TEXT, `status` INTEGER, `seri` TEXT, `pcCode` TEXT, `handoverCode` TEXT, `cashierCode` TEXT, `bookCmis` TEXT, " +
            "`fromDate` TEXT, `toDate` TEXT, `strFromDate` TEXT, `strToDate` TEXT, `home` TEXT, `tax` REAL, `billNum` TEXT, `currency` TEXT, " +
            "`priceDetails` TEXT, `numeDetails` TEXT, `amountDetails` TEXT, `oldIndex` TEXT, `newIndex` TEXT, `nume` TEXT, " +
            "`amountNotTax` INTEGER, `amountTax` INTEGER, `multiple` TEXT, `billType` TEXT, `typeIndex` TEXT, `groupTypeIndex` TEXT, " +
            "`createdDate` TEXT, `idChanged` INTEGER, `dateChanged` TEXT, `edong` TEXT, `pcCodeExt` TEXT, `code` TEXT, `name` TEXT, " +
            "`nameNosign` TEXT, `phoneByevn` TEXT, `phoneByecp` TEXT, `electricityMeter` TEXT, `inning` TEXT, `road` TEXT, `station` TEXT, " +
            "`taxCode` TEXT, `trade` TEXT, `countPeriod` TEXT, `team` TEXT, `type` INTEGER, `lastQuery` TEXT, `groupType` INTEGER, " +
            "`billingChannel` TEXT, `billingType` TEXT, `billingBy` TEXT, `cashierPay` TEXT, `requestDate` TEXT,`edongKey` TEXT NOT NULL, " +
            "`isChecked` INTEGER default 0," +
            "`traceNumber` INTERGER, " +
            "`causeCancelBillOnline` TEXT)";

    private String CREATE_TABLE_DEBT_COLLECTION = "CREATE TABLE `" + TABLE_NAME_DEBT_COLLECTION + "` ( `customerCode` TEXT, `customerPayCode` TEXT, " +
            "`billId` INTEGER NOT NULL PRIMARY KEY, `term` TEXT, `strTerm` TEXT, `amount` INTEGER, `period` TEXT, `issueDate` TEXT, " +
            "`strIssueDate` TEXT, `status` INTEGER, `seri` TEXT, `pcCode` TEXT, `handoverCode` TEXT, `cashierCode` TEXT, `bookCmis` TEXT, " +
            "`fromDate` TEXT, `toDate` TEXT, `strFromDate` TEXT, `strToDate` TEXT, `home` TEXT, `tax` REAL, `billNum` TEXT, `currency` TEXT, " +
            "`priceDetails` TEXT, `numeDetails` TEXT, `amountDetails` TEXT, `oldIndex` TEXT, `newIndex` TEXT, `nume` TEXT, " +
            "`amountNotTax` INTEGER, `amountTax` INTEGER, `multiple` TEXT, `billType` TEXT, `typeIndex` TEXT, `groupTypeIndex` TEXT, " +
            "`createdDate` TEXT, `idChanged` INTEGER, `dateChanged` TEXT, `edong` TEXT, `pcCodeExt` TEXT, `code` TEXT, `name` TEXT, " +
            "`nameNosign` TEXT, `phoneByevn` TEXT, `phoneByecp` TEXT, `electricityMeter` TEXT, `inning` TEXT, `road` TEXT, `station` TEXT, " +
            "`taxCode` TEXT, `trade` TEXT, `countPeriod` TEXT, `team` TEXT, `type` INTEGER, `lastQuery` TEXT, `groupType` INTEGER, " +
            "`billingChannel` TEXT, `billingType` TEXT, `billingBy` TEXT, `cashierPay` TEXT, `requestDate` TEXT,`edongKey` TEXT NOT NULL, " +
            "stateOfDebt INTEGER, stateOfCancel TEXT, stateOfReturn TEXT, suspectedProcessingStatus TEXT, stateOfPush INTEGER, dateOfPush TEXT, " +
            "`isChecked` INTEGER default 0, `traceNumber` INTERGER, `causeCancelBillOnline` TEXT, payments INTEGER, payStatus INTEGER, " +
            "countPrintReceipt INTEGER, printInfo TEXT)";

    private String CREATE_TABLE_HISTORY_PAY = "CREATE TABLE `" + TABLE_NAME_HISTORY_PAY + "` ( `customerCode` TEXT, `customerPayCode` TEXT, " +
            "`billId` INTEGER NOT NULL PRIMARY KEY, `term` TEXT, `strTerm` TEXT, `amount` INTEGER, `period` TEXT, `issueDate` TEXT, " +
            "`strIssueDate` TEXT, `status` INTEGER, `seri` TEXT, `pcCode` TEXT, `handoverCode` TEXT, `cashierCode` TEXT, `bookCmis` TEXT, " +
            "`fromDate` TEXT, `toDate` TEXT, `strFromDate` TEXT, `strToDate` TEXT, `home` TEXT, `tax` REAL, `billNum` TEXT, `currency` TEXT, " +
            "`priceDetails` TEXT, `numeDetails` TEXT, `amountDetails` TEXT, `oldIndex` TEXT, `newIndex` TEXT, `nume` TEXT, " +
            "`amountNotTax` INTEGER, `amountTax` INTEGER, `multiple` TEXT, `billType` TEXT, `typeIndex` TEXT, `groupTypeIndex` TEXT, " +
            "`createdDate` TEXT, `idChanged` INTEGER, `dateChanged` TEXT, `edong` TEXT, `pcCodeExt` TEXT, `code` TEXT, `name` TEXT, " +
            "`nameNosign` TEXT, `phoneByevn` TEXT, `phoneByecp` TEXT, `electricityMeter` TEXT, `inning` TEXT, `road` TEXT, `station` TEXT, " +
            "`taxCode` TEXT, `trade` TEXT, `countPeriod` TEXT, `team` TEXT, `type` INTEGER, `lastQuery` TEXT, `groupType` INTEGER, " +
            "`billingChannel` TEXT, `billingType` TEXT, `billingBy` TEXT, `cashierPay` TEXT, `requestDate` TEXT,`edongKey` TEXT NOT NULL, " +
            "`isChecked` INTEGER default 0, `traceNumber` INTERGER, `causeCancelBillOnline` TEXT, payments INTEGER, payStatus INTEGER, " +
            "stateOfDebt INTEGER, stateOfCancel TEXT, stateOfReturn TEXT, suspectedProcessingStatus TEXT, stateOfPush INTEGER, dateOfPush TEXT, " +
            "countPrintReceipt INTEGER, printInfo TEXT, dateIncurred TEXT, tradingCode INTEGER)";

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
        String query = "SELECT coalesce(SUM(amount), 0) AS SUM, COUNT(*) AS COUNT FROM " + TABLE_NAME_BILL + " WHERE edongKey = '" + edong + "'";
        Cursor mCursor = database.rawQuery(query, null);

        if(mCursor.getCount() != ZERO && mCursor.moveToFirst())
        {
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
        String query = "SELECT coalesce(SUM(amount), 0) AS SUM, COUNT(*) AS COUNT FROM " + TABLE_NAME_BILL + " WHERE edongKey = '" + edong + "' and status != 0";
        Cursor mCursor = database.rawQuery(query, null);

        if(mCursor.getCount() != ZERO && mCursor.moveToFirst())
        {
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
        String query = "SELECT coalesce(SUM(amount), 0) AS SUM, COUNT(*) AS COUNT FROM " + TABLE_NAME_BILL + " WHERE edongKey = '" + edong + "' and status != 0 and billingType IN ('EDONG_OTHER', 'SOURCE_OTHER', 'TIMEOUT', 'REVERT')";
        Cursor mCursor = database.rawQuery(query, null);

        if(mCursor.getCount() != ZERO && mCursor.moveToFirst())
        {
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
        String query = "SELECT coalesce(SUM(amount), 0) AS SUM, COUNT(*) AS COUNT FROM " + TABLE_NAME_BILL + " bill WHERE bill.edongKey = '" + edong +
                "' and bill.status != 0 and  NOT EXISTS (SELECT * FROM " + TABLE_NAME_CUSTOMER + " customer WHERE bill.customerCode = customer.code)";
//                "                  FROM employees" +
//                "                  WHERE departments.department_id = employees.department_id)";
        Cursor mCursor = database.rawQuery(query, null);

        if(mCursor.getCount() != ZERO && mCursor.moveToFirst())
        {
            bill.setAmount(Long.parseLong(mCursor.getString(mCursor.getColumnIndex("SUM"))));
            bill.setCount(Integer.parseInt(mCursor.getString(mCursor.getColumnIndex("COUNT"))));
        }

        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }

        return bill;

    }


    private String getCustomerCodeByCardNo(String cardNo) {
        String query = "SELECT code FROM " + TABLE_NAME_CUSTOMER + " WHERE cardNo like '%" + cardNo + "%'";
        Cursor mCursor = database.rawQuery(query, null);


        String code = null;

        int count = mCursor.getCount();
        if (count != ZERO && mCursor.moveToFirst()) {
            mCursor.moveToFirst();
            code = mCursor.getString(mCursor.getColumnIndex("code"));
        }
        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }
        return code;
    }

    public List<Bill> getBillThuByCodeAndDate(String edong, boolean isMaKH, String customerCode, Calendar dateFrom, Calendar dateTo)
    {
        List<Bill> lst = new ArrayList<>();


        String query;

        if(isMaKH)
        {
            query = "SELECT * FROM " + TABLE_NAME_BILL + " WHERE edongKey = '" + edong +
                    "' and status != 0 and customerCode like '%" + customerCode + "%'";
        }else
        {
            query = "SELECT * FROM " + TABLE_NAME_BILL + " WHERE edongKey = '" + edong +
                    "' and status != 0 and name like '%" + customerCode + "%'";
        }

        Cursor mCursor = database.rawQuery(query, null);

        if (mCursor != null && mCursor.moveToFirst()) {
            int count = mCursor.getCount();
            do {
                String _requestDate = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("requestDate")));
                if(_requestDate.length() == 0)
                {
                    continue;
                }

                String[] arr = _requestDate.split("/");
                if (arr.length != 3)
                {
                    continue;
                }
                Calendar calendar = Calendar.getInstance();
                calendar.set(intConvertNull(Integer.parseInt(arr[2])), intConvertNull(Integer.parseInt(arr[1])) - 1, intConvertNull(Integer.parseInt(arr[0])));

                if((dateFrom != null && calendar.before(dateFrom)) || (dateTo != null && calendar.after(dateTo)))
                {
                    continue;
                }



                String _customerCode = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("customerCode")));
                String _customerPayCode = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("customerPayCode")));
                String _billId = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("billId")));
                String _term = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("term")));
                int _amount = intConvertNull(mCursor.getInt(mCursor.getColumnIndex("amount")));
                String _period = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("period")));
                String _issueDate = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("issueDate")));
                String _strIssueDate = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("strIssueDate")));
                int _status = intConvertNull(mCursor.getInt(mCursor.getColumnIndex("status")));
                String _seri = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("seri")));
                String _pcCode = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("pcCode")));
                String _handoverCode = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("handoverCode")));
                String _cashierCode = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("cashierCode")));
                String _bookCmis = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("bookCmis")));
                String _fromDate = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("fromDate")));
                String _toDate = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("toDate")));
                String _strFromDate = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("strFromDate")));
                String _strToDate = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("strToDate")));
                String _home = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("home")));
                String _tax = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("tax")));
                String _billNum = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("billNum")));
                String _currency = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("currency")));
                String _priceDetails = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("priceDetails")));
                String _numeDetails = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("numeDetails")));
                String _amountDetails = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("amountDetails")));
                String _oldIndex = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("oldIndex")));
                String _newIndex = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("newIndex")));
                String _nume = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("nume")));
                int _amountNotTax = intConvertNull(mCursor.getInt(mCursor.getColumnIndex("amountNotTax")));
                int _amountTax = intConvertNull(mCursor.getInt(mCursor.getColumnIndex("amountTax")));
                String _multiple = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("multiple")));
                String _billType = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("billType")));
                String _typeIndex = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("typeIndex")));
                String _groupTypeIndex = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("groupTypeIndex")));
                String _createdDate = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("createdDate")));
                int _idChanged = intConvertNull(mCursor.getInt(mCursor.getColumnIndex("idChanged")));
                String _dateChanged = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("dateChanged")));
                String _edong = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("edong")));
                String _pcCodeExt = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("pcCodeExt")));
                String _code = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("code")));
                String _name = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("name")));
                String _nameNosign = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("nameNosign")));
                String _phoneByevn = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("phoneByevn")));
                String _phoneByecp = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("phoneByecp")));
                String _electricityMeter = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("electricityMeter")));
                String _inning = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("inning")));
                String _road = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("road")));
                String _station = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("station")));
                String _taxCode = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("taxCode")));
                String _trade = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("trade")));
                String _countPeriod = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("countPeriod")));
                String _team = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("team")));
                int _type = intConvertNull(mCursor.getInt(mCursor.getColumnIndex("type")));
                String _lastQuery = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("lastQuery")));
                int _groupType = intConvertNull(mCursor.getInt(mCursor.getColumnIndex("groupType")));
                String _billingChannel = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("billingChannel")));
                String _billingType = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("billingType")));
                String _billingBy = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("billingBy")));
                String _cashierPay = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("cashierPay")));
                String _edongKey = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("edongKey")));
                int _isChecked = intConvertNull(mCursor.getInt(mCursor.getColumnIndex("isChecked")));
                long _traceNumber = longConvertNull(mCursor.getLong(mCursor.getColumnIndex("customerPayCode")));

                Bill bill = new Bill(_customerCode, _customerPayCode, _billId, _term, _amount, _period, _issueDate, _strIssueDate, _status, _seri, _pcCode, _handoverCode, _cashierCode, _bookCmis, _fromDate, _toDate, _strFromDate, _strToDate, _home, _tax, _billNum, _currency, _priceDetails, _numeDetails, _amountDetails, _oldIndex, _newIndex, _nume, _amountNotTax, _amountTax, _multiple, _billType, _typeIndex, _groupTypeIndex, _createdDate, _idChanged, _dateChanged, _edong, _pcCodeExt, _code, _name, _nameNosign, _phoneByevn, _phoneByecp, _electricityMeter, _inning, _road, _station, _taxCode, _trade, _countPeriod, _team, _type, _lastQuery, _groupType, _billingChannel, _billingType, _billingBy, _cashierPay, _requestDate, _edongKey, _isChecked, _traceNumber);
                bill.setRequestDateCal(calendar);
                lst.add(bill);

            }
            while (mCursor.moveToNext());

            mCursor.close();
        }


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


        String query;

        if(isMaKH)
        {
            query = "SELECT * FROM " + TABLE_NAME_BILL + " WHERE edongKey = '" + edong +
                    "' and status != 0 and customerCode like '%" + customerCode + "%' and billingType IN ('EDONG_OTHER', 'SOURCE_OTHER', 'TIMEOUT', 'REVERT')";
        }else
        {
            query = "SELECT * FROM " + TABLE_NAME_BILL + " WHERE edongKey = '" + edong +
                    "' and status != 0 and name like '%" + customerCode + "%' and billingType IN ('EDONG_OTHER', 'SOURCE_OTHER', 'TIMEOUT', 'REVERT')";
        }

        Cursor mCursor = database.rawQuery(query, null);

        if (mCursor != null && mCursor.moveToFirst()) {
            int count = mCursor.getCount();
            do {
                String _requestDate = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("requestDate")));
                if(_requestDate.length() == 0)
                {
                    continue;
                }

                String[] arr = _requestDate.split("/");
                if (arr.length != 3)
                {
                    continue;
                }
                Calendar calendar = Calendar.getInstance();
                calendar.set(intConvertNull(Integer.parseInt(arr[2])), intConvertNull(Integer.parseInt(arr[1])) - 1, intConvertNull(Integer.parseInt(arr[0])));

                if((dateFrom != null && calendar.before(dateFrom)) || (dateTo != null && calendar.after(dateTo)))
                {
                    continue;
                }



                String _customerCode = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("customerCode")));
                String _customerPayCode = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("customerPayCode")));
                String _billId = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("billId")));
                String _term = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("term")));
                int _amount = intConvertNull(mCursor.getInt(mCursor.getColumnIndex("amount")));
                String _period = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("period")));
                String _issueDate = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("issueDate")));
                String _strIssueDate = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("strIssueDate")));
                int _status = intConvertNull(mCursor.getInt(mCursor.getColumnIndex("status")));
                String _seri = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("seri")));
                String _pcCode = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("pcCode")));
                String _handoverCode = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("handoverCode")));
                String _cashierCode = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("cashierCode")));
                String _bookCmis = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("bookCmis")));
                String _fromDate = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("fromDate")));
                String _toDate = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("toDate")));
                String _strFromDate = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("strFromDate")));
                String _strToDate = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("strToDate")));
                String _home = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("home")));
                String _tax = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("tax")));
                String _billNum = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("billNum")));
                String _currency = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("currency")));
                String _priceDetails = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("priceDetails")));
                String _numeDetails = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("numeDetails")));
                String _amountDetails = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("amountDetails")));
                String _oldIndex = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("oldIndex")));
                String _newIndex = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("newIndex")));
                String _nume = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("nume")));
                int _amountNotTax = intConvertNull(mCursor.getInt(mCursor.getColumnIndex("amountNotTax")));
                int _amountTax = intConvertNull(mCursor.getInt(mCursor.getColumnIndex("amountTax")));
                String _multiple = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("multiple")));
                String _billType = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("billType")));
                String _typeIndex = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("typeIndex")));
                String _groupTypeIndex = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("groupTypeIndex")));
                String _createdDate = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("createdDate")));
                int _idChanged = intConvertNull(mCursor.getInt(mCursor.getColumnIndex("idChanged")));
                String _dateChanged = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("dateChanged")));
                String _edong = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("edong")));
                String _pcCodeExt = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("pcCodeExt")));
                String _code = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("code")));
                String _name = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("name")));
                String _nameNosign = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("nameNosign")));
                String _phoneByevn = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("phoneByevn")));
                String _phoneByecp = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("phoneByecp")));
                String _electricityMeter = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("electricityMeter")));
                String _inning = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("inning")));
                String _road = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("road")));
                String _station = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("station")));
                String _taxCode = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("taxCode")));
                String _trade = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("trade")));
                String _countPeriod = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("countPeriod")));
                String _team = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("team")));
                int _type = intConvertNull(mCursor.getInt(mCursor.getColumnIndex("type")));
                String _lastQuery = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("lastQuery")));
                int _groupType = intConvertNull(mCursor.getInt(mCursor.getColumnIndex("groupType")));
                String _billingChannel = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("billingChannel")));
                String _billingType = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("billingType")));
                String _billingBy = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("billingBy")));
                String _cashierPay = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("cashierPay")));
                String _edongKey = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("edongKey")));
                int _isChecked = intConvertNull(mCursor.getInt(mCursor.getColumnIndex("isChecked")));
                long _traceNumber = longConvertNull(mCursor.getLong(mCursor.getColumnIndex("customerPayCode")));

                Bill bill = new Bill(_customerCode, _customerPayCode, _billId, _term, _amount, _period, _issueDate, _strIssueDate, _status, _seri, _pcCode, _handoverCode, _cashierCode, _bookCmis, _fromDate, _toDate, _strFromDate, _strToDate, _home, _tax, _billNum, _currency, _priceDetails, _numeDetails, _amountDetails, _oldIndex, _newIndex, _nume, _amountNotTax, _amountTax, _multiple, _billType, _typeIndex, _groupTypeIndex, _createdDate, _idChanged, _dateChanged, _edong, _pcCodeExt, _code, _name, _nameNosign, _phoneByevn, _phoneByecp, _electricityMeter, _inning, _road, _station, _taxCode, _trade, _countPeriod, _team, _type, _lastQuery, _groupType, _billingChannel, _billingType, _billingBy, _cashierPay, _requestDate, _edongKey, _isChecked, _traceNumber);
                bill.setRequestDateCal(calendar);
                lst.add(bill);

            }
            while (mCursor.moveToNext());

            mCursor.close();
        }


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

    public long countMoneyAllBillOfCustomer(String edong, String customerCode) {
        if (edong == null)
            return 0;

        database = this.getReadableDatabase();
        long totalMoney = 0;
        String query = "SELECT SUM(amount) AS totalMoney FROM " + TABLE_NAME_BILL + " WHERE edongKey = '" + edong + "' and customerCode ='" + customerCode + "'";
        Cursor mCursor = database.rawQuery(query, null);
        int count = mCursor.getCount();

        if (count != ZERO) {
            mCursor.moveToFirst();
            totalMoney = longConvertNull(mCursor.getLong(mCursor.getColumnIndex("totalMoney")));
        }
        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }
        return totalMoney;
    }

    public String selectRoadFirstInBill(String edong, String code) {
        String query = "SELECT road FROM " + TABLE_NAME_BILL + " WHERE edongKey = '" + edong + "' and customerCode ='" + code + "' and road is not null and road <> ''";
        String road = "";
        database = this.getReadableDatabase();
        Cursor mCursor = database.rawQuery(query, null);
        mCursor.moveToFirst();

        int count = mCursor.getCount();
        //get first value
        if (count != Common.ZERO)
            road = mCursor.getString(mCursor.getColumnIndex("road"));

        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }
        return road;
    }

    public Cursor selectOfflineBill() {
        database = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_BILL + " WHERE status = 2";
        return database.rawQuery(query, null);
    }

    public List<Customer> selectAllCustomer(String edong) {
        return this.selectAllCustomerFitterBy(edong, Common.TYPE_SEARCH.ALL, Common.TEXT_EMPTY);
    }

    public boolean checkStatusPayedOfCustormer(String edong, String code) {
        String query = "SELECT status FROM " + TABLE_NAME_BILL + " WHERE edongKey = '" + edong + "' and customerCode ='" + code + "' and status = 0";
        boolean isPayed = false;
        database = this.getReadableDatabase();
        Cursor mCursor = database.rawQuery(query, null);
        isPayed = (mCursor.getCount() == 0) ? true : false;

        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }
        return isPayed;
    }

    public long updatePayOffine(int billID, int status, String edong) {
        database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", status);
        contentValues.put("edong", edong);
        contentValues.put("edongKey", edong);
        return database.update(TABLE_NAME_BILL, contentValues, "billId = ?", new String[]{String.valueOf(billID)});
    }

    public List<Customer> selectAllCustomerFitterBy(String edong, Common.TYPE_SEARCH typeSearch, String infoSearch) {
        List<Customer> customerList = new ArrayList<>();

        boolean fail = TextUtils.isEmpty(edong) || TextUtils.isEmpty(infoSearch) && typeSearch.getPosition() != Common.TYPE_SEARCH.ALL.getPosition();
        if (fail)
            return customerList;

        String query = null;
        switch (typeSearch.getPosition()) {
            case 0:
                query = "SELECT * FROM " + TABLE_NAME_CUSTOMER + " WHERE edongKey = '" + edong + "'";
                break;
            case 1:
                query = "SELECT * FROM " + TABLE_NAME_CUSTOMER + " WHERE edongKey = '" + edong + "' and code like '%" + infoSearch + "%'";
                break;
            case 2:
                query = "SELECT * FROM " + TABLE_NAME_CUSTOMER + " WHERE edongKey = '" + edong + "' and name like '%" + infoSearch + "%'";
                break;
            case 3:
                query = "SELECT * FROM " + TABLE_NAME_CUSTOMER + " WHERE edongKey = '" + edong + "' and phoneByevn like '%" + infoSearch + "%'";
                break;
            case 4:
                query = "SELECT * FROM " + TABLE_NAME_CUSTOMER + " WHERE edongKey = '" + edong + "' and address like '%" + infoSearch + "%'";
                break;
            case 5:
                query = "SELECT * FROM " + TABLE_NAME_CUSTOMER + " WHERE edongKey = '" + edong + "' and bookCmis like '%" + infoSearch + "%'";
                break;
            case 6:
                query = "SELECT * FROM " + TABLE_NAME_CUSTOMER + " WHERE edongKey = '" + edong + "' and road like '%" + infoSearch + "%'";
                break;

          /*  case 7:
                query = "SELECT * FROM " + TABLE_NAME_CUSTOMER + " WHERE edongKey = '" + edong
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
        Cursor mCursor = database.rawQuery(query, null);

        if (mCursor != null && mCursor.moveToFirst()) {
            int count = mCursor.getCount();
            do {
                String code = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("code")));
                String name = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("name")));
                String address = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("address")));
                String cardNo = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("cardNo")));
                String pcCode = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("pcCode")));
                String pcCodeExt = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("pcCodeExt")));
                String phoneByevn = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("phoneByevn")));
                String phoneByecp = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("phoneByecp")));
                String bookCmis = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("bookCmis")));
                String electricityMeter = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("electricityMeter")));
                String inning = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("inning")));
                String status = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("status")));
                String bankAccount = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("bankAccount")));
                String idNumber = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("idNumber")));
                String bankName = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("bankName")));
                String edongKey = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("edongKey")));
                boolean isShowBill = booleanConvertNull(mCursor.getInt(mCursor.getColumnIndex("isShowBill")));

                String idChanged = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("idChanged")));
                String dateChanged = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("dateChanged")));
                Customer customer = new Customer(code, name, address, pcCode, pcCodeExt, phoneByevn, phoneByecp, bookCmis, electricityMeter, inning, status, bankAccount, idNumber, bankName, edongKey, idChanged, dateChanged, isShowBill);
                customer.setCardNo(cardNo);
                customerList.add(customer);
            }
            while (mCursor.moveToNext());

            mCursor.close();
        }
        return customerList;
    }


    public List<Customer> selectAllCustomerFitter(String _maKH, String _name, String _address, String _phone, String _bookCmis) {
        List<Customer> customerList = new ArrayList<>();


        String query = "SELECT * FROM " + TABLE_NAME_CUSTOMER + " WHERE ";
        if(_maKH.length() != 0)
        {
            query += "code like '%" + _maKH + "%' or ";
            query += "cardNo like '%" + _maKH + "%' ";
        }else
        {
            boolean hasWhere = false;

            if(_name.length() > 0)
            {
                query += (hasWhere ? "and "  : "") + "name like '%" + _name + "%' ";
                hasWhere = true;
            }

            if(_address.length() > 0)
            {
                query += (hasWhere ? "and "  : "") + "address like '%" + _address + "%' ";
                hasWhere = true;
            }

            if(_phone.length() > 0)
            {
                query += (hasWhere ? "and "  : "") + "phoneByevn like '%" + _phone + "%' ";
                hasWhere = true;
            }

            if(_bookCmis.length() > 0)
            {
                query += (hasWhere ? "and "  : "") + "bookCmis like '%" + _bookCmis + "%' ";
                hasWhere = true;
            }
        }

        database = this.getWritableDatabase();
        Cursor mCursor = database.rawQuery(query, null);

        if (mCursor != null && mCursor.moveToFirst()) {
            int count = mCursor.getCount();
            do {
                String code = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("code")));
                String name = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("name")));
                String address = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("address")));
                String cardNo = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("cardNo")));
                String pcCode = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("pcCode")));
                String pcCodeExt = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("pcCodeExt")));
                String phoneByevn = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("phoneByevn")));
                String phoneByecp = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("phoneByecp")));
                String bookCmis = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("bookCmis")));
                String electricityMeter = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("electricityMeter")));
                String inning = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("inning")));
                String status = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("status")));
                String bankAccount = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("bankAccount")));
                String idNumber = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("idNumber")));
                String bankName = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("bankName")));
                String edongKey = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("edongKey")));
                boolean isShowBill = booleanConvertNull(mCursor.getInt(mCursor.getColumnIndex("isShowBill")));

                String idChanged = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("idChanged")));
                String dateChanged = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("dateChanged")));
                Customer customer = new Customer(code, name, address, pcCode, pcCodeExt, phoneByevn, phoneByecp, bookCmis, electricityMeter, inning, status, bankAccount, idNumber, bankName, edongKey, idChanged, dateChanged, isShowBill);
                customer.setCardNo(cardNo);
                customerList.add(customer);
            }
            while (mCursor.moveToNext());

            mCursor.close();
        }
        return customerList;
    }

    public List<PayAdapter.BillEntityAdapter> selectInfoBillOfCustomerToRecycler(String edong, String code) {
        List<PayAdapter.BillEntityAdapter> billList = new ArrayList<>();

        database = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_BILL + " WHERE edongKey = '" + edong + "' and customerCode ='" + code + "' ORDER BY term DESC";
        Cursor mCursor = database.rawQuery(query, null);

        if (mCursor.getCount() == 0)
            return billList;

        mCursor.moveToFirst();
        do {
            int billId = intConvertNull(mCursor.getInt(mCursor.getColumnIndex("billId")));
            int amount = intConvertNull(mCursor.getInt(mCursor.getColumnIndex("amount")));
            int status = intConvertNull(mCursor.getInt(mCursor.getColumnIndex("status")));
            //TODO mark
            boolean isChecked = intConvertNull(mCursor.getInt(mCursor.getColumnIndex("isChecked"))) == 0 ? false : true;
            String customerPayCode = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("customerPayCode")));
            String billingBy = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("billingBy")));

            String strTerm = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("strTerm")));
            String term = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("term")));
            term = Common.convertDateToDate(term, yyyyMMddHHmmssSSS, Common.DATE_TIME_TYPE.MMyyyy);

            String termVisible = strTerm.equals(Common.TEXT_EMPTY) ? term : strTerm;

            String dateRequest = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("requestDate")));

            PayAdapter.BillEntityAdapter bill = new PayAdapter.BillEntityAdapter();
            bill.setBillId(billId);
            bill.setBillingBy(billingBy);
            bill.setCustomerPayCode(customerPayCode);
            bill.setMoneyBill(amount);
            bill.setMonthBill(termVisible);
            bill.setStatus(status);
            bill.setChecked(isChecked);
            if (bill.getStatus() == Common.STATUS_BILLING.CHUA_THANH_TOAN.getCode())
                bill.setPrint(false);
            else bill.setPrint(true);

            bill.setRequestDate(dateRequest);

            billList.add(bill);
        }
        while (mCursor.moveToNext());

        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }
        return billList;
    }

    public int countMoneyAllBill(String edong) {
        database = this.getReadableDatabase();
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

    public int insertNotUpdateCustomerFromSearchOnline(String edong, CustomerInsideBody customer) {
        if (edong == null || edong.trim().isEmpty() || customer == null)
            return ERROR_OCCUR;

        ContentValues initialValues = new ContentValues();

        initialValues.put("code", customer.getCode());
        initialValues.put("name", customer.getName());
        initialValues.put("cardNo", customer.getCardNo());
        initialValues.put("address", customer.getAddress());
        initialValues.put("pcCode", customer.getPcCode());
        initialValues.put("pcCodeExt", customer.getPcCodeExt());
        initialValues.put("phoneByevn", customer.getPhoneByevn());
        initialValues.put("phoneByecp", customer.getPhoneByecp());
        initialValues.put("bookCmis", customer.getBookCmis());
        initialValues.put("electricityMeter", customer.getElectricityMeter());
        initialValues.put("inning", customer.getInning());
        initialValues.put("status", customer.getStatus());
        initialValues.put("bankAccount", customer.getBankAccount());
        initialValues.put("idNumber", customer.getIdNumber());
        initialValues.put("bankName", customer.getBankName());
        initialValues.put("edongKey", edong);

        database = getWritableDatabase();
        int rowAffect = (int) database.insert(TABLE_NAME_CUSTOMER, null, initialValues);
        return rowAffect;
    }

    public int insertNotUpdateBillOfCustomer(String edong, BillInsideCustomer billInsideCustomer) {
        if (edong == null || edong.trim().isEmpty() || billInsideCustomer == null)
            return SQLiteConnection.ERROR_OCCUR;

        ContentValues initialValues = new ContentValues();
        initialValues.put("customerCode", billInsideCustomer.getCustomerCode());
        initialValues.put("customerPayCode", billInsideCustomer.getCustomerPayCode());
        initialValues.put("billId", billInsideCustomer.getBillId());

        String term = billInsideCustomer.getTerm();
        //20170414011107000 != 2015-01-01
        if (term.length() == yyyyMMdd.toString().length()) {
            term = Common.convertDateToDate(term, yyyyMMdd, yyyyMMddHHmmssSSS);
        }

//        String strTerm = Common.convertDateToDate(term, yyyyMMdd, yyyyMMddHHmmssSSS);
//                String strTerm = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("strTerm")));
//        String termVisible = strTerm.equals(Common.TEXT_EMPTY) ? term : strTerm;

        initialValues.put("term", term);
        initialValues.put("strTerm", billInsideCustomer.getStrTerm());
        initialValues.put("amount", billInsideCustomer.getAmount());
        initialValues.put("period", billInsideCustomer.getPeriod());
        initialValues.put("issueDate", billInsideCustomer.getIssueDate());
        initialValues.put("strIssueDate", billInsideCustomer.getStrIssueDate());
        initialValues.put("status", billInsideCustomer.getStatus());
        initialValues.put("seri", billInsideCustomer.getSeri());
        initialValues.put("pcCode", billInsideCustomer.getPcCode());
        initialValues.put("handoverCode", billInsideCustomer.getHandoverCode());
        initialValues.put("cashierCode", billInsideCustomer.getCashierCode());
        initialValues.put("bookCmis", billInsideCustomer.getBookCmis());
        initialValues.put("fromDate", billInsideCustomer.getFromDate());
        initialValues.put("toDate", billInsideCustomer.getToDate());
        initialValues.put("strFromDate", billInsideCustomer.getStrFromDate());
        initialValues.put("strToDate", billInsideCustomer.getStrToDate());
        initialValues.put("home", billInsideCustomer.getHome());
        initialValues.put("tax", billInsideCustomer.getTax());
        initialValues.put("billNum", billInsideCustomer.getBillNum());
        initialValues.put("currency", billInsideCustomer.getCurrency());
        initialValues.put("priceDetails", billInsideCustomer.getPriceDetails());
        initialValues.put("numeDetails", billInsideCustomer.getNumeDetails());
        initialValues.put("amountDetails", billInsideCustomer.getAmountDetails());
        initialValues.put("oldIndex", billInsideCustomer.getOldIndex());
        initialValues.put("newIndex", billInsideCustomer.getNewIndex());
        initialValues.put("nume", billInsideCustomer.getNume());
        initialValues.put("amountNotTax", billInsideCustomer.getAmountNotTax());
        initialValues.put("amountTax", billInsideCustomer.getAmountTax());
        initialValues.put("multiple", billInsideCustomer.getMultiple());
        initialValues.put("billType", billInsideCustomer.getBillType());
        initialValues.put("typeIndex", billInsideCustomer.getTypeIndex());
        initialValues.put("groupTypeIndex", billInsideCustomer.getGroupTypeIndex());
        initialValues.put("createdDate", billInsideCustomer.getCreatedDate());
        initialValues.put("idChanged", billInsideCustomer.getIdChanged());
        initialValues.put("dateChanged", billInsideCustomer.getDateChanged());
        initialValues.put("edong", billInsideCustomer.getEdong());
        initialValues.put("pcCodeExt", billInsideCustomer.getPcCodeExt());
        initialValues.put("code", billInsideCustomer.getCode());
        initialValues.put("name", billInsideCustomer.getName());
        initialValues.put("nameNosign", billInsideCustomer.getNameNosign());
        initialValues.put("phoneByevn", billInsideCustomer.getPhoneByevn());
        initialValues.put("phoneByecp", billInsideCustomer.getPhoneByecp());
        initialValues.put("electricityMeter", billInsideCustomer.getElectricityMeter());
        initialValues.put("inning", billInsideCustomer.getInning());
        initialValues.put("road", billInsideCustomer.getRoad());
        initialValues.put("station", billInsideCustomer.getStation());
        initialValues.put("taxCode", billInsideCustomer.getTaxCode());
        initialValues.put("trade", billInsideCustomer.getTrade());
        initialValues.put("countPeriod", billInsideCustomer.getCountPeriod());
        initialValues.put("team", billInsideCustomer.getTeam());
        initialValues.put("type", billInsideCustomer.getType());
        initialValues.put("lastQuery", billInsideCustomer.getLastQuery());
        initialValues.put("groupType", billInsideCustomer.getGroupType());
        initialValues.put("billingChannel", billInsideCustomer.getBillingChannel());
        initialValues.put("billingType", billInsideCustomer.getBillType());
        initialValues.put("billingBy", billInsideCustomer.getBillingBy());
        initialValues.put("cashierPay", billInsideCustomer.getCashierPay());
        initialValues.put("edongKey", edong);
        database = getWritableDatabase();
        int rowAffect = (int) database.insert(TABLE_NAME_BILL, null, initialValues);

        return rowAffect;
    }

    public void updateBillOfCustomerIsChecked(String edong, String code, int billId, boolean checked) {
        ContentValues initialValues = new ContentValues();
        initialValues.put("isChecked", checked == true ? ONE : ZERO);

        database = getWritableDatabase();
        int rowAffect = (int) database.update(TABLE_NAME_BILL, initialValues, "edongKey = ? and customerCode = ? and billId = ?", new String[]{edong, code, String.valueOf(billId)});
        Log.d(TAG, "updateBillOfCustomerIsChecked: rowAffect is " + rowAffect);
    }

    public int countMoneyAllBillIsChecked(String edong) {
        database = this.getReadableDatabase();
        String query = "SELECT SUM(amount) FROM " + TABLE_NAME_BILL + " WHERE edongKey = '" + edong + "' and isChecked = " + Common.ONE;
        Cursor mCursor = database.rawQuery(query, null);

        int totalMoney = ERROR_OCCUR;
        if (mCursor.moveToFirst())
            totalMoney = mCursor.getInt(0);

        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }
        return totalMoney;
    }

    public long countMoneyAllBillIsCheckedWithStatusPay(String edong, Common.STATUS_BILLING statusBilling) {
        if (edong == null || edong.trim().isEmpty())
            return ERROR_OCCUR;

        database = this.getReadableDatabase();
        String query = "SELECT SUM(amount) FROM " + TABLE_NAME_BILL + " WHERE edongKey = '" + edong + "' and isChecked = " + Common.ONE + " and status = " + statusBilling.getCode();
        Cursor mCursor = database.rawQuery(query, null);

        int totalMoney = ERROR_OCCUR;
        if (mCursor.moveToFirst())
            totalMoney = mCursor.getInt(0);

        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }
        return totalMoney;
    }


    public int countAllBillsIsChecked(String edong) {
        database = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + TABLE_NAME_BILL + " WHERE edongKey = '" + edong + "' and isChecked = " + Common.ONE;
        Cursor mCursor = database.rawQuery(query, null);

        int totalBills = ERROR_OCCUR;
        if (mCursor.moveToFirst())
            totalBills = mCursor.getInt(0);

        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }
        return totalBills;
    }

    public int countAllBillsIsCheckedWithStatusPay(String edong, Common.STATUS_BILLING statusBilling) {
        database = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + TABLE_NAME_BILL + " WHERE edongKey = '" + edong + "' and isChecked = " + Common.ONE + " and status = " + statusBilling.getCode();
        Cursor mCursor = database.rawQuery(query, null);

        int totalBills = ERROR_OCCUR;
        if (mCursor.moveToFirst())
            totalBills = mCursor.getInt(0);

        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }
        return totalBills;
    }


    private List<PayBillsDialogAdapter.Entity> selectAllBillsOfAllCustomerCheckedWithQuery(String edong, String query) {
        database = getReadableDatabase();
        Cursor mCursor = database.rawQuery(query, null);
        List<PayBillsDialogAdapter.Entity> listBillChecked = new ArrayList<>();
        PayBillsDialogAdapter.Entity entity = null;

        if (mCursor.moveToFirst()) {
            do {
                String term = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("term")));
                term = Common.convertDateToDate(term, yyyyMMddHHmmssSSS, Common.DATE_TIME_TYPE.MMyyyy);

                String strTerm = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("strTerm")));
                String termVisible = strTerm.equals(Common.TEXT_EMPTY) ? term : strTerm;

                entity = new PayBillsDialogAdapter.Entity(
                        stringConvertNull(mCursor.getString(mCursor.getColumnIndex("customerCode"))),
                        stringConvertNull(mCursor.getString(mCursor.getColumnIndex("name"))),
                        termVisible,
                        longConvertNull(mCursor.getLong(mCursor.getColumnIndex("amount"))),
                        booleanConvertNull(mCursor.getInt(mCursor.getColumnIndex("isChecked"))),
                        edong,
                        intConvertNull(mCursor.getInt(mCursor.getColumnIndex("billId"))),
                        intConvertNull(mCursor.getInt(mCursor.getColumnIndex("status")))
                );

                listBillChecked.add(entity);
            }
            while (mCursor.moveToNext());
        }

        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }

        return listBillChecked;
    }

    public List<PayBillsDialogAdapter.Entity> selectAllBillsOfAllCustomerChecked(String edong) {
        if (TextUtils.isEmpty(edong))
            return null;

        String query = "SELECT A.billId, A.status,  A.customerCode, A.term, A.strTerm, A.amount, A.isChecked, B.name  FROM (SELECT DISTINCT billId, status, customerCode, term, strTerm, amount, isChecked FROM " + TABLE_NAME_BILL + " WHERE edongKey='" + edong + "' and isChecked = " + ONE + " ORDER BY date(term) DESC ) AS A JOIN TBL_CUSTOMER B on A.customerCode = B.code";
        Log.e(TAG, "selectAllBillsOfAllCustomerChecked: " + query);
        return selectAllBillsOfAllCustomerCheckedWithQuery(edong, query);
    }

    public List<PayBillsDialogAdapter.Entity> selectAllBillsOfAllCustomerCheckedWithStatus(String edong, Common.STATUS_BILLING statusBilling) {
        if (TextUtils.isEmpty(edong))
            return null;

        String query = "SELECT A.billId, A.status,  A.customerCode, A.term, A.strTerm, A.amount, A.isChecked, B.name  FROM (SELECT DISTINCT billId, status, customerCode, term, strTerm, amount, isChecked FROM " + TABLE_NAME_BILL + " WHERE edongKey='" + edong + "' and isChecked = " + ONE + " and status = " + statusBilling.getCode() + " ORDER BY date(term) DESC ) AS A JOIN TBL_CUSTOMER B on A.customerCode = B.code";
        return selectAllBillsOfAllCustomerCheckedWithQuery(edong, query);
    }

    private boolean booleanConvertNull(int isChecked) {
        return isChecked == Common.ONE ? true : false;
    }

    public void updateCustomerIsShowBill(String edong, String code, boolean checked) {
        ContentValues initialValues = new ContentValues();
        initialValues.put("isShowBill", checked == true ? ONE : ZERO);

        database = getWritableDatabase();
        int rowAffect = (int) database.update(TABLE_NAME_CUSTOMER, initialValues, "edongKey = ? and code = ?", new String[]{edong, code});
        Log.d(TAG, "updateBillOfCustomerIsChecked: rowAffect is " + rowAffect);
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

    public int updateBillStatusPay(String edong, String customerCode, Long billId, Common.STATUS_BILLING statusBilling) {
        boolean failInput =
                TextUtils.isEmpty(edong) ||
                        TextUtils.isEmpty(customerCode);
        if (failInput)
            return ERROR_OCCUR;
        database = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", statusBilling.getCode());
        return database.update(TABLE_NAME_BILL, contentValues, "edongKey = ? and customerCode = ? and billId = ?", new String[]{edong, customerCode, String.valueOf(billId)});
    }

    public int countMoneyAllBillIsCheckedWithStatusPay(String edong, String customerCode, Long billId, String dateNow, Long traceNumber) {
        boolean failInput =
                TextUtils.isEmpty(edong) ||
                        TextUtils.isEmpty(customerCode) ||
                        TextUtils.isEmpty(dateNow);
        if (failInput)
            return ERROR_OCCUR;

        database = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("requestDate", dateNow);
        contentValues.put("traceNumber", traceNumber);
        return database.update(TABLE_NAME_BILL, contentValues, "edongKey = ? and customerCode = ? and billId = ?", new String[]{edong, customerCode, String.valueOf(billId)});
    }

    public Long selectTraceNumberBill(String edong, String code, Long billId) {
        database = this.getReadableDatabase();
        String query = "SELECT traceNumber FROM " + TABLE_NAME_BILL + " WHERE edongKey = '" + edong + "' and billId = " + billId;
        Cursor mCursor = database.rawQuery(query, null);
        if (mCursor.moveToFirst())
            return longConvertNull(mCursor.getLong(mCursor.getColumnIndex("traceNumber")));

        return 0l;
    }

    public void updateBillReasonDelete(String edong, String code, Long billId, String reasonDeleteBill, Common.STATUS_BILLING statusBilling) {
        database = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("causeCancelBillOnline", reasonDeleteBill);
        contentValues.put("status", statusBilling.getCode());

        database.update(TABLE_NAME_BILL, contentValues, "edongKey = ? and customerCode = ? and billId = ?", new String[]{edong, code, String.valueOf(billId)});
    }

    public EntityDanhSachThu selectDebtColectionForDanhSachThu(int billId) {
        database = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_BILL + " WHERE billId = " + billId;
        Cursor c = database.rawQuery(query, null);
        EntityDanhSachThu entityDanhSachThu = new EntityDanhSachThu();
        if (c.moveToFirst()) {
            entityDanhSachThu.setEdong(c.getString(c.getColumnIndex("edong")));
            entityDanhSachThu.setCustomerCode(c.getString(c.getColumnIndex("customerCode")));
            entityDanhSachThu.setCustomerPayCode(c.getString(c.getColumnIndex("customerPayCode")));
            entityDanhSachThu.setBillId(c.getInt(c.getColumnIndex("billId")));
            entityDanhSachThu.setTerm(c.getString(c.getColumnIndex("term")));
            entityDanhSachThu.setAmount(c.getInt(c.getColumnIndex("amount")));
            entityDanhSachThu.setPeriod(c.getString(c.getColumnIndex("period")));
            entityDanhSachThu.setIssueDate(c.getString(c.getColumnIndex("issueDate")));
            entityDanhSachThu.setStrIssueDate(c.getString(c.getColumnIndex("strIssueDate")));
            entityDanhSachThu.setStatus(c.getInt(c.getColumnIndex("status")));
            entityDanhSachThu.setSeri(c.getString(c.getColumnIndex("seri")));
            entityDanhSachThu.setPcCode(c.getString(c.getColumnIndex("pcCode")));
            entityDanhSachThu.setHandoverCode(c.getString(c.getColumnIndex("handoverCode")));
            entityDanhSachThu.setCashierCode(c.getString(c.getColumnIndex("cashierCode")));
            entityDanhSachThu.setBookCmis(c.getString(c.getColumnIndex("bookCmis")));
            entityDanhSachThu.setFromDate(c.getString(c.getColumnIndex("fromDate")));
            entityDanhSachThu.setToDate(c.getString(c.getColumnIndex("toDate")));
            entityDanhSachThu.setStrFromDate(c.getString(c.getColumnIndex("strFromDate")));
            entityDanhSachThu.setStrToDate(c.getString(c.getColumnIndex("strToDate")));
            entityDanhSachThu.setHome(c.getString(c.getColumnIndex("home")));
            entityDanhSachThu.setTax(c.getFloat(c.getColumnIndex("tax")));
            entityDanhSachThu.setBillNum(c.getString(c.getColumnIndex("billNum")));
            entityDanhSachThu.setCurrency(c.getString(c.getColumnIndex("currency")));
            entityDanhSachThu.setPriceDetails(c.getString(c.getColumnIndex("priceDetails")));
            entityDanhSachThu.setNumeDetails(c.getString(c.getColumnIndex("numeDetails")));
            entityDanhSachThu.setAmountDetails(c.getString(c.getColumnIndex("amountDetails")));
            entityDanhSachThu.setOldIndex(c.getString(c.getColumnIndex("oldIndex")));
            entityDanhSachThu.setNewIndex(c.getString(c.getColumnIndex("newIndex")));
            entityDanhSachThu.setNume(c.getString(c.getColumnIndex("nume")));
            entityDanhSachThu.setAmountNotTax(c.getInt(c.getColumnIndex("amountNotTax")));
            entityDanhSachThu.setAmountTax(c.getString(c.getColumnIndex("amountTax")));
            entityDanhSachThu.setMultiple(c.getString(c.getColumnIndex("multiple")));
            entityDanhSachThu.setBillType(c.getString(c.getColumnIndex("billType")));
            entityDanhSachThu.setTypeIndex(c.getString(c.getColumnIndex("typeIndex")));
            entityDanhSachThu.setGroupTypeIndex(c.getString(c.getColumnIndex("groupTypeIndex")));
            entityDanhSachThu.setCreatedDate(c.getString(c.getColumnIndex("createdDate")));
            entityDanhSachThu.setIdChanged(c.getInt(c.getColumnIndex("idChanged")));
            entityDanhSachThu.setDateChanged(c.getString(c.getColumnIndex("dateChanged")));
            entityDanhSachThu.setPcCodeExt(c.getString(c.getColumnIndex("pcCodeExt")));
            entityDanhSachThu.setCode(c.getString(c.getColumnIndex("code")));
            entityDanhSachThu.setName(c.getString(c.getColumnIndex("name")));
            entityDanhSachThu.setNameNosign(c.getString(c.getColumnIndex("nameNosign")));
            entityDanhSachThu.setPhoneByevn(c.getString(c.getColumnIndex("phoneByevn")));
            entityDanhSachThu.setPhoneByecp(c.getString(c.getColumnIndex("phoneByecp")));
            entityDanhSachThu.setElectricityMeter(c.getString(c.getColumnIndex("electricityMeter")));
            entityDanhSachThu.setInning(c.getString(c.getColumnIndex("inning")));
            entityDanhSachThu.setRoad(c.getString(c.getColumnIndex("road")));
            entityDanhSachThu.setStation(c.getString(c.getColumnIndex("station")));
            entityDanhSachThu.setTaxCode(c.getString(c.getColumnIndex("taxCode")));
            entityDanhSachThu.setTrade(c.getString(c.getColumnIndex("trade")));
            entityDanhSachThu.setCountPeriod(c.getString(c.getColumnIndex("countPeriod")));
            entityDanhSachThu.setTeam(c.getString(c.getColumnIndex("team")));
            entityDanhSachThu.setType(c.getInt(c.getColumnIndex("type")));
            entityDanhSachThu.setLastQuery(c.getString(c.getColumnIndex("lastQuery")));
            entityDanhSachThu.setGroupType(c.getInt(c.getColumnIndex("groupType")));
            entityDanhSachThu.setBillingChannel(c.getString(c.getColumnIndex("billingChannel")));
            entityDanhSachThu.setBillType(c.getString(c.getColumnIndex("billingType")));
            entityDanhSachThu.setBillingBy(c.getString(c.getColumnIndex("billingBy")));
            entityDanhSachThu.setCashierPay(c.getString(c.getColumnIndex("cashierPay")));
        }
        return entityDanhSachThu;
    }

    public EntityLichSuThanhToan selectDebtColectionForLichSu(int billId) {
        database = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_BILL + " WHERE billId = " + billId;
        Cursor c = database.rawQuery(query, null);
        EntityLichSuThanhToan entityLichSuThanhToan = new EntityLichSuThanhToan();
        if (c.moveToFirst()) {
            entityLichSuThanhToan.setEdong(c.getString(c.getColumnIndex("edong")));
            entityLichSuThanhToan.setCustomerCode(c.getString(c.getColumnIndex("customerCode")));
            entityLichSuThanhToan.setCustomerPayCode(c.getString(c.getColumnIndex("customerPayCode")));
            entityLichSuThanhToan.setBillId(c.getInt(c.getColumnIndex("billId")));
            entityLichSuThanhToan.setTerm(c.getString(c.getColumnIndex("term")));
            entityLichSuThanhToan.setAmount(c.getInt(c.getColumnIndex("amount")));
            entityLichSuThanhToan.setPeriod(c.getString(c.getColumnIndex("period")));
            entityLichSuThanhToan.setIssueDate(c.getString(c.getColumnIndex("issueDate")));
            entityLichSuThanhToan.setStrIssueDate(c.getString(c.getColumnIndex("strIssueDate")));
            entityLichSuThanhToan.setStatus(c.getInt(c.getColumnIndex("status")));
            entityLichSuThanhToan.setSeri(c.getString(c.getColumnIndex("seri")));
            entityLichSuThanhToan.setPcCode(c.getString(c.getColumnIndex("pcCode")));
            entityLichSuThanhToan.setHandoverCode(c.getString(c.getColumnIndex("handoverCode")));
            entityLichSuThanhToan.setCashierCode(c.getString(c.getColumnIndex("cashierCode")));
            entityLichSuThanhToan.setBookCmis(c.getString(c.getColumnIndex("bookCmis")));
            entityLichSuThanhToan.setFromDate(c.getString(c.getColumnIndex("fromDate")));
            entityLichSuThanhToan.setToDate(c.getString(c.getColumnIndex("toDate")));
            entityLichSuThanhToan.setStrFromDate(c.getString(c.getColumnIndex("strFromDate")));
            entityLichSuThanhToan.setStrToDate(c.getString(c.getColumnIndex("strToDate")));
            entityLichSuThanhToan.setHome(c.getString(c.getColumnIndex("home")));
            entityLichSuThanhToan.setTax(c.getFloat(c.getColumnIndex("tax")));
            entityLichSuThanhToan.setBillNum(c.getString(c.getColumnIndex("billNum")));
            entityLichSuThanhToan.setCurrency(c.getString(c.getColumnIndex("currency")));
            entityLichSuThanhToan.setPriceDetails(c.getString(c.getColumnIndex("priceDetails")));
            entityLichSuThanhToan.setNumeDetails(c.getString(c.getColumnIndex("numeDetails")));
            entityLichSuThanhToan.setAmountDetails(c.getString(c.getColumnIndex("amountDetails")));
            entityLichSuThanhToan.setOldIndex(c.getString(c.getColumnIndex("oldIndex")));
            entityLichSuThanhToan.setNewIndex(c.getString(c.getColumnIndex("newIndex")));
            entityLichSuThanhToan.setNume(c.getString(c.getColumnIndex("nume")));
            entityLichSuThanhToan.setAmountNotTax(c.getInt(c.getColumnIndex("amountNotTax")));
            entityLichSuThanhToan.setAmountTax(c.getString(c.getColumnIndex("amountTax")));
            entityLichSuThanhToan.setMultiple(c.getString(c.getColumnIndex("multiple")));
            entityLichSuThanhToan.setBillType(c.getString(c.getColumnIndex("billType")));
            entityLichSuThanhToan.setTypeIndex(c.getString(c.getColumnIndex("typeIndex")));
            entityLichSuThanhToan.setGroupTypeIndex(c.getString(c.getColumnIndex("groupTypeIndex")));
            entityLichSuThanhToan.setCreatedDate(c.getString(c.getColumnIndex("createdDate")));
            entityLichSuThanhToan.setIdChanged(c.getInt(c.getColumnIndex("idChanged")));
            entityLichSuThanhToan.setDateChanged(c.getString(c.getColumnIndex("dateChanged")));
            entityLichSuThanhToan.setPcCodeExt(c.getString(c.getColumnIndex("pcCodeExt")));
            entityLichSuThanhToan.setCode(c.getString(c.getColumnIndex("code")));
            entityLichSuThanhToan.setName(c.getString(c.getColumnIndex("name")));
            entityLichSuThanhToan.setNameNosign(c.getString(c.getColumnIndex("nameNosign")));
            entityLichSuThanhToan.setPhoneByevn(c.getString(c.getColumnIndex("phoneByevn")));
            entityLichSuThanhToan.setPhoneByecp(c.getString(c.getColumnIndex("phoneByecp")));
            entityLichSuThanhToan.setElectricityMeter(c.getString(c.getColumnIndex("electricityMeter")));
            entityLichSuThanhToan.setInning(c.getString(c.getColumnIndex("inning")));
            entityLichSuThanhToan.setRoad(c.getString(c.getColumnIndex("road")));
            entityLichSuThanhToan.setStation(c.getString(c.getColumnIndex("station")));
            entityLichSuThanhToan.setTaxCode(c.getString(c.getColumnIndex("taxCode")));
            entityLichSuThanhToan.setTrade(c.getString(c.getColumnIndex("trade")));
            entityLichSuThanhToan.setCountPeriod(c.getString(c.getColumnIndex("countPeriod")));
            entityLichSuThanhToan.setTeam(c.getString(c.getColumnIndex("team")));
            entityLichSuThanhToan.setType(c.getInt(c.getColumnIndex("type")));
            entityLichSuThanhToan.setLastQuery(c.getString(c.getColumnIndex("lastQuery")));
            entityLichSuThanhToan.setGroupType(c.getInt(c.getColumnIndex("groupType")));
            entityLichSuThanhToan.setBillingChannel(c.getString(c.getColumnIndex("billingChannel")));
            entityLichSuThanhToan.setBillType(c.getString(c.getColumnIndex("billingType")));
            entityLichSuThanhToan.setBillingBy(c.getString(c.getColumnIndex("billingBy")));
            entityLichSuThanhToan.setCashierPay(c.getString(c.getColumnIndex("cashierPay")));
        }
        return entityLichSuThanhToan;
    }

    //endregion

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

        initialValues.put("code", bodyCustomerResponse.getCustomerCode());
        initialValues.put("name", bodyCustomerResponse.getName());
        initialValues.put("cardNo", bodyCustomerResponse.getCardNo());
        initialValues.put("address", bodyCustomerResponse.getAddress());
        initialValues.put("pcCode", bodyCustomerResponse.getPcCode());
        initialValues.put("pcCodeExt", bodyCustomerResponse.getPcCodeExt());
        initialValues.put("phoneByevn", bodyCustomerResponse.getPhoneByEVN());
        initialValues.put("phoneByecp", bodyCustomerResponse.getPhoneByECP());
        initialValues.put("bookCmis", bodyCustomerResponse.getBookCmis());
        initialValues.put("electricityMeter", bodyCustomerResponse.getElectricityMeter());
        initialValues.put("inning", bodyCustomerResponse.getInning());
        initialValues.put("status", bodyCustomerResponse.getStatus());
        initialValues.put("bankAccount", "");
        initialValues.put("idNumber", bodyCustomerResponse.getId());
        initialValues.put("bankName", "");
        initialValues.put("edongKey", MainActivity.mEdong);
        initialValues.put("isShowBill", 0);
        initialValues.put("idChanged", footerCustomerResponse.getIdChanged());
        initialValues.put("dateChanged", footerCustomerResponse.getDateChanged());

        database = getWritableDatabase();
        int rowAffect = (int) database.insert(TABLE_NAME_CUSTOMER, null, initialValues);
        return rowAffect;
    }

    public long insertCustomer(CustomerResponse customerResponse) {
        ContentValues initialValues = new ContentValues();

        views.ecpay.com.postabletecpay.util.entities.response.EntityCustomer.BodyCustomerResponse bodyCustomerResponse = customerResponse.getBodyCustomerResponse();
        views.ecpay.com.postabletecpay.util.entities.response.EntityCustomer.FooterCustomerResponse footerCustomerResponse = customerResponse.getFooterCustomerResponse();

        initialValues.put("code", bodyCustomerResponse.getCustomerCode());
        initialValues.put("name", bodyCustomerResponse.getName());
        initialValues.put("cardNo", bodyCustomerResponse.getCardNo());
        initialValues.put("address", bodyCustomerResponse.getAddress());
        initialValues.put("pcCode", bodyCustomerResponse.getPcCode());
        initialValues.put("pcCodeExt", bodyCustomerResponse.getPcCodeExt());
        initialValues.put("phoneByevn", bodyCustomerResponse.getPhoneByEVN());
        initialValues.put("phoneByecp", bodyCustomerResponse.getPhoneByECP());
        initialValues.put("bookCmis", bodyCustomerResponse.getBookCmis());
        initialValues.put("electricityMeter", bodyCustomerResponse.getElectricityMeter());
        initialValues.put("inning", bodyCustomerResponse.getInning());
        initialValues.put("status", bodyCustomerResponse.getStatus());
        initialValues.put("bankAccount", "");
        initialValues.put("idNumber", bodyCustomerResponse.getId());
        initialValues.put("bankName", "");
        initialValues.put("edongKey", MainActivity.mEdong);
        initialValues.put("isShowBill", 0);
        initialValues.put("idChanged", footerCustomerResponse.getIdChanged());
        initialValues.put("dateChanged", footerCustomerResponse.getDateChanged());

        database = getWritableDatabase();
        int rowAffect = (int) database.insert(TABLE_NAME_CUSTOMER, null, initialValues);
        return rowAffect;
    }

    public long updateCustomer(CustomerResponse customerResponse) {
        ContentValues initialValues = new ContentValues();

        views.ecpay.com.postabletecpay.util.entities.response.EntityCustomer.BodyCustomerResponse bodyCustomerResponse = customerResponse.getBodyCustomerResponse();
        views.ecpay.com.postabletecpay.util.entities.response.EntityCustomer.FooterCustomerResponse footerCustomerResponse = customerResponse.getFooterCustomerResponse();

        initialValues.put("name", bodyCustomerResponse.getName());
        initialValues.put("address", bodyCustomerResponse.getAddress());
        initialValues.put("pcCode", bodyCustomerResponse.getPcCode());
        initialValues.put("cardNo", bodyCustomerResponse.getCardNo());
        initialValues.put("pcCodeExt", bodyCustomerResponse.getPcCodeExt());
        initialValues.put("phoneByevn", bodyCustomerResponse.getPhoneByEVN());
        initialValues.put("phoneByecp", bodyCustomerResponse.getPhoneByECP());
        initialValues.put("bookCmis", bodyCustomerResponse.getBookCmis());
        initialValues.put("electricityMeter", bodyCustomerResponse.getElectricityMeter());
        initialValues.put("inning", bodyCustomerResponse.getInning());
        initialValues.put("status", bodyCustomerResponse.getStatus());
        initialValues.put("bankAccount", "");
        initialValues.put("idNumber", bodyCustomerResponse.getId());
        initialValues.put("bankName", "");
        initialValues.put("edongKey", MainActivity.mEdong);
        initialValues.put("isShowBill", 0);
        initialValues.put("idChanged", footerCustomerResponse.getIdChanged());
        initialValues.put("dateChanged", footerCustomerResponse.getDateChanged());

        database = getWritableDatabase();
        int rowAffect = (int) database.update(TABLE_NAME_CUSTOMER, initialValues, "code = ?", new String[]{bodyCustomerResponse.getCustomerCode()});
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
        String query = "SELECT COUNT(*) FROM " + TABLE_NAME_CUSTOMER + " WHERE code = '" + code + "'";
        Cursor mCursor = database.rawQuery(query, null);
        if (mCursor.moveToFirst())
            return mCursor.getInt(0);
        return 0;
    }

    public Cursor getCustomer(String code) {
        database = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_CUSTOMER + " WHERE code = '" + code + "'";
        return database.rawQuery(query, null);
    }
    //endregion

    //region BILL Danh sách hóa dơn nợ
    public long insertBill(ListBillResponse listBillResponse) {
        ContentValues initialValues = new ContentValues();

        BodyBillResponse bodyBillResponse = listBillResponse.getBodyBillResponse();
        FooterBillResponse footerBillResponse = listBillResponse.getFooterBillResponse();

        initialValues.put("customerCode", bodyBillResponse.getCustomerCode());
        initialValues.put("customerPayCode", "");
        initialValues.put("billId", !bodyBillResponse.getBillId().isEmpty() ? Integer.parseInt(bodyBillResponse.getBillId()) : 0);
        String term = bodyBillResponse.getTerm();
        //20170414011107000 != 2015-01-01
        if (term.length() == yyyyMMdd.toString().length()) {
            term = Common.convertDateToDate(term, yyyyMMdd, yyyyMMddHHmmssSSS);
        }
        initialValues.put("term", term);
        initialValues.put("strTerm", "");
        initialValues.put("amount", !bodyBillResponse.getAmount().equals("") ? Integer.parseInt(bodyBillResponse.getAmount()) : 0);
        initialValues.put("period", bodyBillResponse.getPeriod());
        initialValues.put("issueDate", bodyBillResponse.getIssueDate());
        initialValues.put("strIssueDate", "");
        int status = !bodyBillResponse.getStatus().isEmpty() ? Integer.parseInt(bodyBillResponse.getStatus()) : 0;
        initialValues.put("status", status);
        initialValues.put("seri", bodyBillResponse.getSeri());
        initialValues.put("pcCode", bodyBillResponse.getPcCode());
        initialValues.put("handoverCode", bodyBillResponse.getHandOverCode());
        initialValues.put("cashierCode", bodyBillResponse.getCashierCode());
        initialValues.put("bookCmis", bodyBillResponse.getBookCmis());
        initialValues.put("fromDate", bodyBillResponse.getFromDate());
        initialValues.put("toDate", bodyBillResponse.getToDate());
        initialValues.put("strFromDate", "");
        initialValues.put("strToDate", "");
        initialValues.put("home", bodyBillResponse.getHome());
        initialValues.put("tax", !bodyBillResponse.getTax().isEmpty() ? Float.parseFloat(bodyBillResponse.getTax()) : 0f);
        initialValues.put("billNum", bodyBillResponse.getBillNum());
        initialValues.put("currency", bodyBillResponse.getCurrency());
        initialValues.put("priceDetails", bodyBillResponse.getPriceDetail());
        initialValues.put("numeDetails", bodyBillResponse.getNumeDetail());
        initialValues.put("amountDetails", bodyBillResponse.getAmountDetail());
        initialValues.put("oldIndex", bodyBillResponse.getOldIndex());
        initialValues.put("newIndex", bodyBillResponse.getNewIndex());
        initialValues.put("nume", bodyBillResponse.getNume());
        initialValues.put("amountNotTax", !bodyBillResponse.getAmountNotTax().isEmpty() ? Integer.parseInt(bodyBillResponse.getAmountNotTax()) : 0);
        initialValues.put("amountTax", !bodyBillResponse.getAmountTax().isEmpty() ? Integer.parseInt(bodyBillResponse.getAmountTax()) : 0);
        initialValues.put("multiple", bodyBillResponse.getMultiple());
        initialValues.put("billType", bodyBillResponse.getBillType());
        initialValues.put("typeIndex", bodyBillResponse.getTypeIndex());
        initialValues.put("groupTypeIndex", bodyBillResponse.getGroupTypeIndex());
        initialValues.put("createdDate", bodyBillResponse.getCreatedDate());
        initialValues.put("idChanged", footerBillResponse.getIdChanged());
        initialValues.put("dateChanged", footerBillResponse.getDateChanged());
        initialValues.put("edong", bodyBillResponse.getEdong());
        initialValues.put("pcCodeExt", "");
        initialValues.put("code", "");
        initialValues.put("name", "");
        initialValues.put("nameNosign", "");
        initialValues.put("phoneByevn", "");
        initialValues.put("phoneByecp", "");
        initialValues.put("electricityMeter", "");
        initialValues.put("inning", "");
        initialValues.put("road", "");
        initialValues.put("station", "");
        initialValues.put("taxCode", bodyBillResponse.getTax());
        initialValues.put("trade", "");
        initialValues.put("countPeriod", "");
        initialValues.put("team", "");
        initialValues.put("type", bodyBillResponse.getBillType());
        initialValues.put("lastQuery", "");
        initialValues.put("groupType", !bodyBillResponse.getGroupTypeIndex().isEmpty() ? Integer.parseInt(bodyBillResponse.getGroupTypeIndex()) : 0);
        initialValues.put("billingChannel", "");
        initialValues.put("billingType", bodyBillResponse.getBillingType());
        initialValues.put("billingBy", "");
        initialValues.put("cashierPay", bodyBillResponse.getCashierCode());
        initialValues.put("edongKey", bodyBillResponse.getEdong());

        //nếu status = 1(đã thanh toán) khi insert vào bill thì bật cờ isChecked = 1 tức được chọn và đã thanh toán
        initialValues.put("isChecked", (status == ZERO) ? ZERO : ONE);

        database = getWritableDatabase();
        int rowAffect = (int) database.insert(TABLE_NAME_BILL, null, initialValues);
        return rowAffect;
    }

    public long insertBill(BillResponse listBillResponse) {
        ContentValues initialValues = new ContentValues();

        views.ecpay.com.postabletecpay.util.entities.response.EntityBill.BodyBillResponse bodyBillResponse = listBillResponse.getBodyBillResponse();
        views.ecpay.com.postabletecpay.util.entities.response.EntityBill.FooterBillResponse footerBillResponse = listBillResponse.getFooterBillResponse();

        initialValues.put("customerCode", bodyBillResponse.getCustomerCode());
        initialValues.put("customerPayCode", "");
        initialValues.put("billId", bodyBillResponse.getBillId());
        String term = bodyBillResponse.getTerm();
        //20170414011107000 != 2015-01-01
        if (term.length() == yyyyMMdd.toString().length()) {
            term = Common.convertDateToDate(term, yyyyMMdd, yyyyMMddHHmmssSSS);
        }
        initialValues.put("term", term);
        initialValues.put("strTerm", "");
        initialValues.put("amount", bodyBillResponse.getAmount());
        initialValues.put("period", bodyBillResponse.getPeriod());
        initialValues.put("issueDate", bodyBillResponse.getIssueDate());
        initialValues.put("strIssueDate", "");

        int status = Integer.parseInt(bodyBillResponse.getStatus());
        initialValues.put("status", status);

        initialValues.put("seri", bodyBillResponse.getSeri());
        initialValues.put("pcCode", bodyBillResponse.getPcCode());
        initialValues.put("handoverCode", bodyBillResponse.getHandOverCode());
        initialValues.put("cashierCode", bodyBillResponse.getCashierCode());
        initialValues.put("bookCmis", bodyBillResponse.getBookCmis());
        initialValues.put("fromDate", bodyBillResponse.getFromDate());
        initialValues.put("toDate", bodyBillResponse.getToDate());
        initialValues.put("strFromDate", "");
        initialValues.put("strToDate", "");
        initialValues.put("home", bodyBillResponse.getHome());
        initialValues.put("tax", bodyBillResponse.getTax());
        initialValues.put("billNum", bodyBillResponse.getBillNum());
        initialValues.put("currency", bodyBillResponse.getCurrency());
        initialValues.put("priceDetails", bodyBillResponse.getPriceDetail());
        initialValues.put("numeDetails", bodyBillResponse.getNumeDetail());
        initialValues.put("amountDetails", bodyBillResponse.getAmountDetail());
        initialValues.put("oldIndex", bodyBillResponse.getOldIndex());
        initialValues.put("newIndex", bodyBillResponse.getNewIndex());
        initialValues.put("nume", bodyBillResponse.getNume());
        initialValues.put("amountNotTax", bodyBillResponse.getAmountNotTax());
        initialValues.put("amountTax", bodyBillResponse.getAmountTax());
        initialValues.put("multiple", bodyBillResponse.getMultiple());
        initialValues.put("billType", bodyBillResponse.getBillType());
        initialValues.put("typeIndex", bodyBillResponse.getTypeIndex());
        initialValues.put("groupTypeIndex", bodyBillResponse.getGroupTypeIndex());
        initialValues.put("createdDate", bodyBillResponse.getCreatedDate());
        initialValues.put("idChanged", footerBillResponse.getIdChanged());
        initialValues.put("dateChanged", footerBillResponse.getDateChanged());
        initialValues.put("edong", bodyBillResponse.getEdong());
        initialValues.put("pcCodeExt", "");
        initialValues.put("code", "");
        initialValues.put("name", "");
        initialValues.put("nameNosign", "");
        initialValues.put("phoneByevn", "");
        initialValues.put("phoneByecp", "");
        initialValues.put("electricityMeter", "");
        initialValues.put("inning", "");
        initialValues.put("road", "");
        initialValues.put("station", "");
        initialValues.put("taxCode", bodyBillResponse.getTax());
        initialValues.put("trade", "");
        initialValues.put("countPeriod", "");
        initialValues.put("team", "");
        initialValues.put("type", bodyBillResponse.getBillType());
        initialValues.put("lastQuery", "");
        initialValues.put("groupType", !bodyBillResponse.getGroupTypeIndex().isEmpty() ? Integer.parseInt(bodyBillResponse.getGroupTypeIndex()) : 0);
        initialValues.put("billingChannel", "");
        initialValues.put("billingType", bodyBillResponse.getBillingType());
        initialValues.put("billingBy", "");
        initialValues.put("cashierPay", bodyBillResponse.getCashierCode());
        initialValues.put("edongKey", bodyBillResponse.getEdong());

        //nếu status = 0(chưa thanh toán) khi insert vào bill thì cờ isChecked = 0
        initialValues.put("isChecked", (status == ZERO) ? ZERO : ONE);


        database = getWritableDatabase();
        int rowAffect = (int) database.insert(TABLE_NAME_BILL, null, initialValues);
        return rowAffect;
    }

    public long updateBill(BillResponse listBillResponse) {
        ContentValues initialValues = new ContentValues();

        views.ecpay.com.postabletecpay.util.entities.response.EntityBill.BodyBillResponse bodyBillResponse = listBillResponse.getBodyBillResponse();
        views.ecpay.com.postabletecpay.util.entities.response.EntityBill.FooterBillResponse footerBillResponse = listBillResponse.getFooterBillResponse();

        initialValues.put("customerCode", bodyBillResponse.getCustomerCode());
        initialValues.put("customerPayCode", "");
        String term = bodyBillResponse.getTerm();
        //20170414011107000 != 2015-01-01
        if (term.length() == yyyyMMdd.toString().length()) {
            term = Common.convertDateToDate(term, yyyyMMdd, yyyyMMddHHmmssSSS);
        }
        initialValues.put("term", term);
        initialValues.put("strTerm", "");
        initialValues.put("amount", bodyBillResponse.getAmount());
        initialValues.put("period", bodyBillResponse.getPeriod());
        initialValues.put("issueDate", bodyBillResponse.getIssueDate());
        initialValues.put("strIssueDate", "");

        int status = Integer.parseInt(bodyBillResponse.getStatus());
        initialValues.put("status", status);

        initialValues.put("seri", bodyBillResponse.getSeri());
        initialValues.put("pcCode", bodyBillResponse.getPcCode());
        initialValues.put("handoverCode", bodyBillResponse.getHandOverCode());
        initialValues.put("cashierCode", bodyBillResponse.getCashierCode());
        initialValues.put("bookCmis", bodyBillResponse.getBookCmis());
        initialValues.put("fromDate", bodyBillResponse.getFromDate());
        initialValues.put("toDate", bodyBillResponse.getToDate());
        initialValues.put("strFromDate", "");
        initialValues.put("strToDate", "");
        initialValues.put("home", bodyBillResponse.getHome());
        initialValues.put("tax", bodyBillResponse.getTax());
        initialValues.put("billNum", bodyBillResponse.getBillNum());
        initialValues.put("currency", bodyBillResponse.getCurrency());
        initialValues.put("priceDetails", bodyBillResponse.getPriceDetail());
        initialValues.put("numeDetails", bodyBillResponse.getNumeDetail());
        initialValues.put("amountDetails", bodyBillResponse.getAmountDetail());
        initialValues.put("oldIndex", bodyBillResponse.getOldIndex());
        initialValues.put("newIndex", bodyBillResponse.getNewIndex());
        initialValues.put("nume", bodyBillResponse.getNume());
        initialValues.put("amountNotTax", bodyBillResponse.getAmountNotTax());
        initialValues.put("amountTax", bodyBillResponse.getAmountTax());
        initialValues.put("multiple", bodyBillResponse.getMultiple());
        initialValues.put("billType", bodyBillResponse.getBillType());
        initialValues.put("typeIndex", bodyBillResponse.getTypeIndex());
        initialValues.put("groupTypeIndex", bodyBillResponse.getGroupTypeIndex());
        initialValues.put("createdDate", bodyBillResponse.getCreatedDate());
        initialValues.put("idChanged", footerBillResponse.getIdChanged());
        initialValues.put("dateChanged", footerBillResponse.getDateChanged());
        initialValues.put("edong", bodyBillResponse.getEdong());
        initialValues.put("pcCodeExt", "");
        initialValues.put("code", "");
        initialValues.put("name", "");
        initialValues.put("nameNosign", "");
        initialValues.put("phoneByevn", "");
        initialValues.put("phoneByecp", "");
        initialValues.put("electricityMeter", "");
        initialValues.put("inning", "");
        initialValues.put("road", "");
        initialValues.put("station", "");
        initialValues.put("taxCode", bodyBillResponse.getTax());
        initialValues.put("trade", "");
        initialValues.put("countPeriod", "");
        initialValues.put("team", "");
        initialValues.put("type", bodyBillResponse.getBillType());
        initialValues.put("lastQuery", "");
        initialValues.put("groupType", !bodyBillResponse.getGroupTypeIndex().isEmpty() ? Integer.parseInt(bodyBillResponse.getGroupTypeIndex()) : 0);
        initialValues.put("billingChannel", "");
        initialValues.put("billingType", bodyBillResponse.getBillingType());
        initialValues.put("billingBy", "");
        initialValues.put("cashierPay", bodyBillResponse.getCashierCode());
        initialValues.put("edongKey", bodyBillResponse.getEdong());

        database = getWritableDatabase();
        int rowAffect = (int) database.update(TABLE_NAME_BILL, initialValues, "billId=?", new String[]{String.valueOf(bodyBillResponse.getBillId())});
        return rowAffect;
    }

    public int updateBillWith(String edongKey, int billId, int status, String edong) {
        ContentValues initialValues = new ContentValues();

        initialValues.put("status", status);
        initialValues.put("edong", edong);

        database = getWritableDatabase();
        int rowAffect = (int) database.update(TABLE_NAME_DEBT_COLLECTION, initialValues, " edongKey = ? and billId = ? ", new String[]{edongKey, String.valueOf(billId)});
        return rowAffect;
    }

    public long checkBillExist(String billId) {
        database = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + TABLE_NAME_BILL + " WHERE billId = '" + billId + "'";
        Cursor mCursor = database.rawQuery(query, null);
        if (mCursor.moveToFirst())
            return mCursor.getInt(0);
        return 0;
    }

    public long getMaxIdChanged() {
        database = this.getReadableDatabase();
        String query = "SELECT MAX(idChanged) FROM " + TABLE_NAME_BILL;
        Cursor c = database.rawQuery(query, null);
        if (c.moveToFirst()) {
            return c.getLong(0);
        }
        return 0l;
    }

    public String getMaxDateChanged() {
        database = this.getReadableDatabase();
        String query = "SELECT MAX(dateChanged) FROM " + TABLE_NAME_BILL;
        Cursor c = database.rawQuery(query, null);
        if (c.moveToFirst()) {
            return c.getString(0);
        }
        return "";
    }

    public boolean checkIsHasBillNotPayTermBefore(String edong, String code, String term) {
        database = this.getReadableDatabase();

        String query = "SELECT billId FROM " + TABLE_NAME_BILL + " WHERE edongKey = '" + edong + "' and customerCode = '" + code + "' and status = " + Common.STATUS_BILLING.CHUA_THANH_TOAN.getCode() + " and term < " + term;
        Cursor mCursor = database.rawQuery(query, null);

        if (mCursor.getCount() > 0)
            return true;

        return false;
    }

    public String getTermBillOfCustomer(String edong, String code, int billId) {
        database = this.getReadableDatabase();

        String query = "SELECT term FROM " + TABLE_NAME_BILL + " WHERE edongKey = '" + edong + "' and customerCode = '" + code + "' and billID = " + billId;
        Cursor mCursor = database.rawQuery(query, null);

        if (mCursor.getCount() == 0)
            return null;

        if (mCursor.moveToFirst())
            return stringConvertNull(mCursor.getString(mCursor.getColumnIndex("term")));

        return null;
    }
    //endregion

    //region DEBT Danh sách hóa đơn thu
    public int insertDebtCollection(EntityDanhSachThu entityDanhSachThu) {

        if (entityDanhSachThu.getEdong() == null || entityDanhSachThu.getEdong().trim().isEmpty())
            return SQLiteConnection.ERROR_OCCUR;

        ContentValues initialValues = new ContentValues();
        initialValues.put("customerCode", entityDanhSachThu.getCustomerCode());
        initialValues.put("customerPayCode", entityDanhSachThu.getCustomerPayCode());
        initialValues.put("billId", entityDanhSachThu.getBillId());

        //20170414011107000 != 2015-01-01
        String term = "";
        if (entityDanhSachThu.getTerm().length() == yyyyMMdd.toString().length()) {
            term = Common.convertDateToDate(entityDanhSachThu.getTerm(), yyyyMMdd, yyyyMMddHHmmssSSS);
        }
        initialValues.put("term", term);
        initialValues.put("amount", entityDanhSachThu.getAmount());
        initialValues.put("period", entityDanhSachThu.getPeriod());
        initialValues.put("issueDate", entityDanhSachThu.getIssueDate());
        initialValues.put("strIssueDate", entityDanhSachThu.getStrIssueDate());
        initialValues.put("status", entityDanhSachThu.getStatus());
        initialValues.put("seri", entityDanhSachThu.getSeri());
        initialValues.put("pcCode", entityDanhSachThu.getPcCode());
        initialValues.put("handoverCode", entityDanhSachThu.getHandoverCode());
        initialValues.put("cashierCode", entityDanhSachThu.getCashierCode());
        initialValues.put("bookCmis", entityDanhSachThu.getBookCmis());
        initialValues.put("fromDate", entityDanhSachThu.getFromDate());
        initialValues.put("toDate", entityDanhSachThu.getToDate());
        initialValues.put("strFromDate", entityDanhSachThu.getStrFromDate());
        initialValues.put("strToDate", entityDanhSachThu.getStrToDate());
        initialValues.put("home", entityDanhSachThu.getHome());
        initialValues.put("tax", entityDanhSachThu.getTax());
        initialValues.put("billNum", entityDanhSachThu.getBillNum());
        initialValues.put("currency", entityDanhSachThu.getCurrency());
        initialValues.put("priceDetails", entityDanhSachThu.getPriceDetails());
        initialValues.put("numeDetails", entityDanhSachThu.getNumeDetails());
        initialValues.put("amountDetails", entityDanhSachThu.getAmountDetails());
        initialValues.put("oldIndex", entityDanhSachThu.getOldIndex());
        initialValues.put("newIndex", entityDanhSachThu.getNewIndex());
        initialValues.put("nume", entityDanhSachThu.getNume());
        initialValues.put("amountNotTax", entityDanhSachThu.getAmountNotTax());
        initialValues.put("amountTax", entityDanhSachThu.getAmountTax());
        initialValues.put("multiple", entityDanhSachThu.getMultiple());
        initialValues.put("billType", entityDanhSachThu.getBillType());
        initialValues.put("typeIndex", entityDanhSachThu.getTypeIndex());
        initialValues.put("groupTypeIndex", entityDanhSachThu.getGroupTypeIndex());
        initialValues.put("createdDate", entityDanhSachThu.getCreatedDate());
        initialValues.put("idChanged", entityDanhSachThu.getIdChanged());
        initialValues.put("dateChanged", entityDanhSachThu.getDateChanged());
        initialValues.put("edong", entityDanhSachThu.getEdong());
        initialValues.put("pcCodeExt", entityDanhSachThu.getPcCodeExt());
        initialValues.put("code", entityDanhSachThu.getCode());
        initialValues.put("name", entityDanhSachThu.getName());
        initialValues.put("nameNosign", entityDanhSachThu.getNameNosign());
        initialValues.put("phoneByevn", entityDanhSachThu.getPhoneByevn());
        initialValues.put("phoneByecp", entityDanhSachThu.getPhoneByecp());
        initialValues.put("electricityMeter", entityDanhSachThu.getElectricityMeter());
        initialValues.put("inning", entityDanhSachThu.getInning());
        initialValues.put("road", entityDanhSachThu.getRoad());
        initialValues.put("station", entityDanhSachThu.getStation());
        initialValues.put("taxCode", entityDanhSachThu.getTaxCode());
        initialValues.put("trade", entityDanhSachThu.getTrade());
        initialValues.put("countPeriod", entityDanhSachThu.getCountPeriod());
        initialValues.put("team", entityDanhSachThu.getTeam());
        initialValues.put("type", entityDanhSachThu.getType());
        initialValues.put("lastQuery", entityDanhSachThu.getLastQuery());
        initialValues.put("groupType", entityDanhSachThu.getGroupType());
        initialValues.put("billingChannel", entityDanhSachThu.getBillingChannel());
        initialValues.put("billingType", entityDanhSachThu.getBillingType());
        initialValues.put("billingBy", entityDanhSachThu.getBillingBy());
        initialValues.put("cashierPay", entityDanhSachThu.getCashierPay());
        initialValues.put("edongKey", entityDanhSachThu.getEdong());
        initialValues.put("payments", entityDanhSachThu.getPayments());
        initialValues.put("payStatus", entityDanhSachThu.getPayStatus());
        initialValues.put("stateOfDebt", entityDanhSachThu.getStateOfDebt());
        initialValues.put("stateOfCancel", entityDanhSachThu.getStateOfCancel());
        initialValues.put("stateOfReturn", entityDanhSachThu.getStateOfReturn());
        initialValues.put("suspectedProcessingStatus", entityDanhSachThu.getSuspectedProcessingStatus());
        initialValues.put("stateOfPush", entityDanhSachThu.getStateOfPush());
        initialValues.put("dateOfPush", entityDanhSachThu.getDateOfPush());
        initialValues.put("countPrintReceipt", entityDanhSachThu.getCountPrintReceipt());
        initialValues.put("printInfo", entityDanhSachThu.getPrintInfo());
        database = getWritableDatabase();
        int rowAffect = (int) database.insert(TABLE_NAME_DEBT_COLLECTION, null, initialValues);

        return rowAffect;
    }

    public int updateBillDebtWith(
            String edongKey, int billId, //where
            String edong, Integer paymentMode, int payStatus, Integer stateOfDebt, Integer stateOfCancel,
            Integer stateOfReturn, Integer suspectedProcessingStatus, Integer stateOfPush, String dateOfPush,
            int countPrintReceipt, Integer statusOfPrintInfo) {
        ContentValues initialValues = new ContentValues();

        initialValues.put("edong", edong);
        initialValues.put("payments", paymentMode);

        initialValues.put("payStatus", payStatus);
        initialValues.put("stateOfDebt", stateOfDebt);
        initialValues.put("stateOfCancel", stateOfCancel);
        initialValues.put("stateOfReturn", stateOfReturn);
        initialValues.put("suspectedProcessingStatus", suspectedProcessingStatus);
        initialValues.put("stateOfPush", stateOfPush);
        initialValues.put("dateOfPush", dateOfPush);
        initialValues.put("countPrintReceipt", countPrintReceipt);
        initialValues.put("printInfo", statusOfPrintInfo);

        database = getWritableDatabase();
        int rowAffect = (int) database.update(TABLE_NAME_DEBT_COLLECTION, initialValues, " edongKey = ? and billId = ?", new String[]{edongKey, String.valueOf(billId)});
        return rowAffect;
    }

    public int updateBillDebtWithThanhToanBoiNguonKhac(String edongKey, int billId, String edong, Integer paymentMode, int payStatus, Integer stateOfDebt, Integer stateOfCancel, Integer stateOfReturn, Integer suspectedProcessingStatus, String dateOfPush, int countPrintReceipt, Integer statusOfPrintInfo, Integer tradeCode) {
        ContentValues initialValues = new ContentValues();

        initialValues.put("edong", edong);
        initialValues.put("payments", paymentMode);

        initialValues.put("payStatus", payStatus);
        initialValues.put("stateOfDebt", stateOfDebt);
        initialValues.put("stateOfCancel", stateOfCancel);
        initialValues.put("stateOfReturn", stateOfReturn);
        initialValues.put("suspectedProcessingStatus", suspectedProcessingStatus);
        initialValues.put("dateOfPush", dateOfPush);
        initialValues.put("countPrintReceipt", countPrintReceipt);
        initialValues.put("printInfo", statusOfPrintInfo);
        initialValues.put("tradeCode", tradeCode);

        database = getWritableDatabase();
        int rowAffect = (int) database.update(TABLE_NAME_DEBT_COLLECTION, initialValues, " edongKey = ? and billId = ?", new String[]{edongKey, String.valueOf(billId)});
        return rowAffect;
    }


    public int selectPayStatusDebt(String edong, String code, int billId) {
        database = this.getReadableDatabase();
        String query = "SELECT payStatus FROM " + TABLE_NAME_DEBT_COLLECTION + " WHERE edongKey = '" + edong + "' and customerCode = '" + code + "' and billId = '" + billId + "'";
        Cursor mCursor = database.rawQuery(query, null);
        if (mCursor.moveToFirst())
            return mCursor.getInt(0);
        return ERROR_OCCUR;
    }

    public int updateBillDebtWithThanhToanErrorOrSuccess(
            String edongKey, int billId, //where
            String edong, Integer paymentMode, int payStatus, Integer stateOfDebt, Integer stateOfCancel,
            Integer stateOfReturn, Integer suspectedProcessingStatus, int countPrintReceipt, Integer statusOfPrintInfo) {
        ContentValues initialValues = new ContentValues();

        initialValues.put("edong", edong);
        initialValues.put("payments", paymentMode);

        initialValues.put("payStatus", payStatus);
        initialValues.put("stateOfDebt", stateOfDebt);
        initialValues.put("stateOfCancel", stateOfCancel);
        initialValues.put("stateOfReturn", stateOfReturn);
        initialValues.put("suspectedProcessingStatus", suspectedProcessingStatus);
        initialValues.put("countPrintReceipt", countPrintReceipt);
        initialValues.put("printInfo", statusOfPrintInfo);

        database = getWritableDatabase();
        int rowAffect = (int) database.update(TABLE_NAME_DEBT_COLLECTION, initialValues, " edongKey = ? and billId = ?", new String[]{edongKey, String.valueOf(billId)});
        return rowAffect;
    }

    public int updateBillDebtWithSuspectedProcessingStatus(String edong, int billId, Integer suspectedProcessingStatus) {
        database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("suspectedProcessingStatus", suspectedProcessingStatus);
        return database.update(TABLE_NAME_DEBT_COLLECTION, contentValues, "edongKey = ? and billId = ? ", new String[]{edong, String.valueOf(billId)});
    }

    //endregion

    //region HISTORY Lịch sử
    public int insertPayLib(EntityLichSuThanhToan entityLichSuThanhToan) {

        database = getWritableDatabase();

        if (entityLichSuThanhToan.getEdong() == null || entityLichSuThanhToan.getEdong().trim().isEmpty())
            return SQLiteConnection.ERROR_OCCUR;

        ContentValues initialValues = new ContentValues();
        initialValues.put("customerCode", entityLichSuThanhToan.getCustomerCode());
        initialValues.put("customerPayCode", entityLichSuThanhToan.getCustomerPayCode());
        initialValues.put("billId", entityLichSuThanhToan.getBillId());

        //20170414011107000 != 2015-01-01
        String term = "";
        if (entityLichSuThanhToan.getTerm().length() == yyyyMMdd.toString().length()) {
            term = Common.convertDateToDate(entityLichSuThanhToan.getTerm(), yyyyMMdd, yyyyMMddHHmmssSSS);
        }
        initialValues.put("term", term);
        initialValues.put("amount", entityLichSuThanhToan.getAmount());
        initialValues.put("period", entityLichSuThanhToan.getPeriod());
        initialValues.put("issueDate", entityLichSuThanhToan.getIssueDate());
        initialValues.put("strIssueDate", entityLichSuThanhToan.getStrIssueDate());
        initialValues.put("status", entityLichSuThanhToan.getStatus());
        initialValues.put("seri", entityLichSuThanhToan.getSeri());
        initialValues.put("pcCode", entityLichSuThanhToan.getPcCode());
        initialValues.put("handoverCode", entityLichSuThanhToan.getHandoverCode());
        initialValues.put("cashierCode", entityLichSuThanhToan.getCashierCode());
        initialValues.put("bookCmis", entityLichSuThanhToan.getBookCmis());
        initialValues.put("fromDate", entityLichSuThanhToan.getFromDate());
        initialValues.put("toDate", entityLichSuThanhToan.getToDate());
        initialValues.put("strFromDate", entityLichSuThanhToan.getStrFromDate());
        initialValues.put("strToDate", entityLichSuThanhToan.getStrToDate());
        initialValues.put("home", entityLichSuThanhToan.getHome());
        initialValues.put("tax", entityLichSuThanhToan.getTax());
        initialValues.put("billNum", entityLichSuThanhToan.getBillNum());
        initialValues.put("currency", entityLichSuThanhToan.getCurrency());
        initialValues.put("priceDetails", entityLichSuThanhToan.getPriceDetails());
        initialValues.put("numeDetails", entityLichSuThanhToan.getNumeDetails());
        initialValues.put("amountDetails", entityLichSuThanhToan.getAmountDetails());
        initialValues.put("oldIndex", entityLichSuThanhToan.getOldIndex());
        initialValues.put("newIndex", entityLichSuThanhToan.getNewIndex());
        initialValues.put("nume", entityLichSuThanhToan.getNume());
        initialValues.put("amountNotTax", entityLichSuThanhToan.getAmountNotTax());
        initialValues.put("amountTax", entityLichSuThanhToan.getAmountTax());
        initialValues.put("multiple", entityLichSuThanhToan.getMultiple());
        initialValues.put("billType", entityLichSuThanhToan.getBillType());
        initialValues.put("typeIndex", entityLichSuThanhToan.getTypeIndex());
        initialValues.put("groupTypeIndex", entityLichSuThanhToan.getGroupTypeIndex());
        initialValues.put("createdDate", entityLichSuThanhToan.getCreatedDate());
        initialValues.put("idChanged", entityLichSuThanhToan.getIdChanged());
        initialValues.put("dateChanged", entityLichSuThanhToan.getDateChanged());
        initialValues.put("edong", entityLichSuThanhToan.getEdong());
        initialValues.put("pcCodeExt", entityLichSuThanhToan.getPcCodeExt());
        initialValues.put("code", entityLichSuThanhToan.getCode());
        initialValues.put("name", entityLichSuThanhToan.getName());
        initialValues.put("nameNosign", entityLichSuThanhToan.getNameNosign());
        initialValues.put("phoneByevn", entityLichSuThanhToan.getPhoneByevn());
        initialValues.put("phoneByecp", entityLichSuThanhToan.getPhoneByecp());
        initialValues.put("electricityMeter", entityLichSuThanhToan.getElectricityMeter());
        initialValues.put("inning", entityLichSuThanhToan.getInning());
        initialValues.put("road", entityLichSuThanhToan.getRoad());
        initialValues.put("station", entityLichSuThanhToan.getStation());
        initialValues.put("taxCode", entityLichSuThanhToan.getTaxCode());
        initialValues.put("trade", entityLichSuThanhToan.getTrade());
        initialValues.put("countPeriod", entityLichSuThanhToan.getCountPeriod());
        initialValues.put("team", entityLichSuThanhToan.getTeam());
        initialValues.put("type", entityLichSuThanhToan.getType());
        initialValues.put("lastQuery", entityLichSuThanhToan.getLastQuery());
        initialValues.put("groupType", entityLichSuThanhToan.getGroupType());
        initialValues.put("billingChannel", entityLichSuThanhToan.getBillingChannel());
        initialValues.put("billingType", entityLichSuThanhToan.getBillingType());
        initialValues.put("billingBy", entityLichSuThanhToan.getBillingBy());
        initialValues.put("cashierPay", entityLichSuThanhToan.getCashierPay());
        initialValues.put("edongKey", entityLichSuThanhToan.getEdong());
        initialValues.put("payments", entityLichSuThanhToan.getPayments());
        initialValues.put("payStatus", entityLichSuThanhToan.getPayStatus());
        initialValues.put("stateOfDebt", entityLichSuThanhToan.getStateOfDebt());
        initialValues.put("stateOfCancel", entityLichSuThanhToan.getStateOfCancel());
        initialValues.put("stateOfReturn", entityLichSuThanhToan.getStateOfReturn());
        initialValues.put("suspectedProcessingStatus", entityLichSuThanhToan.getSuspectedProcessingStatus());
        initialValues.put("stateOfPush", entityLichSuThanhToan.getStateOfPush());
        initialValues.put("dateOfPush", entityLichSuThanhToan.getDateOfPush());
        initialValues.put("countPrintReceipt", entityLichSuThanhToan.getCountPrintReceipt());
        initialValues.put("printInfo", entityLichSuThanhToan.getPrintInfo());
        initialValues.put("dateIncurred", entityLichSuThanhToan.getDateIncurred());
        initialValues.put("tradingCode", entityLichSuThanhToan.getTradingCode());
        int rowAffect = (int) database.insert(TABLE_NAME_HISTORY_PAY, null, initialValues);

        return rowAffect;
    }

    public String getCustomerNameByBillId(String edong, long billId) {
        database = this.getReadableDatabase();
        String query = "SELECT name FROM (SELECT customerCode FROM " + TABLE_NAME_BILL + " WHERE edongKey = '" + edong + "' and billId = " + billId + " ) AS A JOIN " + TABLE_NAME_CUSTOMER + " AS B ON A.customerCode = B.code";
        Cursor c = database.rawQuery(query, null);

        c.moveToFirst();
        if (c.getCount() == 0) {
            return null;
        } else return stringConvertNull(c.getString(c.getColumnIndex("name")));
    }

    public String getCustomerCodeByBillId(String edong, long billId) {
        database = this.getReadableDatabase();
        String query = "SELECT customerCode FROM " + TABLE_NAME_BILL + " WHERE edongKey = '" + edong + "' and billId = " + billId;
        Cursor c = database.rawQuery(query, null);

        c.moveToFirst();
        if (c.getCount() == 0) {
            return null;
        } else return stringConvertNull(c.getString(c.getColumnIndex("customerCode")));
    }

    public int updateBillHistoryWith(String edongKey, int billId, String dateIncurred, Integer tradingCode) {
        ContentValues initialValues = new ContentValues();

        initialValues.put("dateIncurred", dateIncurred);
        initialValues.put("tradingCode", tradingCode);

        database = getWritableDatabase();
        int rowAffect = (int) database.update(TABLE_NAME_HISTORY_PAY, initialValues, " edongKey = ? and billId = ?", new String[]{edongKey, String.valueOf(billId)});
        return rowAffect;
    }

    public int updateBillWithWithThanhToanError(String edongKey, int billId, String edong) {
        ContentValues initialValues = new ContentValues();

        initialValues.put("edong", edong);

        database = getWritableDatabase();
        int rowAffect = (int) database.update(TABLE_NAME_HISTORY_PAY, initialValues, " edongKey = ? and billId = ?", new String[]{edongKey, String.valueOf(billId)});
        return rowAffect;
    }

    public int updateBillHistoryWithThanhToanErrorOrSuccess(String edongKey, int billId, String edong, Integer paymentMode, int payStatus, Integer stateOfDebt, Integer stateOfCancel, Integer stateOfReturn, Integer suspectedProcessingStatus, int countPrintReceipt, Integer statusOfPrintInfo, String dateIncurred, Integer tradingCode) {
        ContentValues initialValues = new ContentValues();

        initialValues.put("edong", edong);
        initialValues.put("payments", paymentMode);
        initialValues.put("payStatus", payStatus);
        initialValues.put("stateOfDebt", stateOfDebt);
        initialValues.put("stateOfCancel", stateOfCancel);
        initialValues.put("stateOfReturn", stateOfReturn);
        initialValues.put("suspectedProcessingStatus", suspectedProcessingStatus);
        initialValues.put("countPrintReceipt", countPrintReceipt);
        initialValues.put("printInfo", statusOfPrintInfo);

        initialValues.put("dateIncurred", dateIncurred);
        initialValues.put("tradingCode", tradingCode);

        database = getWritableDatabase();
        int rowAffect = (int) database.update(TABLE_NAME_HISTORY_PAY, initialValues, " edongKey = ? and billId = ?", new String[]{edongKey, String.valueOf(billId)});
        return rowAffect;
    }

    public int updateBillHistoryWithPrintInfo(String edongKey, int billId, Integer statusOfPrintInfo) {
        ContentValues initialValues = new ContentValues();

        initialValues.put("printInfo", statusOfPrintInfo);

        database = getWritableDatabase();
        int rowAffect = (int) database.update(TABLE_NAME_HISTORY_PAY, initialValues, " edongKey = ? and billId = ?", new String[]{edongKey, String.valueOf(billId)});
        return rowAffect;
    }

    public int updateBillHistoryWithSuspectedProcessingStatus(String edong, int billId, Integer suspectedProcessingStatus) {
        database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("suspectedProcessingStatus", suspectedProcessingStatus);
        return database.update(TABLE_NAME_HISTORY_PAY, contentValues, "edongKey = ? and billId = ? ", new String[]{edong, String.valueOf(billId)});
    }

    //endregion

    //region Account Tài khoản
    public int selectNotYetPushMoney(String edongKey) {
        database = getReadableDatabase();
        String query = "SELECT notYetPushMoney FROM " + TABLE_NAME_ACCOUNT + " where edong = '" + edongKey + "'";
        Cursor c = database.rawQuery(query, null);
        if (c.moveToFirst()) {
            return intConvertNull(c.getInt(0));
        }
        return 0;
    }

    public int updateAccountWith(String edong, int notYetPushMoney) {
        database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("notYetPushMoney", notYetPushMoney);
        return database.update(TABLE_NAME_ACCOUNT, contentValues, "edong = ?", new String[]{String.valueOf(edong)});
    }

    //endregion
}

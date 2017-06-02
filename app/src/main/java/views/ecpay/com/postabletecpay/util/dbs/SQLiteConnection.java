package views.ecpay.com.postabletecpay.util.dbs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import views.ecpay.com.postabletecpay.model.adapter.PayAdapter;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.util.entities.response.EntitySearchOnline.BillInsideCustomer;
import views.ecpay.com.postabletecpay.util.entities.response.EntitySearchOnline.CustomerInsideBody;
import views.ecpay.com.postabletecpay.util.entities.sqlite.Account;
import views.ecpay.com.postabletecpay.util.entities.sqlite.Customer;
import views.ecpay.com.postabletecpay.util.entities.sqlite.EvnPC;

import static android.R.attr.id;
import static android.content.ContentValues.TAG;
import static views.ecpay.com.postabletecpay.util.commons.Common.ONE;
import static views.ecpay.com.postabletecpay.util.commons.Common.PATH_FOLDER_CONFIG;
import static views.ecpay.com.postabletecpay.util.commons.Common.PATH_FOLDER_DB;
import static views.ecpay.com.postabletecpay.util.commons.Common.PATH_FOLDER_ROOT;
import static views.ecpay.com.postabletecpay.util.commons.Common.ZERO;

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
    private String TABLE_NAME_CUSTOMER = "TBL_CUSTOMER";
    private String TABLE_NAME_BILL = "TBL_BILL";
    private String TABLE_NAME_LICH_SU_TTOAN = "TBL_LICH_SU_TTOAN";

    private String CREATE_TABLE_ACCOUNT = "CREATE TABLE `" + TABLE_NAME_ACCOUNT + "` (`edong` TEXT NOT NULL PRIMARY KEY, `name` TEXT, `address` TEXT, `email` TEXT, `birthday` TEXT, `session` TEXT, `balance` NUMERIC, `lockMoney` NUMERIC, `changePIN` INTEGER, `verified` INTEGER, `mac` TEXT, `ip` TEXT, `strLoginTime` TEXT, `strLogoutTime` TEXT, `type` INTEGER, `status` TEXT, `idNumber` TEXT, `idNumberDate` TEXT, `idNumberPlace` TEXT, `parentEdong` TEXT )";

    private String CREATE_TABLE_EVN_PC = "CREATE TABLE `" + TABLE_NAME_EVN_PC + "` ( `pcId` NOT NULL PRIMARY KEY, `parentId` INTEGER, `code` TEXT, `ext` TEXT, `fullName` TEXT, `shortName` TEXT, `address` TEXT, `taxCode` TEXT, `phone1` TEXT, `phone2` TEXT, `fax` TEXT, `level` INTEGER )";

    private String CREATE_TABLE_CUSTOMER = "CREATE TABLE `" + TABLE_NAME_CUSTOMER + "` ( `code` TEXT NOT NULL PRIMARY KEY, `name` TEXT, `address` TEXT, `pcCode` TEXT, `pcCodeExt` TEXT, `phoneByevn` TEXT, `phoneByecp` TEXT, `bookCmis` TEXT, `electricityMeter` TEXT, `inning` TEXT, `status` TEXT, `bankAccount` TEXT, `idNumber` TEXT, `bankName` TEXT , `edongKey` TEXT NOT NULL)";

    private String CREATE_TABLE_BILL = "CREATE TABLE `" + TABLE_NAME_BILL + "` ( `customerCode` TEXT, `customerPayCode` TEXT, `billId` INTEGER NOT NULL PRIMARY KEY, `term` TEXT, `strTerm` TEXT, `amount` INTEGER, `period` TEXT, `issueDate` TEXT, `strIssueDate` TEXT, `status` INTEGER, `seri` TEXT, `pcCode` TEXT, `handoverCode` TEXT, `cashierCode` TEXT, `bookCmis` TEXT, `fromDate` TEXT, `toDate` TEXT, `strFromDate` TEXT, `strToDate` TEXT, `home` TEXT, `tax` REAL, `billNum` TEXT, `currency` TEXT, `priceDetails` TEXT, `numeDetails` TEXT, `amountDetails` TEXT, `oldIndex` TEXT, `newIndex` TEXT, `nume` TEXT, `amountNotTax` INTEGER, `amountTax` INTEGER, `multiple` TEXT, `billType` TEXT, `typeIndex` TEXT, `groupTypeIndex` TEXT, `createdDate` TEXT, `idChanged` INTEGER, `dateChanged` TEXT, `edong` TEXT, `pcCodeExt` TEXT, `code` TEXT, `name` TEXT, `nameNosign` TEXT, `phoneByevn` TEXT, `phoneByecp` TEXT, `electricityMeter` TEXT, `inning` TEXT, `road` TEXT, `station` TEXT, `taxCode` TEXT, `trade` TEXT, `countPeriod` TEXT, `team` TEXT, `type` INTEGER, `lastQuery` TEXT, `groupType` INTEGER, `billingChannel` TEXT, `billingType` TEXT, `billingBy` TEXT, `cashierPay` TEXT, `edongKey` TEXT NOT NULL, `isChecked` INTEGER default 0)";

    private String CREATE_TABLE_LICH_SU_TTOAN = "CREATE TABLE " + TABLE_NAME_LICH_SU_TTOAN + "(ID INTEGER AUTOINCREMENT, " +
            "SERI_HDON INTEGER, " + "MA_KHANG TEXT, " + "MA_THE TEXT, " + "TEN_KHANG TEXT, " + "DIA_CHI TEXT, " + "THANG_TTOAN DATE, " +
            "PHIEN_TTOAN INTEGER, " + "SO_TIEN_TTOAN DOUBLE, " + "SO_GCS TEXT, " + "DIEN_LUC TEXT, " + "SO_HO TEXT, " +
            "SO_DAU_KY TEXT, " + "SO_CUOI_KY TEXT, " + "SO_CTO TEXT, " + "SDT_ECPAY TEXT, " + "SDT_EVN TEXT, " + "GIAO_THU INTEGER, " +
            "NGAY_GIAO_THU DATE, " + "TRANG_THAI_TTOAN TEXT, " + "VI_TTOAN TEXT, " + "HTHUC_TTOAN TEXT, " + "TTHAI_TTOAN TEXT, " +
            "TTHAI_CHAM_NO TEXT, " + "TTHAI_HUY TEXT, " + "TTHAI_XLY_NGHI_NGO TEXT, " + "SO_LAN_IN_BNHAN INTEGER, " + "IN_TBAO_DIEN TEXT, " +
            "NGAY_PSINH DATE, " + "MA_GIAO_DICH TEXT)";

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
            db.execSQL(CREATE_TABLE_BILL);
            db.execSQL(CREATE_TABLE_CUSTOMER);
            db.execSQL(CREATE_TABLE_LICH_SU_TTOAN);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_ACCOUNT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_EVN_PC);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_BILL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_CUSTOMER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_LICH_SU_TTOAN);
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
        int id = (int) database.insertWithOnConflict(TABLE_NAME_ACCOUNT, null, initialValues, SQLiteDatabase.CONFLICT_IGNORE);
        if (id == ERROR_OCCUR) {
            database.update(TABLE_NAME_ACCOUNT, initialValues, "edong=?", new String[]{account.getEdong()});  // number 1 is the _id here, update to variable for your code
        }
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

    private String stringConvertNull(String object) {
        return object = (object == null) ? Common.TEXT_EMPTY : object;
    }

    private long longConvertNull(Long object) {
        return object = (object == null) ? 0 : object.longValue();
    }

    private int intConvertNull(Integer object) {
        return object = (object == null) ? 0 : object.intValue();
    }

    public void insertOrUpdateEvnPc(EvnPC evnPC) {
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

    public int countBill(String edong) {
        if (edong == null)
            return 0;

        database = this.getReadableDatabase();

        String query = "SELECT COUNT(*) FROM " + TABLE_NAME_BILL + " WHERE edongKey = '" + edong + "'";
        Cursor mCursor = database.rawQuery(query, null);
        int count = mCursor.getCount();

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

    public List<Customer> selectAllCustomer(String edong) {
        return this.selectAllCustomerFitterBy(edong, Common.TYPE_SEARCH.ALL, Common.TEXT_EMPTY);
    }

    public boolean checkStatusPayedOfCustormer(String edong, String code) {
        String query = "SELECT status FROM " + TABLE_NAME_BILL + " WHERE edongKey = '" + edong + "' and customerCode ='" + code + "' and status = 0";
        boolean isPayed = false;
        database = this.getReadableDatabase();
        Cursor mCursor = database.rawQuery(query, null);
        isPayed = (mCursor.getCount() == 0) ? false : true;

        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }
        return isPayed;
    }

    public List<Customer> selectAllCustomerFitterBy(String edong, Common.TYPE_SEARCH typeSearch, String infoSearch) {
        String query = null;
        switch (typeSearch.getPosition()) {
            case 0:
                query = "SELECT * FROM " + TABLE_NAME_CUSTOMER + " WHERE edongKey = '" + edong + "'";
                break;
            case 1:
                query = "SELECT * FROM " + TABLE_NAME_CUSTOMER + " WHERE edongKey = '" + edong + "' and code ='" + infoSearch + "'";
                break;
            case 2:
                query = "SELECT * FROM " + TABLE_NAME_CUSTOMER + " WHERE edongKey = '" + edong + "' and name ='" + infoSearch + "'";
                break;
            case 3:
                query = "SELECT * FROM " + TABLE_NAME_CUSTOMER + " WHERE edongKey = '" + edong + "' and phoneByevn ='" + infoSearch + "'";
                break;
            case 4:
                query = "SELECT * FROM " + TABLE_NAME_CUSTOMER + " WHERE edongKey = '" + edong + "' and name ='" + infoSearch + "'";
                break;
            case 5:
                query = "SELECT * FROM " + TABLE_NAME_CUSTOMER + " WHERE edongKey = '" + edong + "' and bookCmis ='" + infoSearch + "'";
                break;
            case 6:
                query = "SELECT * FROM " + TABLE_NAME_CUSTOMER + " WHERE edongKey = '" + edong + "' and road ='" + infoSearch + "'";
                break;
        }

        List<Customer> customerList = new ArrayList<>();
        database = this.getWritableDatabase();
        Cursor mCursor = database.rawQuery(query, null);

        if (mCursor != null && mCursor.moveToFirst()) {
            int count = mCursor.getCount();
            do {
                String code = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("code")));
                String name = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("name")));
                String address = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("address")));
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
                boolean isChecked = intConvertNull(mCursor.getInt(mCursor.getColumnIndex("isChecked"))) == 0 ? false : true;

                Customer customer = new Customer(code, name, address, pcCode, pcCodeExt, phoneByevn, phoneByecp, bookCmis, electricityMeter, inning, status, bankAccount, idNumber, bankName, edongKey, isChecked);
                customerList.add(customer);
            }
            while (mCursor.moveToNext());

            mCursor.close();
        }
        return customerList;
    }

    public List<PayAdapter.BillEntityAdapter> selectInfoBillOfCustomer(String edong, String code) {
        List<PayAdapter.BillEntityAdapter> billList = new ArrayList<>();

        database = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_BILL + " WHERE edongKey = '" + edong + "' and customerCode ='" + code + "'";
        Cursor mCursor = database.rawQuery(query, null);

        mCursor.moveToFirst();

        do {
            int billId = intConvertNull(mCursor.getInt(mCursor.getColumnIndex("billId")));
            int amount = intConvertNull(mCursor.getInt(mCursor.getColumnIndex("amount")));
            int status = intConvertNull(mCursor.getInt(mCursor.getColumnIndex("status")));
            String customerPayCode = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("customerPayCode")));
            String billingBy = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("billingBy")));

            String term = stringConvertNull(mCursor.getString(mCursor.getColumnIndex("term")));
            term = Common.convertDateToDate(term, Common.DATE_TIME_TYPE.yyyymmdd, Common.DATE_TIME_TYPE.mmyyyy);


            PayAdapter.BillEntityAdapter bill = new PayAdapter.BillEntityAdapter();
            bill.setBillId(billId);
            bill.setBillingBy(billingBy);
            bill.setCustomerPayCode(customerPayCode);
            bill.setMoneyBill(amount);
            bill.setMonthBill(term);
            bill.setPayed(status == 0 ? false : true);
            bill.setChecked(false);
            if (billingBy.equals(edong) || bill.isPayed())
                bill.setPrint(true);
            else bill.setPrint(false);

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
        String query = "SELECT SUM(amount) FROM " + TABLE_NAME_BILL + " WHERE edongKey = '" + edong + "'";
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

    public int insertNotUpdateCustomer(String edong, CustomerInsideBody customer) {
        if (edong == null || edong.trim().isEmpty() || customer == null)
            return ERROR_OCCUR;

        ContentValues initialValues = new ContentValues();

        initialValues.put("code", customer.getCode());
        initialValues.put("name", customer.getName());
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
        initialValues.put("term", billInsideCustomer.getTerm());
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
        int rowAffect = (int) database.update(TABLE_NAME_BILL, initialValues, "edong = ? and customerCode = ? and billId = ?", new String[]{edong, code, String.valueOf(billId)});
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

    //endregion
}

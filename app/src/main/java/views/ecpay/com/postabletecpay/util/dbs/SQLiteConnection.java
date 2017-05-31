package views.ecpay.com.postabletecpay.util.dbs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import views.ecpay.com.postabletecpay.model.adapter.PayAdapter;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.util.entities.response.EntitySearchOnline.BillInsideCustomer;
import views.ecpay.com.postabletecpay.util.entities.response.EntitySearchOnline.CustomerInsideBody;
import views.ecpay.com.postabletecpay.util.entities.response.EntitySearchOnline.CustomerResponse;
import views.ecpay.com.postabletecpay.util.entities.sqlite.Account;
import views.ecpay.com.postabletecpay.util.entities.sqlite.Customer;
import views.ecpay.com.postabletecpay.util.entities.sqlite.EvnPC;

import static views.ecpay.com.postabletecpay.util.commons.Common.PATH_FOLDER_CONFIG;
import static views.ecpay.com.postabletecpay.util.commons.Common.PATH_FOLDER_DB;
import static views.ecpay.com.postabletecpay.util.commons.Common.PATH_FOLDER_ROOT;

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

    private String CREATE_TABLE_CUSTOMER = "CREATE TABLE `" + TABLE_NAME_CUSTOMER + "` ( `code` TEXT NOT NULL PRIMARY KEY, `name` TEXT, `address` TEXT, `pcCode` TEXT, `pcCodeExt` TEXT, `phoneByevn` TEXT, `phoneByecp` TEXT, `bookCmis` TEXT, `electricityMeter` TEXT, `inning` TEXT, `status` TEXT, `bankAccount` TEXT, `idNumber` TEXT, `bankName` TEXT )";

    private String CREATE_TABLE_BILL = "CREATE TABLE `" + TABLE_NAME_BILL + "` ( `customerCode` TEXT, `customerPayCode` TEXT, `billId` INTEGER NOT NULL PRIMARY KEY, `term` TEXT, `strTerm` TEXT, `amount` INTEGER, `period` TEXT, `issueDate` TEXT, `strIssueDate` TEXT, `status` INTEGER, `seri` TEXT, `pcCode` TEXT, `handoverCode` TEXT, `cashierCode` TEXT, `bookCmis` TEXT, `fromDate` TEXT, `toDate` TEXT, `strFromDate` TEXT, `strToDate` TEXT, `home` TEXT, `tax` REAL, `billNum` TEXT, `currency` TEXT, `priceDetails` TEXT, `numeDetails` TEXT, `amountDetails` TEXT, `oldIndex` TEXT, `newIndex` TEXT, `nume` TEXT, `amountNotTax` INTEGER, `amountTax` INTEGER, `multiple` TEXT, `billType` TEXT, `typeIndex` TEXT, `groupTypeIndex` TEXT, `createdDate` TEXT, `idChanged` INTEGER, `dateChanged` TEXT, `edong` TEXT, `pcCodeExt` TEXT, `code` TEXT, `name` TEXT, `nameNosign` TEXT, `phoneByevn` TEXT, `phoneByecp` TEXT, `electricityMeter` TEXT, `inning` TEXT, `road` TEXT, `station` TEXT, `taxCode` TEXT, `trade` TEXT, `countPeriod` TEXT, `team` TEXT, `type` INTEGER, `lastQuery` TEXT, `groupType` INTEGER, `billingChannel` TEXT, `billingType` TEXT, `billingBy` TEXT, `cashierPay` TEXT )";

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
                    mCursor.getString(mCursor.getColumnIndexOrThrow("edong")),
                    mCursor.getString(mCursor.getColumnIndexOrThrow("name")),
                    mCursor.getString(mCursor.getColumnIndexOrThrow("address")),
                    mCursor.getString(mCursor.getColumnIndexOrThrow("email")),
                    mCursor.getString(mCursor.getColumnIndexOrThrow("birthday")),
                    mCursor.getString(mCursor.getColumnIndexOrThrow("session")),
                    mCursor.getLong(mCursor.getColumnIndexOrThrow("balance")),
                    mCursor.getInt(mCursor.getColumnIndexOrThrow("lockMoney")),
                    (mCursor.getInt(mCursor.getColumnIndexOrThrow("changePIN")) == 1) ? true : false,
                    mCursor.getInt(mCursor.getColumnIndexOrThrow("verified")),
                    mCursor.getString(mCursor.getColumnIndexOrThrow("mac")),
                    mCursor.getString(mCursor.getColumnIndexOrThrow("ip")),
                    mCursor.getString(mCursor.getColumnIndexOrThrow("strLoginTime")),
                    mCursor.getString(mCursor.getColumnIndexOrThrow("strLogoutTime")),
                    mCursor.getInt(mCursor.getColumnIndexOrThrow("type")),
                    mCursor.getString(mCursor.getColumnIndexOrThrow("status")),
                    mCursor.getString(mCursor.getColumnIndexOrThrow("idNumber")),
                    mCursor.getString(mCursor.getColumnIndexOrThrow("idNumberDate")),
                    mCursor.getString(mCursor.getColumnIndexOrThrow("idNumberPlace")),
                    mCursor.getString(mCursor.getColumnIndexOrThrow("parentEdong"))
            );
        }

        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }
        return account;
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

        String query = "SELECT COUNT(*) FROM " + TABLE_NAME_BILL + " WHERE edong = '" + edong + "'";
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
        String query = "SELECT SUM(amount) FROM " + TABLE_NAME_BILL + " WHERE edong = '" + edong + "' and customerCode ='" + customerCode + "'";
        Cursor mCursor = database.rawQuery(query, null);
        int count = mCursor.getCount();

        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }
        return count;
    }

    /* public List<PayAdapter.PayEntityAdapter> selectAllBill(String edong) throws SQLiteException {
         if (edong == null)
             return null;

         List<PayAdapter.PayEntityAdapter> adapterList = new ArrayList<>();

         database = this.getReadableDatabase();
         String query = "SELECT * FROM " + TABLE_NAME_BILL + " WHERE edong = '" + edong + "'";
         Cursor mCursor = database.rawQuery(query, null);

         mCursor.moveToFirst();

         while (mCursor.moveToNext()) {
             String billID = mCursor.getString(mCursor.getColumnIndexOrThrow("billId"));
             String tenKH = mCursor.getString(mCursor.getColumnIndexOrThrow("name"));
             String maKH = mCursor.getString(mCursor.getColumnIndexOrThrow("code"));
             String loTrinh = mCursor.getString(mCursor.getColumnIndexOrThrow("road"));
             int tongTien = mCursor.getInt(mCursor.getColumnIndexOrThrow("amount"));
             String diaChi = mCursor.getString(mCursor.getColumnIndexOrThrow("address"));
             int trangThaiNo = mCursor.getInt(mCursor.getColumnIndexOrThrow("status"));

             PayAdapter.PayEntityAdapter entityAdapter = new PayAdapter.PayEntityAdapter(billID, tenKH, diaChi, loTrinh, maKH, tongTien, trangThaiNo);
             adapterList.add(entityAdapter);
         }

         if (mCursor != null && !mCursor.isClosed()) {
             mCursor.close();
         }
         return adapterList;
     }
 */
    /*public List<PayAdapter.PayEntityAdapter> selectAllBillBy(String edong, Common.TYPE_SEARCH typeSearch, String infoSearch) {

        String query = null;
        switch (typeSearch.getPosition()) {
            case 1:
                query = "SELECT * FROM " + TABLE_NAME_BILL + " WHERE edong = '" + edong + "' and customerPayCode ='" + infoSearch + "' or customerCode = '" + infoSearch + "'";
                break;
            case 2:
                query = "SELECT * FROM " + TABLE_NAME_BILL + " WHERE edong = '" + edong + "' and name ='" + infoSearch + "'";
                break;
            case 3:
                query = "SELECT * FROM " + TABLE_NAME_BILL + " WHERE edong = '" + edong + "' and phoneByevn ='" + infoSearch + "'";
                break;
            case 4:
                query = "SELECT * FROM " + TABLE_NAME_BILL + " WHERE edong = '" + edong + "' and name ='" + infoSearch + "'";
                break;
            case 5:
                query = "SELECT * FROM " + TABLE_NAME_BILL + " WHERE edong = '" + edong + "' and bookCmis ='" + infoSearch + "'";
                break;
            case 6:
                query = "SELECT * FROM " + TABLE_NAME_BILL + " WHERE edong = '" + edong + "' and road ='" + infoSearch + "'";
                break;
        }


        List<PayAdapter.PayEntityAdapter> adapterList = new ArrayList<>();
        database = this.getReadableDatabase();
        Cursor mCursor = database.rawQuery(query, null);
        mCursor.moveToFirst();

        while (mCursor.moveToNext()) {
            String billID = mCursor.getString(mCursor.getColumnIndexOrThrow("billId"));
            String tenKH = mCursor.getString(mCursor.getColumnIndexOrThrow("name"));
            String maKH = mCursor.getString(mCursor.getColumnIndexOrThrow("code"));
            String loTrinh = mCursor.getString(mCursor.getColumnIndexOrThrow("road"));
            int tongTien = mCursor.getInt(mCursor.getColumnIndexOrThrow("amount"));
            String diaChi = mCursor.getString(mCursor.getColumnIndexOrThrow("address"));
            int trangThaiNo = mCursor.getInt(mCursor.getColumnIndexOrThrow("status"));

            PayAdapter.PayEntityAdapter entityAdapter = new PayAdapter.PayEntityAdapter(billID, tenKH, diaChi, loTrinh, maKH, tongTien, trangThaiNo);
            adapterList.add(entityAdapter);
        }

        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }
        return adapterList;
    }
*/
    public List<Customer> selectAllCustomer(String edong) {
        return null;
    }

    public String selectRoadFirstInBill(String edong, String code) {
        String query = "SELECT TOP 1 FROM " + TABLE_NAME_BILL + " WHERE edong = '" + edong + "' and customerCode ='" + code + "' and road is not null and road <> ''";
        String road = "";
        database = this.getReadableDatabase();
        Cursor mCursor = database.rawQuery(query, null);
        mCursor.moveToFirst();
        while (mCursor.moveToNext()) {
            road = mCursor.getString(mCursor.getColumnIndex("road"));
        }
        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }
        return road;
    }

    public boolean checkStatusPayedOfCustormer(String edong, String code) {
        String query = "SELECT status FROM " + TABLE_NAME_BILL + " WHERE edong = '" + edong + "' and customerCode ='" + code + "' and status = 0";
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
            case 1:
                query = "SELECT * FROM " + TABLE_NAME_BILL + " WHERE edong = '" + edong + "' and customerPayCode ='" + infoSearch + "' or customerCode = '" + infoSearch + "'";
                break;
            case 2:
                query = "SELECT * FROM " + TABLE_NAME_BILL + " WHERE edong = '" + edong + "' and name ='" + infoSearch + "'";
                break;
            case 3:
                query = "SELECT * FROM " + TABLE_NAME_BILL + " WHERE edong = '" + edong + "' and phoneByevn ='" + infoSearch + "'";
                break;
            case 4:
                query = "SELECT * FROM " + TABLE_NAME_BILL + " WHERE edong = '" + edong + "' and name ='" + infoSearch + "'";
                break;
            case 5:
                query = "SELECT * FROM " + TABLE_NAME_BILL + " WHERE edong = '" + edong + "' and bookCmis ='" + infoSearch + "'";
                break;
            case 6:
                query = "SELECT * FROM " + TABLE_NAME_BILL + " WHERE edong = '" + edong + "' and road ='" + infoSearch + "'";
                break;
        }

        List<Customer> customerList = new ArrayList<>();
        database = this.getReadableDatabase();
        Cursor mCursor = database.rawQuery(query, null);
        mCursor.moveToFirst();

        while (mCursor.moveToNext()) {
            String code = mCursor.getString(mCursor.getColumnIndexOrThrow("code"));
            String name = mCursor.getString(mCursor.getColumnIndexOrThrow("name"));
            String address = mCursor.getString(mCursor.getColumnIndexOrThrow("address"));
            String pcCode = mCursor.getString(mCursor.getColumnIndexOrThrow("pcCode"));
            String pcCodeExt = mCursor.getString(mCursor.getColumnIndexOrThrow("pcCodeExt"));
            String phoneByevn = mCursor.getString(mCursor.getColumnIndexOrThrow("phoneByevn"));
            String phoneByecp = mCursor.getString(mCursor.getColumnIndexOrThrow("phoneByecp"));
            String bookCmis = mCursor.getString(mCursor.getColumnIndexOrThrow("bookCmis"));
            String electricityMeter = mCursor.getString(mCursor.getColumnIndexOrThrow("electricityMeter"));
            String inning = mCursor.getString(mCursor.getColumnIndexOrThrow("inning"));
            String status = mCursor.getString(mCursor.getColumnIndexOrThrow("status"));
            String bankAccount = mCursor.getString(mCursor.getColumnIndexOrThrow("bankAccount"));
            String idNumber = mCursor.getString(mCursor.getColumnIndexOrThrow("idNumber"));
            String bankName = mCursor.getString(mCursor.getColumnIndexOrThrow("bankName"));

            Customer customer = new Customer(code, name, address, pcCode, pcCodeExt, phoneByevn, phoneByecp, bookCmis, electricityMeter, inning, status, bankAccount, idNumber, bankName);
            customerList.add(customer);
        }

        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }
        return customerList;
    }

    public List<PayAdapter.BillEntityAdapter> selectInfoBillOfCustomer(String edong, String code) {
        List<PayAdapter.BillEntityAdapter> billList = new ArrayList<>();

        database = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_BILL + " WHERE edong = '" + edong + "' and customerCode ='" + code + "'";
        Cursor mCursor = database.rawQuery(query, null);

        mCursor.moveToFirst();
        while (mCursor.moveToNext()) {
            String customerPayCode = mCursor.getString(mCursor.getColumnIndexOrThrow("customerPayCode"));
            int billId = mCursor.getInt(mCursor.getColumnIndexOrThrow("billId"));
            String term = mCursor.getString(mCursor.getColumnIndexOrThrow("term"));
            int amount = mCursor.getInt(mCursor.getColumnIndexOrThrow("amount"));
            int status = mCursor.getInt(mCursor.getColumnIndexOrThrow("status"));
            String billingBy = mCursor.getString(mCursor.getColumnIndexOrThrow("billingBy"));

            PayAdapter.BillEntityAdapter bill = new PayAdapter.BillEntityAdapter();
            bill.setBillId(billId);
            bill.setBillingBy(billingBy);
            bill.setCustomerPayCode(customerPayCode);

            bill.setMoneyBill(amount);
            bill.setMonthBill(term);
            bill.setPayed(status == 0 ? false : true);
            bill.setChecked(!bill.isPayed());
            if (billingBy.equals(edong) || bill.isPayed())
                bill.setPrint(true);
            else bill.setPrint(false);

            billList.add(bill);
        }

        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }
        return billList;
    }

    public int countMoneyAllBill(String edong) {
        database = this.getReadableDatabase();
        String query = "SELECT SUM(amount) FROM " + TABLE_NAME_BILL + " WHERE edong = '" + edong + "'";
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

    public int insertNotUpdateCustomer(CustomerResponse customerResponse) {
        if (customerResponse == null)
            return ERROR_OCCUR;
        CustomerInsideBody customer = customerResponse.getCustomer();
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

        database = getWritableDatabase();
        int rowAffect = (int) database.insert(TABLE_NAME_CUSTOMER, null, initialValues);
        return rowAffect;
    }

    public int insertNotUpdateBillOfCustomer(BillInsideCustomer billInsideCustomer) {
        if (billInsideCustomer == null)
            return SQLiteConnection.ERROR_OCCUR;

        ContentValues initialValues = new ContentValues();
        initialValues.put("customerCode", billInsideCustomer.getCustomerCode());
        initialValues.put("customerPayCode", billInsideCustomer.getCustomerCode());
        initialValues.put("billId", billInsideCustomer.getCustomerCode());
        initialValues.put("term", billInsideCustomer.getCustomerCode());
        initialValues.put("amount", billInsideCustomer.getCustomerCode());
        initialValues.put("period", billInsideCustomer.getCustomerCode());
        initialValues.put("issueDate", billInsideCustomer.getCustomerCode());
        initialValues.put("strIssueDate", billInsideCustomer.getCustomerCode());
        initialValues.put("status", billInsideCustomer.getCustomerCode());
        initialValues.put("seri", billInsideCustomer.getCustomerCode());
        initialValues.put("pcCode", billInsideCustomer.getCustomerCode());
        initialValues.put("handoverCode", billInsideCustomer.getCustomerCode());
        initialValues.put("cashierCode", billInsideCustomer.getCustomerCode());
        initialValues.put("bookCmis", billInsideCustomer.getCustomerCode());
        initialValues.put("fromDate", billInsideCustomer.getCustomerCode());
        initialValues.put("toDate", billInsideCustomer.getCustomerCode());
        initialValues.put("strFromDate", billInsideCustomer.getCustomerCode());
        initialValues.put("strToDate", billInsideCustomer.getCustomerCode());
        initialValues.put("home", billInsideCustomer.getCustomerCode());
        initialValues.put("tax", billInsideCustomer.getCustomerCode());
        initialValues.put("billNum", billInsideCustomer.getCustomerCode());
        initialValues.put("currency", billInsideCustomer.getCustomerCode());
        initialValues.put("priceDetails", billInsideCustomer.getCustomerCode());
        initialValues.put("numeDetails", billInsideCustomer.getCustomerCode());
        initialValues.put("amountDetails", billInsideCustomer.getCustomerCode());
        initialValues.put("oldIndex", billInsideCustomer.getCustomerCode());
        initialValues.put("newIndex", billInsideCustomer.getCustomerCode());
        initialValues.put("nume", billInsideCustomer.getCustomerCode());
        initialValues.put("amountNotTax", billInsideCustomer.getCustomerCode());
        initialValues.put("amountTax", billInsideCustomer.getCustomerCode());
        initialValues.put("multiple", billInsideCustomer.getCustomerCode());
        initialValues.put("billType", billInsideCustomer.getCustomerCode());
        initialValues.put("typeIndex", billInsideCustomer.getCustomerCode());
        initialValues.put("groupTypeIndex", billInsideCustomer.getCustomerCode());
        initialValues.put("createdDate", billInsideCustomer.getCustomerCode());
        initialValues.put("idChanged", billInsideCustomer.getCustomerCode());
        initialValues.put("dateChanged", billInsideCustomer.getCustomerCode());
        initialValues.put("edong", billInsideCustomer.getCustomerCode());
        initialValues.put("pcCodeExt", billInsideCustomer.getCustomerCode());
        initialValues.put("code", billInsideCustomer.getCustomerCode());
        initialValues.put("name", billInsideCustomer.getCustomerCode());
        initialValues.put("nameNosign", billInsideCustomer.getCustomerCode());
        initialValues.put("phoneByevn", billInsideCustomer.getCustomerCode());
        initialValues.put("phoneByecp", billInsideCustomer.getCustomerCode());
        initialValues.put("electricityMeter", billInsideCustomer.getCustomerCode());
        initialValues.put("inning", billInsideCustomer.getCustomerCode());
        initialValues.put("road", billInsideCustomer.getCustomerCode());
        initialValues.put("station", billInsideCustomer.getCustomerCode());
        initialValues.put("taxCode", billInsideCustomer.getCustomerCode());
        initialValues.put("trade", billInsideCustomer.getCustomerCode());
        initialValues.put("countPeriod", billInsideCustomer.getCustomerCode());
        initialValues.put("team", billInsideCustomer.getCustomerCode());
        initialValues.put("type", billInsideCustomer.getCustomerCode());
        initialValues.put("lastQuery", billInsideCustomer.getCustomerCode());
        initialValues.put("groupType", billInsideCustomer.getCustomerCode());
        initialValues.put("billingChannel", billInsideCustomer.getCustomerCode());
        initialValues.put("billingType", billInsideCustomer.getCustomerCode());
        initialValues.put("billingBy", billInsideCustomer.getCustomerCode());
        initialValues.put("cashierPay", billInsideCustomer.getCustomerCode());

        database = getWritableDatabase();
        int rowAffect = (int) database.insert(TABLE_NAME_BILL, null, initialValues);
        return rowAffect;
    }

    //endregion
}

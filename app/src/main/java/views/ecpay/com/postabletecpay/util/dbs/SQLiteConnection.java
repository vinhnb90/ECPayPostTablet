package views.ecpay.com.postabletecpay.util.dbs;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;

import views.ecpay.com.postabletecpay.util.commons.Common;

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

    private String TABLE_NAME_ACCOUNT = "TBL_ACCOUNT";
    private String TABLE_NAME_EVN_PC = "TBL_EVN_PC";
    private String TABLE_NAME_CUSTOMER = "TBL_CUSTOMER";
    private String TABLE_NAME_BILL = "TBL_BILL";
    private String TABLE_NAME_LICH_SU_TTOAN = "TBL_LICH_SU_TTOAN";

    private String CREATE_TABLE_ACCOUNT = "CREATE TABLE `" + TABLE_NAME_ACCOUNT + "` ( `id_account` INTEGER PRIMARY KEY AUTOINCREMENT, `edong` TEXT, `name` TEXT, `address` TEXT, `email` TEXT, `birthday` TEXT, `session` TEXT, `balance` NUMERIC, `lockMoney` NUMERIC, `changePIN` INTEGER, `verified` INTEGER, `mac` TEXT, `ip` TEXT, `strLoginTime` TEXT, `strLogoutTime` TEXT, `type` INTEGER, `status` TEXT, `idNumber` TEXT, `idNumberDate` TEXT, `idNumberPlace` TEXT, `parentEdong` TEXT )";

    private String CREATE_TABLE_EVN_PC = "CREATE TABLE `" + TABLE_NAME_EVN_PC + "` ( `id_evn_pc` INTEGER PRIMARY KEY AUTOINCREMENT, `pcId` INTEGER, `parentId` INTEGER, `code` TEXT, `ext` TEXT, `fullName` TEXT, `shortName` TEXT, `address` TEXT, `taxCode` TEXT, `phone1` TEXT, `phone2` TEXT, `fax` TEXT, `level` INTEGER )";

    private String CREATE_TABLE_CUSTOMER = "CREATE TABLE `" + TABLE_NAME_CUSTOMER + "` ( `id_custormer` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `code` TEXT, `name` TEXT, `address` TEXT, `pcCode` TEXT, `pcCodeExt` TEXT, `phoneByevn` TEXT, `phoneByecp` TEXT, `bookCmis` TEXT, `electricityMeter` TEXT, `inning` TEXT, `status` TEXT, `bankAccount` TEXT, `idNumber` TEXT, `bankName` TEXT )";

    private String CREATE_TABLE_BILL = "CREATE TABLE `" + TABLE_NAME_BILL + "` ( `id_bill` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `customerCode` TEXT, `customerPayCode` TEXT, `billId` TEXT, `term` TEXT, `strTerm` TEXT, `amount` INTEGER, `period` TEXT, `issueDate` TEXT, `strIssueDate` TEXT, `status` INTEGER, `seri` TEXT, `pcCode` TEXT, `handoverCode` TEXT, `cashierCode` TEXT, `bookCmis` TEXT, `fromDate` TEXT, `toDate` TEXT, `strFromDate` TEXT, `strToDate` TEXT, `home` TEXT, `tax` REAL, `billNum` TEXT, `currency` TEXT, `priceDetails` TEXT, `numeDetails` TEXT, `amountDetails` TEXT, `oldIndex` TEXT, `newIndex` TEXT, `nume` TEXT, `amountNotTax` INTEGER, `amountTax` INTEGER, `multiple` TEXT, `billType` TEXT, `typeIndex` TEXT, `groupTypeIndex` TEXT, `createdDate` TEXT, `idChanged` INTEGER, `dateChanged` TEXT, `edong` TEXT, `pcCodeExt` TEXT, `code` TEXT, `name` TEXT, `nameNosign` TEXT, `phoneByevn` TEXT, `phoneByecp` TEXT, `electricityMeter` TEXT, `inning` TEXT, `road` TEXT, `station` TEXT, `taxCode` TEXT, `trade` TEXT, `countPeriod` TEXT, `team` TEXT, `type` INTEGER, `lastQuery` TEXT, `groupType` INTEGER, `billingChannel` TEXT, `billingType` TEXT, `billingBy` TEXT, `cashierPay` TEXT )";

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

    
}

package views.ecpay.com.postabletecpay.util.dbs;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import views.ecpay.com.postabletecpay.util.commons.Common;

/**
 * Created by TungNV on 5/5/17.
 */

public class SQLiteConnection extends SQLiteOpenHelper {
    private static SQLiteConnection instance;
    private SQLiteDatabase database;

    private static String databaseName = "PosTablet.s3db";

    private static int DATABASE_VERSION = 1;

    private String TABLE_NAME_HOA_DON = "TBL_HOA_DON";
    private String TABLE_NAME_KHACH_HANG = "TBL_KHACH_HANG";
    private String TABLE_NAME_LICH_SU_TTOAN = "TBL_LICH_SU_TTOAN";
    private String TABLE_NAME_DIEN_LUC = "TBL_DIEN_LUC";

    private String CREATE_TABLE_HOA_DON = "CREATE TABLE " + TABLE_NAME_HOA_DON + "(ID INTEGER AUTOINCREMENT, " +
            "SERI_HDON INTEGER, " + "MA_KHANG TEXT, " + "MA_THE TEXT, " + "TEN_KHANG TEXT, " + "DIA_CHI TEXT, " + "THANG_TTOAN DATE, " +
            "PHIEN_TTOAN INTEGER, " + "SO_TIEN_TTOAN DOUBLE, " + "SO_GCS TEXT, " + "DIEN_LUC TEXT, " + "SO_HO TEXT, " +
            "SO_DAU_KY TEXT, " + "SO_CUOI_KY TEXT, " + "SO_CTO TEXT, " + "SDT_ECPAY TEXT, " + "SDT_EVN TEXT, " + "GIAO_THU INTEGER, " +
            "NGAY_GIAO_THU DATE, " + "TRANG_THAI_TTOAN TEXT, " + "VI_TTOAN TEXT)";

    private String CREATE_TABLE_KHACH_HANG = "CREATE TABLE " + TABLE_NAME_KHACH_HANG + "(ID INTEGER AUTOINCREMENT, " +
            "MA_KHANG TEXT, " + "MA_THE TEXT, " + "TEN_KHANG TEXT, " + "DIA_CHI TEXT, " + "PHIEN_TTOAN INTEGER, " + "LO_TRINH TEXT, " +
            "SO_GCS TEXT, " + "DIEN_LUC TEXT, " + "SO_HO TEXT, " + "SDT_ECPAY TEXT, " + "SDT_EVN TEXT, " + "NGAY_GIAO_THU DATE)";

    private String CREATE_TABLE_LICH_SU_TTOAN = "CREATE TABLE " + TABLE_NAME_LICH_SU_TTOAN + "(ID INTEGER AUTOINCREMENT, " +
            "SERI_HDON INTEGER, " + "MA_KHANG TEXT, " + "MA_THE TEXT, " + "TEN_KHANG TEXT, " + "DIA_CHI TEXT, " + "THANG_TTOAN DATE, " +
            "PHIEN_TTOAN INTEGER, " + "SO_TIEN_TTOAN DOUBLE, " + "SO_GCS TEXT, " + "DIEN_LUC TEXT, " + "SO_HO TEXT, " +
            "SO_DAU_KY TEXT, " + "SO_CUOI_KY TEXT, " + "SO_CTO TEXT, " + "SDT_ECPAY TEXT, " + "SDT_EVN TEXT, " + "GIAO_THU INTEGER, " +
            "NGAY_GIAO_THU DATE, " + "TRANG_THAI_TTOAN TEXT, " + "VI_TTOAN TEXT, " + "HTHUC_TTOAN TEXT, " + "TTHAI_TTOAN TEXT, " +
            "TTHAI_CHAM_NO TEXT, " + "TTHAI_HUY TEXT, " + "TTHAI_XLY_NGHI_NGO TEXT, " + "SO_LAN_IN_BNHAN INTEGER, " + "IN_TBAO_DIEN TEXT, " +
            "NGAY_PSINH DATE, " + "MA_GIAO_DICH TEXT)";

    private String CREATE_TABLE_DIEN_LUC = "CREATE TABLE " + TABLE_NAME_DIEN_LUC + "(ID INTEGER AUTOINCREMENT, " +
            "MA_DLUC TEXT, " + "TEN_VIET_TAT TEXT, " + "TEN_DAY_DU TEXT, " + "DIA_CHI TEXT, " + "SDT TEXT, " + "SDT_SUACHUA TEXT, " +
            "MA_NHA_CCAP TEXT, " + "NGAY_DBO TEXT)";

    private SQLiteConnection(Context context) {
        super(context, Common.PATH_FOLDER_DB + databaseName, null, DATABASE_VERSION);
        SQLiteDatabase.openOrCreateDatabase(Common.PATH_FOLDER_DB + databaseName, null);
    }

    public static synchronized SQLiteConnection getInstance(Context context) {
        if (instance == null)
            instance = new SQLiteConnection(context.getApplicationContext());
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_DIEN_LUC);
        db.execSQL(CREATE_TABLE_HOA_DON);
        db.execSQL(CREATE_TABLE_KHACH_HANG);
        db.execSQL(CREATE_TABLE_LICH_SU_TTOAN);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_DIEN_LUC);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_HOA_DON);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_KHACH_HANG);
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

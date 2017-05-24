package views.ecpay.com.postabletecpay.model;

import android.content.Context;

import views.ecpay.com.postabletecpay.model.sharedPreference.ICommonSharedReference;
import views.ecpay.com.postabletecpay.model.sharedPreference.SharePrefManager;
import views.ecpay.com.postabletecpay.util.dbs.SQLiteConnection;
import views.ecpay.com.postabletecpay.util.entities.sqlite.Account;
import views.ecpay.com.postabletecpay.util.entities.sqlite.EvnPC;

/**
 * Created by VinhNB on 5/15/2017.
 */

public class LoginModel extends CommonModel {
    public LoginModel(Context context) {
        super(context);
    }

    //region call SQLite
    public void writeSqliteAccountTable(Account account) {
        if (account == null)
            return;

        sqLiteConnection.insertOrUpdateAccount(account);
    }

    public void writeSqliteEvnPcTable(EvnPC evnPC) {
        if (evnPC == null)
            return;
        sqLiteConnection.insertOrUpdateEvnPc(evnPC);
    }
    //endregion
}

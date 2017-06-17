package views.ecpay.com.postabletecpay.model;

import android.content.Context;

import views.ecpay.com.postabletecpay.model.sharedPreference.ICommonSharedReference;
import views.ecpay.com.postabletecpay.model.sharedPreference.SharePrefManager;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.util.dbs.SQLiteConnection;
import views.ecpay.com.postabletecpay.util.entities.sqlite.Account;

/**
 * Created by VinhNB on 5/23/2017.
 */

public abstract class CommonModel implements ICommonSharedReference {
    protected Context context;
    protected SQLiteConnection sqLiteConnection;
    protected SharePrefManager sharePrefManager;

    protected CommonModel(Context context) {
        this.context = context;
        sqLiteConnection = SQLiteConnection.getInstance(context);
        sharePrefManager = SharePrefManager.getInstance(context);
    }

    @Override
    public SharePrefManager getManagerSharedPref() {
        return sharePrefManager;
    }

    public Account getAccountInfo(String edong) {
        if (edong == null)
            return null;

        Account account = sqLiteConnection.selectAccount(edong);
        return account;
    }


}

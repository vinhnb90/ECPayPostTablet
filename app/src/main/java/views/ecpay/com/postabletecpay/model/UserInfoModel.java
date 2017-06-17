package views.ecpay.com.postabletecpay.model;

import android.content.Context;

import views.ecpay.com.postabletecpay.model.sharedPreference.ICommonSharedReference;
import views.ecpay.com.postabletecpay.model.sharedPreference.SharePrefManager;
import views.ecpay.com.postabletecpay.util.dbs.SQLiteConnection;
import views.ecpay.com.postabletecpay.util.entities.sqlite.Account;

/**
 * Created by VinhNB on 5/23/2017.
 */

public class UserInfoModel extends CommonModel {

    public UserInfoModel(Context context) {
        super(context);
    }
}

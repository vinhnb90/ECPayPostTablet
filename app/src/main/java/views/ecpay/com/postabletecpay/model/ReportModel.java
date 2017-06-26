package views.ecpay.com.postabletecpay.model;

import android.content.Context;

import views.ecpay.com.postabletecpay.util.dbs.SQLiteConnection;

/**
 * Created by duydatpham on 6/24/17.
 */

public class ReportModel extends CommonModel {
    public ReportModel(Context context) {
        super(context);
    }

    public SQLiteConnection getConnecttion()
    {
        return sqLiteConnection;
    }

    public static class BillInfo{
        private int count;
        private long amount;


        public BillInfo()
        {
            this.amount = 0;
            this.count = 0;
        }

        public BillInfo(int _count, long _amount)
        {
            this.count = _count;
            this.amount = _amount;
        }


        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public long getAmount() {
            return amount;
        }

        public void setAmount(long amount) {
            this.amount = amount;
        }
    }

}

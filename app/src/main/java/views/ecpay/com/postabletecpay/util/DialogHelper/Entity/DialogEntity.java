package views.ecpay.com.postabletecpay.util.DialogHelper.Entity;

import android.content.Context;

/**
 * Created by VinhNB on 2/21/2017.
 * <p>
 * class DialogEntity design with Builder Pattern
 */

public class DialogEntity {
    private final Context mContext;
    private final String mTitle;
    private final String mMessage;
    private long mTimeShow;

    private DialogEntity(DialogBuilder dialogBuilder) {
        mContext = dialogBuilder.mNestedContext;
        mTitle = dialogBuilder.mNestedTitle;
        mMessage = dialogBuilder.mNestedMessage;
        this.mTimeShow = (dialogBuilder.mNestedTimeShow == 0) ? 0 : dialogBuilder.mNestedTimeShow;
    }

    public Context getmContext() {
        return mContext;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmMessage() {
        return mMessage;
    }

    public long getmTimeShow() {
        return mTimeShow;
    }

    public static class DialogBuilder {
        private final Context mNestedContext;
        private final String mNestedTitle;
        private final String mNestedMessage;

        private long mNestedTimeShow;
        private int mNestedLayout;

        public DialogBuilder(Context mNestedContext, String mNestedTitle, String mNestedMessage) {
            this.mNestedContext = mNestedContext;
            this.mNestedTitle = mNestedTitle;
            this.mNestedMessage = mNestedMessage;
        }

        public DialogBuilder setmNestedTimeShow(long mNestedTimeShow) {
            this.mNestedTimeShow = mNestedTimeShow;
            return this;
        }

        public DialogEntity build() {
            return new DialogEntity(this);
        }
    }
}

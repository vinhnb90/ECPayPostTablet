package views.ecpay.com.postabletecpay.view.Main;

import views.ecpay.com.postabletecpay.view.ICommonView;

/**
 * Created by VinhNB_PC on 6/12/2017.
 */

public interface IMainView extends ICommonView{

    void showTextMessage(String textMessage);

    void refreshInfoMain();

    void startShowPbarDownload();

    void finishHidePbarDownload();
}

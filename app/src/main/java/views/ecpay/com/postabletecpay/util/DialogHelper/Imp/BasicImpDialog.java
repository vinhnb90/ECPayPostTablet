package views.ecpay.com.postabletecpay.util.DialogHelper.Imp;

import android.app.AlertDialog;

import views.ecpay.com.postabletecpay.util.DialogHelper.Entity.DialogEntity;
import views.ecpay.com.postabletecpay.util.commons.Common;


/**
 * lọ hoa không có mặc định một loại hoa
 * Created by VinhNB on 2/28/2017.
 */
public class BasicImpDialog extends DialogImp {

    public BasicImpDialog(DialogEntity dialogEntity) {
//        mDialogEntity = new DialogEntity.DialogBuilder(ApplicationDefault.getContext(), TITLE_DEFAULT, MESSAGE_DEFAULT).build();
        mDialogEntity = dialogEntity;
    }

    @Override
    public void show() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mDialogEntity.getmContext());
        builder.setTitle(mDialogEntity.getmTitle());
        builder.setMessage(mDialogEntity.getmMessage());
        builder.setNeutralButton(Common.TEXT_DIALOG.OK.toString(), null);
        builder.show();
    }
}
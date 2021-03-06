package views.ecpay.com.postabletecpay.util.DialogHelper.Imp;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import views.ecpay.com.postabletecpay.R;
import views.ecpay.com.postabletecpay.util.DialogHelper.Entity.DialogEntity;

/**
 * Created by VinhNB on 2/28/2017.
 */
public class LayoutOKBasicImpDialog extends DialogImp {

    public LayoutOKBasicImpDialog(DialogEntity dialogEntity) {
        if (dialogEntity == null)
            return;
        mDialogEntity = dialogEntity;
    }

    @Override
    public void show() {
        try {
            final AlertDialog.Builder builder = new AlertDialog.Builder(mDialogEntity.getmContext());
            LayoutInflater inflater = (LayoutInflater) mDialogEntity.getmContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.diaglog_layout_button_ok, null);
            builder.setView(view);
            final TextView title = (TextView) view.findViewById(R.id.tv_dialog_layout_title);
            final TextView message = (TextView) view.findViewById(R.id.tv_dialog_layout_message);
            final Button buttonOK = (Button) view.findViewById(R.id.btn_dialog_layout_button_ok);

            title.setText(mDialogEntity.getmTitle());
            message.setText(mDialogEntity.getmMessage());
            final AlertDialog alertDialog = builder.show();
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            buttonOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
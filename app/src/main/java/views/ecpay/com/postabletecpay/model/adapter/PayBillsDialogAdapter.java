package views.ecpay.com.postabletecpay.model.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import views.ecpay.com.postabletecpay.R;
import views.ecpay.com.postabletecpay.util.commons.Common;

import static views.ecpay.com.postabletecpay.util.commons.Common.STATUS_BILLING.CHUA_THANH_TOAN;

/**
 * Created by VinhNB on 6/5/2017.
 */

public class PayBillsDialogAdapter extends RecyclerView.Adapter<PayBillsDialogAdapter.BillDialogViewHorder> {

    private  List<PayAdapter.BillEntityAdapter> listBillChecked;
    private  OnInteractionBillDialogRecycler iPayView;
    private  boolean isDisableCheckbox = false;

    public PayBillsDialogAdapter(OnInteractionBillDialogRecycler payView, List<PayAdapter.BillEntityAdapter> listBillChecked, boolean isDisableCheckbox) {
        this.listBillChecked = listBillChecked;
        this.iPayView = payView;
        this.isDisableCheckbox = isDisableCheckbox;

        for (int i = 0, n = this.listBillChecked.size(); i < n; i ++)
        {
            this.listBillChecked.get(i).setMessageError("");
        }


        Collections.sort(this.listBillChecked, PayAdapter.BillEntityAdapter.TermComparatorBillEntityAdapter);

    }

    @Override
    public BillDialogViewHorder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.row_thanhtoan_list_bills, null);
        PayBillsDialogAdapter.BillDialogViewHorder viewHorder = new BillDialogViewHorder(view);

        return viewHorder;
    }

    @Override
    public void onBindViewHolder(BillDialogViewHorder holder, int position) {
        PayAdapter.BillEntityAdapter billChecked = listBillChecked.get(position);

        holder.getCbChoose().setChecked(billChecked.isChecked());
        holder.getTvCode().setText(String.valueOf(billChecked.getBillId()));

        holder.getTvTerm().setText(billChecked.getTHANG_THANH_TOAN());
        holder.getTvName().setText(billChecked.getTEN_KHACH_HANG());
        holder.getTvAmount().setText(Common.convertLongToMoney(billChecked.getTIEN_THANH_TOAN()));

        //TODO onBindViewHolder bill
        String message = billChecked.getMessageError();
//        holder.getIbtnMessage().setVisibility(TextUtils.isEmpty(message) ? View.INVISIBLE : View.VISIBLE);

        String status = Common.STATUS_BILLING.findCodeMessage(billChecked.getTRANG_THAI_TT()).getMessage();

        if(!TextUtils.isEmpty(message))
        {
            status = message;
        }

        if (billChecked.getTRANG_THAI_TT().equalsIgnoreCase(CHUA_THANH_TOAN.getCode()))
            holder.getCbChoose().setVisibility(View.VISIBLE);
        else
            holder.getCbChoose().setVisibility(View.INVISIBLE);

        holder.getCbChoose().setEnabled(!isDisableCheckbox);

        holder.getTvPayStatus().setText(status);

    }

    @Override
    public int getItemCount() {
        return listBillChecked.size();
    }

    public void refreshData(List<PayAdapter.BillEntityAdapter> listBillChecked) {
        if (listBillChecked == null)
            return;

        this.listBillChecked.clear();
        this.listBillChecked.addAll(listBillChecked);
        notifyDataSetChanged();
    }


    public class BillDialogViewHorder extends RecyclerView.ViewHolder {
        @BindView(R.id.cb_row_thanhtoan_list_bills_choose)
        CheckBox cbChoose;
        @BindView(R.id.tv_row_thanhtoan_list_bills_code)
        TextView tvCode;
        @BindView(R.id.tv_row_thanhtoan_list_bills_term)
        TextView tvTerm;
        @BindView(R.id.tv_row_thanhtoan_list_bills_name)
        TextView tvName;
        @BindView(R.id.tv_row_thanhtoan_list_bills_amount)
        TextView tvAmount;
        @BindView(R.id.tv_row_thanhtoan_list_bills_status_pay)
        TextView tvPayStatus;
//        @BindView(R.id.ibtn_dialog_thanhtoan_message)
//        ImageButton ibtnMessage;

        public BillDialogViewHorder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            cbChoose = (CheckBox) itemView.findViewById(R.id.cb_row_thanhtoan_list_bills_choose);
            tvCode = (TextView) itemView.findViewById(R.id.tv_row_thanhtoan_list_bills_code);
            tvTerm = (TextView) itemView.findViewById(R.id.tv_row_thanhtoan_list_bills_term);
            tvName = (TextView) itemView.findViewById(R.id.tv_row_thanhtoan_list_bills_name);
            tvAmount = (TextView) itemView.findViewById(R.id.tv_row_thanhtoan_list_bills_amount);
            tvPayStatus = (TextView) itemView.findViewById(R.id.tv_row_thanhtoan_list_bills_status_pay);

            cbChoose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    BillDialogViewHorder.this.onCheckedChanged(compoundButton, b);
                }
            });

        }

        public CheckBox getCbChoose() {
            return cbChoose;
        }

        public TextView getTvCode() {
            return tvCode;
        }

        public TextView getTvTerm() {
            return tvTerm;
        }

        public TextView getTvName() {
            return tvName;
        }

        public TextView getTvAmount() {
            return tvAmount;
        }

        public TextView getTvPayStatus() {
            return tvPayStatus;
        }

//        public ImageButton getIbtnMessage() {
//            return ibtnMessage;
//        }

        public void onCheckedChanged(final CompoundButton checkBox, boolean checked) {

            if (checkBox.isPressed()) {

                int position = getAdapterPosition();
                PayAdapter.BillEntityAdapter bill = listBillChecked.get(position);
                bill.setChecked(checked);
                long billId = bill.getBillId();





                int count = 0;
                long total = 0;
                for(int i = 0, n = listBillChecked.size(); i < n; i ++)
                {
                    if (listBillChecked.get(i).isChecked())
                    {
                        count ++;
                        total += listBillChecked.get(i).getTIEN_THANH_TOAN();
                    }
                }

                iPayView.showCountBillsAndTotalMoneyInDialog(count, total);
            }

        }

        @OnClick(R.id.ibtn_dialog_thanhtoan_message)
        public void onClickMessage(View view) {
            Common.runAnimationClickViewScale(view, R.anim.scale_view_push, Common.TIME_DELAY_ANIM);
            int pos = getAdapterPosition();
            iPayView.processClickMessageErrorBillDialog(listBillChecked.get(pos).getMessageError());
        }
    }

    public interface OnInteractionBillDialogRecycler {
        void processCheckedBillsDialog(String edong, int pos, boolean isChecked);

        void processClickMessageErrorBillDialog(String messageError);

        void processUnCheckedBillDialog(String message, Common.TYPE_DIALOG typeDialog);

        void showCountBillsAndTotalMoneyInDialog(int totalBillsInList, long totalMoneyInList);
    }
}

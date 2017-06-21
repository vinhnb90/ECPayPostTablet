package views.ecpay.com.postabletecpay.model.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import views.ecpay.com.postabletecpay.R;
import views.ecpay.com.postabletecpay.util.commons.Common;

/**
 * Created by VinhNB on 6/5/2017.
 */

public class PayBillsDialogAdapter extends RecyclerView.Adapter<PayBillsDialogAdapter.BillDialogViewHorder> {

    private static Context context;
    private static List<Entity> listBillChecked;
    private static OnInteractionBillDialogRecycler onInteractor;

    public PayBillsDialogAdapter(Context context, List<Entity> listBillChecked) {
        this.listBillChecked = listBillChecked;
        this.context = context;
        if (context instanceof OnInteractionBillDialogRecycler)
            this.onInteractor = (OnInteractionBillDialogRecycler) context;
        else
            throw new ClassCastException("Class must be implement OnInteractionBillDialogRecycler!");
    }

    @Override
    public BillDialogViewHorder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.row_thanhtoan_list_bills, null);
        PayBillsDialogAdapter.BillDialogViewHorder viewHorder = new BillDialogViewHorder(view);

        return viewHorder;
    }

    @Override
    public void onBindViewHolder(BillDialogViewHorder holder, int position) {
        Entity billChecked = listBillChecked.get(position);

        holder.getCbChoose().setChecked(billChecked.isChecked());
        holder.getTvCode().setText(billChecked.getCode());

        holder.getTvTerm().setText(billChecked.getTerm());
        holder.getTvName().setText(billChecked.getName());
        holder.getTvAmount().setText(Common.convertLongToMoney(billChecked.getAmount()));

        //TODO onBindViewHolder bill
        String message = billChecked.getMessageError();
        holder.getIbtnMessage().setVisibility(TextUtils.isEmpty(message) ? View.INVISIBLE : View.VISIBLE);

        String status = Common.STATUS_BILLING.findCodeMessage(billChecked.getStatus()).getMessage();
        if (billChecked.getStatus() == Common.STATUS_BILLING.CHUA_THANH_TOAN.getCode())
            holder.getCbChoose().setVisibility(View.VISIBLE);
        else
            holder.getCbChoose().setVisibility(View.INVISIBLE);
        holder.getTvPayStatus().setText(status);
    }

    @Override
    public int getItemCount() {
        return listBillChecked.size();
    }

    public void refreshData(List<Entity> listBillChecked) {
        if (listBillChecked == null)
            return;

        this.listBillChecked.clear();
        this.listBillChecked.addAll(listBillChecked);
        notifyDataSetChanged();
    }

    public static class Entity {
        //code: mã khách hàng
        private String code, name, term;
        private Long amount;
        private boolean isChecked;

        //extends
        private String edong;
        private int billId;
        private int status;
        private String messageError;
        public Entity(String code, String name, String term, Long amount, boolean isChecked, String edong, int billId, int status) {
            this.code = code;
            this.name = name;
            this.term = term;
            this.amount = amount;
            this.isChecked = isChecked;
            this.edong = edong;
            this.billId = billId;
            this.status = status;
        }
        public Entity(String code, String name, String term, Long amount, boolean isChecked, String edong, int billId, int status, String messageError) {
            this.code = code;
            this.name = name;
            this.term = term;
            this.amount = amount;
            this.isChecked = isChecked;
            this.edong = edong;
            this.billId = billId;
            this.status = status;
            this.messageError = messageError;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTerm() {
            return term;
        }

        public void setTerm(String term) {
            this.term = term;
        }

        public Long getAmount() {
            return amount;
        }

        public void setAmount(Long amount) {
            this.amount = amount;
        }

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public String getEdong() {
            return edong;
        }

        public void setEdong(String edong) {
            this.edong = edong;
        }

        public int getBillId() {
            return billId;
        }

        public void setBillId(int billId) {
            this.billId = billId;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getMessageError() {
            return messageError;
        }

        public void setMessageError(String messageError) {
            this.messageError = messageError;
        }
    }

    public static class BillDialogViewHorder extends RecyclerView.ViewHolder {
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
        @BindView(R.id.ibtn_dialog_thanhtoan_message)
        ImageButton ibtnMessage;

        public BillDialogViewHorder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            cbChoose = (CheckBox) itemView.findViewById(R.id.cb_row_thanhtoan_list_bills_choose);
            tvCode = (TextView) itemView.findViewById(R.id.tv_row_thanhtoan_list_bills_code);
            tvTerm = (TextView) itemView.findViewById(R.id.tv_row_thanhtoan_list_bills_term);
            tvName = (TextView) itemView.findViewById(R.id.tv_row_thanhtoan_list_bills_name);
            tvAmount = (TextView) itemView.findViewById(R.id.tv_row_thanhtoan_list_bills_amount);
            tvPayStatus = (TextView) itemView.findViewById(R.id.tv_row_thanhtoan_list_bills_status_pay);

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

        public ImageButton getIbtnMessage() {
            return ibtnMessage;
        }

        @OnCheckedChanged(R.id.cb_row_thanhtoan_list_bills_choose)
        public void onCheckedChanged(CheckBox cbChoose, boolean isChecked) {
            if (cbChoose.isPressed()) {
                int pos = getAdapterPosition();
                listBillChecked.get(pos).setChecked(isChecked);

                onInteractor.processCheckedBillsDialog(pos, isChecked);
            }
        }

        @OnClick(R.id.ibtn_dialog_thanhtoan_message)
        public  void onClickMessage(View view)
        {
            Common.runAnimationClickViewScale(view, R.anim.scale_view_push, Common.TIME_DELAY_ANIM);
            int pos = getAdapterPosition();
            onInteractor.processClickMessageErrorBillDialog(listBillChecked.get(pos).getMessageError());
        }
    }

    public interface OnInteractionBillDialogRecycler {
        void processCheckedBillsDialog(int pos, boolean isChecked);

        void processClickMessageErrorBillDialog(String messageError);
    }
}

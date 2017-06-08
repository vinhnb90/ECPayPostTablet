package views.ecpay.com.postabletecpay.model.adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import views.ecpay.com.postabletecpay.R;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.view.Main.MainActivity;

/**
 * Created by VinhNB on 6/5/2017.
 */

public class PayListBillsAdapter extends RecyclerView.Adapter<PayListBillsAdapter.BillDialogViewHorder> {

    private static Context context;
    private static List<Entity> listBillChecked;
    private static OnInteractionListBillAdapter onInteractor;

    public PayListBillsAdapter(Context context, List<Entity> listBillChecked) {
        this.listBillChecked = listBillChecked;
        this.context = context;
        if (context instanceof OnInteractionListBillAdapter)
            this.onInteractor = (OnInteractionListBillAdapter) context;
        else throw new ClassCastException("Class must be implement OnInteractionListBillAdapter!");
    }

    @Override
    public BillDialogViewHorder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.row_thanhtoan_list_bills, null);
        PayListBillsAdapter.BillDialogViewHorder viewHorder = new BillDialogViewHorder(view);

        return viewHorder;
    }

    @Override
    public void onBindViewHolder(BillDialogViewHorder holder, int position) {
        Entity billChecked = listBillChecked.get(position);

        holder.getCbChoose().setChecked(billChecked.isChecked());
        holder.getTvCode().setText(billChecked.getCode());
        holder.getTvTerm().setText(Common.convertDateToDate(billChecked.getTerm(), Common.DATE_TIME_TYPE.yyyymmdd, Common.DATE_TIME_TYPE.mmyyyy));
        holder.getTvName().setText(billChecked.getName());
        holder.getTvAmount().setText(String.valueOf(billChecked.getAmount()) +Common.TEXT_SPACE + Common.UNIT_MONEY);
        holder.getTvPayStatus().setText(PayAdapter.BillInsidePayAdapter.NOT_PAY_YET);
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
        private String code, name, term;
        private Long amount;
        private boolean isChecked;

        //extends
        private String edong;
        private int billId;

        public Entity(String code, String name, String term, Long amount, boolean isChecked, String edong, int billId) {
            this.code = code;
            this.name = name;
            this.term = term;
            this.amount = amount;
            this.isChecked = isChecked;
            this.edong = edong;
            this.billId = billId;
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

        public String getEdong() {
            return edong;
        }

        public void setEdong(String edong) {
            this.edong = edong;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public int getBillId() {
            return billId;
        }

        public void setBillId(int billId) {
            this.billId = billId;
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

        @OnCheckedChanged(R.id.cb_row_thanhtoan_list_bills_choose)
        public void onCheckedChanged(CheckBox cbChoose, boolean isChecked) {
            if(cbChoose.isPressed()) {
                int pos = getAdapterPosition();
                listBillChecked.get(pos).setChecked(isChecked);

                onInteractor.processDataBillsListChecked(pos, isChecked);
            }
        }
    }

    public interface OnInteractionListBillAdapter {
        void processDataBillsListChecked(int pos, boolean isChecked);
    }
}

package views.ecpay.com.postabletecpay.model.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import views.ecpay.com.postabletecpay.R;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.view.Main.MainActivity;

import static views.ecpay.com.postabletecpay.util.commons.Common.STATUS_BILLING.CHUA_THANH_TOAN;
import static views.ecpay.com.postabletecpay.util.commons.Common.TAG;

/**
 * Created by VinhNB on 6/5/2017.
 */

public class PayBillsDialogAdapter extends RecyclerView.Adapter<PayBillsDialogAdapter.BillDialogViewHorder> {

    private static Context context;
    private static List<Entity> listBillChecked;
    private static OnInteractionBillDialogRecycler onInteractor;
    private static boolean isDisableCheckbox = false;

    public PayBillsDialogAdapter(Context context, List<Entity> listBillChecked, boolean isDisableCheckbox) {
        this.listBillChecked = listBillChecked;
        this.context = context;
        this.isDisableCheckbox = isDisableCheckbox;
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
        if (billChecked.getStatus() == CHUA_THANH_TOAN.getCode())
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

    public void refreshData(List<Entity> listBillChecked) {
        if (listBillChecked == null)
            return;

        this.listBillChecked.clear();
        this.listBillChecked.addAll(listBillChecked);
        notifyDataSetChanged();
    }

    public static class Entity implements Comparable<Entity> {
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


        @Override
        public int compareTo(@NonNull Entity entity) {
            long termThis = Common.convertDateToLong(this.getTerm(), Common.DATE_TIME_TYPE.MMyyyy);
            long termThat = Common.convertDateToLong(entity.getTerm(), Common.DATE_TIME_TYPE.MMyyyy);

            //giảm dần
            return (int) (termThat - termThis);
        }

        public static Comparator<Entity> TermComparator
                = new Comparator<Entity>() {

            public int compare(Entity entity, Entity entityNew) {

                //giảm dần
                return entityNew.compareTo(entity);

                //descending order
                //return fruitName2.compareTo(fruitName1);
            }

        };
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
        public void onCheckedChanged(final CheckBox checkBox, boolean checked) {

            if (checkBox.isPressed()) {

                int position = getAdapterPosition();
                Entity bill = listBillChecked.get(position);
                int billId = bill.getBillId();
                String name = bill.getName();
                String code = bill.getCode();
                String term = bill.getTerm();

                //TODO get all bill of KH code and sort by term
                List<Entity> listBillOfCustomer = new ArrayList<>();
                for (int i = 0; i < listBillChecked.size(); i++) {
                    if (listBillChecked.get(i).getCode().equals(code))
                        listBillOfCustomer.add(listBillChecked.get(i));
                }
                Collections.sort(listBillOfCustomer, Entity.TermComparator);
                Log.d(TAG, "onCheckedChanged: ");

                //TODO get index real in new list listBillOfCustomer
                int indexReal = Common.NEGATIVE_ONE;
                for (int i = 0; i < listBillOfCustomer.size(); i++) {
                    if (listBillOfCustomer.get(i).getBillId() == billId) {
                        indexReal = i;
                        break;
                    }
                }

                //SET up flag
                boolean isNotBillPayedTermBefore = false;
                boolean isNotBillOldestPayedTermBefore = false;

                //setup index newest and oldest is checked
                int indexOldest = Common.NEGATIVE_ONE;
                int indexNewest = Common.NEGATIVE_ONE;

                int index = indexReal;
                for (; index < listBillOfCustomer.size(); index++) {
                    //chưa thanh toán + checked
                    if (listBillOfCustomer.get(index).getStatus() == CHUA_THANH_TOAN.getCode() && listBillOfCustomer.get(index).isChecked())
                        indexOldest = index;
                }

                //TODO xử lý việc check và uncheck
                if (checked) {
                    //TODO  kiểm tra cờ lỗi
                    index = indexReal;
                    for (; index < listBillOfCustomer.size(); index++) {
                        String termIndex = listBillOfCustomer.get(index).getTerm();
                        //chưa checked và khác kỳ thì sẽ lỗi kỳ liên tiếp
                        if (!listBillOfCustomer.get(index).isChecked() && !termIndex.equals(term)) {
                            isNotBillPayedTermBefore = true;

                            if (index == indexOldest) {
                                isNotBillOldestPayedTermBefore = true;
                                isNotBillPayedTermBefore = false;
                            }
                        }
                        else {

                        }
                    }

                    //TODO xử lý nếu có lỗi
                    if (isNotBillOldestPayedTermBefore || isNotBillPayedTermBefore) {
                        ((MainActivity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //giữ nguyên trạng thái checkbox
                                checkBox.setChecked(false);
                            }
                        });

                        //kiểm tra tiếp
                        if (isNotBillPayedTermBefore) {
//                            onInteractor.processUnCheckedBillFragment(Common.CODE_REPONSE_BILL_ONLINE.ex10003.getMessage() + Common.TEXT_MULTI_SPACE + name);
                            onInteractor.processUnCheckedBillDialog(Common.CODE_REPONSE_BILL_ONLINE.ex10003.getMessage() + Common.TEXT_MULTI_SPACE + name);
                            return;
                        }

                        if (isNotBillOldestPayedTermBefore) {
//                            onInteractor.processUnCheckedBillFragment(Common.CODE_REPONSE_BILL_ONLINE.ex10005.getMessage() + Common.TEXT_MULTI_SPACE + customerCode);
                            onInteractor.processUnCheckedBillDialog(Common.CODE_REPONSE_BILL_ONLINE.ex10005.getMessage() + Common.TEXT_MULTI_SPACE + name);
                            return;
                        }
                        return;
                    }

                    //TODO nếu checked ok thì setChecked cho list và xử lý
                    bill.setChecked(checked);
//                    onInteractor.processCheckedBillFragment(edong, code, posCustomerInside, listBillChecked, position, indexBegin, indexEnd);
                    onInteractor.processCheckedBillsDialog(position, checked);
                } else {
                    //TODO tìm kỳ mới nhất được checked
                    indexNewest = Common.NEGATIVE_ONE;
                    for (index = 0; index < listBillOfCustomer.size(); index++) {

                        int status = listBillOfCustomer.get(index).getStatus();
                        boolean isChecked = listBillOfCustomer.get(index).isChecked();
                        if (indexNewest != Common.NEGATIVE_ONE)
                            break;
                        if (status == Common.STATUS_BILLING.CHUA_THANH_TOAN.getCode() && isChecked)
                            indexNewest = index;
                    }

                    //TODO  kiểm tra cờ lỗi
                    index = indexReal;
                    //kiểm tra cờ lỗi kỳ xa nhất: xảy ra khi  index = giá trị kỳ xa nhất  + có kỳ lớn hơn được check
                    if (index == indexOldest && indexNewest != indexOldest && !term.equals(listBillOfCustomer.get(indexNewest).getTerm()))
                        isNotBillOldestPayedTermBefore = true;

                    //kiểm tra cờ lỗi kỳ phải liên tiếp
                    String termIndex = listBillOfCustomer.get(index).getTerm();

                    if (listBillOfCustomer.get(index).getStatus() == Common.STATUS_BILLING.CHUA_THANH_TOAN.getCode()) {
                        if (!termIndex.equals(listBillOfCustomer.get(indexNewest).getTerm())
                                && indexNewest != indexOldest
                                )
                            isNotBillPayedTermBefore = true;

                    }

                    //TODO xử lý nếu có lỗi
                    if (isNotBillOldestPayedTermBefore || isNotBillPayedTermBefore) {
                        ((MainActivity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //giữ nguyên trạng thái
                                checkBox.setChecked(true);
                            }
                        });


                        //kiểm tra tiếp

                        if (isNotBillOldestPayedTermBefore) {
//                            onInteractor.processUnCheckedBillFragment(Common.CODE_REPONSE_BILL_ONLINE.ex10003.getMessage() + Common.TEXT_MULTI_SPACE + name);
                            onInteractor.processUnCheckedBillDialog(Common.CODE_REPONSE_BILL_ONLINE.ex10003.getMessage() + Common.TEXT_MULTI_SPACE + name);
                            return;
                        }

                        if (isNotBillPayedTermBefore) {
//                            onInteractor.processUnCheckedBillFragment(Common.CODE_REPONSE_BILL_ONLINE.ex10005.getMessage() + Common.TEXT_MULTI_SPACE + customerCode);
                            onInteractor.processUnCheckedBillDialog(Common.CODE_REPONSE_BILL_ONLINE.ex10005.getMessage() + Common.TEXT_MULTI_SPACE + name);
                            return;
                        }
                    }

                    //TODO nếu checked ok thì setChecked cho list và xử lý
                    bill.setChecked(checked);
                    onInteractor.processCheckedBillsDialog(position, checked);

                    return;
                }
            }
        }

        @OnClick(R.id.ibtn_dialog_thanhtoan_message)
        public void onClickMessage(View view) {
            Common.runAnimationClickViewScale(view, R.anim.scale_view_push, Common.TIME_DELAY_ANIM);
            int pos = getAdapterPosition();
            onInteractor.processClickMessageErrorBillDialog(listBillChecked.get(pos).getMessageError());
        }
    }

    public interface OnInteractionBillDialogRecycler {
        void processCheckedBillsDialog(int pos, boolean isChecked);

        void processClickMessageErrorBillDialog(String messageError);

        void processUnCheckedBillDialog(String message);
    }
}

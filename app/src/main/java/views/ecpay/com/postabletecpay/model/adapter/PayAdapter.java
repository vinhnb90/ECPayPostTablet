package views.ecpay.com.postabletecpay.model.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import views.ecpay.com.postabletecpay.R;
import views.ecpay.com.postabletecpay.presenter.IPayPresenter;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.util.entities.EntityKhachHang;
import views.ecpay.com.postabletecpay.view.Main.MainActivity;

import static views.ecpay.com.postabletecpay.model.adapter.PayAdapter.BillInsidePayAdapter.IS_DEBT;
import static views.ecpay.com.postabletecpay.model.adapter.PayAdapter.BillInsidePayAdapter.NOT_DEBT;

/**
 * Created by VinhNB on 5/26/2017.
 */

public class PayAdapter extends RecyclerView.Adapter<PayAdapter.PayViewHolder> {
    private Context sContext;

    //giá trị billList[0] = mAdapterList[indexBegin] , billList[billList.size()] = mAdapterList[indexEnd]
    private int indexBegin, indexEnd;
    private IPayPresenter payPresenter;

    private List<DataAdapter> data = new ArrayList<>();
//    private List<PayEntityAdapter BillEntityAdapter> billList = new ArrayList<>();
    @BindDrawable(R.drawable.bg_button)
    Drawable green;
    @BindDrawable(R.drawable.bg_button_orange)
    Drawable violet;
    @BindView(R.id.btn_row_thanhtoan_recycler_print)
    Button btnPrintBill;

    private boolean onBind;

    public PayAdapter(Context sContext, IPayPresenter presenter, List<DataAdapter> data, int indexBegin, int indexEnd) {
        this.sContext = sContext;
        this.data = data;
        this.indexBegin = indexBegin;
        this.indexEnd = indexEnd;
        this.payPresenter = presenter;
    }


    public void UpdateAdapter(List<DataAdapter> data, int indexBegin, int indexEnd)
    {
        this.data = data;
        this.indexBegin = indexBegin;
        this.indexEnd = indexEnd;
        this.notifyDataSetChanged();

    }

    @Override
    public PayViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_thanhtoan_recycler, parent, false);
        ButterKnife.bind(this, view);

        return new PayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PayViewHolder holder, int position) {

        onBind = true;

        holder.setDataAdapter(data.get(position));

        EntityKhachHang entityAdapter = data.get(position).getInfoKH();
        final String code = entityAdapter.getMA_KHANG();
        final String edong = entityAdapter.getE_DONG();

        holder.tvTenKH.setText(entityAdapter.getTEN_KHANG());
        holder.tvDiaChi.setText(entityAdapter.getDIA_CHI());
        holder.tvLoTrinh.setText(entityAdapter.getLO_TRINH());
        holder.tvTongTien.setText(Common.convertLongToMoney(data.get(position).getTotalMoney()));
        holder.tvMaKH.setText(code);

        boolean isPayed = data.get(position).isPayed();
        holder.setPayed(isPayed);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            holder.btnTrangThaiNo.setBackground(isPayed ? green : violet);
        } else {
            holder.btnTrangThaiNo.setBackgroundDrawable(isPayed ? green : violet);
        }
        holder.btnTrangThaiNo.setText(isPayed ? NOT_DEBT : IS_DEBT);

        Common.runAnimationClickViewScale(holder.cardView, R.anim.twinking_view, Common.TIME_DELAY_ANIM);

        DataAdapter dataAdapter = getObjectDataAdapter(code);


        holder.billsInsideAdapter.setBillList(edong, code, dataAdapter.getBillKH(), position);
        holder.rvBill.invalidate();


        boolean isShowBill = false;
        for (int i = 0, n = data.get(position).getBillKH().size(); i < n; i ++)
        {
            if(payPresenter.getPayModel().containBillInSelected(data.get(position).getBillKH().get(i).getBillId()))
            {
                isShowBill = true;
                break;
            }
        }

        data.get(position).setShowBill(isShowBill);

        holder.showBills(isShowBill);
        onBind = false;

    }

    private DataAdapter getObjectDataAdapter(String code) {
        DataAdapter dataAdapter = null;
        int index = 0;
        for(; index<data.size(); index++)
        {
            if(data.get(index).getInfoKH().getMA_KHANG().equals(code)) {
                dataAdapter = data.get(index);
                break;
            }
        }

        return dataAdapter;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class PayViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_row_pay_recycler_customer)
        TextView tvTenKH;
        @BindView(R.id.tv_row_pay_recycler_diaChi)
        TextView tvDiaChi;
        @BindView(R.id.tv_row_pay_recycler_loTrinh)
        TextView tvLoTrinh;
        @BindView(R.id.tv_row_pay_recycler_maKhachHang)
        TextView tvMaKH;
        @BindView(R.id.tv_row_pay_recycler_tongTien)
        TextView tvTongTien;
        @BindView(R.id.btn_row_pay_recycler_trangThaiNo)
        Button btnTrangThaiNo;
        @BindView(R.id.btn_row_thanhtoan_recycler_print)
        Button btn_row_thanhtoan_recycler_print;
        @BindView(R.id.card_row_thanh_toan_recycler)
        LinearLayout cardView;
        @BindView(R.id.rvHoaDon)
        RecyclerView rvBill;
        //        CardView cardView;
        @BindView(R.id.ll_row_thanhtoan_recycler_print)
        LinearLayout ll_row_thanhtoan_recycler_print;
        public View itemView;


        boolean isPayed;


        DataAdapter dataAdapter;

        public PayViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.itemView = itemView;

            billsInsideAdapter = new BillInsidePayAdapter(sContext);
            rvBill.setLayoutManager(new LinearLayoutManager(sContext, LinearLayoutManager.VERTICAL, false));
            rvBill.setAdapter(billsInsideAdapter);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!onBind) {
                        boolean isShowBill = !dataAdapter.isShowBill();

                        dataAdapter.setShowBill(isShowBill);
                        showBills(isShowBill);
                    }
                }
            });

            btn_row_thanhtoan_recycler_print.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    payPresenter.PrintThongBaoDien(dataAdapter);
                }
            });

            //set 10 rows and a page
//            cardView.getLayoutParams().height = (int) (height / PayFragment.ROWS_ON_PAGE);
        }


        public DataAdapter getDataAdapter() {
            return dataAdapter;
        }

        public void setDataAdapter(DataAdapter dataAdapter) {
            this.dataAdapter = dataAdapter;
        }

        public void showBills(boolean show)
        {

            rvBill.setVisibility(show ? View.VISIBLE : View.GONE);

            if(isPayed)
            {
                ll_row_thanhtoan_recycler_print.setVisibility(View.GONE);
            }else
            {
                ll_row_thanhtoan_recycler_print.setVisibility(show ? View.VISIBLE : View.GONE);
            }

        }


        public boolean isPayed() {
            return isPayed;
        }

        public void setPayed(boolean payed) {
            isPayed = payed;
        }

        private BillInsidePayAdapter billsInsideAdapter;

        public View getItemView() {
            return itemView;
        }
    }

    public static class BillEntityAdapter implements Comparable<BillEntityAdapter>{

        private String THANG_THANH_TOAN;
        private long TIEN_THANH_TOAN;
        private boolean isPrint;
        private String TRANG_THAI_TT;
        private boolean isChecked;
        private String requestDate;
        private boolean isPrintEnable;
        private boolean isCheckEnable;
        private String MA_KHACH_HANG;
        private String TEN_KHACH_HANG;
        private String MA_DIEN_LUC;

        private String messageError = "";

        //info extension
        private long billId;
        private String billingBy;

        public BillEntityAdapter() {
        }

        public String getMessageError() {
            return messageError;
        }

        public void setMessageError(String messageError) {
            this.messageError = messageError;
        }

        public String getTEN_KHACH_HANG() {
            return TEN_KHACH_HANG;
        }

        public void setTEN_KHACH_HANG(String TEN_KHACH_HANG) {
            this.TEN_KHACH_HANG = TEN_KHACH_HANG;
        }

        public String getMA_KHACH_HANG() {
            return MA_KHACH_HANG;
        }

        public void setMA_KHACH_HANG(String MA_KHACH_HANG) {
            this.MA_KHACH_HANG = MA_KHACH_HANG;
        }

        public boolean isCheckEnable() {
            return isCheckEnable;
        }

        public void setCheckEnable(boolean checkEnable) {
            isCheckEnable = checkEnable;
        }

        public boolean isPrintEnable() {
            return isPrintEnable;
        }

        public void setPrintEnable(boolean printEnable) {
            isPrintEnable = printEnable;
        }

        public String getTHANG_THANH_TOAN() {
            return THANG_THANH_TOAN;
        }

        public void setTHANG_THANH_TOAN(String THANG_THANH_TOAN) {
            this.THANG_THANH_TOAN = THANG_THANH_TOAN;
        }

        public long getTIEN_THANH_TOAN() {
            return TIEN_THANH_TOAN;
        }

        public void setTIEN_THANH_TOAN(long TIEN_THANH_TOAN) {
            this.TIEN_THANH_TOAN = TIEN_THANH_TOAN;
        }

        public boolean isPrint() {
            return isPrint;
        }

        public void setPrint(boolean print) {
            isPrint = print;
        }

        public String getTRANG_THAI_TT() {
            return TRANG_THAI_TT;
        }

        public void setTRANG_THAI_TT(String TRANG_THAI_TT) {
            this.TRANG_THAI_TT = TRANG_THAI_TT;
        }

        public boolean isChecked() {
            return isChecked;
        }

        public String getMA_DIEN_LUC() {
            return MA_DIEN_LUC;
        }

        public void setMA_DIEN_LUC(String MA_DIEN_LUC) {
            this.MA_DIEN_LUC = MA_DIEN_LUC;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public String getRequestDate() {
            return requestDate;
        }

        public void setRequestDate(String requestDate) {
            this.requestDate = requestDate;
        }

        public long getBillId() {
            return billId;
        }

        public void setBillId(long billId) {
            this.billId = billId;
        }


        public String getBillingBy() {
            return billingBy;
        }

        public void setVI_TTOAN(String billingBy) {
            this.billingBy = billingBy;
        }

        @Override
        public int compareTo(@NonNull BillEntityAdapter billEntityAdapter) {
            long termThis = Common.convertDateToLong(this.getTHANG_THANH_TOAN(), Common.DATE_TIME_TYPE.MMyyyy);
            long termThat = Common.convertDateToLong(billEntityAdapter.getTHANG_THANH_TOAN(), Common.DATE_TIME_TYPE.MMyyyy);

            //giảm dần
            return (int) (termThat - termThis);
        }

        public static Comparator<BillEntityAdapter> TermComparatorBillEntityAdapter
                = new Comparator<BillEntityAdapter>() {

            public int compare(BillEntityAdapter entity, BillEntityAdapter entityNew) {

                //giảm dần
                return entityNew.compareTo(entity);

                //descending order
                //return fruitName2.compareTo(fruitName1);
            }

        };
    }

    public class BillInsidePayAdapter extends RecyclerView.Adapter<BillInsidePayAdapter.BillInsidePayViewHolder> {
        public static final String IS_PAY = "Đã thanh toán";
        public static final String NOT_PAY_YET = "Chưa thanh toán";
        public static final String IS_DEBT = "Còn nợ";
        public static final String NOT_DEBT = "Hết nợ";

        private List<BillEntityAdapter> billList = new ArrayList<>();
        private String code;
        private String edong;

        //extends
        private int posCustomerInside;

        public BillInsidePayAdapter(Context context) {
        }

        public void setBillList(String edong, String code, List<BillEntityAdapter> billList, int posCustomer) {
            this.billList = new ArrayList<>();
            this.billList.addAll(billList);
            this.code = code;
            this.edong = edong;
            this.posCustomerInside = posCustomer;
            notifyDataSetChanged();
        }

        @Override
        public BillInsidePayAdapter.BillInsidePayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View v1 = inflater.inflate(R.layout.row_bill_inside_pay, parent, false);
            BillInsidePayViewHolder viewHolder = new BillInsidePayViewHolder(v1);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(BillInsidePayAdapter.BillInsidePayViewHolder holder, int position) {
            final BillEntityAdapter entity = billList.get(position);
            holder.setData(entity);
            holder.setPosition(position);

            holder.setAdapterParent(this);

            holder.cb.setChecked(entity.isChecked());
            if (entity.getTRANG_THAI_TT().equalsIgnoreCase(Common.STATUS_BILLING.CHUA_THANH_TOAN.getCode())) {
                holder.cb.setEnabled(true);
                holder.ibtnDelete.setVisibility(View.INVISIBLE);

                holder.tvStatusBill.setText(Common.STATUS_BILLING.CHUA_THANH_TOAN.getMessage());
            } else {
                holder.cb.setEnabled(false);
                holder.cb.setChecked(true);
                holder.ibtnDelete.setVisibility(View.VISIBLE);
                holder.tvStatusBill.setText(Common.STATUS_BILLING.DA_THANH_TOAN.getMessage());
                entity.setPrint(entity.getTRANG_THAI_TT().equalsIgnoreCase(Common.STATUS_BILLING.DA_THANH_TOAN.getCode()));
            }
            holder.tvDate.setText(entity.getTHANG_THANH_TOAN());
            holder.tvMoneyBill.setText(Common.convertLongToMoney(entity.getTIEN_THANH_TOAN()));

            if (entity.getTRANG_THAI_TT().equalsIgnoreCase(Common.STATUS_BILLING.CHUA_THANH_TOAN.getCode())){
                holder.tvStatusBill.setTextColor(ContextCompat.getColor(sContext, R.color.colorRed));
            } else {
                holder.tvStatusBill.setTextColor(ContextCompat.getColor(sContext, R.color.colorTextGreen));
            }
            holder.ibtnPrintInside.setVisibility(entity.isPrint ? View.VISIBLE : View.INVISIBLE);

        }

        @Override
        public int getItemCount() {
            return this.billList.size();
        }

        public class BillInsidePayViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.cb_row_bill_inside_pay)
            CheckBox cb;
            @BindView(R.id.tv_row_bill_inside_pay_date)
            TextView tvDate;
            @BindView(R.id.tv_row_bill_inside_pay_money)
            TextView tvMoneyBill;
            @BindView(R.id.tv_row_bill_inside_pay_trang_thai)
            TextView tvStatusBill;
            @BindView(R.id.btn_row_bill_inside_pay_print)
            ImageButton ibtnPrintInside;
            @BindView(R.id.btn_row_bill_inside_pay_delete)
            ImageButton ibtnDelete;

            BillInsidePayAdapter adapterParent;


            BillEntityAdapter data;
            int position;


            public BillInsidePayViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);

                ibtnPrintInside.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        payPresenter.PrintHoaDon(data);
                    }
                });

            }

            public BillInsidePayAdapter getAdapterParent() {
                return adapterParent;
            }

            public void setAdapterParent(BillInsidePayAdapter adapterParent) {
                this.adapterParent = adapterParent;
            }

            public void setPosition(int position) {
                this.position = position;
            }

            public BillEntityAdapter getData() {
                return data;
            }

            public void setData(BillEntityAdapter data) {
                this.data = data;
            }

            @OnCheckedChanged(R.id.cb_row_bill_inside_pay)
            public void onCheckedChanged(final CheckBox checkBox, boolean checked) {
                if (checkBox.isPressed()) {

                    boolean isNotBillPayedTermBefore = false;
                    boolean isNotBillOldestPayedTermBefore = false;

                    int index = position + 1;
                    int indexNotPayedTermOldest = Common.NEGATIVE_ONE;

                    BillEntityAdapter bill = billList.get(position);
                    //TODO check hóa đơn xa nhất
                    for (; index < billList.size(); index++) {
                        if (billList.get(index).getTRANG_THAI_TT().equalsIgnoreCase(Common.STATUS_BILLING.CHUA_THANH_TOAN.getCode()) && !billList.get(index).isChecked())
                            indexNotPayedTermOldest = index;
                    }

                    if (checked) {
                        //TODO check hóa đơn liên tục fragment khi checked

                        if (indexNotPayedTermOldest != Common.NEGATIVE_ONE) {
                            ((MainActivity) sContext).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    checkBox.setChecked(false);
                                }
                            });
                           payPresenter.getIPayView().showMessageNotifyPayfrag(Common.CODE_REPONSE_BILL_ONLINE.ex10003.getMessage() + Common.TEXT_MULTI_SPACE + bill.getMA_KHACH_HANG());
                            return;
                        }
                        payPresenter.addSelectBillToPay(bill, true);
                        bill.setChecked(checked);
//                        interaction.processCheckedBillFragment(edong, code, posCustomerInside, billList, position, indexBegin, indexEnd);
                    } else {
                        //TODO check hóa đơn liên tục fragment khi unchecked

                        boolean hasChange = false;
                        for (int i = 0; i <= position; i ++)
                        {
                            if(billList.get(i).getTRANG_THAI_TT().equalsIgnoreCase(Common.STATUS_BILLING.CHUA_THANH_TOAN.getCode()))
                            {
                                if(i != position && billList.get(i).isChecked())
                                {
                                    hasChange = true;
                                }
                                billList.get(i).setChecked(false);
                                payPresenter.addSelectBillToPay(billList.get(i), false);
                            }
                        }

                        if(hasChange)
                        {
                            notifyDataSetChanged();
                        }

                        return;
                    }
                }
            }

            @OnClick(R.id.btn_row_bill_inside_pay_delete)
            public void doClickDelete(View view) {
                int position = getAdapterPosition();
                BillEntityAdapter bill = billList.get(position);

                payPresenter.getIPayView().processDialogDeleteBillOnline(edong, bill, adapterParent);
            }

            public CheckBox getCb() {
                return cb;
            }

            public TextView getTvDate() {
                return tvDate;
            }

            public TextView getTvMoneyBill() {
                return tvMoneyBill;
            }

            public TextView getTvStatusBill() {
                return tvStatusBill;
            }

            public ImageButton getIbtnPrintInside() {
                return ibtnPrintInside;
            }

            public ImageButton getIbtnDelete() {
                return ibtnDelete;
            }
        }
    }

//    public interface OnInterationBillInsidePayAdapter {
//        void processCheckedBillFragment(String edong, String code, int posCustomer, List<BillEntityAdapter> billList, int posBillInside, int indexBegin, int indexEnd);
//
//        void processDeleteBillOnlineFragment(String edong, String code, BillEntityAdapter bill, int posCustomerInside);
//
//        void processUnCheckedBillFragment(String message);
//    }

    public static class DataAdapter {
        private EntityKhachHang infoKH;
        private List<BillEntityAdapter> billKH;
        private long totalMoney;
        private boolean isShowBill;


        public DataAdapter(EntityKhachHang infoKH, List<BillEntityAdapter> billKH, long total) {
            this.infoKH = infoKH;
            this.billKH = billKH;
            this.totalMoney = total;
        }

        public long getTotalMoney() {
            return totalMoney;
        }

        public void setTotalMoney(long totalMoney) {
            this.totalMoney = totalMoney;
        }

        public boolean isShowBill() {
            return isShowBill;
        }

        public void setShowBill(boolean showBill) {
            isShowBill = showBill;
        }

        public EntityKhachHang getInfoKH() {
            return infoKH;
        }

        public List<BillEntityAdapter> getBillKH() {
            return billKH;
        }


        public boolean isPayed()
        {
            for (int i = 0; i < billKH.size(); i ++)
            {
                if(billKH.get(i).getTRANG_THAI_TT().equalsIgnoreCase(Common.STATUS_BILLING.CHUA_THANH_TOAN.getCode()))
                    return false;
            }
            return true;
        }
    }
}
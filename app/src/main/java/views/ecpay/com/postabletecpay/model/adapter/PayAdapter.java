package views.ecpay.com.postabletecpay.model.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
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
import java.util.List;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import views.ecpay.com.postabletecpay.R;
import views.ecpay.com.postabletecpay.model.PayModel;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.view.Main.MainActivity;
import views.ecpay.com.postabletecpay.view.ThanhToan.IPayView;

import static views.ecpay.com.postabletecpay.model.adapter.PayAdapter.BillInsidePayAdapter.IS_DEBT;
import static views.ecpay.com.postabletecpay.model.adapter.PayAdapter.BillInsidePayAdapter.NOT_DEBT;

/**
 * Created by VinhNB on 5/26/2017.
 */

public class PayAdapter extends RecyclerView.Adapter<PayAdapter.PayViewHolder> {
    private static Context sContext;
    private static IPayView sIPayView;

    //giá trị billList[0] = mAdapterList[indexBegin] , billList[billList.size()] = mAdapterList[indexEnd]
    private int indexBegin, indexEnd;
    private PayModel payModel;

    private static List<PayEntityAdapter> billList = new ArrayList<>();
    @BindDrawable(R.drawable.bg_button_orange)
    Drawable green;
    @BindDrawable(R.drawable.bg_button)
    Drawable violet;
    @BindView(R.id.btn_row_thanhtoan_recycler_print)
    Button btnPrintBill;

    private static int positionCustomer;
    private boolean onBind;

    public PayAdapter(Context sContext, IPayView sIPayView, List<PayEntityAdapter> billList, int indexBegin, int indexEnd) {
        this.sContext = sContext;
        this.sIPayView = sIPayView;
        this.billList = billList;
        this.indexBegin = indexBegin;
        this.indexEnd = indexEnd;

        payModel = new PayModel(sIPayView.getContextView());
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

        positionCustomer = position;
        PayEntityAdapter entityAdapter = billList.get(position);
        final String code = entityAdapter.getMaKH();
        final String edong = entityAdapter.getEdong();
        boolean isPayed = entityAdapter.isPayed();

        holder.tvTenKH.setText(entityAdapter.getTenKH());
        holder.tvDiaChi.setText(entityAdapter.getDiaChi());
        holder.tvLoTrinh.setText(entityAdapter.getLoTrinh());
        holder.tvTongTien.setText(Common.convertLongToMoney(entityAdapter.getTongTien()));
        holder.tvMaKH.setText(code);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            holder.btnTrangThaiNo.setBackground(isPayed ? green : violet);
        } else {
            holder.btnTrangThaiNo.setBackgroundDrawable(isPayed ? green : violet);
        }
        holder.btnTrangThaiNo.setText(isPayed ? NOT_DEBT : IS_DEBT);

        Common.runAnimationClickViewScale(holder.cardView, R.anim.twinking_view, Common.TIME_DELAY_ANIM);

        ((MainActivity)sContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                List<PayAdapter.BillEntityAdapter> listBill = new ArrayList<>();
                listBill = payModel.getAllBillOfCustomer(edong, code);
                holder.billsInsideAdapter.setBillList(edong, code, listBill, positionCustomer);
                holder.rvBill.invalidate();

                boolean isShowBill = billList.get(positionCustomer).isShowBill();
                holder.rvBill.setVisibility(isShowBill ? View.VISIBLE : View.GONE);
            }
        });


        onBind = false;

    }

    @Override
    public int getItemCount() {
        return billList.size();
    }

    public void refreshData(List<PayEntityAdapter> adapterList) {
        if (adapterList == null)
            return;

        this.billList.clear();
        this.billList.addAll(adapterList);
        notifyDataSetChanged();
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
        @BindView(R.id.card_row_thanh_toan_recycler)
        LinearLayout cardView;
        @BindView(R.id.rvHoaDon)
        RecyclerView rvBill;
        //        CardView cardView;
        public View itemView;

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
                    if(!onBind) {
                        boolean isShowBill = billList.get(positionCustomer).isShowBill();

                        billList.get(positionCustomer).setShowBill(isShowBill = !isShowBill);
                        rvBill.setVisibility(isShowBill ? View.VISIBLE : View.GONE);
                    }
                }
            });
            //set 10 rows and a page
//            cardView.getLayoutParams().height = (int) (height / PayFragment.ROWS_ON_PAGE);
        }

        private BillInsidePayAdapter billsInsideAdapter;

        public View getItemView() {
            return itemView;
        }
    }

    public static class PayEntityAdapter {

        private String edong;
        private String tenKH;
        private String diaChi;
        private String loTrinh;
        private String maKH;
        private long tongTien;
        private boolean isPayed;

        //param extension
        private boolean isShowBill;

        public PayEntityAdapter() {
        }

        public PayEntityAdapter(String edong, String tenKH, String diaChi, String loTrinh, String maKH, long tongTien, boolean isPayed, boolean isShowBill) {
            this.edong = edong;
            this.tenKH = tenKH;
            this.diaChi = diaChi;
            this.loTrinh = loTrinh;
            this.maKH = maKH;
            this.tongTien = tongTien;
            this.isPayed = isPayed;
            this.isShowBill = isShowBill;
        }

        public String getEdong() {
            return edong;
        }

        public String getTenKH() {
            return tenKH;
        }

        public String getDiaChi() {
            return diaChi;
        }

        public String getLoTrinh() {
            return loTrinh;
        }

        public String getMaKH() {
            return maKH;
        }

        public long getTongTien() {
            return tongTien;
        }

        public boolean isPayed() {
            return isPayed;
        }

        public void setPayed(boolean payed) {
            this.isPayed = payed;
        }

        public void setEdong(String edong) {
            this.edong = edong;
        }

        public void setTenKH(String tenKH) {
            this.tenKH = tenKH;
        }

        public void setDiaChi(String diaChi) {
            this.diaChi = diaChi;
        }

        public void setLoTrinh(String loTrinh) {
            this.loTrinh = loTrinh;
        }

        public void setMaKH(String maKH) {
            this.maKH = maKH;
        }

        public void setTongTien(long tongTien) {
            this.tongTien = tongTien;
        }

        public boolean isShowBill() {
            return isShowBill;
        }

        public void setShowBill(boolean showBill) {
            isShowBill = showBill;
        }
    }

    public static class BillEntityAdapter {

        private String monthBill;
        private long moneyBill;
        private boolean isPrint;
        private int status;
        private boolean isChecked;
        private String requestDate;

        //info extension
        private long billId;
        private String customerPayCode;
        private String billingBy;

        public BillEntityAdapter() {
        }

        public BillEntityAdapter(String monthBill, long moneyBill, boolean isPrint, int status, boolean isChecked, String requestDate, int billId, String customerPayCode, String billingBy) {
            this.monthBill = monthBill;
            this.moneyBill = moneyBill;
            this.isPrint = isPrint;
            this.status = status;
            this.isChecked = isChecked;
            this.requestDate = requestDate;
            this.billId = billId;
            this.customerPayCode = customerPayCode;
            this.billingBy = billingBy;
        }

        public String getMonthBill() {
            return monthBill;
        }

        public void setMonthBill(String monthBill) {
            this.monthBill = monthBill;
        }

        public long getMoneyBill() {
            return moneyBill;
        }

        public void setMoneyBill(long moneyBill) {
            this.moneyBill = moneyBill;
        }

        public boolean isPrint() {
            return isPrint;
        }

        public void setPrint(boolean print) {
            isPrint = print;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public boolean isChecked() {
            return isChecked;
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

        public String getCustomerPayCode() {
            return customerPayCode;
        }

        public void setCustomerPayCode(String customerPayCode) {
            this.customerPayCode = customerPayCode;
        }

        public String getBillingBy() {
            return billingBy;
        }

        public void setBillingBy(String billingBy) {
            this.billingBy = billingBy;
        }
    }

    public class BillInsidePayAdapter extends RecyclerView.Adapter<BillInsidePayAdapter.BillInsidePayViewHolder> {
        public static final String IS_PAY = "Đã thanh toán";
        public static final String NOT_PAY_YET = "Chưa thanh toán";
        public static final String IS_DEBT = "Còn nợ";
        public static final String NOT_DEBT = "Hết nợ";

        private List<BillEntityAdapter> billList = new ArrayList<>();
        private String code;
        private String edong;
        private OnInterationBillInsidePayAdapter interaction;

        //extends
        private int posCustomerInside;

        public BillInsidePayAdapter(Context context) {
            if (context instanceof OnInterationBillInsidePayAdapter) {
                interaction = (OnInterationBillInsidePayAdapter) context;
            } else
                throw new ClassCastException("Context must be implement BillInsidePayViewHolder.OnInterationBillInsidePayAdapter!");
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
            BillEntityAdapter entity = billList.get(position);
            holder.cb.setChecked(entity.isChecked());
            if (entity.getStatus() == Common.STATUS_BILLING.CHUA_THANH_TOAN.getCode()) {
                holder.cb.setEnabled(true);
                holder.ibtnDelete.setVisibility(View.INVISIBLE);
            } else {
                holder.cb.setEnabled(false);
                holder.ibtnDelete.setVisibility(View.VISIBLE);
            }
            holder.tvDate.setText(entity.getMonthBill());
            holder.tvMoneyBill.setText(Common.convertLongToMoney(entity.getMoneyBill()));
            holder.tvStatusBill.setText(Common.STATUS_BILLING.findCodeMessage(entity.getStatus()).getMessage());

            if (Common.STATUS_BILLING.findCodeMessage(entity.getStatus()).getCode() == Common.STATUS_BILLING.DANG_CHO_HUY.getCode()) {
                holder.tvStatusBill.setTextColor(ContextCompat.getColor(sContext, R.color.colorRed));
            }else {
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

            public BillInsidePayViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            @OnCheckedChanged(R.id.cb_row_bill_inside_pay)
            public void onCheckedChanged(CheckBox checkBox, boolean checked) {
                if (checkBox.isPressed()) {
                    int position = getAdapterPosition();
                    BillEntityAdapter bill = billList.get(position);
                    bill.setChecked(checked);

                    interaction.processCheckedBillFragment(edong, code,  posCustomerInside, billList, position, indexBegin, indexEnd);
                }
            }

            @OnClick(R.id.btn_row_bill_inside_pay_delete)
            public void doClickDelete(View view) {
                int position = getAdapterPosition();
                BillEntityAdapter bill = billList.get(position);

                interaction.processDeleteBillOnlineFragment(edong, code, bill, posCustomerInside);
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

    public interface OnInterationBillInsidePayAdapter {
        void processCheckedBillFragment(String edong, String code, int posCustomer, List<BillEntityAdapter>  billList, int posBillInside, int indexBegin, int indexEnd);

        void processDeleteBillOnlineFragment(String edong, String code, BillEntityAdapter bill, int posCustomerInside);
    }
}
package views.ecpay.com.postabletecpay.model.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
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
import views.ecpay.com.postabletecpay.R;
import views.ecpay.com.postabletecpay.model.PayModel;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.view.ThanhToan.IPayView;

/**
 * Created by VinhNB on 5/26/2017.
 */

public class PayAdapter extends RecyclerView.Adapter<PayAdapter.PayViewHolder> {
    private static Context sContext;
    private static IPayView sIPayView;
    private PayModel payModel;
    private static List<PayEntityAdapter> mAdapterList = new ArrayList<>();
    @BindDrawable(R.drawable.bg_button_orange)
    Drawable green;
    @BindDrawable(R.drawable.bg_button)
    Drawable violet;
    @BindView(R.id.btn_row_thanhtoan_recycler_print)
    Button btnPrintBill;

    public PayAdapter(Context sContext, IPayView sIPayView, List<PayEntityAdapter> mAdapterList) {
        this.sContext = sContext;
        this.sIPayView = sIPayView;
        this.mAdapterList = mAdapterList;

        payModel = new PayModel(sIPayView.getContextView());
    }

    @Override
    public PayViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_thanhtoan_recycler, parent, false);
        ButterKnife.bind(this, view);

        return new PayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PayViewHolder holder, final int position) {
        PayEntityAdapter entityAdapter = mAdapterList.get(position);
        String code = entityAdapter.getMaKH();
        String edong = entityAdapter.getEdong();
        boolean isConNo = entityAdapter.isPayed();

        holder.tvTenKH.setText(entityAdapter.getTenKH());
        holder.tvDiaChi.setText(entityAdapter.getDiaChi());
        holder.tvLoTrinh.setText(entityAdapter.getLoTrinh());
        holder.tvTongTien.setText(entityAdapter.getTongTien() + "");
        holder.tvMaKH.setText(code);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            holder.btnTrangThaiNo.setBackground(isConNo ? green : violet);
        } else {
            holder.btnTrangThaiNo.setBackgroundDrawable(isConNo ? green : violet);
        }
        Common.runAnimationClickViewScale(holder.cardView, R.anim.twinking_view, Common.TIME_DELAY_ANIM);

        List<PayAdapter.BillEntityAdapter> listBill = new ArrayList<>();
        listBill = payModel.getAllBillOfCustomer(edong, code);
        holder.insideTodayAdapter.setBillList(edong, code, listBill, position);

        boolean isShowBill = mAdapterList.get(position).isShowBill();
        holder.rvBill.setVisibility(isShowBill ? View.VISIBLE : View.GONE);

        holder.getItemView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isShowBill = mAdapterList.get(position).isShowBill();

                mAdapterList.get(position).setShowBill(isShowBill = !isShowBill);
                holder.rvBill.setVisibility(isShowBill ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAdapterList.size();
    }

    public void refreshData(List<PayEntityAdapter> adapterList) {
        if (adapterList == null)
            return;

        this.mAdapterList.clear();
        this.mAdapterList.addAll(adapterList);
        this.notifyDataSetChanged();
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

            insideTodayAdapter = new BillInsidePayAdapter(sContext);
            rvBill.setLayoutManager(new LinearLayoutManager(sContext, LinearLayoutManager.VERTICAL, false));
            rvBill.setAdapter(insideTodayAdapter);

            //set 10 rows and a page
//            cardView.getLayoutParams().height = (int) (height / PayFragment.ROWS_ON_PAGE);
        }

        private BillInsidePayAdapter insideTodayAdapter;

        public View getItemView() {
            return itemView;
        }
    }

    public static class PayEntityAdapter {
        public final static String TT_HET_NO = "Hết nợ";
        public final static String TT_CON_NO = "Còn nợ";

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
        private double moneyBill;
        private boolean isPrint;
        private boolean isPayed;
        private boolean isChecked;

        //info extension
        private int billId;
        private String customerPayCode;
        private String billingBy;

        public BillEntityAdapter(String monthBill, double moneyBill, boolean isPrint, boolean isPayed, boolean isChecked, int billId, String customerPayCode, String billingBy) {
            this.monthBill = monthBill;
            this.moneyBill = moneyBill;
            this.isPrint = isPrint;
            this.isPayed = isPayed;
            this.isChecked = isChecked;
            this.billId = billId;
            this.customerPayCode = customerPayCode;
            this.billingBy = billingBy;
        }

        public BillEntityAdapter() {
        }

        public String getMonthBill() {
            return monthBill;
        }

        public void setMonthBill(String monthBill) {
            this.monthBill = monthBill;
        }

        public double getMoneyBill() {
            return moneyBill;
        }

        public void setMoneyBill(double moneyBill) {
            this.moneyBill = moneyBill;
        }

        public boolean isPrint() {
            return isPrint;
        }

        public void setPrint(boolean print) {
            isPrint = print;
        }

        public boolean isPayed() {
            return isPayed;
        }

        public void setPayed(boolean payed) {
            isPayed = payed;
        }

        public boolean isChecked() {
            return isChecked;
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

    public static class BillInsidePayAdapter extends RecyclerView.Adapter<BillInsidePayAdapter.BillInsidePayViewHolder> {
        public static final String IS_PAY = "Đã thanh toán";
        public static final String NOT_PAY_YET = "Chưa thanh toán";

        private static List<BillEntityAdapter> billList;
        private static String code;
        private static String edong;
        private static BillInsidePayViewHolder.OnInterationBillInsidePayAdapter interaction;

        //extends
        private static int posCustomer;

        public BillInsidePayAdapter(Context context) {
            if (context instanceof BillInsidePayViewHolder.OnInterationBillInsidePayAdapter) {
                interaction = (BillInsidePayViewHolder.OnInterationBillInsidePayAdapter) context;
            } else
                throw new ClassCastException("Context must be implement BillInsidePayViewHolder.OnInterationBillInsidePayAdapter!");
        }

        public void setBillList(String edong, String code, List<BillEntityAdapter> billList, int posCustomer) {
            this.billList = billList;
            this.code = code;
            this.edong = edong;
            this.posCustomer = posCustomer;
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
            holder.cb.setEnabled(!entity.isPayed);
            holder.tvDate.setText(entity.getMonthBill());
            holder.tvMoneyBill.setText((int) entity.getMoneyBill() + Common.TEXT_SPACE + Common.UNIT_MONEY);
            holder.tvStatusBill.setText(entity.isPayed ? IS_PAY : NOT_PAY_YET);
            holder.ibtnPrintInside.setVisibility(entity.isPrint ? View.VISIBLE : View.INVISIBLE);
            holder.ibtnDelete.setVisibility(entity.isPayed ? View.VISIBLE : View.INVISIBLE);
        }

        @Override
        public int getItemCount() {
            return this.billList.size();
        }


        public static class BillInsidePayViewHolder extends RecyclerView.ViewHolder {
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
                int position = getAdapterPosition();
                BillEntityAdapter bill = billList.get(position);
                bill.setChecked(checked);

                interaction.processCheckedBillFragment(edong, code, bill, posCustomer);

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

            public interface OnInterationBillInsidePayAdapter {
                void processCheckedBillFragment(String edong, String code, BillEntityAdapter bill, int posCustomer);
            }
        }
    }
}
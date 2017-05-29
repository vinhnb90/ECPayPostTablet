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
import views.ecpay.com.postabletecpay.R;
import views.ecpay.com.postabletecpay.util.commons.Common;

import static views.ecpay.com.postabletecpay.model.adapter.PayAdapter.PayEntityAdapter.TT_CON_NO;
import static views.ecpay.com.postabletecpay.model.adapter.PayAdapter.PayEntityAdapter.TT_HET_NO;

/**
 * Created by VinhNB on 5/26/2017.
 */

public class PayAdapter extends RecyclerView.Adapter<PayAdapter.PayViewHolder> {
    private static Context sContext;
    private List<PayEntityAdapter> mAdapterList = new ArrayList<>();

    final int sdk = android.os.Build.VERSION.SDK_INT;
    @BindDrawable(R.drawable.bg_button_orange)
    Drawable green;
    @BindDrawable(R.drawable.bg_button)
    Drawable violet;
    @BindView(R.id.rvHoaDon)
    RecyclerView rvBill;
    @BindView(R.id.btn_row_thanhtoan_recycler_print)
    Button btnPrintBill;

//    private int width, height;

    /*public PayAdapter(Context sContext, List<PayEntityAdapter> mAdapterList, int width, int height) {
        this.sContext = sContext;
        this.mAdapterList = mAdapterList;
        this.height = height;
        this.width = width;
    }
*/
    public PayAdapter(Context sContext, List<PayEntityAdapter> mAdapterList) {
        this.sContext = sContext;
        this.mAdapterList = mAdapterList;
    }

    @Override
    public PayViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_thanhtoan_recycler, parent, false);
        ButterKnife.bind(this, view);

        return new PayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PayViewHolder holder, int position) {
        PayEntityAdapter entityAdapter = mAdapterList.get(position);

        holder.tvTenKH.setText(entityAdapter.getTenKH());
        holder.tvDiaChi.setText(entityAdapter.getDiaChi());
        holder.tvLoTrinh.setText(entityAdapter.getLoTrinh());
        holder.tvMaKH.setText(entityAdapter.getMaKH());
        holder.tvTongTien.setText(entityAdapter.getTongTien() + "");

        boolean isNo = (entityAdapter.getTrangThaiNo() == 0) ? true : false;

        holder.btnTrangThaiNo.setText(isNo ? TT_CON_NO : TT_HET_NO);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            holder.btnTrangThaiNo.setBackground(isNo ? green : violet);
        } else {
            holder.btnTrangThaiNo.setBackgroundDrawable(isNo ? green : violet);
        }

        Common.runAnimationClickViewScale(holder.cardView, R.anim.twinking_view, Common.TIME_DELAY_ANIM);
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
//        CardView cardView;

        private BillInsidePayAdapter insideTodayAdapter;

        public PayViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            rvBill.setLayoutManager(new LinearLayoutManager(sContext, LinearLayoutManager.VERTICAL, false));
            //set 10 rows and a page
//            cardView.getLayoutParams().height = (int) (height / PayFragment.ROWS_ON_PAGE);

        }
    }

    public static class PayEntityAdapter {
        public final static String TT_HET_NO = "Hết nợ";
        public final static String TT_CON_NO = "Còn nợ";

        private String billID;
        private String tenKH;
        private String diaChi;
        private String loTrinh;
        private String maKH;
        private long tongTien;
        private int trangThaiNo;

        public PayEntityAdapter(String billID, String tenKH, String diaChi, String loTrinh, String maKH, long tongTien, int trangThaiNo) {
            this.billID = billID;
            this.tenKH = tenKH;
            this.diaChi = diaChi;
            this.loTrinh = loTrinh;
            this.maKH = maKH;
            this.tongTien = tongTien;
            this.trangThaiNo = trangThaiNo;
        }

        public static String getTtHetNo() {
            return TT_HET_NO;
        }

        public static String getTtConNo() {
            return TT_CON_NO;
        }

        public String getBillID() {
            return billID;
        }

        public void setBillID(String billID) {
            this.billID = billID;
        }

        public String getTenKH() {
            return tenKH;
        }

        public void setTenKH(String tenKH) {
            this.tenKH = tenKH;
        }

        public String getDiaChi() {
            return diaChi;
        }

        public void setDiaChi(String diaChi) {
            this.diaChi = diaChi;
        }

        public String getLoTrinh() {
            return loTrinh;
        }

        public void setLoTrinh(String loTrinh) {
            this.loTrinh = loTrinh;
        }

        public String getMaKH() {
            return maKH;
        }

        public void setMaKH(String maKH) {
            this.maKH = maKH;
        }

        public long getTongTien() {
            return tongTien;
        }

        public void setTongTien(long tongTien) {
            this.tongTien = tongTien;
        }

        public int getTrangThaiNo() {
            return trangThaiNo;
        }

        public void setTrangThaiNo(int trangThaiNo) {
            this.trangThaiNo = trangThaiNo;
        }
    }

    public static class BillEntityAdapter {
        private String monthBill;
        private double moneyBill;
        private boolean isPrint;
        private boolean isPay;

        public BillEntityAdapter(String monthBill, double moneyBill, boolean isPrint, boolean isPay) {
            this.monthBill = monthBill;
            this.moneyBill = moneyBill;
            this.isPrint = isPrint;
            this.isPay = isPay;
        }

        public String getMonthBill() {
            return monthBill;
        }

        public double getMoneyBill() {
            return moneyBill;
        }

        public boolean isPrint() {
            return isPrint;
        }

        public boolean isPay() {
            return isPay;
        }
    }

    public static class BillInsidePayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<BillEntityAdapter> billList;

        public BillInsidePayAdapter(List<BillEntityAdapter> billList) {
            if (billList == null)
                return;
            this.billList = new ArrayList<>();
            this.billList.addAll(billList);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View v1 = inflater.inflate(R.layout.row_bill_inside_pay, parent, false);
            BillInsidePayViewHolder viewHolder = new BillInsidePayViewHolder(v1);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

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
            }
        }
    }
}
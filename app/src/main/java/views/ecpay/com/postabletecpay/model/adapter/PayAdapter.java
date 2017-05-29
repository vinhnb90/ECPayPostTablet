package views.ecpay.com.postabletecpay.model.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import views.ecpay.com.postabletecpay.R;

import static views.ecpay.com.postabletecpay.model.adapter.PayAdapter.PayEntityAdapter.TT_CON_NO;
import static views.ecpay.com.postabletecpay.model.adapter.PayAdapter.PayEntityAdapter.TT_HET_NO;

/**
 * Created by VinhNB on 5/26/2017.
 */

public class PayAdapter extends RecyclerView.Adapter<PayAdapter.PayViewHolder> {
    private Context mContext;
    private List<PayEntityAdapter> mAdapterList = new ArrayList<>();
    final int sdk = android.os.Build.VERSION.SDK_INT;
    Drawable green, violet;

    public PayAdapter(Context mContext, List<PayEntityAdapter> mAdapterList) {
        this.mContext = mContext;
        this.mAdapterList = mAdapterList;
        green = ContextCompat.getDrawable(mContext, R.drawable.bg_button_orange);
        violet = ContextCompat.getDrawable(mContext, R.drawable.bg_button);
    }

    @Override
    public PayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_thanhtoan_recycler, null);
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

        holder.tvTenKH.setText(isNo ? TT_CON_NO : TT_HET_NO);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            holder.tvTenKH.setBackground(isNo ? green : violet);
        } else {
            holder.tvTenKH.setBackgroundDrawable(isNo ? green : violet);
        }
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


        public PayViewHolder(View itemView) {
            super(itemView);

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
}
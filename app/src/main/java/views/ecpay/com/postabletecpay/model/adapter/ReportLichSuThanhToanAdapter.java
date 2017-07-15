package views.ecpay.com.postabletecpay.model.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import views.ecpay.com.postabletecpay.R;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.util.entities.EntityKhachHang;
import views.ecpay.com.postabletecpay.util.entities.EntityLichSuThanhToan;
import views.ecpay.com.postabletecpay.view.BaoCao.IReportLichSuThanhToanView;

/**
 * Created by duydatpham on 7/15/17.
 */

public class ReportLichSuThanhToanAdapter extends RecyclerView.Adapter<ReportLichSuThanhToanAdapter.LichSuHolder> {

    IReportLichSuThanhToanView reportLichSuThanhToanView;


    List<LichSuThanhToanData> mItems;

    public ReportLichSuThanhToanAdapter(List<LichSuThanhToanData>  lst, IReportLichSuThanhToanView view)
    {
        mItems = lst;
        reportLichSuThanhToanView = view;
    }

    @Override
    public LichSuHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_baocao_lichsuthanhtoan, parent, false);

        return new LichSuHolder(view);
    }

    @Override
    public void onBindViewHolder(LichSuHolder holder, int position) {
        LichSuThanhToanData lichSuThanhToanData = mItems.get(position);

        holder.fillData(lichSuThanhToanData);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class LichSuHolder extends RecyclerView.ViewHolder
    {

        public TextView txtTenKH;
        public TextView txtMaKH;
        public RecyclerView rvLichSu;

        public LichSuHolder(View itemView) {
            super(itemView);


            txtTenKH = (TextView) itemView.findViewById(R.id.row_baocao_lichsu_tenKH);
            txtMaKH = (TextView) itemView.findViewById(R.id.row_baocao_lichsu_maKH);
            rvLichSu = (RecyclerView) itemView.findViewById(R.id.row_baocao_lichsu_list);

        }


        public void fillData(LichSuThanhToanData data)
        {
            txtTenKH.setText(data.getTEN_KHACH_HANG());
            txtMaKH.setText("(" + data.getMA_KHACH_HANG() + ")");

            RowLichSuAdapter adapter = new RowLichSuAdapter(data.getLichSu());
            rvLichSu.setAdapter(adapter);
            rvLichSu.setLayoutManager(new LinearLayoutManager(reportLichSuThanhToanView.getContextView()));
            rvLichSu.setHasFixedSize(true);
            rvLichSu.invalidate();

        }
    }


    public class RowLichSuAdapter extends RecyclerView.Adapter<ReportLichSuThanhToanAdapter.RowLichSuHolder> {

        private List<EntityLichSuThanhToan> mItemLichSu;


        public RowLichSuAdapter(List<EntityLichSuThanhToan> lst)
        {
            mItemLichSu = lst;
        }

        @Override
        public RowLichSuHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_lichsu_khachhang, parent, false);

            return new RowLichSuHolder(view);
        }

        @Override
        public void onBindViewHolder(RowLichSuHolder holder, int position) {
            holder.fillData(mItemLichSu.get(position));
        }

        @Override
        public int getItemCount() {
            return mItemLichSu.size();
        }
    }


    public class RowLichSuHolder extends RecyclerView.ViewHolder
    {

        public TextView tvNgay;
        public TextView tvTinhTrang;
        public TextView tv_fragment_thanh_toan_total_bills_money;

        public RowLichSuHolder(View itemView) {
            super(itemView);


            tvNgay = (TextView) itemView.findViewById(R.id.tvNgay);
            tvTinhTrang = (TextView) itemView.findViewById(R.id.tvTinhTrang);
            tv_fragment_thanh_toan_total_bills_money = (TextView) itemView.findViewById(R.id.tv_fragment_thanh_toan_total_bills_money);

        }


        public void fillData(EntityLichSuThanhToan data)
        {
            tvNgay.setText(Common.parse(data.getNGAY_PHAT_SINH(), Common.DATE_TIME_TYPE.ddMMyyyy.toString()));
            tv_fragment_thanh_toan_total_bills_money.setText(String.valueOf(data.getSO_TIEN_TTOAN()));
            tvTinhTrang.setText(data.getMA_GIAO_DICH());
        }
    }


    public static class LichSuThanhToanData
    {
        private List<EntityLichSuThanhToan> LichSu;
        private String MA_KHACH_HANG, TEN_KHACH_HANG;


        public LichSuThanhToanData(String maKH, String tenKH)
        {
            MA_KHACH_HANG = maKH;
            TEN_KHACH_HANG = tenKH;
            LichSu = new ArrayList<>();
        }

        public List<EntityLichSuThanhToan> getLichSu() {
            return LichSu;
        }

        public void setLichSu(List<EntityLichSuThanhToan> lichSu) {
            LichSu = lichSu;
        }

        public String getMA_KHACH_HANG() {
            return MA_KHACH_HANG;
        }

        public void setMA_KHACH_HANG(String MA_KHACH_HANG) {
            this.MA_KHACH_HANG = MA_KHACH_HANG;
        }

        public String getTEN_KHACH_HANG() {
            return TEN_KHACH_HANG;
        }

        public void setTEN_KHACH_HANG(String TEN_KHACH_HANG) {
            this.TEN_KHACH_HANG = TEN_KHACH_HANG;
        }
    }
}

package views.ecpay.com.postabletecpay.model.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import views.ecpay.com.postabletecpay.R;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.util.entities.EntityHoaDonThu;
import views.ecpay.com.postabletecpay.util.entities.sqlite.Bill;

/**
 * Created by duydatpham on 6/26/17.
 */

public class ReportHoanTraAdapter  extends RecyclerView.Adapter<ReportHoanTraAdapter.RowHoanTraHolder> {

    List<EntityHoaDonThu> mBills;


    public ReportHoanTraAdapter()
    {
        mBills = new ArrayList<>();
    }

    public List<EntityHoaDonThu> getmBills() {
        return mBills;
    }

    public void setmBills(List<EntityHoaDonThu> mBills) {
        this.mBills = mBills;
    }

    @Override
    public RowHoanTraHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_baocao_hoantra, parent, false);

        return new RowHoanTraHolder(view);
    }

    @Override
    public void onBindViewHolder(RowHoanTraHolder holder, int position) {
        EntityHoaDonThu bill = mBills.get(position);
        holder.tvTenKH.setText(bill.getTEN_KHANG());
        holder.tvMaKH.setText(bill.getMA_KHANG());
        holder.tv_fragment_thanh_toan_total_bills_money.setText(Common.convertLongToMoney(bill.getSO_TIEN_TTOAN()));
        holder.tvTinhTrang.setText(Common.TRANG_THAI_HOAN_TRA.CHUA_TRA.getMessage());
        holder.tvKy.setText(Common.parse(bill.getTHANG_TTOAN(), Common.DATE_TIME_TYPE.MMyyyy.toString()));


    }

    @Override
    public int getItemCount() {
        return mBills.size();
    }

    public class RowHoanTraHolder extends RecyclerView.ViewHolder
    {

        public TextView tvTenKH;
        public TextView tvMaKH;
        public TextView tvKy;
        public TextView tv_fragment_thanh_toan_total_bills_money;
        public TextView tvTinhTrang;

        public RowHoanTraHolder(View itemView) {
            super(itemView);

            tvTenKH = (TextView)itemView.findViewById(R.id.tvTenKH);
            tvMaKH = (TextView)itemView.findViewById(R.id.tvMaKH);
            tvKy = (TextView)itemView.findViewById(R.id.tvKy);
            tv_fragment_thanh_toan_total_bills_money = (TextView)itemView.findViewById(R.id.tv_fragment_thanh_toan_total_bills_money);
            tvTinhTrang = (TextView)itemView.findViewById(R.id.tvTinhTrang);

        }
    }
}

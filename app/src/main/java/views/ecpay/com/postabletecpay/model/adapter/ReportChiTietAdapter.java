package views.ecpay.com.postabletecpay.model.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import views.ecpay.com.postabletecpay.R;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.util.entities.sqlite.Bill;

/**
 * Created by duydatpham on 6/25/17.
 */

public class ReportChiTietAdapter extends RecyclerView.Adapter<ReportChiTietAdapter.RowChiTietHolder> {

    List<Bill> mBills;


    public ReportChiTietAdapter()
    {
        mBills = new ArrayList<>();
    }

    public List<Bill> getmBills() {
        return mBills;
    }

    public void setmBills(List<Bill> mBills) {
        this.mBills = mBills;
    }

    @Override
    public RowChiTietHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_baocao_chitiet, parent, false);

        return new RowChiTietHolder(view);
    }

    @Override
    public void onBindViewHolder(RowChiTietHolder holder, int position) {
        Bill bill = mBills.get(position);
        holder.tvTenKH.setText(bill.getName());
        holder.tvMaKH.setText(bill.getCustomerCode());
        holder.tv_fragment_thanh_toan_total_bills_money.setText(Common.convertLongToMoney(bill.getAmount()));
        holder.tvNgay.setText(bill.getRequestDate());
        if(bill.getTerm().length() != 0)
        {
            holder.tvKy.setText(bill.getTerm().substring(4, 6) + "/" + bill.getTerm().substring(0, 4));
        }else
        {
            holder.tvKy.setText("");
        }


    }

    @Override
    public int getItemCount() {
        return mBills.size();
    }

    public class RowChiTietHolder extends RecyclerView.ViewHolder
    {
        public TextView tvTenKH;
        public TextView tvMaKH;
        public TextView tvKy;
        public TextView tvNgay;
        public TextView tv_fragment_thanh_toan_total_bills_money;

        public RowChiTietHolder(View itemView) {
            super(itemView);

            tvTenKH = (TextView)itemView.findViewById(R.id.tvTenKH);
            tvMaKH = (TextView)itemView.findViewById(R.id.tvTenKH);
            tvKy = (TextView)itemView.findViewById(R.id.tvKy);
            tvNgay = (TextView)itemView.findViewById(R.id.tvNgay);
            tv_fragment_thanh_toan_total_bills_money = (TextView)itemView.findViewById(R.id.tv_fragment_thanh_toan_total_bills_money);


        }
    }

}

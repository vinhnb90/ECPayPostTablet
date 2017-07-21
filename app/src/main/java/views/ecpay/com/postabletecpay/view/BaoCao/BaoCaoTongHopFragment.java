package views.ecpay.com.postabletecpay.view.BaoCao;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import views.ecpay.com.postabletecpay.Config.Config;
import views.ecpay.com.postabletecpay.R;
import views.ecpay.com.postabletecpay.presenter.IReportTongHopPresenter;
import views.ecpay.com.postabletecpay.presenter.ReportTongHopPresenter;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.util.entities.sqlite.Account;
import views.ecpay.com.postabletecpay.view.ICommonView;
import views.ecpay.com.postabletecpay.view.Main.MainActivity;
import views.ecpay.com.postabletecpay.view.Printer.Printer;

/**
 * Created by macbook on 4/30/17.
 */

public class BaoCaoTongHopFragment extends Fragment implements View.OnClickListener, IReportTongHopView{

    @Nullable
    @BindView(R.id.tvNgayIn)
    TextView tvNgayIn;
    @Nullable
    @BindView(R.id.tvThuNgan)
    TextView tvThuNgan;
    @Nullable
    @BindView(R.id.tvSHDGiao)
    TextView tvSHDGiao;
    @Nullable
    @BindView(R.id.tvSTGiao)
    TextView tvSTGiao;
    @Nullable
    @BindView(R.id.tvSHDThu)
    TextView tvSHDThu;
    @Nullable
    @BindView(R.id.tvSTThu)
    TextView tvSTThu;
    @Nullable
    @BindView(R.id.tvSHDTon)
    TextView tvSHDTon;
    @Nullable
    @BindView(R.id.tvSTTon)
    TextView tvSTTon;
    @Nullable
    @BindView(R.id.tvSHDThuVangLai)
    TextView tvSHDThuVangLai;
    @Nullable
    @BindView(R.id.tvSTThuVangLai)
    TextView tvSTThuVangLai;
    @Nullable
    @BindView(R.id.tvSHDHoanTra)
    TextView tvSHDHoanTra;
    @Nullable
    @BindView(R.id.tvSTHoanTra)
    TextView tvSTHoanTra;
    @Nullable
    @BindView(R.id.btInBaoCao)
    Button btInBaoCao;

    Unbinder unbinder;

    Account account;
    int hdGiao, hdThu,hdVangLai,hdTraKH;
    long tienGiao, tienThu,tienVangLai,tienTraKHt;


    private IReportTongHopPresenter reportTongHopPresenter;


    IBaoCaoView baoCaoView;

    public static BaoCaoTongHopFragment newInstance(IBaoCaoView view)
    {
        BaoCaoTongHopFragment fragment = new BaoCaoTongHopFragment();
        fragment.baoCaoView = view;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_baocao_tonghop, container, false);
        unbinder = ButterKnife.bind(this, view);

        reportTongHopPresenter = new ReportTongHopPresenter(this);

        btInBaoCao.setOnClickListener(this);

        reportTongHopPresenter.fill();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onStart() {
        super.onStart();
        this.baoCaoView.showBackBtn(false);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btInBaoCao){
            Printer printer = new Printer(getActivity(),Printer.BAO_CAO,account,hdGiao,tienGiao,hdThu,tienThu,hdVangLai,tienVangLai,hdTraKH,tienTraKHt);
            printer.Action();
        }
    }

    @Override
    public void fill(Account account, int hdGiao, long tienGiao, int hdThu, long tienThu, int hdVangLai, long tienVangLai, int hdTraKH, long tienTraKHt) {
        this.account = account;
        this.hdGiao = hdGiao;
        this.tienGiao =  tienGiao;
        this.hdThu = hdThu;
        this.tienThu = tienThu;
        this.hdVangLai = hdVangLai;
        this.tienVangLai = tienVangLai;
        this.hdTraKH = hdTraKH;
        this.tienTraKHt = tienTraKHt;

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        tvNgayIn.setText(dateFormat.format(new Date()));

        tvThuNgan.setText(account.getName());


        tvSHDGiao.setText(String.valueOf(hdGiao));
        tvSTGiao.setText(Common.convertLongToMoney(tienGiao));

        tvSHDThu.setText(String.valueOf(hdThu));
        tvSTThu.setText(Common.convertLongToMoney(tienThu));

        tvSHDThuVangLai.setText(String.valueOf(hdVangLai));
        tvSTThuVangLai.setText(Common.convertLongToMoney(tienVangLai));

        tvSHDHoanTra.setText(String.valueOf(hdTraKH));
        tvSTHoanTra.setText(Common.convertLongToMoney(tienTraKHt));


        tvSHDTon.setText(String.valueOf(hdGiao - (hdThu - hdVangLai)));

        tvSTTon.setText(Common.convertLongToMoney(tienGiao - (tienThu - tienVangLai)));

    }


    @Override
    public void showRespone(String code, String description) {
        if(!Config.isShowRespone())
            return;

        try {
            Toast.makeText(this.getContext(), "CODE: " + code, Toast.LENGTH_LONG).show();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    @Override
    public Context getContextView() {
        return this.getContext();
    }
}

package ir.sadeghlabs.turn.activity;

import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.flyco.animation.BaseAnimatorSet;
import com.flyco.animation.BounceEnter.BounceTopEnter;
import com.flyco.animation.SlideExit.SlideBottomExit;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.MaterialDialog;

import org.berans.view.BRtextview;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ir.sadeghlabs.turn.R;
import ir.sadeghlabs.turn.model.TurnModel;
import ir.sadeghlabs.turn.tools.PersianCalendar;

public class CancelTurnActivity extends AppCompatActivity {
    List<TurnModel> turnModelList = new ArrayList<>();
    private ListViewAdapter adapter;
    private BaseAnimatorSet bas_in;
    private BaseAnimatorSet bas_out;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_cancel_turn);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        populatingData();
        initListView();
        bas_in = new BounceTopEnter();
        bas_out = new SlideBottomExit();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    /**
     * populating data
     */
    private void populatingData() {
        for (int i = 0; i < 10; i++){
            TurnModel turnModel = new TurnModel();

            Calendar calendar = Calendar.getInstance();

            turnModel.setTurnId(i);
            turnModel.setTurnServerId(i);
            turnModel.setDoctorId(i);
            turnModel.setDoctorName("دکتر هادی شاه مرادی");
            turnModel.setSpecialityId(i);
            turnModel.setSpecialityName("فوق تخصص گوش و حلق و بینی");
            turnModel.setTurnDateTime(calendar.getTimeInMillis());
            turnModel.setStatus("در انتظار بیمار");

            turnModelList.add(turnModel);
        }
    }

    /**
     * init list view
     */
    private void initListView() {
        ListView lstCancelTurn = (ListView) findViewById(R.id.lstCancelTurn);

        adapter = new ListViewAdapter();

        lstCancelTurn.setAdapter(adapter);
    }


    /**
     * create list view adapter
     * */
    private class ListViewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return turnModelList.size();
        }

        @Override
        public Object getItem(int position) {
            return turnModelList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;

            if(convertView == null){
                LayoutInflater inflater = getLayoutInflater();
                view = inflater.inflate(R.layout.cancel_turn_list_view_item, null);
            }else {
                view = convertView;
            }


            TurnModel turnModel = (TurnModel) turnModelList.get(position);

            BRtextview lblDoctorName = (BRtextview) view.findViewById(R.id.lblDoctorName);
            BRtextview lblSpecialityName = (BRtextview) view.findViewById(R.id.lblSpecialityName);
            BRtextview lblTurnDate = (BRtextview) view.findViewById(R.id.lblTurnDate);
            BRtextview lblTurnTime = (BRtextview) view.findViewById(R.id.lblTurnTime);
            BRtextview lblTurnStatus = (BRtextview) view.findViewById(R.id.lblTurnStatus);
            ImageView btnCancelTurn = (ImageView) view.findViewById(R.id.btnCancelTurn);

            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(turnModel.getTurnDateTime());
            String time = dateFormat.format(calendar.getTime());
            PersianCalendar persianCalendar = new PersianCalendar(calendar.getTime());

            lblDoctorName.setText(turnModel.getDoctorName());
            lblSpecialityName.setText(turnModel.getSpecialityName());
            lblTurnDate.setText(persianCalendar.getIranianDate());
            lblTurnTime.setText(time);
            lblTurnStatus.setText(turnModel.getStatus());

            btnCancelTurn.setOnClickListener(new CancelButtonCLick(turnModel.getTurnId()));


            return view;
        }
    }

    /**
     * handle cancel button click
     * */
    private class CancelButtonCLick implements View.OnClickListener{
        private int turnId;

        public CancelButtonCLick(int turnId) {
            this.turnId = turnId;
        }

        @Override
        public void onClick(View v) {
            showConfirmDialog(turnId);
        }
    }

    public void setBasIn(BaseAnimatorSet bas_in) {
        this.bas_in = bas_in;
    }

    public void setBasOut(BaseAnimatorSet bas_out) {
        this.bas_out = bas_out;
    }

    private void showConfirmDialog(final int turnId) {
        final MaterialDialog dialog = new MaterialDialog(this);
        dialog.content("آیا برای حذف اطمینان دارید؟").title("پیام سیستم")
                .btnText("انصراف", "تایید")//
                .showAnim(bas_in)//
                .dismissAnim(bas_out)
                .show();

        dialog.setOnBtnClickL(
                new OnBtnClickL() {//left btn click listener
                    @Override
                    public void onBtnClick() {
                        dialog.dismiss();
                    }
                },
                new OnBtnClickL() {//right btn click listener
                    @Override
                    public void onBtnClick() {
                        turnModelList.remove(turnId);
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                }
        );
    }
}

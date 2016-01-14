package ir.sadeghlabs.turn.activity;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ListView;

import org.berans.view.BRtextview;

import java.util.ArrayList;
import java.util.List;

import ir.sadeghlabs.turn.R;
import ir.sadeghlabs.turn.entity.Turn;
import ir.sadeghlabs.turn.management.TurnManagement;
import ir.sadeghlabs.turn.model.Constant;

public class GettingTurnActivity extends AppCompatActivity {
    private List<Turn> turnModelList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_getting_turn);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        populatingData();
        initListView();

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
        TurnManagement turnManagement = new TurnManagement(GettingTurnActivity.this, Constant.DATABASE_NAME, Constant.DATABASE_VERSION);

        turnModelList = turnManagement.getTurnList();
    }

    /**
     * init list view
     */
    private void initListView() {
        ListView lstGettingTurn = (ListView) findViewById(R.id.lstGettingTurn);

        ListViewAdapter adapter = new ListViewAdapter();

        lstGettingTurn.setAdapter(adapter);
    }


    /**
     * create list view adapter
     * */
    private class ListViewAdapter extends BaseAdapter{

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
                view = inflater.inflate(R.layout.getting_turn_list_view_item, null);
            }else {
                view = convertView;
            }


            Turn turnModel = (Turn) turnModelList.get(position);

            BRtextview lblDoctorName = (BRtextview) view.findViewById(R.id.lblDoctorName);
            BRtextview lblSpecialityName = (BRtextview) view.findViewById(R.id.lblSpecialityName);
            BRtextview lblTurnDate = (BRtextview) view.findViewById(R.id.lblTurnDate);
            BRtextview lblTurnTime = (BRtextview) view.findViewById(R.id.lblTurnTime);
            BRtextview lblTurnStatus = (BRtextview) view.findViewById(R.id.lblTurnStatus);

            lblDoctorName.setText(turnModel.getDoctorName());
            lblSpecialityName.setText(turnModel.getSpecialityName());
            lblTurnDate.setText(turnModel.getTurnDate());
            lblTurnTime.setText(turnModel.getTurnTime());

            if(turnModel.isStatus() == 1){
                lblTurnStatus.setText("در انتظار بیمار");
            }else {
                lblTurnStatus.setText("عدم مراجعه بیمار");
            }



            return view;
        }
    }
}

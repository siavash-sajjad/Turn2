package ir.sadeghlabs.turn.activity;

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
import android.widget.ListView;

import org.berans.view.BRtextview;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ir.sadeghlabs.turn.R;
import ir.sadeghlabs.turn.model.MessageModel;
import ir.sadeghlabs.turn.model.TurnModel;
import ir.sadeghlabs.turn.tools.PersianCalendar;

public class MessageActivity extends AppCompatActivity {
    private List<MessageModel> messageModelList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_message);
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
        for (int i = 0; i < 10; i++){
            MessageModel messageModel = new MessageModel();

            Calendar calendar = Calendar.getInstance();

            messageModel.setMessageId(i);
            messageModel.setTitle("خوش آمدید");
            messageModel.setMessageText("به سامانه نوبت دهی دکترها خوش آمدید.امیدواریم بتوانیم به صورت روزافزون در کنار شما خدمت رسانی کنیم");
            messageModel.setMessageDateTime(calendar.getTimeInMillis());
            messageModel.setEnable(true);

            messageModelList.add(messageModel);
        }
    }

    /**
     * init list view
     */
    private void initListView() {
        ListView lstGettingTurn = (ListView) findViewById(R.id.lstMessage);

        ListViewAdapter adapter = new ListViewAdapter();

        lstGettingTurn.setAdapter(adapter);
    }


    /**
     * create list view adapter
     * */
    private class ListViewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return messageModelList.size();
        }

        @Override
        public Object getItem(int position) {
            return messageModelList.get(position);
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
                view = inflater.inflate(R.layout.message_list_view_item, null);
            }else {
                view = convertView;
            }


            MessageModel messageModel = messageModelList.get(position);

            BRtextview lblMessageTitle = (BRtextview) view.findViewById(R.id.lblMessageTitle);
            BRtextview lblMessageText = (BRtextview) view.findViewById(R.id.lblMessageText);
            BRtextview lblTurnDate = (BRtextview) view.findViewById(R.id.lblTurnDate);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(messageModel.getMessageDateTime());
            PersianCalendar persianCalendar = new PersianCalendar(calendar.getTime());

            lblMessageTitle.setText(messageModel.getTitle());
            lblMessageText.setText(messageModel.getMessageText());
            lblTurnDate.setText(persianCalendar.getIranianDate());


            return view;
        }
    }
}

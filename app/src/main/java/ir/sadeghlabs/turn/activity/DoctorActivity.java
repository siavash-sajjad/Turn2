package ir.sadeghlabs.turn.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.flyco.dialog.widget.MaterialDialog;
import com.squareup.picasso.Picasso;

import org.apache.http.message.BasicNameValuePair;
import org.berans.view.BRbutton;
import org.berans.view.BRedittext;
import org.berans.view.BRtextview;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ir.sadeghlabs.connectionhandler.ConnectionHandler;
import ir.sadeghlabs.turn.R;
import ir.sadeghlabs.turn.model.Constant;
import ir.sadeghlabs.turn.model.DoctorModel;
import ir.sadeghlabs.turn.tools.PersianCalendar;

public class DoctorActivity extends AppCompatActivity {
    private AlertDialog.Builder selectedDoctorDialog;
    private List<DoctorModel> doctorModelList = new ArrayList<>();
    public static int doctorId;


    @Override
    protected void onResume() {
        super.onResume();
        selectedDoctorDialog = new AlertDialog.Builder(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        doctorModelList = (List<DoctorModel>) getIntent().getSerializableExtra(Constant.DOCTOR_BUNDLE);


        initListView();
        initTextView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    /**
     * init text view
     */
    private void initTextView() {
        TextView lblCount = (TextView) findViewById(R.id.lblCount);
        lblCount.setText(doctorModelList.size() + " مورد یافت شد");
    }

    /**
     * init list view
     */
    private void initListView() {
        ListView lstDoctor = (ListView) findViewById(R.id.lstDoctor);

        ListViewAdapter adapter = new ListViewAdapter();
        lstDoctor.setAdapter(adapter);
        lstDoctor.setOnItemClickListener(new ListViewItemClick());
    }


    /**
     * create list view adapter
     */
    private class ListViewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return doctorModelList.size();
        }

        @Override
        public Object getItem(int position) {
            return doctorModelList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                LayoutInflater inflater = getLayoutInflater();
                view = inflater.inflate(R.layout.doctor_list_view_item, null);
            } else {
                view = convertView;
            }


            DoctorModel doctorModel = doctorModelList.get(position);

            BRtextview lblDoctorName = (BRtextview) view.findViewById(R.id.lblDoctorName);
            BRtextview lblDateTime = (BRtextview) view.findViewById(R.id.lblDateTime);
            CircleImageView profile_image = (CircleImageView) view.findViewById(R.id.profile_image);

            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
            Calendar calendar = Calendar.getInstance();
            String time = dateFormat.format(calendar.getTime());
            PersianCalendar persianCalendar = new PersianCalendar(calendar.getTime());

            lblDoctorName.setText(doctorModel.getDoctorName());
            lblDateTime.setText("تاریخ: " + persianCalendar.getIranianDate());

            Picasso.with(DoctorActivity.this)
                    .load(doctorModel.getImageAddress())
                    .error(R.mipmap.ic_medical88)
                    .into(profile_image);


            return view;
        }
    }

    private class ListViewItemClick implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            DoctorModel doctorModel = doctorModelList.get(position);

            doctorId = doctorModel.getDoctorId();

            sendDataToServer(doctorModel);
            //showSelectedDoctorDialog();
        }
    }

    private void sendDataToServer(final DoctorModel doctorModel) {
        int selectedDoctorId = doctorModel.getDoctorId();

        List nameValuePairs = new ArrayList();
        nameValuePairs.add(new BasicNameValuePair("BimarestanCode_Fk", String.valueOf(MainActivity.hotpitalCode)));
        nameValuePairs.add(new BasicNameValuePair("ShomarNobat", String.valueOf(selectedDoctorId)));
        nameValuePairs.add(new BasicNameValuePair("ClinicCode_Fk", String.valueOf(MainActivity.clinicCode)));
        nameValuePairs.add(new BasicNameValuePair("InsuranceCode_Fk", String.valueOf(MainActivity.insuranceCode)));

        new ConnectionHandler(DoctorActivity.this, true, Constant.ExistNobat, ir.sadeghlabs.connectionhandler.Constant.method.POST, "در حال ارسال اطلاعات...", false, nameValuePairs, new ConnectionHandler.CommunicatorListener() {
            @Override
            public void onNoInternetConnection() {
                Toast.makeText(DoctorActivity.this, "عدم اتصال به اینترنت", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onPreExecute() {

            }

            @Override
            public void onDone(String result) {
                try {
                    JSONArray resultJsonArray = ConvertToJSONArray(result);
                    JSONObject modelJsonObject = resultJsonArray.getJSONObject(0);

                    int status = modelJsonObject.getInt("status");

                    if (status == Constant.EXIST_TURN) {
                        showSelectedDoctorDialog(doctorModel, modelJsonObject);
                    } else {
                        String message = modelJsonObject.getString("desciption");
                        showNotTurnIsExist(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled() {
                Toast.makeText(DoctorActivity.this, "قطع ارتباط توسط کاربر", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCodeError() {
                Toast.makeText(DoctorActivity.this, "خطا در کدهای اجرایی", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onServerError(Exception e) {
                Toast.makeText(DoctorActivity.this, "خطا در کدهای اجرایی سرور", Toast.LENGTH_LONG).show();
            }
        }).execute();
    }

    JSONArray ConvertToJSONArray(String result) {
        try {
            return new JSONArray(result.substring(result.indexOf("["), result.lastIndexOf("]") + 1));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void showNotTurnIsExist(String message) {
        final MaterialDialog dialog = new MaterialDialog(DoctorActivity.this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.content(message).title("پیام سیستم").btnNum(1).btnText("تایید").show();
    }

    private void showSelectedDoctorDialog(DoctorModel doctorModel, JSONObject modelJsonObject) {
        try {
            LayoutInflater inflater = getLayoutInflater();
            final View selectedDoctorDialogLayout = inflater.inflate(R.layout.selected_doctor_dialog_layout, null);

            BRbutton btnOk = (BRbutton) selectedDoctorDialogLayout.findViewById(R.id.btnOk);
            BRbutton btnCancel = (BRbutton) selectedDoctorDialogLayout.findViewById(R.id.btnCancel);

            BRtextview lblDoctorName = (BRtextview) selectedDoctorDialogLayout.findViewById(R.id.lblDoctorName);
            BRtextview lblDateTime = (BRtextview) selectedDoctorDialogLayout.findViewById(R.id.lblDateTime);
            BRtextview lblDate = (BRtextview) selectedDoctorDialogLayout.findViewById(R.id.lblDate);
            BRtextview lblTime = (BRtextview) selectedDoctorDialogLayout.findViewById(R.id.lblTime);
            BRtextview lblTurn = (BRtextview) selectedDoctorDialogLayout.findViewById(R.id.lblTurn);
            BRtextview lblPrice = (BRtextview) selectedDoctorDialogLayout.findViewById(R.id.lblPrice);

            final BRedittext txtNationalCode = (BRedittext) selectedDoctorDialogLayout.findViewById(R.id.txtNationalCode);

            CircleImageView profile_image = (CircleImageView) selectedDoctorDialogLayout.findViewById(R.id.profile_image);


            String price = modelJsonObject.getString("price").replace("~","");
            String date = modelJsonObject.getString("date");
            String time = modelJsonObject.getString("time");

            lblDoctorName.setText(doctorModel.getDoctorName());
            lblPrice.setText("مبلغ قابل پرداخت: " +price);
            lblDateTime.setText(MainActivity.specialtyModel.getSpecialtyName());
            lblDate.setText("تاریخ مراجعه: " + date);
            lblTime.setText(" ساعت مراجعه: " + time);
            lblTurn.setText("نوبت شما: 25");

            Picasso.with(DoctorActivity.this)
                    .load(doctorModel.getImageAddress())
                    .error(R.mipmap.ic_medical88)
                    .into(profile_image);


            selectedDoctorDialog.setView(selectedDoctorDialogLayout);

            final AlertDialog alertDialog = selectedDoctorDialog.create();
            alertDialog.setCancelable(false);
            alertDialog.show();


            selectedDoctorDialog.setOnKeyListener(new Dialog.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        dialogInterface.dismiss();
                    }
                    return true;
                }
            });

            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (txtNationalCode.getText().toString().matches("")) {
                        Toast.makeText(DoctorActivity.this, "کدملی بیمار را وارد کنید", Toast.LENGTH_LONG).show();
                        txtNationalCode.requestFocus();
                    } else if (!checkPersonalNationalCode(txtNationalCode.getText().toString())) {
                        Toast.makeText(DoctorActivity.this, "کد ملی نادرست است",Toast.LENGTH_LONG).show();
                        txtNationalCode.requestFocus();
                    } else {
                        alertDialog.dismiss();
                        Intent baseInformationActivity = new Intent(DoctorActivity.this, BaseInformationActivity.class);
                        baseInformationActivity.putExtra(Constant.NATIONAL_CODE, txtNationalCode.getText().toString());
                        startActivity(baseInformationActivity);
                    }
                }
            });

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    private boolean checkNationalCode(String nationalCode) {
        boolean isTrue = false;

        if (nationalCode.length() != 10) {
            isTrue = false;
        } else if (nationalCode.equals("1111111111") || nationalCode.equals("1111111111")
                || nationalCode.equals("2222222222") || nationalCode.equals("3333333333")
                || nationalCode.equals("4444444444") || nationalCode.equals("5555555555")
                || nationalCode.equals("6666666666") || nationalCode.equals("7777777777")
                || nationalCode.equals("8888888888") || nationalCode.equals("9999999999")) {
            isTrue = false;
        } else {
            int C = nationalCode.toCharArray()[9];
            int N = (nationalCode.toCharArray()[0] * 10) +
                    (nationalCode.toCharArray()[1] * 9) +
                    (nationalCode.toCharArray()[2] * 8) +
                    (nationalCode.toCharArray()[3] * 7) +
                    (nationalCode.toCharArray()[4] * 6) +
                    (nationalCode.toCharArray()[5] * 5) +
                    (nationalCode.toCharArray()[6] * 4) +
                    (nationalCode.toCharArray()[7] * 3) +
                    (nationalCode.toCharArray()[8] * 2);

            int R = N - N / 11 * 11;

            if ((R == 0 && R == C) || (R == 1 && C == 1) || (R > 1 && C == 11 - R)) {
                isTrue = true;
            } else {
                isTrue = false;
            }
        }

        return isTrue;
    }

    public static boolean checkLegalNationalCode(String nationalCode) {

        if (nationalCode.length() < 11 || Integer.parseInt(nationalCode) == 0)
            return false;

        if (Integer.parseInt(nationalCode.substring(3, 9)) == 0)
            return false;

        int c = Integer.parseInt(nationalCode.substring(10, 11));
        int d = Integer.parseInt(nationalCode.substring(9, 10)) + 2;
        int[] z = new int[] { 29, 27, 23, 19, 17 };
        int s = 0;

        for (byte i = 0; i < 10; i++)
            s += (d + Integer.parseInt(nationalCode.substring(i, i + 1))) * z[i % 5];

        s = s % 11;

        if (s == 10)
            s = 0;

        return (c == s);
    }

    public static boolean checkPersonalNationalCode(String nationalCode) {
        boolean retVal = false;
        float c, n, r;

        if (!(nationalCode.length() < 10 ||
                nationalCode == "0000000000" ||
                nationalCode == "1111111111" ||
                nationalCode == "2222222222" ||
                nationalCode == "3333333333" ||
                nationalCode == "4444444444" ||
                nationalCode == "5555555555" ||
                nationalCode == "6666666666" ||
                nationalCode == "7777777777" ||
                nationalCode == "8888888888" ||
                nationalCode == "9999999999"))
        {
            c = Float.valueOf(nationalCode.substring(9, 10));

            n = Integer.valueOf(nationalCode.substring(0, 1)) * 10 +
                    Integer.valueOf(nationalCode.substring(1, 2)) * 9 +
                    Integer.valueOf(nationalCode.substring(2, 3)) * 8 +
                    Integer.valueOf(nationalCode.substring(3, 4)) * 7 +
                    Integer.valueOf(nationalCode.substring(4, 5)) * 6 +
                    Integer.valueOf(nationalCode.substring(5, 6)) * 5 +
                    Integer.valueOf(nationalCode.substring(6, 7)) * 4 +
                    Integer.valueOf(nationalCode.substring(7, 8)) * 3 +
                    Integer.valueOf(nationalCode.substring(8, 9)) * 2;

            r = (n - ((int)(n / 11) * 11));

            if ((r == 0 && r == c) || (r == 1 && c == 1) || (r > 1 && c == (11 - r)))
                retVal = true;
        }
        return retVal;
    }
}

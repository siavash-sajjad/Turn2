package ir.sadeghlabs.turn.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.flyco.dialog.widget.MaterialDialog;

import org.berans.view.BRbutton;
import org.berans.view.BRtextview;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ir.sadeghlabs.turn.R;
import ir.sadeghlabs.turn.entity.Turn;
import ir.sadeghlabs.turn.management.TurnManagement;
import ir.sadeghlabs.turn.model.CityModel;
import ir.sadeghlabs.turn.model.ClinicModel;
import ir.sadeghlabs.turn.model.Constant;
import ir.sadeghlabs.turn.model.DoctorModel;
import ir.sadeghlabs.turn.model.HospitalModel;
import ir.sadeghlabs.turn.model.HospitalTypeModel;
import ir.sadeghlabs.turn.model.InsuranceTypeModel;
import ir.sadeghlabs.turn.model.SpecialtyModel;
import ir.sadeghlabs.turn.model.StateModel;
import ir.sadeghlabs.turn.tools.PersianCalendar;
import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity {
    private AlertDialog searchDoctorAlertDialog;
    private AlertDialog searchLoadingAlertDialog;
    private List<StateModel> stateModelList = new ArrayList<>();
    private ProgressDialog pDialog;
    private Spinner cmbSpeciality;
    public static int hotpitalCode;
    public static int insuranceCode;
    public static int clinicCode;
    public static SpecialtyModel specialtyModel;
    private Spinner cmbHospital;
    private Spinner cmbTime;
    private Spinner cmbInsuranceType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        if (android.os.Build.VERSION.SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }


    public void btnGettingTurnClick(View view) {
        Intent gettingTurnActivity = new Intent(MainActivity.this, GettingTurnActivity.class);
        startActivity(gettingTurnActivity);
    }

    public void btnMessageClick(View view) {
        Intent messageActivity = new Intent(MainActivity.this, MessageActivity.class);
        startActivity(messageActivity);
    }

    public void btnCancelTurnClick(View view) {
        Intent cancelTurnActivity = new Intent(MainActivity.this, CancelTurnActivity.class);
        startActivity(cancelTurnActivity);
    }

    public void btnGetTurnClick(View view) {
        connectToService();
    }

    public void btnSettingClick(View view){
/*        final MaterialDialog dialog = new MaterialDialog(MainActivity.this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.content("این قسمت در حال بروزرسانی است.").title("پیام سیستم").btnNum(1).btnText("تایید").show();*/

        Intent testActivity = new Intent(MainActivity.this,TestActivity.class);
        startActivity(testActivity);
    }

    public void btnAboutClick(View view){
        Intent aboutActivity = new Intent(MainActivity.this, AboutActivity.class);
        startActivity(aboutActivity);
    }


    ////////////////////////////////////////////////////
    //
    //  SHOW SEARCH DIALOG
    //
    ///////////////////////////////////////////////////
    private void connectToService() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("برقراری ارتباط با سرور...");
        pDialog.setCancelable(true);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.ShowStates,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject jsonObject = ConvertToJSON(response);

                        CreateModel(jsonObject);

                        showSearchDoctorDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NoConnectionError) {
                    Toast.makeText(MainActivity.this, "عدم دسترسی به اینترنت", Toast.LENGTH_LONG).show();
                }
                pDialog.dismiss();
            }
        });

        Volley.newRequestQueue(this).add(stringRequest);
    }

    JSONObject ConvertToJSON(String result) {
        try {
            return new JSONObject(result.substring(result.indexOf("{"), result.lastIndexOf("}") + 1));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void showSearchDoctorDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View searchDoctorLayout = inflater.inflate(R.layout.search_doctor_dialog_layout, null);

        initSpinner(searchDoctorLayout);

        BRbutton btnSearchDoctor = (BRbutton) searchDoctorLayout.findViewById(R.id.btnSearchDoctor);
        btnSearchDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSearchDoctorClick();
            }
        });

        AlertDialog.Builder searchDoctorDialog = new AlertDialog.Builder(this);
        searchDoctorDialog.setView(searchDoctorLayout);

        searchDoctorAlertDialog = searchDoctorDialog.create();
        searchDoctorAlertDialog.setCancelable(false);
        searchDoctorAlertDialog.show();
        searchDoctorAlertDialog.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    searchDoctorAlertDialog.dismiss();
                }
                return true;
            }
        });
    }

    private void initSpinner(View view) {
        Spinner cmbState = (Spinner) view.findViewById(R.id.cmbState);
        final Spinner cmbCity = (Spinner) view.findViewById(R.id.cmbCity);
        cmbHospital = (Spinner) view.findViewById(R.id.cmbHospital);
        cmbSpeciality = (Spinner) view.findViewById(R.id.cmbSpeciality);
        cmbTime = (Spinner) view.findViewById(R.id.cmbTime);
        cmbInsuranceType = (Spinner) view.findViewById(R.id.cmbInsuranceType);

        final StateSpinnerAdapter stateSpinnerAdapter = new StateSpinnerAdapter();
        cmbState.setAdapter(stateSpinnerAdapter);
        cmbState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                StateModel stateModel = (StateModel) parent.getSelectedItem();

                List<CityModel> cityModelList = stateModel.getCityModelList();

                CitySpinnerAdapter citySpinnerAdapter = new CitySpinnerAdapter(cityModelList);
                cmbCity.setAdapter(citySpinnerAdapter);
                cmbCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        CityModel cityModel = (CityModel) parent.getSelectedItem();

                        final List<HospitalModel> hospitalModelList = cityModel.getHospitalModelList();

                        cmbHospital.setAdapter(null);
                        cmbSpeciality.setAdapter(null);
                        cmbTime.setAdapter(null);
                        cmbInsuranceType.setAdapter(null);

                        HospitalSpinnerAdapter hospitalSpinnerAdapter = new HospitalSpinnerAdapter(hospitalModelList);
                        cmbHospital.setAdapter(hospitalSpinnerAdapter);
                        cmbHospital.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                HospitalModel hospitalModel = (HospitalModel) parent.getSelectedItem();

                                hotpitalCode = hospitalModel.getHospitalId();

                                List<SpecialtyModel> specialtyModelList = hospitalModel.getSpecialtyModelList();
                                List<ClinicModel> clinicModelList = hospitalModel.getClinicModelList();
                                List<InsuranceTypeModel> insuranceTypeModelList = hospitalModel.getInsuranceTypeModelList();

                                InsuranceTypeSpinnerAdapter insuranceTypeSpinnerAdapter = new InsuranceTypeSpinnerAdapter(insuranceTypeModelList);
                                cmbInsuranceType.setAdapter(insuranceTypeSpinnerAdapter);
                                cmbInsuranceType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        InsuranceTypeModel insuranceTypeModel = (InsuranceTypeModel) parent.getSelectedItem();
                                        insuranceCode = insuranceTypeModel.getInsuranceTypeId();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });

                                SpecialitySpinnerAdapter specialitySpinnerAdapter = new SpecialitySpinnerAdapter(specialtyModelList);
                                cmbSpeciality.setAdapter(specialitySpinnerAdapter);
                                cmbSpeciality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        specialtyModel = (SpecialtyModel) parent.getSelectedItem();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });

                                ClinicSpinnerAdapter clinicSpinnerAdapter = new ClinicSpinnerAdapter(clinicModelList);
                                cmbTime.setAdapter(clinicSpinnerAdapter);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                        if (pDialog.isShowing()) {
                            pDialog.dismiss();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cmbTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ClinicModel clinicModel = (ClinicModel) parent.getSelectedItem();

                clinicCode = clinicModel.getClinicId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void btnSearchDoctorClick() {
        if(cmbHospital.getAdapter().getCount() == 0 || cmbSpeciality.getAdapter().getCount() == 0 || cmbTime.getAdapter().getCount() == 0 || cmbInsuranceType.getAdapter().getCount() == 0){
            Toast.makeText(MainActivity.this, "ورود تمامی اطلاعات الزامی است.",Toast.LENGTH_LONG).show();
            return;
        }


        LayoutInflater inflater = getLayoutInflater();
        View searchLoadingLayout = inflater.inflate(R.layout.search_doctor_loading, null);

        GifImageView gib = new GifImageView(this);
        gib.setImageResource(R.mipmap.search_loading);

        LinearLayout llSearchLoading = (LinearLayout) searchLoadingLayout.findViewById(R.id.llSearchLoading);
        llSearchLoading.addView(gib);

        final AlertDialog.Builder searchDoctorDialog = new AlertDialog.Builder(this);
        searchDoctorDialog.setView(searchLoadingLayout);

        searchLoadingAlertDialog = searchDoctorDialog.create();
        searchLoadingAlertDialog.setCancelable(false);
        searchLoadingAlertDialog.show();
        searchLoadingAlertDialog.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    //searchLoadingAlertDialog.dismiss();
                }
                return true;
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (searchLoadingAlertDialog.isShowing()) {
                    searchLoadingAlertDialog.dismiss();
                    searchDoctorAlertDialog.dismiss();
                    showDoctorActivity();
                }
            }
        }, 5000);
    }

    public void showDoctorActivity() {
        Intent doctorActivity = new Intent(MainActivity.this, DoctorActivity.class);
        SpecialtyModel specialtyModel = (SpecialtyModel) cmbSpeciality.getSelectedItem();
        doctorActivity.putExtra(Constant.DOCTOR_BUNDLE, (Serializable) specialtyModel.getDoctorModelList());
        startActivity(doctorActivity);

        //finish();
    }

    private void CreateModel(JSONObject jsonObject) {
        stateModelList = new ArrayList<>();

        try {
            JSONArray jsonArray = jsonObject.getJSONArray("data");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject dataItem = jsonArray.getJSONObject(i);

                JSONArray cityArray = dataItem.getJSONArray("city");


                StateModel stateModel = new StateModel();

                int stateId = dataItem.getInt("stateId");
                String stateName = dataItem.getString("title");

                stateModel.setStateId(stateId);
                stateModel.setStateName(stateName);
                stateModel.setCityModelList(createCityModel(cityArray));

                stateModelList.add(stateModel);
            }

        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    private List<CityModel> createCityModel(JSONArray cityArray) {
        List<CityModel> cityModelList = new ArrayList<>();

        try {
            for (int c = 0; c < cityArray.length(); c++) {
                JSONObject cityObject = cityArray.getJSONObject(c);

                JSONArray hospitalTypeArray = cityObject.getJSONArray("hospitalType");
                JSONArray hospitalArray = cityObject.getJSONArray("hospital");


                CityModel cityModel = new CityModel();

                int cityId = cityObject.getInt("cityId");
                String cityName = cityObject.getString("title");

                cityModel.setCityId(cityId);
                cityModel.setCityName(cityName);
                cityModel.setHospitalTypeModelList(createHospitalTypeModel(hospitalTypeArray));
                cityModel.setHospitalModelList(createHospitalModel(hospitalArray));

                cityModelList.add(cityModel);
            }

            return cityModelList;
        } catch (JSONException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private List<HospitalTypeModel> createHospitalTypeModel(JSONArray hospitalTypeArray) {
        List<HospitalTypeModel> hospitalTypeModelList = new ArrayList<>();

        try {
            for (int hp = 0; hp < hospitalTypeArray.length(); hp++) {
                JSONObject hospitalTypeObject = hospitalTypeArray.getJSONObject(hp);

                int hospitalTypeId = hospitalTypeObject.getInt("hospitalTypeId");
                String hospitalTypeName = hospitalTypeObject.getString("title");

                HospitalTypeModel hospitalTypeModel = new HospitalTypeModel();

                hospitalTypeModel.setHospitalModelId(hospitalTypeId);
                hospitalTypeModel.setHospitalModelName(hospitalTypeName);

                hospitalTypeModelList.add(hospitalTypeModel);
            }

            return hospitalTypeModelList;
        } catch (JSONException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private List<HospitalModel> createHospitalModel(JSONArray hospitalArray) {
        List<HospitalModel> hospitalModelList = new ArrayList<>();

        try {
            for (int h = 0; h < hospitalArray.length(); h++) {
                JSONObject hospitalObject = hospitalArray.getJSONObject(h);

                int hospitalId = hospitalObject.getInt("hospitalId");
                String hospitalName = hospitalObject.getString("title");
                JSONArray specialtyArray = hospitalObject.getJSONArray("specialty");
                JSONArray clinicArray = hospitalObject.getJSONArray("clinic");
                JSONArray insuranceTypeArray = hospitalObject.getJSONArray("insurancetype");

                HospitalModel hospitalModel = new HospitalModel();
                hospitalModel.setHospitalId(hospitalId);
                hospitalModel.setHospitalName(hospitalName);
                hospitalModel.setSpecialtyModelList(createSpecialtyModel(specialtyArray));
                hospitalModel.setClinicModelList(createClinicModel(clinicArray));
                hospitalModel.setInsuranceTypeModelList(createInsuranceTypeModel(insuranceTypeArray));

                hospitalModelList.add(hospitalModel);
            }

            return hospitalModelList;
        } catch (JSONException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private List<SpecialtyModel> createSpecialtyModel(JSONArray specialtyArray) {
        List<SpecialtyModel> specialtyModelList = new ArrayList<>();
        try {
            for (int s = 0; s < specialtyArray.length(); s++) {
                JSONObject specialtyObject = specialtyArray.getJSONObject(s);

                int specialtyId = specialtyObject.getInt("specialtyId");
                String specialtyName = specialtyObject.getString("title");

                JSONArray doctorArray = specialtyObject.getJSONArray("doctor");


                SpecialtyModel specialtyModel = new SpecialtyModel();
                specialtyModel.setSpecialtyId(specialtyId);
                specialtyModel.setSpecialtyName(specialtyName);
                specialtyModel.setDoctorModelList(createDoctorModel(doctorArray));

                specialtyModelList.add(specialtyModel);
            }

            return specialtyModelList;
        } catch (JSONException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private List<DoctorModel> createDoctorModel(JSONArray doctorArray) {
        List<DoctorModel> doctorModelList = new ArrayList<>();
        try {
            for (int d = 0; d < doctorArray.length(); d++) {
                JSONObject specialtyObject = doctorArray.getJSONObject(d);

                int doctorId = specialtyObject.getInt("doctorId");
                String doctorName = specialtyObject.getString("name");
                String imageAddress = specialtyObject.getString("img").replace(" ", "%20");


                DoctorModel doctorModel = new DoctorModel();
                doctorModel.setDoctorId(doctorId);
                doctorModel.setDoctorName(doctorName);
                doctorModel.setImageAddress(imageAddress);

                doctorModelList.add(doctorModel);
            }

            return doctorModelList;
        } catch (JSONException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private List<ClinicModel> createClinicModel(JSONArray clinicArray) {
        List<ClinicModel> clinicModelList = new ArrayList<>();
        try {
            for (int c = 0; c < clinicArray.length(); c++) {
                JSONObject clinicObject = clinicArray.getJSONObject(c);

                int clinicId = clinicObject.getInt("clinicId");
                String clinicName = clinicObject.getString("title");

                ClinicModel clinicModel = new ClinicModel();
                clinicModel.setClinicId(clinicId);
                clinicModel.setClinicName(clinicName);

                clinicModelList.add(clinicModel);
            }
            return clinicModelList;
        } catch (JSONException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    protected List<InsuranceTypeModel> createInsuranceTypeModel(JSONArray insuranceTypeArray) {
        List<InsuranceTypeModel> insuranceTypeModelList = new ArrayList<>();

        try {
            for (int i = 0; i < insuranceTypeArray.length(); i++) {
                JSONObject insuranceTypeObject = insuranceTypeArray.getJSONObject(i);

                int insuranceTypeId = insuranceTypeObject.getInt("insuranceType");
                String insuranceTypeName = insuranceTypeObject.getString("title");

                InsuranceTypeModel insuranceTypeModel = new InsuranceTypeModel();
                insuranceTypeModel.setInsuranceTypeId(insuranceTypeId);
                insuranceTypeModel.setInsuranceTypeName(insuranceTypeName);

                insuranceTypeModelList.add(insuranceTypeModel);
            }

            return insuranceTypeModelList;
        } catch (JSONException ex) {
            ex.printStackTrace();
            return null;
        }
    }


    private class StateSpinnerAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return stateModelList.size();
        }

        @Override
        public Object getItem(int position) {
            return stateModelList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return stateModelList.get(position).getStateId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                LayoutInflater inflater = getLayoutInflater();
                view = inflater.inflate(R.layout.spinner_item_layout, null);
            } else {
                view = convertView;
            }

            StateModel stateModel = stateModelList.get(position);

            BRtextview text1 = (BRtextview) view.findViewById(R.id.text1);
            text1.setText(stateModel.getStateName());

            return view;
        }
    }

    private class CitySpinnerAdapter extends BaseAdapter {
        private List<CityModel> cityModelList = new ArrayList<>();

        public CitySpinnerAdapter(List<CityModel> cityModel) {
            this.cityModelList = cityModel;
        }

        @Override
        public int getCount() {
            return cityModelList.size();
        }

        @Override
        public Object getItem(int position) {
            return cityModelList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return cityModelList.get(position).getCityId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                LayoutInflater inflater = getLayoutInflater();
                view = inflater.inflate(R.layout.spinner_item_layout, null);
            } else {
                view = convertView;
            }

            CityModel cityModel = cityModelList.get(position);

            BRtextview text1 = (BRtextview) view.findViewById(R.id.text1);
            text1.setText(cityModel.getCityName());

            return view;
        }
    }

    private class HospitalTypeSpinnerAdapter extends BaseAdapter {
        private List<HospitalTypeModel> hospitalTypeList = new ArrayList<>();

        public HospitalTypeSpinnerAdapter(List<HospitalTypeModel> hospitalTypeList) {
            this.hospitalTypeList = hospitalTypeList;
        }

        @Override
        public int getCount() {
            return hospitalTypeList.size();
        }

        @Override
        public Object getItem(int position) {
            return hospitalTypeList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return hospitalTypeList.get(position).getHospitalModelId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                LayoutInflater inflater = getLayoutInflater();
                view = inflater.inflate(R.layout.spinner_item_layout, null);
            } else {
                view = convertView;
            }

            HospitalTypeModel hospitalTypeModel = hospitalTypeList.get(position);

            BRtextview text1 = (BRtextview) view.findViewById(R.id.text1);
            text1.setText(hospitalTypeModel.getHospitalModelName());

            return view;
        }
    }

    private class HospitalSpinnerAdapter extends BaseAdapter {
        private List<HospitalModel> hospitalModelList;

        public HospitalSpinnerAdapter(List<HospitalModel> hospitalModelList) {
            this.hospitalModelList = hospitalModelList;
        }

        @Override
        public int getCount() {
            return hospitalModelList.size();
        }

        @Override
        public Object getItem(int position) {
            return hospitalModelList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return hospitalModelList.get(position).getHospitalId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                LayoutInflater inflater = getLayoutInflater();
                view = inflater.inflate(R.layout.spinner_item_layout, null);
            } else {
                view = convertView;
            }

            HospitalModel hospitalModel = hospitalModelList.get(position);

            BRtextview text1 = (BRtextview) view.findViewById(R.id.text1);
            text1.setText(hospitalModel.getHospitalName());

            return view;
        }
    }

    private class SpecialitySpinnerAdapter extends BaseAdapter {
        private List<SpecialtyModel> specialtyModelList;

        public SpecialitySpinnerAdapter(List<SpecialtyModel> specialtyModelList) {
            this.specialtyModelList = specialtyModelList;
        }

        @Override
        public int getCount() {
            return specialtyModelList.size();
        }

        @Override
        public Object getItem(int position) {
            return specialtyModelList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return specialtyModelList.get(position).getSpecialtyId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                LayoutInflater inflater = getLayoutInflater();
                view = inflater.inflate(R.layout.spinner_item_layout, null);
            } else {
                view = convertView;
            }

            SpecialtyModel specialtyModel = specialtyModelList.get(position);

            BRtextview text1 = (BRtextview) view.findViewById(R.id.text1);
            text1.setText(specialtyModel.getSpecialtyName());

            return view;
        }
    }

    private class ClinicSpinnerAdapter extends BaseAdapter {
        private List<ClinicModel> clinicModelList;

        public ClinicSpinnerAdapter(List<ClinicModel> clinicModelList) {
            this.clinicModelList = clinicModelList;
        }

        @Override
        public int getCount() {
            return clinicModelList.size();
        }

        @Override
        public Object getItem(int position) {
            return clinicModelList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return clinicModelList.get(position).getClinicId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                LayoutInflater inflater = getLayoutInflater();
                view = inflater.inflate(R.layout.spinner_item_layout, null);
            } else {
                view = convertView;
            }

            ClinicModel clinicModel = clinicModelList.get(position);

            BRtextview text1 = (BRtextview) view.findViewById(R.id.text1);
            text1.setText(clinicModel.getClinicName());

            return view;
        }
    }

    private class InsuranceTypeSpinnerAdapter extends BaseAdapter {
        private List<InsuranceTypeModel> insuranceTypeModelList;

        public InsuranceTypeSpinnerAdapter(List<InsuranceTypeModel> insuranceTypeModelList) {
            this.insuranceTypeModelList = insuranceTypeModelList;
        }

        @Override
        public int getCount() {
            return insuranceTypeModelList.size();
        }

        @Override
        public Object getItem(int position) {
            return insuranceTypeModelList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return insuranceTypeModelList.get(position).getInsuranceTypeId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                LayoutInflater inflater = getLayoutInflater();
                view = inflater.inflate(R.layout.spinner_item_layout, null);
            } else {
                view = convertView;
            }

            InsuranceTypeModel insuranceTypeModel = insuranceTypeModelList.get(position);

            BRtextview text1 = (BRtextview) view.findViewById(R.id.text1);
            text1.setText(insuranceTypeModel.getInsuranceTypeName());

            return view;
        }
    }
}

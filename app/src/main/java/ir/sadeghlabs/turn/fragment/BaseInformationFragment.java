package ir.sadeghlabs.turn.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.berans.view.BRbutton;
import org.berans.view.BRedittext;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import ir.sadeghlabs.connectionhandler.ConnectionHandler;
import ir.sadeghlabs.connectionhandler.Constant;
import ir.sadeghlabs.turn.R;
import ir.sadeghlabs.turn.activity.BaseInformationActivity;
import ir.sadeghlabs.turn.activity.DoctorActivity;
import ir.sadeghlabs.turn.activity.MainActivity;
import ir.sadeghlabs.turn.model.GenderModel;

/**
 * Created by Siavash on 12/15/2015.
 */
public class BaseInformationFragment extends Fragment {
    private FragmentActivity context;
    private View layoutView;
    private BRedittext txtSickName;
    private BRedittext txtMobile;
    private BRedittext txtInsuranceNumber;
    private BRedittext txtAddress;
    private Spinner cmbGender;
    private Spinner cmbInsuranceType;



    private List<GenderModel> genderModelList = new ArrayList<>();


    @Override
    public void onAttach(Activity activity) {
        context = (FragmentActivity) activity;
        super.onAttach(activity);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.fragment_base_information, null);

        populateGender();
        initEditText();
        initSpinner();
        initButton();

        return layoutView;
    }

    /**
     * populate gender data
     */
    private void populateGender() {
        GenderModel menModel = new GenderModel();
        menModel.setGenderId(1);
        menModel.setTitle("مذکر");

        GenderModel womenModel = new GenderModel();
        menModel.setGenderId(2);
        menModel.setTitle("مؤنث");

        genderModelList.add(menModel);
        genderModelList.add(womenModel);
    }

    /**
     * init edit text
     */
    private void initEditText() {
        txtSickName = (BRedittext) layoutView.findViewById(R.id.txtSickName);
        txtMobile = (BRedittext) layoutView.findViewById(R.id.txtMobile);
        txtInsuranceNumber = (BRedittext) layoutView.findViewById(R.id.txtInsuranceNumber);
        txtAddress = (BRedittext) layoutView.findViewById(R.id.txtAddress);
    }

    /**
     * init spinner
     */
    private void initSpinner() {
        cmbGender = (Spinner) layoutView.findViewById(R.id.cmbGender);
        cmbInsuranceType = (Spinner) layoutView.findViewById(R.id.cmbInsuranceType);

        ArrayAdapter cmbGenderAdapter = ArrayAdapter.createFromResource(context, R.array.gender, R.layout.spinner_item_layout);
        cmbGender.setAdapter(cmbGenderAdapter);

        ArrayAdapter cmbInsuranceTypeAdapter = ArrayAdapter.createFromResource(context, R.array.insuranceType, R.layout.spinner_item_layout);
        cmbInsuranceType.setAdapter(cmbInsuranceTypeAdapter);
    }

    /**
     * init button
     */
    private void initButton() {
        BRbutton btnBaseInformationFragmentOk = (BRbutton) layoutView.findViewById(R.id.btnBaseInformationFragmentOk);
        BRbutton btnBaseInformationFragmentCancel = (BRbutton) layoutView.findViewById(R.id.btnBaseInformationFragmentCancel);

        btnBaseInformationFragmentOk.setOnClickListener(new ButtonClick());
        btnBaseInformationFragmentCancel.setOnClickListener(new ButtonClick());
    }

    /**
     * handle button click
     */
    private class ButtonClick implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnBaseInformationFragmentOk:
/*                    FactorFragment factorFragment = new FactorFragment();

                    context.getSupportFragmentManager().beginTransaction().replace(R.id.container, factorFragment, "factorFragment").addToBackStack(null).commit();*/
                    sendDateToServer();
                    break;
                case R.id.btnBaseInformationFragmentCancel:
                    getActivity().finish();
                    break;
            }
        }
    }

    /**
     *
     * */
    private void sendDateToServer() {
        try {
            String name = txtSickName.getText().toString();
            String mobile = txtMobile.getText().toString();
            String gender = cmbGender.getSelectedItemPosition() == 0 ? "مرد" : "زن";
            String shomareBimeh = txtInsuranceNumber.getText().toString();
            String address = txtAddress.getText().toString();

            if(name.matches("") || mobile.matches("")){
                Toast.makeText(context,"ورود تمامی اطلاعات الزامی است",Toast.LENGTH_LONG).show();
                return;
            }

            List nameValuePairs = new ArrayList();
            nameValuePairs.add(new BasicNameValuePair("User", ir.sadeghlabs.turn.model.Constant.USER));
            nameValuePairs.add(new BasicNameValuePair("Password", ir.sadeghlabs.turn.model.Constant.PASSWORD));
            nameValuePairs.add(new BasicNameValuePair("BimarestanCode_Fk", String.valueOf(MainActivity.hotpitalCode)));
            nameValuePairs.add(new BasicNameValuePair("ShomarNobat", String.valueOf(DoctorActivity.doctorId)));
            nameValuePairs.add(new BasicNameValuePair("InsuranceCode_Fk", String.valueOf(MainActivity.insuranceCode)));
            nameValuePairs.add(new BasicNameValuePair("CodeMeli", BaseInformationActivity.nationalCode));
            nameValuePairs.add(new BasicNameValuePair("Name", URLEncoder.encode(name)));
            nameValuePairs.add(new BasicNameValuePair("Mobile", mobile));
            nameValuePairs.add(new BasicNameValuePair("Gender", gender));
            nameValuePairs.add(new BasicNameValuePair("CodeBime", shomareBimeh));
            nameValuePairs.add(new BasicNameValuePair("Address", URLEncoder.encode(address, "UTF-8")));

            new ConnectionHandler(context, true, ir.sadeghlabs.turn.model.Constant.GetNobat, Constant.method.POST, "ارسال اطلاعات به سرور...", false, nameValuePairs, new ConnectionHandler.CommunicatorListener() {
                @Override
                public void onNoInternetConnection() {
                    Toast.makeText(context, "عدم اتصال به اینترنت", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onPreExecute() {

                }

                @Override
                public void onDone(String result) {
                    String link = result.replaceAll("amp;","");
                    link=link.substring(link.indexOf(";")+1,link.indexOf("</string>"));
                    natToBrowser(link);
                }

                @Override
                public void onCancelled() {
                    Toast.makeText(context, "قطع ارتباط توسط کاربر", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCodeError() {
                    Toast.makeText(context, "خطا در کدهای اجرایی", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onServerError(Exception e) {
                    Toast.makeText(context, "خطا در کدهای اجرایی سرور", Toast.LENGTH_LONG).show();
                }
            }).execute();

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void natToBrowser(String link){
        BrowserFragment browserFragment = new BrowserFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ir.sadeghlabs.turn.model.Constant.URL_LINK,link);
        browserFragment.setArguments(bundle);
        context.getSupportFragmentManager().beginTransaction().replace(R.id.container,browserFragment,"browserFragment").addToBackStack(null).commit();
    }
}

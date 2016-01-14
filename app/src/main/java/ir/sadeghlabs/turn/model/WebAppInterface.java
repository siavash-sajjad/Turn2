package ir.sadeghlabs.turn.model;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import ir.sadeghlabs.turn.R;
import ir.sadeghlabs.turn.activity.DoctorActivity;
import ir.sadeghlabs.turn.activity.MainActivity;
import ir.sadeghlabs.turn.entity.Turn;
import ir.sadeghlabs.turn.fragment.ResultFragment;
import ir.sadeghlabs.turn.management.TurnManagement;
import ir.sadeghlabs.turn.tools.PersianCalendar;

/**
 * Created by Siavash on 1/7/2016.
 */
public class WebAppInterface {
    Context mContext;
    private FragmentManager supportFragmentManager;

    /**
     * Instantiate the interface and set the context
     */
    public WebAppInterface(Context c, FragmentManager supportFragmentManager) {
        mContext = c;
        this.supportFragmentManager = supportFragmentManager;
    }

    /**
     * Show a toast from the web page
     */
    @JavascriptInterface
    public void sendDataToAndroid(String toast) {
        String[] result = toast.split(";");

        if (result[0].matches("1")) {
            saveData(result);
        } else {
            Toast.makeText(mContext, result[1].toLowerCase(), Toast.LENGTH_LONG).show();
            navToMainPage();
        }
    }

    private void saveData(String[] info) {
        Turn turn = new Turn();
        turn.setTurn_Id(0);
        turn.setTurnServer_Id(0);
        turn.setDoctor_Id(DoctorActivity.doctorId);
        turn.setDoctorName(info[3]);
        turn.setSpeciality_Id(MainActivity.specialtyModel.getSpecialtyId());
        turn.setSpecialityName(MainActivity.specialtyModel.getSpecialtyName());
        turn.setStatus(0);
        turn.setTurnDate(info[7]);
        turn.setTurnTime(info[4]);
        turn.setTurn("0");
        turn.setHospitalName(info[1]);
        turn.setClinicName(info[5]);
        turn.setPaymentCode(info[2]);
        turn.setPrice(info[6]);

        SimpleDateFormat simpleAdapter = new SimpleDateFormat("HH:mm");
        Calendar calendar = Calendar.getInstance();
        PersianCalendar persianCalendar = new PersianCalendar();
        String date = persianCalendar.getIranianDate() + " " + simpleAdapter.format(calendar.getTime());

        turn.setSubmitDdateTime(date);

        TurnManagement turnManagement = new TurnManagement(mContext, Constant.DATABASE_NAME, Constant.DATABASE_VERSION);
        int turnId = turnManagement.saveTurn(turn, false);

        if (turnId > 0) {
            navToResult(turnId);
        }

    }

    private void navToMainPage() {
        Intent mainActivity = new Intent(mContext, MainActivity.class);
        mContext.startActivity(mainActivity);
    }

    private void navToResult(int turnId) {
        ResultFragment resultFragment = new ResultFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constant.TURN_ID, String.valueOf(turnId));
        resultFragment.setArguments(bundle);
        supportFragmentManager.beginTransaction().replace(R.id.container, resultFragment, "resultFragment").addToBackStack(null).commit();
    }
}

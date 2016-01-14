package ir.sadeghlabs.turn.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import ir.sadeghlabs.turn.R;
import ir.sadeghlabs.turn.fragment.BaseInformationFragment;
import ir.sadeghlabs.turn.fragment.FactorFragment;
import ir.sadeghlabs.turn.model.Constant;

public class BaseInformationActivity extends AppCompatActivity {
    private int num = 10 * 60 * 1000;
    private TextView lblCounterDownTimer;
    public static String nationalCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_information);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nationalCode = getIntent().getStringExtra(Constant.NATIONAL_CODE);


        initTextView();
        counterDownTimer();
        setFirstView();


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
        lblCounterDownTimer = (TextView) findViewById(R.id.lblCounterDownTimer);
    }

    /**
     * init counter down time
     */
    private void counterDownTimer() {
        final Handler handler = new Handler();
        handler.post(new Runnable() {

            @Override
            public void run() {

                int seconds = (int) (num / 1000) % 60;
                int minutes = (int) ((num / (1000 * 60)) % 60);

                lblCounterDownTimer.setText(Integer.toString(minutes) + " : " + Integer.toString(seconds));

                if (num == 0) {
                    lblCounterDownTimer.setText("00 : 00");
                    handler.removeCallbacks(this);

                    finish();
                    Intent mainActivity = new Intent(BaseInformationActivity.this, MainActivity.class);
                    startActivity(mainActivity);
                }

                num -= 1000;

                handler.postDelayed(this, 1000);
            }
        });
    }

    /**
     * set first view
     */
    private void setFirstView() {
        BaseInformationFragment baseInformationFragment = new BaseInformationFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, baseInformationFragment, "baseInformationFragment").addToBackStack(null).commit();
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);

        if (fragment != null && fragment instanceof BaseInformationFragment) {
            finish();
        }else if(fragment != null && fragment instanceof FactorFragment){
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            BaseInformationFragment baseInformationFragment = new BaseInformationFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.container, baseInformationFragment, "baseInformationFragment").addToBackStack(null).commit();
        }else {
            super.onBackPressed();
        }
    }
}

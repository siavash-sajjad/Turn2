package ir.sadeghlabs.turn.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import ir.sadeghlabs.turn.R;
import ir.sadeghlabs.turn.management.DatabaseManagement;
import ir.sadeghlabs.turn.model.Constant;
import pl.droidsonroids.gif.GifImageView;

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        checkDatabase();
        initLinearLayout();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                navToMain();
                finish();
            }
        }, Constant.SPLASH_TIME_OUT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    private void checkDatabase() {
        DatabaseManagement databaseManagement = new DatabaseManagement(getBaseContext());

        try {
            boolean databaseIsExists = databaseManagement.getDatabase();

            if (!databaseIsExists) {
                databaseManagement.checkPath();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * init gif image
     * */
    private void initLinearLayout(){
        GifImageView gib = new GifImageView(this);
        gib.setImageResource(R.mipmap.ic_splash);

        LinearLayout llLogo = (LinearLayout) findViewById(R.id.llLogo);
        llLogo.addView(gib);
    }

    private void navToMain() {
        Intent mainActivity = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(mainActivity);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.exit(0);
    }
}

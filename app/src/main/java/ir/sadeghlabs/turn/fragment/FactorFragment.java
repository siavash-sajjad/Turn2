package ir.sadeghlabs.turn.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ir.sadeghlabs.turn.R;

/**
 * Created by Siavash on 12/16/2015.
 */
public class FactorFragment extends Fragment {
    private FragmentActivity context;
    private View layoutView;




    @Override
    public void onAttach(Activity activity) {
        context = (FragmentActivity) activity;
        super.onAttach(activity);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.fragment_factor, null);

        initTextView();

        return layoutView;
    }

    /**
     * init text view
     * */
    private void initTextView(){

    }
}

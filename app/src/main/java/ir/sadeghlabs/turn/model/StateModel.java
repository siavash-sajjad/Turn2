package ir.sadeghlabs.turn.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Siavash on 1/4/2016.
 */
public class StateModel {
    private int stateId;
    private String stateName;
    private List<CityModel> cityModelList;


    public int getStateId() {
        return stateId;
    }

    public void setStateId(int stateId) {
        this.stateId = stateId;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public List<CityModel> getCityModelList() {
        return cityModelList;
    }

    public void setCityModelList(List<CityModel> cityModelList) {
        this.cityModelList = cityModelList;
    }
}

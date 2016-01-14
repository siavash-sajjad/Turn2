package ir.sadeghlabs.turn.model;

import java.util.List;

/**
 * Created by Siavash on 1/4/2016.
 */
public class CityModel {
    private int cityId;
    private String cityName;
    private List<HospitalTypeModel> hospitalTypeModelList;
    private List<HospitalModel> hospitalModelList;


    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public List<HospitalTypeModel> getHospitalTypeModelList() {
        return hospitalTypeModelList;
    }

    public void setHospitalTypeModelList(List<HospitalTypeModel> hospitalTypeModelList) {
        this.hospitalTypeModelList = hospitalTypeModelList;
    }

    public List<HospitalModel> getHospitalModelList() {
        return hospitalModelList;
    }

    public void setHospitalModelList(List<HospitalModel> hospitalModelList) {
        this.hospitalModelList = hospitalModelList;
    }
}

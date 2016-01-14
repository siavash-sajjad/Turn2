package ir.sadeghlabs.turn.model;

import java.util.List;

/**
 * Created by Siavash on 1/4/2016.
 */
public class SpecialtyModel {
    private int SpecialtyId;
    private String SpecialtyName;
    private List<DoctorModel> doctorModelList;


    public int getSpecialtyId() {
        return SpecialtyId;
    }

    public void setSpecialtyId(int specialtyId) {
        SpecialtyId = specialtyId;
    }

    public String getSpecialtyName() {
        return SpecialtyName;
    }

    public void setSpecialtyName(String specialtyName) {
        SpecialtyName = specialtyName;
    }

    public List<DoctorModel> getDoctorModelList() {
        return doctorModelList;
    }

    public void setDoctorModelList(List<DoctorModel> doctorModelList) {
        this.doctorModelList = doctorModelList;
    }
}

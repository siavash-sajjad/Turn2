package ir.sadeghlabs.turn.model;

import java.util.List;

/**
 * Created by Siavash on 1/4/2016.
 */
public class HospitalModel {
    private int hospitalId;
    private String hospitalName;
    private List<SpecialtyModel> specialtyModelList;
    private List<ClinicModel> clinicModelList;
    private List<InsuranceTypeModel> insuranceTypeModelList;


    public int getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(int hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public List<SpecialtyModel> getSpecialtyModelList() {
        return specialtyModelList;
    }

    public void setSpecialtyModelList(List<SpecialtyModel> specialtyModelList) {
        this.specialtyModelList = specialtyModelList;
    }

    public List<ClinicModel> getClinicModelList() {
        return clinicModelList;
    }

    public void setClinicModelList(List<ClinicModel> clinicModelList) {
        this.clinicModelList = clinicModelList;
    }

    public List<InsuranceTypeModel> getInsuranceTypeModelList() {
        return insuranceTypeModelList;
    }

    public void setInsuranceTypeModelList(List<InsuranceTypeModel> insuranceTypeModelList) {
        this.insuranceTypeModelList = insuranceTypeModelList;
    }
}

package ir.sadeghlabs.turn.entity;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by Siavash on 11/9/2015.
 */
public class Turn {
    @DatabaseField(generatedId = true)
    private int Turn_Id;

    @DatabaseField(canBeNull = false)
    private int TurnServer_Id;

    @DatabaseField(canBeNull = false)
    private int Doctor_Id;

    @DatabaseField(canBeNull = false)
    private String DoctorName;

    @DatabaseField(canBeNull = false)
    private int Speciality_Id;

    @DatabaseField(canBeNull = false)
    private String SpecialityName;

    @DatabaseField(canBeNull = false)
    private String TurnDate;

    @DatabaseField(canBeNull = false)
    private String TurnTime;

    @DatabaseField(canBeNull = false)
    private String Turn;

    @DatabaseField(canBeNull = false)
    private String SubmitDateTime;

    @DatabaseField(canBeNull = false)
    private int Status;

    @DatabaseField(canBeNull = false)
    private String HospitalName;

    @DatabaseField(canBeNull = false)
    private String PaymentCode;

    @DatabaseField(canBeNull = false)
    private String ClinicName;

    @DatabaseField(canBeNull = false)
    private String Price;

    @DatabaseField(canBeNull = false)
    private String AlarmTime;


    public int getTurn_Id() {
        return Turn_Id;
    }

    public void setTurn_Id(int turn_Id) {
        Turn_Id = turn_Id;
    }

    public int getTurnServer_Id() {
        return TurnServer_Id;
    }

    public void setTurnServer_Id(int turnServer_Id) {
        TurnServer_Id = turnServer_Id;
    }

    public int getDoctor_Id() {
        return Doctor_Id;
    }

    public void setDoctor_Id(int doctor_Id) {
        Doctor_Id = doctor_Id;
    }

    public String getDoctorName() {
        return DoctorName;
    }

    public void setDoctorName(String doctorName) {
        DoctorName = doctorName;
    }

    public int getSpeciality_Id() {
        return Speciality_Id;
    }

    public void setSpeciality_Id(int speciality_Id) {
        Speciality_Id = speciality_Id;
    }

    public String getSpecialityName() {
        return SpecialityName;
    }

    public void setSpecialityName(String specialityName) {
        SpecialityName = specialityName;
    }

    public String getTurnDate() {
        return TurnDate;
    }

    public void setTurnDate(String turnDate) {
        TurnDate = turnDate;
    }

    public String getTurnTime() {
        return TurnTime;
    }

    public void setTurnTime(String turnTime) {
        TurnTime = turnTime;
    }

    public String getTurn() {
        return Turn;
    }

    public void setTurn(String turn) {
        Turn = turn;
    }

    public String getSubmitDdateTime() {
        return SubmitDateTime;
    }

    public void setSubmitDdateTime(String submitDdateTime) {
        SubmitDateTime = submitDdateTime;
    }

    public int isStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getHospitalName() {
        return HospitalName;
    }

    public void setHospitalName(String hospitalName) {
        HospitalName = hospitalName;
    }

    public String getPaymentCode() {
        return PaymentCode;
    }

    public void setPaymentCode(String paymentCode) {
        PaymentCode = paymentCode;
    }

    public String getClinicName() {
        return ClinicName;
    }

    public void setClinicName(String clinicName) {
        ClinicName = clinicName;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getAlarmTime() {
        return AlarmTime;
    }

    public void setAlarmTime(String alarmTime) {
        AlarmTime = alarmTime;
    }
}

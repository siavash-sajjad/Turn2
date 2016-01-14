package ir.sadeghlabs.turn.model;

/**
 * Created by Siavash on 12/10/2015.
 */
public class TurnModel {
    private int turnId;
    private int turnServerId;
    private int doctorId;
    private String doctorName;
    private int SpecialityId;
    private String SpecialityName;
    private long turnDateTime;
    private String status;



    public int getTurnId() {
        return turnId;
    }

    public void setTurnId(int turnId) {
        this.turnId = turnId;
    }

    public int getTurnServerId() {
        return turnServerId;
    }

    public void setTurnServerId(int turnServerId) {
        this.turnServerId = turnServerId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public int getSpecialityId() {
        return SpecialityId;
    }

    public void setSpecialityId(int specialityId) {
        SpecialityId = specialityId;
    }

    public String getSpecialityName() {
        return SpecialityName;
    }

    public void setSpecialityName(String specialityName) {
        SpecialityName = specialityName;
    }

    public long getTurnDateTime() {
        return turnDateTime;
    }

    public void setTurnDateTime(long turnDateTime) {
        this.turnDateTime = turnDateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

package ir.sadeghlabs.turn.model;

import java.io.Serializable;

/**
 * Created by Siavash on 12/10/2015.
 */
public class DoctorModel implements Serializable {
    private int doctorId;
    private String doctorName;
    private String imageAddress;
    private long dateTime;
    private int laseTurn;


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

    public String getImageAddress() {
        return imageAddress;
    }

    public void setImageAddress(String imageAddress) {
        this.imageAddress = imageAddress;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public int getLaseTurn() {
        return laseTurn;
    }

    public void setLaseTurn(int laseTurn) {
        this.laseTurn = laseTurn;
    }
}

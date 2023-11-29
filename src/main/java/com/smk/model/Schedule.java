package com.smk.model;

import java.util.Date;

public class Schedule extends Model{
    private long id;
    private long depaturedId;
    private long arrivalId;
    private Date depatureDate;
    private String flightNumber;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDepaturedId() {
        return depaturedId;
    }

    public void setDepaturedId(long depaturedId) {
        this.depaturedId = depaturedId;
    }

    public long getArrivalId() {
        return arrivalId;
    }

    public void setArrivalId(long arrivalId) {
        this.arrivalId = arrivalId;
    }

    public Date getDepatureDate() {
        return depatureDate;
    }

    public void setDepatureDate(Date depatureDate) {
        this.depatureDate = depatureDate;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }
}

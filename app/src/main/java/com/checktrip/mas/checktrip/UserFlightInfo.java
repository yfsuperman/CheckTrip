package com.checktrip.mas.checktrip;

/**
 * Created by FanYang on 11/2/16.
 */

public class UserFlightInfo {
    private String arrivalAirportAddress, arrivalAirportFsCode, arrivalGate, arrivalYearMonthDay, arrivalTerminal,arrivalTime;
    private String departureAirportAddress, departureAirportFsCode,departureGate,departureYearMonthDay, departureTerminal, departureTime;
    private String airlineCode, flightId, flightNumber, linkToFlight, passenger, tsa, date;
    private String willCheckBag;


    public UserFlightInfo(){}


    public UserFlightInfo(String arrivalAirportAddress, String arrivalAirportFsCode, String arrivalGate, String arrivalYearMonthDay, String arrivalTerminal, String arrivalTime, String departureAirportAddress, String departureAirportFsCode, String departureGate, String departureYearMonthDay, String departureTerminal, String departureTime, String airlineCode, String flightId, String flightNumber, String linkToFlight, String passenger, String tsa, String date, String willCheckBag) {
        this.arrivalAirportAddress = arrivalAirportAddress;
        this.arrivalAirportFsCode = arrivalAirportFsCode;
        this.arrivalGate = arrivalGate;
        this.arrivalYearMonthDay = arrivalYearMonthDay;
        this.arrivalTerminal = arrivalTerminal;
        this.arrivalTime = arrivalTime;
        this.departureAirportAddress = departureAirportAddress;
        this.departureAirportFsCode = departureAirportFsCode;
        this.departureGate = departureGate;
        this.departureYearMonthDay = departureYearMonthDay;
        this.departureTerminal = departureTerminal;
        this.departureTime = departureTime;
        this.airlineCode = airlineCode;
        this.flightId = flightId;
        this.flightNumber = flightNumber;
        this.linkToFlight = linkToFlight;
        this.passenger = passenger;
        this.tsa = tsa;
        this.date = date;
        this.willCheckBag = willCheckBag;
    }

    public String getArrivalAirportAddress() {
        return arrivalAirportAddress;
    }

    public void setArrivalAirportAddress(String arrivalAirportAddress) {
        this.arrivalAirportAddress = arrivalAirportAddress;
    }

    public String getArrivalAirportFsCode() {
        return arrivalAirportFsCode;
    }

    public void setAirrivalAirportFsCode(String airrivalAirportFsCode) {
        this.arrivalAirportFsCode = airrivalAirportFsCode;
    }

    public String getArrivalGate() {
        return arrivalGate;
    }

    public void setArrivalGate(String arrivalGate) {
        this.arrivalGate = arrivalGate;
    }

    public String getArrivalYearMonthDay() {
        return arrivalYearMonthDay;
    }

    public void setArrivalYearMonthDay(String arrivalYearMonthDay) {
        this.arrivalYearMonthDay = arrivalYearMonthDay;
    }

    public String getArrivalTerminal() {
        return arrivalTerminal;
    }

    public void setArrivalTerminal(String arrivalTerminal) {
        this.arrivalTerminal = arrivalTerminal;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getDepartureAirportAddress() {
        return departureAirportAddress;
    }

    public void setDepartureAirportAddress(String departureAirportAddress) {
        this.departureAirportAddress = departureAirportAddress;
    }

    public String getDepartureAirportFsCode() {
        return departureAirportFsCode;
    }

    public void setDepartureAirportFsCode(String departureAirportFsCode) {
        this.departureAirportFsCode = departureAirportFsCode;
    }

    public String getDepartureGate() {
        return departureGate;
    }

    public void setDepartureGate(String departureGate) {
        this.departureGate = departureGate;
    }

    public String getDepartureYearMonthDay() {
        return departureYearMonthDay;
    }

    public void setDepartureYearMonthDay(String departureYearMonthDay) {
        this.departureYearMonthDay = departureYearMonthDay;
    }

    public String getDepartureTerminal() {
        return departureTerminal;
    }

    public void setDepartureTerminal(String departureTerminal) {
        this.departureTerminal = departureTerminal;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getAirlineCode() {
        return airlineCode;
    }

    public void setAirlineCode(String airlineCode) {
        this.airlineCode = airlineCode;
    }

    public String getFlightId() {
        return flightId;
    }

    public void setFlightId(String flightId) {
        this.flightId = flightId;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getLinkToFlight() {
        return linkToFlight;
    }

    public void setLinkToFlight(String linkToFlight) {
        this.linkToFlight = linkToFlight;
    }

    public String getPassenger() {
        return passenger;
    }

    public void setPassenger(String passenger) {
        this.passenger = passenger;
    }

    public String getTsa() {
        return tsa;
    }

    public void setTsa(String tsa) {
        this.tsa = tsa;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setArrivalAirportFsCode(String arrivalAirportFsCode) {
        this.arrivalAirportFsCode = arrivalAirportFsCode;
    }

    public String getWillCheckBag() {
        return willCheckBag;
    }

    public void setWillCheckBag(String willCheckBag) {
        this.willCheckBag = willCheckBag;
    }
}

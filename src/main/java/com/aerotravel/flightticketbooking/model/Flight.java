// Edited by Karan Katyal and Aniket Panhale
package com.aerotravel.flightticketbooking.model;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Flight {
    @Id
    @GeneratedValue
    private long flightId;
    private String flightNumber;
    @ManyToOne
    private Airport departureAirport;
    @ManyToOne
    private Airport destinationAirport;

    private String departureDate;

    private String arrivalDate;

    private String departureTime;

    private String arrivalTime;

    private double flightCharge;

    @ManyToOne
    Aircraft aircraft;

    @OneToMany(mappedBy = "flight")
    List<Passenger> passengers = new ArrayList<>();

    public Flight() {
    }


    public Flight(long id,String flightNumber, Airport departureAirport, Airport destinationAirport,
    		double flightCharge, String departureDate, String arrivalDate) {

    	this.flightId = id;
    	this.flightNumber = flightNumber;
    	this.departureAirport = departureAirport;
    	this.destinationAirport = destinationAirport;
    	this.flightCharge = flightCharge;
    	this.departureDate = departureDate;
    	this.arrivalDate = arrivalDate;
    }
    
    public Flight(String flightNumber, Airport departureAirport, Airport destinationAirport,
                  double flightCharge, String departureDate, String arrivalDate) {

        this.flightNumber = flightNumber;
        this.departureAirport = departureAirport;
        this.destinationAirport = destinationAirport;
        this.flightCharge = flightCharge;
        this.departureDate = departureDate;
        this.arrivalDate = arrivalDate;
    }

    public long getFlightId() {
        return flightId;
    }

    public void setFlightId(long flightId) {
        this.flightId = flightId;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public Airport getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(Airport departureAirport) {
        this.departureAirport = departureAirport;
    }

    public Airport getDestinationAirport() {
        return destinationAirport;
    }

    public void setDestinationAirport(Airport destinationAirport) {
        this.destinationAirport = destinationAirport;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public double getFlightCharge() {
        return flightCharge;
    }

    public void setFlightCharge(double flightCharge) {
        this.flightCharge = flightCharge;
    }

    public Aircraft getAircraft() {
        return aircraft;
    }

    public void setAircraft(Aircraft aircraft) {
        this.aircraft = aircraft;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<Passenger> passengers) {
        this.passengers = passengers;
    }
}

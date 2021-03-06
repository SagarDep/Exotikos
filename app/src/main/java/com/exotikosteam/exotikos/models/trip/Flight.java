package com.exotikosteam.exotikos.models.trip;

import android.text.TextUtils;

import com.exotikosteam.exotikos.models.ExotikosDatabase;
import com.exotikosteam.exotikos.models.airline.Airline;
import com.exotikosteam.exotikos.models.airport.Airport;
import com.exotikosteam.exotikos.models.flightstatus.AirportResources;
import com.exotikosteam.exotikos.models.flightstatus.Appendix;
import com.exotikosteam.exotikos.models.flightstatus.FlightStatus;
import com.exotikosteam.exotikos.models.flightstatus.ScheduledFlight;
import com.exotikosteam.exotikos.utils.Utils;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.parceler.Parcel;

import java.util.Date;
import java.util.List;

/**
 * Created by lramaswamy on 11/9/16.
 */
@Parcel(analyze = {Flight.class})
@Table(database = ExotikosDatabase.class)
public class Flight extends BaseModel {

    @Column
    @PrimaryKey(autoincrement = true)
    Integer id;

    @Column(name = "trip_id", excludeFromToModelMethod = false)
    Integer tripId;

    @Column(name = "flight_number")
    String flightNumber;

    @Column(name = "flight_carrier")
    String flightCarrier;

    @Column(name = "flight_carrier_name")
    String flightCarrierName;

    @Column(name = "flight_id")
    Integer flightId;

    @Column(name = "seat_number")
    String seatNumber;

    @Column(name = "baggage")
    String baggage;

    @Column(name = "departure_gate")
    String departureGate;

    @Column(name = "departure_time")
    String departureTime;

    @Column(name = "departure_terminal")
    String departureTerminal;

    @Column(name = "departure_time_UTC")
    Date departureTimeUTC;

    @Column(name = "departure_airport_IATA")
    String departureAirportIATA;

    @Column(name = "departure_city")
    String departureCity;

    @Column(name = "arrival_gate")
    String arrivalGate;

    @Column(name = "arrival_time")
    String arrivalTime;

    @Column(name = "arrival_terminal")
    String arrivalTerminal;

    @Column(name = "arrival_time_UTC")
    Date arrivalTimeUTC;

    @Column(name = "arrival_airport_IATA")
    String arrivalAirportIATA;

    @Column(name = "arrival_city")
    String arrivalCity;

    @Column(name = "arrival_city_image_url")
    String arrivalCityImageUrl;

    //Empty constructor for Parceler
    public Flight() {
    }

    public Flight(ScheduledFlight sch, Appendix appendix) {
        super();
        this.setArrivalAirportIATA(sch.getArrivalAirportFsCode());
        this.setArrivalTerminal(sch.getArrivalTerminal());
        this.setArrivalTime(sch.getArrivalTime());
        this.setDepartureAirportIATA(sch.getDepartureAirportFsCode());
        this.setDepartureTerminal(sch.getDepartureTerminal());
        this.setDepartureTime(sch.getDepartureTime());
        this.setFlightCarrier(sch.getCarrierFsCode());
        this.setFlightNumber(sch.getFlightNumber());
        if (appendix != null) {
            setAppendinxData(appendix);
        }
    }

    public Flight(FlightStatus flightStatus, String seatNo, Appendix appendix) {
        super();
        this.setArrivalAirportIATA(flightStatus.getArrivalAirportFsCode());
        this.setArrivalTime(flightStatus.getArrivalDate().getDateLocal());
        this.setDepartureAirportIATA(flightStatus.getDepartureAirportFsCode());
        this.setDepartureTime(flightStatus.getDepartureDate().getDateLocal());

        this.setArrivalTimeUTC(Utils.parseLongFormatDate(flightStatus.getArrivalDate().getDateUtc()));
        this.setDepartureTimeUTC(Utils.parseLongFormatDate(flightStatus.getArrivalDate().getDateUtc()));

        this.setFlightId(flightStatus.getFlightId());
        this.setFlightCarrier(flightStatus.getCarrierFsCode());
        this.setFlightNumber(flightStatus.getFlightNumber());

        AirportResources ar = flightStatus.getAirportResources();
        if (ar != null) {
            this.setDepartureGate(ar.getDepartureGate());
            this.setDepartureTerminal(ar.getDepartureTerminal());
            this.setArrivalTerminal(ar.getArrivalTerminal());
        }

        if (!TextUtils.isEmpty(seatNo)) {
            this.setSeatNumber(seatNo);
        }
        if (appendix != null) {
            setAppendinxData(appendix);
        }
    }

    private void setAppendinxData(Appendix appendix) {
        List<Airport> airports = appendix.getAirports();
        List<Airline> airlines = appendix.getAirlines();
        // Find airport name
        for (Airport a: airports) {
            if (a.getFs().equals(this.getArrivalAirportIATA())) {
                this.setArrivalCity(a.getCity());
                Date localDate = Utils.getUTCDate(this.getArrivalTime(), a.getTimeZoneRegionName());
                this.setArrivalTimeUTC(localDate);

            }
            if (a.getFs().equals(this.getDepartureAirportIATA())) {
                this.setDepartureCity(a.getCity());
                Date localDate = Utils.getUTCDate(this.getDepartureTime(), a.getTimeZoneRegionName());
                this.setDepartureTimeUTC(localDate);
            }
        }

        // Find carrier name
        for (Airline al: airlines) {
            if (al.getFs().equals(this.getFlightCarrier())) {
                this.setFlightCarrierName(al.getName());
                break;
            }
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getFlightCarrier() {
        return flightCarrier;
    }

    public void setFlightCarrier(String flightCarrier) {
        this.flightCarrier = flightCarrier;
    }

    public String getFlightCarrierName() {
        return flightCarrierName;
    }

    public void setFlightCarrierName(String flightCarrierName) {
        this.flightCarrierName = flightCarrierName;
    }

    public String getAirlineIconUrl() {
        return String.format("http://www.gstatic.com/flights/airline_logos/70px/%s.png", flightCarrier);
    }

    public String getDepartureTerminal() {
        return departureTerminal;
    }

    public void setDepartureTerminal(String departureTerminal) {
        this.departureTerminal = departureTerminal;
    }

    public String getArrivalTerminal() {
        return arrivalTerminal;
    }

    public void setArrivalTerminal(String arrivalTerminal) {
        this.arrivalTerminal = arrivalTerminal;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public Integer getTripId() {
        return tripId;
    }

    public void setTripId(Integer tripId) {
        this.tripId = tripId;
    }

    public Integer getFlightId() {
        return flightId;
    }

    public void setFlightId(Integer flightId) {
        this.flightId = flightId;
    }

    public String getDepartureGate() {
        return departureGate;
    }

    public void setDepartureGate(String departureGate) {
        this.departureGate = departureGate;
    }

    public String getDepartureAirportIATA() {
        return departureAirportIATA;
    }

    public void setDepartureAirportIATA(String departureAirportIATA) {
        this.departureAirportIATA = departureAirportIATA;
    }

    public String getDepartureCity() {
        return departureCity;
    }

    public void setDepartureCity(String departureCity) {
        this.departureCity = departureCity;
    }

    public String getArrivalAirportIATA() {
        return arrivalAirportIATA;
    }

    public void setArrivalAirportIATA(String arrivalAirportIATA) {
        this.arrivalAirportIATA = arrivalAirportIATA;
    }

    public String getArrivalCity() {
        return arrivalCity;
    }

    public void setArrivalCity(String arrivalCity) {
        this.arrivalCity = arrivalCity;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public Date getDepartureTimeUTC() {
        return departureTimeUTC;
    }

    public void setDepartureTimeUTC(Date departureTimeUTC) {
        this.departureTimeUTC = departureTimeUTC;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Date getArrivalTimeUTC() {
        return arrivalTimeUTC;
    }

    public void setArrivalTimeUTC(Date arrivalTimeUTC) {
        this.arrivalTimeUTC = arrivalTimeUTC;
    }

    public String getBaggage() {
        return baggage;
    }

    public void setBaggage(String baggage) {
        this.baggage = baggage;
    }

    public String getArrivalGate() {
        return arrivalGate;
    }

    public void setArrivalGate(String arrivalGate) {
        this.arrivalGate = arrivalGate;
    }

    public String getArrivalCityImageUrl() {
        return arrivalCityImageUrl;
    }

    public void setArrivalCityImageUrl(String arrivalCityImageUrl) {
        this.arrivalCityImageUrl = arrivalCityImageUrl;
    }

    //=================== DB operations ========================

    public static Flight get(Integer id) {
        return SQLite.select().from(Flight.class).where(Flight_Table.id.eq(id)).querySingle();
    }

    public static List<Flight> getAll() {
        return SQLite.select().from(Flight.class).queryList();
    }

    public static void mergeFlights(Flight to, Flight from) {
        to.setArrivalTimeUTC((from.getArrivalTimeUTC() != null ? from.getArrivalTimeUTC() : to.getDepartureTimeUTC()));
        to.setArrivalTime((!TextUtils.isEmpty(from.getArrivalTime()) ? from.getArrivalTime() : to.getArrivalTime()));
        to.setDepartureTimeUTC((from.getDepartureTimeUTC() != null ? from.getDepartureTimeUTC() : to.getDepartureTimeUTC()));
        to.setDepartureTime((!TextUtils.isEmpty(from.getDepartureTime()) ? from.getDepartureTime() : to.getDepartureTime()));
        to.setArrivalTerminal((!TextUtils.isEmpty(from.getArrivalTerminal()) ? from.getArrivalTerminal() : to.getArrivalTerminal()));
        to.setDepartureTerminal((!TextUtils.isEmpty(from.getDepartureTerminal()) ? from.getDepartureTerminal() : to.getDepartureTerminal()));
        to.setArrivalAirportIATA((!TextUtils.isEmpty(from.getArrivalAirportIATA()) ? from.getArrivalAirportIATA() : to.getArrivalAirportIATA()));
        to.setDepartureAirportIATA((!TextUtils.isEmpty(from.getDepartureAirportIATA()) ? from.getDepartureAirportIATA() : to.getDepartureAirportIATA()));
        to.setArrivalCity((!TextUtils.isEmpty(from.getArrivalCity()) ? from.getArrivalCity() : to.getArrivalCity()));
        to.setDepartureCity((!TextUtils.isEmpty(from.getDepartureCity()) ? from.getDepartureCity() : to.getDepartureCity()));
        to.setArrivalGate((!TextUtils.isEmpty(from.getArrivalGate()) ? from.getArrivalGate() : to.getArrivalGate()));
        to.setDepartureGate((!TextUtils.isEmpty(from.getDepartureGate()) ? from.getDepartureGate() : to.getDepartureGate()));
        to.setFlightCarrier((!TextUtils.isEmpty(from.getFlightCarrier()) ? from.getFlightCarrier() : to.getFlightCarrier()));
        to.setFlightCarrierName((!TextUtils.isEmpty(from.getFlightCarrierName()) ? from.getFlightCarrierName() : to.getFlightCarrierName()));
        to.setFlightId((from.getFlightId() != null ? from.getFlightId() : to.getFlightId()));
        to.setSeatNumber((!TextUtils.isEmpty(from.getSeatNumber()) ? from.getSeatNumber() : to.getSeatNumber()));
        to.setBaggage((!TextUtils.isEmpty(from.getBaggage()) ? from.getBaggage() : to.getBaggage()));
    }
}

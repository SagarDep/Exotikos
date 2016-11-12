package com.exotikosteam.exotikos.models.trip;

import com.exotikosteam.exotikos.models.ExotikosDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.OneToMany;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lramaswamy on 11/9/16.
 */

@Parcel(analyze = {TripStatus.class})
@Table(database = ExotikosDatabase.class)
public class TripStatus extends BaseModel {

    @Column
    @PrimaryKey(autoincrement = true)
    Integer id;

    @Column(name = "passenger_name")
    String passengerName;

    @Column
    Integer flightStepOrdinal;

    FlightStep flightStep;

    @ForeignKey(saveForeignKeyModel = false)
    Flight currentFlight;

    List<Flight> flights = new ArrayList<>();

    //Empty constructor for Parceler
    public TripStatus() {}

    public TripStatus(List<Flight> flights) {
        this.flights = flights;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public void setFlightStep(FlightStep flightStep) {
        this.flightStepOrdinal = flightStep.ordinal();
        this.flightStep = flightStep;
    }

    @OneToMany(methods = OneToMany.Method.ALL, variableName = "flights")
    public List<Flight> getFlights() {
        if (flights == null) {
            flights = SQLite.select()
                    .from(Flight.class)
                    .where(Flight_Table.trip_id.eq(id))
                    .orderBy(Flight_Table.order, true)
                    .queryList();
        }
        return flights;
    }

    public void setFlights(List<Flight> flights) {
        this.flights = flights;
    }

    public FlightStep getFlightStep() {
        return flightStep;
    }

    public Flight getCurrentFlight() {
        return currentFlight;
    }

    public void setCurrentFlight(Flight currentFlight) {
        this.currentFlight = currentFlight;
    }

    public static TripStatus get(Integer id) {
        return SQLite.select().from(TripStatus.class).where(TripStatus_Table.id.eq(id)).querySingle();
    }

    public static List<TripStatus> getAll() {
        return SQLite.select().from(TripStatus.class).queryList();
    }

    public static void save(TripStatus tripStatus) {
        tripStatus.save();
    }

    public static TripStatus newMockInstance() {
        TripStatus trip = new TripStatus();
        trip.setFlightStep(FlightStep.CHECK_IN);
        List<Flight> flights = new ArrayList<Flight>();
        //String arrivalDate, String departureDate, String flightNumber, String departureTime,
        // String departureTerminal, String arrivalTime, String arrivalTerminal, String seatNumber
        flights.add(0, Flight.newInstance("", "January 19, 2017 ", "BX456", "6:44 AM", "A14", "12:30 PM","", "23B"));
        flights.add(1, Flight.newInstance("January 19, 2017", "January 19, 2017", "ZK250", "1:44 PM", "B9", "3:45 PM","A23", "6C"));
        flights.add(2, Flight.newInstance("January 19, 2017", "January 19, 2017", "BN05", "6:44 PM", "C34", "8:45 PM","D43", "2B"));
        trip.setFlights(flights);
        return trip;
    }
}

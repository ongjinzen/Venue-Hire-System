/**
 *
 */
package unsw.venues;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Venue Hire System for COMP2511.
 *
 * A basic prototype to serve as the "back-end" of a venue hire system. Input
 * and output is in JSON format.
 *
 * @author Robert Clifton-Everest
 *
 */
public class VenueHireSystem {

    /**
     * Constructs a venue hire system. Initially, the system contains no venues,
     * rooms, or bookings.
     */
	
	List<Venue> venues;
	List<Reservation> reservations;
	
    public VenueHireSystem() {
        this.venues = new ArrayList<Venue>();
        this.reservations = new ArrayList<Reservation>();
    }

    private void processCommand(JSONObject json) {
        switch (json.getString("command")) {

        case "room":
            String venue = json.getString("venue");
            String room = json.getString("room");
            String size = json.getString("size");
            addRoom(venue, room, size);
            break;

        case "request":
            String id = json.getString("id");
            LocalDate start = LocalDate.parse(json.getString("start"));
            LocalDate end = LocalDate.parse(json.getString("end"));
            int small = json.getInt("small");
            int medium = json.getInt("medium");
            int large = json.getInt("large");

            JSONObject result = request(id, start, end, small, medium, large);

            System.out.println(result.toString(2));
            break;
            
        case "list":
        	System.out.println("list called");

        // TODO Implement other commands
        }
    }

    private void addRoom(String venue, String room, String size) {
        
    	boolean added = false;
    	
    	for(Venue i : venues) {
    		if (i.getName() == venue) {
    			i.addRoom(room, size);
    			added = true;
    		}
    	}
    	
    	if (added == false) {
    		Venue newVenue = new Venue(venue);
    		venues.add(newVenue);
    		newVenue.addRoom(room, size);
    	}
    }

    public JSONObject request(String id, LocalDate start, LocalDate end,
            int small, int medium, int large) {
    	
        JSONObject result = new JSONObject();
        boolean added = false;
        
        for(Venue i : venues) {
        	if(i.checkAvailability(start, end, small, medium, large)) {
        		Reservation newReservation = makeReservation(id, start, end, i, small, medium, large);
        		result.put("status", "success");
        		result.put("venue", i.name);
        		JSONArray rooms = new JSONArray();
        		for (Room j : newReservation.getBookedRooms()) {
        			rooms.put(j.getName());
        		}
        		result.put("rooms", rooms);
        		reservations.add(newReservation);
        		added = true;
        	}
        }
        
        if(added == false) {
        	result.put("status", "rejected");
        }
        
        return result;
    }
    
    private Reservation makeReservation(String id, LocalDate start, LocalDate end,
    		Venue venue, int small, int medium, int large) {
    	
    	Reservation newReservation = new Reservation(id, venue, start, end);

    	for(Room i : venue.rooms) {
    		if ((i.size == "small") && (small > 0)){
    			newReservation.addRoom(i);
    		} else if ((i.size == "medium") && (medium > 0)){
    			newReservation.addRoom(i);
    		} else if ((i.size == "large") && (large > 0)){
    			newReservation.addRoom(i);
    		}
    	}

		
    	for (Room i : newReservation.getBookedRooms()) {
    		i.addReservation(newReservation);
    	}

    	return newReservation;    	
    }

    public static void main(String[] args) {
        VenueHireSystem system = new VenueHireSystem();

        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (!line.trim().equals("")) {
                JSONObject command = new JSONObject(line);
                system.processCommand(command);
            }
        }
        sc.close();
    }

}

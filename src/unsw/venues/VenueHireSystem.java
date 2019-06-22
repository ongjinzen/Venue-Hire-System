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

            System.out.println(result.toString());
            break;
            
        case "change":
        	System.out.println("change called");
        	String id1 = json.getString("id");
            LocalDate start1 = LocalDate.parse(json.getString("start"));
            LocalDate end1 = LocalDate.parse(json.getString("end"));
            int small1 = json.getInt("small");
            int medium1 = json.getInt("medium");
            int large1 = json.getInt("large");

            JSONObject result1 = change(id1, start1, end1, small1, medium1, large1);
            
            System.out.println(result1.toString());
        	
        	break;
        	
        case "cancel":
        	String id2 = json.getString("id");
        	JSONObject result2 = cancel(id2);
        	System.out.println(result2.toString());
        	break;
        	
        case "list":
        	System.out.println("Hello");
        	break;
        }
    }

    private void addRoom(String venue, String room, String size) {
        
    	boolean added = false;
    	
    	for(Venue i : venues) {
    		if (i.getName().equals(venue)) {
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
        		break;
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

    	for(Room i : venue.getRooms()) {
    		if ((i.getSize().equals("small")) && (small > 0) && (i.checkAvailable(start, end))){
    			newReservation.addRoom(i);
    			small--;
    		} else if ((i.getSize().equals("medium")) && (medium > 0) && (i.checkAvailable(start, end))){
    			newReservation.addRoom(i);
    			medium--;
    		} else if ((i.getSize().equals("large")) && (large > 0) && (i.checkAvailable(start, end))){
    			newReservation.addRoom(i);
    			large--;
    		}
    	}

		
    	for (Room i : newReservation.getBookedRooms()) {
    		i.addReservation(newReservation);
    	}

    	return newReservation;    	
    }
    
    public JSONObject cancel(String id) {
    	JSONObject result = new JSONObject();
    	
    	boolean found = false;
    	Reservation target = null;
    	for(Reservation i : reservations) {
    		if(i.getId().equals(id)) {
    			target = i;
    		}
    	}
    	
    	for(Room j : target.bookedRooms) {
			j.removeReservation(target);
			found = true;
		}
		
    	
    	if(found == true) {
    		reservations.remove(target);
    		System.out.println("Hello");
    		found = true;
    		result.put("status", "success");
    	} else {
    		result.put("status", "rejected");
    	}
    	
    	return result;
    }
    
    public JSONObject change(String id, LocalDate start, LocalDate end, int small, int medium, int large) {
    	
    	JSONObject result = new JSONObject();
    	boolean foundOriginal = false;
    	Reservation original = null;
    	
    	for(Reservation i : reservations) {
    		if(i.getId().equals(id)) {
    			original = i;
    			foundOriginal = true;
    		}
    	}
    	
    	if(foundOriginal == true) {
			cancel(original.getId());
			result = request(id, start, end, small, medium, large);
			
			if(result.getString("status").equals("rejected")) {
				reservations.add(original);
				for(Room i : original.getBookedRooms()) {
					i.addReservation(original);
				}
			}
			
    	} else {
    		result.put("status", "rejected");
    	}
    	
    	return result;
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

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

	private List<Venue> venues;
	private List<Reservation> reservations;
	
    /**
     * Instantiates a new VenueHireSystem
     * and initializes a new list of venues
     * and a new list of reservations
     */
    public VenueHireSystem() {
        this.venues = new ArrayList<Venue>();
        this.reservations = new ArrayList<Reservation>();
    }

    /**
     * Processes the command parsed in.
     * @param json Command to be carried out
     */
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
        	String venue3 = json.getString("venue");
        	JSONArray result3 = list(venue3);
        	System.out.println(result3.toString(1));
        	break;
        }
    }

    /**
     * Add a new room to the system
     * <p>
     * This method adds a room to the system
     * by adding it to a list of rooms in a venue.
     * If the venue does not already exist, it will
     * be instantiated.
     * @param venue Name of venue
     * @param room Name of room
     * @param size Size of room
     */
    public void addRoom(String venue, String room, String size) {
        
    	boolean added = false;
    	
    	// Look for the venue and add a room to the venue.
    	for(Venue i : venues) {
    		if (i.getName().equals(venue)) {
    			i.addRoom(room, size);
    			added = true;
    		}
    	}
    	
    	// If venue does not exist, create the venue
    	// and add the room to it.
    	if (added == false) {
    		Venue newVenue = new Venue(venue);
    		venues.add(newVenue);
    		newVenue.addRoom(room, size);
    	}
    }

    /**
     * This method checks if a request for a reservation
     * can be fulfilled. If it can be fulfilled, the reservation
     * will be made. If it cannot be fulfilled, the reservation
     * will not be made.
     * @param id ID of reservation
     * @param start Start date
     * @param end End date
     * @param small Number of small rooms required
     * @param medium Number of medium rooms required
     * @param large Number of large rooms required
     * @return result of booking
     */
    public JSONObject request(String id, LocalDate start, LocalDate end,
            int small, int medium, int large) {
    	
        JSONObject result = new JSONObject();
        boolean added = false;
        
        /**
         * Check for venues that are able to fulfill the request.
         * Make a reservation at the first available venue.
         * Add reservation to list of reservations in rooms used
         * Add the reservation object to the list of reservations
         * in the system.
         */
        for(Venue i : venues) {
        	if(i.checkAvailability(start, end, small, medium, large)) {
        		Reservation newReservation = makeReservation(id, start, end, i, small, medium, large);
        		result.put("status", "success");
        		result.put("venue", i.getName());
        		JSONArray rooms = new JSONArray();
        		for (Room j : newReservation.getBookedRooms()) {
        			j.addReservation(newReservation);
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
    
    /**
     * Instantiates a reservation.
     * @param id ID of reservation
     * @param start Start date
     * @param end End date
     * @param venue Name of venue
     * @param small Number of small rooms required
     * @param medium Number of medium rooms required
     * @param large Number of large rooms required
     * @return Reservation object
     */
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

    	return newReservation;    	
    }
    
    /**
     * Cancels a reservation by removing
     * reservation object with the given ID
     * from the lists of reservations in the
     * list of rooms in the reservation object
     * as well as the list of reservations in
     * the system.
     * @param id ID of reservation
     * @return Result of cancellation
     */
    public JSONObject cancel(String id) {
    	JSONObject result = new JSONObject();
    	
    	boolean found = false;
    	Reservation target = null;
    	for(Reservation i : reservations) {
    		if(i.getId().equals(id)) {
    			target = i;
    		}
    	}
    	
    	for(Room j : target.getBookedRooms()) {
			j.removeReservation(target);
			found = true;
		}
		
    	
    	if(found == true) {
    		reservations.remove(target);
    		found = true;
    		result.put("status", "success");
    	} else {
    		result.put("status", "rejected");
    	}
    	
    	return result;
    }
    
    /**
     * @param id ID of reservation
     * @param start New start date
     * @param end New end date
     * @param small New number of small rooms required
     * @param medium New number of medium rooms required
     * @param large New number of large rooms required
     * @return Result of change
     */
    public JSONObject change(String id, LocalDate start, LocalDate end, int small, int medium, int large) {
    	
    	JSONObject result = new JSONObject();
    	boolean foundOriginal = false;
    	Reservation original = null;
    	
    	// Looking for the reservation
    	for(Reservation i : reservations) {
    		if(i.getId().equals(id)) {
    			original = i;
    			foundOriginal = true;
    		}
    	}
    	
    	/**
    	 * Cancel original reservation and
    	 * try to make a new reservation with
    	 * the new requirements.
    	 * If no suitable venues are found,
    	 * add the old reservation back to the lists
    	 */
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
    
    /**
     * Create a list of reservations for
     * each room in a venue.
     * Add the lists into a list to be
     * put into the JSONArray
     * @param venue Name of venue
     * @return command result
     */
    public JSONArray list(String venue) {
    	
    	JSONArray result = new JSONArray();
    	boolean venueFound = false;
    	Venue venObject = null;
    	
    	for(Venue i : venues) {
    		if(i.getName().equals(venue)) {
    			venueFound = true;
    			venObject = i;
    		}
    	}
    	
    	if (venueFound == true);{
    		for(Room i : venObject.getRooms()) {
    			JSONObject room = new JSONObject();
    			JSONArray listReservations = new JSONArray();
    			for(Reservation k : i.getReservations()) {
    				JSONObject reservation = new JSONObject();
    				reservation.put("id", k.getId());
    				reservation.put("start", k.getStart());
    				reservation.put("end", k.getEnd());
    				listReservations.put(reservation);
    			}    			
    			room.put("reservations", listReservations);
    			room.put("room", i.getName());    			
    			result.put(room);
        	}
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

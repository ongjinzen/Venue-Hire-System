package unsw.venues;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Venue {
	
	private String name;
	private List<Room> rooms;
	
	/**
	 * Initialises a new venue.
	 * Name of venue will be the string parsed in.
	 * Initialises a new list of rooms in the venue.
	 * @param name Name of venue
	 */
	public Venue(String name) {
		this.name = name;
		this.rooms = new ArrayList<Room>();
	}
	
	/**
	 * @return Name of venue
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return List of rooms
	 */
	public List<Room> getRooms() {
		return rooms;
	}
	
	/**
	 * Add a room to the venue.
	 * @param name Name of room
	 * @param size Size of room
	 */
	public void addRoom(String name, String size) {
		
		boolean alreadyExists = false;
		
		for(Room i : rooms) {
			if(i.getName().equals(name)) {
				alreadyExists = true;
			}
		}
		
		if(alreadyExists == false) {
			Room newRoom = new Room(name, size);
			rooms.add(newRoom);
		}
	}
	
	/**
	 * Check if the venue has enough rooms of each size at
	 * the given dates to accommodate the specified required
	 * number of rooms.
	 * @param start Start date
	 * @param end End date
	 * @param small Number of small rooms required.
	 * @param medium Number of medium rooms required.
	 * @param large Number of large rooms required.
	 * @return Returns true if a venue can accomodate a request.
	 */
	public boolean checkAvailability(LocalDate start, LocalDate end,
			int small, int medium, int large) {
		
		boolean available = true;
		int availableSmall = countAvailable(start, end, "small");
		int availableMedium = countAvailable(start, end, "medium");
		int availableLarge = countAvailable(start, end, "large");
		
		if ((small > availableSmall) || (medium > availableMedium)
				|| (large > availableLarge)) {
			available = false;
		}

		return available;
	}
	
	/**
	 * Counts the number of rooms of a specified size
	 * that are available for booking between two
	 * specified dates.
	 * @param start Start date
	 * @param end End date
	 * @param size Size of room
	 * @return Number of rooms available
	 */
	private int countAvailable(LocalDate start, LocalDate end, String size) {
		
		int roomsAvailable = 0;

		for (Room i : rooms) {
			if ((i.getSize().equals(size)) && (i.checkAvailable(start, end))){
				roomsAvailable++;
			}
		}
		
		return roomsAvailable;
	}

	
}

package unsw.venues;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Venue {
	
	private String name;
	private List<Room> rooms;
	
	public Venue(String name) {
		this.name = name;
		this.rooms = new ArrayList<Room>();
	}
	
	public String getName() {
		return name;
	}

	public List<Room> getRooms() {
		return rooms;
	}
	
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

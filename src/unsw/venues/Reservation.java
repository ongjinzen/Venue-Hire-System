package unsw.venues;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Reservation {

	String id;
	Venue venue;
	List<Room> bookedRooms;
	LocalDate start;
	LocalDate end;
	
	public Reservation(String id, Venue venue,
			LocalDate start, LocalDate end) {
		
		this.id = id;
		this.venue = venue;
		this.start = start;
		this.end = end;
		this.bookedRooms = new ArrayList<Room>();
	}
	
	public void addRoom(Room room) {
		bookedRooms.add(room);
	}

	public List<Room> getBookedRooms() {
		return bookedRooms;
	}
	
}

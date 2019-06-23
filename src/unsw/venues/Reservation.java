package unsw.venues;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Reservation {

	private String id;
	private List<Room> bookedRooms;
	private LocalDate start;
	private LocalDate end;
	
	public Reservation(String id, Venue venue,
			LocalDate start, LocalDate end) {
		
		this.id = id;
		this.start = start;
		this.end = end;
		this.bookedRooms = new ArrayList<Room>();
	}
	
	public String getId() {
		return id;
	}
	public void addRoom(Room room) {
		bookedRooms.add(room);
	}

	public List<Room> getBookedRooms() {
		return bookedRooms;
	}
	
	public LocalDate getStart() {
		return start;
	}
	
	public LocalDate getEnd() {
		return end;
	}
}

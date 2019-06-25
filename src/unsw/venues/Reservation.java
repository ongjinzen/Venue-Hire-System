package unsw.venues;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Reservation {

	private String id;
	private List<Room> bookedRooms;
	private LocalDate start;
	private LocalDate end;
	
	/**
	 * Initializes a new reservation.
	 * @param id Reservation ID
	 * @param venue Name of venue
	 * @param start Start date
	 * @param end End date
	 */
	public Reservation(String id, Venue venue,
			LocalDate start, LocalDate end) {
		
		this.id = id;
		this.start = start;
		this.end = end;
		this.bookedRooms = new ArrayList<Room>();
	}
	
	/**
	 * @return Reservation ID
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param room Room to be used in reservation
	 */
	public void addRoom(Room room) {
		bookedRooms.add(room);
	}

	/**
	 * @return List of booked rooms in reservation
	 */
	public List<Room> getBookedRooms() {
		return bookedRooms;
	}
	
	/**
	 * @return Start date of reservation
	 */
	public LocalDate getStart() {
		return start;
	}
	
	/**
	 * @return End date of reservation
	 */
	public LocalDate getEnd() {
		return end;
	}
}

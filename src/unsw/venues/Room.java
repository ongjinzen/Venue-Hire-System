package unsw.venues;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Room {

	private String name;
	private String size;
	private List<Reservation> reservations;
	
	/**
	 * Initializes a new room of a specified
	 * name and size.
	 * @param name Name of room
	 * @param size Size of room
	 */
	public Room(String name, String size) {
		
		this.name = name;
		this.size = size;
		this.reservations = new ArrayList<Reservation>();
	}
	
	/**
	 * @return Name of room
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return Size of room
	 */
	public String getSize() {
		return size;
	}

	/**
	 * Returns a list of reservations that uses the room
	 * @return List of reservations
	 */
	public List<Reservation> getReservations() {
		return reservations;
	}

	/**
	 * Adds a reservation to the reservations list
	 * in the room.
	 * @param reservation Reservation to be added
	 */
	public void addReservation(Reservation reservation) {
		
		boolean emptyList = true;
		/**
		 * Inserts reservation into a chronologically ordered
		 * list of reservations according to the start date.
		 */
		for(Reservation i : reservations) {
			if(i.getStart().isAfter(reservation.getStart())) {
				reservations.add(reservations.indexOf(i), reservation);
				emptyList = false;
				break;
			}
		}
		
		if(emptyList == true) {
			reservations.add(reservation);
		}
	}
	
	/**
	 * Removes a reservation from the list of reservations
	 * in the room.
	 * @param reservation Reservation to be removed.
	 */
	public void removeReservation(Reservation reservation) {
		reservations.remove(reservation);
	}
	
	/**
	 * Check if a room is available between two specified dates.
	 * @param start Start date
	 * @param end End date
	 * @return Returns true if the room is available.
	 */
	public boolean checkAvailable(LocalDate start, LocalDate end) {
		
		boolean available = true;
		
		for(Reservation i : reservations) {
			if (checkClash(start, end, i.getStart(), i.getEnd())) {
				available = false;
			}
		}
		
		return available;
	}
	
	/**
	 * Check if there is any clash between a new pair of dates
	 * and the dates of a prior reservation.
	 * @param newStart New start date
	 * @param newEnd New start date
	 * @param start Old start date
	 * @param end Old start date
	 * @return Returns true if there is a clash.
	 */
	private boolean checkClash(LocalDate newStart, LocalDate newEnd,
			LocalDate start, LocalDate end) {
		
		if (newEnd.isBefore(start) || newStart.isAfter(end)) {
			return false;
		} else {
			return true;
		}
	}
}

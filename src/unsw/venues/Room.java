package unsw.venues;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Room {

	private String name;
	private String size;
	private List<Reservation> reservations;
	
	public Room(String name, String size) {
		
		this.name = name;
		this.size = size;
		this.reservations = new ArrayList<Reservation>();
	}
	
	public String getName() {
		return name;
	}

	public String getSize() {
		return size;
	}

	public List<Reservation> getReservations() {
		return reservations;
	}

	public void addReservation(Reservation reservation) {
		
		boolean emptyList = true;
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
	
	public void removeReservation(Reservation reservation) {
		reservations.remove(reservation);
	}
	
	public boolean checkAvailable(LocalDate start, LocalDate end) {
		
		boolean available = true;
		
		for(Reservation i : reservations) {
			if (checkClash(start, end, i.getStart(), i.getEnd())) {
				available = false;
			}
		}
		
		return available;
	}
	
	private boolean checkClash(LocalDate newStart, LocalDate newEnd,
			LocalDate start, LocalDate end) {
		
		if (newEnd.isBefore(start) || newStart.isAfter(end)) {
			return false;
		} else {
			return true;
		}
	}
}

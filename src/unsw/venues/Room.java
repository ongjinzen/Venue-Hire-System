package unsw.venues;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Room {

	String name;
	String size;
	List<Reservation> reservations;
	
	public Room(String name, String size) {
		
		this.name = name;
		this.size = size;
		this.reservations = new ArrayList<Reservation>();
		System.out.println(size + name);
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
		reservations.add(reservation);
	}
	
	public boolean checkAvailable(LocalDate start, LocalDate end) {
		
		boolean available = true;
		
		for(Reservation i : reservations) {
			if (checkClash(start, end, i.start, i.end)) {
				available = false;
			}
		}
		
		return available;
	}
	
	private boolean checkClash(LocalDate newStart, LocalDate newEnd,
			LocalDate start, LocalDate end) {
		
		if (newEnd.isBefore(start) || newStart.isAfter(end)) {
			return true;
		} else {
			return false;
		}
		
	}
	
}

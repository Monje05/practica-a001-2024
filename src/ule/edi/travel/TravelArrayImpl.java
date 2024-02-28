package ule.edi.travel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ule.edi.model.*;

public class TravelArrayImpl implements Travel {
	
	private static final Double DEFAULT_PRICE = 100.0;
	private static final Byte DEFAULT_DISCOUNT = 25;
	private static final Byte CHILDREN_EXMAX_AGE = 18;
	private Date travelDate;
	private int nSeats;
	
	private Double price;    // precio de entradas 
	private Byte discountAdvanceSale;   // descuento en venta anticipada (0..100)
   	
	private Seat[] seats;
		
	
	
   public TravelArrayImpl(Date date, int nSeats){

	   this.travelDate = date;
	   this.nSeats = nSeats;
	   this.seats = new Seat[this.nSeats];
	   this.price = DEFAULT_PRICE;
	   this.discountAdvanceSale = DEFAULT_DISCOUNT;

   }
   
   
   public TravelArrayImpl(Date date, int nSeats, Double price, Byte discount){

	   this.travelDate = date;
	   this.nSeats = nSeats;
	   this.price = price;
	   this.discountAdvanceSale = discount;
	   this.seats = new Seat[this.nSeats];
	   
   }

   
@Override
public Byte getDiscountAdvanceSale() {
	return this.discountAdvanceSale;
}

@Override
public int getNumberOfSoldSeats() {
	int contador = 0;
	for(int i = 0; i < this.nSeats; i++) {
		if(this.seats[i] != null) {
			contador++;
		}
	}
	return contador;
}


@Override
public int getNumberOfNormalSaleSeats() {
	int contador = 0;
	for(int i = 0; i < this.nSeats; i++) {
		if(this.seats[i] != null && this.seats[i].getAdvanceSale() == false){
			contador++;
		}
	}
	return contador;
}


@Override
public int getNumberOfAdvanceSaleSeats() {
	int contador = 0;
	for(int i = 0; i < this.nSeats; i++) {
		if(this.seats[i] != null && this.seats[i].getAdvanceSale() == true){
			contador++;
		}
	}
	return contador;
}


@Override
public int getNumberOfSeats() {
	return nSeats;
}


@Override
public int getNumberOfAvailableSeats() {
	int contador = 0;
	for(int i = 0; i < this.nSeats; i++) {
		if(seats[i] == null) {
			contador++;
		}
	}
	return contador;

}

@Override
public Seat getSeat(int pos) {
	return this.seats[pos - 1];
}


@Override
public Person refundSeat(int pos) {

	Person refund = null;

	if(pos > 0 && pos < this.nSeats) {

		Seat seat = this.seats[pos - 1];
		if(seat != null) {
			Person person = seat.getHolder();
			this.seats[pos - 1] = null;
			refund = person;
		} 
	}
	return refund;
}



private boolean isChildren(int age) {
	boolean children = false;
	if(age < CHILDREN_EXMAX_AGE ) {
		children = true;
	}
	return children;
}

private boolean isAdult(int age) {
	boolean adult = false;
	if(age >= CHILDREN_EXMAX_AGE) {
		adult = true;
	}
	return adult;
}


@Override
public List<Integer> getAvailableSeatsList() {
	List<Integer> lista=new ArrayList<Integer>(nSeats);

	for(int i = 0; i < this.nSeats; i++) {
		if(this.seats[i] == null) {
			lista.add(i + 1);
		}
	}
	
	return lista;
}


@Override
public List<Integer> getAdvanceSaleSeatsList() {
	List<Integer> lista=new ArrayList<Integer>(nSeats);

	for(int i = 0; i < this.nSeats;i++) {
		if(this.seats[i].getAdvanceSale() == true) {
			lista.add(i + 1);
		}
	}
	
	
	return lista;
}


@Override
public int getMaxNumberConsecutiveSeats() {
	int consecutiveSeats = 0;
	int maxConsecutiveSeats = 0;
	for(int i = 0; i < this.nSeats;i++) {
		if(this.seats[i] !=  null) {
			consecutiveSeats++;
		}else{
			consecutiveSeats = 0;
		}

		maxConsecutiveSeats = Math.max(maxConsecutiveSeats, consecutiveSeats);
	}
	return maxConsecutiveSeats;
}




@Override
public boolean isAdvanceSale(Person p) {
	for(int i = 0; i < this.nSeats; i++) {
		if(this.seats[i] != null && this.seats[i].getHolder().equals(p) && this.seats[i].getAdvanceSale()) {
			return true;
		}
	}
	return false;
}


@Override
public Date getTravelDate() {
	return this.travelDate;
}


@Override
public boolean sellSeatPos(int pos, String nif, String name, int edad, boolean isAdvanceSale) {
	boolean correctSeat = false;
	if(pos > 0 && pos <= this.nSeats) {
		Seat seat = this.seats[pos - 1];

		if(seat == null) {
			Person newPerson = new Person(nif, name, edad);
			seat = new Seat(isAdvanceSale, newPerson);
			this.seats[pos - 1] = seat;
			correctSeat = true;
		}
	}
	return correctSeat;
}


@Override
public int getNumberOfChildren() {
	int childrens = 0;
	for(int i = 0; i < this.nSeats;i++){
		if(this.seats[i] != null){
			int age = this.seats[i].getHolder().getAge();
			if(isChildren(age)){
			 childrens++;
			}
		}
	}
	return childrens;
}


@Override
public int getNumberOfAdults() {
	int adults = 0;
	for(int i = 0; i < this.nSeats;i++){
		if(this.seats[i] != null){
			int age = this.seats[i].getHolder().getAge();
			if(isAdult(age)){
			 adults++;
			}
		}
	}
	return adults;
}



@Override
public Double getCollectionTravel() {
	Double total = 0.0;
	Double discount = this.getPrice()*(100 - this.discountAdvanceSale)/100;
	for(int i = 0; i < this.nSeats; i++) {
		if(this.seats[i] != null) {
			if(this.seats[i].getAdvanceSale()) {
				total = total + discount;
			} else {
				total = total + getPrice();
			}
		}
	}
	
	return total;
}


@Override
public int getPosPerson(String nif) {
	
	int pos = -1;

	for(int i = 0; i < this.nSeats; i++) {
		if(this.seats[i] != null) {
			if(this.seats[i].getHolder().getNif().equals(nif)) {
				pos = i + 1;
			}
		}
	}
	return pos;	
}


@Override
public int sellSeatFrontPos(String nif, String name, int edad, boolean isAdvanceSale) {

	int pos = -1;

	for(int i = 0; i <  this.nSeats; i++) {
		if(this.seats[i] == null) {
			Person newPerson = new Person(nif, name, edad);
			Seat newSeat = new Seat(isAdvanceSale, newPerson);
			this.seats[i] = newSeat;
			pos = i + 1;
			break;

		}
	}
	return pos;
}


@Override
public int sellSeatRearPos(String nif, String name, int edad, boolean isAdvanceSale) {
	
	int pos = -1;

	for(int i = nSeats - 1; i > 0;i--) {
		if(this.seats[i] == null) {
			Person newPerson = new Person(nif, name, edad);
			Seat newSeat = new Seat(isAdvanceSale, newPerson);
			this.seats[i] = newSeat;
			pos = i + 1;
			break;
		}
	}

	return pos;
}




@Override
public Double getSeatPrice(Seat seat) {
	Double seatPrice = getPrice();
	Double discount = this.getPrice()*(100 - this.discountAdvanceSale/100);

	if(seat != null) {
		if(seat.getAdvanceSale()) {
			seatPrice = discount;
		}
	}

	return seatPrice;
}


@Override
public double getPrice() {
	return this.price;
}


}	
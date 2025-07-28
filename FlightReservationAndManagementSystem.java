import java.util.*;
public class FlightReservationAndManagementSystem {
}
class PassengerAlreadyExistsException extends Exception{
    public PassengerAlreadyExistsException(String msg){
        super(msg);
    }
}
class FlightAlreadyExistsException extends Exception{
    public FlightAlreadyExistsException(String msg){
        super(msg);
    }
}
class PassengerNotFoundException extends Exception{
    public PassengerNotFoundException(String msg){
        super(msg);
    }
}
class FlightNotFoundException extends Exception{
    public FlightNotFoundException(String msg){
        super(msg);
    }
}
class SeatNotAvailableException extends Exception{
    public SeatNotAvailableException(String msg){
        super(msg);
    }
}
class SeatAlreadyAvailableException extends Exception{
    public SeatAlreadyAvailableException(String msg){
        super(msg);
    }
}
class DuplicatePassengerException extends Exception{
    public DuplicatePassengerException(String msg){
        super(msg);
    }
}
class DuplicateFlightException extends Exception{
    public DuplicateFlightException(String msg){
        super(msg);
    }
}
class CapacityHasBeenReachedException extends Exception{
    public CapacityHasBeenReachedException(String msg){
        super(msg);
    }
}
class Flight{
    private String flightNumber;
    private Seat[] seats;
    private HashMap<Passenger,Seat> assignedSeats;
    private final int capacity;
    private long departureTime;
    private long arrivalTime;
    private String destination;
    private double price;
    private ArrayList<Passenger> businessPassengers;
    private ArrayList<Passenger> economyPassengers;
    private HashMap<Seat,Passenger> businessClass;
    private HashMap<Seat,Passenger> economyClass;
    public Flight(String flightNumber,int capacity,long departureTime,long arrivalTime,String destination,double price){
        this.flightNumber = flightNumber;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.destination = destination;
        this.price = price;
        this.capacity = capacity;
        this.seats = new Seat[capacity];
        this.businessPassengers = new ArrayList<>();
        this.economyPassengers = new ArrayList<>();
        this.businessClass = new HashMap<>();
        this.economyClass = new HashMap<>();
        assignSeatNumbers();
    }
    public String getFlightNumber(){
        return flightNumber;
    }
    public long getDepartureTime(){
        return departureTime;
    }
    public long getArrivalTime(){
        return arrivalTime;
    }
    public String getDestination(){
        return destination;
    }
    public Seat[] getSeats(){
        return seats;
    }
    public void addPassengerToBusinessClass(Passenger passenger) throws CapacityHasBeenReachedException{
        if(businessPassengers.size() == capacity/10){
            throw new CapacityHasBeenReachedException("Capacity for business class has been reached");
        }
        businessPassengers.add(passenger);
    }
    public void addPassengerToEconomyClass(Passenger passenger) throws CapacityHasBeenReachedException{
        if(economyPassengers.size()-capacity/10 == capacity){
            throw new CapacityHasBeenReachedException("Capacity for economy class has been reached");
        }
        economyPassengers.add(passenger);
    }
    public Passenger findPassengerByPassengerId(String passengerId) throws PassengerNotFoundException{
        for(Passenger i: economyPassengers){
            if(i.getPassengerId().equals(passengerId)){
                return i;
            }
        }
        for(Passenger i : businessPassengers){
            if(i.getPassengerId().equals(passengerId)){
                return i;
            }
        }
        throw new PassengerNotFoundException("Passenger not found");
    }
    public void assignSeatNumbers(){
        for(int i =0;i<capacity;i++){
            if(i<capacity/10){
                seats[i] = new Seat("B"+i);
                continue;
            }
            seats[i] = new Seat("E" + i);
        }
    }
    public void assignSeatsToPassengers() throws SeatNotAvailableException{
        int x = 0;
        businessPassengers.trimToSize();
        for(int i =0;i<capacity/10;i++){
            if(businessPassengers.get(i) == null){
                break;
            }
            if(seats[i].isOccupied()){
                continue;
            }
            while(x<businessPassengers.size()){
                if(businessPassengers.get(x).getSeat() == null){
                    businessPassengers.get(x).assignSeatToPassenger(seats[i]);
                    businessClass.put(seats[i],businessPassengers.get(x));
                    seats[i].occupy();
                    x++;
                    break;
                }
            }
        }
        economyPassengers.trimToSize();
        int y  =0;
        for(int i =capacity/10;i<capacity-capacity/10;i++){
            if(economyPassengers.get(i) == null){
                break;
            }
            if(seats[i].isOccupied()){
                continue;
            }
            while(y<economyPassengers.size()){
                if(economyPassengers.get(y).getSeat() == null){
                    economyPassengers.get(y).assignSeatToPassenger(seats[i]);
                    economyClass.put(seats[i],economyPassengers.get(y));
                    seats[i].occupy();
                    y++;
                    break;
                }
            }
        }
    }
    public boolean isSeatAvailable(String seatNumber){
        for(Seat i : seats){
            if(i.getSeatNumber().equals(seatNumber)){
                return i.isOccupied();
            }
        }
        return false;
    }
    public double calculatePrice(String seatNumber){
        if(seatNumber.contains("B")){
            return price*2.5;
        }
        return price;
    }
    public void assignSeatsToPassenger(String passengerId,Seat seat) throws CapacityHasBeenReachedException,PassengerNotFoundException{
        if(assignedSeats.values().size() == capacity){
            throw new CapacityHasBeenReachedException("Capacity has been reached");
        }
        assignedSeats.put(findPassengerByPassengerId(passengerId),seat);
    }
    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof Flight)){
            return false;
        }
        Flight other = (Flight) obj;
        return other.getFlightNumber().equals(flightNumber);
    }
}
class Passenger{
    private String name;
    private String passengerId;
    private Seat seat;
    public Passenger(String name,String passengerId){
        this.name = name;
        this.passengerId = passengerId;
    }
    public void assignSeatToPassenger(Seat seat){
        this.seat = seat;
    }
    public String getPassengerId(){
        return passengerId;
    }
    public String getName(){
        return name;
    }
    public Seat getSeat(){
        return seat;
    }
    public void displayInfo(){
        System.out.println("Passenger Id: " + passengerId + " Name: " + name + " Seat Number: " + seat.getSeatNumber());
    }
    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof Passenger)){
            return false;
        }
        Passenger other = (Passenger) obj;
        return other.getPassengerId().equals(passengerId)&&other.getName().equals(name);
    }
    @Override
    public String toString(){
        return name + "\n" + passengerId + "\n" + seat;
    }
}
class Seat{
    private String seatNumber;
    private boolean isOccupied;
    public Seat(String seatNumber){
        this.seatNumber = seatNumber;
        this.isOccupied = false;
    }
    public Seat(String seatNumber,boolean isOccupied){
        this.seatNumber = seatNumber;
        this.isOccupied = isOccupied;
    }
    public String getSeatNumber(){
        return seatNumber;
    }
    public boolean isOccupied(){
        return isOccupied;
    }
    public void occupy() throws SeatNotAvailableException{
        if(isOccupied){
            throw new SeatNotAvailableException("Seat not available");
        }
        this.isOccupied = true;
    }
    public void emptySeat() throws SeatAlreadyAvailableException{
        if(!isOccupied){
            throw new SeatAlreadyAvailableException("Seat is already available");
        }
        this.isOccupied = false;
    }
    public void displayInfo(){
        System.out.println("Seat Number: " + seatNumber + " Is occupied: " + isOccupied);
    }
    @Override
    public String toString(){
        return seatNumber + "\n"+ isOccupied;
    }
}
class AirlineManager{
    private ArrayList<Flight> flights;
    private ArrayList<Passenger> passengers;
    public AirlineManager(){
        this.flights = new ArrayList<>();
    }
    public void addFlight(Flight flight) throws DuplicateFlightException{
        if(flights.contains(flight)){
            throw new DuplicateFlightException("This flight is already registered to system");
        }
        flights.add(flight);
    }
    public void addPassenger(Passenger passenger) throws PassengerAlreadyExistsException{
        if(passengers.contains(passenger)){
            throw new PassengerAlreadyExistsException("Passenger already exists");
        }
        passengers.add(passenger);
    }
    public Passenger findPassengerByPassengerId(String passengerId) throws PassengerNotFoundException{
        for(Passenger i : passengers){
            if(i.getPassengerId().equals(passengerId)){
                return i;
            }
        }
        throw new PassengerNotFoundException("Passenger not found");
    }
    public Flight findFlightByFlightNumber(String flightNumber) throws FlightNotFoundException{
        for(Flight i : flights){
            if(i.getFlightNumber().equals(flightNumber)){
                return i;
            }
        }
        throw new FlightNotFoundException("Flight not found");
    }
    public void addAPassengerToBusinessClass(String flightNumber,String passengerId) throws PassengerNotFoundException,FlightNotFoundException,CapacityHasBeenReachedException{
        findFlightByFlightNumber(flightNumber).addPassengerToBusinessClass(findPassengerByPassengerId(passengerId));
    }
    public void addPassengerToEconomyClass(String flightNumber,String passengerId) throws PassengerNotFoundException,FlightNotFoundException,CapacityHasBeenReachedException{
        findFlightByFlightNumber(flightNumber).addPassengerToEconomyClass(findPassengerByPassengerId(passengerId));
    }
    public double totalMoneyIncomeFromAFlight(String flightNumber) throws FlightNotFoundException{
        double total = 0;
        for(Seat i : findFlightByFlightNumber(flightNumber).getSeats()){
            if(i.isOccupied()){
                total += findFlightByFlightNumber(flightNumber).calculatePrice(i.getSeatNumber());
            }
        }
        return total;
    }
    public void assignSeatNumbersAtAFlight(String flightNumber) throws FlightNotFoundException{
        findFlightByFlightNumber(flightNumber).assignSeatNumbers();
    }
    public void assignSeatsToPassengersAtFlights() throws SeatNotAvailableException{
        for(Flight i : flights){
            i.assignSeatsToPassengers();
        }
    }
    public void assignSeatsToPassengerAtAFlight(String flightNumber) throws FlightNotFoundException,SeatNotAvailableException{
        findFlightByFlightNumber(flightNumber).assignSeatsToPassengers();
    }
}
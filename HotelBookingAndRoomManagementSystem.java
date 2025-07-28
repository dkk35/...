import java.util.*;
import java.io.*;
public class HotelBookingAndRoomManagementSystem{
    public static void main(String[] args){
        Hotel hotel = new Hotel();
        Scanner scn = new Scanner(System.in);
        while(true){
            try{
                System.out.print("1. Add Room\n2. Add Customer\n3. Book Room\n4. Cancel Booking\n5. View All Rooms\n6. View All Available Rooms\n7. Save Bookings\n8. Load Bookings\n9. Exit");
                int q = scn.nextInt();
                switch(q){
                    case 1:
                        scn.nextLine();
                        System.out.println("Enter room number");
                        int roomNumber = scn.nextInt();
                        scn.nextLine();
                        System.out.println("Enter room type");
                        String roomType = scn.nextLine();
                        System.out.println("is this room booked by someone? y/n");
                        String x = scn.nextLine();
                        if(x.equals("y")){
                            System.out.println("Enter customer details: Customer Id:");
                            int customerId = scn.nextInt();
                            scn.nextLine();
                            System.out.println("Enter customer's name:");
                            String name = scn.nextLine();
                            System.out.println("Enter customers email:");
                            String email = scn.nextLine();
                            hotel.addRoom(new Room(roomNumber,roomType,new Customer(customerId,name,email)));
                            hotel.addCustomer(new Customer(customerId,name,email));
                            System.out.println("New room successfully added");
                        }else{
                            hotel.addRoom(new Room(roomNumber,roomType));
                            System.out.println("New Room successfully added");
                        }
                        break;
                    case 2:
                        scn.nextLine();
                        System.out.println("Enter customer details: customer id: ");
                        int customerId = scn.nextInt();
                        scn.nextLine();
                        System.out.println("Enter customer's name: ");
                        String name = scn.nextLine();
                        System.out.println("Enter customer's email: ");
                        String email = scn.nextLine();
                        hotel.addCustomer(new Customer(customerId,name,email));
                        System.out.println("New customer successfully added");
                        break;
                    case 3:
                        scn.nextLine();
                        System.out.println("Enter room number to be booked:");
                        int roomNumber2 = scn.nextInt();
                        scn.nextLine();
                        System.out.println("Enter customer id: ");
                        int customerId2 = scn.nextInt();
                        hotel.bookRoom(roomNumber2,customerId2);
                        System.out.println("Room booked successfully");
                        scn.nextLine();
                        break;
                    case 4:
                        scn.nextLine();
                        System.out.println("Enter room number to cancel the booking: ");
                        int roomNumber3 = scn.nextInt();
                        scn.nextLine();
                        System.out.println("Enter customer id: ");
                        int customerId3 = scn.nextInt();
                        hotel.cancelBooking(roomNumber3,customerId3);
                        System.out.println("Booking cancelled successfully");
                        scn.nextLine();
                        break;
                    case 5:
                        hotel.displayAllRooms();
                        scn.nextLine();
                        break;
                    case 6:
                        hotel.displayAvailableRooms();
                        scn.nextLine();
                        break;
                    case 7:
                        scn.nextLine();
                        System.out.println("Enter file name to be record: ");
                        String fileName = scn.nextLine();
                        hotel.saveBookingsToFile(fileName);
                        System.out.println("Bookings saved successfully");
                        break;
                    case 8:
                        scn.nextLine();
                        System.out.println("Enter the file name to be loaded: ");
                        String fileName2 = scn.nextLine();
                        hotel.loadBookingsFromFile(fileName2);
                        break;
                    case 9:
                        System.out.println("Have a nice day");
                        return;
                }
            }catch(Exception ex){
                System.out.println(ex.getMessage());
            }
        }
    }
}
class RoomAlreadyExistsException extends Exception{
    public RoomAlreadyExistsException(String msg) {
        super(msg);
    }
}
class CustomerAlreadyExistsException extends Exception{
    public CustomerAlreadyExistsException(String msg) {
        super(msg);
    }
}
class RoomNotAvailableException extends Exception{
    public RoomNotAvailableException(String msg) {
        super(msg);
    }
}
class CustomerNotFoundException extends Exception{
    public CustomerNotFoundException(String msg) {
        super(msg);
    }
}
class BookingNotFoundException extends Exception{
    public BookingNotFoundException(String msg) {
        super(msg);
    }
}
class Hotel{
    private ArrayList<Room> rooms;
    private ArrayList<Customer> customers;
    public Hotel() {
        this.rooms = new ArrayList<>();
        this.customers = new ArrayList<>();
    }
    public void addRoom(Room room) throws RoomAlreadyExistsException{
        if(rooms.contains(room)) {
            throw new RoomAlreadyExistsException("Room already exists");
        }
        rooms.add(room);
    }
    public void addCustomer(Customer customer) throws CustomerAlreadyExistsException{
        if(customers.contains(customer)) {
            throw new CustomerAlreadyExistsException("Customer already exists");
        }
        customers.add(customer);
    }
    public Room findRoomByRoomNumber(int roomNumber) throws RoomNotAvailableException{
        for(Room i : rooms) {
            if(i.getRoomNumber() == roomNumber) {
                return i ;
            }
        }
        throw new RoomNotAvailableException("Room not found");
    }
    public Customer findCustomerByCustomerId(int customerId) throws CustomerNotFoundException{
        for(Customer i : customers) {
            if(i.getCustomerId() == customerId) {
                return i;
            }
        }
        throw new CustomerNotFoundException("Customer not found");
    }
    public void bookRoom(int roomNumber,int customerId) throws RoomNotAvailableException,CustomerNotFoundException{
        findRoomByRoomNumber(roomNumber).book(findCustomerByCustomerId(customerId));
    }
    public void cancelBooking(int roomNumber,int customerId) throws BookingNotFoundException,RoomNotAvailableException{
        if(findRoomByRoomNumber(roomNumber).isBooked() == false) {
            throw new BookingNotFoundException("Booking not found");
        }
        findRoomByRoomNumber(roomNumber).cancelTheBooking();
    }
    public void displayAllRooms() {
        for(Room i : rooms) {
            i.displayInfo();
        }
    }
    public void displayAvailableRooms() {
        for(Room i : rooms) {
            if(i.isBooked() == false) {
                i.displayInfo();
            }
        }
    }
    public void saveBookingsToFile(String filename) throws FileNotFoundException{
        PrintWriter writer = new PrintWriter(filename + ".txt");
        for(Room i : rooms){
            writer.println(i.getRoomNumber());
            writer.println(i.getRoomType());
            if(i.isBooked()){
                writer.println(i.isBooked());
                writer.println(i.getBookedBy().toString());
            }else{
                writer.println(i.isBooked());

            }
        }
        writer.println("x");
        for(Customer i : customers){
            writer.println(i.getCustomerId());
            writer.println(i.getName());
            writer.println(i.getEmail());
            writer.println("x");
        }
        writer.close();
    }
    public void loadBookingsFromFile(String filename) throws FileNotFoundException, RoomAlreadyExistsException, CustomerAlreadyExistsException {
        File file = new File(filename + ".txt");
        Scanner scn = new Scanner(file);

        while (scn.hasNext()) {
            String line = scn.nextLine();
            if (line.equals("x")) break;

            int roomNumber = Integer.parseInt(line);
            String roomType = scn.nextLine();
            boolean booked = Boolean.parseBoolean(scn.nextLine());

            if (booked) {
                String[] customerData = scn.nextLine().split(",");
                int customerId = Integer.parseInt(customerData[0]);
                String name = customerData[1];
                String email = customerData[2];

                Customer customer = new Customer(customerId, name, email);
                addCustomer(customer);
                addRoom(new Room(roomNumber, roomType, customer));
            } else {
                addRoom(new Room(roomNumber, roomType));
            }
        }

        while (scn.hasNext()) {
            String line = scn.nextLine();
            if (line.equals("x")) continue;

            int customerId = Integer.parseInt(line);
            String name = scn.nextLine();
            String email = scn.nextLine();
            addCustomer(new Customer(customerId, name, email));
        }

        scn.close();
    }
}
class Room{
    private int roomNumber;
    private String roomType;
    private boolean isBooked;
    private Customer bookedBy;
    public Room(int roomNumber,String roomType) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.isBooked = false;
    }
    public Room(int roomNumber,String roomType,Customer bookedBy){
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.bookedBy = bookedBy;
        this.isBooked = true;
    }
    public int getRoomNumber() {
        return roomNumber;
    }
    public String getRoomType(){
        return roomType;
    }
    public boolean isBooked() {
        return isBooked;
    }
    public Customer getBookedBy(){
        return bookedBy;
    }
    public void book(Customer customer) throws RoomNotAvailableException{
        if(isBooked) {
            throw new RoomNotAvailableException("Room is not available");
        }
        this.bookedBy = customer;
        this.isBooked = true;
    }
    public void cancelTheBooking() throws BookingNotFoundException{
        if(isBooked == false) {
            throw new BookingNotFoundException("Room is already available");
        }
        this.isBooked = false;
        this.bookedBy = null;
    }
    public void displayInfo() {
        if(isBooked){
            System.out.println("Room Number: " + roomNumber + " Room Type: " + roomType + " is Booked: " + isBooked + " Booked by: " + bookedBy.getName());
        }else{
            System.out.println("Room Number: " + roomNumber + " Room Type: " + roomType + " is Booked: " + isBooked);
        }
    }
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Room == false) {
            return false;
        }
        Room other = (Room) obj;
        return other.getRoomNumber() == roomNumber;
    }
}
class Customer{
    private int customerId;
    private String name;
    private String email;
    public Customer(int customerId,String name,String email) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
    }
    public int getCustomerId() {
        return customerId;
    }
    public String getName() {
        return name;
    }
    public String getEmail(){
        return email;
    }
    public void displayInfo() {
        System.out.println("Customer Id: " + customerId + " Name: " + name + " E-mail: " + email);
    }
    @Override
    public String toString(){
        return customerId + ","+name+","+email;
    }
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Customer)) {
            return false;
        }
        Customer other = (Customer) obj;
        return other.getName().equals(name)&& other.getCustomerId() == customerId;
    }
}

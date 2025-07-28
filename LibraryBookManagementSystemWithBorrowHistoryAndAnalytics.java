import java.util.*;
import java.io.*;
public class LibraryBookManagementSystemWithBorrowHistoryAndAnalytics{
    public static void main(String[] args){
        try{
            Scanner scn = new Scanner(System.in);
            LibrarySystem system = new LibrarySystem();
            while(true){
                System.out.println("==== Library System ====");
                System.out.println("1. Add Book\n2. Register User\n3. Borrow Book\n4. Return Book\n5. View User Borrow History\n6. Display All Books\n7. Display All Users\n8.View Top 3 Most Borrowed Books\n9.Load A Library From A File\n10.Save & Exit");
                int c = scn.nextInt();
                switch(c){
                    case 1:
                        scn.nextLine();
                        System.out.println("Enter Books Title: ");
                        String title = scn.nextLine();
                        System.out.println("Enter book's author: ");
                        String author = scn.nextLine();
                        System.out.println("Enter book's isbn: ");
                        String isbn = scn.nextLine();
                        system.addNewBook(new Book(title,author,isbn));
                        System.out.println("New book added to library successfully");
                        break;
                    case 2:
                        scn.nextLine();
                        System.out.println("Enter user's name: ");
                        String name = scn.nextLine();
                        System.out.println("Enter user's id: ");
                        int id = scn.nextInt();
                        system.addNewUser(new User(name,id));
                        System.out.println("New user added to system successfully");
                        scn.nextLine();
                        break;
                    case 3:
                        scn.nextLine();
                        System.out.println("Enter the book's isbn to be borrowed: ");
                        String isbn2 = scn.nextLine();
                        System.out.println("Enter the user's id who wants to borrow the book: ");
                        int id2 = scn.nextInt();
                        system.borrowBook(isbn2,id2);
                        System.out.println("Book is borrowed successfully");
                        scn.nextLine();
                        break;
                    case 4:
                        scn.nextLine();
                        System.out.println("Enter isbn of the book: ");
                        String isbn3 = scn.nextLine();
                        System.out.println("Enter the id of the user: ");
                        int id3 = scn.nextInt();
                        system.returnBook(isbn3,id3);
                        System.out.println("Book returned successfully");
                        scn.nextLine();
                        break;
                    case 5:
                        scn.nextLine();
                        System.out.println("Enter the user's id to see the borrow history: ");
                        int id4 = scn.nextInt();
                        system.viewBorrowHistoryForAUser(id4);
                        scn.nextLine();
                        break;
                    case 6:
                        scn.nextLine();
                        system.displayAllBooks();
                        break;
                    case 7:
                        scn.nextLine();
                        system.displayAllUsers();
                        break;
                    case 8:
                        system.showMostlyBorrowedThreeBooks();
                        scn.nextLine();
                        break;
                    case 9:
                        scn.nextLine();
                        System.out.println("Enter the file name to be ridden");
                        String filename = scn.nextLine();
                        system.readLibraryFromAFile(filename);
                        break;
                    case 10:
                        scn.nextLine();
                        System.out.println("Enter the file name to record the library recordings: ");
                        String filename2 = scn.nextLine();
                        system.saveLibraryToAFile(filename2);
                        System.out.println("Save successfully. Have a nice day!");
                        return;

                }
            }
        }catch(Exception ex){
            System.out.println(ex.getMessage());
                }
            }
        }

class AlreadyAvailableException extends Exception{
    public AlreadyAvailableException(String msg){
        super(msg);
    }
}
class BorrowedAlreadyException extends Exception{
    public BorrowedAlreadyException(String msg){
        super(msg);
    }
}
class BookAlreadyExistsException extends Exception{
    public BookAlreadyExistsException(String msg){
        super(msg);
    }
}
class UserAlreadyExistsException extends Exception{
    public UserAlreadyExistsException(String msg){
        super(msg);
    }
}
class BookNotFoundException extends Exception{
    public BookNotFoundException(String msg){
        super(msg);
    }
}
class UserNotFoundException extends Exception{
    public UserNotFoundException(String msg){
        super(msg);
    }
}
class Book{
    private String title;
    private String author;
    private String isbn;
    private boolean isAvailable;
    private int borrowCount;
    public Book(String title,String author,String isbn){
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.isAvailable = true;
        this.borrowCount = 0;
    }
    public Book(String title,String author,String isbn,boolean isAvailable,int borrowCount){
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.isAvailable = isAvailable;
        this.borrowCount = borrowCount;
    }
    public String getTitle(){
        return title;
    }
    public String getAuthor(){
        return author;
    }
    public String getIsbn(){
        return isbn;
    }
    public boolean isAvailable(){
        return isAvailable;
    }
    public int getBorrowCount(){
        return borrowCount;
    }
    public void borrow() throws BorrowedAlreadyException{
        if(isAvailable == false){
            throw new BorrowedAlreadyException("Book is already borrowed");
        }
        this.borrowCount++;
        this.isAvailable = false;
    }
    public void returnBook() throws AlreadyAvailableException{
        if(isAvailable){
            throw new AlreadyAvailableException("Book is already available");
        }
        this.isAvailable = true;
    }
    public void displayInfo(){
        System.out.println("Title: " + title + " Author: " + author + " Isbn: " + isbn + " Is available: " + isAvailable + " Borrow Count: " + borrowCount);
    }
    @Override
    public String toString(){
        return title+","+author + "," +isbn + "," + isAvailable + "," +borrowCount;
    }
    @Override
    public boolean equals(Object obj){
        if(obj instanceof Book == false){
            return false;
        }
        Book other = (Book) obj;
        return other.getIsbn().equals(isbn) && other.getTitle().equals(title);
    }
}
class User{
    private String name;
    private int id;
    private ArrayList<Book> borrowedBooks;
    private Logger logger;
    public User(String name, int id){
        this.name = name;
        this.id = id;
        this.borrowedBooks = new ArrayList<>();
        this.logger = new Logger();
    }
    public User(String name,int id,ArrayList<Book> borrowedBooks){
        this.name = name;
        this.id = id;
        this.borrowedBooks = borrowedBooks;
        this.logger = new Logger();
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }
    public void addBook(Book book) throws BorrowedAlreadyException,BookAlreadyExistsException{
        if(borrowedBooks.contains(book) == true){
            throw new BookAlreadyExistsException("Book is already borrowed by this user");
        }
        borrowedBooks.add(book);
        book.borrow();
        logger.log("Book " + book.getTitle() + " is borrowed by " + name);
    }
    public void returnBorrowedBook(Book book) throws AlreadyAvailableException{
        if(borrowedBooks.contains(book) == false){
            throw new AlreadyAvailableException("Book is not borrowed by this user");
        }
        borrowedBooks.remove(book);
        book.returnBook();
        logger.log("Book " + book.getTitle() + " is returned by user " + name);
    }
    public void displayInfo(){
        String x = "Name: " + name + " ID: " + id;
        if(borrowedBooks.isEmpty()){
            System.out.println(x);
        }else{
            x += " Borrowed Books: ";
            for(int i =0;i<borrowedBooks.size();i++){
                x+=borrowedBooks.get(i).toString();
            }
            System.out.println(x);
        }
    }
    public void showHistoryForUser(){
        for(String i : logger.getHistory()){
            System.out.println(i);
        }
    }
    @Override
    public boolean equals(Object obj){
        if(obj instanceof User == false){
            return false;
        }
        User other = (User) obj;
        return other.getId() == id;
    }
    @Override
    public String toString(){
        String x = name + "," + id;
        if(borrowedBooks.isEmpty()){
            return x;
        }
        for(Book i : borrowedBooks){
            x+= ","+i.toString();
        }
        return x;
    }
}
class LibrarySystem{
    private ArrayList<Book> books;
    private ArrayList<User> users;
    public LibrarySystem(){
        this.books = new ArrayList<>();
        this.users = new ArrayList<>();
    }
    public void addNewBook(Book book) throws BookAlreadyExistsException{
        if(books.contains(book)){
            throw new BookAlreadyExistsException("Book already exists");
        }
        books.add(book);
    }
    public void addNewUser(User user) throws UserAlreadyExistsException{
        if(users.contains(user)){
            throw new UserAlreadyExistsException("User already exists");
        }
        users.add(user);
    }
    public Book findBookByIsbn(String isbn) throws BookNotFoundException{
        for(Book i : books){
            if(i.getIsbn().equals(isbn)){
                return i;
            }
        }
        throw new BookNotFoundException("Book not found");
    }
    public User findUserById(int id) throws UserNotFoundException{
        for(User i : users){
            if(i.getId() == id){
                return i;
            }
        }
        throw new UserNotFoundException("User not found");
    }
    public void borrowBook(String isbn,int id) throws BookNotFoundException,UserNotFoundException,BorrowedAlreadyException,BookAlreadyExistsException{
        findUserById(id).addBook(findBookByIsbn(isbn));
    }
    public void returnBook(String isbn,int id) throws BookNotFoundException,UserNotFoundException,AlreadyAvailableException{
        findUserById(id).returnBorrowedBook(findBookByIsbn(isbn));
    }
    public void displayAllBooks(){
        for(Book i : books){
            i.displayInfo();
        }
    }
    public void displayAllUsers(){
        for(User i : users){
            i.displayInfo();
        }
    }
    public void showMostlyBorrowedThreeBooks(){
        Book x = books.get(0);
        Book y = books.get(0);
        Book z = books.get(0);
        for(Book i : books){
            if(i.getBorrowCount() > x.getBorrowCount()){
                x = i;
            }
        }
        x.displayInfo();
        for(Book i:books){
            if(i.equals(x)){
                continue;
            }
            if(i.getBorrowCount() > y.getBorrowCount()){
                y = i;
            }
        }
        y.displayInfo();
        for(Book i : books){
            if(i.equals(x) || i.equals(z)){
                continue;
            }
            if(i.getBorrowCount() > z.getBorrowCount()){
                z = i;
            }
        }
        z.displayInfo();
    }
    public void viewBorrowHistoryForAUser(int id) throws UserNotFoundException{
        for(User i : users){
            if(i.getId() == id){
                i.showHistoryForUser();
                return;
            }
        }
        throw new UserNotFoundException("User not found");
    }
    public void saveLibraryToAFile(String filename) throws FileNotFoundException{
        PrintWriter x = new PrintWriter(filename + ".txt");
        for(Book i : books){
            x.println(i.toString());
        }
        x.println();
        for(User i : users){
            x.println(i.toString());
        }
        x.close();
    }
    public void readLibraryFromAFile(String filename) throws FileNotFoundException,BookAlreadyExistsException,UserAlreadyExistsException{
        File file = new File(filename + ".txt");
        Scanner scn = new Scanner(file);
        while(scn.hasNext()){
            String x= scn.nextLine();
            if(x.isEmpty()){
                break;
            }
            String[] parts = x.split(",");
            addNewBook(new Book(parts[0],parts[1],parts[2],Boolean.valueOf(parts[3]),Integer.valueOf(parts[4])));
        }
        while(scn.hasNext()){
            String x = scn.nextLine();
            String[] parts = x.split(",");
            ArrayList<Book> borrowedBooks = new ArrayList<>();
            if(parts.length>2){
                addNewUser(new User(parts[0],Integer.valueOf(parts[1])));
            }else{
                for(int i = 2;i<parts.length;i+=5){
                    borrowedBooks.add(new Book(parts[i],parts[i+1],parts[i+2],Boolean.valueOf(parts[i+3]),Integer.valueOf(parts[i+4])));
                }
                addNewUser(new User(parts[0],Integer.valueOf(parts[1]),borrowedBooks));
            }
        }
    }
}
class Logger{
    private ArrayList<String> logHistory;
    public Logger(){
        this.logHistory = new ArrayList<>();
    }
    public void log(String msg){
        logHistory.add(msg);
    }
    public List<String> getHistory(){
        return logHistory;
    }
}
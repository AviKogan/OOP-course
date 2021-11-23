

/**
 * This class represents a library, which hold a collection of books. Patrons can register at the library to
 * be able to check out books, if a copy of the requested book is available.
 * @author Avi Kogan
 * @see Patron
 * @see Book
 */
public class Library {

    /** Represent the id of available book in the library. */
    static private final int AVAILABLE_BOOK_ID = -1;

    /** Return value if the searched item didn't found. */
    static private final int NOT_FOUND = -1;

    /** The maximal number of books this library can hold. */
    private final int maxBookCapacity;

    /** The maximal number of books this library allows a single patron to borrow at the same time. */
    private final int maxBorrowedBooks;

    /** The maximal number of registered patrons this library can handle. */
    private final int maxPatronCapacity;

    /** The array that holds all the books in the Library, the index that contain book represent the book's id,
     *  will initialize with maxBookCapacity*/
    private Book[] libraryBooks;

    /** The array that holds all the patron of the Library, will initialize with maxPatronCapacity */
    private Patron[] libraryPatrons;

    /** Represent the current books capacity in the library */
    private int currentBookCapacity = 0;

    /** represent the number of patrons that currently registered at the library */
    private int currentPatronsCapacity = 0;

    /*----=  Constructors  =-----*/

    /**
     * Creates a new book with the given characteristic.
     * @param maxBookCapacity The maximal number of books this library can hold.
     * @param maxBorrowedBooks The maximal number of books this library allows a single patron to borrow at the
     *                        same time.
     * @param maxPatronCapacity The maximal number of registered patrons this library can handle.
     */
    public Library(int maxBookCapacity, int maxBorrowedBooks, int maxPatronCapacity){
        this.maxBookCapacity = maxBookCapacity;
        this.maxBorrowedBooks = maxBorrowedBooks;
        this.maxPatronCapacity = maxPatronCapacity;

        libraryBooks = new Book[maxBookCapacity]; //init empty from books library
        for(int i = 0; i < maxBookCapacity; ++i){
            libraryBooks[i] = null;
        }

        libraryPatrons = new Patron[maxPatronCapacity]; //init empty from patrons library
        for(int i = 0; i < maxPatronCapacity; ++i){
            libraryPatrons[i] = null;
        }
    }

    /*----=  Instance Methods  =-----*/

    /**
     * Adds the given book to this library, if there is place available, and it isn't already in the library.
     * @param book The book to add to this library.
     * @return a non-negative id number for the book if there was a spot and the book was successfully added,
     *         or if the book was already in the library; a negative number otherwise.
     */
    public int addBookToLibrary(Book book){

        if(currentBookCapacity == maxBookCapacity) return getBookId(book);

        for(int i = 0; i < currentBookCapacity; ++i){
            if(book == libraryBooks[i]) return i;
        }

        int bookId = currentBookCapacity;
        libraryBooks[bookId] = book;
        currentBookCapacity++;
        return bookId;
    }

    /**
     * Returns true if the given number is an id of some book in the library, false otherwise.
     * @param bookId The id to check.
     * @return true if the given number is an id of some book in the library, false otherwise.
     */
    public boolean isBookIdValid(int bookId){
        if(bookId >= 0 && bookId < maxBookCapacity) { //make sure the id in the range of books array.
            return libraryBooks[bookId] != null;
        }
        return false;
    }

    /**
     * Returns the non-negative id number of the given book if he is owned by this library, -1 otherwise.
     * @param book The book for which to find the id number.
     * @return a non-negative id number of the given book if he is owned by this library, -1 otherwise.
     */
    public int getBookId(Book book){

        for(int i = 0; i < currentBookCapacity; ++i){
            if(book == libraryBooks[i]) return i;
        }

        return Library.NOT_FOUND;
    }

    /**
     * Returns true if the book with the given id is available, false otherwise.
     * @param bookId The id number of the book to check.
     * @return true if the book with the given id is available, false otherwise.
     */
    public boolean isBookAvailable(int bookId){
        if(isBookIdValid(bookId)) return libraryBooks[bookId].currentBorrowerId == AVAILABLE_BOOK_ID;
        return false;
    }

    /**
     * Registers the given Patron to this library, if there is a spot available.
     * @param patron The patron to register to this library.
     * @return a non-negative id number for the patron if there was a spot and the patron was successfully
     *         registered or if the patron was already registered. a negative number otherwise.
     */
    public int registerPatronToLibrary(Patron patron){
        if(currentPatronsCapacity == maxPatronCapacity) return getPatronId(patron);

        for(int i = 0; i < currentPatronsCapacity; ++i){
            if(patron == libraryPatrons[i]) return i;
        }

        int patronId = currentPatronsCapacity;
        libraryPatrons[patronId] = patron;
        currentPatronsCapacity++;
        return patronId;

    }

    /**
     * Returns true if the given number is an id of a patron in the library, false otherwise.
     * @param patronId The id to check.
     * @return true if the given number is an id of a patron in the library, false otherwise.
     */
    public boolean isPatronIdValid(int patronId){
        if(patronId >= 0 && patronId < maxPatronCapacity) return libraryPatrons[patronId] != null;

        return false;
    }

    /**
     * Returns the non-negative id number of the given patron if he is registered to this library,
     * -1 otherwise.
     * @param patron The patron for which to find the id number.
     * @return a non-negative id number of the given patron if he is registered to this library, -1 otherwise.
     */
    public int getPatronId(Patron patron){
        for(int i = 0; i < currentPatronsCapacity; i++){
            if(patron == libraryPatrons[i]) return i;
        }
        return Library.NOT_FOUND;
    }

    /**
     * Marks the book with the given id number as borrowed by the patron with the given patron id, if this
     * book is available, the given patron isn't already borrowing the maximal number of books allowed, and
     * if the patron will enjoy this book
     * @param bookId The id number of the book to borrow.
     * @param patronId The id number of the patron that will borrow the book.
     * @return true if the book was borrowed successfully, false otherwise.
     */
    public boolean borrowBook(int bookId, int patronId){
        if(isPatronIdValid(patronId) &&
           isBookAvailable(bookId) &&
           libraryPatrons[patronId].willEnjoyBook(libraryBooks[bookId])){

            int patronBorrowedBooksNumber = 0;
            for(int i = 0; i < currentBookCapacity; ++i){
                if(libraryBooks[i].currentBorrowerId == patronId){
                    patronBorrowedBooksNumber++;
                }
            }

            if(patronBorrowedBooksNumber < maxBorrowedBooks){
                libraryBooks[bookId].currentBorrowerId = patronId;
                return true;
            }
        }
        return false;
    }

    /**
     * Return the given book.
     * @param bookId id number of the book to return.
     */
    public void returnBook(int bookId){
        if(isBookIdValid(bookId)) libraryBooks[bookId].returnBook();
    }

    /**
     * Suggest the patron with the given id the book he will enjoy the most, out of all available books he
     * will enjoy, if any such exist.
     * @param patronId The id number of the patron to suggest the book to.
     * @return The available book the patron with the given ID will enjoy the most. Null if no book is
     *         available.
     */
    public Book suggestBookToPatron(int patronId){
        if(!isPatronIdValid(patronId)) return null;

        Patron patron = libraryPatrons[patronId];
        Book enjoyableBook = null;
        int maxEnjoyableBookVal = -1;

        for(int i = 0; i < currentBookCapacity; ++i){
            if(patron.willEnjoyBook(libraryBooks[i])){
                int bookScore = patron.getBookScore(libraryBooks[i]);
                if(bookScore > maxEnjoyableBookVal){
                    enjoyableBook = libraryBooks[i];
                    maxEnjoyableBookVal = bookScore;
                }
            }
        }
        return enjoyableBook;
    }
}

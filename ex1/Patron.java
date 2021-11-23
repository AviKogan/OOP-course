

/**
 * This class represents a library patron that has a name and assigns values to different literary aspects of
 * books.
 * @author Avi Kogan
 * @see Book
 */
public class Patron{

    /** The first name of the patron. */
    private final String name;

    /** The last name of the patron. */
    private final String lastName;

    /** The weight the patron assigns to the comic aspects of books. */
    private final int comicTendency;

    /** The weight the patron assigns to the dramatic aspects of books. */
    private final int dramaticTendency;

    /** The weight the patron assigns to the educational aspects of books. */
    private final int educationalTendency;

    /** The minimal literary value a book must have for this patron to enjoy it. */
    private final int patronEnjoymentThreshold;

    /*----=  Constructors  =-----*/

    /**
     * Creates a new book with the given characteristic.
     * @param patronFirstName The first name of the patron.
     * @param patronLastName The last name of the patron.
     * @param comicTendency The weight the patron assigns to the comic aspects of books.
     * @param dramaticTendency The weight the patron assigns to the dramatic aspects of books.
     * @param educationalTendency The weight the patron assigns to the educational aspects of books.
     * @param patronEnjoymentThreshold The minimal literary value a book must have for this patron to enjoy
     *                                 it.
     */
    public Patron(String patronFirstName, String patronLastName, int comicTendency, int dramaticTendency,
                  int educationalTendency, int patronEnjoymentThreshold){
        this.name = patronFirstName;
        this.lastName = patronLastName;
        this.comicTendency = comicTendency;
        this.dramaticTendency = dramaticTendency;
        this.educationalTendency = educationalTendency;
        this.patronEnjoymentThreshold = patronEnjoymentThreshold;
    }

    /*----=  Instance Methods  =-----*/

    /**
     * Returns a string representation of the patron, which is a sequence of its first and last name,
     * separated by a single white space. For example, if the patron's first name is "Ricky" and his last
     * name is "Bobby", this method will return the String "Ricky Bobby".
     * @return the String representation of this patron.
     */
    String stringRepresentation(){
        return name+" "+lastName;
    }

    /**
     * @param book The book to asses.
     * @return the literary value this patron assigns to the given book.
     */
    int getBookScore(Book book){
        return book.comicValue * comicTendency + book.dramaticValue * dramaticTendency +
                        book.educationalValue * educationalTendency;
    }

    /**
     *
     * @param book The book to asses.
     * @return true if this patron will enjoy the given book, false otherwise.
     */
    boolean willEnjoyBook(Book book){
        int bookScore = getBookScore(book);
        return bookScore >= patronEnjoymentThreshold;
    }
}

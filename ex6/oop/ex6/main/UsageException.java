package oop.ex6.main;

/**
 * Exception thrown with error message to declare usage error, error handled by printing 2 at the end.
 *
 * @author Avi Kogan.
 * @author Carnml Gadot.
 */
class UsageException extends Exception{

    private static final long serialVersionUID = 1L;

    UsageException() { System.err.println("USAGE: oop.ex6.main.Sjavac source_file_name"); }
}

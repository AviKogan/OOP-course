package oop.ex6.main;

import oop.ex6.parser.FileProcessor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

/**
 * Abstract class that run the process on the given Sjavac file.
 *
 * @author Avi Kogan
 * @author Carmel Gadot
 */
public abstract class Sjavac {

    /* The index of the need to check Sjavac file in the args. */
    private static final int NUM_OF_EXPECTED_ARGS = 1;

    /* The expected length of args */
    private static final int FILE_INDEX_IN_ARGS = 0;

    /* num represent legal code */
    private static final int LEGAL_CODE = 0;

    /* num represent illegal code */
    private static final int ILLEGAL_CODE = 1;

    /* num represent IO error */
    private static final int IO_ERROR = 2;

    /**
     * The main of program, run the validation on the given Sjavac file, return 0 if the file valid,
     *                                                                   return 1 if the file invalid
     *                                                                   return 2 in IO or Usage Exception.
     *
     * @param args the given args to the program.
     */
    public static void main(String[] args) {

        try {
            if (args.length != NUM_OF_EXPECTED_ARGS) throw new UsageException();

            List<String> lines = Files.readAllLines(new File(args[FILE_INDEX_IN_ARGS]).toPath());
            FileProcessor.processFile(lines);
            System.out.println(LEGAL_CODE);

        } catch (IOException exp) {
            System.err.println("The sJava file" + exp.getMessage() + "not found, or its not a valid file.");
            System.out.println(IO_ERROR);
        } catch (UsageException e) {
            System.out.println(IO_ERROR);
        } catch (IllegalCodeException e) {
            System.out.println(ILLEGAL_CODE);
        }
    }
}

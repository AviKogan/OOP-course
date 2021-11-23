package filesprocessing;

/**
 * This class represent an warning thrown while the DirectoryProcessor runs.
 */
class Warning implements Printable{

    /**
     * The line in the file where the warning occurred.
     */
    private final int _lineOfWarning;

    /**
     * Class constructor, create instance of Warning with the given line number.
     * @param lineOfWarning the line number the warning occurred.
     */
    public Warning(int lineOfWarning){
        _lineOfWarning = lineOfWarning;
    }

    /**
     * Override the filesprocessing.Printable print method.
     * Prints the warning starter and than the line the warning occurred according to the given line number
     * in the instance constructor.
     */
    public void print() {
        System.err.println("Warning in line " + _lineOfWarning);
    }
}

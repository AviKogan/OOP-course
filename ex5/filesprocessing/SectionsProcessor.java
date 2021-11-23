package filesprocessing;

import filesprocessing.Filters.Filter;
import filesprocessing.Filters.FilterFactory;
import filesprocessing.Orders.Order;
import filesprocessing.Orders.OrderFactory;
import filesprocessing.exceptions.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * The class process the sections of command file.
 *
 * @author Avi kogan
 */
abstract class SectionsProcessor {

    /**
     * The number of lines separated between FILTER name sub-section to the followed ORDER sub-section name.
     */
    private static final int LINES_BETWEEN_FILTER_TO_ORDER = 2;

    /**
     * The number of lines separated between FILTER name sub-section to the followed filter type.
     */
    private static final int LINES_BETWEEN_FILTER_TO_FILTER_TYPE = 1;

    /**
     * The number of lines separated between FILTER name sub-section to the followed order type.
     */
    private static final int LINES_BETWEEN_FILTER_TO_ORDER_TYPE = 3;

    /**
     * The minimal valid number of lines in command file, inlude FILTER sub section than filter-type and
     * last ORDER sub-section.
     */
    private static final int MINIMAL_LINES_IN_SECTION = 3;

    /**
     * Represent the sub-section of filter name.
     */
    private static final String FILTER_SUB_SECTION_NAME = "FILTER";

    /**
     * Represent the sub-section of order name.
     */
    private static final String ORDER_SUB_SECTION_NAME = "ORDER";

    /**
     * Used for comparisons.
     */
    private static final int EQUAL = 0;

    /**
     * Assumes both params are valid.
     * @param curOrderNameIndex The index of the the current section ORDER name, to check with it the next
     *                          section start line.
     * @param lines list of all the lines from the DirectoryProcessor current command file.
     * @return According to the given order name index in the lines, return the next start section line, if
     *         there is no new section return the end of file line (the size of lines list).
     */
    private static int getNextSectionStartIndex(int curOrderNameIndex, List<String> lines){
        int afterOrderIndex = curOrderNameIndex + 1;

        if(afterOrderIndex == lines.size() ||
           lines.get(afterOrderIndex).compareTo(FILTER_SUB_SECTION_NAME) == EQUAL) {
            return afterOrderIndex;
        }
        return afterOrderIndex + 1;
    }

    /**
     * Validate the lines not empty, the file format and the sub-sections names.
     *
     * @param lines List of the lines of the commands file.
     * @throws ErrorBadFormatException if the List empty of the number of lines not valid.
     *
     * @throws ErrorBadSubSectionNameException if in one of the sub sections lines not valid.
     */
    private static void validateSectionsList(List<String> lines) throws ErrorsExceptions {
        if(lines.size() < MINIMAL_LINES_IN_SECTION){
            throw new ErrorBadFormatException();
        }

        int curFilterNameLineNumber, curOrderNameLineNumber;
        int curSectionStartIndex = 0;
        while(curSectionStartIndex != lines.size()) {
            curFilterNameLineNumber = curSectionStartIndex;
            curOrderNameLineNumber = curSectionStartIndex + LINES_BETWEEN_FILTER_TO_ORDER;
            String filterSubSectionName = lines.get(curFilterNameLineNumber);
            String orderSubSectionName = lines.get(curOrderNameLineNumber);

            if (filterSubSectionName.compareTo(FILTER_SUB_SECTION_NAME) != EQUAL ||
                    orderSubSectionName.compareTo(ORDER_SUB_SECTION_NAME) != EQUAL) {
                throw new ErrorBadSubSectionNameException();
            }

            curSectionStartIndex = getNextSectionStartIndex(curOrderNameLineNumber, lines);

            if(curSectionStartIndex == lines.size()) return; //end of file, empty line

            if(curSectionStartIndex + LINES_BETWEEN_FILTER_TO_ORDER >= lines.size()){
                throw new ErrorBadFormatException(); // next section not full
            }
        }
    }

    /**
     * Create Printable objects (of type Warning or Section) according to each section process, insert
     * them into the printable LinkedList that returned in the given sections order, if some
     * WarningsExceptions thrown in section process, it insert suitable Warning before the Section in the
     * printable LinkedList.
     *
     * @param commandFilePath the command file with the sections to process
     * @param arrayToProcess the array to process according in each section.
     * @return linkedList with all the sections and the warning if in the order they appeared during the
     *         process of the sections.
     *
     * @throws ErrorsExceptions if command file format not valid thrown ErrorBadFormatException
     *                          if command file contain not valid sub section name thrown
     *                          ErrorBadSubSectionNameException
     *                          if an error occurs while reading from the command file.
     */
    public static LinkedList<filesprocessing.Printable>
    loadSections(String commandFilePath, ArrayList<File> arrayToProcess)
            throws ErrorsExceptions {

        LinkedList<Printable> printable = new LinkedList<>();
        List<String> lines;

        try{
            Path path = new File(commandFilePath).toPath();
            lines = Files.readAllLines(path);
            validateSectionsList(lines);
        } catch (IOException e) {
            throw new ErrorCommandsFileNotExistException();
        }

        int curFilterTypeIndex, curOrderTypeIndex;
        int curStartSectionIndex = 0;
        while(curStartSectionIndex != lines.size()){
            Filter curSectionFilter;
            Order curSectionOrder;
            curFilterTypeIndex = curStartSectionIndex + LINES_BETWEEN_FILTER_TO_FILTER_TYPE;
            curOrderTypeIndex = curStartSectionIndex + LINES_BETWEEN_FILTER_TO_ORDER_TYPE;

            try{
                curSectionFilter = FilterFactory.getFilter(lines.get(curFilterTypeIndex));
            } catch (WarningFilterException e) {
                printable.add(new Warning(curFilterTypeIndex + 1));
                curSectionFilter = FilterFactory.getDefault();
            }

            try{
                if(curOrderTypeIndex == lines.size() ||
                   lines.get(curOrderTypeIndex).compareTo(FILTER_SUB_SECTION_NAME) == EQUAL){
                    //check if the last line is empty or if the Order type line is new start section
                    curSectionOrder = OrderFactory.getDefault();
                }else{
                    curSectionOrder = OrderFactory.getOrder(lines.get(curOrderTypeIndex));
                }
            }catch (WarningOrderException e){
                printable.add(new Warning(curOrderTypeIndex + 1));
                curSectionOrder = OrderFactory.getDefault();
            }
            Section curSection = new Section(arrayToProcess, curSectionFilter, curSectionOrder);
            curSection.filter();
            curSection.sort();
            printable.add(curSection);
            curStartSectionIndex = getNextSectionStartIndex(curStartSectionIndex + 2, lines);
        }

        return printable;
    }
}

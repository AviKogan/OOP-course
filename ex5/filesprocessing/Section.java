package filesprocessing;

import filesprocessing.Filters.Filter;
import filesprocessing.Filters.FilterFactory;
import filesprocessing.Orders.Order;
import filesprocessing.Orders.OrderFactory;

import java.io.File;
import java.util.ArrayList;

/**
 * This class represent a section in a command file.
 */
class Section implements Printable{

    /**
     * Class constructor, copy the ArrayList with the files in the directory that the section need to process.
     * @param directoryFiles the ArrayList with the files in the directory that the section need to process.
     * @param filterToSet the filter of the section.
     * @param orderToSet the order of the section.
     */
    public Section(ArrayList<File> directoryFiles, Filter filterToSet, Order orderToSet){
        _directoryFiles = new ArrayList<>(directoryFiles);
        _filter = filterToSet;
        _order = orderToSet;
    }

    /**
     * Contains the files in the directory that the section need to process.
     */
    private ArrayList<File> _directoryFiles;

    /**
     * The section given filter.
     */
    private Filter _filter;

    /**
     * The section given order.
     */
    private Order _order;

    /**
     * Override the filesprocessing.Printable print method.
     * Prints all the files in the _directoryFiles member file after file  in the order they appear.
     */
    public void print() {
        for(File file : _directoryFiles){
            System.out.println(file.getName());
        }
    }

    /**
     * Filter the instance _directoryFiles according to the section filter member.
     */
    public void filter(){
        _directoryFiles = _filter.filter(_directoryFiles);
    }

    /**
     * Sort the _directoryFiles according to the section order member.
     */
    public void sort(){
        _order.sort(_directoryFiles);
    }
}

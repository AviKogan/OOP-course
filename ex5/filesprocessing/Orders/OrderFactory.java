package filesprocessing.Orders;

import filesprocessing.exceptions.WarningOrderException;

/**
 * Create Order objects.
 *
 * @author Avi Kogan
 */
public abstract class OrderFactory {

    /**
     * Represent the order of type abs in the command file
     */
    private static final String ABS_ORDER = "abs";

    /**
     * Represent the order of type size in the command file
     */
    private static final String SIZE_ORDER = "size";

    /**
     * Represent the order of type 'type' in the command file
     */
    private static final String TYPE_ORDER = "type";

    /**
     * The splitter between the filter parts in the command file.
     */
    private static final String FILTER_SPLITTER = "#";

    /**
     * Create and return Order object based on the given orderType.
     *
     * @param orderType the Order object type to create as string.
     * @return Order object matched to the given orderType.
     * @throws WarningOrderException if the orderType not match one of the available order types.
     */
    public static Order getOrder(String orderType) throws WarningOrderException {

        String[] orderParts = orderType.split(FILTER_SPLITTER);
        if (orderParts.length == 0) throw new WarningOrderException();

        switch (orderParts[0]){
            case ABS_ORDER:
                return new Abs(orderParts);
            case SIZE_ORDER:
                return new Size(orderParts);
            case TYPE_ORDER:
                return new Type(orderParts);
            default:
                throw new WarningOrderException();
        }
    }

    /**
     * @return the default order type - the Abs Order Object.
     */
    public static Order getDefault(){ return new Abs(); }
}

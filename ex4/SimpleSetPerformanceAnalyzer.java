import java.util.HashSet;
import java.util.LinkedList;
import java.util.TreeSet;

/**
 * Compare run time of add and contain operations between 5 collections.
 * Which collections, files and words will be tested can be controlled with boolean variables in the class.
 *
 * @author Avi Kogan
 */
public class SimpleSetPerformanceAnalyzer {

    private static final int NUM_OF_IMPLEMENTATIONS = 5;

    private static final int FACADE_LINKED_LIST_IND = 0;
    private static final int FACADE_TREE_SET_IND = 1;
    private static final int FACADE_HASHSET_IND = 2;
    private static final int OPEN_HASH_SET_IND = 3;
    private static final int CLOSED_HASH_SET_IND = 4;

    //The names of the data structures according to the order in _simpleSetImplementations
    private final String[] DSIndexesByName = {"LINKED_LIST", "TREE_SET", "HASHSET", "OPEN_HASH_SET",
                                              "CLOSED_HASH_SET"};

    //Choose which data structures will be tested.
    private final boolean CHECK_LINKED_LIST = false;
    private final boolean CHECK_TREE_SET = false;
    private final boolean CHECK_HASHSET_IND = false;
    private final boolean CHECK_OPEN_HASH_SET = true;
    private final boolean CHECK_CLOSED_HASH_SET = false;

    private final boolean[] checkedDS = {CHECK_LINKED_LIST, CHECK_TREE_SET, CHECK_HASHSET_IND,
                                         CHECK_OPEN_HASH_SET, CHECK_CLOSED_HASH_SET};

    //Choose which data files will be tested.
    private final boolean CHECK_DATA1 = true;
    private final boolean CHECK_DATA2 = true;

    private final boolean[] checkedDataFiles = {CHECK_DATA1, CHECK_DATA2};

    //Choose which words to check if contained after each data file words added, index 0 for data1,
    //                                                                           index 1 for data2.
    private final String[][] wordsToCheckContained = {{"hi", "-13170890158"}, {"23", "hi"}};

    //convert rate between nano to ms.
    private static final int CONVERT_NANO_MS = 1000000;

    private static final String[] data1Words = Ex4Utils.file2array( "/Users/avikogan/IdeaProjects/EX4/src" +
            "/data1.txt");
    private static final String[] data2Words = Ex4Utils.file2array("/Users/avikogan/IdeaProjects/EX4/src" +
            "/data2.txt");

    private static final String[][] dataFiles = {data1Words, data2Words};

    private static final String[] dataFilesIndexesByName = {"data1", "data2"};

    private static final int NUM_OF_CONTAIN_ITERATIONS = 10000;

    //array of all the SimpleSet implementations to test.
    SimpleSet[] _simpleSetImplementations = new SimpleSet[NUM_OF_IMPLEMENTATIONS];

    private void initSimpleSetImplementations(){
        _simpleSetImplementations[FACADE_LINKED_LIST_IND] = new CollectionFacadeSet(new LinkedList<String>());
        _simpleSetImplementations[FACADE_TREE_SET_IND] = new CollectionFacadeSet(new TreeSet<String>());
        _simpleSetImplementations[FACADE_HASHSET_IND] = new CollectionFacadeSet(new HashSet<String>());
        _simpleSetImplementations[OPEN_HASH_SET_IND] = new OpenHashSet();
        _simpleSetImplementations[CLOSED_HASH_SET_IND] = new ClosedHashSet();
    }

    /**
     * Runs the tests according to the state of the variables (boolean variables in the class) that determine
     * which files and which structures to test.
     */
    public void runAnalyze(){
        for(int i = 0; i < checkedDataFiles.length; ++i){
            if(checkedDataFiles[i]){
                initSimpleSetImplementations();
                System.out.println("Testing data file " + dataFilesIndexesByName[i]);
                //Test adding with this file.
                testAddDataFile(dataFiles[i]);

                //Test contain with this file.
                testContain(wordsToCheckContained[i]);
            }

        }
    }

    /**
     * Add the given words array, word after word to the array that marked as true in checkedDS.
     * prints for each data structure the time the add of each file took in ms.
     * @param wordsArray the array with the words to add.
     */
    private void testAddDataFile(String[] wordsArray){
        for(int i = 0; i < checkedDS.length; i++){
            if(checkedDS[i]){
                System.out.println("Start add to " + DSIndexesByName[i]);
                long startTime = System.nanoTime();
                addToDSWords(_simpleSetImplementations[i], wordsArray);
                long dif = (System.nanoTime() - startTime) / CONVERT_NANO_MS;
                System.out.println("End adding to " + DSIndexesByName[i] + ", it took: " + dif + " ms.");
            }
        }
    }

    /**
     * Add to dSToAddTo the words from wordsArray.
     * assume both not null.
     * @param dSToAddTo the data structure to add wordsArray to him.
     * @param wordsArray the words to add to dSToAddTo.
     */
    private void addToDSWords(SimpleSet dSToAddTo, String[] wordsArray){
        for(String word : wordsArray){
            dSToAddTo.add(word);
        }
    }

    /**
     * Runs the tests according to the state of the variables (boolean variables in the class) that determine
     * which data structures to test.
     * prints for each data structure the time the contain of each word took in ns.
     * @param wordsToCheck the word to check if contained in the data structures that marked to test.
     */
    private void testContain(String[] wordsToCheck){
        for(String wordToCheck : wordsToCheck){
            for(int i = 0; i < checkedDS.length; i++){
                if(checkedDS[i]){
                    if(i != FACADE_LINKED_LIST_IND){
                        //First run without time checking for linkedList.
                        runContainInDS(_simpleSetImplementations[i], wordToCheck);
                    }

                    //Second run without time checking.
                    System.out.println("Start contain '" + wordToCheck + "' test " + DSIndexesByName[i]);
                    long startTime = System.nanoTime();
                    runContainInDS(_simpleSetImplementations[i], wordToCheck);
                    long dif = (System.nanoTime() - startTime) / NUM_OF_CONTAIN_ITERATIONS;
                    System.out.println("End check contain '" + wordToCheck +"' in " + DSIndexesByName[i] + "," +
                                       " it took: " + dif + " " + "ns.");
                }
            }
        }
    }

    /**
     * Run contain in dsToCheck with the word wordsToCheck.
     * assume both not null
     * @param dsToCheck the data structure to run contain on with the wordsToCheck.
     * @param wordsToCheck the word to check if contained in dsToCheck.
     */
    private void runContainInDS(SimpleSet dsToCheck, String wordsToCheck){
        for(int i = 0; i < NUM_OF_CONTAIN_ITERATIONS; ++i){
            dsToCheck.contains(wordsToCheck);
        }
    }
}

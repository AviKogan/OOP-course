package filesprocessing;

import filesprocessing.exceptions.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Run process from given command file on given directory, prints the process output.
 * @author Avi kogan
 */
public abstract class DirectoryProcessor {

    /**
     * The expected number of arguments given to the program.
     */
    private static final int EXPECTED_ARGS_NUMBER = 2;

    /**
     * The index in the args array of the path to the directory to filter.
     */
    private static final int DIR_PATH_IND = 0;

    /**
     * The index in the args array of the path to the Command file to filter according to.
     */
    private static final int COMMAND_FILE_PATH_IND = 1;

    /**
     * Validate the given args actually a exist directory and file.
     * @param args the args to validate.
     * @throws ErrorsExceptions of one of the args not valid.
     */
    private static void validateArgs(String[] args) throws ErrorsExceptions{

        if(args.length != EXPECTED_ARGS_NUMBER) throw new ErrorUsageException();

        Path srcDirPath = Paths.get(args[DIR_PATH_IND]);
        Path CommandFilePath = Paths.get(args[COMMAND_FILE_PATH_IND]);
        if(!Files.isDirectory(srcDirPath)) throw new ErrorDirectoryNotExistException();
        else if(!Files.exists(CommandFilePath)) throw new ErrorCommandsFileNotExistException();
    }


    /**
     * The main function, run the directory process according to the given args.
     * @param args the args to run the directory process according to.
     */
    public static void main(String[] args){

        try{
            validateArgs(args);

            File dir = new File(args[DIR_PATH_IND]);
            File[] filesAndDirInSrcDir = dir.listFiles(); //dir path validated will not return null.
            ArrayList<File> filesInDir = new ArrayList<>();
            for(int i = 0; i < filesAndDirInSrcDir.length; ++i){
                if(filesAndDirInSrcDir[i].isFile()){
                    filesInDir.add(filesAndDirInSrcDir[i]);
                }
            }

            LinkedList<Printable> printables = SectionsProcessor.loadSections(args[COMMAND_FILE_PATH_IND],
                                                                            filesInDir);
            for(Printable printable : printables){
                printable.print();
            }
        }catch(ErrorsExceptions ignored) {}
    }
}

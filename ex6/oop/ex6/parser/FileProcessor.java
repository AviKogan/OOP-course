package oop.ex6.parser;
import oop.ex6.main.IllegalCodeException;
import oop.ex6.scopes.*;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Abstract class process the Sjavac file.
 *
 * @author Avi Kogan
 * @author Carel Gadot
 */
public abstract class FileProcessor {

    /**
     * initialized the static members for the file process.
     */
    public static void initStatics(){
        curFileLine = 0;
        openScopes = 0;
        ScopeFactory.initGlobal();
        Global.initDeclaredMethods();
    }

    /* Represent the number of openScopes after all the brackets that opened got closed */
    private static final int ALL_BRACKETS_CLOSED = 0;

    /* Markers for the file line reading */
    private static int curFileLine, openScopes, innerScopeLine;

    /* Initialized ti avoid creating new Matcher in each call */
    private static final Matcher methodMatcher = RegexTool.METHOD_DECLARATION.matcher(""),
                                 conditionMatcher = RegexTool.CONDITION_LINE.matcher("");

    /**
     * The method get the file and check if the file is valid, if not will throw exception.
     * @param SJavaLines the Sjava file lines in List.
     * @throws IllegalCodeException thrown if some Exception that should print 1 at the end thrown while
     *                              processing
     */
    public static void processFile(List<String> SJavaLines) throws IllegalCodeException {
        initStatics();
        Global global = ScopeFactory.getGlobal();
        processGlobal(SJavaLines, global);
        global.validateScope(); //will validate the need to be declared variables and the function calls.
    }

    /**
     * Insert to the opened method its inner line until the method closed, start inserting line according
     * to the static curFileLine, while inserting lines updating the openScopes, will finish the reading if
     * the method closed or if the file ends..
     * @param SJavaLines the Sjava file.
     * @param cur the opened scope.
     */
    private static void scanInnerScopes(List<String> SJavaLines, Scope cur) {
        while(openScopes != ALL_BRACKETS_CLOSED && curFileLine < SJavaLines.size()) {
            cur.addLine(SJavaLines.get(curFileLine));
            if (RegexTool.isMatch(SJavaLines.get(curFileLine), RegexTool.OPEN_BRACKET_SUFFIX_REGEX)) {
                openScopes++;
            } else if(RegexTool.isMatch(SJavaLines.get(curFileLine), RegexTool.CLOSE_BRACKET_SUFFIX_REGEX)) {
                openScopes--;
            }
            curFileLine++;
        }
    }

    /**
     * Process the global scopes, create inner method scopes and create the variables in the scopes.
     * @param SJavaLines the Sjava file.
     * @param global the global scope object of the file.
     * @throws IllegalCodeException if invalid line or the structure of the file (open close brackets) is
     *                              invalid.
     */
    private static void processGlobal(List<String> SJavaLines, Global global) throws IllegalCodeException {
        while (curFileLine < SJavaLines.size()) {
            String line = SJavaLines.get(curFileLine);

            if (RegexTool.isMatch(line, RegexTool.EMPTY_LINE_REGEX) ||
                    RegexTool.isMatch(line, RegexTool.COMMENT_LINE_REGEX)) {
                curFileLine++;
            }else if(methodMatcher.reset(line).matches()){
                Method newMethod = ScopeFactory.addMethodToGlobal(methodMatcher);
                openScopes++;
                curFileLine++;
                scanInnerScopes(SJavaLines, newMethod);
            } else if (RegexTool.isMatch(line, RegexTool.SEMI_COLON_SUFFIX_REGEX)) {
                if(RegexTool.isMatch(line, RegexTool.RETURN_LINE)){
                    // return is invalid in the global.
                    throw new ReturnInGlobalException();
                }
                SemiColonProcessor.parse(line, global);
                curFileLine++;
            } else throw new SyntaxException(); //not valid line
        }
        if(openScopes != ALL_BRACKETS_CLOSED){
            throw new MisMatchOpenCloseBracketsException();
        }
    }

    /**
     * Process inner scopes after the global processed, get a Scope and process it's line.
     * @param scopeLines the lines in the files of the scope.
     * @param cur the inner scope object.
     * @param curLine the line to start process from in scopeLines.
     * @throws IllegalCodeException if invalid line appear in the inner scope.
     */
    public static void processInnerScopes(List<String> scopeLines, Scope cur, int curLine) throws IllegalCodeException{
        innerScopeLine = curLine;
        while (innerScopeLine < scopeLines.size()) {
            String line = scopeLines.get(innerScopeLine);

            if (RegexTool.isMatch(line, RegexTool.EMPTY_LINE_REGEX) ||
                    RegexTool.isMatch(line, RegexTool.COMMENT_LINE_REGEX)) {
                innerScopeLine++;
            } else if (conditionMatcher.reset(line).matches()) {
                innerScopeLine++;
                Scope inner = ScopeFactory.createCondition(cur, conditionMatcher);
                processInnerScopes(scopeLines, inner, innerScopeLine);
            } else if (RegexTool.isMatch(line, RegexTool.CLOSE_BRACKET_SUFFIX_REGEX)) {
                innerScopeLine++;
                return;
            } else if (RegexTool.isMatch(line, RegexTool.SEMI_COLON_SUFFIX_REGEX)) {
                SemiColonProcessor.parse(line, cur);
                innerScopeLine++;
            } else throw new SyntaxException(); //not valid line
        }
    }
}

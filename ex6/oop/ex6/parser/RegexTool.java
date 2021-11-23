package oop.ex6.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Abstract class contain all the needed compiled regexes for the Sjavac program.
 *
 * @author Avi Kogan.
 * @author Carmel Gadot.
 */
public abstract class RegexTool {
    public static final String OPERATOR = "\\s*(\\|\\||&&)\\s*",  COMMA =  ",";

    private static final String SPACE =  "\\s*",
            MUST_SPACE = "\\s+",
            TYPE_REGEX = "(double|int|String|boolean|char)",
            FINAL = "final",
            SEMI_COLON = ";",
            EQUAL = "=",
            INT_VALUE =  "(-)?\\d+",
            DOUBLE_VALUE_REGEX = "(-?)(\\d+)(\\.\\d*)?|(-?)(\\d*)(\\.\\d+)?",
            STRING_VALUE = "\".*\"",
            CHAR_VALUE = "'.'",
            BOOLEAN_VALUE = "true|false|"+DOUBLE_VALUE_REGEX+"|"+INT_VALUE,
            VAR_NAME = "_\\w+|[a-zA-Z]\\w*",
            VALUE = BOOLEAN_VALUE+"|"+DOUBLE_VALUE_REGEX+"|"+INT_VALUE+"|"+CHAR_VALUE+"|"+STRING_VALUE+"|"+VAR_NAME,
            METHOD_NAME = "[a-zA-Z]\\w*",
            INITIAL_OPTIONAL = "("+SPACE+EQUAL+SPACE+"("+VALUE+")"+SPACE+")?",
            MULTIPLE_VARIABLE_OPTIONAL = "("+SPACE+"("+VAR_NAME+")"+INITIAL_OPTIONAL+SPACE+COMMA+SPACE+")*",
            DEFINE_ONE_VARIABLE = SPACE+"("+VAR_NAME+")"+INITIAL_OPTIONAL+SPACE,
            DEFINING_VARIABLE_REGEX = SPACE+TYPE_REGEX+MUST_SPACE+MULTIPLE_VARIABLE_OPTIONAL+DEFINE_ONE_VARIABLE+
                    SPACE+SEMI_COLON+SPACE,
            MULTIPLE_VARIABLE_FINAL_OPTIONAL = "("+SPACE+"("+VAR_NAME+")"+SPACE+EQUAL+SPACE+"("+VALUE+")"+SPACE+COMMA+SPACE+ ")*",
            DEFINE_ONE_FINAL_VARIABLE = SPACE+"("+VAR_NAME+")"+SPACE+EQUAL+SPACE+"("+VALUE+")" +SPACE,
            DEFINING_FINAL_VARIABLE_REGEX = SPACE+FINAL+MUST_SPACE+TYPE_REGEX+MUST_SPACE+
                    MULTIPLE_VARIABLE_FINAL_OPTIONAL+DEFINE_ONE_FINAL_VARIABLE+SPACE+SEMI_COLON+SPACE,
            INITIAL_OPTIONAL_FACTORY = "(" + SPACE + EQUAL + SPACE +"("+VALUE+")" + SPACE + ")?",
            ONE_CONDITION_EXP = "\\s*("+VAR_NAME+"|true|false|"+DOUBLE_VALUE_REGEX+"|"+INT_VALUE+")\\s*";


    public static final Pattern OPEN_BRACKET_SUFFIX_REGEX = Pattern.compile(".*[{]\\s*"),
            CLOSE_BRACKET_SUFFIX_REGEX = Pattern.compile("\\s*[}]\\s*"),
            SEMI_COLON_SUFFIX_REGEX =  Pattern.compile(".*;\\s*"),
            COMMENT_LINE_REGEX = Pattern.compile("//.*"),
            EMPTY_LINE_REGEX = Pattern.compile("\\s*"),
            CONDITION_LINE = Pattern.compile("\\s*(if|while)\\s*\\((.+)\\)\\s*\\{\\s*"),
            CALLING_METHOD_LINE = Pattern.compile("\\s*("+METHOD_NAME+")\\s*\\((.*)\\)\\s*;\\s*"),
            METHOD_DECLARATION = Pattern.compile("\\s*void\\s+("+METHOD_NAME+")\\s*\\((.*)\\)\\s*[{]\\s*"),
            RETURN_LINE = Pattern.compile("\\s*return\\s*;\\s*"),
            CHANGING_VARIABLE_VALUE_LINE = Pattern.compile("\\s*("+VAR_NAME+")\\s*=\\s*("+VALUE+")\\s*;\\s*"),
            VAR_NAME_PATTERN = Pattern.compile(VAR_NAME),
            DEFINING_VARIABLE_LINE = Pattern.compile(DEFINING_FINAL_VARIABLE_REGEX+"|"+DEFINING_VARIABLE_REGEX),
            BOOLEAN_REGEX = Pattern.compile("true|false|"+DOUBLE_VALUE_REGEX+"|"+INT_VALUE),
            CHAR_REGEX = Pattern.compile(CHAR_VALUE),
            DOUBLE_VALUE_REGEX_PATTERN = Pattern.compile(DOUBLE_VALUE_REGEX),
            INT_VALUE_REGEX_PATTERN = Pattern.compile(INT_VALUE),
            STRING_REGEX_PATTERN =  Pattern.compile("\".*\""),
            FINAL_VARIABLE_DECLARATION = Pattern.compile("\\s*final\\s+"+TYPE_REGEX+".+"),
            NOT_FINAL_VARIABLE_DECLARATION = Pattern.compile("\\s*"+TYPE_REGEX+".+"),
            REGEX_VARIABLE_FACTORY =
                    Pattern.compile("\\s*(final\\s)?\\s*"+TYPE_REGEX+"?\\s*(" + VAR_NAME + ")" +
                            INITIAL_OPTIONAL_FACTORY + SPACE + ";?" + SPACE),
            FUNCTION_PARAMS = Pattern.compile("\\s*(final\\s)?\\s*"+TYPE_REGEX+"\\s+("+VAR_NAME+")\\s*"),
            ONE_CONDITION_PATTERN = Pattern.compile(ONE_CONDITION_EXP),
            TYPE_FOR_CONDITION = Pattern.compile("int|double|boolean"),
            TYPE_FOR_BOOLEAN_ASSIGMENT = Pattern.compile("int|double|boolean"),
            TYPE_FOR_DOUBLE_ASSIGMENT = Pattern.compile("int|double"),
            PARAM_PATTERN = Pattern.compile("\\s*("+VALUE+")\\s*");


    /**
     * @param line the line to check.
     * @param p the patter to check if the line match to.
     * @return true if the line match to the given pattern, otherwise false.
     */
    public static boolean isMatch(String line, Pattern p) {
        Matcher matcher = p.matcher(line);
        return matcher.matches();
    }
}

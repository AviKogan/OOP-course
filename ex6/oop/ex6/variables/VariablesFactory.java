package oop.ex6.variables;

import oop.ex6.main.IllegalCodeException;
import oop.ex6.parser.RegexTool;
import oop.ex6.parser.SyntaxException;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Abstract class has a single static method createVariable it is used to create Variable objects with
 * with the appropriate ValueValidator obj according to variableLine.
 */
public abstract class VariablesFactory {

    /* The number group in the regex in the createParamVariables method */
    private static final int FINAL = 1, TYPE  = 2, NAME  = 3;

    /* string represent double type */
    private static final String DOUBLE = "double";

    /* string represent boolean type */
    private static final String BOOLEAN = "boolean";

    /* string represent char type */
    private static final String CHAR = "char";

    /* string represent int type */
    private static final String INT = "int";

    /* string represent string type */
    private static final String STRING = "String";

    /**
     * Create new variable according to the given parameters.
     * @param isFinal the final state in the declaration
     * @param type the type in the declaration.
     * @param varName the name of the declared variable.
     * @param value the value in the declaration.
     * @param isGlobal represent if the variable declared in the global scope.
     * @return new created variable according to the given parameters.
     * @throws NameTypeException if the variable name not valid.
     * @throws ValueOfVariableException if the variable value can be other declared variable
     * @throws ValueTypeException if the value not match to the variable type.
     */
    public static Variable createVariable(boolean isFinal, String type, String varName, String value,
                                          boolean isGlobal) throws IllegalCodeException,
            ValueOfVariableException {

        if (type.equals(INT))
            return new Variable(new IntValueValidator(), varName, isFinal, value, type, isGlobal);

        if (type.equals(BOOLEAN))
            return new Variable(new BooleanValueValidator(), varName, isFinal, value, type, isGlobal);

        if (type.equals(DOUBLE))
            return new Variable(new DoubleValueValidator(), varName, isFinal, value, type, isGlobal);

        if (type.equals(STRING))
            return new Variable(new StringValueValidator(), varName, isFinal, value, type, isGlobal);

        if (type.equals(CHAR))
            return new Variable(new CharValueValidator(), varName, isFinal, value, type, isGlobal);

        throw new NameTypeException();
    }

    /**
     * Creates from params line of method List of initialized variables.
     * @param paramsLine the method parameters line.
     * @return List with the created Variable objects.
     * @throws SyntaxException if the params line not is not valid pattern.
     */
    public static List<Variable> createParamVariables(String paramsLine) throws IllegalCodeException {
        LinkedList<Variable> paramsVariables = new LinkedList<>();
        if(RegexTool.isMatch(paramsLine, RegexTool.EMPTY_LINE_REGEX)) return paramsVariables;

        String[] params = paramsLine.split(RegexTool.COMMA);
        for(String param : params){
            Matcher m = RegexTool.FUNCTION_PARAMS.matcher(param);
            if(!m.matches()) throw new SyntaxException();
            Variable newVar;
            if(m.group(FINAL) != null){ //final
                newVar = createVariable(true, m.group(TYPE), m.group(NAME), null, false);

            }else if(m.group(FINAL) == null){ //not final
                newVar = createVariable(false, m.group(TYPE), m.group(NAME), null, false);
            } else throw new SyntaxException();
            newVar.setInitialized();// params considered initialized
            paramsVariables.add(newVar);
        }

        return paramsVariables;
    }

}


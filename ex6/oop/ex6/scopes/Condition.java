package oop.ex6.scopes;

import oop.ex6.main.IllegalCodeException;
import oop.ex6.parser.RegexTool;
import oop.ex6.parser.SyntaxException;
import oop.ex6.variables.Variable;

import java.util.regex.Matcher;


/**
 * Represent a scope of condition - while or if.
 *
 * @author Avi Kogan.
 * @author Carmel Gadot.
 */
class Condition extends Scope{

    /**
     * Class constructor.
     * @param prev the previous scope.
     * @param conditionParams the String inside the parenthesis of the condition.
     *
     * @throws SyntaxException if the syntax of the condition is invalid.
     * @throws UnknownVariableNameException if in the condition exist variable name and the variable is
     *                                      unknown.
     */
    public Condition(Scope prev, String conditionParams) throws IllegalCodeException {
        super(prev);
        String[] array = conditionParams.split(RegexTool.OPERATOR);
        for( String str: array) {
            Matcher m = RegexTool.ONE_CONDITION_PATTERN.matcher(str);
            if(!m.matches()) throw new SyntaxException();
            //check if condition param can be variable
            if(RegexTool.isMatch(m.group(1), RegexTool.VAR_NAME_PATTERN) &&
                    !m.group(1).equals("true") && !m.group(1).equals("false")){
                checkIfVariableValid(m.group(1));
            }
        }

    }

    /**
     * @param varName the name of the variable in the condition.
     *
     * @throws InvalidVariableForConditionException if the variable in the condition not valid for
     *                                              condition line
     * @throws UnknownVariableNameException if in the condition exist variable name and the variable is
     *                                      unknown.
     */
    private void checkIfVariableValid(String varName) throws IllegalCodeException {
        Variable cur = getVariable(varName);
        if(!cur.isInitialized() || !RegexTool.isMatch(cur.getType(), RegexTool.TYPE_FOR_CONDITION)){
            throw new InvalidVariableForConditionException();
        }
    }


    /**
     * @return used to identify the scope variables - if global or not.
     */
    @Override
    public boolean isGlobal() { return false; }
}

package oop.ex6.scopes;

import oop.ex6.main.IllegalCodeException;
import oop.ex6.parser.FileProcessor;
import oop.ex6.parser.RegexTool;
import oop.ex6.parser.SyntaxException;
import oop.ex6.variables.Variable;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Represent a methods scope in the file.
 *
 * @author Avi Kogan.
 * @author Carmel Gadot.
 */
public class Method extends Scope{

    /**
     * Class constructor, assumes the name of the method is valid name for method.
     * @param prev the method previous scope.
     * @param name the method name.
     * @throws DuplicateMethodNameException if method with the same name already declared.
     */
    Method(Scope prev, String name) throws DuplicateMethodNameException {
        super(prev);
        validateName(name);
    }

    /**
     * Validate the method scope is valid.
     * @throws IllegalCodeException if the return line not at the end of the scope, or if there is
     *                              invalid line in the scope.
     */
    public void validateScope() throws IllegalCodeException {
        validateReturnAtEnd();
        FileProcessor.processInnerScopes(_scopeLines, this, 0);
    }

    /**
     * Check if the given name match to already declared method.
     * @param name the name of the method.
     * @throws DuplicateMethodNameException if the name match to already declared method.
     */
    private void validateName(String name) throws DuplicateMethodNameException {
        if(Global.declaredMethod.containsKey(name)) throw new DuplicateMethodNameException();
    }

    /**
     * Add the given Variable object to params and local variable.
     * @param newParam the param Variable object to add.
     * @throws DuplicateVariableDeclarationException if the param name match to other param name.
     */
    public void addParam(Variable newParam) throws DuplicateVariableDeclarationException{
        if(_paramNames.contains(newParam.getName())) throw new DuplicateVariableDeclarationException();
        _paramNames.add(newParam.getName());
        _localVariables.put(newParam.getName(), newParam);
        _params.add(newParam);
    }

    /**
     * Check if return statement appear in the line before the closer brackets.
     * @throws IllegalCodeException if the method contain less than 2 lines or if there is no return statement
     *                              in the line before the closer brackets.
     */
    private void validateReturnAtEnd() throws IllegalCodeException {
        if(_scopeLines.size() < MIN_NUN_OF_LINES_IN_METHOD_SCOPE) throw new IllegalCodeException();
        if(!RegexTool.isMatch(_scopeLines.get(_scopeLines.size() - LINES_TO_RETURN), RegexTool.RETURN_LINE)){
            throw new SyntaxException();
        }
    }

    /**
     * @return used to identify the scope variables - if global or not.
     */
    @Override
    public boolean isGlobal() { return false; }

    /**
     * @return the params variables list.
     */
    LinkedList<Variable> getParams() { return _params; }

    /**
     * @return the number of params in the method.
     */
    int getNumOfParams() { return _params.size(); }

    /* Contain the params names, used to avoid name duplication */
    HashSet<String> _paramNames = new HashSet<>();

    /* Contain the variables in the params, all considered initialize */
    LinkedList<Variable> _params = new LinkedList<>();

    /* Represent the minimal lines in method scope, return and closed brackets lines. */
    private static final int MIN_NUN_OF_LINES_IN_METHOD_SCOPE = 2;

    /* The place the return statement should appear related to the last line */
    private static final int LINES_TO_RETURN = 2;
}

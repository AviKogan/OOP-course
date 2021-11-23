package oop.ex6.scopes;

import oop.ex6.main.IllegalCodeException;
import oop.ex6.parser.RegexTool;
import oop.ex6.variables.ValueOfVariableException;
import oop.ex6.variables.ValueTypeException;
import oop.ex6.variables.Variable;
import oop.ex6.variables.VariablesFactory;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;

/**
 * Abstract class Represent a scope in Sjava file.
 *
 * @author Avi Kogan.
 * @author Carmel Gadot.
 */
public abstract class Scope {

    /**
     * Class constructor, used to init the the shared members.
     * @param prev the scope previous scope.
     */
    Scope(Scope prev){ _prev = prev; }

    /**
     * adds to the scope its line.
     * @param line the scope line to add.
     */
    public void addLine(String line){
        _scopeLines.add(line);
    }

    /**
     * @return used to identify the scope variables - if global or not.
     */
    public abstract boolean isGlobal();

    /**
     * Adds to the scope a declared variables.
     *
     * @param newVar the new variable to add to the scope.
     * @throws  DuplicateVariableDeclarationException if the variable name already declared in the scope.
     */
    public void addNewVariable(Variable newVar) throws DuplicateVariableDeclarationException{
        checkVarNameDup(newVar.getName());
        _localVariables.put(newVar.getName(), newVar);
    }

    /**
     * @param name name of variable to check.
     * @throws DuplicateVariableDeclarationException if the variable name already declared in the scope.
     */
    protected void checkVarNameDup(String name) throws DuplicateVariableDeclarationException {
        if(_localVariables.containsKey(name)) throw new DuplicateVariableDeclarationException();
    }

    /**
     * Called if the assigment to variable failed but the value can be an exist variable, check if the
     * value is actually declared variable and trying to make assigment.
     * @param isFinal the assigned variable final state.
     * @param type the assigned variable type.
     * @param varName the assigned variable name.
     * @param value the value in assigment that can be a declared variable.
     * @throws IllegalCodeException if the assigment failed.
     */
    public void addDeclarationFailedAssigment (boolean isFinal, String type, String varName,
                                                    String value) throws IllegalCodeException{
        checkVarNameDup(varName);
        Variable assigned = getVariable(value);
        if(assigned.isInitialized()){
            Variable newVar = VariablesFactory.createVariable(isFinal, type, varName, null, false);
            newVar.setInitialized();
            newVar.validateTypeForAssignment(assigned.getType());
            _localVariables.put(varName, newVar);
            return;
        }
        throw new IllegalCodeException();
    }

    /**
     * Validate if the given method call is valid.
     * @param methodCall the matcher of the method call line.
     * @throws IllegalCodeException if the method call invalid.
     */
    public void methodCall(Matcher methodCall) throws IllegalCodeException {
        Method called = Global.declaredMethod.get(methodCall.group(1));
        if(called == null) throw new IllegalCodeException(); // not exist todo change name
        String[] calledParams;
        if(methodCall.group(2).equals("")){
            calledParams =  new String[]{};
        }else{
            calledParams = methodCall.group(2).split(RegexTool.COMMA);
        }
        if(called.getNumOfParams() != calledParams.length) throw new IllegalCodeException();
        for(int i = 0; i < calledParams.length; ++i){
            try{
                if(!paramMatcher.reset(calledParams[i]).matches()) throw new IllegalCodeException();
                called.getParams().get(i).isValidValue(paramMatcher.group(1));
            } catch (ValueOfVariableException e){
                Variable paramVal = getVariable(calledParams[i]);
                if(!paramVal.isInitialized()) throw new IllegalCodeException();
                called.getParams().get(i).validateTypeForAssignment(paramVal.getType());
            }

        }
    }

    /**
     * Used when assignment to variable not in declaration happened.
     * @param varName the assigned variable name.
     * @param value the assigned value.
     * @throws IllegalCodeException if the assigment invalid.
     */
    public void updateValueForDeclaredVar(String varName, String value) throws IllegalCodeException{
        Variable cur = getVariable(varName);
        try{
            cur.setValue(value);
            // not final
            updateHelper(varName, cur);
        } catch (ValueOfVariableException e){
            Variable assigned = getVariable(value);
            if(assigned.isInitialized() && !cur.isFinal()){
                cur.validateTypeForAssignment(assigned.getType());
                updateHelper(varName, cur);
            }
            throw new ValueTypeException();
        }
    }

    /**
     * Helper for updateValueForDeclaredVar method, creates a copy of the variable in the local scope if
     * the variable from the global scope, otherwise initialize the variable.
     * @param varName the assigned variable name.
     * @param cur the assigned variable Object.
     * @throws IllegalCodeException if the creation in the local scope failed.
     */
    private void updateHelper(String varName, Variable cur) throws IllegalCodeException {
        if (cur.isGlobal()) {
            // create local copy to avoid affecting the global var
            Variable newVar = VariablesFactory.createVariable(false, cur.getType(), varName, null, false);
            newVar.setInitialized();
            _localVariables.put(varName, newVar);
        } else {
            //not global -> init
            cur.setInitialized();
        }
    }

    /**
     * @param varName the variable name to return its Variable object.
     * @return the Variable object found in the scope or in the closest outer scope, if not found return null.
     * @throws UnknownVariableNameException if the variable didnt founf by its name in the scope and also
     *                                      in the outer scopes.
     */
    protected Variable getVariable(String varName) throws UnknownVariableNameException {
        Scope curScope = this;
        while( curScope != null){
            if(curScope._localVariables.containsKey(varName)){
                return curScope._localVariables.get(varName);
            }
            curScope = curScope._prev;
        }

        throw new UnknownVariableNameException();
    }

    /* Contain the scope lines. */
    protected LinkedList<String> _scopeLines = new LinkedList<>();

    /* Represent the previous scope related to this scope. */
    protected Scope _prev;

    /* Map the declared variables in the scope, name of the variable to it's Variable object. */
    protected HashMap<String , Variable> _localVariables = new HashMap<>();

    /*  Created to avoid creating new Matcher each time methodCall called. */
    private final Matcher paramMatcher = RegexTool.PARAM_PATTERN.matcher("");
}

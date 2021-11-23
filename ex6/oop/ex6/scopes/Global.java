package oop.ex6.scopes;

import oop.ex6.main.IllegalCodeException;
import oop.ex6.variables.ValueOfVariableException;
import oop.ex6.variables.ValueTypeException;
import oop.ex6.variables.Variable;
import oop.ex6.variables.VariablesFactory;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;

/**
 * Represent the global scope of the file.
 *
 * @author Avi Kogan.
 * @author Carmel Gadot.
 */
public class Global extends Scope{

    /**
     * Class constructor.
     * @param prev the previous scope.
     */
    Global(Scope prev) { super(prev); }

    /**
     * Init the static member that contain all the declared method in the file.
     */
    public static void initDeclaredMethods(){ declaredMethod = new HashMap<>(); }

    /**
     * public to allow to give all the scopes access to the declared method.
     */
    public static HashMap<String, Method> declaredMethod;

    /**
     * validate the global scope, validating the inner scopes.
     * @throws IllegalCodeException if there is invalid line in one of the inner methods scopes.
     */
    public void validateScope() throws IllegalCodeException {
        for(Method m : _methodDeclarationOrder){
            m.validateScope();
        }
    }

    /**
     * throws exception because calling to method is invalid in the global scope.
     * @param methodCall the matcher of the method call line.
     * @throws MethodCallInGlobalException the exception represent invalid call in global to method.
     */
    @Override
    public void methodCall(Matcher methodCall) throws MethodCallInGlobalException {
        throw new MethodCallInGlobalException();
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
    @Override
    public void addDeclarationFailedAssigment(boolean isFinal, String type, String varName, String value)
            throws IllegalCodeException {
        checkVarNameDup(varName);
        Variable valueVar = getVariable(value);

        // value is exist variable.
        if(!valueVar.isInitialized()) throw new ValueTypeException();
        Variable newVar = VariablesFactory.createVariable(isFinal, type, varName, null, true);
        newVar.validateTypeForAssignment(valueVar.getType());
        newVar.setInitialized();
        _localVariables.put(varName, newVar);
    }

    /**
     * Adds the declared method object in the global.
     * @param methodToAdd  the declared
     */
    public void addMethods(Method methodToAdd) {
        _methodDeclarationOrder.add(methodToAdd);
    }

    /**
     * Used when assignment to variable not in declaration happened.
     * @param varName the assigned variable name.
     * @param value the assigned value.
     * @throws IllegalCodeException if the assigment invalid.
     */
    @Override
    public void updateValueForDeclaredVar(String varName, String value) throws IllegalCodeException {
        Variable cur = getVariable(varName);
        try{
            cur.setValue(value);
            cur.setInitialized();
        }catch (ValueOfVariableException e){
            Variable valueVariable = getVariable(value);
            if(!valueVariable.isInitialized()) throw new IllegalCodeException();
            cur.validateTypeForAssignment(valueVariable.getType());
            cur.setInitialized();
        }
    }

    /**
     * @return used to identify the scope variables - if global or not.
     */
    @Override
    public boolean isGlobal() { return true; }

    /* Contain the methods declared in the global by its order */
    private final LinkedList<Method> _methodDeclarationOrder = new LinkedList<>();
}

package oop.ex6.scopes;

import oop.ex6.main.IllegalCodeException;
import oop.ex6.variables.Variable;
import oop.ex6.variables.VariablesFactory;

import java.util.List;
import java.util.regex.Matcher;

/**
 * Abstract class that creates scopes.
 *
 * @author Avi Kogan
 * @author Carmel Gadot.
 */
public abstract class ScopeFactory {

    /* singleton, the global scope of the file */
    private static Global _global;

    /**
     * Initialize a single global scope.
     */
    public static void initGlobal(){ _global = new Global(null); }

    /**
     * @return the global scope of the file.
     */
    public static Global getGlobal(){ return _global; }

    /**
     * Creates new method and add it to the global scope.
     * @param methodMatcher the matcher of the method declaration line.
     * @return the new created Method object.
     * @throws IllegalCodeException if the creation of the method failed.
     */
    public static Method addMethodToGlobal(Matcher methodMatcher) throws IllegalCodeException {
        if(Global.declaredMethod.containsKey(methodMatcher.group(1))) throw new DuplicateMethodNameException();
        //foo(int a, final int a)
        Method newMethod = new Method(_global, methodMatcher.group(1));
        List<Variable> params = VariablesFactory.createParamVariables(methodMatcher.group(2));
        for(Variable v : params){
            newMethod.addParam(v);
        }
        Global.declaredMethod.put(methodMatcher.group(1), newMethod);
        _global.addMethods(newMethod);
        return newMethod;
    }

    /**
     * create a condition scope.
     * @param prevScope the previous scope of the new condition scope.
     * @param conditionMatcher the matcher of the condition declaration line.
     * @return new Condition object
     * @throws IllegalCodeException if the condition creation failed.
     */
    public static Condition createCondition(Scope prevScope, Matcher conditionMatcher) throws IllegalCodeException {
        return new Condition(prevScope, conditionMatcher.group(2));
    }
}

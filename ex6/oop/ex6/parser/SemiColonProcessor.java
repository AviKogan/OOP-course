package oop.ex6.parser;

import oop.ex6.main.IllegalCodeException;
import oop.ex6.scopes.Scope;
import oop.ex6.variables.*;
import oop.ex6.variables.VariablesFactory;

import java.util.regex.Matcher;

/**
 * Abstract class with static method used to process line in Sjava file that end with semi colon.
 *
 * @author Avi Kogan.
 * @author Carmel Gadot.
 */
public abstract class SemiColonProcessor {

    /**
     * Parsing the line and add to the given scope the line parsing result.
     * @param line the line that ends with semicolon.
     * @param scope the scope the line inside.
     * @throws IllegalCodeException if the line is invalid.
     */
    public static void parse(String line, Scope scope) throws IllegalCodeException {

        /* declaring variables */
        if (RegexTool.isMatch(line, RegexTool.DEFINING_VARIABLE_LINE)) {
            declarationParser(line, scope);
        }

        /* changing variable values */
        else if (RegexTool.isMatch(line, RegexTool.CHANGING_VARIABLE_VALUE_LINE)) {
            Matcher m = RegexTool.CHANGING_VARIABLE_VALUE_LINE.matcher(line);
            if(!m.matches()) throw new SyntaxException();

            scope.updateValueForDeclaredVar(m.group(1), m.group(2));
        }

        /* calling methods */
        else if (callMatcher.reset(line).matches()) {
            scope.methodCall(callMatcher);
        }

        /* returning */
        else if (RegexTool.isMatch(line, RegexTool.RETURN_LINE)) {
            //do nothing in the inned scopes, method validate it by checking the line before the end.
        }

        /* no match to valid semi colon line */
        else throw new SyntaxException();

    }

    /* declared to avoid creating new one each time the relevant method called. */
    private static final Matcher finalMatcher = RegexTool.FINAL_VARIABLE_DECLARATION.matcher(""),
                                 notFinalMatcher = RegexTool.NOT_FINAL_VARIABLE_DECLARATION.matcher(""),
                                 callMatcher = RegexTool.CALLING_METHOD_LINE.matcher(""),
                                 declarationMatcher = RegexTool.REGEX_VARIABLE_FACTORY.matcher("");

    /**
     * Parse line that match to variable declaration and add to the scope the variable in the line
     * according to the parsing.
     * @param line the line of the declaration.
     * @param scope the scope the declaration in.
     * @throws IllegalCodeException if the declaration process failed.
     */
    private static void declarationParser(String line, Scope scope) throws IllegalCodeException {

        String[] declarationParts = line.split(RegexTool.COMMA);
        if (finalMatcher.reset(line).matches()) {
            processDeclaration(finalMatcher, scope, declarationParts, true);
        } else if (notFinalMatcher.reset(line).matches()) {
            processDeclaration(notFinalMatcher, scope, declarationParts, false);
        }
    }

    /**
     * Helper method for declarationParser, add to the given scope the variable after checking if if final
     * declaration.
     * @param matcher the matcher of the line - can be final declaration or not.
     * @param scope the scope the declaration in.
     * @param declarationParts the declaration after split by comma.
     * @param isFinal true if the line match to final variable declaration, otherwise false.
     * @throws IllegalCodeException if the line of declaration is invalid because syntax or because
     *                              variable name or value.
     */
    private static void processDeclaration(Matcher matcher, Scope scope, String[] declarationParts,
                                           boolean isFinal)
            throws IllegalCodeException {

        for (String var : declarationParts) {
            if (!declarationMatcher.reset(var).matches()) throw new SyntaxException();

            try {
                scope.addNewVariable(VariablesFactory.createVariable(isFinal, matcher.group(1),
                        declarationMatcher.group(3), declarationMatcher.group(5), scope.isGlobal()));
            }catch (ValueOfVariableException e) {
                scope.addDeclarationFailedAssigment(isFinal, matcher.group(1),
                        declarationMatcher.group(3), declarationMatcher.group(5));
            }
        }
    }
}

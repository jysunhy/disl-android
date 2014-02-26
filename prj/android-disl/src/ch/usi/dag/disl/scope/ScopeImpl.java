package ch.usi.dag.disl.scope;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.objectweb.asm.Type;

import ch.usi.dag.disl.exception.ScopeParserException;
import ch.usi.dag.disl.util.Constants;

/**
 * <p>
 * Filters methods based on class name, method name, method parameters and
 * return type.
 *
 *
 * <p>
 * Name of the filtered method is specified as follows:
 *
 * <p>
 * <b>returnparam classname.methodname(parameters)</b>
 *
 *
 * <p>
 * <b>wildcards</b> Some filter patterns might be completely or partly
 * substituted with "*" wild card that might be expanded to none or unspecified
 * number of non-white character.
 *
 *
 * <p>
 * <b>methodname</b> is a mandatory part of the pattern specified. The method
 * name might be partly or completely replaced using "*". To filter in class
 * initializer or class instance constructor specify respectively "clinit" or
 * "init".
 *
 * <p>
 * Examples:
 * <ul>
 *
 * <li>*</li>
 * <ul>
 * matches:
 * <li>all methods</li>
 * </ul>
 *
 * <li>*init</li>
 * <ul>
 * matches:
 * <li>class initializer - "clinit"</li>
 * <li>class constructor - "init"</li>
 * <li>every other method ending with "init"</li>
 * </ul>
 *
 * </ul>
 *
 *
 * <p>
 * <b>returnparam</b> is the returning parameter of the method. It is specified
 * by fully qualified name of the class or pritimive type. If the returnparam is
 * missing, it matches all the return types. Wild cards might substitute pattern
 * partly or completely.
 *
 * <p>
 * Examples:
 * <ul>
 *
 * <li>* or nothing</li>
 * <ul>
 * matches:
 * <li>all return types</li>
 * </ul>
 *
 * <li>*.String</li>
 * <ul>
 * matches:
 * <li>java.lang.String</li>
 * <li>my.package.String</li>
 * <li>every other String class in all packages</li>
 * </ul>
 *
 * <li>*String</li>
 * <ul>
 * matches:
 * <li>java.lang.String</li>
 * <li>my.package.String</li>
 * <li>my.package.BigString</li>
 * <li>every other String class in all packages</li>
 * <li>every other class ending with String in all packages</li>
 * </ul>
 *
 * </ul>
 *
 *
 * <p>
 * <b>classname</b> is the fully qualified name of the class where the filtered
 * method resides. Classname is not required. If it's not specified all classes
 * match. Packagename might be omitted and in such case all packages with
 * classname specified do match. To specify a class without any package add
 * "[default]" as packagename. Wild cards might substitute pattern partly or
 * completely.
 *
 * <p>
 * Examples:
 * <ul>
 *
 * <li>* or nothing</li>
 * <ul>
 * matches:
 * <li>all classes</li>
 * </ul>
 *
 * <li>TargetClass</li>
 * <ul>
 * matches:
 * <li>TargetClass in all packages</li>
 * </ul>
 *
 * <li>[default].TargetClass</li>
 * <ul>
 * matches:
 * <li>TargetClass in default package only</li>
 * not matches:
 * <li>my.pkg.TargetClass</li>
 * </ul>
 *
 * <li>TargetClass*</li>
 * <ul>
 * matches:
 * <li>TargetClassFoo in all packages</li>
 * <li>TargetClassBar in all packages</li>
 * <li>every other class starting with TargetClass in all packages</li>
 * </ul>
 *
 * <li>my.pkg.*Math</li>
 * <ul>
 * matches:
 * <li>my.pkg.Math</li>
 * <li>my.pkg.FastMath</li>
 * <li>my.pkg.new.FastMath</li>
 * <li>my.pkg.new.fast.Math</li>
 * <li>every other class ending with Math in my.pkg subpackages</li>
 * </ul>
 *
 * </ul>
 *
 *
 * <p>
 * <b>parameters</b> are specified same as <i>returnparam</i> and are separated
 * by ",". Parameter can be partly or completely substituted with "*". ".." can
 * be supplied instead of last parameter specification and matches all remaining
 * method parameters.
 *
 * <p>
 * Examples:
 * <ul>
 *
 * <li>(..)</li>
 * <ul>
 * matches:
 * <li>()</li>
 * <li>(int)</li>
 * <li>every other</li>
 * </ul>
 *
 * <li>(int, int, ..)</li>
 * <ul>
 * matches:
 * <li>(int, int)</li>
 * <li>(int, int, int)</li>
 * <li>every other starting with two ints</li>
 * </ul>
 *
 * <li>(java.lang.String, java.lang.String[])</li>
 * <ul>
 * matches:
 * <li>(java.lang.String, java.lang.String[])</li>
 * </ul>
 *
 * </ul>
 *
 *
 * <p>
 * Complete examples:
 * <ul>
 *
 * <li>my.pkg.TargetClass.main(java.lang.String[])</li>
 * <ul>
 * matches:
 * <li>exactly this one method</li>
 * </ul>
 *
 * <li>int *</li>
 * <ul>
 * matches:
 * <li>all methods returning integer</li>
 * </ul>
 *
 * <li>*(int, int, int)</li>
 * <ul>
 * matches:
 * <li>all methods accepting three integers</li>
 * </ul>
 *
 * </ul>
 */
public class ScopeImpl implements Scope {

    private final String PARAM_BEGIN      = "(";
    private final String PARAM_END        = ")";
    private final String PARAM_DELIM      = ",";
    private final String METHOD_DELIM     = ".";
    private final String PARAM_MATCH_REST = "..";
    private final String DEFAULT_PKG      = "[default]";
    private final String RETURN_DELIM     = " ";

    private String       classWildCard;
    private String       methodWildCard;
    private String       returnWildCard;
    private List<String> paramsWildCard;

    private int lastWhitespace(final String where) {

        final char[] whereCharArray = where.toCharArray();

        for (int i = whereCharArray.length - 1; i >= 0; --i) {

            if (Character.isWhitespace(whereCharArray[i])) {
                return i;
            }
        }

        return -1;
    }

    // thx -
    // http://stackoverflow.com/questions/4067809/how-to-check-space-in-string
    private boolean containsWhiteSpace(final String toCheck) {

        for (final char c : toCheck.toCharArray()) {
            if (Character.isWhitespace(c)) {
                return true;
            }
        }

        return false;
    }

    public ScopeImpl(final String scopeExpression) throws ScopeParserException {

        // -- parse the scope into parts - trim whitespace everywhere --

        // parse it from the end
        // its better because you can easier identify return type
        // otherwise you don't know if the first empty space doesn't mean
        // something else

        String restOfExpr = scopeExpression;

        // -- method parameters --
        final int paramBegin = restOfExpr.lastIndexOf(PARAM_BEGIN);
        if (paramBegin != -1) {

            // + 1 - don't include PARAM_BEGIN
            String paramsStr = restOfExpr.substring(paramBegin + 1);
            restOfExpr = restOfExpr.substring(0, paramBegin);

            // remove whitespace
            paramsStr = paramsStr.trim();

            // PARAM_END check
            if (!paramsStr.endsWith(PARAM_END)) {
                throw new ScopeParserException("Scope \"" + scopeExpression
                        + "\" should end with \"" + PARAM_END + "\"");
            }

            // remove PARAM_END
            final int paramEnd = paramsStr.lastIndexOf(PARAM_END);
            paramsStr = paramsStr.substring(0, paramEnd);

            paramsWildCard = new LinkedList<String>();

            // test for emptiness
            if (!paramsStr.trim().isEmpty()) {

                // separate params and trim them again
                final String[] params = paramsStr.split(PARAM_DELIM);
                for (String param : Arrays.asList(params)) {

                    param = param.trim();

                    if (param.isEmpty()) {
                        throw new ScopeParserException("Scope \""
                                + scopeExpression
                                + "\" has bad parameter definition");
                    }

                    paramsWildCard.add(param);
                }
            }

            final int pmrIndex = paramsWildCard.indexOf(PARAM_MATCH_REST);

            // if the index is valid, the first occurrence of PARAM_MATCH_REST
            // should be at the end of the parameters
            if (pmrIndex != -1 && pmrIndex != paramsWildCard.size() - 1) {
                throw new ScopeParserException("Scope \""
                        + scopeExpression
                        + "\" should have \"" + PARAM_MATCH_REST + "\""
                        + " only as last parameter");
            }
        }

        // see ScopeTest bugs for reference
        // split returnExpr first cause
        // after parsing parameters and trimming split around white space
        // this should work
        String returnExpr = "";
        if (restOfExpr.split(RETURN_DELIM).length == 2) {
            returnExpr = restOfExpr.split(RETURN_DELIM, 2)[0];
            restOfExpr = restOfExpr.split(RETURN_DELIM, 2)[1];
        }

        // -- method name --
        final int methodDelim = restOfExpr.lastIndexOf(METHOD_DELIM);
        if (methodDelim != -1) {
            // + 1 - don't include METHOD_DELIM
            methodWildCard = restOfExpr.substring(methodDelim + 1);
            restOfExpr = restOfExpr.substring(0, methodDelim);
        }
        else {
            methodWildCard = restOfExpr;
            restOfExpr = null;
        }

        // remove whitespace
        methodWildCard = methodWildCard.trim();

        if (methodWildCard.isEmpty()) {
            throw new ScopeParserException("Scope \"" + scopeExpression
                    + "\" should have defined method at least as \"*\"");
        }

        // -- full class name --
        if (restOfExpr != null) {

            // remove whitespace
            restOfExpr = restOfExpr.trim();

            if (!restOfExpr.isEmpty()) {

                final int classDelim = lastWhitespace(restOfExpr);
                if (classDelim != -1) {
                    // + 1 - don't include whitespace
                    classWildCard = restOfExpr.substring(classDelim + 1);
                    restOfExpr = restOfExpr.substring(0, classDelim);
                }
                else {
                    classWildCard = restOfExpr;
                    restOfExpr = null;
                }

                // if there is no package specified - allow any
                if (classWildCard.indexOf(Constants.PACKAGE_STD_DELIM) == -1
                        && !classWildCard.startsWith(WildCard.WILDCARD_STR)) {
                    classWildCard = WildCard.WILDCARD_STR +
                            Constants.PACKAGE_STD_DELIM + classWildCard;
                }
            }
        }

        // -- method return type --

        restOfExpr = returnExpr;
        // remove whitespace for next parsing
        if (restOfExpr != null) {

            restOfExpr = restOfExpr.trim();

            if (!restOfExpr.isEmpty()) {

                // no whitespace in restOfExpr
                if (containsWhiteSpace(restOfExpr)) {
                    throw new ScopeParserException("Cannot parse scope \""
                            + scopeExpression + "\"");
                }

                returnWildCard = restOfExpr;
            }
        }
    }

    @Override
    public boolean matches(String className, final String methodName,
            final String methodDesc) {
        // write(className, methodName, methodDesc);

        // -- match class (with package) --

        // replace delimiters for matching
        className = className.replace(
                Constants.PACKAGE_INTERN_DELIM, Constants.PACKAGE_STD_DELIM);

        // if className has default package (nothing), add our default package
        // reasons:
        // 1) we can restrict scope on default package by putting our default
        // package into scope
        // 2) default package would not be matched if no package was specified
        // in the scope (because of substitution made)
        if (className.indexOf(Constants.PACKAGE_STD_DELIM) == -1) {
            className = DEFAULT_PKG + Constants.PACKAGE_STD_DELIM + className;
        }

        if (classWildCard != null
                && !WildCard.match(className, classWildCard)) {
            return false;
        }

        // -- match method name --

        if (methodWildCard != null
                && !WildCard.match(methodName, methodWildCard)) {
            return false;
        }

        // -- match parameters --

        if (paramsWildCard != null) {

            // get parameters and match one by one
            final Type[] parameters = Type.getArgumentTypes(methodDesc);

            // get last param
            String lastParamWC = null;
            if (!paramsWildCard.isEmpty()) {
                lastParamWC = paramsWildCard.get(paramsWildCard.size() - 1);
            }

            // if the last param is not PARAM_MATCH_REST then test for equal
            // size
            if (!PARAM_MATCH_REST.equals(lastParamWC) &&
                    parameters.length != paramsWildCard.size()) {
                return false;
            }

            // not enough parameters
            if (PARAM_MATCH_REST.equals(lastParamWC) &&
                    parameters.length < paramsWildCard.size() - 1) {
                return false;
            }

            for (int i = 0; i < parameters.length; ++i) {

                final String paramWC = paramsWildCard.get(i);

                // if there is PARAM_MATCH_REST then stop
                // works even if there is no additional parameter
                if (paramWC.equals(PARAM_MATCH_REST)) {
                    break;
                }

                final String typeName = parameters[i].getClassName();

                if (!WildCard.match(typeName, paramWC)) {
                    return false;
                }
            }
        }

        // -- match return type --

        if (returnWildCard != null) {
            final Type returnType = Type.getReturnType(methodDesc);
            final String typeName = returnType.getClassName();

            if (!WildCard.match(typeName, returnWildCard)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        String p = "";

        if (paramsWildCard != null) {
            p += "(";
            for (final String s : paramsWildCard) {
                p += s + ", ";
            }
            if (p.length() > 1) {
                p = p.substring(0, p.length() - 2);
            }
            p += ")";
        }

        return String.format("r=%s c=%s m=%s p=%s", returnWildCard, classWildCard, methodWildCard, p);
    }
}

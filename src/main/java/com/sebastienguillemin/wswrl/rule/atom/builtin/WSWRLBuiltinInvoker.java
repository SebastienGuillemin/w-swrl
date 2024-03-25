package com.sebastienguillemin.wswrl.rule.atom.builtin;

import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.List;

import org.swrlapi.builtins.SWRLBuiltInLibrary;

import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLLiteralBuiltInVariable;
import com.sebastienguillemin.wswrl.exception.BuiltInInvocationException;

class WSWRLBuiltinInvoker {
    private final static Hashtable<String, SWRLBuiltInLibrary> builtinLibrairies;

    static {
        builtinLibrairies = new Hashtable<>();

        builtinLibrairies.put("swrlb", new org.swrlapi.builtins.swrlb.SWRLBuiltInLibraryImpl());
    }

    public static boolean invoke(String builtinPrefixedName, List<WSWRLLiteralBuiltInVariable> arguments) throws BuiltInInvocationException {
        String[] builtinNameParts = builtinPrefixedName.split(":");
        if (builtinNameParts.length != 2)
            throw new BuiltInInvocationException(builtinPrefixedName, "Missing prefix or builtIn name.");

        SWRLBuiltInLibrary builtInLibrary = builtinLibrairies.get(builtinNameParts[0]);
        if (builtInLibrary == null)
            throw new BuiltInInvocationException(builtinPrefixedName,
                    "No librairy found for prefix : " + builtinNameParts[0]);

        try {
            System.out.println("Invoking : '" + builtinNameParts[1] + "' with arguments " + arguments);
            Method method = builtInLibrary.getClass().getMethod(builtinNameParts[1], List.class);
            return (boolean) method.invoke(builtInLibrary, arguments);
        } catch (NoSuchMethodException e) {
            throw new BuiltInInvocationException(builtinPrefixedName,
                    "Method " + builtinNameParts[1] + "does not exists.");
        } catch (Exception e) {
            throw new BuiltInInvocationException(builtinPrefixedName, e.getCause().getMessage());
        }
    }
}

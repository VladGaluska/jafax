package org.dxworks.dxplatform.plugins.insider.utils;

public class ImportUtils {

    public static final String IMPORT_SUFFIX = "([\\.;])([a-zA-Z_0-9]*\\.)*([a-zA-Z_0-9]*|\\*)*(;){0,1}";

    public static String wrapImportPackage(String _import) {
        return "(" + _import.replace(".", "\\.") + ")" + IMPORT_SUFFIX;
    }

    public static String unwrapImportPackage(String wrappedImport) {
        int index = wrappedImport.indexOf(IMPORT_SUFFIX);
        if (index == -1)
            return wrappedImport;

        String _import = wrappedImport.substring(1, index - 1);
        _import = _import.replace("\\", "");

        return _import;
    }
}

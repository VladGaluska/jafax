package org.dxworks.dxplatform.plugins.insider.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ImportUtilsTest {

    @Test
    public void fingerprintsWrappedAsImports() {
        String expected = "(org\\.dxworks\\.dxplatform)([\\.;])([a-zA-Z_0-9]*\\.)*([a-zA-Z_0-9]*|\\*)*(;){0,1}";
        String actual = ImportUtils.wrapImportPackage("org.dxworks.dxplatform");

        assertEquals(expected, actual);
    }

    @Test
    public void testUnwrapImport() {
        String expected = "org.dxworks.dxplatform";
        String actual = ImportUtils.unwrapImportPackage("(org\\.dxworks\\.dxplatform)([\\.;])([a-zA-Z_0-9]*\\.)*([a-zA-Z_0-9]*|\\*)*(;){0,1}");

        assertEquals(expected, actual);
    }
}
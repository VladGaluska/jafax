package org.dxworks.dxplatform.plugins.insider.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LibraryImport {
    private String _import;
    private String library;

    public String[] toStringArray() {
        return new String[]{_import, library};
    }
}
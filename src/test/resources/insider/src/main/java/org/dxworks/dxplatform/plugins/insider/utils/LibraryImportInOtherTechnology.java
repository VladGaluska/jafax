package org.dxworks.dxplatform.plugins.insider.utils;

import lombok.Data;
import lombok.ToString;

import java.util.Objects;

@Data
@ToString(callSuper = true)
public class LibraryImportInOtherTechnology extends LibraryImport {
    private String otherTechnology;

    public LibraryImportInOtherTechnology(String _import, String library, String otherTechnology) {
        super(_import, library);
        this.otherTechnology = otherTechnology;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LibraryImportInOtherTechnology that = (LibraryImportInOtherTechnology) o;
        return Objects.equals(get_import(), that.get_import());
    }

    @Override
    public int hashCode() {
        return Objects.hash(get_import());
    }
}
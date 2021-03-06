package org.dxworks.dxplatform.plugins.insider.library.detector.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PackagingUnit {

    @EqualsAndHashCode.Include
    protected String name;

    protected int frequency;
    protected int unitNumber;

    public PackagingUnit(String name, int frequency) {
        this.name = name;
        this.frequency = frequency;
        this.unitNumber = 1;
    }

    public void increment(int frequency) {
        this.frequency += frequency;
        unitNumber++;
    }
}

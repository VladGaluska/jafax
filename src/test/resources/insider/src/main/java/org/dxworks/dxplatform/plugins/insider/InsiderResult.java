package org.dxworks.dxplatform.plugins.insider;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class InsiderResult {
    private String name;
    private String category;
    private String file;
    private Integer value;
}

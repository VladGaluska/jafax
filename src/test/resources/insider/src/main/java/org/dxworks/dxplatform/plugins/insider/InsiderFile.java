package org.dxworks.dxplatform.plugins.insider;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@Data
public class InsiderFile {

    private String name;
    private String path;
    private String extension;

    private String content;

    private List<Integer> lineBreaks;

    private int lines;

    public InsiderFile() {
        extractLineBreaks(content);
        lines = lineBreaks.size();
    }

    public String getLine(Integer index) {
        if (index == lines - 1)
            return (content.substring(lineBreaks.get(index) + 1));
        return content.substring(lineBreaks.get(index) + 1, lineBreaks.get(index + 1) + 1);
    }

    private void extractLineBreaks(String content) {
        lineBreaks = new ArrayList<>();
        lineBreaks.add(-1);
        for (int i = 0; i < content.length(); i++) {
            if (content.charAt(i) == '\n') {
                lineBreaks.add(i);
            }
        }
    }
}

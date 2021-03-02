package utils.filefinder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JavaFiles {

    public List<String> javaFiles = new ArrayList<>();

    public List<String> jarFiles = new ArrayList<>();

}

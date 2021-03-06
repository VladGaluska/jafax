package org.dxworks.dxplatform.plugins.insider.technology.finder.model.xml.old;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "technology")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class TechnologyXmlDTO {
    private String name;

    @XmlElement(name = "category")
    private List<CategoryXmlDTO> categories;
}

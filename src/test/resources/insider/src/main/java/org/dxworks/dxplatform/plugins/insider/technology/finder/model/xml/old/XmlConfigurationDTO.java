package org.dxworks.dxplatform.plugins.insider.technology.finder.model.xml.old;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "config")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class XmlConfigurationDTO {
    private String name;
    private int version;

    private ConfigurationDataXmlDTO data;
}

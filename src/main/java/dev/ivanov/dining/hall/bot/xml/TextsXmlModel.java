package dev.ivanov.dining.hall.bot.xml;

import java.util.List;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.XmlValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(
  name = "texts",
  namespace = "http://dev.ivanov/dining-hall-bot"
)
public class TextsXmlModel {

  @XmlElement(name = "text", namespace = "http://dev.ivanov/dining-hall-bot")
  private List<TextEntryXmlModel> entries;


  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(name = "", namespace = "http://dev.ivanov/dining-hall-bot")
  public static class TextEntryXmlModel {
    @XmlAttribute(name = "key", required = true)
    private String key;

    @XmlValue
    private String value;
  }
}

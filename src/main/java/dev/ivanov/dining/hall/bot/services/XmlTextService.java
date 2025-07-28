package dev.ivanov.dining.hall.bot.services;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import dev.ivanov.dining.hall.bot.xml.TextsXmlModel;
import jakarta.annotation.PostConstruct;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class XmlTextService implements TextService {

  Logger logger = LoggerFactory.getLogger(XmlTextService.class);

  private final ResourceLoader resourceLoader;

  @Getter
  private Map<String, String> texts = new HashMap<>();

  @PostConstruct
  public void init() throws IOException, JAXBException {
    logger.trace("init()");
    Resource resource = resourceLoader.getResource("classpath:texts.xml");
    JAXBContext jaxbContext = JAXBContext.newInstance(TextsXmlModel.class);
    Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
    TextsXmlModel textsXmlModel = (TextsXmlModel) unmarshaller
        .unmarshal(resource.getInputStream());
    if (textsXmlModel != null && textsXmlModel.getEntries() != null) {
      textsXmlModel.getEntries().forEach(entry -> {
        texts.put(entry.getKey(), entry.getValue().trim());
      });
    }
    logger.info("texts.xml loaded: {} texts found", texts.size());
  }

  @Override
  public String getText(String key) {
    logger.trace("getText({})", key);
    return texts.get(key);
  }
}

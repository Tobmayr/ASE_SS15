package com.smartwg.core.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.tagcloud.DefaultTagCloudItem;
import org.primefaces.model.tagcloud.DefaultTagCloudModel;
import org.primefaces.model.tagcloud.TagCloudModel;
import org.springframework.context.annotation.Scope;

import com.smartwg.core.util.Constants;

@Named("about")
@Scope("view")
public class AboutBean {

  private List<String> images;
  private TagCloudModel model;

  
  private ResourceBundle ms = SmartWG.getMessageBundle();

  @PostConstruct
  public void init() {
    images = new ArrayList<String>();
    for (int i = 1; i <= 16; i++) {
      images.add("pic" + i + ".png");
    }

    model = new DefaultTagCloudModel();

    for (int i = 1; i <= 17; i++) {
      String s = "aboutF" + i;
      int x = new Random().nextInt((5 - 1) + 1) + 1;
      model.addTag(new DefaultTagCloudItem(ms.getString(s), "#", x));
    }
  }

  public List<String> getImages() {
    return images;
  }

  public TagCloudModel getModel() {
    return model;
  }
}

package com.smartwg.core.internal.domain.dtos;

import com.smartwg.core.internal.domain.entities.Resource;

/**
 * @author Tobias Ortmayr (to)
 */
public class ResourceDTO {

  private Integer id;
  private String name;
  private String type;
  private byte[] content;
  private String fileURL;


  public ResourceDTO() {};

  public ResourceDTO(final Integer id, final String name, final String type) {
    this.id = id;
    this.name = name;
    this.type = type;
  }


  public ResourceDTO(final Resource resource) {
    this.id = resource.getId();
    this.name = resource.getName();
    this.type = resource.getType();
    this.content = resource.getContent();
    this.fileURL = resource.getFileUrl();
  }

  public byte[] getContent() {
    return content;
  }

  public void setContent(byte[] content) {
    this.content = content;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public String getSimpleType() {
    if (type.equals("application/pdf")) {
      return "PDF";
    } else {
      return type.substring(6, type.length());
    }

  }

  public void setType(String type) {
    this.type = type;
  }

  public String getFileURL() {
    return fileURL;
  }

  public void setFileURL(String fileURL) {
    this.fileURL = fileURL;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }



}

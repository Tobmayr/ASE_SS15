package com.smartwg.core.internal.domain.entities;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Tobias Ortmayr (to)
 */
@Entity
@Table(name = "resource")
public class Resource implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String type;

  @Basic(fetch = FetchType.LAZY)
  @Column(name = "content", length = 16777215)
  private byte[] content;

  @Column(name = "file_url")
  private String fileUrl;

  /**
   * default constructor for hibernate
   */
  public Resource() {}

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
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

  public void setType(String type) {
    this.type = type;
  }

  public byte[] getContent() {
    return content;
  }

  public void setContent(byte[] content) {
    this.content = content;
  }

  public String getFileUrl() {
    return fileUrl;
  }

  public void setFileUrl(String fileUrl) {
    this.fileUrl = fileUrl;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Resource)) {
      return false;
    }

    Resource resource = (Resource) o;

    return new EqualsBuilder().append(id, resource.id).append(name, resource.name)
        .append(type, resource.type).append(content, resource.content)
        .append(fileUrl, resource.fileUrl).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).append(id).append(name).append(type).append(content)
        .append(fileUrl).toHashCode();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("id", id).append("name", name).append("type", type)
        .append("content", content).append("fileUrl", fileUrl).toString();
  }
}

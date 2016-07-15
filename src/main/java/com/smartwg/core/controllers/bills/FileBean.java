package com.smartwg.core.controllers.bills;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.context.annotation.Scope;

import com.smartwg.core.facades.ResourceFacade;
import com.smartwg.core.internal.domain.dtos.ResourceDTO;

@Named("file")
@Scope("singleton")
public class FileBean {

  @Inject
  private ResourceFacade facade;
  // private StreamedContent file;
  private static final int REFERENCE_DIMENSION = 250;

  private int width;
  private int height;

  public StreamedContent getFile() throws IOException {
    FacesContext context = FacesContext.getCurrentInstance();

    if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
      // So, we're rendering the view. Return a stub StreamedContent so that it will generate right
      // URL.
      return new DefaultStreamedContent();
    } else {
      // So, browser is requesting the image. Return a real StreamedContent with the image bytes.
      String id = context.getExternalContext().getRequestParameterMap().get("id");

      ResourceDTO file = facade.findById(Integer.valueOf(id));
      setScaledDimensions(file.getContent());

      return new DefaultStreamedContent(new ByteArrayInputStream(file.getContent()), file.getType());
    }
  }



  private void setScaledDimensions(byte[] content) throws IOException {
    ByteArrayInputStream in = new ByteArrayInputStream(content);
    BufferedImage image = ImageIO.read(in);

    double ratWidth = (double) (image.getWidth() / REFERENCE_DIMENSION);
    double ratHeight = (double) (image.getHeight() / REFERENCE_DIMENSION);
    if (ratHeight <= 0 || ratWidth <= 0) {
      height = image.getHeight();
      width = image.getWidth();
    } else {
      if (ratWidth > ratHeight) {
        height = (int) (image.getHeight() / ratWidth);
        width = (int) (image.getWidth() / ratWidth);
      } else {
        height = (int) (image.getHeight() / ratHeight);
        width = (int) (image.getWidth() / ratHeight);
      }

    }
    in.close();
  }

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }


}

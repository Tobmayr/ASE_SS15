package com.smartwg.core.controllers;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.joda.time.DateTime;
import org.springframework.context.annotation.Scope;

import com.googlecode.wickedcharts.highcharts.options.Axis;
import com.googlecode.wickedcharts.highcharts.options.AxisType;
import com.googlecode.wickedcharts.highcharts.options.ChartOptions;
import com.googlecode.wickedcharts.highcharts.options.DataLabels;
import com.googlecode.wickedcharts.highcharts.options.Marker;
import com.googlecode.wickedcharts.highcharts.options.Options;
import com.googlecode.wickedcharts.highcharts.options.PlotLine;
import com.googlecode.wickedcharts.highcharts.options.PlotOptions;
import com.googlecode.wickedcharts.highcharts.options.PlotOptionsChoice;
import com.googlecode.wickedcharts.highcharts.options.SeriesType;
import com.googlecode.wickedcharts.highcharts.options.Stacking;
import com.googlecode.wickedcharts.highcharts.options.Symbol;
import com.googlecode.wickedcharts.highcharts.options.Title;
import com.googlecode.wickedcharts.highcharts.options.ZoomType;
import com.googlecode.wickedcharts.highcharts.options.Symbol.PredefinedSymbol;
import com.googlecode.wickedcharts.highcharts.options.color.HexColor;
import com.googlecode.wickedcharts.highcharts.options.series.Coordinate;
import com.googlecode.wickedcharts.highcharts.options.series.CustomCoordinatesSeries;
import com.googlecode.wickedcharts.highcharts.theme.GridTheme;
import com.googlecode.wickedcharts.highcharts.theme.Theme;
import com.smartwg.core.controllers.user.UserBean;
import com.smartwg.core.facades.ActivityFacade;
import com.smartwg.core.facades.BillFacade;
import com.smartwg.core.facades.PaymentFacade;
import com.smartwg.core.internal.domain.dtos.ActivityDTO;
import com.smartwg.core.internal.domain.dtos.BillDTO;
import com.smartwg.core.internal.domain.dtos.PaymentDTO;
import com.smartwg.core.internal.domain.dtos.PaymentUserDTO;
import com.smartwg.core.util.Constants;
import com.smartwg.core.util.PrimefacesUtil;

/**
 * @author Tobias Ortmayr (to) , Oezde Simsek (os)
 */
@Named("dashboard")
@Scope("view")
public class DashboardBean {
  @Inject
  private ActivityFacade facade;

  @Inject
  private PaymentFacade paymentFacade;

  @Inject
  private UserBean userBean;

  @Inject
  private BillFacade billFacade;


  private List<ActivityDTO> activities;
  private List<BillDTO> bills;
  private Options options;
  private Theme theme;

  private ResourceBundle valMs = SmartWG.getValidationBundle();
  
  private ResourceBundle ms = SmartWG.getMessageBundle();

  @PostConstruct
  public void init() {
    final Map<String, String> requestParameterMap = PrimefacesUtil.getRequestParameterMap();
    String msg = requestParameterMap.get("msg");
    String group = requestParameterMap.get("group");
    if (msg != null) {
      String message = valMs.getString(msg);
      if (group != null) {
        message = message.replace(":group", group);
      }
      PrimefacesUtil.addValidationMessage(FacesMessage.SEVERITY_INFO, message);
    }

    DateTime dEnd = DateTime.now();
    Date start = new DateTime(dEnd.getYear(), dEnd.getMonthOfYear(), 1, 0, 0, 0, 0).toDate();
    Date end = new DateTime(dEnd.getYear(), dEnd.getMonthOfYear() + 1, 1, 0, 0, 0, 0).toDate();

    activities =
        facade.getAssignedActivitesBetweenTimespan(start, end, userBean.getCurrentUserGroup()
            .getGroupId(), userBean.getCurrentUserGroup().getUserId());

    List<PaymentUserDTO> unconfirmedPayments =
        paymentFacade.findUnconfirmedPaymentUsersByUserId(userBean.getCurrentUser().getId());
    if (unconfirmedPayments != null) {
      for (PaymentUserDTO paymentUserDTO : unconfirmedPayments) {
        PaymentDTO paymentDTO = paymentFacade.findById(paymentUserDTO.getPaymentId());
        if (paymentDTO != null) {
          Calendar cal = Calendar.getInstance();
          cal.setTime(paymentDTO.getDate());
          int lastDayOfMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH) - 1;
          Date lastDateOfPaymentMonth =
              new DateTime(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, lastDayOfMonth, 0,
                  0, 0, 0).toDate();
          if (DateTime.now().toDate().after(lastDateOfPaymentMonth)) {
            FacesMessage msg1 = new FacesMessage();
            msg1.setSummary(valMs.getString("paymentReminderSummary"));
            msg1.setDetail(valMs.getString("paymentReminderDetail"));
            msg1.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage(null, msg1);
            break;
          }
        }
      }
    }

    makeChart(start, end);
    theme = new GridTheme();
    theme.addColor(new HexColor("#7cb5ec"));
    theme.addColor(new HexColor("#f7a35c"));
    theme.addColor(new HexColor("#90ee7e"));
    theme.addColor(new HexColor("#7798BF"));
    theme.addColor(new HexColor("#aaeeee"));
    theme.addColor(new HexColor("#ff0066"));
    theme.addColor(new HexColor("#eeaaee"));
    theme.addColor(new HexColor("#55BF3B"));
    theme.addColor(new HexColor("#DF5353"));
    theme.addColor(new HexColor("#7798BF"));
    theme.addColor(new HexColor("#aaeeee"));
  }

  public void setxAxisDatetime() {
    Axis xAxis = new Axis();
    xAxis.setType(AxisType.DATETIME);
    xAxis.setTitle(new Title(ms.getString("time")));
    xAxis.setMaxPadding(new Float(0.01));
    xAxis.setMinPadding(new Float(0.01));
    options.setxAxis(xAxis);
  }

  public void makeLineChart(String title) {

    options.setChartOptions(new ChartOptions().setType(SeriesType.LINE).setZoomType(ZoomType.X));
    options.setTitle(new Title(title));

  }

  public void setyAxis(String title) {
    PlotLine plotLines = new PlotLine();
    plotLines.setValue(0f);
    plotLines.setWidth(1);
    plotLines.setColor(new HexColor("#999999"));

    Axis yAxis = new Axis();
    yAxis.setTitle(new Title(title));
    yAxis.setPlotLines(Collections.singletonList(plotLines));
    options.setyAxis(yAxis);
  }

  public void setPlotOptions() {
    options.setPlotOptions(new PlotOptionsChoice().setColumn(new PlotOptions().setStacking(
        Stacking.NORMAL).setDataLabels(
        new DataLabels().setEnabled(Boolean.TRUE).setColor(new HexColor("#FFFFFF")))));
  }

  public void makeChart(Date start, Date end) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy, MM, dd");;
    options = new Options();
    options.setSubtitle(new Title(ms.getString("subTitle")));
    setyAxis(ms.getString("totalPrice") + " ("
        + userBean.getCurrentGroup().getCountry().getCurrency().getIsoCode() + ")");
    makeLineChart(ms.getString("statCur"));
    setxAxisDatetime();
    setBills(billFacade.findByParameters(userBean.getCurrentUserGroup().getGroupId(), -1, start,
        end, -1));
    options.addSeries(makeBillsSeries(simpleDateFormat, ms.getString("totalPrice")));
  }

  public CustomCoordinatesSeries<String, Float> makeBillsSeries(SimpleDateFormat sdf,
      String seriesName) {
    List<Coordinate<String, Float>> seriesData = new ArrayList<Coordinate<String, Float>>();
    for (BillDTO b : getBills()) {
      String date = sdf.format(b.getDate());

      String year = date.substring(0, 4);
      if (date.length() == 12) {

        String month = date.substring(6, 8);
        String day = date.substring(10);

        date = year + ", " + (Integer.parseInt(month) - 1) + ", " + day;
      } else if (date.length() == 8) {
        String month = date.substring(6, 8);
        date = year + ", " + (Integer.parseInt(month) - 1);
      }


      boolean added = false;
      int i = 0;
      String coordinateString = "Date.UTC(" + date + ")";
      while (i < seriesData.size() && !added) {
        Coordinate<String, Float> c = seriesData.get(i);
        if (c.getX().equals(coordinateString)) {
          float old = c.getY();
          seriesData.remove(c);
          seriesData.add(new Coordinate<String, Float>(coordinateString, old
              + b.getTotal().floatValue()));
          added = true;

        }
        i++;
      }
      if (!added) {
        seriesData.add(new Coordinate<String, Float>(coordinateString, b.getTotal().floatValue()));

      }
    }
    Marker marker = new Marker();
    marker.setEnabled(Boolean.TRUE);
    marker.setColor(new HexColor("#666666"));
    marker.setRadius(4);
    marker.setLineWidth(1);
    marker.setSymbol(new Symbol(PredefinedSymbol.SQUARE));

    CustomCoordinatesSeries<String, Float> series = new CustomCoordinatesSeries<String, Float>();
    series.setName(seriesName);
    series.setData(seriesData);
    series.setMarker(marker);

    return series;
  }

  public List<ActivityDTO> getActivities() {
    return activities;
  }

  public void setActivities(List<ActivityDTO> activities) {
    this.activities = activities;
  }

  public Options getOptions() {
    return options;
  }

  public void setOptions(Options options) {
    this.options = options;
  }

  public List<BillDTO> getBills() {
    return bills;
  }

  public void setBills(List<BillDTO> bills) {
    this.bills = bills;
  }

  public Theme getTheme() {
    return theme;
  }

  public void setTheme(Theme theme) {
    this.theme = theme;
  }



}

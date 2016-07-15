package com.smartwg.core.controllers.statistics;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

import com.googlecode.wickedcharts.highcharts.options.Axis;
import com.googlecode.wickedcharts.highcharts.options.AxisType;
import com.googlecode.wickedcharts.highcharts.options.ChartOptions;
import com.googlecode.wickedcharts.highcharts.options.DataLabels;
import com.googlecode.wickedcharts.highcharts.options.Function;
import com.googlecode.wickedcharts.highcharts.options.HorizontalAlignment;
import com.googlecode.wickedcharts.highcharts.options.Legend;
import com.googlecode.wickedcharts.highcharts.options.LegendLayout;
import com.googlecode.wickedcharts.highcharts.options.Marker;
import com.googlecode.wickedcharts.highcharts.options.Options;
import com.googlecode.wickedcharts.highcharts.options.PlotLine;
import com.googlecode.wickedcharts.highcharts.options.PlotOptions;
import com.googlecode.wickedcharts.highcharts.options.PlotOptionsChoice;
import com.googlecode.wickedcharts.highcharts.options.SeriesType;
import com.googlecode.wickedcharts.highcharts.options.Stacking;
import com.googlecode.wickedcharts.highcharts.options.Symbol;
import com.googlecode.wickedcharts.highcharts.options.Symbol.PredefinedSymbol;
import com.googlecode.wickedcharts.highcharts.options.Title;
import com.googlecode.wickedcharts.highcharts.options.Tooltip;
import com.googlecode.wickedcharts.highcharts.options.VerticalAlignment;
import com.googlecode.wickedcharts.highcharts.options.ZoomType;
import com.googlecode.wickedcharts.highcharts.options.color.HexColor;
import com.googlecode.wickedcharts.highcharts.options.functions.PercentageAndValueFormatter;
import com.googlecode.wickedcharts.highcharts.options.series.Coordinate;
import com.googlecode.wickedcharts.highcharts.options.series.CustomCoordinatesSeries;
import com.googlecode.wickedcharts.highcharts.theme.GridTheme;
import com.googlecode.wickedcharts.highcharts.theme.Theme;
import com.smartwg.core.controllers.NavigationBean;
import com.smartwg.core.controllers.SmartWG;
import com.smartwg.core.controllers.user.UserBean;
import com.smartwg.core.facades.ActivityFacade;
import com.smartwg.core.facades.BillFacade;
import com.smartwg.core.facades.CategoryFacade;
import com.smartwg.core.facades.ShopFacade;
import com.smartwg.core.internal.domain.dtos.ActivityDTO;
import com.smartwg.core.internal.domain.dtos.BillDTO;
import com.smartwg.core.internal.domain.dtos.CategoryDTO;
import com.smartwg.core.internal.domain.dtos.CostEntryDTO;
import com.smartwg.core.internal.domain.dtos.ShopDTO;
import com.smartwg.core.internal.domain.dtos.UserDTO;
import com.smartwg.core.util.Constants;

/**
 * 
 * @author ozdesimsek (os)
 *
 */

@Named("statistics")
@Scope("session")
public class StatisticsBean {

  private static final Logger logger = LoggerFactory.getLogger(StatisticsBean.class);
  private ResourceBundle ms = SmartWG.getMessageBundle();

  @Inject
  private CategoryFacade categoryFacade;
  @Inject
  private UserBean userBean;
  @Inject
  private ShopFacade shopFacade;
  @Inject
  private ActivityFacade activityFacade;
  @Inject
  private BillFacade billFacade;
  @Inject
  private NavigationBean navigationBean;

  private Date from, to;
  private boolean monthly, daily, yearly, weekly;
  private String show, custom, group, isDone, yAxisActivity;
  private CategoryDTO category;
  private List<CategoryDTO> categories;
  private ShopDTO shop;
  private List<ShopDTO> shops;
  private ActivityDTO activity;
  private List<ActivityDTO> activities;
  private List<BillDTO> bills;
  private Theme theme;
  private Integer current_group_id;
  private List<CostEntryDTO> costEntries;
  private List<UserDTO> assignees, users;
  private UserDTO assignee, user;
  private boolean showChart;

  private Options options;

  @PostConstruct
  public void init() {
    reset();
    current_group_id = userBean.getCurrentUserGroup().getGroupId();
    categories = categoryFacade.findByGroup(current_group_id);
    shops =
        shopFacade
            .findByGroup(current_group_id, userBean.getCurrentUserGroup().getGroupCountryId());
    activities = activityFacade.getActivitiesForGroup(current_group_id);
    assignees = userBean.getUsersOfCurrentGroup();
    users = userBean.getUsersOfCurrentGroup();

    options = new Options();
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

  public String reset() {
    custom = null;
    from = null;
    to = null;
    shop = null;
    category = null;
    activity = null;
    show = null;
    assignee = null;
    user = null;
    activities = activityFacade.getActivitiesForGroup(current_group_id);
    users = userBean.getUsersOfCurrentGroup();
    assignees = userBean.getUsersOfCurrentGroup();
    categories = categoryFacade.findByGroup(current_group_id);
    shops =
        shopFacade
            .findByGroup(current_group_id, userBean.getCurrentUserGroup().getGroupCountryId());
    group = null;
    showChart = false;
    return navigationBean.getPageStatistics() + Constants.PAGE_REDIRECT;
  }

  /**
   * sets the X-Axis with DateTime to options
   */
  public void setxAxisDatetime() {
    Axis xAxis = new Axis();
    xAxis.setType(AxisType.DATETIME);
    xAxis.setTitle(new Title(ms.getString("time")));
    xAxis.setGridLineWidth(1);
    xAxis.setMaxPadding(new Float(0.2));
    xAxis.setMinPadding(new Float(0.2));
    options.setxAxis(xAxis);
  }

  /**
   * sets the X-Axis linear to options with given title
   * 
   * @param title title for x axis
   */
  public void setxAxisLinear(String title) {
    Axis xAxis = new Axis();
    xAxis.setType(AxisType.LINEAR);
    xAxis.setTitle(new Title(title));
    xAxis.setGridLineWidth(1);
    xAxis.setMaxPadding(new Float(0.2));
    xAxis.setMinPadding(new Float(0.2));
    options.setxAxis(xAxis);
  }

  /**
   * sets X-Axis linear to options with given title for Weekly view
   * 
   * @param title title for x axis
   */
  public void setxAxisLinearWeek(String title) {
    Axis xAxis = new Axis();
    xAxis.setTitle(new Title(title));
    xAxis.setGridLineWidth(1);
    xAxis.setMaxPadding(new Float(0.2));
    xAxis.setMinPadding(new Float(0.2));
    options.setxAxis(xAxis);
  }

  /**
   * creates a column chart with given title
   * 
   * @param title title for the chart
   */
  public void makeColumnChart(String title) {
    options.setChartOptions(new ChartOptions().setType(SeriesType.COLUMN).setZoomType(ZoomType.X)
        .setSpacingRight(20).setHeight(540));
    options.setTitle(new Title(title));
    setPlotOptions();
    setLegend();

  }

  /**
   * creates a column chart with given title for percentage view
   * 
   * @param title title for the chart
   */
  public void makeColumnPercentageChart(String title) {
    options.setChartOptions(new ChartOptions().setType(SeriesType.COLUMN).setZoomType(ZoomType.X)
        .setSpacingRight(20).setHeight(540));
    options.setTitle(new Title(title));
    options.setTooltip(new Tooltip().setFormatter(new PercentageAndValueFormatter()));

    options.setPlotOptions(new PlotOptionsChoice().setColumn(new PlotOptions()
        .setStacking(Stacking.PERCENT)));
    setLegend();

  }

  /**
   * creates a line chart with given title
   * 
   * @param title title for the chart
   */
  public void makeLineChart(String title) {

    options.setChartOptions(new ChartOptions().setType(SeriesType.LINE).setZoomType(ZoomType.X)
        .setSpacingRight(20).setHeight(540));
    options.setTitle(new Title(title));
    setLegend();
    setPlotOptions();
  }

  /**
   * sets the y-axis with given title
   * 
   * @param title title for y-axis
   */
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

  /**
   * sets the legend for the options
   */
  public void setLegend() {
    options.setLegend(new Legend().setLayout(LegendLayout.VERTICAL)
        .setAlign(HorizontalAlignment.RIGHT).setVerticalAlign(VerticalAlignment.TOP).setX(-10)
        .setY(20).setBorderWidth(0));
  }

  /**
   * sets the plot options for the options
   */
  public void setPlotOptions() {
    options.setPlotOptions(new PlotOptionsChoice().setColumn(new PlotOptions().setStacking(
        Stacking.NORMAL).setDataLabels(
        new DataLabels().setEnabled(Boolean.TRUE).setColor(new HexColor("#FFFFFF")))));
  }

  /**
   * creates the chart for the view
   */
  public void makeChart() {
    SimpleDateFormat simpleDateFormat;
    options = new Options();
    options.setSubtitle(new Title(ms.getString("subTitle")));

    if (custom.equals("Bill")) { // ====================BILL==============================

      setyAxis("Total Price" + " ("
          + userBean.getCurrentGroup().getCountry().getCurrency().getIsoCode() + ")");
      int userId = -1;
      if (user != null)
        userId = user.getId();
      int shopId = -1;
      if (shop != null)
        shopId = shop.getId();


      if (group.equals("percent")) {
        makeColumnPercentageChart(ms.getString("userPercent"));
      } else if (user != null) {
        makeColumnChart(ms.getString("statFromUser") + " " + user.getUserName());
      } else {
        if (!show.equals("yearly") && !show.equals("monthly") && !show.equals("weekly")) {
          makeLineChart(ms.getString("stats"));
        } else {
          makeColumnChart(ms.getString("stats"));
        }

      }
      if (show.equals("monthly")) { // ==========================================MONTH

        simpleDateFormat = new SimpleDateFormat("yyyy, MM");
        setxAxisDatetime();
      } else if (show.equals("yearly")) { // =====================================YEAR

        simpleDateFormat = new SimpleDateFormat("yyyy");
        setxAxisLinear(ms.getString("years"));

      } else if (show.equals("weekly")) { // =====================================WEEK
        simpleDateFormat = new SimpleDateFormat("yyyy");
        setxAxisLinearWeek(ms.getString("numOfWeeks"));
        if (to == null) {
          setTo(new Date());
        }
        if (from == null) {
          SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
          String dateInString = "01-01-2010 00:00:01";
          Date date;
          try {
            date = sdf.parse(dateInString);
            setFrom(date);
          } catch (ParseException e) {
            logger.error("parse error in makeChart");
          }
        }
        if (group.equals("percent")) {
          for (UserDTO u : users) {
            setBills(billFacade.findByParameters(current_group_id, u.getId(), from, to, shopId));
            for (CustomCoordinatesSeries<String, Float> c : sortBillsWeeklyByYear(ms
                .getString("billsOfUser") + " " + u.getUserName())) {
              if (c != null)
                options.addSeries(c);
            }
          }
          showChart = true;
          return;
        } else if (group.equals("groupShops") && shop == null) {
          List<CustomCoordinatesSeries<String, Float>> seriesList =
              new ArrayList<CustomCoordinatesSeries<String, Float>>();
          for (ShopDTO s : getShops()) {
            shopId = s.getId();
            setBills(billFacade.findByParameters(current_group_id, userId, from, to, shopId));
            for (CustomCoordinatesSeries<String, Float> c : sortBillsWeeklyByYear(s.getName())) {
              seriesList.add(c);
            }
          }
          for (CustomCoordinatesSeries<String, Float> c : seriesList) {
            options.addSeries(c);
          }
        } else {
          setBills(billFacade.findByParameters(current_group_id, userId, from, to, shopId));
          for (CustomCoordinatesSeries<String, Float> c : sortBillsWeeklyByYear(ms
              .getString("sBills"))) {
            options.addSeries(c);
          }
        }

        showChart = true;
        return;

      } else { // =============================================DAILY
        simpleDateFormat = new SimpleDateFormat("yyyy, MM, dd");
        setxAxisDatetime();

      }


      if (group.equals("groupShops") && shop == null) {
        for (ShopDTO s : getShops()) {
          shopId = s.getId();
          setBills(billFacade.findByParameters(current_group_id, userId, from, to, shopId));
          options.addSeries(makeBillsSeries(simpleDateFormat,
              ms.getString("totalPrice") + " " + s.getName()));
        }
      } else if (group.equals("percent")) {
        for (UserDTO u : users) {
          setBills(billFacade.findByParameters(current_group_id, u.getId(), from, to, shopId));
          options.addSeries(makeBillsSeries(simpleDateFormat, ms.getString("totalPriceBillsUser")
              + " " + u.getUserName()));
        }

      } else {
        setBills(billFacade.findByParameters(current_group_id, userId, from, to, shopId));
        options.addSeries(makeBillsSeries(simpleDateFormat, ms.getString("totalPrice")));
      }
      showChart = true;
      return;

    } else if (custom.equals("Category")) { // ====================CATEGORY==============
      if (show.equals("monthly")) {
        simpleDateFormat = new SimpleDateFormat("yyyy, MM");
        setxAxisDatetime();
        makeColumnChart(ms.getString("statCat"));

        makeChartCategory(simpleDateFormat);
        showChart = true;
        return;
      } else if (show.equals("yearly")) {

        simpleDateFormat = new SimpleDateFormat("yyyy");
        setxAxisLinear(ms.getString("years"));
        makeColumnChart(ms.getString("statCat"));
        makeChartCategory(simpleDateFormat);
        showChart = true;
        return;
      } else if (show.equals("weekly")) {
        simpleDateFormat = new SimpleDateFormat("yyyy");
        if (to == null) {
          setTo(new Date());
        }
        if (from == null) {
          SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
          String dateInString = "01-01-2010 00:00:01";
          Date date;
          try {
            date = sdf.parse(dateInString);
            setFrom(date);
          } catch (ParseException e) {
            logger.error("parse error in makeChart");
          }
        }
        setxAxisLinearWeek(ms.getString("numOfWeeks"));
        setyAxis(ms.getString("totalPrice") + " ("
            + userBean.getCurrentGroup().getCountry().getCurrency().getIsoCode() + ")");
        makeColumnChart(ms.getString("statCatWeek"));
        makeChartCategoryWeekly(simpleDateFormat);
        showChart = true;
        return;

      } else {

        simpleDateFormat = new SimpleDateFormat("yyyy, MM, dd");
        setxAxisDatetime();
        makeColumnChart(ms.getString("statCat"));
        makeChartCategory(simpleDateFormat);
        showChart = true;
        return;
      }
    } else if (custom.equals("Activity")) { // ====================ACTIVITY==================
      if (show.equals("monthly")) {

        simpleDateFormat = new SimpleDateFormat("yyyy, MM");
        setxAxisDatetime();
        makeColumnChart(ms.getString("statAct"));
        makeChartActivity(simpleDateFormat);
        showChart = true;
        return;

      } else if (show.equals("yearly")) {

        simpleDateFormat = new SimpleDateFormat("yyyy");
        setxAxisLinear(ms.getString("years"));
        makeColumnChart(ms.getString("statAct"));
        makeChartActivity(simpleDateFormat);
        showChart = true;
        return;
      } else if (show.equals("weekly")) {
        simpleDateFormat = new SimpleDateFormat("yyyy");
        if (to == null) {
          setTo(new Date());
        }
        if (from == null) {
          SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
          String dateInString = "01-01-2010 00:00:01";
          Date date;
          try {
            date = sdf.parse(dateInString);
            setFrom(date);
          } catch (ParseException e) {
            logger.error("parse error in makeChart");
          }
        }
        setxAxisLinearWeek(ms.getString("numOfWeeks"));
        makeColumnChart(ms.getString("statActWeek"));
        makeChartActivityWeekly(simpleDateFormat);
        showChart = true;
        return;

      } else {

        simpleDateFormat = new SimpleDateFormat("yyyy, MM, dd");
        setxAxisDatetime();
        makeColumnChart(ms.getString("statAct"));
        makeChartActivity(simpleDateFormat);
        showChart = true;
        return;
      }
    }

  }

  /**
   * creates the series for bills
   * 
   * @param sdf Simple Date Format for Bill's date
   * @param seriesName name for the series
   * @return custom coordinate series of bills
   */
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
      if (show.equals("yearly")) {
        coordinateString = date;
      }
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

  /**
   * sorts bills weekly with their year
   * 
   * @param name name for the series
   * @return list of series of sorted bills
   */
  public List<CustomCoordinatesSeries<String, Float>> sortBillsWeeklyByYear(String name) {
    List<CustomCoordinatesSeries<String, Float>> list =
        new ArrayList<CustomCoordinatesSeries<String, Float>>();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
    String lastyearStr = sdf.format(getTo());
    int lastyear = new Integer(lastyearStr);

    HashMap<String, List<BillDTO>> map = new HashMap<String, List<BillDTO>>();

    List<BillDTO> inner = new ArrayList<BillDTO>();
    int i = 1;
    int size = getBills().size();
    for (BillDTO b : getBills()) {

      String year = sdf.format(b.getDate());
      if (new Integer(year) == lastyear && i != size) {
        inner.add(b);
      } else if (new Integer(year) != lastyear && i == size) {
        if (inner.size() != 0)
          map.put(lastyear + "", inner);
        inner = new ArrayList<BillDTO>();
        inner.add(b);
        map.put(year, inner);
      } else if (new Integer(year) == lastyear && i == size) {
        inner.add(b);
        map.put(year, inner);
      } else {

        map.put(lastyear + "", inner);
        inner = new ArrayList<BillDTO>();
        while (new Integer(year) != lastyear) {
          lastyear--;
        }
        inner.add(b);


      }
      i++;

    }


    for (String key_year : map.keySet()) {
      List<Coordinate<String, Float>> seriesData = new ArrayList<Coordinate<String, Float>>();
      Calendar cal = Calendar.getInstance();
      for (BillDTO b : map.get(key_year)) {
        cal.setTime(b.getDate());
        int week = cal.get(Calendar.WEEK_OF_YEAR);


        boolean added = false;
        int index = 0;
        String coordinateString = week + "";

        while (index < seriesData.size() && !added) {
          Coordinate<String, Float> c = seriesData.get(index);
          if (c.getX().equals(coordinateString)) {
            float old = c.getY();
            seriesData.remove(c);
            seriesData.add(new Coordinate<String, Float>(coordinateString, old
                + b.getTotal().floatValue()));
            added = true;

          }
          index++;
        }
        if (!added) {
          seriesData
              .add(new Coordinate<String, Float>(coordinateString, b.getTotal().floatValue()));

        }
      }
      Marker marker = new Marker();
      marker.setEnabled(Boolean.TRUE);
      marker.setColor(new HexColor("#666666"));
      marker.setRadius(4);
      marker.setLineWidth(1);
      marker.setSymbol(new Symbol(PredefinedSymbol.SQUARE));

      CustomCoordinatesSeries<String, Float> series = new CustomCoordinatesSeries<String, Float>();
      series.setName(key_year + " " + name);
      series.setData(seriesData);
      series.setMarker(marker);
      list.add(series);
    }
    return list;

  }


  /**
   * creates the chart for categories
   * 
   * @param sdf Simple Date Format for dates
   */
  public void makeChartCategory(SimpleDateFormat sdf) {
    setyAxis(ms.getString("totalPrice") + " ("
        + userBean.getCurrentGroup().getCountry().getCurrency().getIsoCode() + ")");
    setPlotOptions();

    for (CustomCoordinatesSeries<String, Float> c : sortBillsByCategories(sdf)) {
      options.addSeries(c);
    }

  }

  /**
   * creates the chart of categories for weekly view
   * 
   * @param sdf Simple Date Format for dates
   */
  public void makeChartCategoryWeekly(SimpleDateFormat sdf) {
    setyAxis(ms.getString("totalPrice") + " ("
        + userBean.getCurrentGroup().getCountry().getCurrency().getIsoCode() + ")");
    setPlotOptions();

    for (CustomCoordinatesSeries<String, Float> c : sortBillsByCategoriesWeekly(sdf)) {
      options.addSeries(c);
    }

  }

  /**
   * sorts the bills by categories with given date format
   * 
   * @param sdf Simple Date Format for dates
   * @return list of the sorted series
   */
  public List<CustomCoordinatesSeries<String, Float>> sortBillsByCategories(SimpleDateFormat sdf) {
    List<CustomCoordinatesSeries<String, Float>> seriesList =
        new ArrayList<CustomCoordinatesSeries<String, Float>>();
    if (category == null) {
      for (CategoryDTO c : getCategories()) {
        int user_id = -1;
        if (user != null)
          user_id = user.getId();
        int shop_id = -1;
        if (shop != null)
          shop_id = shop.getId();
        setCostEntries(billFacade.findCostEntryByParameters(current_group_id, user_id, from, to,
            c.getId(), shop_id));
        seriesList.add(makeCostEntriesSeries(c.getName(), sdf));
      }
    } else {
      int user_id = -1;
      if (user != null)
        user_id = user.getId();
      int shop_id = -1;
      if (shop != null)
        shop_id = shop.getId();
      setCostEntries(billFacade.findCostEntryByParameters(current_group_id, user_id, from, to,
          category.getId(), shop_id));
      seriesList.add(makeCostEntriesSeries(category.getName(), sdf));
    }
    return seriesList;
  }

  /**
   * sorts the bills by categories with given date format for weekly view
   * 
   * @param sdf Simple Date Format for dates
   * @return list of the sorted series
   */
  public List<CustomCoordinatesSeries<String, Float>> sortBillsByCategoriesWeekly(
      SimpleDateFormat sdf) {
    List<CustomCoordinatesSeries<String, Float>> seriesList =
        new ArrayList<CustomCoordinatesSeries<String, Float>>();
    if (category == null) {
      for (CategoryDTO c : getCategories()) {
        int user_id = -1;
        if (user != null)
          user_id = user.getId();
        int shop_id = -1;
        if (shop != null)
          shop_id = shop.getId();
        setCostEntries(billFacade.findCostEntryByParameters(current_group_id, user_id, from, to,
            c.getId(), shop_id));
        for (CustomCoordinatesSeries<String, Float> ccs : makeCostEntriesSeriesWeekly(c.getName(),
            sdf)) {
          seriesList.add(ccs);
        }
      }
    } else {
      int user_id = -1;
      if (user != null)
        user_id = user.getId();
      int shop_id = -1;
      if (shop != null)
        shop_id = shop.getId();
      setCostEntries(billFacade.findCostEntryByParameters(current_group_id, user_id, from, to,
          category.getId(), shop_id));
      for (CustomCoordinatesSeries<String, Float> ccs : makeCostEntriesSeriesWeekly(
          category.getName(), sdf)) {
        seriesList.add(ccs);
      }
    }
    return seriesList;
  }

  /**
   * creates series of cost entries with given category name and date format
   * 
   * @param category_name name of the category
   * @param sdf simple date format for dates
   * @return series of cost entries
   */
  public CustomCoordinatesSeries<String, Float> makeCostEntriesSeries(String category_name,
      SimpleDateFormat sdf) {
    List<Coordinate<String, Float>> seriesData = new ArrayList<Coordinate<String, Float>>();
    for (CostEntryDTO ce : getCostEntries()) {
      String date = sdf.format(ce.getDate());

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

      if (show.equals("yearly")) {
        coordinateString = date;
      }

      while (i < seriesData.size() && !added) {
        Coordinate<String, Float> c = seriesData.get(i);

        if (c.getX().equals(coordinateString)) {
          float old = c.getY();
          seriesData.remove(c);
          seriesData.add(new Coordinate<String, Float>(coordinateString, old
              + ce.getAmount().floatValue()));
          added = true;

        }
        i++;
      }
      if (!added) {
        seriesData
            .add(new Coordinate<String, Float>(coordinateString, ce.getAmount().floatValue()));
      }
    }
    Marker marker = new Marker();
    marker.setEnabled(Boolean.TRUE);
    marker.setColor(new HexColor("#666666"));
    marker.setRadius(4);
    marker.setLineWidth(1);
    marker.setSymbol(new Symbol(PredefinedSymbol.SQUARE));

    CustomCoordinatesSeries<String, Float> series = new CustomCoordinatesSeries<String, Float>();
    series.setName(category_name);
    series.setData(seriesData);
    series.setMarker(marker);

    return series;
  }

  /**
   * creates series of cost entries with given category name and date format for weekly view
   * 
   * @param category_name name of the category
   * @param sdf simple date format for dates
   * @return series of cost entries
   */
  public List<CustomCoordinatesSeries<String, Float>> makeCostEntriesSeriesWeekly(
      String category_name, SimpleDateFormat sdf) {
    List<CustomCoordinatesSeries<String, Float>> list =
        new ArrayList<CustomCoordinatesSeries<String, Float>>();
    String lastyearStr = sdf.format(getTo());
    int lastyear = new Integer(lastyearStr);

    HashMap<String, List<CostEntryDTO>> map = new HashMap<String, List<CostEntryDTO>>();

    List<CostEntryDTO> inner = new ArrayList<CostEntryDTO>();
    int i = 1;
    int size = getCostEntries().size();
    for (CostEntryDTO ce : getCostEntries()) {

      String year = sdf.format(ce.getDate());
      if (new Integer(year) == lastyear && i != size) {
        inner.add(ce);
      } else if (new Integer(year) != lastyear && i == size) {
        if (inner.size() != 0)
          map.put(lastyear + "", inner);
        inner = new ArrayList<CostEntryDTO>();
        inner.add(ce);
        map.put(year, inner);
      } else if (new Integer(year) == lastyear && i == size) {
        inner.add(ce);
        map.put(year, inner);
      } else {

        map.put(lastyear + "", inner);
        inner = new ArrayList<CostEntryDTO>();
        while (new Integer(year) != lastyear) {
          lastyear--;
        }
        inner.add(ce);
      }
      i++;

    }

    for (String key_year : map.keySet()) {
      List<Coordinate<String, Float>> seriesData = new ArrayList<Coordinate<String, Float>>();
      Calendar cal = Calendar.getInstance();
      for (CostEntryDTO ce : map.get(key_year)) {
        cal.setTime(ce.getDate());
        int week = cal.get(Calendar.WEEK_OF_YEAR);

        boolean added = false;
        int index = 0;
        String coordinateString = week + "";

        while (index < seriesData.size() && !added) {
          Coordinate<String, Float> c = seriesData.get(index);
          if (c.getX().equals(coordinateString)) {
            float old = c.getY();
            seriesData.remove(c);
            seriesData.add(new Coordinate<String, Float>(coordinateString, old
                + ce.getAmount().floatValue()));
            added = true;

          }
          index++;
        }
        if (!added) {
          seriesData.add(new Coordinate<String, Float>(coordinateString, ce.getAmount()
              .floatValue()));

        }
      }
      Marker marker = new Marker();
      marker.setEnabled(Boolean.TRUE);
      marker.setColor(new HexColor("#666666"));
      marker.setRadius(4);
      marker.setLineWidth(1);
      marker.setSymbol(new Symbol(PredefinedSymbol.SQUARE));

      CustomCoordinatesSeries<String, Float> series = new CustomCoordinatesSeries<String, Float>();
      series.setName(key_year + " " + category_name);
      series.setData(seriesData);
      series.setMarker(marker);
      list.add(series);
    }

    return list;

  }

  /**
   * creates chart of activities with given date format
   * 
   * @param sdf simple date format for dates
   */
  public void makeChartActivity(SimpleDateFormat sdf) {
    setPlotOptions();
    setyAxis(ms.getString("ratings"));
    

    options.setTooltip(new Tooltip().setFormatter(new Function(
        "return '<b>'+ this.series.name +'</b><br/>'+Highcharts.dateFormat('%e. %b', this.x)")));

    for (CustomCoordinatesSeries<String, Float> c : sortActivities(sdf)) {
      options.addSeries(c);
    }

  }

  /**
   * creates chart of activities with given date format for weekly view
   * 
   * @param sdf simple date format for dates
   */
  public void makeChartActivityWeekly(SimpleDateFormat sdf) {
    setPlotOptions();
    setyAxis(ms.getString("ratings"));
    

    // options.setTooltip(new Tooltip().setFormatter(new Function(
    // "return '<b>'+ this.series.name +'</b><br/>'+Highcharts.dateFormat('%e. %b', this.x)")));

    for (CustomCoordinatesSeries<String, Float> c : sortActivitiesWeekly(sdf)) {
      options.addSeries(c);
    }

  }

  /**
   * returns a list of series of sorted activities with given date format for weekly view
   * 
   * @param sdf simple date format for dates
   * @return list of series of activities
   */
  public List<CustomCoordinatesSeries<String, Float>> sortActivitiesWeekly(SimpleDateFormat sdf) {

    List<CustomCoordinatesSeries<String, Float>> seriesList =
        new ArrayList<CustomCoordinatesSeries<String, Float>>();
    if (activity == null) {
      if (getAssignee() == null) {
        String userNames = "";
        for (int i = 0; i < getAssignees().size(); i++) {
          if (i == getAssignees().size() - 1) {
            userNames += assignees.get(i).getUserName();
          } else {
            userNames += assignees.get(i).getUserName() + ", ";
          }
        }
        options.setTitle(new Title(ms.getString("statActYU") + " " + userNames));

        for (UserDTO u : getAssignees()) {
          setActivities(activityFacade.findByParameters(current_group_id, u.getId(), from, to,
              isDone, -1));
          List<Date> dates = new ArrayList<Date>();
          for (ActivityDTO act : getActivities()) {
            dates.add(act.getDate());
          }
          for (CustomCoordinatesSeries<String, Float> serie : makeActivitySeriesWeekly(sdf)) {
            seriesList.add(serie);

          }
        }
      } else {

        int assignee_id = -1;
        if (assignee != null)
          assignee_id = assignee.getId();
        setActivities(activityFacade.findByParameters(current_group_id, assignee_id, from, to,
            isDone, -1));

        for (CustomCoordinatesSeries<String, Float> serie : makeActivitySeriesWeekly(sdf)) {
          seriesList.add(serie);
        }
      }

    } else {
      List<ActivityDTO> l = new ArrayList<ActivityDTO>();
      int assignee_id = -1;
      if (assignee != null)
        assignee_id = assignee.getId();
      options
          .setTitle(new Title(ms.getString("statActU") + " " + activity.getAssigentTo_Username()));
      l.addAll(activityFacade.findByParameters(current_group_id, assignee_id, from, to, isDone,
          activity.getId()));
      setActivities(l);
      for (CustomCoordinatesSeries<String, Float> serie : makeActivitySeriesWeekly(sdf)) {
        seriesList.add(serie);
      }
    }
    return seriesList;
  }

  /**
   * returns a list of series of sorted activities with given date format
   * 
   * @param sdf simple date format for dates
   * @return list of series of activities
   */
  public List<CustomCoordinatesSeries<String, Float>> sortActivities(SimpleDateFormat sdf) {

    List<CustomCoordinatesSeries<String, Float>> seriesList =
        new ArrayList<CustomCoordinatesSeries<String, Float>>();
    if (activity == null) {
      if (getAssignee() == null) {
        String userNames = "";
        for (int i = 0; i < getAssignees().size(); i++) {
          if (i == getAssignees().size() - 1) {
            userNames += assignees.get(i).getUserName();
          } else {
            userNames += assignees.get(i).getUserName() + ", ";
          }
        }
        options.setTitle(new Title(ms.getString("statActUG") + " " + userNames));

        for (UserDTO u : getAssignees()) {
          setActivities(activityFacade.findByParameters(current_group_id, u.getId(), from, to,
              isDone, -1));
          List<Date> dates = new ArrayList<Date>();
          for (ActivityDTO act : getActivities()) {
            CustomCoordinatesSeries<String, Float> serie = makeActivitySeries(act, sdf);
            serie.setStack(u.getUserName());
            seriesList.add(serie);

            dates.add(act.getDate());

          }
        }
      } else {

        int assignee_id = -1;
        if (assignee != null)
          assignee_id = assignee.getId();
        setActivities(activityFacade.findByParameters(current_group_id, assignee_id, from, to,
            isDone, -1));

        for (ActivityDTO act : getActivities()) {
          CustomCoordinatesSeries<String, Float> serie = makeActivitySeries(act, sdf);
          seriesList.add(serie);
        }
      }

    } else {
      List<ActivityDTO> l = new ArrayList<ActivityDTO>();
      int assignee_id = -1;
      if (assignee != null)
        assignee_id = assignee.getId();
      options
          .setTitle(new Title(ms.getString("statActU") + " " + activity.getAssigentTo_Username()));
      l.addAll(activityFacade.findByParameters(current_group_id, assignee_id, from, to, isDone,
          activity.getId()));
      setActivities(l);
      for (ActivityDTO act : getActivities()) {
        CustomCoordinatesSeries<String, Float> serie = makeActivitySeries(act, sdf);
        seriesList.add(serie);
      }
    }
    return seriesList;
  }

  /**
   * creates the activity series with given activity object and date format
   * 
   * @param activityDTO activity object
   * @param sdf simple date format for dates
   * @return series of the given activity
   */
  public CustomCoordinatesSeries<String, Float> makeActivitySeries(ActivityDTO activityDTO,
      SimpleDateFormat sdf) {
    List<Coordinate<String, Float>> seriesData = new ArrayList<Coordinate<String, Float>>();

    String date = sdf.format(activityDTO.getDate());
    String year = date.substring(0, 4);
    if (date.length() == 12) {

      String month = date.substring(6, 8);
      String day = date.substring(10);

      date = year + ", " + (Integer.parseInt(month) - 1) + ", " + day;
    } else if (date.length() == 8) {
      String month = date.substring(6, 8);
      date = year + ", " + (Integer.parseInt(month) - 1);
    }

    String coordinateString = "Date.UTC(" + date + ")";

    if (show.equals("yearly")) {
      coordinateString = date;
    }
    seriesData.add(new Coordinate<String, Float>(coordinateString, new Integer(activityDTO
          .getRating()).floatValue()));
    Marker marker = new Marker();
    marker.setEnabled(Boolean.TRUE);
    marker.setColor(new HexColor("#666666"));
    marker.setRadius(4);
    marker.setLineWidth(1);
    marker.setSymbol(new Symbol(PredefinedSymbol.SQUARE));

    CustomCoordinatesSeries<String, Float> series = new CustomCoordinatesSeries<String, Float>();
    series.setMarker(marker);
    series.setData(seriesData);
    series.setName(activityDTO.getName() + ", " + activityDTO.getAssigentTo_Username());
    return series;
  }

  /**
   * creates the series of activities with the given activity object, date format, coordinate and
   * year for weekly view
   * 
   * @param act activity object
   * @param sdf simple date format
   * @param coordinate_string coordinate string for coordinate
   * @param year year of the activity
   * @return series of the given activity
   */
  public CustomCoordinatesSeries<String, Float> makeActivitySeriesWeeklyWithAct(ActivityDTO act,
      SimpleDateFormat sdf, String coordinate_string, String year) {
    List<Coordinate<String, Float>> seriesData = new ArrayList<Coordinate<String, Float>>();


    seriesData.add(new Coordinate<String, Float>(coordinate_string, new Integer(act.getRating())
          .floatValue()));
    

    Marker marker = new Marker();
    marker.setEnabled(Boolean.TRUE);
    marker.setColor(new HexColor("#666666"));
    marker.setRadius(4);
    marker.setLineWidth(1);
    marker.setSymbol(new Symbol(PredefinedSymbol.SQUARE));

    CustomCoordinatesSeries<String, Float> series = new CustomCoordinatesSeries<String, Float>();
    series.setMarker(marker);
    series.setData(seriesData);
    series.setName(act.getName() + ", " + act.getAssigentTo_Username() + ", " + year);
    series.setStack(act.getAssigentTo_Username() + " " + year);
    return series;
  }

  /**
   * returns a list of activity series with given date format for weekly view
   * 
   * @param sdf simple date format for dates
   * @return the list of activity series
   */
  public List<CustomCoordinatesSeries<String, Float>> makeActivitySeriesWeekly(SimpleDateFormat sdf) {
    List<CustomCoordinatesSeries<String, Float>> list =
        new ArrayList<CustomCoordinatesSeries<String, Float>>();
    String lastyearStr = sdf.format(getTo());
    int lastyear = new Integer(lastyearStr);

    HashMap<String, List<ActivityDTO>> map = new HashMap<String, List<ActivityDTO>>();

    List<ActivityDTO> inner = new ArrayList<ActivityDTO>();
    int i = 1;
    int size = getActivities().size();
    for (ActivityDTO ce : getActivities()) {

      String year = sdf.format(ce.getDate());
      if (new Integer(year) == lastyear && i != size) {
        inner.add(ce);
      } else if (new Integer(year) != lastyear && i == size) {
        if (inner.size() != 0)
          map.put(lastyear + "", inner);
        inner = new ArrayList<ActivityDTO>();
        inner.add(ce);
        map.put(year, inner);
      } else if (new Integer(year) == lastyear && i == size) {
        inner.add(ce);
        map.put(year, inner);
      } else {

        map.put(lastyear + "", inner);
        inner = new ArrayList<ActivityDTO>();
        while (new Integer(year) != lastyear) {
          lastyear--;
        }
        inner.add(ce);
      }
      i++;

    }

    for (String key_year : map.keySet()) {
      List<CustomCoordinatesSeries<String, Float>> seriesData =
          new ArrayList<CustomCoordinatesSeries<String, Float>>();
      Calendar cal = Calendar.getInstance();
      for (ActivityDTO ce : map.get(key_year)) {
        logger.info(ce.getName());
        cal.setTime(ce.getDate());
        int week = cal.get(Calendar.WEEK_OF_YEAR);

        String coordinateString = week + "";

        CustomCoordinatesSeries<String, Float> ccs =
            makeActivitySeriesWeeklyWithAct(ce, sdf, coordinateString, key_year);

        seriesData.add(ccs);
      }
      list.addAll(seriesData);
    }

    return list;
  }

  public Date getTo() {
    return to;
  }

  public void setTo(Date to) {
    this.to = to;
  }

  public Date getFrom() {
    return from;
  }

  public void setFrom(Date from) {
    this.from = from;
  }

  public boolean isYearly() {
    return yearly;
  }

  public void setYearly(boolean yearly) {
    this.yearly = yearly;
  }

  public boolean isMonthly() {
    return monthly;
  }

  public void setMonthly(boolean monthly) {
    this.monthly = monthly;
  }

  public boolean isDaily() {
    return daily;
  }

  public void setDaily(boolean daily) {
    this.daily = daily;
  }

  public boolean isWeekly() {
    return weekly;
  }

  public void setWeekly(boolean weekly) {
    this.weekly = weekly;
  }

  public String getShow() {
    return show;
  }

  public void setShow(String show) {
    this.show = show;
  }

  public CategoryDTO getCategory() {
    return category;
  }

  public void setCategory(CategoryDTO category) {
    this.category = category;
  }

  public List<CategoryDTO> getCategories() {
    return categories;
  }

  public void setCategories(List<CategoryDTO> categories) {
    this.categories = categories;
  }

  public String getCustom() {
    return custom;
  }

  public void setCustom(String custom) {
    this.custom = custom;
  }

  public ShopDTO getShop() {
    return shop;
  }

  public void setShop(ShopDTO shop) {
    this.shop = shop;
  }

  public List<ShopDTO> getShops() {
    return shops;
  }

  public void setShops(List<ShopDTO> shops) {
    this.shops = shops;
  }

  public ActivityDTO getActivity() {
    return activity;
  }

  public void setActivity(ActivityDTO activity) {
    this.activity = activity;
  }

  public List<ActivityDTO> getActivities() {
    return activities;
  }

  public void setActivities(List<ActivityDTO> activities) {
    this.activities = activities;
  }

  public List<BillDTO> getBills() {
    return bills;
  }

  public void setBills(List<BillDTO> bills) {
    this.bills = bills;
  }

  public Options getOptions() {
    return options;
  }

  public void setOptions(Options options) {
    this.options = options;
  }

  public Theme getTheme() {
    return theme;
  }

  public void setTheme(Theme theme) {
    this.theme = theme;
  }

  public Integer getCurrent_group_id() {
    return current_group_id;
  }

  public void setCurrent_group_id(Integer current_group_id) {
    this.current_group_id = current_group_id;
  }

  public List<CostEntryDTO> getCostEntries() {
    return costEntries;
  }

  public void setCostEntries(List<CostEntryDTO> costEntries) {
    this.costEntries = costEntries;
  }

  public List<UserDTO> getAssignees() {
    return assignees;
  }

  public void setAssignees(List<UserDTO> assignees) {
    this.assignees = assignees;
  }

  public UserDTO getAssignee() {
    return assignee;
  }

  public void setAssignee(UserDTO assignee) {
    this.assignee = assignee;
  }

  public List<UserDTO> getUsers() {
    return users;
  }

  public void setUsers(List<UserDTO> users) {
    this.users = users;
  }

  public UserDTO getUser() {
    return user;
  }

  public void setUser(UserDTO user) {
    this.user = user;
  }

  public boolean isShowChart() {
    return showChart;
  }

  public void setShowChart(boolean showChart) {
    this.showChart = showChart;
  }

  public String getGroup() {
    return group;
  }

  public void setGroup(String group) {
    this.group = group;
  }

  public String getIsDone() {
    return isDone;
  }

  public void setIsDone(String isDone) {
    this.isDone = isDone;
  }

  public String getyAxisActivity() {
    return yAxisActivity;
  }

  public void setyAxisActivity(String yAxisActivity) {
    this.yAxisActivity = yAxisActivity;
  }

}

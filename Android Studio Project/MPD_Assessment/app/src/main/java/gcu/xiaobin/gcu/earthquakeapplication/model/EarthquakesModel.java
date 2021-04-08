package gcu.xiaobin.gcu.earthquakeapplication.model;

import java.io.Serializable;

/**
 * @author Xiaobin_Ma
 * @student_id S1803078
 * @project EarthQuakeApplication
 * @package_name gcu.xiaobin.gcu.earthquakeapplication.model
 * @date 06/04/2021
 * @time 08:15
 * @year 2021
 * @month 04
 * @month_short Apr
 * @month_full April
 * @day 06
 * @day_short Tue
 * @day_full Tuesday
 * @hour 08
 * @minute 15
 */
public class EarthquakesModel implements Serializable {

    private String title = "";
    //private DescriptionModel description = new DescriptionModel();
    private String description = "";
    private String link = "";
    private String pubDate = "";
    private String category = "";
    private String geoLat = "";
    private String getLong = "";

    public String getTitle() {
        return title;
    }

    public EarthquakesModel setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public EarthquakesModel setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getLink() {
        return link;
    }

    public EarthquakesModel setLink(String link) {
        this.link = link;
        return this;
    }

    public String getPubDate() {
        return pubDate;
    }

    public EarthquakesModel setPubDate(String pubDate) {
        this.pubDate = pubDate;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public EarthquakesModel setCategory(String category) {
        this.category = category;
        return this;
    }

    public String getGeoLat() {
        return geoLat;
    }

    public EarthquakesModel setGeoLat(String geoLat) {
        this.geoLat = geoLat;
        return this;
    }

    public String getGetLong() {
        return getLong;
    }

    public EarthquakesModel setGetLong(String getLong) {
        this.getLong = getLong;
        return this;
    }
}
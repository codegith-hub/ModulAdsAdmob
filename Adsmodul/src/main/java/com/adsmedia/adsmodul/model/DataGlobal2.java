package com.adsmedia.adsmodul.model;

public class DataGlobal2 {
    public int id;
    public String title;
    public String category;
    public String description;
    public String link_image;
    public String link_mp3;
    public String link_download1;
    public String link_download2;
    public String link_webview;
    public String short_description;

    public String getShort_description() {
        return short_description;
    }

    public void setShort_description(String short_description) {
        this.short_description = short_description;
    }

    public String getLink_webview() {
        return link_webview;
    }

    public void setLink_webview(String link_webview) {
        this.link_webview = link_webview;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink_image() {
        return link_image;
    }

    public void setLink_image(String link_image) {
        this.link_image = link_image;
    }

    public String getLink_mp3() {
        return link_mp3;
    }

    public void setLink_mp3(String link_mp3) {
        this.link_mp3 = link_mp3;
    }

    public String getLink_download1() {
        return link_download1;
    }

    public void setLink_download1(String link_download1) {
        this.link_download1 = link_download1;
    }

    public void setLink_download2(String link_download2) {
        this.link_download2 = link_download2;
    }

    public String getLink_download2() {
        return link_download2;
    }

    public String getCategory() {
        return category;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DataGlobal2 other = (DataGlobal2) obj;
        return id == other.id;
    }
}

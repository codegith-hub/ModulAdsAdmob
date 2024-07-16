package com.adsmedia.adsmodul.model;

public class DataGlobal {
    public int id;
    public String title;
    public String category;
    public String description;
    public String link_image;
    public String link_mp3;
    public String link_download1;
    public String link_download2;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public String getLink_image() {
        return link_image;
    }

    public String getLink_mp3() {
        return link_mp3;
    }

    public String getLink_download1() {
        return link_download1;
    }

    public void setLink_download2(String link_download2) {
        this.link_download2 = link_download2;
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
        DataGlobal other = (DataGlobal) obj;
        return id == other.id;
    }
}

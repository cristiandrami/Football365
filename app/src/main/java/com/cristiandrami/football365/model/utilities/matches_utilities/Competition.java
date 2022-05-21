package com.cristiandrami.football365.model.utilities.matches_utilities;

public class Competition {

    private String id;
    private String name;
    private String geographicalArea;
    private String imageUrl;


    public Competition(){
        /** This is the empty constructor of the class */
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGeographicalArea() {
        return geographicalArea;
    }

    public void setGeographicalArea(String geographicalArea) {
        this.geographicalArea = geographicalArea;
    }
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    @Override
    public String toString() {
        return name + " - "+geographicalArea;
    }
}

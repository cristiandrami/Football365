package com.cristiandrami.football365.ui.news;

import java.util.Objects;

public class NewsRecyclerViewItemModel {
    //see the recycler_view_item layout
    //this is for the image


    private String image;

    //this is for the main string title
    private String title;

    //this is for the description
    private String description;

    private String link;

    int type;


    public NewsRecyclerViewItemModel(){
        /***
         * this is the empty constructor
         */
    }


    public NewsRecyclerViewItemModel(String image, String title, String descriptionRecyclerViewItem) {
        this.image = image;
        this.title = title;
        this.description = descriptionRecyclerViewItem;

    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return "NewsRecyclerViewItemModel{" +
                "newsImage='" + image + '\'' +
                ", newsTitle='" + title + '\'' +
                ", newsDescription='" + description + '\'' +
                ", articleLink='" + link + '\'' +
                '}';
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NewsRecyclerViewItemModel)) return false;
        NewsRecyclerViewItemModel that = (NewsRecyclerViewItemModel) o;
        return Objects.equals(getTitle(), that.getTitle());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle());
    }
}

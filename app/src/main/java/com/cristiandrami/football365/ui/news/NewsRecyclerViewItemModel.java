package com.cristiandrami.football365.ui.news;

public class NewsRecyclerViewItemModel {
    //see the recycler_view_item layout
    //this is for the image


    private String newsImage;

    //this is for the main string title
    private String newsTitle;

    //this is for the description
    private String newsDescription;

    private String articleLink;




    public NewsRecyclerViewItemModel(String newsImage, String newsTitle, String descriptionRecyclerViewItem) {
        this.newsImage = newsImage;
        this.newsTitle = newsTitle;
        this.newsDescription = descriptionRecyclerViewItem;

    }

    public String getNewsImage() {
        return newsImage;
    }

    public void setNewsImage(String newsImage) {
        this.newsImage = newsImage;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getNewsDescription() {
        return newsDescription;
    }

    public void setNewsDescription(String newsDescription) {
        this.newsDescription = newsDescription;
    }

    public String getArticleLink() {
        return articleLink;
    }

    public void setArticleLink(String articleLink) {
        this.articleLink = articleLink;
    }
}

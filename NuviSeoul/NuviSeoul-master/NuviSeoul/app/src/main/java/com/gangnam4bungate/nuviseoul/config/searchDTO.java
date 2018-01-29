package com.gangnam4bungate.nuviseoul.config;

/**
 * Created by shj89 on 2017-09-16.
 */

public class searchDTO {
    public String contentId;
    public String title;
    public String firstImage;
    public String mapX;
    public String mapY;

    public searchDTO(String contentId, String title, String firstImage, String mapX, String mapY) {
        this.contentId = contentId;
        this.title = title;
        this.firstImage = firstImage;
        this.mapX = mapX;
        this.mapY = mapY;
    }
}

package com.gangnam4bungate.nuviseoul.config;

/**
 * Created by shj89 on 2017-10-20.
 */

public class searchDetailDTO {
    public String homePage;
    public String overView;

    public searchDetailDTO(String overView, String homePage) {
        this.homePage = homePage;
        this.overView = overView;
    }
}

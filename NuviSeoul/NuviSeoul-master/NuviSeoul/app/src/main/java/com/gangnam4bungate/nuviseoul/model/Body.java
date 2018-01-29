package com.gangnam4bungate.nuviseoul.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wsseo on 2017. 7. 6..
 */

public class Body {
    @SerializedName("items")
    Item mitems;
    public Item getItems(){
        return this.mitems;
    }
}

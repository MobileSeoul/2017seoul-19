package com.gangnam4bungate.nuviseoul.model.detail;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wsseo on 2017. 7. 6..
 */

public class DetailBody {
    @SerializedName("items")
    DetailItem mitems;
    public DetailItem getItems(){
        return this.mitems;
    }
}

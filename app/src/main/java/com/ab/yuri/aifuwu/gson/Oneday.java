package com.ab.yuri.aifuwu.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Yuri on 2017/2/11.
 */

public class Oneday {

    /**
     * id : 432
     * hitokoto : XX什么的，最讨厌了！
     * type : f
     * from : 网络
     * creator : kira0769
     * cearted_at : null
     */

    @SerializedName("hitokoto")
    public String text;
    public String from;
}

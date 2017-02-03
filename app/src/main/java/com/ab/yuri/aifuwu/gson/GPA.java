package com.ab.yuri.aifuwu.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Yuri on 2017/1/19.
 */

public class GPA {

    /**
     * gpa : 3.59
     * detail : [{"学年":"2014-2015","学期":"1","课程代码":"B3500011S","课程名称":"大学生心理健康","课程性质":"必修","课程归属":" ","学分":"0.5","绩点":"3.70","成绩":"87","辅修标记":"0","补考成绩":" ","重修成绩":" ","学院名称":"教育科学与技术学院","备注":" ","重修标记":"0","课程英文名称":""},"..."]
     */

    public String gpa;

    public List<DetailBean> detail;

    public static class DetailBean {
        /**
         * 学年 : 2014-2015
         * 学期 : 1
         * 课程代码 : B3500011S
         * 课程名称 : 大学生心理健康
         * 课程性质 : 必修
         * 课程归属 :
         * 学分 : 0.5
         * 绩点 : 3.70
         * 成绩 : 87
         * 辅修标记 : 0
         * 补考成绩 :
         * 重修成绩 :
         * 学院名称 : 教育科学与技术学院
         * 备注 :
         * 重修标记 : 0
         * 课程英文名称 :
         */

        @SerializedName("学年")
        public String year;
        @SerializedName("学期")
        public String term;
        @SerializedName("课程代码")
        public String lessonCode;
        @SerializedName("课程名称")
        public String lessonName;
        @SerializedName("学分")
        public String lessonCredit;
        @SerializedName("绩点")
        public String lessonGPA;
        @SerializedName("成绩")
        public String lessonScore;
    }



}

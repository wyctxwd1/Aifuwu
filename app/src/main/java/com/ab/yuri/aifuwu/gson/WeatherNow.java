package com.ab.yuri.aifuwu.gson;

import java.util.List;

/**
 * Created by Yuri on 2017/1/26.
 */

public class WeatherNow {


        /**
         * basic : {"city":"南京","cnty":"中国","id":"CN101190101","lat":"32.048000","lon":"118.769000","update":{"loc":"2017-01-26 18:57","utc":"2017-01-26 10:57"}}
         * now : {"cond":{"code":"101","txt":"多云"},"fl":"8","hum":"52","pcpn":"0","pres":"1024","tmp":"12","vis":"5","wind":{"deg":"160","dir":"南风","sc":"3-4","spd":"10"}}
         * status : ok
         */

        public BasicBean basic;
        public NowBean now;
        public String status;

        public static class BasicBean {
            /**
             * city : 南京
             * cnty : 中国
             * id : CN101190101
             * lat : 32.048000
             * lon : 118.769000
             * update : {"loc":"2017-01-26 18:57","utc":"2017-01-26 10:57"}
             */


            public String id;

            public UpdateBean update;

            public static class UpdateBean {
                /**
                 * loc : 2017-01-26 18:57
                 * utc : 2017-01-26 10:57
                 */

                public String loc;

            }
        }

        public static class NowBean {
            /**
             * cond : {"code":"101","txt":"多云"}
             * fl : 8
             * hum : 52
             * pcpn : 0
             * pres : 1024
             * tmp : 12
             * vis : 5
             * wind : {"deg":"160","dir":"南风","sc":"3-4","spd":"10"}
             */

            public CondBean cond;

            public String tmp;


            public static class CondBean {
                /**
                 * code : 101
                 * txt : 多云
                 */

                public String txt;
            }

        }
    }

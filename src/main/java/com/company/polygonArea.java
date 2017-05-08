package com.company;

import java.awt.*;

/**
 * Created by zhou on 17-5-5.
 */
public class polygonArea {

    public static double GetArea(Polygon ls) {
        if (ls.npoints < 4)
            return 0;

        double sum = 0;

        for (int i = 0; i < ls.npoints - 1; ++i) {
            Point p = new Point(ls.xpoints[i], ls.ypoints[i]);
            Point q = new Point(ls.xpoints[i + 1], ls.ypoints[i + 1]);
            sum += (p.x + q.x) * (q.y - p.y);
        }

        return sum / 2;

    }

//    double GetPrjArea(const vector &ls)
//    {
//        if (ls.size() < 4)
//            return 0;
//
//        double dArea = GetArea(ls);
//        dArea = abs(dArea);
//        if (dArea == 0)
//            return 0;
//
//        double xmin, ymin, xmax, ymax;
//        xmin = xmax = ls[0].x;
//        ymax = ymin = ls[0].y;
//
//        for (size_t i=1; i<ls.size(); ++i)
//        {
//    const Coordinate& p = ls[i];
//            xmin = min(xmin, p.x);
//            ymin = min(ymin, p.y);
//            xmax = max(xmax, p.x);
//            ymax = max(ymax, p.y);
//        }
//
//        Coordinate p1, p2;
//        p1.x = xmin;
//        p1.y = (ymin+ymax)/2;
//        p2.x = xmax;
//        p2.y = (ymin+ymax)/2;
//        double dx = GetPrjDistance(p1, p2);
//
//        p1.x = p2.x = xmin;
//        p1.y = ymin;
//        p2.y = ymax;
//        double dy = GetPrjDistance(p1, p2);
//        dy *= dx;
//
//        dx = (xmax-xmin)*(ymax-ymin);
//        dy /= dx;
//        dArea *= dy;
//        return dArea;
//
//    }
}

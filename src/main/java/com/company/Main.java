package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    final static String newPlace = "/home/zhou/target";
    final static String oldPlace = "/home/zhou/location";
    public static void main(String[] args) {
	// write your code here
        省级行政区 fi = new 省级行政区();
        市级行政区 se = new 市级行政区(fi.get省级());
        县级行政区 th = new 县级行政区(se.get市级());
////        nameChange nc = new nameChange(se.get市级(),th.get县级());
        fileConveter.Conveter(se.get市级(),true);
        fileConveter.Conveter(th.get县级(),false);
//
//        //nameChange.creatN("/home/zhou/location/GPS/GCJ/admin2locality/","/home/zhou/target/output/",se.get市级());
        json2xml.createmm(fi.get省级名字(),se.get市级名字(),th.get县级名字());
        json3xml.createmm(fi.get省级名字(),se.get市级名字(),th.get县级名字());
//        Scanner sc = new Scanner(System.in);
//        String s = sc.nextLine();
//        polyConverter.pvert(s);
//        while(s!=null)
//        {
//            System.out.println(polyConverter.Kvert(s));
//            s = sc.nextLine();
//        }
    }
}

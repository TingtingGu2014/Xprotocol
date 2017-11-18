/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xprotocol.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author zhao0677
 */
public class HtmlParser {
    
    public static Elements getElementsByTagName(Document doc, String tagName){
        Elements elements = null;
        try{
            elements = doc.select(tagName);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return elements;
    }
    
    public static Elements getElementsByTagNameFromHtmlString(String html, String tagName){
        Elements elements = null;
        try{
            Document doc = Jsoup.parse(html);
            elements = doc.select(tagName);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return elements;
    }
    
    public static String getImgLinksFromHtmlString(String html){
        
        String json = null;
        List<String> srcList = new ArrayList<>();
        Elements imgs = getElementsByTagNameFromHtmlString(html, "img");
        if(null != imgs && imgs.size() > 0){
            for(Element image : imgs){
                String src = image.attr("src");
                if(!UtilsStringHelper.isEmptyString(src)){
                    srcList.add(src);
                }
            }
        }
        String[] srcArray = srcList.toArray(new String[srcList.size()]);
        json = Arrays.toString(srcArray);
        return json;
    }
}

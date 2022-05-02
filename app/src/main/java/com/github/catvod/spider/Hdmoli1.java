package com.github.catvod.spider;

import com.github.catvod.crawler.SpiderDebug;
import com.github.catvod.utils.okhttp.OKCallBack;
import com.github.catvod.utils.okhttp.OkHttpUtil;
import okhttp3.Call;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.seimicrawler.xpath.antlr.XpathParser;

import java.util.HashMap;
import java.util.List;

public class Hdmoli1 extends XPathFilter{
    @Override
    public String playerContent(String flag, String id, List<String> vipFlags) {
        try {
                JSONObject headers = new JSONObject();
                headers.put("Host", " parse.hdmoli.net:18226");
                headers.put("origin", " https://www.hdmoli.com");
                // headers.put("Accept", " */*");
                //  headers.put("User-Agent", " Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.114 Safari/537.36");

                // headers.put("Accept-Language", " zh-CN,zh;q=0.9,en-US;q=0.3,en;q=0.7");
                // headers.put("Accept-Encoding", " gzip, deflate, br");
                // 播放页 url
                String url = "https://www.hdmoli.com"+ "/play/" + id + ".html";
                Document doc = Jsoup.parse(OkHttpUtil.string(url, getHeaders(url)));
                /*取得script下面的JS变量*/
                Elements e = doc.select("div>script");
                /*循环遍历script下面的JS变量*/
                String player = "";
                String player1 = "";
                String zimu = "";
                JSONObject result = new JSONObject();
                for (Element element : e) {
                    /*取得JS变量数组*/
                    String[] data = element.data().toString().split("var");
                    /*取得单个JS变量*/
                    for (String variable : data) {
                        /*过滤variable为空的数据*/
                        if (variable.contains("=")) {
                            /*取到满足条件的JS变量*/
                            if (variable.contains("now")) {
                                String[] kvp = variable.split("\"");
                                player1 = kvp[1].replaceAll("\"", "").replaceAll(";", "");
                                if (variable.contains("wmkk")){
                                    String ulr2 = player1;
                                     player = ulr2;
                                }
                                if (variable.contains("video")||variable.contains("|")||variable.contains("mp4")) {
                                    if (variable.contains("video")) {
                                        if (variable.contains("|")) {
                                            int start = player1.indexOf("https");
                                            int end = player1.lastIndexOf('|');
                                            String url2 = player1.substring(start, end);
                                            String url3 = url2 + "&type=json";
                                            Document doc2 = Jsoup.parse(OkHttpUtil.string(url3, getHeaders(url)));
                                            String doc3 = doc2.html().trim();
                                            int start2 = doc3.indexOf("http");
                                            int end2 = doc3.lastIndexOf("tg") + 2;
                                            String json2 = doc3.substring(start2, end2).replaceAll("/","");
                                            String json3 = json2;
                                            player = json3;
                                           
                                        }else {
                                            String url2 = player1;
                                            String url3 = url2 + "&type=json";
                                            Document doc2 = Jsoup.parse(OkHttpUtil.string(url3, getHeaders(url)));
                                            String doc3 = doc2.html().trim();
                                            int start2 = doc3.indexOf("http");
                                            int end2 = doc3.lastIndexOf("tg") + 2;
                                            String json2 = doc3.substring(start2, end2).replaceAll("/","");
                                            String json3 = json2;
                                            player = json3;
                                        }
                                    }
                                    if  (variable.contains("mp4")) {
                                        int start = player1.indexOf("https");
                                        int end = player1.lastIndexOf("mp4") +3;
                                        String url2 = player1.substring(start, end);
                                        Document doc2 = Jsoup.parse(OkHttpUtil.string(url2, getHeaders(url)));
                                        String doc3 = doc2.html().trim();
                                        int start2 = doc3.indexOf("https");
                                        int end2 = doc3.lastIndexOf("mp4") + 3;
                                        String json2 = doc3.substring(start2, end2);
                                        player = json2;
                                    }
                                    if(variable.contains("vvt")){
                                        int start1 = player1.indexOf('|')+1;
                                        int end1 = player1.lastIndexOf('t')+1 ;
                                        zimu =player1.substring(start1, end1);
                                    }
                                }


                            }
                        }
                    }
                    String videoUrl = player;
                    result.put("parse", 0);
                    //result.put("playUrl", player);
                    result.put("subf", "/vvt");
                    result.put("subt", zimu);
                    result.put("url", videoUrl);
                    result.put("header",headers.toString());

            }
            return result.toString();
        } catch (Exception e) {
            SpiderDebug.log(e);
        }
        return "";
    }


}

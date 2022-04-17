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

public class Libvio extends XPathFilter{
    @Override
    public String playerContent(String flag, String id, List<String> vipFlags) {
        try {
            JSONObject headers = new JSONObject();
            // headers.put("Host", " parse.hdmoli.net:18226");
            //headers.put("origin", " https://www.hdmoli.com");
            // headers.put("Accept", " */*");
            //  headers.put("User-Agent", " Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.114 Safari/537.36");

            // headers.put("Accept-Language", " zh-CN,zh;q=0.9,en-US;q=0.3,en;q=0.7");
            // headers.put("Accept-Encoding", " gzip, deflate, br");
            // 播放页 url
            String url = "https://www.libvio.com"+ "/play/" + id + ".html";
            Document doc = Jsoup.parse(OkHttpUtil.string(url, getHeaders(url)));
            /*取得script下面的JS变量*/
            Elements e = doc.select("div>script");
            /*循环遍历script下面的JS变量*/
            String player = "";
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
                        if (variable.contains("url")) {
                            String[] kvp = variable.split("\"");
                            player = kvp[1].replaceAll("\"", "").replaceAll(";", "");
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

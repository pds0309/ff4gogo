package com.pds.openapi.api;



import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public final class Fifa4PlayerImgApi {

    private Fifa4PlayerImgApi(){
        //
    }

    public static final String DEFAULT_IMAGE = "https://ff4ggimages.s3.ap-northeast-2.amazonaws.com/unknown.png";

    public static String findPlayerImgUrl(int playerId) {
        try {
            String url = "https://fo4.dn.nexoncdn.co.kr/live/externalAssets/common/playersAction/p" + playerId + ".png";
            URL obj = new URL(url);
            if(hasImage(obj)){
                return url;
            }
            int code = playerId % (playerId / 1000000 * 10000);
            String url2 = "https://fo4.dn.nexoncdn.co.kr/live/externalAssets/common/players/p" + code + ".png";
            return (hasImage(new URL(url2))) ? url2 : DEFAULT_IMAGE;
        } catch (MalformedURLException | ArithmeticException e ) {
            return DEFAULT_IMAGE;
        }
    }
    static boolean hasImage(URL obj){
        try {
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("HEAD");
            if (!con.getHeaderFields().get(null).get(0).contains("403")) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}

package locationcrawler.scnu.com.locationcrawler;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;


public class CrwalerThread extends Thread {

    private double latitude=23.138;
    private double longitude=113.350;
    private Handler mHandler;
    private String requestUrl;
//    private String key ="";
    private int currentPage=1;

    public  CrwalerThread(double latitude, double longitude, Handler mHandler){
        this.latitude = latitude;
        this.longitude = longitude;
        this.mHandler = mHandler;
        try {
            requestUrl = "http://restapi.amap.com/v3/place/around?key="+Constant.KEY
                    +"&location="+String.valueOf(latitude)+","+String.valueOf(longitude)
                    +"&output=json&radius=1000&keywords="+URLEncoder.encode("美食","utf-8")
                    +"&offset=100&page="+currentPage;
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run(){
        String requestResult = generateLocationList();
        if(requestResult.isEmpty()){
            this.mHandler.sendEmptyMessage(Constant.REQUEST_FAIL);

        }else {
            Message msg = new Message();
            msg.what = Constant.REQUEST_OK;
            msg.obj = requestResult;
            this.mHandler.sendMessage(msg);
        }

//        Log.e("network result",result);
    }

    public String sendHttpRequest(){

        try {
            URL url=new URL(requestUrl);
            URLConnection connection =url.openConnection();
            InputStream IS=connection.getInputStream();
            InputStreamReader ISR =new InputStreamReader(IS,"UTF-8");
            BufferedReader BR =new BufferedReader(ISR);

            String line;
            StringBuilder builder=new StringBuilder();
            while((line=BR.readLine())!=null){
                builder.append(line);
            }
            BR.close();
            ISR.close();
            IS.close();
            Log.e("network result",builder.toString());
            return builder.toString();

        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public String generateLocationList(){

//        ArrayList list = new ArrayList();

        String jsonStr = sendHttpRequest();

        try {
            JSONObject jsonObject = new JSONObject(jsonStr);

            JSONArray poisArray = jsonObject.getJSONArray("pois");


            if (poisArray.length()!=0){
                for (int i=0; i<poisArray.length();i++){

                    JSONObject locationBean = poisArray.getJSONObject(i);

                    String name = locationBean.getString("name");
                    String address = locationBean.getString("address");
                    String location = locationBean.getString("location");

                    String writeStr = name+"\n"+address+"\n"+location+"\n\n";

                    String writeFilename = "db_"+this.latitude+"_"+this.longitude+".txt";
                    FileUtil.writeToFile(writeFilename,writeStr);

//                    list.add(bean);

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return "db_"+this.latitude+"_"+this.longitude+".txt";

    }
}

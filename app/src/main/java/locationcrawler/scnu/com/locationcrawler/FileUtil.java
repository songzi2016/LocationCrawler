package locationcrawler.scnu.com.locationcrawler;

/**
 * Created by Administrator on 2019/5/30.
 */

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by mike on 5/5/16.
 */
public class FileUtil {

    private  static String myRoot;
    private static String SDCardRoot;

    static
    {

        // 得到当前外部存储设备的目录
        SDCardRoot = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + File.separator;//在后面加上该系统特有的文件分隔符

        myRoot = SDCardRoot + "LocationCrawler" + File.separator;
        Log.e("file root", "SDCardRoot--------->"+SDCardRoot+"\n"
                +"myRoot------>"+ myRoot);
    }

    public static boolean writeToFile(String filename,String data){


            File dirFile = new File(myRoot);
            if(!dirFile.exists()){ //如果路径不存在，新建路径
                boolean dirResult = dirFile.mkdirs();
                if (dirResult == false){//如果创建新路径失败
                    return false;
                }
            }

            File file = new File(myRoot+filename);

            if(!file.exists())//如果文件不存在，新建文件
            {
                try
                {
                    file.createNewFile();//创建文件之前必须保证相应的目录存在，否则会创建失败
                }
                catch (IOException ioe)
                {
                    return false;
                }
            }

            FileWriter fw = null;
            try
            {
                fw = new FileWriter(file,true);//true表示向该文件里面追加文件
                fw.write(data);
            }
            catch(IOException e)
            {
                return false;
            }
            finally
            {
                if(fw != null)
                {
                    try
                    {
                        fw.close();
                    }
                    catch(IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
            return true;//函数正常执行完，返回true




    }

}

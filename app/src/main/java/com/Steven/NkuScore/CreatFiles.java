package com.Steven.NkuScore;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Steven on 2016/7/29.
 */
public class CreatFiles {

    public boolean isSdCardExist() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    public String getSdCardPath() {
        boolean exist = isSdCardExist();
        String sdpath = "";
        if (exist) {
            sdpath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath();
        } else {
            sdpath = "存储空间不适用";
        }
        return sdpath;
    }

    public void CreatPath() throws IOException {
        File flie = new File (this.getSdCardPath()+"/我开查分");
        if (!flie.exists()){
            try{
                flie.mkdirs();
            }catch (Exception e){
                //TODO: handle exception
                e.printStackTrace();
            }
        }
        File dir = new File(this.getSdCardPath()+"/我开查分"+"/成绩单.html");
        if (!dir.exists()) {
            try {
                dir.createNewFile();
            } catch (Exception e) {
                //TODO: handle exception
                e.printStackTrace();
            }
        }
    }
//    测试存储的用户情况
//    public void testFile(){
//        File test = new File(this.getSdCardPath()+"/我开查分"+"/names.txt");
//        File fileToCopy = new File("/data/data/com.Steven.NkuScore/shared_prefs/com.Steven.NkuScore_preferences.xml");
//        if (!test.exists()){
//            try {
//                test.createNewFile();
//            }catch (IOException e){
//                e.printStackTrace();
//            }
//        }
//        if (!fileToCopy.exists()){
//            Log.d("aaa", "---------");
//        }else {
//            try {
//                FileInputStream fileInputStream = new FileInputStream(fileToCopy);
//                FileOutputStream fileOutputStream = new FileOutputStream(test);
//                byte[] buf = new byte[200*1024];
//                int bytes;
//                while ((bytes = fileInputStream.read(buf,0,buf.length))!=-1){
//                    fileOutputStream.write(buf,0,bytes);
//                    fileOutputStream.flush();
//                }
//
//                fileInputStream.close();
//                fileOutputStream.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}

package com.Steven.NkuLogin;

import android.os.Environment;

import java.io.File;
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
            }
        }
        File dir = new File(this.getSdCardPath()+"/我开查分"+"/成绩单.html");
        if (!dir.exists()){
            try{
                dir.createNewFile();
            }catch (Exception e){
                //TODO: handle exception
            }
        }
    }
}

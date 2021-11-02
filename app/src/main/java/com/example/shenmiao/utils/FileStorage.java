package com.example.shenmiao.utils;

import android.os.Environment;

import java.io.File;
import java.util.UUID;


public class FileStorage {
    private File cropIconDir;
    private File iconDir;

    public FileStorage() {
        //MEDIA_MOUNTED  SD卡正常使用 TRUE TRUE TRUE TRUE TRUE
        //只有在SD卡状态为MEDIA_MOUNTED时/mnt/sdcard目录才是可读可写，并且可以创建目录及文件。
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File external = Environment.getExternalStorageDirectory();//获取sd卡 路径
            String rootDir = "/" + "Base";
            cropIconDir = new File(external, rootDir + "/crop");
            if (!cropIconDir.exists()) { // 检测是否有这个目录存在
                cropIconDir.mkdirs();   //创建

            }
            iconDir = new File(external, rootDir + "/icon");
            if (!iconDir.exists()) {
                iconDir.mkdirs();
            }
        }
    }
    //裁剪存放目录
    public File createCropFile() {
        String fileName = "";
        if (cropIconDir != null) {
            //UUID  设备唯一标识码
            fileName = UUID.randomUUID().toString() + ".png";
        }
        return new File(cropIconDir, fileName);  // cropIconDir: File对象类型的目录路径    fileName:文件名或目录名。
    }

    //头像存放目录
    public File createIconFile() {
        String fileName = "";
        if (iconDir != null) {
            fileName = UUID.randomUUID().toString() + ".png";
        }
        return new File(iconDir, fileName);
    }

}

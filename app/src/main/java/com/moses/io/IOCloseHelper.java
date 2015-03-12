package com.moses.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by 丹 on 2014/12/15.
 */
public class IOCloseHelper {

    /**
     * 考虑到demo中IO异常很多，因此这里考虑封装一个工具类专门用于关闭使用过的IO流
     * @param
     */
   public static boolean closeFileInputStream(FileInputStream fis){
        if(fis!= null){
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
       return true;
   }

    public static boolean closeFileOutputStream(FileOutputStream fos){
        if(fos!= null){
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public static boolean closeBufferedInputStreamB(BufferedInputStream bis){
        if(bis!= null){
            try {
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public static boolean closeBufferedOutputStream(BufferedOutputStream bos){
        if(bos!= null){
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

}

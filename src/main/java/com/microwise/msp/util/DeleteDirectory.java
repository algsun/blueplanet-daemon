package com.microwise.msp.util;

import java.io.File;

/**
 * author : chenyaofei
 * Created by Administrator on 2016/7/8.
 */
public class DeleteDirectory {
    /**
     * 删除空目录
     * dir 将要删除的目录路径
     */
    public boolean deleteEmptyDir(String dir) {
        return (new File(dir)).delete();
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     * dir 将要删除的目录路径
     */
    public boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

}

package app.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author jinbing
 */
public class AssetUtils {

    /**
     * 复制assets中的文件到指定目录下
     * @param context
     * @param assetsFileName
     * @param targetPath
     * @return
     */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static boolean copyAssetData(Context context, String assetsFileName, String targetPath) {
        try {
            InputStream inputStream = context.getAssets().open(assetsFileName);
            File file = new File(targetPath + File.separator + assetsFileName);
            File dir = file.getParentFile();
            if (!dir.exists()) {
                dir.mkdirs();
            }

            dir.setReadable(true, false);
            dir.setExecutable(true, false);

            FileOutputStream output = new FileOutputStream(file.getPath());
            byte[] buf = new byte[10240];
            int count = 0;
            while ((count = inputStream.read(buf)) > 0) {
                output.write(buf, 0, count);
            }

            file.setReadable(true, false);

            output.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 复制assets中的文件夹到指定目录下
     * @param context
     * @param dirName
     * @param targetFolder
     */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static void copyAssetsDir(Context context, String dirName, String targetFolder) {
        try {
            File dir = new File(targetFolder + File.separator + dirName);
            if(!dir.exists() && !dir.isDirectory())
                dir.mkdirs();

            dir.setReadable(true, false);
            dir.setExecutable(true, false);

            String[] filenames = context.getAssets().list(dirName);
            InputStream inputStream = null;
            for(String filename : filenames) {
                String name = dirName + File.separator + filename;

                //如果是文件，则直接拷贝，如果是文件夹，就会抛出异常，捕捉后递归拷贝
                try {
                    inputStream = context.getAssets().open(name);
                    inputStream.close();
                    copyAssetData(context, name, targetFolder);
                } catch (Exception e) {
                    copyAssetsDir(context, name, targetFolder);
                } finally {
                    inputStream = null;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 复制assets中的文件/文件夹到指定目录下
     * @param context
     * @param dirName
     * @param targetFolder
     */
    public static void copyAssets(Context context, String dirName, String targetFolder) {
        //如果是文件，则调用复制文件方法，如果是文件夹，就会抛出异常，捕捉后调用复制文件夹方法
        try {
            InputStream inputStream = context.getAssets().open(dirName);
            inputStream.close();
            copyAssetData(context, dirName, targetFolder);
        } catch (IOException e) {
            copyAssetsDir(context, dirName, targetFolder);
        }
    }

}

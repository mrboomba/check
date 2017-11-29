package mrboomba.check.util;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mrboomba on 28/11/2560.
 */

public class FileUtil {
    public static boolean SD = false;
    private File sd = Environment.getExternalStorageDirectory();
    private String path = sd.getPath() + "/Checker";

    public FileUtil() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            SD = true;
        } else {
            SD = false;
        }
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    public void CreateFolder(String URL) {
        File file = new File(URL);
        if (!file.exists())
            file.mkdir();
    }

    public static List<String> getFileFormSDcard(File dir, String type) {
        List<String> listFilesName = new ArrayList<String>();
        if (SD) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].getName().indexOf(".") >= 0) {
                        // 只取Type类型的文件
                        String filesResult = files[i].getName().substring(
                                files[i].getName().indexOf("."));
                        if (filesResult.toLowerCase()
                                .equals(type.toLowerCase())) {
                            listFilesName.add(files[i].getName());
                        }

                    }
                }
            }
        }
        return listFilesName;
    }

    public String getFile(String FileName) {
        String URL = path + "/" + FileName;
        return URL;
    }

    public ArrayList<String> getName(String Name, String type) {
        ArrayList<String> list = new ArrayList<String>();
        if (SD) {
            String res = "";
            String a = null;
            FileUtil fe = new FileUtil();
            File file = new File(fe.getFile(Name));
            File[] files = new File(file.getPath()).listFiles();
            int start = 1;
            for (File f : files) {
                if (f.getName().indexOf(type) >= 0) {
                    res += f.getPath();
                    start = res.lastIndexOf("/") + 1;
                    a = res.substring(start);
                    list.add(a);

                }

            }
        }
        return list;
    }

    public void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) {
                InputStream inStream = new FileInputStream(oldPath);
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread;
                    fs.write(buffer, 0, byteread);
                }
                fs.close();
                inStream.close();
                // oldfile.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

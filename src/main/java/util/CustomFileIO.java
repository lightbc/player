package util;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * Description:自定义文件流
 * Package:util
 *
 * @author lightbc
 * @version 1.0
 */
public class CustomFileIO {
    /**
     * 读属性文件
     *
     * @param file
     * @return
     */
    public Map<String, String> readPropFile(File file) {
        Map<String, String> map = null;
        if (file.exists()) {
            InputStream is;
            FileInputStream fis;
            try {
                Properties prop = new Properties();
                fis = new FileInputStream(file);
                is = new BufferedInputStream(fis);
                prop.load(is);
                Iterator<String> it = prop.stringPropertyNames().iterator();
                map = new HashMap<String, String>();
                while (it.hasNext()) {
                    String key = it.next();
                    String value = prop.getProperty(key);
                    map.put(key, value);
                }
                is.close();
                fis.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return map;
    }

    /**
     * 写属性文件
     *
     * @param component
     */
    public void writePropFile(CustomPlayerComponent component) {
        if (component.getRltPath() != null) {
            File file = new File(CustomSystem.getInstance().installPath() + "prop.properties");
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            FileOutputStream fos;
            try {
                Properties prop = new Properties();
                fos = new FileOutputStream(file);
                prop.setProperty("player.resources.src", component.getRltPath());
                prop.store(fos, "The last time you opened the file directory");
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

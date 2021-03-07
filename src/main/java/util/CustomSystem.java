package util;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * Description:自定义系统设置
 * Package:util
 *
 * @author lightbc
 * @version 1.0
 */
public class CustomSystem {
    private static CustomSystem system;

    private CustomSystem() {
    }

    public static CustomSystem getInstance() {
        if (system == null) {
            system = new CustomSystem();
        }
        return system;
    }

    /**
     * 处理系统资源加载路径信息
     */
    public String getPath() {
        String path = "";
        File file = new File(installPath() + "prop.properties");
        if (file.exists()) {
            Map<String, String> map = new CustomFileIO().readPropFile(file);
            if (map != null) {
                path = map.get("player.resources.src");
            }
        }
        return path;
    }

    /**
     * 获取图标存放路径
     *
     * @return
     */
    public String getIconPath() {
        return installPath() + "icons/";
    }

    /**
     * 获取安装路径
     *
     * @return
     */
    String installPath() {
        String installPath;
        String s = "";
        try {
            installPath = CustomSystem.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            s = installPath.substring(1, installPath.lastIndexOf("/") + 1);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return s;
    }
}

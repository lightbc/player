package constants.settings;

import java.awt.*;

/**
 * Description:设置功能中需要的常量类型
 * Package:constants
 *
 * @author lightbc
 * @version 1.0
 */
public class Settings {
    // 关于界面窗体水平位置
    public static final int ABOUT_WINDOW_X = 0;
    // 关于界面窗体垂直位置
    public static final int ABOUT_WINDOW_Y = 35;
    // 关于界面窗体宽度
    public static final int ABOUT_WINDOW_W = 500;
    // 关于界面窗体高度
    public static final int ABOUT_WINDOW_H = 90;
    // 单元格内容
    public static final Object[][] ABOUT_TABLE_CELLS = {{"Author：", "lightbc"}, {"Blog：", "https://www.cnblogs.com/lightbc/"}, {"GitHub：", "https://github.com/lightbc"}};
    // 表头
    public static final Object[] ABOUT_TABLE_TITLE = {"name", "address"};
    // 关于界面表格行高
    public static final int ABOUT_TABLE_R_H = 30;
    // 关于界面窗体层级面板约束值
    public static final int ABOUT_WINDOW_PANE_L = 100;
    // 过滤协议
    public static final String[] ABOUT_FILTER_PROTOCOL = {"http", "https"};
    // 打开URL确认框消息
    public static final String ABOUT_OPEN_URL_CONFIRM_M = "您确认要访问该主页吗？";
    // 打开URL确认框标题
    public static final String ABOUT_OPEN_URL_CONFIRM_T = "标题";
    // 执行的命令行命令
    public static final String ABOUT_EXEC_COMMAND = "cmd.exe /c start ";
    // 表格边框颜色
    public static final Color ABOUT_LINE_BORDER_COLOR = new Color(117, 132, 147);
    // URL格式错误提示信息
    public static final String ABOUT_PROTOCOL_LOG_M = "===========================>:请检查协议格式是否有无或传值是否为空！";


    // 选择对话框宽度
    public static final int CHOOSE_DIALOG_W = 500;
    // 选择对话框高度
    public static final int CHOOSE_DIALOG_H = 300;
    // 选择对话框关闭图片
    public static final String CHOOSE_CLOSE_IMAGE = "close.png";
}

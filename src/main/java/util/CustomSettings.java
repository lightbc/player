package util;

import constants.settings.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

/**
 * Description:自定义设置
 * Package:util
 *
 * @author lightbc
 * @version 1.0
 */
public class CustomSettings {
    private static Logger logger = LoggerFactory.getLogger(CustomSettings.class);

    public JDialog dialog;

    private CustomPlayerComponent component;

    public CustomSettings(CustomPlayerComponent component) {
        this.component = component;
    }

    /**
     * settings选择列表
     *
     * @param setting
     */
    public void settingsListChooser(String setting) {
        if (dialog != null) {
            dialog.dispose();
        }
        dialog = new JDialog();
        dialog.setSize(Settings.CHOOSE_DIALOG_W, Settings.CHOOSE_DIALOG_H);
        dialog.setLocationRelativeTo(null);
        dialog.setUndecorated(true);
        dialog.setVisible(true);

        // 关闭dialog
        JButton closeBtn = new JButton();
        closeBtn.setBounds(dialog.getWidth() - component.btnWidth, 0, component.btnWidth, component.btnHeight);
        component.setCommonButton(closeBtn, Settings.CHOOSE_CLOSE_IMAGE);
        dialog.add(closeBtn);

        //关闭事件监听
        closeBtn.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                component.getSettingsList().setVisible(false);
                dialog.dispose();
            }
        });
        // dialog标题
        JLabel lb = new JLabel();
        lb.setBounds(0, 0, component.labelWidth + 30, component.labelHeight);

        // 层级面板
        component.setLayeredPane(new JLayeredPane());
        JLabel defaultChoose = component.getDefaultChoose();
        JLayeredPane layeredPane = component.getLayeredPane();
        layeredPane.setOpaque(true);
        layeredPane.setBackground(component.getChooserColor());
        layeredPane.setBounds(0, 0, 500, 300);
        layeredPane.add(defaultChoose, new Integer(200));
        layeredPane.add(closeBtn, new Integer(200), 1);
        layeredPane.add(lb, new Integer(200), 1);

        if (setting.equals("Skins")) {
            lb.setText("Skins");
            setColorList(238, 238, 238, 0, dialog);
            setColorList(94, 94, 94, 1, dialog);
            setColorList(139, 0, 139, 2, dialog);
            setColorList(141, 238, 238, 3, dialog);
            setColorList(238, 92, 66, 4, dialog);
            defaultChoose.setVisible(true);
        } else if (setting.equals("Author")) {
            showAbout();
            lb.setText("Author");
            defaultChoose.setVisible(false);
        }

    }

    /**
     * 颜色选择列表
     *
     * @param r
     * @param g
     * @param b
     * @param pos
     * @param dialog
     */
    void setColorList(final int r, final int g, final int b, final int pos, JDialog dialog) {
        component.setColorChooserLab(new JLabel());
        JLabel colorChooserLab = component.getColorChooserLab();
        colorChooserLab.setBounds(component.colorLabWidth * pos, component.colorLabTop, component.colorLabWidth, component.colorLabHeight);
        colorChooserLab.setOpaque(true);
        colorChooserLab.setBackground(new Color(r, g, b));

        JLayeredPane layeredPane = component.getLayeredPane();

        layeredPane.add(colorChooserLab, new Integer(100));
        dialog.add(layeredPane);

        colorChooserLab.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    component.getFramePanel().setBackground(new Color(r, g, b));
                    component.setChooserColor(new Color(r, g, b));
                    Color chooserColor = component.getChooserColor();
                    component.getFileName().setBackground(chooserColor);
                    component.getLayeredPane().setBackground(chooserColor);
                    component.getDefaultChoose().setBounds(component.colorLabWidth * (pos + 1) - component.checkWidth, component.colorLabTop + component.colorLabHeight - component.checkHeight, 40, 40);
                }
            }
        });
    }

    /**
     * 显示关于作者
     */
    void showAbout() {
        component.setTable(new JTable());
        JTable table = component.getTable();
        table.setBounds(Settings.ABOUT_WINDOW_X, Settings.ABOUT_WINDOW_Y, Settings.ABOUT_WINDOW_W, Settings.ABOUT_WINDOW_H);
        table.setOpaque(true);
        table.setBackground(component.getChooserColor());
        table.setBorder(BorderFactory.createLineBorder(Settings.ABOUT_LINE_BORDER_COLOR));
        // 单元格内容
        Object[][] rowObj = Settings.ABOUT_TABLE_CELLS;
        // 标题
        Object[] colObj = Settings.ABOUT_TABLE_TITLE;
        // 默认表格model
        DefaultTableModel model = new DefaultTableModel(rowObj, colObj) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        table.setModel(model);
        table.setRowHeight(Settings.ABOUT_TABLE_R_H);
        JLayeredPane layeredPane = component.getLayeredPane();
        layeredPane.add(table, new Integer(Settings.ABOUT_WINDOW_PANE_L));
        dialog.add(layeredPane);

        // table 鼠标事件监听
        table.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    JTable table = component.getTable();
                    int row = table.getSelectedRow();
                    int col = table.getSelectedColumn();
                    String value = "";
                    if (row != -1 && col != -1) {
                        value = (String) table.getValueAt(row, col);
                    }
                    try {
                        // http | https协议操作
                        if ((value.indexOf(Settings.ABOUT_FILTER_PROTOCOL[0]) != -1 || value.indexOf(Settings.ABOUT_FILTER_PROTOCOL[1]) != -1) && value != null) {
                            int result = JOptionPane.showConfirmDialog(null, Settings.ABOUT_OPEN_URL_CONFIRM_M, Settings.ABOUT_OPEN_URL_CONFIRM_T, JOptionPane.YES_NO_CANCEL_OPTION);
                            if (result == 0) {
                                // 打开符合条件的链接
                                Runtime.getRuntime().exec(Settings.ABOUT_EXEC_COMMAND + value);
                            }
                        } else {
                            logger.debug(Settings.ABOUT_PROTOCOL_LOG_M);
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 创建settings操作项
     *
     * @param opts
     * @param list
     * @param model
     */
    public void createSettingsOpts(String[] opts, JList list, DefaultListModel model) {
        list.setBounds(component.topLeft, 25, component.btnWidth + 20, component.btnHeight * opts.length);
        list.setVisible(false);
        for (String opt : opts) {
            model.addElement(opt);
        }
        list.setModel(model);
    }
}

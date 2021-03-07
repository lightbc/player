package util;

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
        dialog.setSize(500, 300);
        dialog.setLocationRelativeTo(null);
        dialog.setUndecorated(true);
        dialog.setVisible(true);

        // 关闭dialog
        JButton closeBtn = new JButton();
        closeBtn.setBounds(dialog.getWidth() - component.btnWidth, 0, component.btnWidth, component.btnHeight);
        component.setCommonButton(closeBtn, "close.png");
        dialog.add(closeBtn);

        //关闭事件监听
        closeBtn.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                component.settingsList.setVisible(false);
                dialog.dispose();
            }
        });
        // dialog标题
        JLabel lb = new JLabel();
        lb.setBounds(0, 0, component.labelWidth + 30, component.labelHeight);

        // 层级面板
        component.layeredPane = new JLayeredPane();
        JLayeredPane layeredPane = component.layeredPane;
        layeredPane.setOpaque(true);
        layeredPane.setBackground(component.chooserColor);
        layeredPane.setBounds(0, 0, 500, 300);
        layeredPane.add(component.defaultChoose, new Integer(200));
        layeredPane.add(closeBtn, new Integer(200), 1);
        layeredPane.add(lb, new Integer(200), 1);

        if (setting.equals("Skins")) {
            lb.setText("Skins");
            setColorList(238, 238, 238, 0, dialog);
            setColorList(94, 94, 94, 1, dialog);
            setColorList(139, 0, 139, 2, dialog);
            setColorList(141, 238, 238, 3, dialog);
            setColorList(238, 92, 66, 4, dialog);
            component.defaultChoose.setVisible(true);
        } else if (setting.equals("Author")) {
            showAbout();
            lb.setText("Author");
            component.defaultChoose.setVisible(false);
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
        component.colorChooserLab = new JLabel();
        JLabel colorChooserLab = component.colorChooserLab;
        colorChooserLab.setBounds(component.colorLabWidth * pos, component.colorLabTop, component.colorLabWidth, component.colorLabHeight);
        colorChooserLab.setOpaque(true);
        colorChooserLab.setBackground(new Color(r, g, b));

        component.layeredPane.add(colorChooserLab, new Integer(100));
        dialog.add(component.layeredPane);

        colorChooserLab.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    component.framePanel.setBackground(new Color(r, g, b));
                    component.chooserColor = new Color(r, g, b);
                    component.fileName.setBackground(component.chooserColor);
                    component.layeredPane.setBackground(component.chooserColor);
                    component.defaultChoose.setBounds(component.colorLabWidth * (pos + 1) - component.checkWidth, component.colorLabTop + component.colorLabHeight - component.checkHeight, 40, 40);
                }
            }
        });
    }

    /**
     * 显示关于
     */
    void showAbout() {
        component.table = new JTable();
        JTable table = component.table;
        table.setBounds(0, 35, 500, 90);
        table.setOpaque(true);
        table.setBackground(component.chooserColor);
        table.setBorder(BorderFactory.createLineBorder(new Color(117, 132, 147)));
        // 单元格内容
        Object[][] rowObj = {{"Author：", "lightbc"}, {"Blog：", "https://www.cnblogs.com/lightbc/"}, {"GitHub：", "https://github.com/lightbc"}};
        // 标题
        Object[] colObj = {"name", "address"};
        // 默认表格model
        DefaultTableModel model = new DefaultTableModel(rowObj, colObj) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        table.setModel(model);
        table.setRowHeight(30);
        component.layeredPane.add(table, new Integer(100));
        dialog.add(component.layeredPane);

        // table 鼠标事件监听
        table.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    int row = component.table.getSelectedRow();
                    int col = component.table.getSelectedColumn();
                    String value = "";
                    if (row != -1 && col != -1) {
                        value = (String) component.table.getValueAt(row, col);
                    }
                    try {
                        // http | https协议操作
                        if ((value.indexOf("http://") != -1 || value.indexOf("https://") != -1) && value != null) {
                            int result = JOptionPane.showConfirmDialog(null, "您确认要访问该主页吗？", "提示", JOptionPane.YES_NO_CANCEL_OPTION);
                            if (result == 0) {
                                // 打开符合条件的链接
                                Runtime.getRuntime().exec("cmd.exe /c start " + value);
                            }
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

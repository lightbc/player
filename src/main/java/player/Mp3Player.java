package player;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileView;
import javax.swing.plaf.metal.MetalSliderUI;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * MP3播放器
 *
 * @author lightbc
 */
public class Mp3Player extends JFrame {
    private JPanel framePanel;
    private JLabel playerIcon;
    private JLabel fileType;
    private JScrollPane scrollPane;
    private JScrollBar scrollBar;
    private JTextField fileName;
    private int scrollSpeed = 0;
    private JButton settings;
    private JList settingsList;
    private JDialog dialog;
    private JLabel defaultChoose = new JLabel();
    private JTable table;
    private JButton minimumWindow;
    private JButton closeWindow;
    private JLabel currentTime;
    private JLabel timeSeparator;
    private JLabel durationTime;
    private int duration;
    private JProgressBar progressBar;
    private int count = 0;
    private JButton play;
    private JButton volume;
    private JSlider volSlider;
    private int volValue = 50;
    private JButton playType;
    private boolean isRepeat = true;
    private JButton addFile;
    private int labelWidth = 30;
    private int labelHeight = 30;
    private int labelHints = 4;
    private int space = 5;
    private int btnWidth = 25;
    private int btnHeight = 25;
    private int btnHints = 4;
    private int topLeft = 725;
    private int bottomLeft = 710;
    private String iconsPath = "";
    private String itp = "";
    private JFileChooser fileChooser;
    private String[] fontFamilyNames;
    private Font[] fList;
    private String rltPath = "";
    private File rpf;
    private Properties prop = new Properties();
    private Media media;
    private MediaPlayer mp;
    private Timer timer;
    private Timer fnTimer;
    private JLabel colorChooserLab;
    private JLayeredPane layeredPane;
    private Color chooserColor;
    private int colorLabWidth = 100;
    private int colorLabHeight = 100;
    private int colorLabTop = 100;
    private int checkWidth = 40;
    private int checkHeight = 40;

    /**
     * 构造方法
     */
    public Mp3Player() {
        initFrame();
    }

    /**
     * 初始化frame
     */
    void initFrame() {

        // 获取安装路径
        itp = installPath();
        // 获取图标路径
        iconsPath = itp + "icons/";

        // 读取最近一次选择文件的上级路径
        rpf = new File(itp + "prop.properties");
        if (rpf.exists()) {
            Map<String, String> map = readPropFile(rpf);
            if (map != null) {
                rltPath = map.get("player.resources.src");
            }
        }

        // 设置frame基础参数
        this.setSize(800, 150);
        this.setIconImage(new ImageIcon(iconsPath + "icon.png").getImage());
        this.setLocationRelativeTo(null);
        this.setUndecorated(true);

        // 创建panel容器
        framePanel = new JPanel(null);
        framePanel.setOpaque(true);

        framePanelComponent(framePanel);

        this.add(framePanel);
        this.setVisible(true);

        // 创建jfxpanel对象
        new JFXPanel();

    }

    /**
     * 向panel添加组件
     *
     * @param panel
     */
    void framePanelComponent(JPanel panel) {

        // logo
        playerIcon = new JLabel();
        playerIcon.setBounds(0, 0, labelWidth, labelHeight);
        setCommonLabel(playerIcon, "icon.png");
        panel.add(playerIcon);

        // 文件类型
        fileType = new JLabel("", JLabel.CENTER);
        fileType.setBounds(labelWidth + space, 0, labelWidth, labelHeight);
        panel.add(fileType);

        // 文件名
        fileName = new JTextField("", 20);
        fileName.setBorder(new EmptyBorder(0, 0, 0, 0));
        fileName.setBackground(chooserColor);
        fileName.setDisabledTextColor(new Color(51, 51, 51));
        fileName.setHorizontalAlignment(JTextField.CENTER);
        fileName.setEnabled(false);

        // 获取本地字体
        fontFamilyNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        fList = new Font[fontFamilyNames.length];
        for (int i = 0; i < fontFamilyNames.length; i++) {
            fList[i] = new Font(fontFamilyNames[i], fileName.getFont().getStyle(), fileName.getFont().getSize());
        }

        fileName.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                JTextField textField = (JTextField) e.getSource();
                String s = textField.getText();
                Font font = textField.getFont();
                if (font.canDisplayUpTo(s) != -1) {
                    for (int i = 0; i < fList.length; i++) {
                        if (fList[i].canDisplayUpTo(s) == -1) {
                            textField.setFont(fList[i]);
                            break;
                        }
                    }
                }
            }
        });

        // 滚动条
        scrollPane = new JScrollPane(fileName);
        scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        scrollPane.setBounds(labelWidth * 2 + space * 3, 0, 100, labelHeight);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollBar = scrollPane.getHorizontalScrollBar();
        panel.add(scrollPane);

        // 设置
        settings = new JButton();
        settings.setToolTipText("settings");
        settings.setBounds(topLeft, 0, btnWidth, btnHeight);
        setCommonButton(settings, "settings.png");
        panel.add(settings);

        // 设置事件监听
        settings.addMouseListener(new MouseAdapter() {
            boolean bool = false;

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (!bool) {
                        settingsList.setVisible(true);
                        bool = true;
                    } else {
                        settingsList.setVisible(false);
                        bool = false;
                    }
                }
            }
        });

        // 设置列表
        settingsList = new JList();
        String[] opts = {"Skins", "Author"};
        createSettingsOpts(opts, settingsList, new DefaultListModel());
        panel.add(settingsList);

        defaultChoose.setBounds(colorLabWidth - checkWidth, colorLabTop + colorLabHeight - checkHeight, 40, 40);
        setCommonLabel(defaultChoose, "check.png");

        // 设置列表事件监听
        settingsList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (settingsList.getSelectedValue().equals("Skins")) {
                        settingsListChooser(settingsList.getSelectedValue().toString());
                    } else if (settingsList.getSelectedValue().equals("Author")) {
                        settingsListChooser(settingsList.getSelectedValue().toString());
                    }
                }
            }
        });

        // 最小化
        minimumWindow = new JButton();
        minimumWindow.setToolTipText("minimum");
        minimumWindow.setBounds(topLeft + btnWidth, 0, btnWidth, btnHeight);
        setCommonButton(minimumWindow, "min.png");
        panel.add(minimumWindow);

        // 最小化事件监听
        minimumWindow.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    setExtendedState(Frame.ICONIFIED);
                }
            }
        });

        // 关闭窗体
        closeWindow = new JButton();
        closeWindow.setToolTipText("close");
        closeWindow.setBounds(topLeft + btnWidth * 2, 0, btnWidth, btnHeight);
        setCommonButton(closeWindow, "close.png");
        panel.add(closeWindow);

        // 关闭窗体事件监听
        closeWindow.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    System.exit(0);
                }
            }
        });

        // 当前时间
        currentTime = new JLabel("00:00", JLabel.CENTER);
        currentTime.setFont(new Font("宋体", Font.BOLD, 20));
        currentTime.setBounds(0, labelHeight, 70, 70);
        panel.add(currentTime);

        // 分隔
        timeSeparator = new JLabel("/", JLabel.CENTER);
        timeSeparator.setBounds(currentTime.getWidth(), labelHeight + currentTime.getHeight() / 2, 10, 35);
        panel.add(timeSeparator);

        // 总时间
        durationTime = new JLabel("00:00", JLabel.CENTER);
        durationTime.setBounds(currentTime.getWidth() + timeSeparator.getWidth(), labelHeight + currentTime.getHeight() / 2, 35, 35);
        panel.add(durationTime);

        // 进度条
        progressBar = new JProgressBar();
        progressBar.setBounds(currentTime.getWidth() + timeSeparator.getWidth() + durationTime.getWidth() + space * 2, labelHeight + currentTime.getHeight() / 2, 670, 7);
        panel.add(progressBar);

        // 播放/暂停
        play = new JButton();
        play.setToolTipText("play/pause");
        play.setBounds(0, labelHeight + currentTime.getHeight() + space, 35, 35);
        setCommonButton(play, "play.png");
        panel.add(play);

        // 播放/暂停事件监听
        play.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (mp != null) {
                        mp.setVolume(volValue);
                        if (mp.getStatus() == MediaPlayer.Status.READY) {
                            setCommonButton(play, "pause.png");
                            mp.play();
                            timer();
                        } else if (mp.getStatus() == MediaPlayer.Status.PLAYING) {
                            setCommonButton(play, "play.png");
                            mp.pause();
                            adjustTimer("PAUSED");
                        } else if (mp.getStatus() == MediaPlayer.Status.PAUSED) {
                            setCommonButton(play, "pause.png");
                            mp.play();
                            adjustTimer("PLAYING");
                        } else if (mp.getStatus() == MediaPlayer.Status.STOPPED) {
                            setCommonButton(play, "pause.png");
                            mp.play();
                            timer();
                        }
                    }
                }
            }
        });

        // 音量
        volume = new JButton();
        volume.setToolTipText("volume");
        volume.setBounds(play.getWidth(), labelHeight + currentTime.getHeight() + space, 35, 35);
        setCommonButton(volume, "volume.png");
        panel.add(volume);

        // 音量事件监听
        volume.addMouseListener(new MouseAdapter() {
            boolean bool;


            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (!bool) {
                        volSlider.setVisible(true);
                        bool = true;
                    } else {
                        volSlider.setVisible(false);
                        bool = false;
                    }
                }
            }
        });

        // 音量滑块
        volSlider = new JSlider(0, 100, volValue);
        volSlider.setBounds(play.getWidth() + volume.getWidth(), labelHeight + currentTime.getHeight() + space * 3, 150, 15);
        volSlider.setVisible(false);
        panel.add(volSlider);

        // 滑块事件监听
        volSlider.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                volValue = volSlider.getValue();
                adjustVolume(volValue);
                if (volValue == 0) {
                    setCommonButton(volume, "mute.png");
                } else {
                    setCommonButton(volume, "volume.png");
                }
            }
        });

        // 滑块自定义UI
        volSlider.setUI(new MetalSliderUI() {
            // 绘制滑块
            public void paintThumb(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(Color.white);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.fillRect(thumbRect.x + 5, thumbRect.y, thumbRect.width - 10, thumbRect.height);
            }

            // 绘制轨道
            public void paintTrack(Graphics g) {
                int cy, cw;
                Rectangle trackBounds = trackRect;
                if (slider.getOrientation() == JSlider.HORIZONTAL) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setPaint(new Color(163, 184, 204));
                    cy = (trackBounds.height / 2) - 2;
                    cw = trackBounds.width;

                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.translate(trackBounds.x, trackBounds.y + cy);
                    g2.fillRect(0, -cy + 5, cw, cy);

                    int trackLeft = 0;
                    int trackRight;
                    trackRight = trackRect.width - 1;

                    int middleOfThumb;
                    int fillLeft;
                    int fillRight;

                    middleOfThumb = thumbRect.x + (thumbRect.width / 2);
                    middleOfThumb -= thumbRect.x;

                    if (!drawInverted()) {
                        fillLeft = !slider.isEnabled() ? trackLeft : trackLeft + 1;
                        fillRight = middleOfThumb;
                    } else {
                        fillLeft = middleOfThumb;
                        fillRight = !slider.isEnabled() ? trackRight - 1 : trackRight - 2;
                    }
                    g2.setPaint(new GradientPaint(0, 0, new Color(163, 184, 204), cw, 0, new Color(163, 184, 204), true));
                    g2.fillRect(0, -cy + 5, fillRight - fillLeft, cy);

                    g2.setPaint(slider.getBackground());
                    g2.fillRect(10, 10, cw, 5);

                    g2.setPaint(Color.white);
                    g2.drawLine(0, cy, cw - 1, cy);

                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
                    g2.translate(-trackBounds.x, -(trackBounds.y + cy));
                } else {
                    super.paintTrack(g);
                }
            }
        });

        // 是否循环播放
        playType = new JButton();
        playType.setToolTipText("repeat/no repeat");
        playType.setBounds(bottomLeft, labelHeight + currentTime.getHeight() + space, 35, 35);
        setCommonButton(playType, "repeat.png");
        panel.add(playType);

        // 是否循环
        playType.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (!isRepeat) {
                        setCommonButton(playType, "repeat.png");
                        isRepeat = true;
                    } else {
                        setCommonButton(playType, "norepeat.png");
                        isRepeat = false;
                    }
                }
            }
        });

        // 选择文件
        addFile = new JButton();
        addFile.setToolTipText("choose file");
        addFile.setBounds(bottomLeft + playType.getWidth(), labelHeight + currentTime.getHeight() + space, 35, 35);
        setCommonButton(addFile, "plus.png");
        panel.add(addFile);

        // 选择事件监听
        addFile.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (rltPath.equals("")) {
                        fileChooser = new JFileChooser("c:/");
                        fileChooser.changeToParentDirectory();
                    } else {
                        fileChooser = new JFileChooser(rltPath);
                    }
                    fileChooser.setFileView(new FileView() {

                        public String getName(File f) {
                            return super.getName(f);
                        }


                        public Icon getIcon(File f) {
                            return fileChooser.getFileSystemView().getSystemIcon(f);
                        }
                    });
                    fileChooser.setDialogTitle("文件");
                    // 关闭多选
                    fileChooser.setMultiSelectionEnabled(false);
                    // 单个文件选择
                    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                    // 关闭选择"所有文件"
                    fileChooser.removeChoosableFileFilter(fileChooser.getAcceptAllFileFilter());
                    // 设置可选文件类型
                    fileChooser.setFileFilter(new FileNameExtensionFilter("*.mp3", "mp3"));
                    int result = fileChooser.showOpenDialog(null);

                    if (fileChooser.getSelectedFile() != null && result == JFileChooser.APPROVE_OPTION) {
                        File file = fileChooser.getSelectedFile();
                        // 记录上次访问文件的上级目录
                        rltPath = file.getParent();
                        writePropFile(rltPath);
                        initPlayer();
                        player(file);
                    }
                }
            }
        });
    }

    /**
     * 设置普通的label
     *
     * @param label
     * @param name
     */
    void setCommonLabel(JLabel label, String name) {
        String path = iconsPath + name;
        Image img = new ImageIcon(path).getImage().getScaledInstance(labelWidth, labelHeight, labelHints);
        label.setIcon(new ImageIcon(img));
    }

    /**
     * 设置普通的button
     *
     * @param button
     * @param name
     */
    void setCommonButton(JButton button, String name) {
        if (name != null) {
            String path = iconsPath + name;
            Image img = new ImageIcon(path).getImage().getScaledInstance(btnWidth, btnHeight, btnHints);
            button.setIcon(new ImageIcon(img));
        }
        // 不可设为焦点
        button.setFocusPainted(false);
        // 设置透明
        button.setContentAreaFilled(false);
        button.setBorder(new EmptyBorder(2, 2, 2, 2));
    }

    /**
     * 播放器
     *
     * @param file
     */
    void player(File file) {
        if (file != null && file.exists()) {
            // 截取文件类型并转换为大写
            String type = file.getName().substring(file.getName().lastIndexOf(".") + 1).toUpperCase();
            // 截取不带扩展名的文件名
            String name = file.getName().substring(0, file.getName().lastIndexOf("."));
            fileType.setText(type);
            fileName.setText(name);
            if (fnTimer != null) {
                fnTimer.stop();
                scrollSpeed = 0;
                fileNameTimer();
            } else {
                fileNameTimer();
            }
            AudioHeader header = null;
            try {
                // 获取音频文件的header
                header = AudioFileIO.read(file).getAudioHeader();
            } catch (CannotReadException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (TagException ex) {
                ex.printStackTrace();
            } catch (ReadOnlyFileException ex) {
                ex.printStackTrace();
            } catch (InvalidAudioFrameException ex) {
                ex.printStackTrace();
            }
            if (header != null) {
                // 获取持续时间（秒）
                duration = header.getTrackLength();
                int minutes = duration / 60;
                int seconds = duration % 60;
                // 格式持续时间，例：xx:xx
                String dtFormat = (minutes > 9 ? String.valueOf(minutes) : "0" + minutes) +
                        ":" +
                        (seconds > 9 ? String.valueOf(seconds) : "0" + seconds);
                durationTime.setText(dtFormat);
                // 创建media
                media = new Media(file.toURI().toString());
                // 创建media player
                mp = new MediaPlayer(media);
            }
        }
    }

    /**
     * 调整播放的timer
     *
     * @param status
     */
    void adjustTimer(String status) {
        if (status.equals("PLAYING")) {
            timer.start();
        } else if (status.equals("PAUSED")) {
            timer.stop();
        }
    }

    /**
     * 播放timer
     */
    void timer() {
        progressBar.setMinimum(count);
        progressBar.setMaximum(duration);
        timer = new Timer(1000, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                count++;
                int minutes = count / 60;
                int seconds = count % 60;
                // 格式当前已播放时间，例：xx:xx
                String current = (minutes > 9 ? String.valueOf(minutes) : "0" + minutes) +
                        ":" +
                        (seconds > 9 ? String.valueOf(seconds) : "0" + seconds);
                progressBar.setValue(count);
                currentTime.setText(current);
                if (count > duration) {
                    initPlayer();
                    if (isRepeat) {
                        mp.play();
                        timer.start();
                        setCommonButton(play, "pause.png");
                    }
                }
            }
        });
        timer.start();
    }

    /**
     * filename timer
     */
    void fileNameTimer() {
        fnTimer = new Timer(250, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                scrollSpeed += 10;
                if (scrollSpeed > scrollBar.getMaximum()) {
                    scrollSpeed = 0;
                }
                scrollBar.setValue(scrollSpeed);
            }
        });
        if (scrollBar.getMaximum() > scrollPane.getWidth()) {
            fnTimer.start();
        }
    }

    /**
     * 音量调节
     *
     * @param volValue
     */
    void adjustVolume(int volValue) {
        if (mp != null) {
            mp.setVolume(volValue);
        }
    }

    /**
     * 初始播放器
     */
    void initPlayer() {
        if (mp != null && timer != null) {
            mp.stop();
            currentTime.setText("00:00");
            count = 0;
            progressBar.setValue(0);
            timer.stop();
            setCommonButton(play, "play.png");
        }
    }

    /**
     * settings选择列表
     *
     * @param setting
     */
    void settingsListChooser(String setting) {
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
        closeBtn.setBounds(dialog.getWidth() - btnWidth, 0, btnWidth, btnHeight);
        setCommonButton(closeBtn, "close.png");
        dialog.add(closeBtn);

        //关闭事件监听
        closeBtn.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                settingsList.setVisible(false);
                dialog.dispose();
            }
        });
        // dialog标题
        JLabel lb = new JLabel();
        lb.setBounds(0, 0, labelWidth + 30, labelHeight);

        // 层级面板
        layeredPane = new JLayeredPane();
        layeredPane.setOpaque(true);
        layeredPane.setBackground(chooserColor);
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
        colorChooserLab = new JLabel();
        colorChooserLab.setBounds(colorLabWidth * pos, colorLabTop, colorLabWidth, colorLabHeight);
        colorChooserLab.setOpaque(true);
        colorChooserLab.setBackground(new Color(r, g, b));

        layeredPane.add(colorChooserLab, new Integer(100));
        dialog.add(layeredPane);

        colorChooserLab.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                framePanel.setBackground(new Color(r, g, b));
                chooserColor = new Color(r, g, b);
                fileName.setBackground(chooserColor);
                layeredPane.setBackground(chooserColor);
                defaultChoose.setBounds(colorLabWidth * (pos + 1) - checkWidth, colorLabTop + colorLabHeight - checkHeight, 40, 40);
            }
        });
    }

    /**
     * 显示关于
     */
    void showAbout() {
        table = new JTable();
        table.setBounds(0, 35, 500, 90);
        table.setOpaque(true);
        table.setBackground(chooserColor);
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
        layeredPane.add(table, new Integer(100));
        dialog.add(layeredPane);

        // table 鼠标事件监听
        table.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                int col = table.getSelectedColumn();
                String value = "";
                if (row != -1 && col != -1) {
                    value = (String) table.getValueAt(row, col);
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
        });
    }

    /**
     * 创建settings操作项
     *
     * @param opts
     * @param list
     * @param model
     */
    void createSettingsOpts(String[] opts, JList list, DefaultListModel model) {
        list.setBounds(topLeft, 25, btnWidth + 20, btnHeight * opts.length);
        list.setVisible(false);
        for (String opt : opts) {
            model.addElement(opt);
        }
        list.setModel(model);
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
            installPath = Mp3Player.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            s = installPath.substring(1, installPath.lastIndexOf("/") + 1);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return s;
    }

    /**
     * 读属性文件
     *
     * @param file
     * @return
     */
    Map<String, String> readPropFile(File file) {
        Map<String, String> map = null;
        if (file.exists()) {
            InputStream is;
            FileInputStream fis;
            try {
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
     * @param path
     */
    void writePropFile(String path) {
        if (path != null) {
            if (!rpf.exists()) {
                try {
                    rpf.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            FileOutputStream fos;
            try {
                fos = new FileOutputStream(rpf);
                prop.setProperty("player.resources.src", path);
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

    /**
     * 主方法
     *
     * @param args
     */
    public static void main(String[] args) {
        new Mp3Player();
    }

}

package util;

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
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * Description:自定义播放器组件功能
 * Package:util
 *
 * @author lightbc
 * @version 1.0
 */
public class CustomPlayerComponent extends JFrame {
    public JPanel framePanel;
    public JLabel playerIcon;
    public JLabel fileType;
    public JScrollPane scrollPane;
    public JScrollBar scrollBar;
    public int scrollSpeed = 0;
    public JLabel defaultChoose = new JLabel();
    public JTable table;
    public JLabel currentTime;
    public JLabel timeSeparator;
    public JLabel durationTime;
    public int duration;
    public JProgressBar progressBar;
    public int count = 0;
    public int labelWidth = 30;
    public int labelHeight = 30;
    public int labelHints = 4;
    public int space = 5;
    public int btnWidth = 25;
    public int btnHeight = 25;
    public int btnHints = 4;
    public int topLeft = 725;
    public int bottomLeft = 710;
    public String iconsPath = "";
    public String itp = "";
    public String[] fontFamilyNames;
    public File rpf;
    public Properties prop = new Properties();
    public Media media;
    public Timer timer;
    public Timer fnTimer;
    public JLabel colorChooserLab;
    public JLayeredPane layeredPane;
    public Color chooserColor;
    public int colorLabWidth = 100;
    public int colorLabHeight = 100;
    public int colorLabTop = 100;
    public int checkWidth = 40;
    public int checkHeight = 40;

    public JTextField fileName;
    public Font[] fList;
    public JButton settings;
    public JList settingsList;
    public JButton minimumWindow;
    public JButton closeWindow;
    public JButton play;
    public MediaPlayer mp;
    public int volValue = 50;
    public JButton volume;
    public JSlider volSlider;
    public JButton playType;
    public boolean isRepeat = true;
    public JButton addFile;
    public String rltPath = "";
    public JFileChooser fileChooser;

    /**
     * 初始化frame
     */
    public void initFrame() {
        iconsPath = CustomSystem.getInstance().getIconPath();
        rltPath = CustomSystem.getInstance().getPath();

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
    public void framePanelComponent(JPanel panel) {

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

        // 设置列表
        settingsList = new JList();
        String[] opts = {"Skins", "Author"};
        new CustomSettings(this).createSettingsOpts(opts, settingsList, new DefaultListModel());
        panel.add(settingsList);

        defaultChoose.setBounds(colorLabWidth - checkWidth, colorLabTop + colorLabHeight - checkHeight, 40, 40);
        setCommonLabel(defaultChoose, "check.png");

        // 最小化
        minimumWindow = new JButton();
        minimumWindow.setToolTipText("minimum");
        minimumWindow.setBounds(topLeft + btnWidth, 0, btnWidth, btnHeight);
        setCommonButton(minimumWindow, "min.png");
        panel.add(minimumWindow);

        // 关闭窗体
        closeWindow = new JButton();
        closeWindow.setToolTipText("close");
        closeWindow.setBounds(topLeft + btnWidth * 2, 0, btnWidth, btnHeight);
        setCommonButton(closeWindow, "close.png");
        panel.add(closeWindow);

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

        // 音量
        volume = new JButton();
        volume.setToolTipText("volume");
        volume.setBounds(play.getWidth(), labelHeight + currentTime.getHeight() + space, 35, 35);
        setCommonButton(volume, "volume.png");
        panel.add(volume);

        // 音量滑块
        volSlider = new JSlider(0, 100, volValue);
        volSlider.setBounds(play.getWidth() + volume.getWidth(), labelHeight + currentTime.getHeight() + space * 3, 150, 15);
        volSlider.setVisible(false);
        panel.add(volSlider);

        // 滑块自定义UI
        CustomUI ui = CustomUI.getInstance();
        ui.volSliderUI(volSlider);

        // 是否循环播放
        playType = new JButton();
        playType.setToolTipText("repeat/no repeat");
        playType.setBounds(bottomLeft, labelHeight + currentTime.getHeight() + space, 35, 35);
        setCommonButton(playType, "repeat.png");
        panel.add(playType);

        // 选择文件
        addFile = new JButton();
        addFile.setToolTipText("choose file");
        addFile.setBounds(bottomLeft + playType.getWidth(), labelHeight + currentTime.getHeight() + space, 35, 35);
        setCommonButton(addFile, "plus.png");
        panel.add(addFile);

        CustomListener listener = new CustomListener(this, new CustomFileIO());
        listener.startListener();
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
    protected void setCommonButton(JButton button, String name) {
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
    protected void player(File file) {
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
    protected void adjustTimer(String status) {
        if (status.equals("PLAYING")) {
            timer.start();
        } else if (status.equals("PAUSED")) {
            timer.stop();
        }
    }

    /**
     * 播放timer
     */
    protected void timer() {
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
    protected void adjustVolume(int volValue) {
        if (mp != null) {
            mp.setVolume(volValue);
        }
    }

    /**
     * 初始播放器
     */
    protected void initPlayer() {
        if (mp != null && timer != null) {
            mp.stop();
            currentTime.setText("00:00");
            count = 0;
            progressBar.setValue(0);
            timer.stop();
            setCommonButton(play, "play.png");
        }
    }
}

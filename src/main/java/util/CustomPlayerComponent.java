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
import entity.Components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

/**
 * Description:自定义播放器组件功能
 * Package:util
 *
 * @author lightbc
 * @version 1.0
 */
public class CustomPlayerComponent extends Components {
    public int labelWidth = 30;

    public int labelHeight = 30;

    public int labelHints = 4;

    public int space = 5;

    public int btnWidth = 25;

    public int btnHeight = 25;

    public int btnHints = 4;

    public int topLeft = 725;

    public int bottomLeft = 710;

    public int colorLabWidth = 100;

    public int colorLabHeight = 100;

    public int colorLabTop = 100;

    public int checkWidth = 40;

    public int checkHeight = 40;

    private Timer timer;

    private Timer fnTimer;

    private CustomListener listener;

    /**
     * 初始化frame
     */
    public void initFrame() {
        setIconsPath(CustomSystem.getInstance().getIconPath());
        setRltPath(CustomSystem.getInstance().getPath());

        // 设置frame基础参数
        this.setSize(800, 150);
        this.setIconImage(new ImageIcon(getIconsPath() + "icon.png").getImage());
        this.setLocationRelativeTo(null);
        this.setUndecorated(true);

        // 创建panel容器
        setFramePanel(new JPanel(null));
        JPanel framePanel = getFramePanel();
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
        setPlayerIcon(new JLabel());
        JLabel playerIcon = getPlayerIcon();
        playerIcon.setBounds(0, 0, labelWidth, labelHeight);
        setCommonLabel(playerIcon, "icon.png");
        panel.add(playerIcon);

        // 文件类型
        setFileType(new JLabel("", JLabel.CENTER));
        JLabel fileType = getFileType();
        fileType.setBounds(labelWidth + space, 0, labelWidth, labelHeight);
        panel.add(fileType);

        // 歌名滚动
        scrollSongName();

        // 设置
        setSettings(new JButton());
        JButton settings = getSettings();
        settings.setToolTipText("settings");
        settings.setBounds(topLeft, 0, btnWidth, btnHeight);
        setCommonButton(settings, "settings.png");
        panel.add(settings);

        // 设置列表
        setSettingsList(new JList());
        JList settingsList = getSettingsList();
        String[] opts = {"Skins", "Author"};
        new CustomSettings(this).createSettingsOpts(opts, settingsList, new DefaultListModel());
        panel.add(settingsList);

        getDefaultChoose().setBounds(colorLabWidth - checkWidth, colorLabTop + colorLabHeight - checkHeight, 40, 40);
        setCommonLabel(getDefaultChoose(), "check.png");

        // 最小化
        setMinimumWindow(new JButton());
        JButton minimumWindow = getMinimumWindow();
        minimumWindow.setToolTipText("minimum");
        minimumWindow.setBounds(topLeft + btnWidth, 0, btnWidth, btnHeight);
        setCommonButton(minimumWindow, "min.png");
        panel.add(minimumWindow);

        // 关闭窗体
        setCloseWindow(new JButton());
        JButton closeWindow = getCloseWindow();
        closeWindow.setToolTipText("close");
        closeWindow.setBounds(topLeft + btnWidth * 2, 0, btnWidth, btnHeight);
        setCommonButton(closeWindow, "close.png");
        panel.add(closeWindow);

        // 当前时间
        setCurrentTime(new JLabel("00:00", JLabel.CENTER));
        JLabel currentTime = getCurrentTime();
        currentTime.setFont(new Font("宋体", Font.BOLD, 20));
        currentTime.setBounds(0, labelHeight, 70, 70);
        panel.add(currentTime);

        // 分隔
        setTimeSeparator(new JLabel("/", JLabel.CENTER));
        JLabel timeSeparator = getTimeSeparator();
        timeSeparator.setBounds(currentTime.getWidth(), labelHeight + currentTime.getHeight() / 2, 10, 35);
        panel.add(timeSeparator);

        // 总时间
        setDurationTime(new JLabel("00:00", JLabel.CENTER));
        JLabel durationTime = getDurationTime();
        durationTime.setBounds(currentTime.getWidth() + timeSeparator.getWidth(), labelHeight + currentTime.getHeight() / 2, 35, 35);
        panel.add(durationTime);

        // 进度条
        setProgressBar(new JProgressBar());
        JProgressBar progressBar = getProgressBar();
        progressBar.setBounds(currentTime.getWidth() + timeSeparator.getWidth() + durationTime.getWidth() + space * 2, labelHeight + currentTime.getHeight() / 2, 670, 7);
        panel.add(progressBar);

        // 播放/暂停
        setPlay(new JButton());
        JButton play = getPlay();
        play.setToolTipText("play/pause");
        play.setBounds(0, labelHeight + currentTime.getHeight() + space, 35, 35);
        setCommonButton(play, "play.png");
        panel.add(play);

        // 音量
        setVolume(new JButton());
        JButton volume = getVolume();
        volume.setToolTipText("volume");
        volume.setBounds(play.getWidth(), labelHeight + currentTime.getHeight() + space, 35, 35);
        setCommonButton(volume, "volume.png");
        panel.add(volume);

        // 音量滑块
        setVolSlider(new JSlider(0, 100, getVolValue()));
        JSlider volSlider = getVolSlider();
        volSlider.setBounds(play.getWidth() + volume.getWidth(), labelHeight + currentTime.getHeight() + space * 3, 150, 15);
        volSlider.setVisible(false);
        panel.add(volSlider);

        // 滑块自定义UI
        CustomUI ui = CustomUI.getInstance();
        ui.volSliderUI(volSlider);

        // 是否循环播放
        setPlayType(new JButton());
        JButton playType = getPlayType();
        playType.setToolTipText("repeat/no repeat");
        playType.setBounds(bottomLeft, labelHeight + currentTime.getHeight() + space, 35, 35);
        setCommonButton(playType, "repeat.png");
        panel.add(playType);

        // 选择文件
        setAddFile(new JButton());
        JButton addFile = getAddFile();
        addFile.setToolTipText("choose file");
        addFile.setBounds(bottomLeft + playType.getWidth(), labelHeight + currentTime.getHeight() + space, 35, 35);
        setCommonButton(addFile, "plus.png");
        panel.add(addFile);

        listener = new CustomListener(this, new CustomFileIO());
        listener.startListener();
    }

    /**
     * 设置普通的label
     *
     * @param label
     * @param name
     */
    void setCommonLabel(JLabel label, String name) {
        String path = getIconsPath() + name;
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
            String path = getIconsPath() + name;
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
            getFileType().setText(type);
            getFileName().setText(name);
            if (fnTimer != null) {// 如果歌名滚动的定时器不为空，则将定时器置空
                fnTimer = null;
            }
            fileNameTimer();
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
                setDuration(header.getTrackLength());
                int duration = getDuration();
                int minutes = duration / 60;
                int seconds = duration % 60;
                // 格式持续时间，例：xx:xx
                String dtFormat = (minutes > 9 ? String.valueOf(minutes) : "0" + minutes) +
                        ":" +
                        (seconds > 9 ? String.valueOf(seconds) : "0" + seconds);
                getDurationTime().setText(dtFormat);
                // 创建media
                setMedia(new Media(file.toURI().toString()));
                Media media = getMedia();
                // 创建media player
                setMp(new MediaPlayer(media));
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
        JProgressBar progressBar = getProgressBar();
        int duration = getDuration();
        progressBar.setMinimum(0);
        progressBar.setMaximum(duration);
        timer = new Timer(1000, new ActionListener() {
            int count = 0;
            public void actionPerformed(ActionEvent e) {
                count++;
                int minutes = count / 60;
                int seconds = count % 60;
                // 格式当前已播放时间，例：xx:xx
                String current = (minutes > 9 ? String.valueOf(minutes) : "0" + minutes) +
                        ":" +
                        (seconds > 9 ? String.valueOf(seconds) : "0" + seconds);
                getProgressBar().setValue(count);
                getCurrentTime().setText(current);
                if (count > getDuration()) {
                    initPlayer();
                    count = 0;
                    if (isRepeat()) {
                        getMp().play();
                        timer.start();
                        setCommonButton(getPlay(), "pause.png");
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
            int scrollSpeed = getScrollSpeed();
            public void actionPerformed(ActionEvent e) {
                scrollSpeed += 10;
                if (scrollSpeed > getScrollBar().getMaximum()) {
                    scrollSpeed = 0;
                }
                getScrollBar().setValue(scrollSpeed);
            }
        });
        if (getScrollBar().getMaximum() > getScrollPane().getWidth()) {
            fnTimer.start();
        }
    }

    /**
     * 音量调节
     *
     * @param volValue
     */
    protected void adjustVolume(int volValue) {
        if (getMp() != null) {
            getMp().setVolume(volValue);
        }
    }

    /**
     * 初始播放器
     */
    protected void initPlayer() {
        if (getMp() != null && timer != null) {
            getMp().stop();
            getCurrentTime().setText("00:00");
            getProgressBar().setValue(0);
            timer.stop();
            setCommonButton(getPlay(), "play.png");
            scrollSongName();
        }
    }

    /**
     * 歌名滚动方法
     */
    public void scrollSongName() {
        // 文件名
        setFileName(new JTextField("", 20));
        JTextField fileName = getFileName();
        fileName.setBorder(new EmptyBorder(0, 0, 0, 0));
        fileName.setBackground(getChooserColor());
        fileName.setDisabledTextColor(new Color(51, 51, 51));
        fileName.setHorizontalAlignment(JTextField.CENTER);
        fileName.setEnabled(false);

        // 获取本地字体
        String[] fontFamilyNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        Font[] fList = new Font[fontFamilyNames.length];
        for (int i = 0; i < fontFamilyNames.length; i++) {
            fList[i] = new Font(fontFamilyNames[i], fileName.getFont().getStyle(), fileName.getFont().getSize());
        }
        setfList(fList);

        // 滚动条
        setScrollPane(new JScrollPane(fileName));
        JScrollPane scrollPane = getScrollPane();
        scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        scrollPane.setBounds(labelWidth * 2 + space * 3, 0, 100, labelHeight);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        setScrollBar(scrollPane.getHorizontalScrollBar());
        getFramePanel().add(scrollPane);
    }
}

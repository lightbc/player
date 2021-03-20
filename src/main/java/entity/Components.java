package entity;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.swing.*;
import java.awt.*;

/**
 * Description:组件实体类
 * Package:entity
 *
 * @author lightbc
 * @version 1.0
 */
public class Components extends JFrame {
    private JPanel framePanel;

    private JLabel playerIcon;

    private JLabel fileType;

    private JScrollPane scrollPane;

    private JScrollBar scrollBar;

    private int scrollSpeed = 0;

    private JLabel defaultChoose = new JLabel();

    private JTable table;

    private JLabel currentTime;

    private JLabel timeSeparator;

    private JLabel durationTime;

    private int duration;

    private JProgressBar progressBar;

    private String iconsPath;

    private String[] fontFamilyNames;

    private Media media;

    private JLabel colorChooserLab;

    private JLayeredPane layeredPane;

    private Color chooserColor;

    // 页面组件
    private JTextField fileName;

    private Font[] fList;

    private JButton settings;

    private JList settingsList;

    private JButton minimumWindow;

    private JButton closeWindow;

    private JButton play;

    private MediaPlayer mp;

    private JButton volume;

    private JSlider volSlider;

    private JButton playType;

    private boolean isRepeat = true;

    private JButton addFile;

    private String rltPath;

    private JFileChooser fileChooser;

    private int volValue = 50;

    public int getVolValue() {
        return volValue;
    }

    public void setVolValue(int volValue) {
        this.volValue = volValue;
    }

    public JPanel getFramePanel() {
        return framePanel;
    }

    public void setFramePanel(JPanel framePanel) {
        this.framePanel = framePanel;
    }

    public JLabel getPlayerIcon() {
        return playerIcon;
    }

    public void setPlayerIcon(JLabel playerIcon) {
        this.playerIcon = playerIcon;
    }

    public JLabel getFileType() {
        return fileType;
    }

    public void setFileType(JLabel fileType) {
        this.fileType = fileType;
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    public void setScrollPane(JScrollPane scrollPane) {
        this.scrollPane = scrollPane;
    }

    public JScrollBar getScrollBar() {
        return scrollBar;
    }

    public void setScrollBar(JScrollBar scrollBar) {
        this.scrollBar = scrollBar;
    }

    public int getScrollSpeed() {
        return scrollSpeed;
    }

    public void setScrollSpeed(int scrollSpeed) {
        this.scrollSpeed = scrollSpeed;
    }

    public JLabel getDefaultChoose() {
        return defaultChoose;
    }

    public void setDefaultChoose(JLabel defaultChoose) {
        this.defaultChoose = defaultChoose;
    }

    public JTable getTable() {
        return table;
    }

    public void setTable(JTable table) {
        this.table = table;
    }

    public JLabel getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(JLabel currentTime) {
        this.currentTime = currentTime;
    }

    public JLabel getTimeSeparator() {
        return timeSeparator;
    }

    public void setTimeSeparator(JLabel timeSeparator) {
        this.timeSeparator = timeSeparator;
    }

    public JLabel getDurationTime() {
        return durationTime;
    }

    public void setDurationTime(JLabel durationTime) {
        this.durationTime = durationTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public JProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(JProgressBar progressBar) {
        this.progressBar = progressBar;
    }


    public String getIconsPath() {
        return iconsPath;
    }

    public void setIconsPath(String iconsPath) {
        this.iconsPath = iconsPath;
    }

    public String[] getFontFamilyNames() {
        return fontFamilyNames;
    }

    public void setFontFamilyNames(String[] fontFamilyNames) {
        this.fontFamilyNames = fontFamilyNames;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public JLabel getColorChooserLab() {
        return colorChooserLab;
    }

    public void setColorChooserLab(JLabel colorChooserLab) {
        this.colorChooserLab = colorChooserLab;
    }

    @Override
    public JLayeredPane getLayeredPane() {
        return layeredPane;
    }

    @Override
    public void setLayeredPane(JLayeredPane layeredPane) {
        this.layeredPane = layeredPane;
    }

    public Color getChooserColor() {
        return chooserColor;
    }

    public void setChooserColor(Color chooserColor) {
        this.chooserColor = chooserColor;
    }


    public JTextField getFileName() {
        return fileName;
    }

    public void setFileName(JTextField fileName) {
        this.fileName = fileName;
    }

    public Font[] getfList() {
        return fList;
    }

    public void setfList(Font[] fList) {
        this.fList = fList;
    }

    public JButton getSettings() {
        return settings;
    }

    public void setSettings(JButton settings) {
        this.settings = settings;
    }

    public JList getSettingsList() {
        return settingsList;
    }

    public void setSettingsList(JList settingsList) {
        this.settingsList = settingsList;
    }

    public JButton getMinimumWindow() {
        return minimumWindow;
    }

    public void setMinimumWindow(JButton minimumWindow) {
        this.minimumWindow = minimumWindow;
    }

    public JButton getCloseWindow() {
        return closeWindow;
    }

    public void setCloseWindow(JButton closeWindow) {
        this.closeWindow = closeWindow;
    }

    public JButton getPlay() {
        return play;
    }

    public void setPlay(JButton play) {
        this.play = play;
    }

    public MediaPlayer getMp() {
        return mp;
    }

    public void setMp(MediaPlayer mp) {
        this.mp = mp;
    }

    public JButton getVolume() {
        return volume;
    }

    public void setVolume(JButton volume) {
        this.volume = volume;
    }

    public JSlider getVolSlider() {
        return volSlider;
    }

    public void setVolSlider(JSlider volSlider) {
        this.volSlider = volSlider;
    }

    public JButton getPlayType() {
        return playType;
    }

    public void setPlayType(JButton playType) {
        this.playType = playType;
    }

    public boolean isRepeat() {
        return isRepeat;
    }

    public void setRepeat(boolean repeat) {
        isRepeat = repeat;
    }

    public JButton getAddFile() {
        return addFile;
    }

    public void setAddFile(JButton addFile) {
        this.addFile = addFile;
    }

    public String getRltPath() {
        return rltPath;
    }

    public void setRltPath(String rltPath) {
        this.rltPath = rltPath;
    }

    public JFileChooser getFileChooser() {
        return fileChooser;
    }

    public void setFileChooser(JFileChooser fileChooser) {
        this.fileChooser = fileChooser;
    }

}

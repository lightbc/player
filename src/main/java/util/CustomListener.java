package util;

import javafx.scene.media.MediaPlayer;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileView;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

/**
 * Description:自定义监听
 * Package:util
 *
 * @author lightbc
 * @version 1.0
 */
public class CustomListener {
    private CustomPlayerComponent component;

    private CustomFileIO io;

    public CustomListener(CustomPlayerComponent component, CustomFileIO io) {
        this.component = component;
        this.io = io;
    }

    /**
     * 文件名输入框监听
     */
    public void fileNameListener() {
        component.getFileName().addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                JTextField textField = (JTextField) e.getSource();
                String s = textField.getText();
                Font font = textField.getFont();
                if (font.canDisplayUpTo(s) != -1) {
                    Font[] fList = component.getfList();
                    for (int i = 0; i < fList.length; i++) {
                        if (fList[i].canDisplayUpTo(s) == -1) {
                            textField.setFont(fList[i]);
                            break;
                        }
                    }
                }
            }
        });
    }

    /**
     * 设置监听
     */
    public void settingsListener() {
        component.getSettings().addMouseListener(new MouseAdapter() {
            boolean bool = false;

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    JList settingsList = component.getSettingsList();
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
    }

    /**
     * 设置列表监听
     */
    public void settingsListListener() {
        component.getSettingsList().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    JList settingsList = component.getSettingsList();
                    CustomSettings settings = new CustomSettings(component);
                    if (settingsList.getSelectedValue().equals("Skins")) {
                        settings.settingsListChooser(settingsList.getSelectedValue().toString());
                    } else if (settingsList.getSelectedValue().equals("Author")) {
                        settings.settingsListChooser(settingsList.getSelectedValue().toString());
                    }
                }
            }
        });
    }

    /**
     * 窗体最小化监听
     */
    public void minimumWindowListener() {
        component.getMinimumWindow().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    component.setExtendedState(Frame.ICONIFIED);
                }
            }
        });
    }

    /**
     * 关闭窗体监听
     */
    public void closeWindowListener() {
        component.getCloseWindow().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    System.exit(0);
                }
            }
        });
    }

    /**
     * 播放列表监听
     */
    public void playListener() {
        component.getPlay().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    MediaPlayer mp = component.getMp();
                    if (mp != null) {
                        mp.setVolume(component.getVolValue());
                        JButton play = component.getPlay();
                        if (mp.getStatus() == MediaPlayer.Status.READY) {
                            component.setCommonButton(play, "pause.png");
                            mp.play();
                            component.timer();
                        } else if (mp.getStatus() == MediaPlayer.Status.PLAYING) {
                            component.setCommonButton(play, "play.png");
                            mp.pause();
                            component.adjustTimer("PAUSED");
                        } else if (mp.getStatus() == MediaPlayer.Status.PAUSED) {
                            component.setCommonButton(play, "pause.png");
                            mp.play();
                            component.adjustTimer("PLAYING");
                        } else if (mp.getStatus() == MediaPlayer.Status.STOPPED) {
                            component.setCommonButton(play, "pause.png");
                            mp.play();
                            component.timer();
                        }
                    }
                }
            }
        });
    }

    /**
     * 音量监听
     */
    public void volumeListener() {
        component.getVolume().addMouseListener(new MouseAdapter() {
            boolean bool;

            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    JSlider volSlider = component.getVolSlider();
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
    }

    /**
     * 音量滑块监听
     */
    public void volSliderListener() {
        component.getVolSlider().addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                component.setVolValue(component.getVolSlider().getValue());
                int volValue = component.getVolValue();
                component.adjustVolume(volValue);
                JButton volume = component.getVolume();
                if (volValue == 0) {
                    component.setCommonButton(volume, "mute.png");
                } else {
                    component.setCommonButton(volume, "volume.png");
                }
            }
        });
    }

    /**
     * 播放类型监听（是否循环播放）
     */
    public void playTypeListener() {
        component.getPlayType().addMouseListener(new MouseAdapter() {
            boolean isRepeat = component.isRepeat();
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    JButton playType = component.getPlayType();
                    if (!isRepeat) {
                        component.setCommonButton(playType, "repeat.png");
                        isRepeat = true;
                    } else {
                        component.setCommonButton(playType, "norepeat.png");
                        isRepeat = false;
                    }
                    component.setRepeat(isRepeat);
                }
            }
        });
    }

    /**
     * 添加文件监听
     */
    public void addFileListener() {
        component.getAddFile().addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    String rltPath = component.getRltPath();
                    if (rltPath.equals("")) {
                        component.setFileChooser(new JFileChooser("c:/"));
                        ;
                        component.getFileChooser().changeToParentDirectory();
                    } else {
                        component.setFileChooser(new JFileChooser(rltPath));
                    }
                    JFileChooser fileChooser = component.getFileChooser();
                    fileChooser.setFileView(new FileView() {

                        public String getName(File f) {
                            return super.getName(f);
                        }


                        public Icon getIcon(File f) {
                            return component.getFileChooser().getFileSystemView().getSystemIcon(f);
                        }
                    });
                    fileChooser.setDialogTitle("文件");
                    // 开启多选
                    fileChooser.setMultiSelectionEnabled(true);
                    // 文件选择模式-仅文件
                    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                    // 关闭选择"所有文件"
                    fileChooser.removeChoosableFileFilter(fileChooser.getAcceptAllFileFilter());
                    // 设置可选文件类型
                    fileChooser.setFileFilter(new FileNameExtensionFilter("*.mp3", "mp3"));
                    int result = fileChooser.showOpenDialog(null);

                    if (fileChooser.getSelectedFiles() != null && result == JFileChooser.APPROVE_OPTION) {
                        File[] files = fileChooser.getSelectedFiles();
                        // 记录上次访问文件的上级目录
                        component.setRltPath(files[0].getParent());
                        io.writePropFile(component);
                        component.initPlayer();
                        component.player(files[0]);
                    }
                }
            }
        });
    }

    /**
     * 开始监听
     */
    public void startListener() {
        this.addFileListener();
//        this.fileNameListener();
        this.settingsListener();
        this.settingsListListener();
        this.minimumWindowListener();
        this.closeWindowListener();
        this.playListener();
        this.volumeListener();
        this.volSliderListener();
        this.playTypeListener();
    }
}

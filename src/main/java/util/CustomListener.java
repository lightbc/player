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
        component.fileName.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                JTextField textField = (JTextField) e.getSource();
                String s = textField.getText();
                Font font = textField.getFont();
                if (font.canDisplayUpTo(s) != -1) {
                    for (int i = 0; i < component.fList.length; i++) {
                        if (component.fList[i].canDisplayUpTo(s) == -1) {
                            textField.setFont(component.fList[i]);
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
        component.settings.addMouseListener(new MouseAdapter() {
            boolean bool = false;

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (!bool) {
                        component.settingsList.setVisible(true);
                        bool = true;
                    } else {
                        component.settingsList.setVisible(false);
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
        component.settingsList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    CustomSettings settings = new CustomSettings(component);
                    if (component.settingsList.getSelectedValue().equals("Skins")) {
                        settings.settingsListChooser(component.settingsList.getSelectedValue().toString());
                    } else if (component.settingsList.getSelectedValue().equals("Author")) {
                        settings.settingsListChooser(component.settingsList.getSelectedValue().toString());
                    }
                }
            }
        });
    }

    /**
     * 窗体最小化监听
     */
    public void minimumWindowListener() {
        component.minimumWindow.addMouseListener(new MouseAdapter() {
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
        component.closeWindow.addMouseListener(new MouseAdapter() {
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
        component.play.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (component.mp != null) {
                        component.mp.setVolume(component.volValue);
                        if (component.mp.getStatus() == MediaPlayer.Status.READY) {
                            component.setCommonButton(component.play, "pause.png");
                            component.mp.play();
                            component.timer();
                        } else if (component.mp.getStatus() == MediaPlayer.Status.PLAYING) {
                            component.setCommonButton(component.play, "play.png");
                            component.mp.pause();
                            component.adjustTimer("PAUSED");
                        } else if (component.mp.getStatus() == MediaPlayer.Status.PAUSED) {
                            component.setCommonButton(component.play, "pause.png");
                            component.mp.play();
                            component.adjustTimer("PLAYING");
                        } else if (component.mp.getStatus() == MediaPlayer.Status.STOPPED) {
                            component.setCommonButton(component.play, "pause.png");
                            component.mp.play();
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
        component.volume.addMouseListener(new MouseAdapter() {
            boolean bool;

            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (!bool) {
                        component.volSlider.setVisible(true);
                        bool = true;
                    } else {
                        component.volSlider.setVisible(false);
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
        component.volSlider.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                component.volValue = component.volSlider.getValue();
                component.adjustVolume(component.volValue);
                if (component.volValue == 0) {
                    component.setCommonButton(component.volume, "mute.png");
                } else {
                    component.setCommonButton(component.volume, "volume.png");
                }
            }
        });
    }

    /**
     * 播放类型监听（是否循环播放）
     */
    public void playTypeListener() {
        component.playType.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (!component.isRepeat) {
                        component.setCommonButton(component.playType, "repeat.png");
                        component.isRepeat = true;
                    } else {
                        component.setCommonButton(component.playType, "norepeat.png");
                        component.isRepeat = false;
                    }
                }
            }
        });
    }

    /**
     * 添加文件监听
     */
    public void addFileListener() {
        component.addFile.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (component.rltPath.equals("")) {
                        component.fileChooser = new JFileChooser("c:/");
                        component.fileChooser.changeToParentDirectory();
                    } else {
                        component.fileChooser = new JFileChooser(component.rltPath);
                    }
                    component.fileChooser.setFileView(new FileView() {

                        public String getName(File f) {
                            return super.getName(f);
                        }


                        public Icon getIcon(File f) {
                            return component.fileChooser.getFileSystemView().getSystemIcon(f);
                        }
                    });
                    component.fileChooser.setDialogTitle("文件");
                    // 开启多选
                    component.fileChooser.setMultiSelectionEnabled(true);
                    // 文件选择模式-仅文件
                    component.fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                    // 关闭选择"所有文件"
                    component.fileChooser.removeChoosableFileFilter(component.fileChooser.getAcceptAllFileFilter());
                    // 设置可选文件类型
                    component.fileChooser.setFileFilter(new FileNameExtensionFilter("*.mp3", "mp3"));
                    int result = component.fileChooser.showOpenDialog(null);

                    if (component.fileChooser.getSelectedFiles() != null && result == JFileChooser.APPROVE_OPTION) {
                        File[] files = component.fileChooser.getSelectedFiles();
                        // 记录上次访问文件的上级目录
                        component.rltPath = files[0].getParent();
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
        this.fileNameListener();
        this.settingsListener();
        this.settingsListListener();
        this.minimumWindowListener();
        this.closeWindowListener();
        this.playListener();
        this.volumeListener();
        this.volSliderListener();
        this.playTypeListener();
        this.addFileListener();
    }
}

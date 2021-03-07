package player;

import util.*;

/**
 * MP3播放器
 *
 * @author lightbc
 */
public class Mp3Player {

    /**
     * 构造方法
     */
    public Mp3Player() {
        new CustomPlayerComponent().initFrame();
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

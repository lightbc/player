package util;

import javax.swing.*;
import javax.swing.plaf.metal.MetalSliderUI;
import java.awt.*;

/**
 * Description:自定义ui
 * Package:util
 *
 * @author lightbc
 * @version 1.0
 */
public class CustomUI {
    private static CustomUI ui;

    private CustomUI() {
    }

    public static CustomUI getInstance() {
        if (ui == null) {
            ui = new CustomUI();
        }
        return ui;
    }

    /**
     * 重绘滑块
     *
     * @param volSlider
     */
    public void volSliderUI(JSlider volSlider) {
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
    }
}

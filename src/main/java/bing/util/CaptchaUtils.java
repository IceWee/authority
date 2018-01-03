package bing.util;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.Random;

public class CaptchaUtils {

    private static final String WORD = "ACDEFGHJKLMNPQRTUVWXY234679";

    private static final String NUMBER = "0123456789";

    private CaptchaUtils() {
        super();
    }

    /**
     * 生成指定长度验证码字符串
     *
     * @param length
     * @return
     */
    public static String captcha(int length) {
        StringBuilder builder = new StringBuilder();
        int len = WORD.length();
        for (int i = 0; i < length; i++) {
            builder.append(WORD.charAt((int) Math.round(Math.random() * (len - 1))));
        }
        return builder.toString();
    }

    /**
     * 生成指定长度数字验证码字符串
     *
     * @param length
     * @return
     */
    public static String number(int length) {
        StringBuilder builder = new StringBuilder();
        int len = NUMBER.length();
        for (int i = 0; i < length; i++) {
            builder.append(NUMBER.charAt((int) Math.round(Math.random() * (len - 1))));
        }
        return builder.toString();
    }

    /**
     * 生成验证码图片
     *
     * @param captcha
     * @return
     */
    public static BufferedImage captchaImage(String captcha) {
        return captchaImage(120, 50, captcha);
    }

    /**
     * 生成验证码图片
     *
     * @param width
     * @param height
     * @param captcha
     * @return
     */
    public static BufferedImage captchaImage(int width, int height, String captcha) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        Random random = new Random();
        // 设置背景色
        g.setColor(new Color(230, 230, 250));
        // 填充背景
        g.fillRect(0, 0, width, height);
        g.setColor(Color.BLACK);
        // 绘制边缘
        g.drawRect(-1, -1, width + 1, height + 1);
        g.setColor(Color.GRAY);
        // 设置字体,随机选择字体,暂设置8种字体
        Font font = new Font(
                (new String[]{"Arial", "Arial Black", "Arial Italic", "Courier New", "Courier New Bold Italic", "Courier New Italic", "Franklin Gothic Medium", "Franklin Gothic Medium Italic"})[random
                        .nextInt(8)],
                Font.PLAIN, 30);
        g.setFont(font);
        // 定义字体颜色范围
        int red = random.nextInt(160), green = random.nextInt(50), blue = random.nextInt(50);
        // 定义一个无干扰线区间和一个起始位置
        int nor = random.nextInt(50), rsta = random.nextInt(131);
        // 绘制干扰正弦曲线 M:曲线平折度, D:Y轴常量 V:X轴焦距
        int m = random.nextInt(15) + 5, d = random.nextInt(23) + 15, v = random.nextInt(5) + 1;
        double x = 0.0;
        double y = m * Math.sin(Math.toRadians(v * x)) + d;
        double px, py;
        for (int i = 0; i < 131; i++) {
            px = x + 1;
            py = m * Math.sin(Math.toRadians(v * px)) + d;
            if (rsta < i && i < (rsta + nor)) {
                g.setColor(new Color(230, 230, 250));
            } else {
                g.setColor(new Color(red, green, blue));
            }
            // 随机设置像素点宽带(线宽)
            g.setStroke(new BasicStroke((float) (Math.random() + 1.5f)));
            g.draw(new Line2D.Double(x, y, px, py));
            x = px;
            y = py;
        }
        char[] codes = captcha.toCharArray();
        for (int i = 0; i < codes.length; i++) {
            // 旋转图形
            int degree = (random.nextInt(20) - 10) % 360;
            // 将角度转为弧度
            double ang = degree * 0.0174532925;
            g.rotate(ang, width / 2.0, height / 2.0);
            g.setColor(new Color(red, green, blue));
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int gr = random.nextInt(8);
            g.drawString(String.valueOf(codes[i]), 24 * i + 10 + gr, 38);
        }
        g.dispose();
        return image;
    }

}

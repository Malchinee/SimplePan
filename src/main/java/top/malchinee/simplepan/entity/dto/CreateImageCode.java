package top.malchinee.simplepan.entity.dto;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

public class CreateImageCode {
    // 图片宽度
    private int width = 160;
    // 图片高度
    private int height = 40;
    // 验证码字符个数
    private int codeCount = 4;
    // 验证码干扰线数
    private int lineCount = 20;
    /**
     * 验证码
     */
    private String code = null;
    /**
     * 验证码图片buffer
     */
    private BufferedImage buffImg = null;

    Random random = new Random();
    public CreateImageCode() {
        createImage();
    }

    public CreateImageCode(int width, int height) {
        this.height = height;
        this.width = width;
        createImage();
    }

    public CreateImageCode(int width, int height, int codeCount) {
        this.height = height;
        this.width = width;
        this.codeCount = codeCount;
        createImage();
    }

    public CreateImageCode(int width, int height, int codeCount, int lineCount) {
        this.height = height;
        this.width = width;
        this.codeCount = codeCount;
        this.lineCount = lineCount;
        createImage();
    }

    /**
     * 生成图片
     */
    private void createImage() {
        int fontWidth = width / codeCount;  // 字体的宽度
        int fontHeight = height - 5;
        int codeY = height - 8;

        // 图像buffer
        buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = buffImg.getGraphics();
        // 设置背景色
        g.setColor(getRandColor(200, 250));
        g.fillRect(0, 0, width, height);
        // 设置字体
        Font font = new Font("Fixedsys", Font.BOLD, fontHeight);
        g.setFont(font);

        // 设置干扰线
        for(int i = 0; i < lineCount; i ++) {
            int xs = random.nextInt(width);
            int ys = random.nextInt(height);
            int xe = xs + random.nextInt(width);
            int ye = ys + random.nextInt(height);
            g.setColor(getRandColor(1, 255));
            g.drawLine(xs, ys, xe, ye);
        }

        // 添加噪点
        float yawpRate = 0.01f; // 噪声率
        int area = (int) (yawpRate * width * height);
        for(int i = 0; i < area; i ++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            buffImg.setRGB(x, y, random.nextInt(255));
        }

        String str1 = randomStr(codeCount);
        this.code = str1;
        for(int i = 0; i < codeCount; i ++) {
            String strRand = str1.substring(i, i + 1);
            g.setColor(getRandColor(1, 255));
            g.drawString(strRand, i * fontWidth + 3, codeY);
        }
    }

    /**
     * 得到随机字符串
     * @param n
     * @return
     */
    private String randomStr(int n) {
        String str1 = "ABCDEFGHIJKLMNPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        String str2 = "";
        int len = str1.length() - 1;
        double r;
        for(int i = 0; i < n; i ++) {
            r = (Math.random()) * len;
            str2 = str2 + str1.charAt((int)r);
        }
        return str2;
    }

    /**
     * 得到随机颜色
     * @param fc
     * @param bc
     * @return
     */
    private Color getRandColor(int fc, int bc) {
        if(fc > 255) {
            fc = 255;
        }
        if(bc > 255) {
            bc = 255;
        }
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

    private void shearY(Graphics g, int w1, int h1, Color color) {
        int period = random.nextInt(40) + 10;

        boolean borderGap = true;
        int frames = 20;
        int phase = 7;
        for(int i = 0; i < w1; i ++) {
            double d = (double) (period >> 1) * Math.sin((double) i) / (double) period + (6.2831853071795862D * (double) phase) / (double)(w1);
            g.copyArea(i, 0, 1, h1, 0, (int) d);
            if(!borderGap) {
                g.setColor(color);
                g.drawLine(i, (int) d, i, 0);
                g.drawLine(i, (int)d +h1, i, h1);
            }
        }
    }

    public void write(OutputStream sos ) throws IOException {
        ImageIO.write(buffImg, "png", sos);
        sos.close();
    }

    public String getCode() {
        return code.toLowerCase();
    }
}

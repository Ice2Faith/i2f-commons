package i2f.commons.component.qrcode;

import i2f.commons.component.qrcode.core.QrCodeWorker;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.OutputStream;

public class QrCodeUtil {
    public static BufferedImage toQrCode(String content,int size) throws Exception {
        BufferedImage image=new QrCodeWorker()
                .setCodeSize(size)
                .makeQrCode(content,null);
        return image;
    }
    public static void toQrCode(String content, int size, OutputStream os) throws Exception {
        BufferedImage image=new QrCodeWorker()
                .setCodeSize(size)
                .makeQrCode(content,null);
        ImageIO.write(image,"JPG",os);
    }
    public static BufferedImage toQrCode(String content,int size,
                                         String logoImg,int logoSize) throws Exception {
        BufferedImage image=new QrCodeWorker()
                .setCodeSize(size)
                .setLogoSize(logoSize)
                .makeQrCode(content,logoImg);
        return image;
    }
    public static void toQrCode(String content,int size,
                                String logoImg,int logoSize,
                                OutputStream os) throws Exception {
        BufferedImage image=new QrCodeWorker()
                .setCodeSize(size)
                .setLogoSize(logoSize)
                .makeQrCode(content,logoImg);
        ImageIO.write(image,"JPG",os);
    }
    public static String parseQrCode(String fileName) throws Exception {
        return new QrCodeWorker().parseQrCode(new File(fileName));
    }
    public static String parseQrCode(File file) throws Exception {
        return new QrCodeWorker().parseQrCode(file);
    }
}

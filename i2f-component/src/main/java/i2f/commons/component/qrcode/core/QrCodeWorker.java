package i2f.commons.component.qrcode.core;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import i2f.commons.core.utils.safe.CheckUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.Hashtable;

public class QrCodeWorker {
    private int codeSize=300;
    private int logoSize=60;
    private String charset ="UTF-8";
    public QrCodeWorker setCodeSize(int size){
        this.codeSize=size;
        return this;
    }
    public QrCodeWorker setLogoSize(int size){
        this.logoSize=size;
        return this;
    }
    public QrCodeWorker setCharset(String charset){
        this.charset =charset;
        return this;
    }
    public BufferedImage makeQrCode(String content, String logoPath) throws Exception {
        //设置属性
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        //容错，在之后的设置Logo时，就是利用此容错来的，这个模式H下，最大可被遮挡30%
        /**com.google.zxing.EncodeHintType：编码提示类型,枚举类型
         * EncodeHintType.CHARACTER_SET：设置字符编码类型
         * EncodeHintType.ERROR_CORRECTION：设置误差校正
         *      ErrorCorrectionLevel：误差校正等级，L = ~7% correction、M = ~15% correction、Q = ~25% correction、H = ~30% correction
         *      不设置时，默认为 L 等级，等级不一样，生成的图案不同，但扫描的结果是一样的
         * EncodeHintType.MARGIN：设置二维码边距，单位像素，值越小，二维码距离四周越近
         * */
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, charset);
        hints.put(EncodeHintType.MARGIN, 1);
        //获得二维码矩阵
        BitMatrix bitMatrix = new MultiFormatWriter()
                .encode(content,
                    BarcodeFormat.QR_CODE,
                        codeSize,
                        codeSize,
                        hints);
        //绘制二维码到图片
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000
                        : 0xFFFFFFFF);
            }
        }
        if (CheckUtil.isEmptyStr(logoPath,true)) {
            return image;
        }
        // 插入Logo
        File logoImgFile=new File(logoPath);
        if(!logoImgFile.exists() && !logoImgFile.isFile()){
            return image;
        }
        Image logoImg=ImageIO.read(logoImgFile);
        int srcLogoImgWidth=logoImg.getWidth(null);
        int srcLogoImgHeight=logoImg.getHeight(null);
        //缩放Logo
        if(srcLogoImgHeight>logoSize){
            srcLogoImgHeight=logoSize;
        }
        if(srcLogoImgWidth>logoSize){
            srcLogoImgWidth=logoSize;
        }
        logoImg=logoImg.getScaledInstance(logoSize,logoSize,BufferedImage.TYPE_INT_RGB);
        //绘制最终二维码
        Graphics2D graph= image.createGraphics();
        int posX=(codeSize-srcLogoImgWidth)/2;
        int posY=(codeSize-srcLogoImgHeight)/2;
        graph.drawImage(logoImg,posX,posY,srcLogoImgWidth,srcLogoImgHeight,null);
        //绘制Logo圆角边框
        Shape shape=new RoundRectangle2D.Float(posX,posY,srcLogoImgWidth,srcLogoImgHeight,6,6);
        graph.setStroke(new BasicStroke(3f));
        graph.draw(shape);
        graph.dispose();

        return image;
    }

    public String parseQrCode(InputStream is) throws Exception {
        BufferedImage image=ImageIO.read(is);
        if(image==null){
            return null;
        }
        return parseQrCode(image);
    }

    public String parseQrCode(File file) throws Exception {
        BufferedImage image=ImageIO.read(file);
        if(image==null){
            return null;
        }
        return parseQrCode(image);
    }

    public String parseQrCode(BufferedImage image) throws Exception {
        BufferedImageLuminanceSource source=new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap=new BinaryBitmap(new HybridBinarizer(source));
        Hashtable<DecodeHintType,Object> hints=new Hashtable<DecodeHintType, Object>();
        hints.put(DecodeHintType.CHARACTER_SET, charset);
        Result result=new MultiFormatReader().decode(bitmap,hints);
        return result.getText();
    }
}

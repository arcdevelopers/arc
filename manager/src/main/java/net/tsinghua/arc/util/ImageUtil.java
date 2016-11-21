package net.tsinghua.arc.util;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by ji on 16-11-16.
 */
public class ImageUtil {
    public static void makeSmallImage(InputStream srcImageFile, String dstImageFileName) throws Exception {
        FileOutputStream fileOutputStream = null;
        JPEGImageEncoder encoder = null;
        BufferedImage tagImage = null;
        Image srcImage = null;
        try {
            srcImage = ImageIO.read(srcImageFile);
            int srcWidth = srcImage.getWidth(null);//原图片宽度
            int srcHeight = srcImage.getHeight(null);//原图片高度
            int dstMaxSize = 120;//目标缩略图的最大宽度/高度，宽度与高度将按比例缩写
            int dstWidth = srcWidth;//缩略图宽度
            int dstHeight = srcHeight;//缩略图高度
            float scale = 0;
            //计算缩略图的宽和高
            if (srcWidth > dstMaxSize) {
                dstWidth = dstMaxSize;
                scale = (float) srcWidth / (float) dstMaxSize;
                dstHeight = Math.round((float) srcHeight / scale);
            }
            srcHeight = dstHeight;
            if (srcHeight > dstMaxSize) {
                dstHeight = dstMaxSize;
                scale = (float) srcHeight / (float) dstMaxSize;
                dstWidth = Math.round((float) dstWidth / scale);
            }
            //生成缩略图
            tagImage = new BufferedImage(dstWidth, dstHeight, BufferedImage.TYPE_INT_RGB);
            tagImage.getGraphics().drawImage(srcImage, 0, 0, dstWidth, dstHeight, null);
            fileOutputStream = new FileOutputStream(dstImageFileName);
            encoder = JPEGCodec.createJPEGEncoder(fileOutputStream);
            encoder.encode(tagImage);
            fileOutputStream.close();
            fileOutputStream = null;
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (Exception e) {
                }
                fileOutputStream = null;
            }
            encoder = null;
            tagImage = null;
            srcImage = null;
        }
    }
}

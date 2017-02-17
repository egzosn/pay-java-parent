package in.egan.pay.common.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;


/**
 * 二维码生成工具
 * @author  egan
 * @email egzosn@gmail.com
 * @date  2017/2/7 10:35
 */
public class MatrixToImageWriter {


	   private static final int BLACK = 0xFF000000;
	   private static final int WHITE = 0xFFFFFFFF;
	 
	   private MatrixToImageWriter() {}

    /**
	 * 根据二维矩阵的碎片 生成对应的二维码图像缓冲
	 * @param matrix  二维矩阵的碎片 包含 宽高 行，字节
	 * @see com.google.zxing.common.BitMatrix
	 * @return
     */
	   public static BufferedImage toBufferedImage(BitMatrix matrix) {
	     int width = matrix.getWidth();
	     int height = matrix.getHeight();
	     BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	     for (int x = 0; x < width; x++) {
	       for (int y = 0; y < height; y++) {
	         image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
	       }
	     }
	     return image;
	   }


	/**
	 * 二维码生成文件
	 * @param matrix
	 * @param format
	 * @param file
	 * @throws IOException
	 */
	   public static void writeToFile(BitMatrix matrix, String format, File file)
	       throws IOException {
	     BufferedImage image = toBufferedImage(matrix);
	     if (!ImageIO.write(image, format, file)) {
	       throw new IOException("Could not write an image of format " + format + " to " + file);
	     }
	   }


	/**
	 * 二维码生成流
	 * @param matrix
	 * @param format
	 * @param stream
	 * @throws IOException
	 */
	   public static void writeToStream(BitMatrix matrix, String format, OutputStream stream)
	       throws IOException {
	     BufferedImage image = toBufferedImage(matrix);
	     if (!ImageIO.write(image, format, stream)) {
	       throw new IOException("Could not write an image of format " + format);
	     }
	   }
	   
	   
	   /**
	    * 二维码信息写成JPG文件
	    * @param content
	    * @param fileUrl
	    */
	  public static void writeInfoToJpgFile(String content, String fileUrl){
		    MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
		    Map hints = new HashMap();
		    hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		    try {
				BitMatrix bitMatrix = multiFormatWriter.encode(content,
						BarcodeFormat.QR_CODE, 250, 250, hints);
				File file1 = new File(fileUrl);
				MatrixToImageWriter.writeToFile(bitMatrix, "jpg", file1);
			} catch (Exception e) {
				e.printStackTrace();
			}	 
	   }
	  
	  
	  /**
	   * 二维码信息写成JPG BufferedImage
	   * @param content
	   * @return
	   */
	  public static BufferedImage writeInfoToJpgBuff(String content){
		    BufferedImage re=null;
		  
		    MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
		    Map hints = new HashMap();
		    hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		    try {
				BitMatrix bitMatrix = multiFormatWriter.encode(content,
						BarcodeFormat.QR_CODE, 250, 250, hints);
				re=MatrixToImageWriter.toBufferedImage(bitMatrix);
			} catch (Exception e) {
				e.printStackTrace();
			}	 
		    
		  return re;
	  }
	 
	   
	   
}

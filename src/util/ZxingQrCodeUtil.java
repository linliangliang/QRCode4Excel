package util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class ZxingQrCodeUtil {

	/*
	 * 定义二维码的宽高
	 */
	private static int WIDTH = 300;
	private static int HEIGHT = 300;
	private static String FORMAT = "png";// 二维码格式
	// 生成二维码

	public static void createZxingqrCode(String content, String currentDayPath, String imageName) {
		if (content == null || currentDayPath == null || null == imageName) {
			return;
		}

		// 定义二维码参数
		Map hints = new HashMap();
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");// 设置编码
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);// 设置容错等级
		hints.put(EncodeHintType.MARGIN, 1);// 设置边距默认是5,这里设置为0

		try {
			BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, WIDTH, HEIGHT, hints);
			Path path = new File(currentDayPath + "\\" + imageName).toPath();
			MatrixToImageWriter.writeToPath(bitMatrix, FORMAT, path);// 写到指定路径下

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// 读取二维码
	public static void readZxingQrCode() {
		MultiFormatReader reader = new MultiFormatReader();
		File file = new File("D:\\qr.png");
		try {
			BufferedImage image = ImageIO.read(file);
			BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)));
			Map hints = new HashMap();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");// 设置编码
			Result result = reader.decode(binaryBitmap, hints);
			System.out.println("解析结果:" + result.toString());
			System.out.println("二维码格式:" + result.getBarcodeFormat());
			System.out.println("二维码文本内容:" + result.getText());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
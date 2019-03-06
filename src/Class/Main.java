package Class;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import java.util.Date;

import javax.imageio.ImageIO;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import entity.Data;
import util.ZxingQrCodeUtil;
import util.addDate;
import util.fileUtil;

import java.util.Calendar;

public class Main {

	static String rootPath = "D:\\tempImage";// 根目录
	static String currentMonthPath = rootPath + "\\" + getYearAndMonth();// 当月文件夹

	public static void main(String[] args) {

		String currentDayPath = "";

		// 根据设备创建文件夹
		createDir();

		System.out.println("正在打印中...");
		for (int k = 1; k <= getDaysOfMonth(); k++) {// 31天
			// 第k天的二维码打印
			// 新建第k天的文件夹

			for (int j = 0; j < Data.assetNumbers.length; j++) {// 三台设备
				currentDayPath = currentMonthPath + "\\" + Data.assetNumbers[j] + "\\" + k;
				if (!fileUtil.fileExists(currentDayPath)) {// 文件路径不存在
					fileUtil.createDir(currentDayPath);
				}

				for (int i = 0; i < Data.assetEquipmentMaintainPlan[j].length; i++) {// 第j台设备有i个保养计划

					String tempName = addDate.addDateString(Data.assetEquipmentMaintainPlan[j][i], k);
					try {
						ZxingQrCodeUtil.createZxingqrCode(tempName, currentDayPath, util(tempName) + ".png");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

		}
		System.out.println("完成打印");

		// 将导出的excel添加到excel中
		for (int i = 0; i < Data.assetEquipmentMaintainPlan.length; i++) {
			importImage2Excel(Data.assetNumbers[i]);
		}

	}

	private static void createDir() {
		// 新建文件夹
		if (!fileUtil.fileExists(rootPath)) {// 文件路径不存在

			fileUtil.createDir(rootPath);
		}
		// 新建文件夹
		if (!fileUtil.fileExists(currentMonthPath)) {// 文件路径不存在

			fileUtil.createDir(currentMonthPath);
		}

		// 根据指定设备设备创建文件夹
		for (int i = 0; i < Data.assetNumbers.length; i++) {
			if (!fileUtil.fileExists(currentMonthPath + "\\" + Data.assetNumbers[i])) {// 文件路径不存在
				fileUtil.createDir(currentMonthPath + "\\" + Data.assetNumbers[i]);
			}
		}
	}

	/**
	 * 将文件名中的/化成_
	 * 
	 * @param name
	 * @return
	 */
	private static String util(String name) {
		return name.replaceAll("/", "_");
	}

	/**
	 * 获取当前月的天数
	 * 
	 * @return
	 */
	private static int getDaysOfMonth() {

		Calendar c1 = Calendar.getInstance();

		Calendar c = Calendar.getInstance();
		c.set(c1.get(Calendar.YEAR), (c1.get(Calendar.MONTH) + 1), 0); // 输入类型为int类型

		int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
		System.out.println("dayOfMonth:" + dayOfMonth);
		return dayOfMonth;
	}

	/**
	 * 获取年和月的拼接字符串:2019_03
	 * 
	 * @return
	 */
	private static String getYearAndMonth() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String[] tem = sdf.format(new Date()).split("-");

		return tem[0] + "_" + tem[1];
	}

	/**
	 * 将生成的图片导入到excel
	 * 
	 * @param assertNumber
	 *            用设备号来当文件名,和文件夹的名字
	 */
	private static void importImage2Excel(String assertNumber) {
		String excelName = currentMonthPath + "\\" + assertNumber + ".xls";
		HSSFWorkbook wb = new HSSFWorkbook();
		/*
		 * // 创建一个sheet HSSFSheet sheet = wb.createSheet("sheet01");
		 * sheet.setColumnWidth(0, 4500);// 设置第一列列框 sheet.setColumnWidth(1, 4500);//
		 * 设置第二列列框 // 利用HSSFPatriarch将图片写入EXCEL HSSFPatriarch patriarch =
		 * sheet.createDrawingPatriarch();
		 */
		try {
			for (int i = 1; i <= getDaysOfMonth(); i++) { // 有n天的图片需要导入

				// 便利某个天的所有图片
				File root = new File(currentMonthPath + "\\" + assertNumber + "\\" + i);
				File[] files = root.listFiles();

				HSSFSheet sheet = wb.createSheet("" + i);
				sheet.setColumnWidth(0, 8000);// 设置第一列列框
				sheet.setColumnWidth(1, 4500);// 设置第二列列框
				sheet.setDefaultRowHeight((short) 1700);

				/*// 添加表头
				HSSFRow rowHead = sheet.createRow(0);
				rowHead.createCell((short) 0).setCellValue("文件名");
				// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				rowHead.createCell((short) 1).setCellValue("二维码");*/

				for (int j = 0; j < files.length; j++) {
					File file = files[j];
					System.out.println(file.getAbsolutePath());

					// 导入文件名
					HSSFRow row = sheet.createRow(j);
					HSSFCell cell = row.createCell((short) 0);
					// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(file.getName());

					// 导入图片

					FileOutputStream fileOut = null;
					BufferedImage bufferImg = null;// 图片
					System.out.println("开始导入第" + i + "天的图片");
					// 先把读进来的图片放到一个ByteArrayOutputStream中，以便产生ByteArray
					ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
					// 将图片读到BufferedImage

					bufferImg = ImageIO.read(new File(file.getAbsolutePath()));

					// 将图片写入流中
					ImageIO.write(bufferImg, "png", byteArrayOut);
					// 创建一个sheet

					// 利用HSSFPatriarch将图片写入EXCEL
					HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
					/**
					 * 该构造函数有8个参数 前四个参数是控制图片在单元格的位置，分别是图片距离单元格left，top，right，bottom的像素距离
					 * 后四个参数，前连个表示图片左上角所在的cellNum和 rowNum，后天个参数对应的表示图片右下角所在的cellNum和 rowNum，
					 * excel中的cellNum和rowNum的index都是从0开始的
					 * 
					 */
					// 图片一导出到单元格B2中
					HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) 1, j , (short) 2, j + 1);
					// 插入图片
					patriarch.createPicture(anchor,
							wb.addPicture(byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
					// 生成的excel文件地址
					fileOut = new FileOutputStream(excelName);
					// 写入excel文件
					wb.write(fileOut);
				}
				/*
				 * for (File file : files) {
				 * 
				 * }
				 */
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

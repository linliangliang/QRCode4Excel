package util;

import java.io.File;

public class fileUtil {

	/**
	 * 创建文件夹
	 * 
	 * @param dir
	 */
	public static void createDir(String dir) {
		File fileDir = new File(dir);
		if (!fileDir.isFile() && !fileDir.exists()) {
			try {
				fileDir.mkdir();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	/**
	 * 判断文件路径是否存在
	 * 
	 * @param dir
	 */
	public static boolean fileExists(String dir) {
		File fileDir = new File(dir);
		if (fileDir.isDirectory() && fileDir.exists()) {
			return true;

		}
		return false;
	}

}

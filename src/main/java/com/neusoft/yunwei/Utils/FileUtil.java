/**
 * 
 */
package com.neusoft.yunwei.Utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;


import com.neusoft.yunwei.exception.AppException;


/**
 * @author Delen
 * @Created 2014年12月29日
 */
public class FileUtil {

	/**
	 * 
	 * @return
	 */
	public static String getAppDirectory() {
		File f = new File(".");
		String path = f.getAbsolutePath();
		// logger.debug("path = "+path);
		return path.substring(0, path.length() - 1);
	}

	public static void unZip(String zipfile, String destDir) {
		destDir = destDir.endsWith("/") ? destDir : destDir + "/";
		byte b[] = new byte[1024];
		int length;
		ZipFile zipFile;
		try {
			zipFile = new ZipFile(new File(zipfile));
			Enumeration enumeration = zipFile.entries();
			ZipEntry zipEntry = null;
			while (enumeration.hasMoreElements()) {
				zipEntry = (ZipEntry) enumeration.nextElement();
				File loadFile = new File(destDir + zipEntry.getName());
				if (zipEntry.isDirectory()) {
					// 这段都可以不要，因为每次都貌似从最底层开始遍历的
					loadFile.mkdirs();
				} else {
					if (!loadFile.getParentFile().exists())
						loadFile.getParentFile().mkdirs();
					try (OutputStream outputStream = new FileOutputStream(loadFile); InputStream inputStream = zipFile.getInputStream(zipEntry)) {
						while ((length = inputStream.read(b)) > 0)
							outputStream.write(b, 0, length);
					}
				}
			}
			System.out.println(" unZip file : " + zipfile + " done ");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static public void deletePath(String path) throws AppException {
		File item = new File(path);
		if (item.exists() && item.isDirectory()) {// 判断是文件还是目录
			if (item.listFiles().length == 0) {// 若目录下没有文件则直接删除
				item.delete();
			} else {// 若有则把文件放进数组，并判断是否有下级目录
				File delFile[] = item.listFiles();
				int i = item.listFiles().length;
				for (int j = 0; j < i; j++) {
					if (delFile[j].isDirectory()) {
						deletePath(delFile[j].getAbsolutePath());// 递归调用del方法并取得子目录路径
					}
					delFile[j].delete();// 删除文件
				}
			}
		}
	}

	/**
	 * 
	 * @return
	 */
	public static String getClasspath() {
		String path = FileUtil.class.getResource("/").getPath();
		// logger.debug("path = "+path);
		return path;
	}

	/**
	 * 
	 * @param file
	 * @return
	 */
	public static ByteBuffer readFile(File file) throws Exception {
		ByteBuffer mBuf = null;
		FileChannel fiChannel = null;
		try {
			fiChannel = new FileInputStream(file).getChannel();
			mBuf = ByteBuffer.allocate((int) fiChannel.size());
			fiChannel.read(mBuf);
			mBuf.flip();
		} catch (Exception e) {
			new Exception("readFile:" + file + " fail:", e);
		} finally {
			if (file != null) {
				file = null;
			}
			if (fiChannel != null) {
				fiChannel.close();
				fiChannel = null;
			}
		}
		return mBuf;
	}

	/**
	 * 
	 * @param src
	 *
	 * @throws Exception
	 */
	public static void saveFile(ByteBuffer src, File file) throws Exception {
		FileChannel foChannel = null;
		try {
			foChannel = new FileOutputStream(file).getChannel();
			foChannel.write(src);
			// foChannel.close();
			// foChannel = null;
		} catch (Exception e) {
			throw new Exception("saveFile:" + file + " fail", e);
		} finally {
			if (src != null) {
				src.clear();
				src = null;
			}
			if (foChannel != null) {
				foChannel.close();
				foChannel = null;
			}
		}
	}

	/**
	 * 
	 * @param src
	 * @param filename
	 * @throws Exception
	 */
	public static void saveFile(ByteBuffer src, String filename) throws Exception {
		FileChannel foChannel = null;
		try {
			foChannel = new FileOutputStream(filename).getChannel();
			foChannel.write(src);
			// foChannel.close();
			// foChannel = null;
		} catch (Exception e) {
			throw new Exception("", e);
		} finally {
			if (src != null) {
				src.clear();
				src = null;
			}
			if (foChannel != null) {
				foChannel.close();
				foChannel = null;
			}
		}
	}

	static public File createPathByCheck(String path) throws AppException {
		path = formatPath(path);
		File res = new File(path);
		if (!res.exists()) {
			try {
				res.mkdirs();
				System.out.println("=== create path:" + path);
			} catch (Exception e) {
				throw new AppException("Can't not create path:" + path, e);
			}
		} else if (!res.isDirectory()) {
			throw new AppException("Is not file path:" + path);
		}

		return res;
	}

	static public String formatPath(String path) throws AppException {
		if (path == null) {
			throw new AppException("path is null!!!");
		}
		if (!path.endsWith("/")) {
			path = path + "/";
		}
		return path;
	}

	public static void zip(File inputFile, String outputZipFileName) throws AppException {
		try (ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outputZipFileName)); BufferedOutputStream bo = new BufferedOutputStream(out)) {
			zip(out, inputFile, inputFile.getName(), bo);
		} catch (Exception e) {
			throw new AppException("zip path: " + inputFile.getPath() + " error!!!", e);
		}
		System.out.println(" zip file : " + outputZipFileName + " done ");
	}

	public static void zip(ZipOutputStream out, File f, String base, BufferedOutputStream bo) throws Exception {
		if (f.isDirectory()) {
			File[] fl = f.listFiles();
			if (fl.length == 0) {
				out.putNextEntry(new ZipEntry(base + "/"));
			}
			for (int i = 0; i < fl.length; i++) {
				zip(out, fl[i], base + "/" + fl[i].getName(), bo); // 递归遍历子文件夹
			}
		} else {
			out.putNextEntry(new ZipEntry(base));
			try (FileInputStream in = new FileInputStream(f); BufferedInputStream bi = new BufferedInputStream(in)) {
				int b;
				while ((b = bi.read()) != -1) {
					bo.write(b);
				}
				bo.flush();
			}
		}
	}

}

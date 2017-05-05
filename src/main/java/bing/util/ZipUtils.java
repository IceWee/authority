package bing.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang3.StringUtils;

/**
 * <p>
 * Compress/Uncompress Utils
 * </p>
 *
 * @author IceWee
 * @see java.util.zip.ZipOutputStream
 * @see java.util.zip.ZipFile
 * @see java.util.zip.ZipEntry
 */
public class ZipUtils {

	private static final int CACHE_SIZE = 1024;

	private ZipUtils() {
		super();
	}

	/**
	 * <p>
	 * Compress a folder to a zip file
	 * </p>
	 * 
	 * @param sourceFolder which to be compressed
	 * @param zipFolder compress file created folder
	 * @param zipName compress file name
	 * @throws IOException
	 */
	public static void zip(String sourceFolder, String zipFolder, String zipName) {
		String destFolder = StringUtils.replace(zipFolder, "\\", "/");
		destFolder = StringUtils.appendIfMissing(destFolder, "/");
		String zipFilePath = destFolder + zipName;
		zip(sourceFolder, zipFilePath);
	}

	/**
	 * <p>
	 * Compress a folder to a zip file
	 * </p>
	 * 
	 * @param sourceFolder which to be compressed
	 * @param zipFilePath compress file created path
	 * @throws IOException
	 */
	public static void zip(String sourceFolder, String zipFilePath) {
		OutputStream out = null;
		BufferedOutputStream bos = null;
		ZipOutputStream zos = null;
		try {
			createParentDirs(new File(zipFilePath));
			out = new FileOutputStream(zipFilePath);
			bos = new BufferedOutputStream(out);
			zos = new ZipOutputStream(bos);
			File file = new File(sourceFolder);
			String basePath;
			if (file.isDirectory()) {
				basePath = file.getPath();
			} else {
				basePath = file.getParent();
			}
			zipFile(file, basePath, zos);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (zos != null) {
				try {
					zos.closeEntry();
					zos.close();
				} catch (IOException e) {
				}
			}
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}
	}

	/**
	 * <p>
	 * Uncompress a zip file
	 * </p>
	 * 
	 * @param zipFilePath path of zip file
	 * @param unzipFolder folder of uncompressed
	 * @throws IOException
	 */
	public static void unzip(final String zipFilePath, final String unzipFolder) {
		ZipFile zipFile = null;
		try {
			String destFolder = StringUtils.replace(unzipFolder, "\\", "/");
			destFolder = StringUtils.appendIfMissing(unzipFolder, "/");
			zipFile = new ZipFile(zipFilePath);
			Enumeration<?> emu = zipFile.entries();
			BufferedInputStream bis = null;
			FileOutputStream fos = null;
			BufferedOutputStream bos = null;
			File file;
			ZipEntry entry;
			byte[] cache = new byte[CACHE_SIZE];
			while (emu.hasMoreElements()) {
				entry = (ZipEntry) emu.nextElement();
				if (entry.isDirectory()) {
					new File(destFolder + entry.getName()).mkdirs();
					continue;
				}
				bis = new BufferedInputStream(zipFile.getInputStream(entry));
				file = new File(destFolder + entry.getName());
				createParentDirs(file);
				fos = new FileOutputStream(file);
				bos = new BufferedOutputStream(fos, CACHE_SIZE);
				int bytes;
				while ((bytes = bis.read(cache, 0, CACHE_SIZE)) != -1) {
					fos.write(cache, 0, bytes);
				}
				bos.flush();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (zipFile != null) {
					zipFile.close();
				}
			} catch (IOException e) {
			}
		}
	}

	/**
	 * <p>
	 * Recursion compress a file/folder to zip file
	 * </p>
	 * 
	 * @param parentFile
	 * @param parentFile
	 * @param zos
	 * @throws IOException
	 */
	private static void zipFile(File parentFile, String basePath, ZipOutputStream zos) {
		if (parentFile.isDirectory()) {
			File[] files = parentFile.listFiles();
			for (File file : files) {
				zipFile(file, basePath, zos);
			}
		} else {
			String pathName = parentFile.getPath().substring(basePath.length() + 1);
			InputStream in = null;
			BufferedInputStream bis = null;
			try {
				in = new FileInputStream(parentFile);
				bis = new BufferedInputStream(in);
				zos.putNextEntry(new ZipEntry(pathName));
				int bytes;
				byte[] cache = new byte[CACHE_SIZE];
				while ((bytes = bis.read(cache, 0, CACHE_SIZE)) != -1) {
					zos.write(cache, 0, bytes);
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			} finally {
				if (bis != null) {
					try {
						bis.close();
					} catch (IOException e) {
					}
				}
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
					}
				}
			}
		}
	}

	/**
	 * <p>
	 * create parent directories of the file
	 * </p>
	 * 
	 * @param file
	 */
	private static void createParentDirs(File file) {
		File parentFile = file.getParentFile();
		if (parentFile != null && !parentFile.exists()) {
			parentFile.mkdirs();
		}
	}

}
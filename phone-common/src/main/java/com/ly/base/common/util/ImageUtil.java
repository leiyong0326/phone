package com.ly.base.common.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;

import com.ly.base.common.em.ext.FileTypeEnum;

/**
 * 图片压缩工具类
 * 
 * @author LeiYong
 */
@SuppressWarnings("restriction")
public class ImageUtil {
	public static final int DEFAULT_WIDTH = 300;
	public static final int DEFAULT_HEIGHT = 200;
	public static final boolean DEFAULT_RATE = true;
	public static final float QUALITY = 0.5f;

	/**
	 * 图片压缩
	 * 
	 * @param inputDir
	 *            原图路径
	 * @param outputDir
	 *            压缩后路径
	 * @param inputFileName
	 *            原图文件名
	 * @param outputFileName
	 *            压缩后文件名
	 * @param width
	 *            压缩宽
	 * @param height
	 *            压缩高
	 * @param gp
	 *            是否等比压缩
	 * @deprecated 务必不要使用org.sun包下的类,重新实现后删除此段
	 * @return
	 */
	public static Boolean compressPic(String inputDir, String outputDir, String inputFileName, String outputFileName,
			int tarWidth, int tarHeight, boolean gp) {
		// 获得源文件
		File inputFile = new File(inputDir + inputFileName);
		if (!inputFile.isFile()) {
			return false;
		}
		File outputFile = new File(outputDir + outputFileName);
		return compressPic(inputFile, outputFile, tarWidth, tarHeight, gp);
	}

	/**
	 * 图片压缩
	 * 
	 * @param inputFile
	 *            输入文件
	 * @param outputFile
	 *            输出文件
	 * @deprecated 务必不要使用org.sun包下的类,重新实现后删除此段
	 * @return
	 */
	public static Boolean compressPic(File inputFile, File outputFile) {
		return compressPic(inputFile, outputFile, DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_RATE);
	}

	/**
	 * 图片压缩
	 * 
	 * @param inputFile
	 *            输入文件
	 * @param outputFile
	 *            输出文件
	 * @param gp
	 *            是否等比压缩
	 * @deprecated 务必不要使用org.sun包下的类,重新实现后删除此段
	 * @return
	 */
	public static Boolean compressPic(File inputFile, File outputFile, boolean gp) {
		return compressPic(inputFile, outputFile, DEFAULT_WIDTH, DEFAULT_HEIGHT, gp);
	}

	/**
	 * 图片压缩
	 * 
	 * @param inputFile
	 *            原图
	 * @param outputFile
	 *            目标文件
	 * @param tarWidth
	 *            目标宽度
	 * @param tarHeight
	 *            目标高度
	 * @param gp
	 *            是否等比压缩
	 * @deprecated 务必不要使用org.sun包下的类,重新实现后删除此段
	 * @return
	 */
	public static Boolean compressPic(File inputFile, File outputFile, int tarWidth, int tarHeight, boolean gp) {
		// 获得源文件
		if (!inputFile.isFile()) {
			return false;
		}
		// 判断图片格式是否正确
		if (!FileUtil.checkFileType(inputFile, FileTypeEnum.Img)) {
			return false;
		}
		try {
			Image img = ImageIO.read(inputFile);
			int width = img.getWidth(null);
			int height = img.getHeight(null);
			if (width > 0) {
				int newWidth, newHeight;
				// 判断是否是等比缩放
				if (gp == true) {
					if (tarWidth == 0 && tarHeight == 0) {
						return false;
					}
					int wdRate = 0, hgRate = 0;
					// 为等比缩放计算输出的图片宽度及高度
					if (tarWidth > 0) {
						wdRate = width / tarWidth;
					}
					if (tarHeight > 0) {
						hgRate = height / tarHeight;
					}
					// 根据缩放比率大的进行缩放控制
					if (wdRate > hgRate) {
						newWidth = tarWidth;
						newHeight = (int) (img.getHeight(null) / wdRate);
					} else {
						newHeight = tarHeight;
						newWidth = (int) (img.getWidth(null) / hgRate);
					}
				} else {
					if (tarWidth == 0 || tarHeight == 0) {
						return false;
					}
					newWidth = tarWidth; // 输出的图片宽度
					newHeight = tarHeight; // 输出的图片高度
				}
				BufferedImage tag = new BufferedImage((int) newWidth, (int) newHeight, BufferedImage.TYPE_INT_RGB);

				// Image.SCALE_SMOOTH 的缩略算法 生成缩略图片的平滑度的 优先级比速度高 生成的图片质量比较好 但速度慢
				tag.getGraphics().drawImage(img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH), 0, 0, null);
				if (!FileUtil.createFolder(outputFile.getParent())) {
					return false;
				}
				FileOutputStream out = new FileOutputStream(outputFile);
				// JPEGImageEncoder可适用于其他图片类型的转换
//				JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
//				encoder.encode(tag);
			} else {
				return false;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
//		} catch (ImageFormatException e) {
//			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 压缩图片，不改变图片尺寸情况下改变存储大小（清晰度）
	 * 
	 * @param srcFilePath
	 * @param descFilePath
	 * @return
	 * @throws IOException
	 */
	public static boolean resize(String srcFilePath, String descFilePath, float quality) throws IOException {
		// 获得源文件
		File inputFile = new File(srcFilePath);
		if (!inputFile.isFile()) {
			return false;
		}
		File outputFile = new File(descFilePath);
		return resize(inputFile, outputFile, quality);
	}

	/**
	 * 压缩图片，不改变图片尺寸情况下改变存储大小（清晰度）
	 * 
	 * @param srcFilePath
	 * @param descFilePath
	 * @return
	 * @throws IOException
	 */
	public static Boolean resize(File inputFile, File outputFile) throws IOException {
		return resize(inputFile, outputFile, QUALITY);
	}

	/**
	 * 压缩图片，不改变图片尺寸情况下改变存储大小（清晰度）
	 * 
	 * @param srcFilePath
	 * @param descFilePath
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("static-access")
	public static boolean resize(File inputFile, File outputFile, float quality) throws IOException {
		// 获得源文件
		if (!inputFile.isFile()) {
			return false;
		}
		// 判断图片格式是否正确
		if (!FileUtil.checkFileType(inputFile, FileTypeEnum.Img)) {
			return false;
		}
		BufferedImage src = null;
		FileOutputStream out = null;
		ImageWriter imgWrier;
		ImageWriteParam imgWriteParams;

		// 指定写图片的方式为 jpg
		imgWrier = ImageIO.getImageWritersByFormatName("jpg").next();
		imgWriteParams = new javax.imageio.plugins.jpeg.JPEGImageWriteParam(null);
		// 要使用压缩，必须指定压缩方式为MODE_EXPLICIT
		imgWriteParams.setCompressionMode(imgWriteParams.MODE_EXPLICIT);
		// 这里指定压缩的程度，参数qality是取值0~1范围内，
		imgWriteParams.setCompressionQuality(quality);
		imgWriteParams.setProgressiveMode(imgWriteParams.MODE_DISABLED);
		ColorModel colorModel = ImageIO.read(inputFile).getColorModel();// ColorModel.getRGBdefault();
		// 指定压缩时使用的色彩模式
		imgWriteParams.setDestinationType(
				new javax.imageio.ImageTypeSpecifier(colorModel, colorModel.createCompatibleSampleModel(16, 16)));

		try {
			src = ImageIO.read(inputFile);
			if (!FileUtil.createFolder(outputFile.getParent())) {
				return false;
			}
			out = new FileOutputStream(outputFile);

			imgWrier.reset();
			// 必须先指定 out值，才能调用write方法, ImageOutputStream可以通过任何
			// OutputStream构造
			imgWrier.setOutput(ImageIO.createImageOutputStream(out));
			// 调用write方法，就可以向输入流写图片
			imgWrier.write(null, new IIOImage(src, null, null), imgWriteParams);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}

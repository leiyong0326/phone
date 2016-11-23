package com.ly.base.common.system;

public class UpYunConfig {

	/**
	 * 默认上传域名
	 */
	public static final String DEFAULT_UPLOAD_URL = "http://v0.api.upyun.com/";

	/**
	 * 默认上传方式
	 */
	public static final String DEFAULT_UPLOAD_METHOD = "PUT";

	public static final String METHOD_HEAD = "HEAD";

	public static final String METHOD_GET = "GET";

	public static final String METHOD_PUT = "PUT";

	public static final String METHOD_DELETE = "DELETE";

	// 默认的超时时间：30秒
	public static int TIME_OUT = 30 * 1000;

	/**
	 * 创建目录 SDK内部使用
	 */
	public static final String PARAM_KEY_MAKE_DIR = "folder";

	/**
	 * 路径的分割符
	 */
	public static final String UPYUN_DIR_SEPARATOR = "/";

	/**
	 * 图片上传大小常量 2M 2*1024*2014 = 2097152 Bytes(B) not bits
	 */
	public static final Long UPLOAD_IMG_SIZE = 2097152L;

	/**
	 * 图片upyun空间名称
	 */
	public static final String UPYUN_PIC_BUCKET_NAME = "cy-carimg-files";

	/**
	 * upyun账户
	 */
	public static final String UPYUN_USERNAME = "qhcykj";
	/**
	 * upyun登录密码
	 */
	public static final String UPYUN_PASSWORD = "Cheyun2016";

	/**
	 * upyun操作员账号
	 */
	public static final String UPYUN_OPERATOR_USERNAME = "qhcykjadmin";

	/**
	 * upyun操作员密码
	 */
	public static final String UPYUN_OPERATOR_PASSWORD = "qhcykj2016";

	/**
	 * 上传文件访问路径前缀 最终访问路径为 http://cy-carimg-files.b0.upaiyun.com/img/1.jpg
	 */
	public static final String FILE_ACCESS_URL = "http://" + UPYUN_PIC_BUCKET_NAME + ".b0.upaiyun.com";

	/**
	 * 空间表单api 秘钥,upyun后台高级功能里面设置
	 */
	public static final String UPYUN_FORM_API_SECRET = "f5vVO3Kh64nV648Vuk2yaml4XYI=";

	/**
	 * 默认授权有效期，Unix 时间戳 30分钟
	 */
	public static final long DEFAULT_EXPIRATION = 30 * 60 * 1000;

	/**
	 * 默认上传文件的分块数
	 */
	public static final int DEFAULT_FILE_BLOCKS = 1;

	/**
	 * 文件加密字符
	 */
	public static final String FILE_SECRET = "qhcy";

	// 目录声明开始--最终目录为/空间名称/机构简码/目录--------------------
	/**
	 * 新车图片目录
	 */
	public static final String CAR_IMG_FOLDER = "car_img";

	/**
	 * 二手车图片目录
	 */
	public static final String CAR_OLD_IMG_FOLDER = "old_car_img";

	/**
	 * 精品配件图片目录
	 */
	public static final String PARTS_IMG_FOLDER = "parts_img";

	/**
	 * 其他图片目录
	 */
	public static final String ORTHER_IMG = "other_img";

	/**
	 * 新车配置详情目录
	 */
	public static final String CAR_DETAIL_HTML_FOLDER = "car_detail_html";

	/**
	 * 精品详情目录
	 */
	public static final String PARTS_DETAIL_HTML_FOLDER = "parts_detail_html";

	/**
	 * 活动内容保存目录
	 */
	public static final String ACT_DETAIL_HTML_FOLDER = "act_html";

	// 目录声明结束-----------------------------------------------
}

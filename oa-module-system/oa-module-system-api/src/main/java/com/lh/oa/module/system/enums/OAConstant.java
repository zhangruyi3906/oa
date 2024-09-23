package com.lh.oa.module.system.enums;

/**
 * OA系统常量
 *
 * @author Rz Liu
 * @date 2023-01-10
 */
public class OAConstant {

    /**
     * 文件MD5检查 key
     */
    public static final String OA_FILE_CHECK_MD5 = "OA_FILE_CHECK_MD5";
    /**
     * 文件分片序号 key
     */
    public static final String OA_FILE_CHUNK_NUM = "OA_FILE_CHUNK_NUM";
    /**
     * 文件分片group key
     */
    public static final String OA_FILE_CHUNK_GROUP = "OA_FILE_CHUNK_GROUP";
    /**
     * 文件分片path key
     */
    public static final String OA_FILE_CHUNK_PATH = "OA_FILE_CHUNK_PATH";

    /**
     * 默认文件夹
     */
    public static final String FILE_DEFAULT_DIR = "文件资料";

    /**
     * 文件类型
     */
    public static final String[] IMAGE_EXTENSION = {"bmp", "gif", "jpg", "JPG", "jpeg", "JPEG", "png", "PNG"};
    public static final String[] VIDEO_EXTENSION = {"mp4", "avi", "flv", "rmvb"};
    public static final String[] AUDIO_EXTENSION = {"mp3", "wav", "wma", "swf", "wmv", "mid", "mpg", "asf", "rm"};
    public static final String[] DOC_EXTENSION = {"doc", "docx", "xls", "xlsx", "ppt", "pptx", "pdf", "html", "htm", "css", "js", "txt", "text", "xml"};
    public static final String[] COMPRESS_EXTENSION = {"rar", "zip", "gz", "bz2", "7z"};
    /**
     * 预览视频
     */
    public static final String[] VIDEO_PREVIEW_EXT = {"mp4"};
    /**
     * 预览音频
     */
    public static final String[] AUDIO_PREVIEW_EXT = {"mp3"};
    /**
     * 预览WORD
     */
    public static final String[] WORD_PREVIEW_EXT = {"doc", "docx"};
    /**
     * 预览EXCEL
     */
    public static final String[] EXCEL_PREVIEW_EXT = {"xls", "xlsx"};
    /**
     * 预览PPT
     */
    public static final String[] PPT_PREVIEW_EXT = {"ppt", "pptx"};
    /**
     * 预览可以直接打开的其他文档
     */
    public static final String[] OTHER_DOC_PREVIEW_EXT = {"pdf", "html", "htm", "css", "js", "txt", "text", "xml"};

    /**
     * http协议 prefix
     */
    public static final String FDFS_PROTOCOL_PREFIX = "http://";

    public static final String DEFAULT_PAGEABLE_PARAM_NAME = "pageable";

    public static final String OAUTH_TOKEN_KEY = "OAUTH_TOKEN";

    public static final String LOGIN_USER_KEY = "LOGIN_USER";

    public static final String LOGIN_PROJECT_KEY = "LOGIN_PROJECT";
    public static final String LOGIN_OA_AUTH_INFO_KEY = "LOGIN_OA_AUTH_INFO";
    public final static int DAY_SECONDS = 86400;

    public static final String LOGIN_CLIENT_KEY = "LOGIN_CLIENT";

    public static final String CAMERA_MANAGEMENT_YINGSHI_TOKEN = "CAMERA_MANAGEMENT_YINGSHI_TOKEN";

    public static final String CAMERA_MANAGEMENT_YINGSHI_PICTURE_URL= "CAMERA_MANAGEMENT_YINGSHI_%S_%S";

    public static final String QIYE_WECHAT_TOKEN = "QIYE_WECHAT_TOKEN";

    public static final String QIYE_WECHAT_JNT_CONSTRUCT_TOKEN = "QIYE_WECHAT_JNT_CONSTRUCT_TOKEN";

    public static final int SYS_DEFAULT_USER_ID = 1;

    public static final int SYS_SUPER_ADMIN_ROLE_ID = 1;//系统总管理
    public static final String SYS_SUPER_ADMIN_ROLE_CODE = "ADMINISTRATOR";
    public static final int SYS_ADMIN_ROLE_ID = 2;//系统管理人员
    public static final String SYS_ADMIN_ROLE_CODE = "SYS_ADMIN";
    public static final int SYS_USER_ROLE_ID = 3;//系统普通人员
    public static final String SYS_USER_ROLE_CODE = "SYS_USER";
    public static final int PRO_MANAGER_ROLE_ID = 4;//项目管理人员
    public static final String PRO_MANAGER_ROLE_CODE = "PRO_MANAGER";
    public static final int PRO_CONSTRUCTOR_ROLE_ID = 5;//项目施工人员
    public static final String PRO_CONSTRUCTOR_ROLE_CODE = "PRO_CONSTRUCTOR";

    public static final int SYS_LANHAI_ORG_ID = 1; //蓝海
    public static final int SYS_ANNENGDA_ORG_ID = 1; //安能达

    public static final String SYS_ROLE_KEY = "AUTH_ROLE";
    public static final String SYS_ORG_LAN_HAI = "LanHai";
    public static final String SYS_ORG_AN_NENG_DA = "AnNengDa";
    public static final String OA_WAREHOUSE_CODE_PREFIX = "OAWH";
    public static final String OA_MATERIAL_CODE_PREFIX = "OAMA";



}

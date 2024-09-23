package com.lh.oa.module.system.enums.file;

import cn.hutool.core.util.ArrayUtil;
import com.lh.oa.module.system.enums.OAConstant;

import java.util.Arrays;

/**
 * 文件资料类型
 *
 * @author Rz Liu
 * @date 2023-01-17
 */
public enum FileType {

    DIR("文件夹", ""),
    IMAGE("图片", "picturePreviewImpl"),
    VIDEO("视频", "videoPreviewImpl"),
    AUDIO("音频", "audioPreviewImpl"),
    DOC("文档", "docPreviewImpl"),
    COMPRESS("压缩文件", "compressPreviewImpl"),
    OTHERS("其他", "otherPreviewImpl"),
    ;

    private final String msg;
    private final String insName;

    FileType(String msg, String insName) {
        this.msg = msg;
        this.insName = insName;
    }

    public String msg() {
        return msg;
    }

    public String insName() {
        return insName;
    }

    public static FileType get(String extension) {
        if (ArrayUtil.contains(OAConstant.IMAGE_EXTENSION, extension)) return FileType.IMAGE;
        else if (ArrayUtil.contains(OAConstant.VIDEO_EXTENSION, extension)) return FileType.VIDEO;
        else if (ArrayUtil.contains(OAConstant.AUDIO_EXTENSION, extension)) return FileType.AUDIO;
        else if (ArrayUtil.contains(OAConstant.DOC_EXTENSION, extension)) return FileType.DOC;
        else if (ArrayUtil.contains(OAConstant.COMPRESS_EXTENSION, extension)) return FileType.COMPRESS;
        else return FileType.OTHERS;
    }

    public static FileType get(int ordinal) {
        return Arrays.stream(FileType.values()).filter(f -> f.ordinal() == ordinal).findFirst().orElse(FileType.OTHERS);
    }
}

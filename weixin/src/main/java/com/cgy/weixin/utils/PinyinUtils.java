package com.cgy.weixin.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * Created by cgy
 * 2018/5/31  17:28
 * 拼音工具(需要pinyin4j-2.5.0.jar)
 */
public class PinyinUtils {

    /**
     * 根据传入的字符串(包含汉字),得到拼音
     * 沧晓 -> CANGXIAO
     * 沧  晓*& -> CANGXIAO
     * 沧晓f5 -> CANGXIAO
     * @param str
     * @return
     */
    public static String getPinyin(String str) {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

        StringBuilder sb = new StringBuilder();

        char[] charArray = str.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            char c = charArray[i];
            //如果是空格,跳过
            if (Character.isWhitespace(c)) {
                continue;
            }

            if (c > -127 && c < 128 || !(c >= 0x4E00 && c <= 0x9FA5)) {
                //肯定不是汉字
                sb.append(c);
            } else {
                String s = "";
                try {
                    s = PinyinHelper.toHanyuPinyinStringArray(c, format)[0];
                    sb.append(s);
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                    sb.append(s);

                }
            }
        }
        return sb.toString();
    }
}

package com.qxcmp.bible;

import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class BibleHelper {


    private static final String BIBLE_TEXT_SEARCH_PATTERN = "(\\D+)(\\d+)(?:\\D+(\\d+)(?:\\D+(\\d+))?)?";

    /**
     * 转换圣经搜索命令为搜索请求对象
     *
     * @param version 圣经版本
     * @param text    搜索指令
     *
     * @return 搜索结果
     */
    public Optional<BibleSearchRequest> convertFromText(BibleVersion version, String text) {
        text = text.trim();

        Pattern pattern = Pattern.compile(BIBLE_TEXT_SEARCH_PATTERN);
        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            try {
                BibleSearchRequest.BibleSearchRequestBuilder requestBuilder = BibleSearchRequest.builder();

                String book = matcher.group(1);
                String chapter = matcher.group(2);
                String startVerse = matcher.group(3);
                String endVerse = matcher.group(4);

                requestBuilder.version(version);
                requestBuilder.book(book.trim());
                requestBuilder.chapter(Integer.parseInt(chapter));

                if (Objects.nonNull(startVerse)) {
                    requestBuilder.startVerse(Integer.parseInt(startVerse));

                    if (Objects.isNull(endVerse)) {
                        requestBuilder.endVerse(Integer.parseInt(startVerse));
                    } else {
                        requestBuilder.endVerse(Integer.parseInt(endVerse));
                    }
                } else {
                    requestBuilder.wholeChapter(true);
                }

                return Optional.of(requestBuilder.build());
            } catch (Exception e) {
                return Optional.empty();
            }

        }

        return Optional.empty();
    }

    public String covertBookNameToPinyin(String title) {
        try {
            return PinyinHelper.convertToPinyinString(title.replaceAll("[\\s+,.?-_——，。！？]", ""), " ", PinyinFormat.WITHOUT_TONE);
        } catch (PinyinException e) {
            return title;
        }
    }
}

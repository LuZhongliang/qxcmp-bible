package com.qxcmp.bible;

import lombok.Builder;
import lombok.Data;

/**
 * 圣经搜索请求
 *
 * @author Aaric
 * @see BibleSearchResponse
 */
@Data
@Builder
public class BibleSearchRequest {

    /**
     * 圣经版本
     */
    private BibleVersion version;

    /**
     * 书卷名称 - 对应的圣经版本实际名称
     */
    private String book;

    /**
     * 章节数
     */
    private int chapter;

    /**
     * 是否搜索整个章节
     */
    private boolean wholeChapter;

    /**
     * 如果不是搜索整个章节，则表示搜索的起始经文数
     */
    private int startVerse;

    /**
     * 如果不是搜索整个章节，则表示搜索的结束经文数
     */
    private int endVerse;

}

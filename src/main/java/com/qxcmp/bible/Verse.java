package com.qxcmp.bible;

import lombok.Builder;
import lombok.Data;

/**
 * 经文
 * <p>
 * 代表了圣经里面的一段经文
 *
 * @author aaric
 */
@Data
@Builder
public class Verse {

    private BibleVersion version;

    private String bookName;

    private int chapterId;

    private int verseId;

    private String content;
}

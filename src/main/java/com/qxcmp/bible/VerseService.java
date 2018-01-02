package com.qxcmp.bible;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Multimap;
import com.google.common.collect.Table;
import com.qxcmp.core.QxcmpConfigurator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 经文服务
 *
 * @author aaric
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class VerseService implements QxcmpConfigurator {

    /**
     * 圣经书卷名称
     * <p>
     * 1. 圣经版本
     * 2. 书卷名称列表
     */
    private ArrayListMultimap<BibleVersion, String> bookNames = ArrayListMultimap.create();

    /**
     * 圣经存贮对象
     * <p>
     * 1. 圣经版本
     * 2. 书卷名称
     * 3. 书卷经文（经文节数， 经文）
     */
    private Table<BibleVersion, String, ArrayListMultimap<Integer, Verse>> verses = HashBasedTable.create();

    private final BibleHelper bibleHelper;

    /**
     * 获取指定圣经版本的书卷名称
     *
     * @param version 圣经版本
     *
     * @return 书卷名称列表
     */
    public List<String> getBookNames(BibleVersion version) {
        return bookNames.get(version);
    }

    /**
     * 获取下一个书卷的名称
     *
     * @param version  圣经版本
     * @param bookName 当前书卷名称
     *
     * @return 如果当前书卷不存在，或者已经为最后一个书卷，返回空
     */
    public Optional<String> getNextBookName(BibleVersion version, String bookName) {
        List<String> names = bookNames.get(version);

        int index = names.indexOf(bookName);

        if (index >= 0) {
            try {
                return Optional.of(names.get(index + 1));
            } catch (Exception e) {
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }

    /**
     * 获取某个书卷的经文
     *
     * @param version  圣经版本
     * @param bookName 书卷名称
     *
     * @return 获取某个书卷的经文
     */
    public Multimap<Integer, Verse> getBookVerse(BibleVersion version, String bookName) {
        return verses.get(version, bibleHelper.covertBookNameToPinyin(bookName));
    }

    public BibleSearchResponse search(BibleSearchRequest request) {
        String bookPinyin = bibleHelper.covertBookNameToPinyin(request.getBook());

        List<Verse> result = verses.get(request.getVersion(), bookPinyin).get(request.getChapter());

        if (request.isWholeChapter()) {
            request.setStartVerse(1);
            request.setEndVerse(result.size());
            return BibleSearchResponse.builder().request(request).verses(result).build();
        } else {
            int startIndex = request.getStartVerse();
            int endIndex = request.getEndVerse();

            if (request.getStartVerse() > request.getEndVerse()) {
                startIndex = request.getEndVerse();
                endIndex = request.getStartVerse();
            }

            request.setStartVerse(startIndex);
            request.setEndVerse(endIndex);

            if (result.isEmpty()) {
                return BibleSearchResponse.builder().verses(result).request(request).build();
            } else {
                return BibleSearchResponse.builder().request(request).verses(result.subList(startIndex - 1, endIndex <= result.size() ? endIndex : result.size())).build();
            }
        }
    }

    @Override
    public void config() {
        loadBookName();
        loadBible("简体和合本.txt");
    }

    /**
     * 按照顺序加载圣经书卷名称
     */
    private void loadBookName() {
        bookNames.put(BibleVersion.JTHHB, "创世记");
        bookNames.put(BibleVersion.JTHHB, "出埃及记");
        bookNames.put(BibleVersion.JTHHB, "利未记");
        bookNames.put(BibleVersion.JTHHB, "民数记");
        bookNames.put(BibleVersion.JTHHB, "申命记");
        bookNames.put(BibleVersion.JTHHB, "约书亚记");
        bookNames.put(BibleVersion.JTHHB, "士师记");
        bookNames.put(BibleVersion.JTHHB, "路得记");
        bookNames.put(BibleVersion.JTHHB, "撒母耳记上");
        bookNames.put(BibleVersion.JTHHB, "撒母耳记下");
        bookNames.put(BibleVersion.JTHHB, "列王记上");
        bookNames.put(BibleVersion.JTHHB, "列王记下");
        bookNames.put(BibleVersion.JTHHB, "历代志上");
        bookNames.put(BibleVersion.JTHHB, "历代志下");
        bookNames.put(BibleVersion.JTHHB, "以斯拉记");
        bookNames.put(BibleVersion.JTHHB, "尼希米记");
        bookNames.put(BibleVersion.JTHHB, "以斯帖记");
        bookNames.put(BibleVersion.JTHHB, "约伯记");
        bookNames.put(BibleVersion.JTHHB, "诗篇");
        bookNames.put(BibleVersion.JTHHB, "箴言");
        bookNames.put(BibleVersion.JTHHB, "传道书");
        bookNames.put(BibleVersion.JTHHB, "雅歌");
        bookNames.put(BibleVersion.JTHHB, "以赛亚书");
        bookNames.put(BibleVersion.JTHHB, "耶利米书");
        bookNames.put(BibleVersion.JTHHB, "耶利米哀歌");
        bookNames.put(BibleVersion.JTHHB, "以西结书");
        bookNames.put(BibleVersion.JTHHB, "但以理书");
        bookNames.put(BibleVersion.JTHHB, "何西阿书");
        bookNames.put(BibleVersion.JTHHB, "约珥书");
        bookNames.put(BibleVersion.JTHHB, "阿摩司书");
        bookNames.put(BibleVersion.JTHHB, "俄巴底亚书");
        bookNames.put(BibleVersion.JTHHB, "约拿书");
        bookNames.put(BibleVersion.JTHHB, "弥迦书");
        bookNames.put(BibleVersion.JTHHB, "那鸿书");
        bookNames.put(BibleVersion.JTHHB, "哈巴谷书");
        bookNames.put(BibleVersion.JTHHB, "西番雅书");
        bookNames.put(BibleVersion.JTHHB, "哈该书");
        bookNames.put(BibleVersion.JTHHB, "撒迦利亚");
        bookNames.put(BibleVersion.JTHHB, "玛拉基书");
        bookNames.put(BibleVersion.JTHHB, "马太福音");
        bookNames.put(BibleVersion.JTHHB, "马可福音");
        bookNames.put(BibleVersion.JTHHB, "路加福音");
        bookNames.put(BibleVersion.JTHHB, "约翰福音");
        bookNames.put(BibleVersion.JTHHB, "使徒行传");
        bookNames.put(BibleVersion.JTHHB, "罗马书");
        bookNames.put(BibleVersion.JTHHB, "哥林多前书");
        bookNames.put(BibleVersion.JTHHB, "哥林多后书");
        bookNames.put(BibleVersion.JTHHB, "加拉太书");
        bookNames.put(BibleVersion.JTHHB, "以弗所书");
        bookNames.put(BibleVersion.JTHHB, "腓立比书");
        bookNames.put(BibleVersion.JTHHB, "歌罗西书");
        bookNames.put(BibleVersion.JTHHB, "帖撒罗尼迦前书");
        bookNames.put(BibleVersion.JTHHB, "帖撒罗尼迦後书");
        bookNames.put(BibleVersion.JTHHB, "提摩太前书");
        bookNames.put(BibleVersion.JTHHB, "提摩太後书");
        bookNames.put(BibleVersion.JTHHB, "提多书");
        bookNames.put(BibleVersion.JTHHB, "腓利门书");
        bookNames.put(BibleVersion.JTHHB, "希伯来书");
        bookNames.put(BibleVersion.JTHHB, "雅各书");
        bookNames.put(BibleVersion.JTHHB, "彼得前书");
        bookNames.put(BibleVersion.JTHHB, "彼得後书");
        bookNames.put(BibleVersion.JTHHB, "约翰一书");
        bookNames.put(BibleVersion.JTHHB, "约翰二书");
        bookNames.put(BibleVersion.JTHHB, "约翰三书");
        bookNames.put(BibleVersion.JTHHB, "犹大书");
        bookNames.put(BibleVersion.JTHHB, "启示录");
    }

    private void loadBible(String file) {
        try {
            BibleVersion version = BibleVersion.fromName(StringUtils.substringBefore(file, ".txt"));
            IOUtils.readLines(new ClassPathResource("/bible/" + file).getInputStream(), Charset.defaultCharset()).forEach(s -> {
                String mainContent = StringUtils.substringBefore(s, "[[");
                String bookName = mainContent.split("\\s+")[0];

                if (!bookNames.get(version).contains(bookName)) {
                    throw new RuntimeException("未找到对应书卷名称：" + bookName);
                }

                int chapter = Integer.parseInt(mainContent.split("\\s+")[1]);
                int verseId = Integer.parseInt(mainContent.split("\\s+")[2]);
                String verseContent = StringUtils.substringBetween(s, "[[", "]]");

                String book = bibleHelper.covertBookNameToPinyin(bookName);

                ArrayListMultimap<Integer, Verse> verseMultimap = verses.get(version, book);

                if (Objects.isNull(verseMultimap)) {
                    verseMultimap = ArrayListMultimap.create();
                    verses.put(version, book, verseMultimap);
                }

                verseMultimap.put(chapter, Verse.builder()
                        .version(version)
                        .bookName(bookName)
                        .chapterId(chapter)
                        .verseId(verseId)
                        .content(verseContent)
                        .build());
            });
        } catch (IOException e) {
            log.error("Can't load bible {}, cause: {}", file, e.getMessage());
        }
    }
}

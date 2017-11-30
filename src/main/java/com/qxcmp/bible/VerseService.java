package com.qxcmp.bible;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.qxcmp.framework.core.QxcmpConfigurator;
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
     * 圣经存贮对象
     */
    private Table<BibleVersion, String, ArrayListMultimap<Integer, Verse>> verses = HashBasedTable.create();

    private final BibleHelper bibleHelper;

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
        log.info("Loading 《简体和合本》");
        loadBible("简体和合本.txt");
        log.info("《简体和合本》 loaded");
    }

    private void loadBible(String file) {

        BibleVersion version = BibleVersion.fromName(StringUtils.substringBefore(file, ".txt"));

        try {
            IOUtils.readLines(new ClassPathResource("/bible/" + file).getInputStream(), Charset.defaultCharset()).forEach(s -> {
                String mainContent = StringUtils.substringBefore(s, "[[");
                String bookName = mainContent.split("\\s+")[0];
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
                        .versionContent(verseContent)
                        .build());
            });
        } catch (IOException e) {
            log.error("Can't load bible {}, cause: {}", file, e.getMessage());
        }
    }
}

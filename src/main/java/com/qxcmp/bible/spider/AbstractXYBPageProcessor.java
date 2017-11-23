package com.qxcmp.bible.spider;

import com.qxcmp.bible.BibleVersion;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import us.codecraft.webmagic.Page;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

public abstract class AbstractXYBPageProcessor extends AbstractBiblePageProcessor {

    @Override
    protected void processPage(BibleVersion version, String bookName, Page page) {
        try {
            String bibleIndex = StringUtils.substringBefore(StringUtils.substringAfterLast(page.getUrl().get(), "/"), ".htm");
            int bookId = Integer.parseInt(bibleIndex.substring(1, 3));
            int chapterId = Integer.parseInt(bibleIndex.substring(4, 7));

            List<String> verses = page.getHtml().$("td", "text").all();

            for (int i = 4; i < verses.size(); i += 4) {
                int verseId = Integer.parseInt(verses.get(i + 1));
                String verseContent = verses.get(i + 3);

                FileUtils.writeStringToFile(new File(String.format("/tmp/bible/%s.txt", version.getName())),
                        String.format("%s %d %d [[%s]]\n", bookName, chapterId, verseId, verseContent), Charset.defaultCharset(), true);
//
//                Optional<Verse> verseOptional = verseService.findOne(version, bookId, chapterId, verseId);
//
//                if (verseOptional.isPresent()) {
//                    verseService.update(verseOptional.get().getId(), verse -> {
//                        verse.setVersion(version);
//                        verse.setBookId(bookId);
//                        verse.setBookName(bookName);
//                        verse.setBookPinyin(bibleHelper.covertBookNameToPinyin(bookName));
//                        verse.setChapterId(chapterId);
//                        verse.setVerseId(verseId);
//                        verse.setVersionContent(verseContent);
//                    });
//                } else {
//                    verseService.create(() -> {
//                        Verse verse = verseService.next();
//                        verse.setVersion(version);
//                        verse.setBookId(bookId);
//                        verse.setBookName(bookName);
//                        verse.setBookPinyin(bibleHelper.covertBookNameToPinyin(bookName));
//                        verse.setChapterId(chapterId);
//                        verse.setVerseId(verseId);
//                        verse.setVersionContent(verseContent);
//                        return verse;
//                    });
//                }

            }
        } catch (Exception ignored) {

        }
    }
}

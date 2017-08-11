package com.qxcmp.bible.spider;

import com.qxcmp.bible.BibleHelper;
import com.qxcmp.bible.BibleVersion;
import com.qxcmp.bible.Verse;
import com.qxcmp.bible.VerseService;
import com.qxcmp.platform.module.spider.SpiderPageProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.Page;

public abstract class AbstractBiblePageProcessor extends SpiderPageProcessor<Verse> {

    protected VerseService verseService;

    protected BibleHelper bibleHelper;

    protected abstract void processPage(BibleVersion version, String bookName, Page page);

    @Autowired
    public void setVerseService(VerseService verseService) {
        this.verseService = verseService;
    }

    @Autowired
    public void setBibleHelper(BibleHelper bibleHelper) {
        this.bibleHelper = bibleHelper;
    }
}

package com.qxcmp.bible;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VerseServiceTest {

    @Autowired
    private VerseService verseService;

    @Test
    public void testSearchWholeChapter() throws Exception {
        BibleSearchResponse searchResponse = verseService.search(BibleSearchRequest.builder().version(BibleVersion.JTHHB).book("创世纪").chapter(1).wholeChapter(true).build());
        assertEquals(31, searchResponse.getVerses().size());
        assertEquals("神看光是好的、就把光暗分开了。 ", searchResponse.getVerses().get(3).getVersionContent());
    }

    @Test
    public void testSearchSingleVerse() throws Exception {
        BibleSearchResponse searchResponse = verseService.search(BibleSearchRequest.builder().version(BibleVersion.JTHHB).book("约翰福音").chapter(3).startVerse(16).endVerse(16).build());
        assertEquals("神爱世人、甚至将他的独生子赐给他们、叫一切信他的、不至灭亡、反得永生。 ", searchResponse.getVerses().get(0).getVersionContent());
    }

    @Test
    public void testSearchRange() throws Exception {
        BibleSearchResponse searchResponse = verseService.search(BibleSearchRequest.builder().version(BibleVersion.JTHHB).book("启示录").chapter(2).startVerse(4).endVerse(5).build());
        assertEquals(2, searchResponse.getVerses().size());
        assertEquals("然而有一件事我要责备你、就是你把起初的爱心离弃了。 ", searchResponse.getVerses().get(0).getVersionContent());
    }
}
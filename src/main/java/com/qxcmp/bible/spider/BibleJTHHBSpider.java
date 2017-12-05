package com.qxcmp.bible.spider;

import com.qxcmp.bible.BibleVersion;
import com.qxcmp.bible.Verse;
import com.qxcmp.spdier.Spider;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;

import java.util.Optional;

/**
 * 圣经和合本抓取蜘蛛
 *
 * @author aaric
 */
@Spider(
        group = "圣经",
        name = "简体和合本",
        startUrls = "http://www.godcom.net/hhb/cuvgb.htm",
        pipelines = BibleDummyPipeline.class,
        disabled = true,
        order = 1)
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class BibleJTHHBSpider extends AbstractHHBPageProcessor {

    public BibleJTHHBSpider() {
        getSite().setCharset("gbk");
    }

    @Override
    protected boolean isTargetPage(Page page) {
        return page.getUrl().get().matches(".*/hhb/B\\d+C\\d+.htm");
    }

    @Override
    protected Optional<Verse> processTargetPage(Page page) {
        page.addTargetRequests(page.getHtml().links().regex(".*/hhb/B\\d+C\\d+.htm").all());

        String bookName = page.getHtml().$("h2", "text").get().replaceAll("[\\s\\d[a-zA-Z]]", "");

        processPage(BibleVersion.JTHHB, bookName, page);

        return Optional.empty();
    }

    @Override
    protected void processContentPage(Page page) {
        page.addTargetRequests(page.getHtml().links().regex(".*/hhb/B\\d+C\\d+.htm").all());
    }
}

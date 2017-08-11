package com.qxcmp.bible;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.List;

/**
 * 圣经搜索结果
 *
 * @author Aaric
 * @see BibleSearchRequest
 */
@Data
@Builder
public class BibleSearchResponse {

    private BibleSearchRequest request;

    @Singular
    private List<Verse> verses;

}

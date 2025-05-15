package com.vecanhac.ddd.domain.projection;

import java.math.BigDecimal;

public interface EventTrendingProjection {
    Long getId();
    String getTitle();
    String getSlug();
    String getCoverImageUrl();
    BigDecimal getMinTicketPrice();
    Long getTotalSold();
}
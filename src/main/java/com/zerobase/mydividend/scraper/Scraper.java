package com.zerobase.mydividend.scraper;

import com.zerobase.mydividend.model.Company;
import com.zerobase.mydividend.model.ScrapedResult;

public interface Scraper {
    Company scrapCompanyByTicker(String ticker);

    ScrapedResult scrap(Company company);
}

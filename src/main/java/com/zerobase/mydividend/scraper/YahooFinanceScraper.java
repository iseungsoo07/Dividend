package com.zerobase.mydividend.scraper;

import com.zerobase.mydividend.exception.impl.UnexpectedMonthException;
import com.zerobase.mydividend.model.Company;
import com.zerobase.mydividend.model.Dividend;
import com.zerobase.mydividend.model.ScrapedResult;
import com.zerobase.mydividend.model.constants.Month;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class YahooFinanceScraper implements Scraper {

    private static final String STATISTICS_URL = "https://finance.yahoo.com/quote/%s/history?period1=%d&period2=%d&interval=1mo";
    private static final String SUMMARY_URL = "https://finance.yahoo.com/quote/%s?p=%s";

    private static final long START_TIME = 86400; // 60초 * 60분 * 24시간 -> 1일

    @Override
    public Company scrapCompanyByTicker(String ticker) {
        String url = String.format(SUMMARY_URL, ticker, ticker);

        try {
            Document document = Jsoup.connect(url).get();
            Element titleEle = document.getElementsByTag("h1").get(0);

            String companyName = titleEle.text().split(" - ")[1];

            return Company.builder()
                    .ticker(ticker)
                    .name(companyName)
                    .build();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public ScrapedResult scrap(Company company) {
        ScrapedResult scrapedResult = new ScrapedResult();
        scrapedResult.setCompany(company);

        long now = System.currentTimeMillis() / 1000;

        try {
            String url = String.format(STATISTICS_URL, company.getTicker(), START_TIME, now);
            Connection connection = Jsoup.connect(url);

            Document document = connection.get();

            Elements parsingDivs = document.getElementsByAttributeValue("data-test", "historical-prices");
            Element tableEle = parsingDivs.get(0);

            Element tbody = tableEle.children().get(1);

            List<Dividend> dividends = new ArrayList<>();
            for (Element e : tbody.children()) {
                String text = e.text();

                if (!text.endsWith("Dividend")) {
                    continue;
                }

                // Aug 11, 2023 0.24 Dividend
                String[] splits = text.split(" ");
                int year = Integer.parseInt(splits[2]);
                int month = Month.monthStringToNumber(splits[0]);
                int day = Integer.parseInt(splits[1].replace(",", ""));
                String dividend = splits[3];

                if (month < 0) {
                    throw new UnexpectedMonthException();
                }

                dividends.add(new Dividend(LocalDateTime.of(year, month, day, 0, 0), dividend));
            }

            scrapedResult.setDividends(dividends);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return scrapedResult;
    }
}

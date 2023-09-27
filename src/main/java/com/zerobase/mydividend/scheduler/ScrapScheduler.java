package com.zerobase.mydividend.scheduler;

import com.zerobase.mydividend.model.Company;
import com.zerobase.mydividend.model.ScrapedResult;
import com.zerobase.mydividend.model.constants.CacheKey;
import com.zerobase.mydividend.persist.CompanyRepository;
import com.zerobase.mydividend.persist.DividendRepository;
import com.zerobase.mydividend.persist.entity.CompanyEntity;
import com.zerobase.mydividend.persist.entity.DividendEntity;
import com.zerobase.mydividend.scraper.YahooFinanceScraper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@EnableCaching
@RequiredArgsConstructor
public class ScrapScheduler {

    private final CompanyRepository companyRepository;
    private final YahooFinanceScraper yahooFinanceScraper;
    private final DividendRepository dividendRepository;

    @Scheduled(cron = "${scheduler.scrap.yahoo}")
    @CacheEvict(value = CacheKey.KEY_FINANCE, allEntries = true)
    public void yahooFinanceScheduling() {
        log.info("스크래핑 스케줄러 시작");
        // 저장된 회사 목록을 조회
        List<CompanyEntity> companies = companyRepository.findAll();

        // 회사마다 배당금 정보를 새로 스크래핑
        for (CompanyEntity companyEntity : companies) {
            ScrapedResult scrapedResult = yahooFinanceScraper
                    .scrap(new Company(companyEntity.getTicker(), companyEntity.getName()));

            // 스크래핑한 배당금 정보 중 데이터베이스에 없다면 저장
            scrapedResult.getDividends().stream()
                    .map(dividend -> new DividendEntity(companyEntity.getId(), dividend))
                    .forEach(dividendEntity -> {
                        boolean exists = dividendRepository
                                .existsByCompanyIdAndDate(dividendEntity.getCompanyId(), dividendEntity.getDate());

                        if (!exists) {
                            dividendRepository.save(dividendEntity);
                            log.info("새로운 배당금 정보가 추가되었습니다 -> {}", dividendEntity.toString());
                        }
                    });

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }


    }
}

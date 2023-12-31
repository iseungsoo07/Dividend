package com.zerobase.mydividend.service;

import com.zerobase.mydividend.exception.impl.AlreadyExistCompanyException;
import com.zerobase.mydividend.exception.impl.NoSuchCompanyException;
import com.zerobase.mydividend.model.Company;
import com.zerobase.mydividend.model.ScrapedResult;
import com.zerobase.mydividend.persist.CompanyRepository;
import com.zerobase.mydividend.persist.DividendRepository;
import com.zerobase.mydividend.persist.entity.CompanyEntity;
import com.zerobase.mydividend.persist.entity.DividendEntity;
import com.zerobase.mydividend.scraper.YahooFinanceScraper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final YahooFinanceScraper yahooFinanceScraper;
    private final DividendRepository dividendRepository;

    public Company save(String ticker) {
        if (companyRepository.existsByTicker(ticker)) {
            throw new AlreadyExistCompanyException();
        }

        return storeCompanyAndDividend(ticker);
    }

    public Page<CompanyEntity> getAllCompany(Pageable pageable) {
        return companyRepository.findAll(pageable);
    }

    private Company storeCompanyAndDividend(String ticker) {
        // ticker를 기준으로 회사를 스크래핑
        Company company = yahooFinanceScraper.scrapCompanyByTicker(ticker);

        if (company == null) {
            throw new NoSuchCompanyException();
        }

        // ticker로 조회한 회사가 존재할 경우, 회사의 배당금 정보를 스크래핑
        ScrapedResult scrapedResult = yahooFinanceScraper.scrap(company);

        CompanyEntity companyEntity = companyRepository.save(new CompanyEntity(company));
        List<DividendEntity> dividendEntities = scrapedResult.getDividends().stream()
                .map(dividend -> new DividendEntity(companyEntity.getId(), dividend))
                .collect(Collectors.toList());

        dividendRepository.saveAll(dividendEntities);

        return company;
    }

    public List<String> getCompanyNamesByPrefix(String prefix) {
        Pageable limit = PageRequest.of(0, 10);
        Page<CompanyEntity> companyEntities = companyRepository.findByNameStartingWithIgnoreCase(prefix, limit);

        return companyEntities.stream().map(CompanyEntity::getName).collect(Collectors.toList());
    }

    public String deleteCompany(String ticker) {
        CompanyEntity companyEntity = companyRepository.findByTicker(ticker)
                .orElseThrow(NoSuchCompanyException::new);

        dividendRepository.deleteAllByCompanyId(companyEntity.getId());
        companyRepository.delete(companyEntity);

        return companyEntity.getName();
    }
}

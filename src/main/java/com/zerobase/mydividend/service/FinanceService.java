package com.zerobase.mydividend.service;

import com.zerobase.mydividend.exception.impl.NoSuchCompanyException;
import com.zerobase.mydividend.model.Company;
import com.zerobase.mydividend.model.Dividend;
import com.zerobase.mydividend.model.ScrapedResult;
import com.zerobase.mydividend.model.constants.CacheKey;
import com.zerobase.mydividend.persist.CompanyRepository;
import com.zerobase.mydividend.persist.DividendRepository;
import com.zerobase.mydividend.persist.entity.CompanyEntity;
import com.zerobase.mydividend.persist.entity.DividendEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FinanceService {

    private final DividendRepository dividendRepository;
    private final CompanyRepository companyRepository;

    @Cacheable(key = "#companyName", value = CacheKey.KEY_FINANCE)
    public ScrapedResult getDividendByCompanyName(String companyName) {
        log.info("{} 회사 조회", companyName);

        // 회사명을 기준으로 회사 정보를 조회
        CompanyEntity company = companyRepository.findByName(companyName)
                .orElseThrow(NoSuchCompanyException::new);

        // 조회된 회사 ID로 배당금 정보를 조회
        List<DividendEntity> dividendEntities = dividendRepository.findAllByCompanyId(company.getId());

        // 결과를 조합해서 반환
        List<Dividend> dividends = dividendEntities.stream()
                .map(dividendEntity -> new Dividend(dividendEntity.getDate(), dividendEntity.getDividend()))
                .collect(Collectors.toList());


        return new ScrapedResult(new Company(company.getTicker(), company.getName()), dividends);
    }

}

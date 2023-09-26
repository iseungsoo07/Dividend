package com.zerobase.mydividend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScrapedResult {
    private Company company;
    private List<Dividend> dividends;
}

package com.zerobase.mydividend.persist.entity;

import com.zerobase.mydividend.model.Company;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Builder
@Entity(name = "COMPANY")
public class CompanyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String ticker;

    public CompanyEntity(Company company) {
        this.name = company.getName();
        this.ticker = company.getTicker();
    }
}

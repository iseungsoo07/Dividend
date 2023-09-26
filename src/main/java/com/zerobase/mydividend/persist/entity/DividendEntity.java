package com.zerobase.mydividend.persist.entity;

import com.zerobase.mydividend.model.Dividend;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "DIVIDEND")
public class DividendEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long companyId;

    private LocalDateTime date;

    private String dividend;

    public DividendEntity(Long companyId, Dividend dividend) {
        this.companyId = companyId;
        this.dividend = dividend.getDividend();
        this.date = dividend.getDate();
    }
}

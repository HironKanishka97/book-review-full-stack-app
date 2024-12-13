
package com.hk.bookreview.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SaveReviewDto {

    private String bookTitle;
    private String author;
    private BigDecimal rating;
    private String review;
    private Date dateAdded;

}


package com.hk.bookreview.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetReviewDto {
    private ReviewDto reviewDto;
    private String res;

}

package com.hk.bookreview.service;


import com.hk.bookreview.dto.GetReviewDto;
import com.hk.bookreview.dto.ReviewDto;
import com.hk.bookreview.dto.SaveReviewDto;
import com.hk.bookreview.entity.Review;
import com.hk.bookreview.repository.* ;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ReviewService {

    @Autowired
    private ReviewRepo reviewRepo;
    @Autowired
    private ModelMapper modelMapper;

    public ReviewService() {
    }

    public List<ReviewDto> getAllReviews() {
        List<Review> reviewList = reviewRepo.findAll();
        List<ReviewDto> list =modelMapper.map(reviewList,new TypeToken<List<ReviewDto>>() {}.getType());
        return list;
    }

    public GetReviewDto getReview(Integer reviewId) {
        if (!reviewRepo.existsById(reviewId)) {
            return new GetReviewDto(null, "404");
        } else {
            Review review = reviewRepo.findById(reviewId).get();
            ReviewDto reviewDto = modelMapper.map(review, ReviewDto.class);
            return new GetReviewDto(reviewDto, "200");
        }
    }

    public String saveReview(SaveReviewDto reviewDto) {
        if (reviewRepo.existsByBookTitle(reviewDto.getBookTitle())) {
            return "409";
        } else {
            Review review = modelMapper.map(reviewDto, Review.class);
            reviewRepo.save(review);
            return "200";
        }
    }

    public String updateReview(Integer reviewId, ReviewDto reviewDto) {
        if (!reviewRepo.existsById(reviewId)) {
            return "404";
        } else {
            Review existingReview = reviewRepo.findById(reviewId).orElse(null);
            if (existingReview != null) {
                reviewRepo.save(modelMapper.map(reviewDto, Review.class));
                return "200";
            } else {
                return "500";
            }
        }
    }

    public String deleteReview(Integer reviewId) {
        if (!reviewRepo.existsById(reviewId)) {
            return "404";
        } else {
            reviewRepo.deleteById(reviewId);
            return "200";
        }
    }
}


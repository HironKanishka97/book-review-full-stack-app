package com.hk.bookreview.controller;


import com.hk.bookreview.dto.GetReviewDto;
import com.hk.bookreview.dto.ResponseDto;
import com.hk.bookreview.dto.ReviewDto;
import com.hk.bookreview.dto.SaveReviewDto;
import com.hk.bookreview.service.ReviewService;
import java.util.List;

import com.hk.bookreview.util.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"api/v1/bookreviews/"})
@CrossOrigin
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @GetMapping({"getallreviews"})
    public ResponseEntity<ResponseDto> getAllReviews() {
        ResponseDto responseDto;
        try {
            List<ReviewDto> reviews = this.reviewService.getAllReviews();
            if (reviews != null && !reviews.isEmpty()) {
                responseDto = new ResponseDto(VarList.RSP_SUCCESS, "Reviews Found", reviews);
                return new ResponseEntity<>(responseDto, HttpStatus.OK);
            } else {
                responseDto = new ResponseDto(VarList.RSP_NO_DATA_FOUND, "No Reviews Found", null);
                return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            responseDto = new ResponseDto(VarList.RSP_ERROR, e.getMessage(), null);
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping({"getreview/{id}"})
    public ResponseEntity<ResponseDto> getReview(@PathVariable Integer id) {
        try {
            GetReviewDto getReviewDto = this.reviewService.getReview(id);
            String res = getReviewDto.getRes();
            ReviewDto reviewDto = getReviewDto.getReviewDto();
            ResponseDto responseDto;
            if (res.equals(VarList.RSP_SUCCESS)) {
                responseDto = new ResponseDto(VarList.RSP_SUCCESS, "Review Found", reviewDto);
                return new ResponseEntity<>(responseDto, HttpStatus.OK);
            } else if (res.equals(VarList.RSP_NO_DATA_FOUND)) {
                responseDto = new ResponseDto(VarList.RSP_NO_DATA_FOUND, "Review Not Found", null);
                return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
            } else {
                responseDto = new ResponseDto(VarList.RSP_ERROR, "Internal Server Error", null);
                return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            ResponseDto responseDto = new ResponseDto(VarList.RSP_ERROR, e.getMessage(), null);
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping({"savereview"})
    public ResponseEntity<ResponseDto> saveReview(@RequestBody SaveReviewDto reviewDto) {
        ResponseDto responseDto;
        try {
            System.out.println(reviewDto);
            String res = this.reviewService.saveReview(reviewDto);
            if (res.equals(VarList.RSP_SUCCESS)) {
                responseDto = new ResponseDto(VarList.RSP_SUCCESS, "Saved Successfully", reviewDto);
                return new ResponseEntity<>(responseDto, HttpStatus.OK);
            } else if (res.equals(VarList.RSP_DUPLICATED)) {
                responseDto = new ResponseDto(VarList.RSP_DUPLICATED, "Review Already Exists", reviewDto);
                return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
            } else {
                responseDto = new ResponseDto(VarList.RSP_ERROR, "Internal Server Error", reviewDto);
                return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            responseDto = new ResponseDto(VarList.RSP_ERROR, e.getMessage(), null);
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping({"updatereview/{id}"})
    public ResponseEntity<ResponseDto> updateReview(@PathVariable Integer id, @RequestBody ReviewDto reviewDto) {
        ResponseDto responseDto;
        try {
            String res = this.reviewService.updateReview(id, reviewDto);
            if (res.equals(VarList.RSP_SUCCESS)) {
                responseDto = new ResponseDto(VarList.RSP_SUCCESS, "Updated Successfully", reviewDto);
                return new ResponseEntity<>(responseDto, HttpStatus.OK);
            } else if (res.equals(VarList.RSP_NO_DATA_FOUND)) {
                responseDto = new ResponseDto(VarList.RSP_NO_DATA_FOUND, "Review Not Found", reviewDto);
                return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
            } else if (res.equals(VarList.RSP_ERROR)) {
                responseDto = new ResponseDto(VarList.RSP_ERROR, "Internal Server Error", reviewDto);
                return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
            } else {
                responseDto = new ResponseDto(VarList.RSP_ERROR, "Internal Server Error", reviewDto);
                return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            responseDto = new ResponseDto(VarList.RSP_ERROR, e.getMessage(), null);
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping({"deletereview/{id}"})
    public ResponseEntity<ResponseDto> deleteReview(@PathVariable Integer id) {
        ResponseDto responseDto;
        try {
            String res = this.reviewService.deleteReview(id);
            if (res.equals(VarList.RSP_SUCCESS)) {
                responseDto = new ResponseDto(VarList.RSP_SUCCESS, "Deleted Successfully", null);
                return new ResponseEntity<>(responseDto, HttpStatus.OK);
            } else if (res.equals(VarList.RSP_NO_DATA_FOUND)) {
                responseDto = new ResponseDto(VarList.RSP_NO_DATA_FOUND, "Review Not Found", null);
                return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
            } else {
                responseDto = new ResponseDto(VarList.RSP_ERROR, "Internal Server Error", null);
                return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            responseDto = new ResponseDto(VarList.RSP_ERROR, e.getMessage(), null);
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

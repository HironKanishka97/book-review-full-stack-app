package com.hk.bookreview.repository;


import com.hk.bookreview.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ReviewRepo extends JpaRepository<Review, Integer> {
    boolean existsByBookTitle(String title);
}
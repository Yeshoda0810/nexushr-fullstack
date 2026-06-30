package com.nexushr.backend.service;
import com.nexushr.backend.model.Employee;
import com.nexushr.backend.model.PerformanceReview;
import com.nexushr.backend.model.ReviewStatus;
import com.nexushr.backend.model.dto.CreateGoalsRequest;
import com.nexushr.backend.model.dto.SubmitReviewRequest;
import com.nexushr.backend.repository.EmployeeRepository;
import com.nexushr.backend.repository.PerformanceReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PerformanceService {
    private final PerformanceReviewRepository performanceReviewRepository;
    private final EmployeeRepository employeeRepository;

    // Employee creates a review cycle entry with their own goals/achievements
    public PerformanceReview createGoals(String email, CreateGoalsRequest request) {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        PerformanceReview review = PerformanceReview.builder()
                .employee(employee)
                .reviewYear(request.getReviewYear())
                .reviewPeriod(request.getReviewPeriod())
                .goals(request.getGoals())
                .achievements(request.getAchievements())
                .status(ReviewStatus.DRAFT)
                .build();

        return performanceReviewRepository.save(review);
    }

    // Manager/Admin submits feedback + rating, moves review to SUBMITTED
    public PerformanceReview submitReview(Long reviewId, String reviewerEmail, SubmitReviewRequest request) {
        PerformanceReview review = performanceReviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        Employee reviewer = employeeRepository.findByEmail(reviewerEmail)
                .orElseThrow(() -> new RuntimeException("Reviewer not found"));

        review.setReviewer(reviewer);
        review.setManagerFeedback(request.getManagerFeedback());
        review.setRating(request.getRating());
        review.setStatus(ReviewStatus.SUBMITTED);

        return performanceReviewRepository.save(review);
    }

    // Employee acknowledges that they've read the feedback
    public PerformanceReview acknowledgeReview(Long reviewId, String email) {
        PerformanceReview review = performanceReviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        if (!review.getEmployee().getId().equals(employee.getId())) {
            throw new RuntimeException("You can only acknowledge your own review");
        }

        if (review.getStatus() != ReviewStatus.SUBMITTED) {
            throw new RuntimeException("Only submitted reviews can be acknowledged");
        }

        review.setStatus(ReviewStatus.ACKNOWLEDGED);
        return performanceReviewRepository.save(review);
    }

    public List<PerformanceReview> getMyReviews(String email) {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        return performanceReviewRepository.findByEmployeeIdOrderByReviewYearDesc(employee.getId());
    }

    public List<PerformanceReview> getEmployeeReviews(Long employeeId) {
        return performanceReviewRepository.findByEmployeeIdOrderByReviewYearDesc(employeeId);
    }
}

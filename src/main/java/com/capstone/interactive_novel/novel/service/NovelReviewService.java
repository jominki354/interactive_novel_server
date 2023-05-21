package com.capstone.interactive_novel.novel.service;

import com.capstone.interactive_novel.common.exception.ErrorCode;
import com.capstone.interactive_novel.common.exception.INovelException;
import com.capstone.interactive_novel.novel.domain.NovelEntity;
import com.capstone.interactive_novel.novel.domain.NovelReviewEntity;
import com.capstone.interactive_novel.novel.dto.NovelReviewDto;
import com.capstone.interactive_novel.novel.repository.NovelRepository;
import com.capstone.interactive_novel.novel.repository.NovelReviewRepository;
import com.capstone.interactive_novel.novel.repository.NovelReviewRepositoryQuerydsl;
import com.capstone.interactive_novel.reader.domain.ReaderEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.capstone.interactive_novel.common.exception.ErrorCode.*;
import static com.capstone.interactive_novel.common.type.PublisherType.READER;
import static com.capstone.interactive_novel.novel.domain.NovelReviewStatus.AVAILABLE;
import static com.capstone.interactive_novel.novel.domain.NovelReviewStatus.DEACTIVATED;

@Service
@RequiredArgsConstructor
public class NovelReviewService {
    private final NovelRepository novelRepository;
    private final NovelReviewRepository novelReviewRepository;
    private final NovelReviewRepositoryQuerydsl novelReviewRepositoryQuerydsl;

    @Transactional
    public NovelReviewDto createNovelReview(ReaderEntity reader,
                                            Long novelId,
                                            String review,
                                            int novelScore) {
        NovelEntity novel = novelRepository.findById(novelId)
                .orElseThrow(() -> new INovelException(ErrorCode.NOVEL_NOT_FOUND));
        if(novelReviewRepository.findByReaderAndNovelReviewStatus(reader, AVAILABLE).isPresent()) {
            throw new INovelException(CANNOT_REVIEW_EXCESS_ONE);
        }
        if(novel.getReader().getId() == reader.getId()) {
            throw new INovelException(CANNOT_REVIEW_OWN_NOVEL);
        }
        if(novelScore < 1 || novelScore > 5) {
            throw new INovelException(WRONG_NOVEL_SCORE_VALUE);
        }

        novel.setTotalScore(novel.getTotalScore() + novelScore);
        novelRepository.save(novel);

        NovelReviewEntity novelReview = NovelReviewEntity.builder()
                .reader(reader)
                .review(review)
                .reviewerId(reader.getId())
                .reviewerName(reader.getUsername())
                .novel(novel)
                .novelScore(novelScore)
                .novelReviewStatus(AVAILABLE)
                .publisherType(READER)
                .build();
        novelReviewRepository.save(novelReview);
        return NovelReviewDto.of(novelReview.getId(), novelReview.getReviewerName(), novelReview.getReviewerId(), novelReview.getReview(), novelReview.getNovel().getId(), novelReview.getNovelScore(), novelReview.getPublisherType());
    }

    @Transactional
    public void deactivateNovelReview(ReaderEntity reader, Long novelId, Long reviewId) {
        NovelEntity novel = novelRepository.findById(novelId)
                .orElseThrow(() -> new INovelException(NOVEL_NOT_FOUND));
        NovelReviewEntity novelReview = novelReviewRepository.findById(reviewId)
                .orElseThrow(() -> new INovelException(NOVEL_REVIEW_NOT_FOUND));
        if(novelReview.getReviewerId() != reader.getId()) {
            throw new INovelException(UNMATCHED_USER_INFO);
        }
        if(novelReview.getNovel().getId() != novel.getId()) {
            throw new INovelException(UNMATCHED_NOVEL_REVIEW_INFO);
        }

        novelReview.setNovelReviewStatus(DEACTIVATED);
        novelReviewRepository.save(novelReview);

        novel.setTotalScore(novel.getTotalScore() - novelReview.getNovelScore());
        novelRepository.save(novel);
    }

    public List<NovelReviewDto> viewListOfNewNovelReview(long startIdx, long endIdx, Long novelId, String method, String order) {
        NovelEntity novel = novelRepository.findById(novelId)
                .orElseThrow(() -> new INovelException(NOVEL_NOT_FOUND));
        switch (method) {
            case "new":
                switch (order) {
                    case "asc" -> {
                        return novelReviewRepositoryQuerydsl.viewListOfNewNovelReviewByAsc(startIdx, endIdx, novel);
                    }
                    case "desc" -> {
                        return novelReviewRepositoryQuerydsl.viewListOfNewNovelReviewByDesc(startIdx, endIdx, novel);
                    }
                    default -> {
                    }
                }
            case "popular":
                switch (order) {
                    case "asc" -> {
                        return novelReviewRepositoryQuerydsl.viewListOfPopularNovelReviewByAsc(startIdx, endIdx, novel);
                    }
                    case "desc" -> {
                        return novelReviewRepositoryQuerydsl.viewListOfPopularNovelReviewByDesc(startIdx, endIdx, novel);
                    }
                    default -> {
                    }
                }
        }
        throw new INovelException(INVALID_PARAMETER_VALUE);
    }
}

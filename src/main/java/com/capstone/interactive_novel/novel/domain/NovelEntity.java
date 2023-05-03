package com.capstone.interactive_novel.novel.domain;

import com.capstone.interactive_novel.publisher.domain.PublisherEntity;
import com.capstone.interactive_novel.reader.domain.ReaderEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NovelEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private ReaderEntity reader;

    @ManyToOne
    private PublisherEntity publisher;

    @Column(unique = true)
    private String novelName;

    private Long totalScore;

    private String novelIntroduce;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private NovelStatus novelStatus;

    public static NovelEntity createNovelByReader(ReaderEntity reader, String novelName, String novelIntroduce, String imageUrl) {
        return NovelEntity.builder()
                .reader(reader)
                .novelName(novelName)
                .novelIntroduce(novelIntroduce)
                .imageUrl(imageUrl)
                .novelStatus(NovelStatus.FREE)
                .totalScore(0L)
                .build();
    }
}
package com.project.devgram.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewAccuse {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long reviewAccuseSeq;

	private String content;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
	private LocalDateTime reportAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reviewSeq")
	@JsonBackReference
	private Review review;

	private String username;

}

package com.project.devgram.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.project.devgram.type.ReviewCode;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "review")
public class Review implements ReviewCode {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long reviewSeq;
	private double mark;
	private String content;
	private String status;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
	private LocalDateTime createdAt;

	private String username;


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "productSeq")
	@JsonBackReference
	private Product product;


	@Builder.Default
	@OneToMany(mappedBy = "review")
	@JsonManagedReference
	private List<ReviewAccuse> reviewAccuseList = new ArrayList<>();
}

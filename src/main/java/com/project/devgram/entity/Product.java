package com.project.devgram.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.project.devgram.type.ProductCode;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product")
public class Product implements ProductCode {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long productSeq;
	private String title;
	private String content;
	private Integer hits;
	private double rating;
	private Integer likeCount;
	private double price;
	private String status;
	private int reviewCount;
	private double totalRating;

	private String imageUrl;

	@Builder.Default
	@OneToMany(mappedBy = "product")
	@JsonManagedReference
	private List<Review> reviewList = new ArrayList<>();

	@Builder.Default
	@OneToMany(mappedBy = "product")
	@JsonManagedReference
	private List<ProductLike> productLikes = new ArrayList<>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_Seq")
	@JsonBackReference
	private Category category;

	public void addReview(Review review) {
		this.reviewList.add(review);
	}
}

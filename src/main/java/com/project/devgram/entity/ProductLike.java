package com.project.devgram.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;


@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product_like")
public class ProductLike {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long productLikeSeq;

	private String username;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_seq")
	@JsonBackReference
	private Product product;

}

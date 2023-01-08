package com.project.devgram.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "category")
public class Category {

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	Long category_Seq;
	String name;
	String color;
	@Builder.Default
	@OneToMany(mappedBy = "category")
	@JsonManagedReference
	private List<Product> productList = new ArrayList<>();

}

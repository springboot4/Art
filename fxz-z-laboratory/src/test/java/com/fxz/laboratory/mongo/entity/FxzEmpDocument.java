package com.fxz.laboratory.mongo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

/**
 * 对应一个文档
 */
@Document("emp")
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class FxzEmpDocument {

	/**
	 * 映射文档中的_id
	 */
	@Id
	private Integer id;

	@Field("username")
	private String name;

	@Field
	private int age;

	@Field
	private Double salary;

	private Date birthday;

}
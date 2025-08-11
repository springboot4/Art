package com.art.ai.service.workflow.domain.node;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author fxz
 * @since 2025/8/10 17:27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NodeOutputVariable implements Serializable {

	private String name;

	private String dataType;

	private Object value;

}

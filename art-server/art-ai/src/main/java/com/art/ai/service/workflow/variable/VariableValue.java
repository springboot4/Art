package com.art.ai.service.workflow.variable;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * @author fxz
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({ @JsonSubTypes.Type(value = VariableValue.StringVariableValue.class, name = "string"),
		@JsonSubTypes.Type(value = VariableValue.NumberVariableValue.class, name = "number"),
		@JsonSubTypes.Type(value = VariableValue.BooleanVariableValue.class, name = "boolean"),
		@JsonSubTypes.Type(value = VariableValue.ObjectVariableValue.class, name = "object"),
		@JsonSubTypes.Type(value = VariableValue.ArrayVariableValue.class, name = "array"),
		@JsonSubTypes.Type(value = VariableValue.FileVariableValue.class, name = "file") })
public sealed interface VariableValue<T> {

	T getValue();

	String getType();

	boolean isReadOnly();

	default String asText() {
		return getValue() != null ? getValue().toString() : "";
	}

	record BooleanVariableValue(Boolean value, boolean readOnly) implements VariableValue<Boolean> {
		@Override
		public Boolean getValue() {
			return value;
		}

		@Override
		public String getType() {
			return "boolean";
		}

		@Override
		public boolean isReadOnly() {
			return readOnly;
		}
	}

	record StringVariableValue(String value, boolean readOnly) implements VariableValue<String> {
		@Override
		public String getValue() {
			return value;
		}

		@Override
		public String getType() {
			return "string";
		}

		@Override
		public boolean isReadOnly() {
			return readOnly;
		}
	}

	record ObjectVariableValue(Object value, boolean readOnly) implements VariableValue<Object> {
		@Override
		public Object getValue() {
			return value;
		}

		@Override
		public String getType() {
			return "object";
		}

		@Override
		public boolean isReadOnly() {
			return readOnly;
		}
	}

	record NumberVariableValue(Number value, boolean readOnly) implements VariableValue<Number> {
		@Override
		public Number getValue() {
			return value;
		}

		@Override
		public String getType() {
			return "number";
		}

		@Override
		public boolean isReadOnly() {
			return readOnly;
		}
	}

	record FileVariableValue(java.io.File value, boolean readOnly) implements VariableValue<java.io.File> {
		@Override
		public java.io.File getValue() {
			return value;
		}

		@Override
		public String getType() {
			return "file";
		}

		@Override
		public boolean isReadOnly() {
			return readOnly;
		}
	}

	record ArrayVariableValue(java.util.List<?> value, boolean readOnly) implements VariableValue<java.util.List<?>> {
		@Override
		public java.util.List<?> getValue() {
			return value;
		}

		@Override
		public String getType() {
			return "array";
		}

		@Override
		public boolean isReadOnly() {
			return readOnly;
		}
	}

}

package net.zomis.aiscores.extra;

import net.zomis.aiscores.FieldScore;

public class ParamAndField<Params, Field> {
	private final Params	params;
	private final Field	field;
	private final FieldScore<Field>	fieldScore;

	public ParamAndField(Params params, FieldScore<Field> field) {
		this.params = params;
		this.fieldScore = field;
		this.field = field.getField();
	}
	public Field getField() {
		return field;
	}
	public Params getParams() {
		return params;
	}
	public FieldScore<Field> getFieldScore() {
		return fieldScore;
	}
	@Override
	public String toString() {
		return field + " -- " + params;
	}
}

package net.zomis.aiscores.extra;

import java.util.List;

@Deprecated
public class ParamAndFieldList<Params, Field> {
	private List<Field>	list;
	private Params	param;

	@Deprecated
	public ParamAndFieldList(Params param, List<Field> fieldList) {
		this.param = param;
		this.list = fieldList;
	}
	
	public List<Field> getList() {
		return list;
	}
	public Params getParam() {
		return param;
	}
}

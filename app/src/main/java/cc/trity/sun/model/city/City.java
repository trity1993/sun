package cc.trity.sun.model.city;

import java.util.ArrayList;
import java.util.List;

public class City extends BasePlace {

	private List<County> countyList=new ArrayList<>();

	public List<County> getCountyList() {
		return countyList;
	}

	public void setCountyList(List<County> countyList) {
		this.countyList = countyList;
	}
}

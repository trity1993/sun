package cc.trity.sun.model.city;

public class County extends BasePlace {
	public County() {}

	public County(String placeName,String weaterCode) {
		this.placeName=placeName;
		this.weaterCode = weaterCode;
	}

	private String weaterCode;

	public String getWeaterCode() {
		return weaterCode;
	}

	public void setWeaterCode(String weaterCode) {
		this.weaterCode = weaterCode;
	}
}

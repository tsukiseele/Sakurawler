package map;

import com.tsukiseele.koracrawler.core.Bean;
import com.tsukiseele.utils.TextUtil;
import com.tsukiseele.utils.ObjectUtil;

public class Image extends Bean {
	public static final String URL_SMALLER = "smaller";
	public static final String URL_LARGER = "larger";
	public static final String URL_DEFAULT = "default";

	private String title;
	private String tags;
	private String catalogUrl;
	private String extraUrl;
	private String coverUrl;
	private String sampleUrl;
	private String largerUrl;
	private String originUrl;
	private String datetime;

	public void setExtraUrl(String extraUrl) {
		this.extraUrl = extraUrl;
	}

	public void setCatalogUrl(String catalogUrl) {
		this.catalogUrl = catalogUrl;
	}

	public void setCoverUrl(String coverUrl) {
		this.coverUrl = coverUrl;
	}

	public String getCoverUrl() {
		return coverUrl;
	}

	public void setSampleUrl(String sampleUrl) {
		this.sampleUrl = sampleUrl;
	}

	public String getSampleUrl() {
		return sampleUrl;
	}

	public void setLargerUrl(String largerUrl) {
		this.largerUrl = largerUrl;
	}

	public String getLargerUrl() {
		return largerUrl;
	}

	public void setOriginUrl(String originUrl) {
		this.originUrl = originUrl;
	}

	public String getOriginUrl() {
		return originUrl;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getTags() {
		return tags;
	}
	
	public String getTitle() {
		return title;
	}

	public String findSmallerUrl() {
		return TextUtil.nonEmpty(getSampleUrl()) ? getSampleUrl() : TextUtil.nonEmpty(getLargerUrl()) ? getLargerUrl() : getOriginUrl();
	}

	public String findLargerUrl() {
		return TextUtil.nonEmpty(getOriginUrl()) ? getOriginUrl() : TextUtil.nonEmpty(getLargerUrl()) ? getLargerUrl() : getSampleUrl();
	}

	public String getUrl(String flag) {
		switch (flag) {
			case URL_SMALLER :
				return findSmallerUrl();
			case URL_LARGER :
				return findLargerUrl();
			default :
				return TextUtil.nonEmpty(getLargerUrl()) ? getLargerUrl() : findSmallerUrl();
		}
	}

	@Override
	public String getCatalogUrl() {
		return catalogUrl;
	}

	@Override
	public String getExtraUrl() {
		return extraUrl;
	}

	@Override
	public String toString() {
		return TextUtil.toString(this);
	}

	@Override
	public int hashCode() {
		return TextUtil.toString(this).hashCode();
	}
}

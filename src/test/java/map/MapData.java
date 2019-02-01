package map;

import com.tsukiseele.koracrawler.core.MetaData;

import java.util.HashMap;
import java.util.Map;

public class MapData extends MetaData {
	private Map<String, String> metadata = new HashMap<>();
	
	@Override
	public Map<String, String> getMetaData() {
		return metadata;
	}
	
	@Override
	public String getCatalogUrl() {
		return metadata.get("catalogUrl");
	}
	
	@Override
	public String getExtraUrl() {
		return metadata.get("extraUrl");
	}
}

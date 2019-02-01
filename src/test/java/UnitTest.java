import com.google.gson.JsonParseException;
import com.tsukiseele.koracrawler.KoraCrawler;
import com.tsukiseele.koracrawler.bean.Gallery;
import com.tsukiseele.koracrawler.bean.Site;
import com.tsukiseele.koracrawler.core.DocumentParser;
import com.tsukiseele.utils.IOUtil;
import map.MapData;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UnitTest {
	@Test
	public void test() {
		File root = new File(System.getProperty("user.dir"));
		List<File> files = IOUtil.scanDirectory(new File(root, "resource/rules"));
		List<Site> sites = new ArrayList<>();
		for (File file : files) {
			try {
				sites.add(Site.fromJSON(IOUtil.readText(file.getAbsolutePath())));
			} catch (Exception e) {
				System.out.println(file);
				throw new JsonParseException("json parse error: " + file.getName(), e);
			}
		}
		
		KoraCrawler crawler = new KoraCrawler(sites.get(2));
		crawler.setPageCode(1);
		DocumentParser<MapData> parser = crawler.buildParser(MapData.class);
		
		try {
			Gallery<MapData> gallery = parser.parseGallery();
			for (MapData image : gallery) {
				System.out.println(image.getMetaData());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}

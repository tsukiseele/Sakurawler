import com.tsukiseele.koracrawler.KoraCrawler;
import com.tsukiseele.koracrawler.bean.Site;
import com.tsukiseele.utils.OkHttpUtil;
import com.tsukiseele.utils.TextUtil;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Map;

public class MoeCrawler extends KoraCrawler {
	private Site site;
	private Mode mode;
	
	public MoeCrawler(Site site) {
		super(site);
		this.site = site;
	}
	
	public static KoraCrawler with(Site site) {
		KoraCrawler crawler = new KoraCrawler(site);
		return crawler;
	}
	
	// 使用指定的模式爬取
	public KoraCrawler params(int pageCode) {
		this.mode = new Mode(TYPE_HOME, pageCode, null, null, null);
		return this;
	}
	
	public KoraCrawler params(int pageCode, String keyword) {
		if (TextUtil.isEmpty(keyword))
			return params(pageCode);
		this.mode = new Mode(TYPE_SEARCH, pageCode, keyword, null, null);
		return this;
	}
	
	public KoraCrawler params(int pageCode, String extraKey, String extraData) {
		if (TextUtil.isEmpty(extraKey))
			return params(pageCode);
		this.mode = new Mode(TYPE_EXTRA, pageCode, null, extraKey, extraData);
		return this;
	}
	public InputStream requestByteStream(String url) throws IOException {
		return requestByteStream(url, getHeaders());
	}
	
	public InputStream requestByteStream(String url, Map<String, String> headers) throws IOException {
		OkHttpClient client = OkHttpUtil.getOkHttpClient();
		Request request = new Request.Builder()
				.headers(Headers.of(headers))
				.url(url)
				.get()
				.build();
		Response response = client.newCall(request).execute();
		return response.body().byteStream();
	}
	
	public Reader requestCharStream(String url) throws IOException {
		
		return requestCharStream(url, getHeaders());
	}
	
	public Reader requestCharStream(String url, Map<String, String> headers) throws IOException {
		OkHttpClient client = OkHttpUtil.getOkHttpClient();
		Request request = new Request.Builder()
				.headers(Headers.of(headers))
				.url(url)
				.get()
				.build();
		Response response = client.newCall(request).execute();
		return response.body().charStream();
	}
	
	@Override
	protected String request(String url) throws IOException {
		System.out.println("Request " + url + " ............ ");
		
		if (site.hasFlag(Site.FLAG_LOAD_JS)) {
//			try {
//				return loadJavaScript(url);
//			} catch (Exception e) {
//				e.printStackTrace();
			return requestDocument(url);
//			}
		} else {
			return requestDocument(url);
		}
	}
	
	private String requestDocument(String url) throws IOException {
		// 请求网页
		OkHttpClient client = OkHttpUtil.getOkHttpClient();
		Request request = new Request.Builder()
				.headers(Headers.of(getHeaders()))
				.url(url)
				.get()
				.build();
		Response response = client.newCall(request).execute();
		return response.body().string();
	}
}

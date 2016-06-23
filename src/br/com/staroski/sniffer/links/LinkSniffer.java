package br.com.staroski.sniffer.links;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class LinkSniffer {

	private class Download implements Runnable {

		private final Link link;

		Download(Link link) {
			this.link = link;
		}

		@Override
		public void run() {
			try {
				link.downloadTo(downloadDir);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private File downloadDir;
	private String[] extensions;

	private String url;

	public LinkSniffer(String downloadDir, String url, String... extensions) throws MalformedURLException {
		this.downloadDir = new File(downloadDir);
		this.url = url;
		int count = extensions.length;
		this.extensions = new String[count];
		for (int i = 0; i < count; i++) {
			this.extensions[i] = extensions[i].toLowerCase();
		}
	}

	public void execute() throws Exception {
		List<Link> links = extractLinks();
		Thread[] downloads = prepareDownloads(links);
		for (Thread download : downloads) {
			download.start();
		}
		for (Thread download : downloads) {
			if (download.isAlive()) {
				download.join();
			}
		}
	}

	private boolean accept(String href) {
		if (extensions.length < 1) {
			return true;
		}
		href = href.toLowerCase();
		for (String extension : extensions) {
			if (href.endsWith(extension)) {
				return true;
			}
		}
		return false;
	}

	private List<Link> extractLinks() throws Exception {
		List<Link> links = new ArrayList<>();
		Connection connection = Jsoup.connect(url);
		Document document = connection.get();
		Elements elements = document.select("a");
		for (Element element : elements) {
			String href = element.attr("href");
			if (accept(href)) {
				links.add(new Link(this.url, href));
			}
		}
		return links;
	}

	private Thread[] prepareDownloads(List<Link> links) {
		int count = links.size();
		Thread[] downloads = new Thread[count];
		for (int i = 0; i < count; i++) {
			Link link = links.get(i);
			downloads[i] = new Thread(new Download(link), "Downloading " + link);
		}
		return downloads;
	}
}

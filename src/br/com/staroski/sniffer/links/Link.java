package br.com.staroski.sniffer.links;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class Link {

	public final String href;
	public final String url;

	public Link(String url, String href) {
		this.url = url;
		this.href = href;
	}

	public void downloadTo(File dir) throws Exception {
		downloadTo(dir, false);
	}

	public void downloadTo(File dir, boolean overwrite) throws Exception {
		if (!dir.exists()) {
			dir.mkdirs();
		} else if (dir.isFile()) {
			throw new IllegalArgumentException(String.format("\"%s\" is not a directory!", dir.getAbsolutePath()));
		}
		String urlToConnect = href;
		URL url = null;
		try {
			url = new URL(urlToConnect);
		} catch (MalformedURLException wrongUrl) {
			urlToConnect = this.url + href;
			url = new URL(urlToConnect);
		}
		File file = new File(dir, urlToConnect.substring(urlToConnect.lastIndexOf('/')));
		if (file.exists() && !overwrite) {
			return;
		}
		InputStream input = url.openStream();
		OutputStream output = new FileOutputStream(file);
		copy(input, output);
		input.close();
		output.close();
	}

	@Override
	public String toString() {
		return String.format("%s[url: \"%s\", href: \"%s\"]", getClass().getSimpleName(), url, href);
	}

	private void copy(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[8192];
		for (int read = -1; (read = in.read(buffer)) != -1; out.write(buffer, 0, read)) {}
		out.flush();

	}
}

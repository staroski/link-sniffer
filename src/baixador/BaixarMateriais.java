package baixador;

import org.jsoup.HttpStatusException;

import br.com.staroski.sniffer.links.LinkSniffer;

public class BaixarMateriais {

	public static void main(String[] args) {
		try {
			BaixarMateriais programa = new BaixarMateriais();
			programa.executar();
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			System.exit(0);
		}
	}

	private void executar() throws Exception {
		try {
			String[] tipos = new String[] { ".pdf", ".xlsm", ".xls", ".doc", ".docx", ".mp4" };
			String pagina = "<página web onde serão procurados os links de arquivos para baixar>";
			String pasta = "<diretório onde os arquivos serão salvos>";
			System.out.print(pagina);
			LinkSniffer sniffer = new LinkSniffer(pasta, pagina, tipos);
			sniffer.execute();
			System.out.println("    OK");
		} catch (HttpStatusException httpError) {
			System.err.println("    Erro " + httpError.getStatusCode());
		}
	}
}

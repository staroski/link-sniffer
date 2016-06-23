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

	private void executar() {
		String downloads = "C:\\Users\\ricardo.staroski\\Downloads\\download-sniffer";
		String site = "http://tesourodiretosemsegredos.com.br/wp-content/uploads";
		for (int ano = 2014; ano <= 2016; ano++) {
			for (int mes = 1; mes <= 12; mes++) {
				String pasta = String.format("%s/%04d/%02d/", downloads, ano, mes);
				String pagina = String.format("%s/%04d/%02d/", site, ano, mes);
				System.out.print(pagina);
				try {
					String[] extensoes = new String[] { ".pdf", ".xlsm", ".xls", ".doc", ".docx", ".mp4" };
					LinkSniffer sniffer = new LinkSniffer(pasta, pagina, extensoes);
					sniffer.execute();
					System.out.println("    OK");
				} catch (HttpStatusException httpError) {
					System.err.println("    Erro " + httpError.getStatusCode());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}

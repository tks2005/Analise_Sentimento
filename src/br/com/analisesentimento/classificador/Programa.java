package br.com.analisesentimento.classificador;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.analisesentimento.DAO.TweetDAO;
import br.com.analisesentimento.classes.Dia;
import br.com.analisesentimento.twitterUtils.ConverteStatusParaTweet;
import br.com.analisesentimento.twitterUtils.FerramentaTwitter;
import br.com.analisesentimento.utils.GeradorKRI;
import br.com.analisesentimento.utils.LeitorArquivos;
import br.com.analisesentimento.utils.LimpadorTexto;
import twitter4j.Status;

public class Programa extends Thread {

	FerramentaTwitter exe;
	List<Status> todosResultados;
	List<String> termos;

	public void run() {

		// while (true) {
		//
		// alimentaBase();
		// System.out.println("DADOS RECOLHIDOS");
		// SimpleDateFormat format = new
		// SimpleDateFormat("dd/MM/yyyy - hh:mm:ss");
		// String hora = format.format(new Date());
		// System.out.println(hora);
		// try {
		// sleep(20 * 60 * 1000); // 20 min
		// }
		// catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// }

		// imprimeClassificacaoDiaria();
		avaliacaoKFold(5);
		// imprimeClassificacaoDiaria();
	}

	// public void run () {
	//
	// while (true) {
	//
	// try {
	// todosResultados = exe.getTweets(termos);
	// }
	// catch (TwitterException e1) {
	// e1.printStackTrace();
	// }
	//
	// alimentaBase(todosResultados);
	//
	// System.out.println("DADOS RECOLHIDOS");
	// SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy - hh:mm:ss");
	// String hora = format.format(new Date());
	// System.out.println(hora);
	//
	// try {
	// sleep(60 * 60 * 1000);
	// }
	// catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// }

	public void alimentaBase() {
		List<Status> todosResultados = new ArrayList<Status>();
		if (exe == null)
			exe = new FerramentaTwitter();

		try {
			todosResultados.addAll(exe.getTweets("sustentabilidade"));
			todosResultados.addAll(exe.getTweets("sustentavel"));
			todosResultados.addAll(exe.getTweets("biologia"));
			todosResultados.addAll(exe.getTweets("ecosofia"));
			todosResultados.addAll(exe.getTweets("desflorestamento"));
			todosResultados.addAll(exe.getTweets("terremoto"));
			todosResultados.addAll(exe.getTweets("mst"));
			todosResultados.addAll(exe.getTweets("ambiente"));
			todosResultados.addAll(exe.getTweets("desmatamento"));
			todosResultados.addAll(exe.getTweets("amazonia"));
			todosResultados.addAll(exe.getTweets("erosão"));
			todosResultados.addAll(exe.getTweets("desastre"));
			todosResultados.addAll(exe.getTweets("rompimento"));
			todosResultados.addAll(exe.getTweets("ecologia"));
			todosResultados.addAll(exe.getTweets("extinção"));
			todosResultados.addAll(exe.getTweets("meio-ambiente"));
			todosResultados.addAll(exe.getTweets("preservação"));
			todosResultados.addAll(exe.getTweets("animal"));
			todosResultados.addAll(exe.getTweets("aves"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (Status status : todosResultados) {
			TweetDAO dao = new TweetDAO();
			try {
				ConverteStatusParaTweet convert = new ConverteStatusParaTweet();
				dao.insertTweet(convert.converte(status));
			} catch (Exception e) {
				// e.printStackTrace();
			}
		}
	}

	public void alimentaBase(List<Status> todosResultados) {

		for (Status status : todosResultados) {
			TweetDAO dao = new TweetDAO();
			try {
				ConverteStatusParaTweet convert = new ConverteStatusParaTweet();
				dao.insertTweet(convert.converte(status));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void avaliacaoKFold(int k) {
		Classificador clas = new Classificador();
		List<String> positivas = LeitorArquivos.getListaPositivasTreinamento();
		List<String> negativas = LeitorArquivos.getListaNegativasTreinamento();
		List<String> neutras = LeitorArquivos.getListaNeutrasTreinamento();
		LimpadorTexto limp = new LimpadorTexto();
		limp.limpaLista(neutras);
		limp.limpaLista(negativas);
		limp.limpaLista(positivas);
		List<String[]> positivasPonderadas = clas.getListaPonderada(positivas,
				"positivo");
		List<String[]> negativasPonderadas = clas.getListaPonderada(negativas,
				"negativo");
		List<String[]> neutrasPonderadas = clas.getListaPonderada(neutras,
				"neutro");
		List<String[]> aux = new ArrayList<String[]>();
		aux.addAll(positivasPonderadas);
		aux.addAll(negativasPonderadas);
		aux.addAll(neutrasPonderadas);
		Collections.shuffle(aux);

		int tamanho = aux.size() / k;
		int tempAux = 0;
		int fator = tamanho;

		List<List<String[]>> lista = new ArrayList<List<String[]>>();

		for (int i = 0; i < k; i++) {
			List<String[]> temp = aux.subList(tempAux, tamanho);
			lista.add(temp);
			tempAux = tamanho;
			tamanho = tamanho + fator;
		}

		for (int i = 0; i < k; i++) {
			List<String[]> tweetsTreinamento = new ArrayList<String[]>();

			for (int j = 0; j < k; j++) {
				if (j != i)
					tweetsTreinamento.addAll(lista.get(j));
			}

			clas.treinamentoBayesianoPonderado(tweetsTreinamento);
			List<String> testePositivas = LeitorArquivos.getLista("risco4Positivas.txt");
			List<String> testeNegativas = LeitorArquivos.getLista("risco4Negativas.txt");
			List<String> testeNeutras = LeitorArquivos.getLista("risco4Neutras.txt");
			List<String[]> testepositivasPonderadas = clas.getListaPonderada(testePositivas, "positivo");
			List<String[]> testenegativasPonderadas = clas.getListaPonderada(testeNegativas, "negativo");
			List<String[]> testeneutrasPonderadas = clas.getListaPonderada(testeNeutras, "neutro");
			List<String[]> testeAux = new ArrayList<String[]>();
			testeAux.addAll(testepositivasPonderadas);
			testeAux.addAll(testenegativasPonderadas);
			testeAux.addAll(testeneutrasPonderadas);
			List<String[]> temp = clas.classificaTweetsPonderadoBayes(testeAux);
			
//			List<String[]> temp = clas.classificaTweetsPonderadoBayes(lista
//					.get(i));

			System.out.println("############################");
			System.out.println("POSITIVAS: " + clas.positivas);
			System.out.println("NEGATIVAS: " + clas.negativas);
			System.out.println("NEUTRAS: " + clas.neutras);
			System.out.println("VERDADEIROS POSITIVOS: "
					+ clas.verdadeiroPositivo);
			System.out.println("VERDADEIROS NEGATIVOS: "
					+ clas.verdadeiroNegativo);
			System.out.println("VERDADEIROS NEUTROS: " + clas.verdadeiroNeutro);
			System.out.println("FALSOS POSITIVOS: " + clas.falsoPositivo);
			System.out.println("FALSOS NEGATIVOS: " + clas.falsoNegativo);
			System.out.println("FALSOS NEUTROS: " + clas.falsoNeutro);
			System.out.println("ACERTOS: " + clas.acertos);
			System.out.println("ERROS: " + clas.erros);
			System.out.println("TWEETS TREINAMENTO: "
					+ tweetsTreinamento.size());
			System.out.println("TWEETS TESTE: " + lista.get(i).size());
			double a = lista.get(i).size();
			double b = clas.acertos;
			double c = b / a;
			System.out.println("PRECISAO POSITIVO: " + clas.verdadeiroPositivo
					/ (clas.verdadeiroPositivo + clas.falsoPositivo));
			System.out.println("PRECISAO NEGATIVO: " + clas.verdadeiroNegativo
					/ (clas.verdadeiroNegativo + clas.falsoNegativo));
			System.out.println("PRECISAO NEUTRO: " + clas.verdadeiroNeutro
					/ (clas.verdadeiroNeutro + clas.falsoNeutro));
			System.out
					.println("RECALL POSITIVO: "
							+ clas.verdadeiroPositivo
							/ (clas.verdadeiroPositivo + clas.falsoNegativo + clas.falsoNeutro));
			System.out
					.println("RECALL NEGATIVO: "
							+ clas.verdadeiroNegativo
							/ (clas.verdadeiroNegativo + clas.falsoPositivo + clas.falsoNeutro));
			System.out
					.println("RECALL NEUTRO: "
							+ clas.verdadeiroNeutro
							/ (clas.verdadeiroNeutro + clas.falsoPositivo + clas.falsoNegativo));
			System.out.println("ACURACIA: " + calculaAcuracia(clas));

			System.out.println("");
			clas = new Classificador();
		}

	}

	public double calculaAcuracia(Classificador clas) {
		double acuracia = (clas.verdadeiroNegativo + clas.verdadeiroNeutro + clas.verdadeiroPositivo)
				/ (clas.acertos + clas.erros);
		return acuracia;
	}

	public void imprimeTweetsBayes(List<Status> tweets) {
		LimpadorTexto limp = new LimpadorTexto();
		Classificador classificador = new Classificador();

		classificador.treinaPonderado();
		List<String> sentencas = limp.ajustaTweets(tweets);
		List<String[]> classificados = classificador
				.classificaTweetsBayes(sentencas);

		for (String[] st : classificados) {
			System.out.print(st[1]);
			System.out.print("\t");
			System.out.print("\t");
			System.out.print(st[0]);
			System.out.println("");
		}
		System.out.println("POSITIVOS: " + classificador.positivas);
		System.out.println("NEGATIVOS: " + classificador.negativas);
		System.out.println("NEUTRAS: " + classificador.neutras);
	}

	public void imprimeTweetsScore(List<Status> tweets) {
		LimpadorTexto limp = new LimpadorTexto();
		Classificador classificador = new Classificador();

		List<String> sentencas = limp.ajustaTweets(tweets);
		List<String[]> classificados = classificador
				.classificaTweetsScore(sentencas);

		for (String[] st : classificados) {
			System.out.print(st[1]);
			System.out.print("\t");
			System.out.print("\t");
			System.out.print(st[0]);
			System.out.println("");
		}
		System.out.println("POSITIVOS: " + classificador.positivas);
		System.out.println("NEGATIVOS: " + classificador.negativas);
		System.out.println("NEUTRAS: " + classificador.neutras);

	}

	public void imprimeClassificacaoDiaria() {

		System.out.println("CLASSIFICACAO DE TWEETS: ");
		System.out.println("");
		Classificador classificador = new Classificador();
		classificador.treinaPonderado();

		List<Dia> dias = new ArrayList<Dia>();

		for (int i = 1; i <= 12; i++) {
			String arquivo = "dia" + i;
			List<String> tweetsDia = LeitorArquivos.getDias(arquivo);
			classificador.classificaTweetsBayes(tweetsDia);
			Dia dia = new Dia(classificador.positivas, classificador.negativas,
					classificador.neutras);
			// System.out.println("DIA " + i);
			// System.out.println("Positivas: " + classificador.positivas);
			// System.out.println("Negativas: " + classificador.negativas);
			// System.out.println("Neutras: " + classificador.neutras);
			// int total = classificador.positivas + classificador.neutras +
			// classificador.negativas;
			// System.out.println("Total: " + total);
			// System.out.println("##################################");
			// System.out.println("");
			// System.out.println("");
			dias.add(dia);
			classificador.zera();
		}

		GeradorKRI gerador = new GeradorKRI();
		// gerador.calculaEvolucaoKRI(dias);
		// gerador.calculaKRI(dias);
		gerador.calculaMME(dias);
		gerador.calculaMMA(dias);

	}

}

package br.com.analisesentimento.utils;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Status;
import br.com.analisesentimento.classes.Tweet;

public class LimpadorTexto {

  public static List<String> ajustaTweets (List<Status> tweets) {
    List<String> tweetsLimpos = new ArrayList<String>();

    for (Status tweet : tweets) {
      if (!tweet.isRetweet()) {
        String tw = tweet.getText();
        tw = tw.toLowerCase();
        tw = retiraLinks(tw);
        tw = corrigeString(tw);
        tw = retiraAcentuacao(tw);
        tweetsLimpos.add(tw);
      }
    }

    return tweetsLimpos;
  }

  public static List<String> ajustaTweetsTreinamento (List<Tweet> tweets) {

    List<String> tws = new ArrayList<String>();
    
    for (Tweet tweet : tweets) {
      String tw = tweet.getTweet();
      tw = tw.toLowerCase();
      tw = retiraLinks(tw);
      tw = corrigeString(tw);
      tw = retiraAcentuacao(tw);
      tws.add(tw);
    }
    return tws;
  }

  public void limpaLista (List<String> lista) {
    for (String st : lista) {
      st = corrigeString(st);
      st = retiraAcentuacao(st);
    }

  }

  public static String retiraLinks (String sentenca) {
    if (sentenca.contains("http://")) {
      int i = sentenca.lastIndexOf("http://");
      int j;
      for (j = i; j < sentenca.length(); j++) {
        String s = String.valueOf(sentenca.charAt(j));
        if (s.equals(" "))
          break;
      }
      String s = sentenca.substring(i, j);
      sentenca = sentenca.replace(s, "");
    }
    if (sentenca.contains("https://")) {
      int i = sentenca.lastIndexOf("https://");
      int j;
      for (j = i; j < sentenca.length(); j++) {
        String s = String.valueOf(sentenca.charAt(j));
        if (s.equals(" "))
          break;
      }
      String s = sentenca.substring(i, j);
      sentenca = sentenca.replace(s, "");

    }
    if (sentenca.contains("migre.me")) {
      int i = sentenca.lastIndexOf("migre.me");
      int j;
      for (j = i; j < sentenca.length(); j++) {
        String s = String.valueOf(sentenca.charAt(j));
        if (s.equals(" "))
          break;
      }
      String s = sentenca.substring(i, j);
      sentenca = sentenca.replace(s, "");

    }

    return sentenca;
  }

  public static String corrigeString (String sentenca) {
    sentenca = sentenca.replace(".", "");
    sentenca = sentenca.replace(",", "");
    sentenca = sentenca.replace(":", "");
    sentenca = sentenca.replace(";", "");
    sentenca = sentenca.replace("!", "");
    sentenca = sentenca.replace("$", "");
    sentenca = sentenca.replace("%", "");
    sentenca = sentenca.replace("&", "");
    sentenca = sentenca.replace("*", "");
    sentenca = sentenca.replace("(", "");
    sentenca = sentenca.replace(")", "");
    sentenca = sentenca.replace("=", "");
    sentenca = sentenca.replace("+", "");
    sentenca = sentenca.replace("§", "");
    sentenca = sentenca.replace("/", "");
    sentenca = sentenca.replace("?", "");
    sentenca = sentenca.replace("[", "");
    sentenca = sentenca.replace("{", "");
    sentenca = sentenca.replace("~", "");
    sentenca = sentenca.replace("^", "");
    sentenca = sentenca.replace("]", "");
    sentenca = sentenca.replace("}", "");
    sentenca = sentenca.replace("\\", "");
    sentenca = sentenca.replace("|", "");
    sentenca = sentenca.replace("<", "");
    sentenca = sentenca.replace(">", "");
    sentenca = sentenca.replace("-", "");
    sentenca = sentenca.replace("_", "");
    sentenca = sentenca.replace("RT", "");
    sentenca = sentenca.replace("\"", "");
    sentenca = sentenca.replace("'", "");

    return sentenca;
  }

  public static String retiraAcentuacao (String sentenca) {

    sentenca = sentenca.replace("ç", "c");
    sentenca = sentenca.replace("à", "a");
    sentenca = sentenca.replace("á", "a");
    sentenca = sentenca.replace("ã", "a");
    sentenca = sentenca.replace("â", "a");

    sentenca = sentenca.replace("è", "e");
    sentenca = sentenca.replace("é", "e");
    sentenca = sentenca.replace("ê", "e");

    sentenca = sentenca.replace("ì", "i");
    sentenca = sentenca.replace("í", "i");
    sentenca = sentenca.replace("î", "i");

    sentenca = sentenca.replace("ò", "o");
    sentenca = sentenca.replace("ó", "o");
    sentenca = sentenca.replace("ô", "o");
    sentenca = sentenca.replace("õ", "o");

    sentenca = sentenca.replace("ú", "u");
    sentenca = sentenca.replace("ù", "u");
    sentenca = sentenca.replace("û", "u");

    return sentenca;
  }

}

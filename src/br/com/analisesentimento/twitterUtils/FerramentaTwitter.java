package br.com.analisesentimento.twitterUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

public class FerramentaTwitter {

  private Twitter twitter;

  public List<Status> getTweets (String termo) throws TwitterException {
    Twitter twitter = getTwitter();
    List<Status> todosResultados = new ArrayList<Status>();
    Query query = new Query(termo);
    query.lang("pt");
    query.count(100);
    QueryResult result = twitter.search(query);
    while (result.hasNext()) {
      todosResultados.addAll(result.getTweets());
      query = result.nextQuery();
      result = twitter.search(query);
    }
    return todosResultados;
  }

  public List<Status> getTweets (List<String> termos) throws TwitterException {
    Twitter twitter = getTwitter();
    List<Status> todosResultados = new ArrayList<Status>();
    
    for (String termo : termos) {
      Query query = new Query();
      query.setQuery(termo);
      query.lang("pt");
      query.count(100);
      QueryResult result = twitter.search(query);
      while (result.hasNext()) {
        todosResultados.addAll(result.getTweets());
        query = result.nextQuery();
        result = twitter.search(query);
      }
    }
    return todosResultados;
  }

  public List<Status> getTimeline (String usuario) {
    Twitter twitter = getTwitter();
    List<Status> timeline = new ArrayList<Status>();
    try {
      for (int i = 1; i < 10; i++) {
        Paging pg = new Paging(i);
        timeline.addAll(twitter.getUserTimeline(usuario, pg));
      }

    }
    catch (TwitterException e) {
      e.printStackTrace();
    }
    return timeline;
  }

  private Twitter getTwitter () {

    if (twitter == null) {
      try {
        twitter = TwitterFactory.getSingleton();
        AccessToken token = recoveryAcessToken();
        twitter.setOAuthConsumer("lPA5tHrNwL5OCtur3wA", "BSyQ4lyOChGGRPcJHE4G6jAdDmT1J8gjhp8JjrVg");
        twitter.setOAuthAccessToken(token);
      }
      catch (ClassNotFoundException e) {
        e.printStackTrace();
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    }
    return twitter;
  }

  public void obterPIN () throws TwitterException, IOException {
    Twitter twitter = TwitterFactory.getSingleton();
    twitter.setOAuthConsumer("lPA5tHrNwL5OCtur3wA", "BSyQ4lyOChGGRPcJHE4G6jAdDmT1J8gjhp8JjrVg");
    RequestToken requestToken = twitter.getOAuthRequestToken();
    AccessToken accessToken = null;
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    while (null == accessToken) {
      System.out.println("Open the following URL and grant access to your account:");
      System.out.println(requestToken.getAuthorizationURL());
      System.out.print("Enter the PIN(if aviailable) or just hit enter.[PIN]:");
      String pin = br.readLine();
      try {
        if (pin.length() > 0) {
          accessToken = twitter.getOAuthAccessToken(requestToken, pin);
        }
        else {
          accessToken = twitter.getOAuthAccessToken();
        }
      }
      catch (TwitterException te) {
        if (401 == te.getStatusCode()) {
          System.out.println("Unable to get the access token.");
        }
        else {
          te.printStackTrace();
        }
      }
    }

    storeAccessToken(accessToken);
  }

  private static void storeAccessToken (AccessToken accessToken) throws IOException {
    File token = new File("token.txt");
    FileOutputStream out = new FileOutputStream(token);
    ObjectOutputStream objOut = new ObjectOutputStream(new BufferedOutputStream(out));
    objOut.writeObject(accessToken);
    objOut.close();

  }

  private static AccessToken recoveryAcessToken () throws IOException, ClassNotFoundException {
    FacesContext context = FacesContext.getCurrentInstance();
    ServletContext serv = (ServletContext) context.getExternalContext().getContext();
    String temp = serv.getRealPath("token.txt");
    File token = new File(temp);
    FileInputStream in = new FileInputStream(token);
    ObjectInputStream objIn = new ObjectInputStream(new BufferedInputStream(in));
    AccessToken accessToken = (AccessToken) objIn.readObject();
    objIn.close();

    return accessToken;

  }

}

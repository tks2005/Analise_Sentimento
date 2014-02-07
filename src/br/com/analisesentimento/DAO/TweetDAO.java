package br.com.analisesentimento.DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.analisesentimento.classes.Tweet;
import br.com.analisesentimento.utils.BDUtil;

/**
 * @author richard.santana
 * 
 */
public class TweetDAO {

  public void insertTweet (Tweet tweet) {
    String sql = "insert into TWEETS (ID_TWEET,ID_USER,DATA_CRIACAO,NOME_USER,QTDE_FAVORITOS,QTDE_RETWEETS,TWEET,EH_RETWEET) values (?,?,?,?,?,?,?,?)";
    Connection conn = BDUtil.getInstance();
    PreparedStatement ps;
    try {
      ps = conn.prepareStatement(sql);

      ps.setLong(1, tweet.getId());
      ps.setLong(2, tweet.getIdUser());
      Date data = new Date(tweet.getDataCriacao().getTimeInMillis());
      ps.setDate(3, data);
      ps.setString(4, tweet.getNomeUser());
      ps.setInt(5, tweet.getFavoritos());
      ps.setInt(6, tweet.getRetweets());
      ps.setString(7, tweet.getTweet());
      ps.setBoolean(8, tweet.getEhRetweet());
      ps.execute();
      conn.commit();
    }
    catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public List<Tweet> getIntervaloData (int risco, Date dataInicio, Date dataFim) {
    List<Tweet> tweets = new ArrayList<Tweet>();
    Connection conn = BDUtil.getInstance();
    String sql = "SELECT * FROM twitter_all AS t INNER JOIN TMP_HT_Risco AS r ON t.origem = r.id_ht WHERE r.id_risco = ? AND t.created_at BETWEEN ? AND ?";
    try {
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setInt(1, risco);;
      ps.setDate(2, dataInicio);
      ps.setDate(3, dataFim);
      ResultSet rs = ps.executeQuery();
      
      while (rs.next()){
        Tweet tweet = new Tweet();
        tweet.setTweet(rs.getString("tweet_text"));
        tweet.setNomeUser(rs.getString("usuario_screen_name"));
        tweet.setId(rs.getLong("id"));
        tweet.setClassificacao(2);
        Date data = rs.getDate("created_at");
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        tweet.setDataCriacao(cal);
        
        tweets.add(tweet);
      }
    }
    catch (SQLException e) {
      e.printStackTrace();
    }

    return tweets;
  }

}

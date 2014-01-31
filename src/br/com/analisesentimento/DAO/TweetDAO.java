package br.com.analisesentimento.DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import br.com.analisesentimento.classes.Tweet;
import br.com.analisesentimento.utils.BDUtil;

/**
 * @author richard.santana
 * 
 */
public class TweetDAO {

	public void insertTweet(Tweet tweet) {
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
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}

package kr.kkiro.projects.bukkit.EntityProtect.utils.database;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.avaje.ebean.validation.NotEmpty;

@Entity()
@Table(name = "ep_players")
public class PlayerSet {
	@Id
	@GeneratedValue
	private long id;

	@NotEmpty
	private String player;

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return this.id;
	}

	public void setPlayer(String player) {
		this.player = player;
	}

	public String getPlayer() {
		return this.player;
	}
}

package kr.kkiro.projects.bukkit.EntityProtect.utils.database;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.avaje.ebean.validation.NotEmpty;
import com.avaje.ebean.validation.NotNull;

@Entity()
@Table(name = "ep_players")
public class PlayerSet {
	@Id
	@GeneratedValue
	private long id;

	@NotNull
	private int breedCount;
	
	@NotEmpty
	private String player;

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return this.id;
	}
	
	public void setBreedCount(int breedCount) {
		this.breedCount = breedCount;
	}

	public int getBreedCount() {
		return this.breedCount;
	}

	public void setPlayer(String player) {
		this.player = player;
	}

	public String getPlayer() {
		return this.player;
	}
}

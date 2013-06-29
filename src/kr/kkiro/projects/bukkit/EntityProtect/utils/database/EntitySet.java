package kr.kkiro.projects.bukkit.EntityProtect.utils.database;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.validation.NotEmpty;
import com.avaje.ebean.validation.NotNull;

@Entity()
@Table(name = "ep_entities")
public class EntitySet {
	@Id
	@GeneratedValue
	private long id;

	@NotNull
	private UUID entity;

	@NotNull
	private PlayerSet owner;

	@OneToMany
	private List<PlayerSet> members;

	@Version
	Timestamp regtime;

	@NotNull
	private double lastX;

	@NotNull
	private double lastY;

	@NotNull
	private double lastZ;

	@NotEmpty
	private String lastWorld;

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return this.id;
	}

	public void setEntity(UUID entity) {
		this.entity = entity;
	}

	public UUID getEntity() {
		return this.entity;
	}

	public void setOwner(PlayerSet owner) {
		this.owner = owner;
	}

	public PlayerSet getOwner() {
		return this.owner;
	}

	public void setOwner(String owner) {
		PlayerSet playerset = Ebean.find(PlayerSet.class).where()
				.eq("player", owner).findUnique();
		if (playerset == null) {
			playerset = new PlayerSet();
			playerset.setPlayer(owner);
			playerset.setBreedCount(0);
			DatabaseUtils.save(playerset);
		}
		this.setOwner(playerset);
	}

	public void setMembers(List<PlayerSet> members) {
		this.members = members;
	}

	public void setMembersString(String[] members) {
		List<String> players = new ArrayList<String>();
		for (int i = 0; i < members.length; ++i) {
			players.add(members[i]);
		}
		setMembersString(players);
	}

	public void setMembersString(List<String> members) {
		List<PlayerSet> players = new ArrayList<PlayerSet>();
		int size = members.size();
		for (int i = 0; i < size; ++i) {
			PlayerSet playerset = Ebean.find(PlayerSet.class).where()
					.eq("player", owner).findUnique();
			if (playerset == null) {
				playerset = new PlayerSet();
				playerset.setPlayer(members.get(i));
				playerset.setBreedCount(0);
				DatabaseUtils.save(playerset);
			}
			players.add(playerset);
		}
		this.members = players;
	}

	public List<PlayerSet> getMembers() {
		return this.members;
	}

	public List<String> getMembersString() {
		List<String> players = new ArrayList<String>();
		int size = members.size();
		for (int i = 0; i < size; ++i) {
			players.add(members.get(i).getPlayer());
		}
		return players;
	}

	public void setRegtime(Timestamp regtime) {
		this.regtime = regtime;
	}

	public Timestamp getRegtime() {
		return this.regtime;
	}

	public void setLocation(Location location) {
		this.lastX = location.getX();
		this.lastY = location.getY();
		this.lastZ = location.getZ();
		this.lastWorld = location.getWorld().getName();
	}

	public Location getLocation() {
		World world = Bukkit.getServer().getWorld(this.lastWorld);
		return new Location(world, lastX, lastY, lastZ);
	}
}

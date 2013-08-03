package kr.kkiro.projects.bukkit.EntityProtect.utils.database;

import java.sql.Date;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import kr.kkiro.projects.bukkit.EntityProtect.bukkit.EntityProtect;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import com.avaje.ebean.validation.NotNull;

@Entity()
@Table(name = "ep_entities")
public class EntitySet {
	@Id
	private int id;

	@NotNull
	private UUID entity;

	@NotNull
	private int owner;

	/*@JoinTable(
		      name="ep_entities_players",
		      joinColumns={@JoinColumn(name="entities_id", referencedColumnName="id")},
		      inverseJoinColumns={@JoinColumn(name="players_id", referencedColumnName="id")})
	private List<PlayerSet> members;*/

	Date regtime;

	private double lastX;

	private double lastY;

	private double lastZ;

	private String lastWorld;

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public void setEntity(UUID entity) {
		this.entity = entity;
	}

	public UUID getEntity() {
		return this.entity;
	}

	public void setOwner(int owner) {
		this.owner = owner;
	}
	
	public void setOwner(PlayerSet owner) {
		this.owner = owner.getId();
		Thread.dumpStack();
	}

	public int getOwner() {
		return this.owner;
	}
	
	public PlayerSet getOwnerItem() {
		return EntityProtect.getInstance().getDatabase().find(PlayerSet.class, this.owner);
	}

	public void setOwner(String owner) {
		PlayerSet playerset = EntityProtect.getInstance().getDatabase().find(PlayerSet.class).where()
				.ieq("player", owner).findUnique();
		if (playerset == null) {
			playerset = new PlayerSet();
			playerset.setPlayer(owner);
			playerset.setBreedCount(0);
			DatabaseUtils.save(playerset);
		}
		this.setOwner(playerset.getId());
	}

	/*public void setMembers(List<PlayerSet> members) {
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
	}*/

	public void setRegtime(Date regtime) {
		this.regtime = regtime;
	}

	public Date getRegtime() {
		return this.regtime;
	}

	public double getLastX() {
		return this.lastX;
	}
	
	public void setLastX(double lastX) {
		this.lastX = lastX;
	}
	
	public double getLastY() {
		return this.lastY;
	}
	
	public void setLastY(double lastY) {
		this.lastY = lastY;
	}
	
	public double getLastZ() {
		return this.lastZ;
	}
	
	public void setLastZ(double lastZ) {
		this.lastZ = lastZ;
	}
	
	public void setLastWorld(String world) {
		this.lastWorld = world;
	}

	public String getLastWorld() {
		return this.lastWorld;
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

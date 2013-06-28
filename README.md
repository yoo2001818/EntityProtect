# EntityProtect
# Summary

EntityProtect is a Bukkit plugin for protecting entities.

This plugin does:
- Protects entities from damaging, shearing, breeding, etc...
- Protects player owns too many entites.
- Monitors owner of entites when breeding, throwing eggs, etc...
- Commands for managing entities

# Commands

- */ep* - Shows number of entities you can own.
- */ep help [Command]* - Shows help.
- */ep addmember <Range> <Player>[, Player[, ...]]* - Gives players permission to use your entities
- */ep delmember <Range> <Player>[, Player[, ...]]* - Takes players permission to use your entities
- */ep killrange <Range>* - Kills your entities without drops in your range.
- */ep killoutrange <Range>* - Kills your entities without drops not in your range.
- */ep kill* - Kills all your entities without drops.
- */ep transfer <Player>* - Transfer ownership of your entities.

## Admin Commands
- */ep killplayer <Player>* - Kills all of player's entities without drops.
- */ep killall* - Kills all of owned entities without drops.
- */ep forceowner <Range> <Player>* - Force set owner of entities in range.

# Permissions

- *entityprotect.create-entities.<Type>* - With this permission, Player can create <Type>. See "Supported Entities" page.
- *enttiyprotect.bypass-protect.<Activity>* - With this permission, Player can bypass protection. See "Supported Protections" page.
- *entityprotect.entity-own.<Group>* - This player can own <Group> amount of entity.
- *entityprotect.command* - Player can do /ep help, /ep.
- *entityprotect.command.addmember* - Player can do /ep addmember.
- *entityprotect.command.delmember* - Player can do /ep delmember.
- *entityprotect.command.killrange* - Player can do /ep killrange.
- *entityprotect.command.killoutrange* - Player can do /ep killoutrange.
- *entityprotect.command.kill* - Player can do /ep kill
- *entityprotect.command.transfer* - Player can do /ep transfer
- *entityprotect.command.killplayer* - Player can do /ep killplayer
- *entityprotect.command.killall* - Player can do /ep killall
- *entityprotect.command.forceowner* - Player can do /ep forceowner

# Supported Entities

- Chicken
- Cow
- MushroomCow
- Pig
- Sheep
- Wolf
- Ozelot

# Supported Protections

**This protections don't applied to owners, and configurable!**

- *damaging* - Prevent attacking.
- *damage-force-1hp* - Force set damage to 1hp.
- *slaying* - Denies slaying.
- *shearing-sheep* - Denies shearing sheep.
- *shearing-mushroomcow* - Denies shearing MushroomCow.
- *filling-cow* - Denies Filling milk from Cow.
- *filling-mushroomcow* - Denies Filling soup from MushroomCow.
- *pushing* - Denies entities move by Players.
- *environment-damaging* - Denies damaging from Environment.
- *monster-damaging* - Denies damaging from Monsters.
- *breeding* - Denies entites enter *love mode*.
- *dropping-items* - Denies entites drop items when killed by non-owner.

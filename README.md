# CraftFabric ![](https://tr7zw.dev/u/1580753328.png) ![Java CI](https://github.com/tr7zw/CraftFabric/workflows/Java%20CI/badge.svg)
# ON HOLD TO SEE HOW [FukkitMC](https://github.com/FukkitMC) TURNS OUT

This project aims to implement the basics of the [Paper API](https://github.com/PaperMC/Paper) as a Mod for the Fabric Modloader.

## Goals

- Provide the API as complete as possible
- Working Bukkit/Spigot/Paper plugins in ``Singleplayer`` and ``Multiplayer``
- Working builds for versions that don't get official Spigot releases (Snapshots/Pre-releases/Combat-snapshots)
- Other mods should be able to use the Paper API and interact with plugins, and plugins should be able to interact with other mods

## Status

- Plugins can load (onLoad/onEnable/onDisable are working)
- Each Singleplayer world has it's own plugin folder inside the world folder
- First simple Events are working (For example AsyncPlayerPreLoginEvent, PlayerJoinEvent, PlayerQuitEvent)
- Commands are (somewhat) working (Currently a hacky solution)
- The first parts of the API are working (Entities, ItemStacks, Inventories, Commands, Scheduler, Scoreboards)
- First plugins loading and (somewhat) working:
  - EssentialsX (Basic commands like /spawn, /home, /ci, /i, /money, /speed, /gm, /fly, /time, /heal, /warp)
  - QuickBoard (Shows scoreboard, animations work, placerholder API doesn't seem to work)
  - Vault (Starts without error)

## Credits

- [Bukkit](https://bukkit.org/) for the original API and server
- [Spigot](https://www.spigotmc.org/) for the currently maintained server software
- [PaperMC](https://papermc.io/) for their high performance and extended Spigot fork
- [Fabric](https://fabricmc.net/) for the Modloader and Mod API
- [SpongePowered Mixins](https://github.com/SpongePowered/Mixin) for the awesome and simple trait/mixin framework

## Legal

This project uses reworked classes from [CraftBukkit](https://hub.spigotmc.org/stash/projects/SPIGOT/repos/craftbukkit/browse). Most changes are because of the different deobfuscation mappings used by Fabric compared to Spigot, but other changes may be made because of the different architecture. Mixins are used to hook into the native Minecraft code, so no  Minecraft source code is pressent in this repo.

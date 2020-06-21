# DeathSwap

[![Minecraft Version: v1.15.2](https://img.shields.io/badge/minecraft-v1.15.2-%2334aa2f%20)](https://www.minecraft.net/)
[![Release: v0.1.0](https://img.shields.io/badge/release-v0.1.0-informational)](https://github.com/ainterr/death-swap/releases/latest/)
[![License: MIT](https://img.shields.io/badge/license-MIT-red)](LICENSE.txt)

DeathSwap is a minecraft minigame plugin where the objective is to kill other
players with traps. Player positions are swapped every five minutes and the
last player standing wins. Any number of players (two or more) supported.
Inspiration from the popular YouTuber Dream's
[video](https://www.youtube.com/watch?v=vXS1pXWslxs).

## Installation

1. Download the latest release [here](https://github.com/ainterr/death-swap/releases/latest/).
2. place the plugin `.jar` in your [Spigot](https://www.spigotmc.org/) or [Bukkit](https://dev.bukkit.org/) server's `plugins/` directory.
3. Reload the server with the `reload` command or restart the server.

## Usage

Server operators may use the following commands to control the DeathSwap game.

To start a game of DeathSwap, run:

```
death-swap-start
```

To stop a running game of DeathSwap, run:

```
death-swap-stop
```

### Configuration

The default DeathSwap interval is 5 minutes - to set the interval (for example,
to 10 minutes), run:

```
death-swap-configure interval 600
```

## Contributing

Pull requests and issues are more than welcome.

## License

[MIT](LICENSE.txt)

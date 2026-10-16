<p align="center"><img src="1.21.11/src/main/resources/assets/noeb/icon.png" width="128"></p>

# No Eat Bounce

removes the up and down bounce of the item in your hand while you eat food or use potions. the item still moves to your mouth, it just stops bobbing.

client side fabric mod. settings with the menu (key **X** or mod menu, 1.21 to 1.21.11 jar needs [Wysss Library](https://github.com/realwysss/Wysss-Library)):

- **mod on**: turn it off to get the normal vanilla animation back
- **bounce height**: slider from 0% to 100%. 0% (default) removes the bounce, 100% is vanilla, anything in between is a smaller bounce

## downloads

| jar | minecraft |
|---|---|
| `no-eat-bounce-mc1.21-1.21.11-1.1.0.jar` | 1.21 to 1.21.11 |
| `no-eat-bounce-mc26.1-26.2-1.0.0.jar` | 26.1 to 26.2 |
| `no-eat-bounce-mc26.3-1.0.0.jar` | 26.3 |

needs fabric loader. mod menu and cloth config are optional, without them the mod works with the default (bounce removed) and no settings screen.

## how it works

one mixin on the held item renderer. the bounce is a single `0.1` constant in `applyEatTransform`, the mod multiplies it by your slider value. in 26.3 the class is called `FirstPersonHandsAndItemsRenderer`, so that version has its own project.

## folders

- `1.21.11` fabric loom 1.17, java 21, mojang mappings
- `26.2` loom 1.18, java 25, builds the 26.1 to 26.2 jar
- `26.3` loom 1.18, java 25

all rights reserved. made by wysss.

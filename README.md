# CyberClasses

Forge 1.20.1 player class framework for the CyberSpectra Season 2 modpack.

CyberClasses runs after CyberRaces character creation. Once a player has chosen
a race, CyberClasses opens its own mandatory class selector.

Initial classes:
- Knight
- Archer
- Rogue
- Mage
- Berserker
- Cleric
- Spellblade
- Ranger

The restriction system currently controls sword/axe use, ranged weapons, magic,
shields, and armour weight. Custom item tags make mod compatibility data-driven.

CyberClasses is intentionally its own mod. CyberRaces owns races, CyberClasses
owns the shared class definitions for both players and Wild NPCs, and CyberNpc
owns the AI/loadout behaviour used by NPCs after CyberClasses assigns their class.

Wild NPC class spawn rarity, class IDs, magic requirements and legacy class
migration now live in CyberClasses so adding or changing a class only needs one
shared definition. Classless remains NPC-only and is never shown in the player
class selector.

Current development version: `0.1.1-alpha.2`.

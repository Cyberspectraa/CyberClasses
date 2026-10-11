# CyberClasses

**Current version: 0.2.1-alpha.7** · Minecraft **1.20.1** · Forge **47.4.10** · Java **17**

CyberClasses defines the shared player and Wild NPC class system for CyberSpectra Season 2: class selection, gear and magic restrictions, mana tiers, advancement paths and class-related quest unlocks.

**Design ownership:** CyberRaces owns race and Cyber Level progression; CyberClasses owns class definitions and rules; CyberNpc owns NPC equipment, combat, loadouts and decision-making. CyberQuest can grant symbolic class unlocks without depending on class-specific implementation details.

## Character creation

With the full ecosystem installed, a player selects a **race in CyberRaces**, then chooses a **class in CyberClasses** before the first-arrival cinematic. The creation flow holds newly arriving players safely until the required selections are complete; existing saved characters are handled through the migration/creation-state checks.

There are **12 player-selectable base classes**:

| Class | Intended style | Base equipment/magic identity |
| --- | --- | --- |
| Knight | Frontline defender | Heavy armour, melee weapons, shields |
| Berserker | Aggressive melee | Heavy armour and melee weapons |
| Archer | Long-range combat | Ranged weapons, medium armour |
| Ranger | Wilderness fighter | Mixed melee/ranged, medium armour |
| Rogue | Mobile skirmisher | Blades, light armour |
| Monk | Unarmed fighter | Light armour, no standard weapon focus |
| Mage | Pure spellcaster | Light armour, very high mana |
| Cleric | Divine support | Axes, shields, medium armour, magic |
| Spellblade | Melee/caster hybrid | Swords, medium armour, lower mana |
| Druid | Nature spellcaster | Medium armour, high mana |
| Bard | Group support caster | Light armour, magic and blades |
| Alchemist | Potion/utility specialist | Light armour, no mana requirement |

**Classless** is an additional **NPC-only** class. It is the most common Wild NPC class and is deliberately not offered in the player creator. NPC class spawn weights and spell-mod requirements are defined in `CyberClass`, not duplicated in CyberNpc.

## Rules and restrictions

The server-side restriction engine enforces effective class/advancement rules for:

- Swords, axes, bows/crossbows and other tagged ranged weapons.
- Shields and magic-capable items.
- Light, medium and heavy armour limits.
- Magic casting and class-specific mana pool tiers, including the optional Iron's Spells 'n Spellbooks bridge.

Item tags allow additional weapon and armour types from other mods to be categorised without rewriting class logic. Default classification also handles supported vanilla items. Restrictions are evaluated from the player's **effective class path**, including any selected advancement; not all classes may use the same equipment or spell access.

## Class advancement — Cyber Level 20

At **Cyber Level 20**, a player can choose one of **three paths for their base class**, for **36 defined advancements**:

| Base class | Advancement choices |
| --- | --- |
| Knight | Vanguard, Templar, Guardian |
| Berserker | Reaver, Juggernaut, Bloodrager |
| Archer | Marksman, Sharpshooter, Arcane Archer |
| Ranger | Hunter, Pathfinder, Warden |
| Rogue | Assassin, Duelist, Shadow |
| Monk | Iron Fist, Windwalker, Ascetic |
| Mage | Sorcerer, Elementalist, Necromancer |
| Cleric | Paladin, Oracle, Inquisitor |
| Spellblade | Battlemage, Rune Knight, Arcane Duelist |
| Druid | Shaman, Wildspeaker, Stormcaller |
| Bard | Skald, Minstrel, Mesmer |
| Alchemist | Apothecary, Bombardier, Transmuter |

A dedicated advancement screen and server-side state store the choice. Advancements **currently define equipment permissions, armour limits, magic access and mana tiers** for the effective class path. Their flavour descriptions (for example, necromancy, assassination or potion mastery) are **not a guarantee that bespoke active mechanics for every path already exist**.

Class and advancement definitions are also exposed to Wild CyberNpc entities via the lightweight `NpcClassRegistry` bridge.

## Quest unlocks

`ClassQuestUnlockManager` saves class-scoped, symbolic unlock IDs awarded by compatible CyberQuest rewards. The unlock storage and lookup APIs exist now; further class-specific gameplay can consume these flags without changing the underlying quest framework. An unlock ID is **not automatically a fully implemented new ability**.

## Commands

Player commands:

```mcfunction
/cyberclasses info
/cyberclasses advance
```

Admin commands (permission level 2):

```mcfunction
/cyberclasses set <player> <class>
/cyberclasses setadvancement <player> <advancement>
/cyberclasses clearadvancement <player>
/cyberclasses reset <player>
```

`/cyberclasses advance` opens advancement selection subject to its eligibility checks; admin setters are testing/maintenance tools, not substitutes for the normal progression flow.

## Integrations and status

- **CyberRaces:** race setup and Cyber Level thresholds.
- **CyberNpc:** shared Wild NPC class rolls, advancements, equipment and combat AI; NPC-only Classless support.
- **CyberQuest:** quest requirements/rewards involving class or advancement, and class-specific symbolic unlocks.
- **Iron's Spells 'n Spellbooks:** mana tiers and player spellcasting restrictions when available; spellcasting NPC classes are conditionally generated where applicable.
- **CyberServer:** world-owned policy can gate natural Wild NPC class advancement.

This is an **alpha gameplay framework**. Class rules, selection and advancement definitions exist, but unique signature skill sets for all 36 advancement paths and broader content/balance testing remain future work.

# CyberClasses

**Current development version: 0.2.1-alpha.14** · Minecraft **1.20.1** · Forge **47.4.10** · Java **17**

CyberClasses defines the shared player and Wild NPC class system for CyberSpectra Season 2: class selection, gear and magic restrictions, mana tiers, advancement paths and class-related quest unlocks.

**Design ownership:** CyberRaces owns race and Cyber Level progression; CyberClasses owns class definitions and rules; CyberNpc owns NPC equipment, combat, loadouts and decision-making. CyberQuest can grant symbolic class unlocks without depending on class-specific implementation details.

## Character creation

In worlds with CyberNpc's configured introduction, the player selects a race first, completes summoning and the Pope's greeting, then registers a class at the Guild Receptionist. Worlds without configured intro retain the original class creator. Existing saved classes are preserved. No physical town-exit gate is required.

Starter kits are optional, with a one-time saved choice per character. Magic classes start with empty spellbooks rather than learned spells. Mages receive a genuine Tinkers' Construct wooden dagger when Tinkers is installed; only all-wood Tinkers daggers are permitted as the Mage sword-class exception. Vanilla wooden swords and higher-material daggers are not allowed. Without Tinkers, the rest of the Mage kit is supplied without an invalid replacement dagger. See [guild registration](docs/GUILD_CLASS_REGISTRATION.md) (earlier wooden sword and boundary details superseded) and [Mage dagger](docs/MAGE_WOODEN_DAGGER.md).

With CyberRaces 0.3.2-alpha.76, players earn Cyber XP only after choosing a class. Vanilla XP and already-earned Cyber Levels are unaffected. CyberQuest supplies the introductory guild quest.

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

## Class advancement — earned through Class Books

The 36 class advancement paths retain a **baseline Cyber Level 20 definition**, but **as of 0.2.1-alpha.8 players no longer receive a free advancement-selection popup**. They need a level-gated **Class Book**, with different rarities unlocking different class paths:

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


### Creative inventory and class-change VFX

All four Class Books are available together in the dedicated **CyberClasses** Creative inventory tab, as well as the regular Ingredients tab. Creative or `/give` books are unattuned until first right-click; the next valid use consumes/applies the class change.

Successful transformations now use a **dedicated CyberClasses Photon FX** (`cyberclasses:class_transformation`): short-lived magical halos, rising motes and a closing shimmer. It is attached to the **player entity** rather than a block, so its origin follows players as they move or turn. The custom effect is bundled **inside CyberClasses.jar**, generated at build time; Photon is optional and CyberServer is **not required**. Different book rarities adjust effect size. Without Photon, class changes still work with vanilla enchanting/end-rod particles and sounds. No intro/summoning visual is reused.

### Class Book rarity and use

| Book | Minimum Cyber Level | Effect |
| --- | ---: | --- |
| **Common** | 8 | Changes to one of six ordinary base classes (Knight, Archer, Rogue, Ranger, Monk or Alchemist) |
| **Uncommon** | 20 | Changes to any of the 12 selectable base classes |
| **Rare** | 30 | Grants a **named advancement for the player's current base class** |
| **Legendary** | 50 | Grants a **named advanced path from any base class**, changing base class if required |

Books contain a **specific class or advancement target**, visible in their item name and tooltip. They are consumed **only after** a valid class change and level checks pass. A Rare book cannot be used by an incompatible base class. Books obtained via creative mode or `/give` begin unattuned; the first right-click assigns a random target to that book without consuming it, and the next right-click applies its effect if eligible. Players can trade targeted books.

**Survival acquisition:** a server-side hostile-mob loot roll (2.6% per qualifying kill) awards a book with weighted rarity; Wither and Ender Dragon kills yield a guaranteed Rare or Legendary book. Books use vanilla book/enchanted book textures, so no texture-pack dependency is required.

Existing saved class advancements remain valid. Staff/admin class setters remain available for testing. Advanced class paths **currently define equipment permissions, armour limits, magic access and mana tiers**; their flavour descriptions do **not** guarantee bespoke active mechanics for every path.

Class and advancement definitions are also exposed to Wild CyberNpc entities via the lightweight `NpcClassRegistry` bridge.

## Quest unlocks

`ClassQuestUnlockManager` saves class-scoped, symbolic unlock IDs awarded by compatible CyberQuest rewards. The unlock storage and lookup APIs exist now; further class-specific gameplay can consume these flags without changing the underlying quest framework. An unlock ID is **not automatically a fully implemented new ability**.

## Commands

This mod no longer registers its own commands. Register and advance classes through the normal gameplay interfaces.
Operators use the retained `/cadmin` commands provided by the companion mods. No saved character or quest data was removed.

## Integrations and status

- **CyberRaces:** race setup and Cyber Level thresholds.
- **CyberNpc:** shared Wild NPC class rolls, advancements, equipment and combat AI; NPC-only Classless support.
- **CyberQuest:** quest requirements/rewards involving class or advancement, and class-specific symbolic unlocks.
- **Iron's Spells 'n Spellbooks:** mana tiers and player spellcasting restrictions when available; spellcasting NPC classes are conditionally generated where applicable.
- **CyberServer:** world-owned policy can gate natural Wild NPC class advancement.

This is an **alpha gameplay framework**. Class Books now provide a survival-based advancement route, but unique signature skill sets for all 36 advancement paths and broader content/balance testing remain future work.

## Build and status

Minecraft 1.20.1, Forge 47.4.10 and JDK 17. Use matching CyberClasses versions on client and server. The development-no-actions branch contains the current class source; the README-only refresh does not change the existing mod version. Follow the repository's Gradle build setup. Do not run GitHub Actions or change main.

Current companions: CyberNpc 0.45.9, CyberRaces 0.3.2-alpha.76, CyberQuest 0.3.0-alpha.23 and CyberServer 0.2.7. Older [README](https://github.com/Cyberspectraa/CyberClasses/blob/3c1463835156ca6f2b2248687d065703dea414a2/README.md) remains available in Git history.

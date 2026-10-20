# Guild class registration and optional kits

When CyberNpc's town introduction has been configured, choosing a race
starts summoning immediately; the class creator is **not** forced before
the cinematic. The player meets the Pope, explores town, then opens class
registration at a Guild Receptionist. Existing saved classes are unchanged.
Worlds without the configured intro retain the original class creator.

- The guild class screen can be closed with Escape; players must return to
  register a class to leave a configured town boundary.
- The **class** is mandatory but the **starter kit** is optional. Choosing
  "NO" does not lock out class progression or quests.
- Exactly one starter-kit decision per character is stored in CyberClasses
  persistent player data (`StarterKitDecided`, `StarterKitAccepted`).
  The flag intentionally survives admin class resets and death, so the kit
  cannot be farmed. Existing characters do not receive retroactive kits.
- If the inventory is full, the chosen kit drops safely by the player.
- Mage gets an **empty** Iron's Spells **copper spellbook**, **wooden sword**,
  leather armour and bread. No spell scrolls or prelearned spells.
  Only the wooden sword is an exception for Mage combat restrictions.
  Higher-tier swords remain forbidden.
- Every other player-selectable class has a modest survival-appropriate kit.
  Magic classes receive empty books, not preloaded spells.
- CyberClasses network protocol bumped to 3 because the class selection packet
  now includes a kit toggle and the open packet distinguishes Guild mode.

CyberNpc must be updated alongside this change; use the companion build.

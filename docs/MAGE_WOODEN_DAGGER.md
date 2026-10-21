# Mage starter wooden dagger (0.2.1-alpha.13)

Mage starter kit uses a **real Tinkers' Construct wooden dagger**
(tconstruct:dagger with tconstruct:wood material), constructed through
Tinkers' ToolBuildHandler.createSingleMaterial API via optional reflection.

A bare dagger ItemStack lacks its proper Tinkers' tool stats, so it is
**not** issued as a fallback. If Tinkers is absent, Mage retains the
empty spellbook, armour and food; no incompatible Tinkers dependency.

ClassRestrictionEngine treats Tinkers daggers as sword-class weapons:
Mage may use only a dagger whose `tic_materials` are all wood, and
cannot use vanilla wooden swords or upgraded metal/stone daggers.
Other classes follow their existing sword permissions.

Starter kits remain optional and can only be decided once per character.

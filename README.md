# More Curios Slots

<p align="center">
  <a href="https://modrinth.com/mod/more-curios-slots"><img src="https://i.imgur.com/rJGFMCf.png" alt="Modrinth" width="250" height="auto"></a>
  <a href="https://www.curseforge.com/minecraft/mc-mods/more-curios-slots"><img src="https://i.imgur.com/ts51S3Q.png" alt="CurseForge" width="298" height="auto"></a>
</p>

> ## WARNING!! This mod, like the [Curios](https://modrinth.com/mod/curios) mod, requires other supported mods to work. It won't do much by itself. If you don't know any, try [Artifacts](https://modrinth.com/mod/artifacts) or [Unique Accessories](https://modrinth.com/mod/unique-accessories).


This mod allows you to craft more slots to use with Curios. They are pricey, but worth it.

![Charm Crafting](https://cdn.modrinth.com/data/v4mOUzHT/images/e6cc2e553e2a7250926b2026e2fea775ffed2006.webp)

You can edit the crafting recipes with KubeJS or datapacks. Curios uses tags like `#curios:charm`, `#curios:head` etc. to refer to items that fit in a specific slot. 

> Example with more slots.
![Sample use](https://cdn.modrinth.com/data/v4mOUzHT/images/9d2c646ac22cf9050dbae10fa574e54a9a2267b6.webp)

## Adding extra slots for slots from other mods

Datapacks can add new recipes for new slots, make a new recipe for any slot at `data/<your_pack>/recipe/extra_<new_slot>_slot.json`

### Sample recipe:

<details><summary>Show recipe json</summary>

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "misc",
  "pattern": ["NEN", "DSD", "NGN"],
  "key": {
    "N": { "item": "minecraft:iron_block" },
    "E": { "item": "minecraft:emerald_block" },
    "D": { "item": "minecraft:diamond_block" },
    "G": { "item": "minecraft:gold_block" },
    "S": { "tag": "curios:<new_slot>" }
  },
  "result": {
    "item": "more_curios_slots:extra_slot",
    "count": 1,
    "nbt": "{Slot:\"<new_slot>\"}"
  }
}
```

</details>

### Notes:

- Find slot ids with `/curios list`, or by looking for the slot list inside the jar of the mod that adds them.
- On this version the targeted slot is stored in the item's NBT (`Slot`), since data components only exist in 1.20.5+.
- You can't currently remove the pre-installed curios slots.
- You can use `/give @s more_curios_slots:extra_slot{Slot:"<new_slot>"}` to give yourself any extra slot.

## Troubleshooting

> When using artifacts, effects don't stack.

\> This is just how artifacts is programmed, pretty sad. It theoretically would be possible to change this behavior via mixins, but i'm not experienced enough yet.

> I can't equip multiple spellstones with Enigmatic Legacy

\> Make sure `B:SpellstonesMultiequip` is set to true in Enigmatic Legacy's Settings

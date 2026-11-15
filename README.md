# More Curios Slots

<p align="center">
  <a href="https://modrinth.com/mod/more-curios-slots"><img src="https://i.imgur.com/rJGFMCf.png" alt="Modrinth" width="250" height="auto"></a>
  <a href="https://www.curseforge.com/minecraft/mc-mods/more-curios-slots"><img src="https://i.imgur.com/ts51S3Q.png" alt="CurseForge" width="298" height="auto"></a>
</p>

> ## WARNING!! This mod, like the [Curios](https://modrinth.com/mod/curios) mod, requires other supported mods to work. It won't do much by itself. If you don't know any, try [Artifacts](https://modrinth.com/mod/artifacts) or [Unique Accessories](https://modrinth.com/mod/unique-accessories).

This mod allows you to craft more slots to use with Curios. They are pricey, but worth it.

![Charm Crafting](https://files.catbox.moe/lc1t17.webp)

You can edit the crafting recipes with KubeJS or datapacks. Curios uses tags like `#curios:charm`, `#curios:head` etc. to refer to items that fit in a specific slot.

> Example with more slots.
> ![Sample use](https://files.catbox.moe/igbnrp.webp)

## Adding extra slots for slots from other mods

Datapacks can add new recipes for new slots, make a new recipe for any slot at `data/<your_pack>/recipe/extra_<new_slot>_slot.json`

### Sample recipe:

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
    "id": "more_curios_slots:extra_slot",
    "count": 1,
    "components": { "more_curios_slots:slot": "<new_slot>" }
  }
}
```

### Notes:

- Find slot ids with `/curios list`.
- You can't currently remove the pre-installed curios slots.

## Troubleshooting

> "This slot is not available in your modpack! It will be useless."

\> This means you don't have any mod that uses curios. Read the first part of this page.

> When using artifacts, effects don't stack.

\> This is just how artifacts is programmed, pretty sad. It theoretically would be possible to change this behavior via mixins, but i'm not experienced enough yet.

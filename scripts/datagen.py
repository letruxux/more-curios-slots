import json, os

curio_slots = [
    "head", "feet", "hands", "charm", "belt", "ring",
    "back", "bracelet", "necklace", "body", "curio" #"calendar"
]

dest_recipe_path = "./src/main/resources/data/more_curios_slots/recipes"
dest_model_path = "./src/main/resources/assets/more_curios_slots/models/item"

def create_recipe_json(slot):
    return {
        "type": "minecraft:crafting_shaped",
        "pattern": [
            "NEN",
            "DSD",
            "NGN"
        ],
        "key": {
            "N": {"item": "minecraft:iron_block"},
            "D": {"item": "minecraft:diamond_block"},
            "E": {"item": "minecraft:emerald_block"},
            "G": {"item": "minecraft:gold_block"},
            "S": {"tag": f"curios:{slot}"}
        },
        "result": {"item": f"more_curios_slots:extra_{slot}_slot"}
    }

def create_model_json(slot):
    return {
        "parent": "minecraft:item/generated",
        "textures": {
            "layer0": "more_curios_slots:item/extra_{}_slot".format(slot)
        }
    }

for slot in curio_slots:
    json_recipe = create_recipe_json(slot)
    open(os.path.join(dest_recipe_path,f"extra_{slot}_slot.json"), "w").write(json.dumps(json_recipe, indent=4))

    json_model = create_model_json(slot)
    open(os.path.join(dest_model_path,f"extra_{slot}_slot.json"), "w").write(json.dumps(json_model, indent=4))



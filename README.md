![Slab Types](https://user-images.githubusercontent.com/130693918/231869727-494e7181-4d8c-450d-b411-2b0478a12aa8.png)
# Universal-Slabs
A simple mod that makes all slabs placeable vertically as well as horizontally. Note that this mod does **not** add *new* items or blocks, it just modifies the existing slab blocks.  

While this mod aims to work with other mods that add slabs, it is not guaranteed that all features, especially the block models, work as expected.


### Fixing the block models manually

In case, your game is missing block models of slabs(displays a black-pink block instead of the actual slab), you can add missing block state files yourself. 

Add a resource pack that contains assets/minecraft/blockstates and add any not_working_slab.json in the blockstates folder. Copy any of the slab jsons from this repository's src/main/generated/assets/minecraft/blockstates and adjust the "model" specifications of the not working blockstates to match "minecraft:block/actual_slab" or "modid:block/actual_slab".

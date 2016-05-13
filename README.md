# David:
- Enemy 2(Animation, logic)
- Enemy 3(Animation, logic)
- Spawn multiple enemies(Time between them/prob. what enemy will spawn)
- Player Proj blows up enemy Proj
- Player needs reach end of the map and defeat all the enemies
..* Add score counter to the game
- Player has 3hp

- ~~Prevent enemies from spawning on each other(have AABB test)~~
- ~~Player intersects enemy, player dies~~
- ~~Give player 3 seconds before damage is dealt again (do the red animation)~~

#Bugs;
- If one snail stops moving(under player) all of them do.
- Jumping on a snail while on a platform hits their hitbox causing damage to player
- More snails that spawn the faster the shootTimer/animation timers go
- First projectile will blow up correct projectile if I double shoot first one ignores first enemy projectile
..* Have monkey be outside loop then aliens inside loop check all projectiles

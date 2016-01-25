DefaultWorldGenerator-port

This was originally by fireball1725 for minecraft 1.7.10
(Original posting can be found here):
http://minecraft.curseforge.com/projects/default-world-generator

It has been since ported to 1.8.9 by EzTerry, with additional server side
features, as well as fixing some original gotchas:


* Mod allows changing the configured default World Type (level type) and 
additional settings string for both the client and server

* If used on a server the mod is server side only, so the client need not
include it.

* By default the user can still change the world type, however it can be
configured to remove the option from the create world screen.

* The custom configuration now is the customization strings you add into the
client, with the auto name->id conversion internal to 1.8 it is no longer
required for this mod to manually parse block ids.

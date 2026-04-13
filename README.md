# ComputerPresence
show your computer status as a 
[Discord Rich Presence Activity](https://docs.discord.com/developers/platform/rich-presence)!

> [!WARNING]
> Users of this application need to abide by Discord's
> [Term of Service](https://discord.com/terms) and the
> [Community Guidelines](https://discord.com/guidelines).
> Failure to follow may result in account termination. Use responsibly!
> 
> This application isn't sponsored nor supported by Discord!

## Building
Java 25 is required for building.

On your terminal, execute these commands:
1. `git clone https://github.com/brickwall2900/ComputerPresenceReborn.git`
2. `cd ComputerPresenceReborn`
3. `./gradlew build`

...or open this project in an IDE as a Gradle project.

If you want to create an executable fat JAR: `./gradlew shadowJar`.
The executable JAR should be at `./build/libs`


### Why "reborn"?
this is a rewrite of an older project that aims to do the same thing.

## It needs this to work
ComputerPresence needs a Discord Application ID to display the Rich Presence in your profile.

By default, it runs with a default built-in Application ID. You can change it using one of the following:
* through command line parameter: `-id <your-app-id-here>`
* through system property: `-Ddiscord.appId=<your-app-id-here>`

You can create one yourself in the [Discord Developer Portal](https://discord.com/developers/home).

## How it works
ComputerPresence runs a Lua script for defining what to show in Discord Rich Presence.
A lot of the APIs (exposed by the `presence` global in Lua) *somewhat* mirrors the 
[Activity class in the Social SDK](https://discord.com/developers/docs/social-sdk/classdiscordpp_1_1Activity.html).

Lua documentation can be found on the Wiki.
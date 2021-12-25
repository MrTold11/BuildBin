# BuildBin

Simple plugin for saving and pasting buildings (without schematics).

## About
Plugin saves and loads all NBT data, correctly places beds and doors (as well as any other Bisected blocks).
It also saves the distance between a player and a building, but not direction.
BuildBin can easily be multiversion, but for now it's not necessary.

## Commands:
`/bin` - get selection tool (a stick)  
`/copybuild <filename>` - save selected region into `plugins/BuildBin/filename.mcbld`  
`/pastebuild <filename>` - paste saved building  

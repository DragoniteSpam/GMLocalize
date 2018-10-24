# GameMakerOrphanedFiles
Sometimes when you're working on Game Maker projects, you stop using scripts or sprites or objects but don't delete them. This leads to bloated projects, and can make it hard to find the assets that you *do* need. This will hopefully make it easier to detect those assets, so that you can decide if you want to delete them.

Note: I'm currently writing this for Game Maker Studio 1. If I ever finish this and there's demand for it, or if I get bored enough, I'll make a version for Game Maker Studio 2, also.

24 October 2018: am working on a GMS2 version. No idea when it'll be done.

# Usage
1. Make sure you have Java installed. It should probably be at least Java 8, too. I don't know what happens if you try running this with like Java 5 or something. Probably nothing good. If you're interested in cloning this project, I'm guessing you already have that, though.

2. `go.bat` is a Windows Batch file that automatically compiles and runs `GMOrphanedFiles.java`. Because I've had experience with Java failing to recompile changed classes, it deletes existing ones before it does this. This probably is a bit of a time sink, but because this project is so miniscule it shouldn't really matter.

3. If you have a Game Maker project that you want to be the ginuea pig, copy it to this folder under the name `project.gmx`. Or edit the batch file to point to an existing one.

4. `jar-me.bat` is for when you have something that works and you want to turn it into a jar. I couldn't figure out how to make it automatically increase a version number, though, so the jar it spits out will just be called `gmof-x.x.jar` and it's your job (or mine) to give it the right number.

Other than that, follow the directions, they tell you what to do.
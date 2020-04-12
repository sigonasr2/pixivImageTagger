# pixivImageTagger
A Pixiv Image Tagger, tagging all jpg images from Pixiv automatically with Pixiv's native image naming scheme.

# What pixivImageTagger does?
If you have a directory containing a bunch of saved Pixiv images, then this program will help you get tags automatically applied to the images. 

Images **must** be in this format:
  *64696271_p0.jpg*
These are obtained when saving the image directly in pixiv.

The Image Tagger crawls the site for the data and finds English tags for the image, then applies them based on a Whitelist.

# How to use pixivImageTagger
Make sure all your images you want to tag are in the same directory. Then start by running Tagger.jar. A folder selection window pops up. Select the directory with all the Pixiv images inside.

Once you click the **Open** button, the program will retrieve all tag information for the images. Once it is done, you will get three files in the same directory that you ran Tagger.jar.

  -**TAG_DATA.txt** - Shows what tags each image, by Pixiv Image ID, each one contains
  
  -**SORTED_TAGS.txt** - Shows how many images have the same tags in common, sorted from highest to lowest.
  
  -**RAW_TAGS.txt** - A file containing every tag, without any additional data.
  
Review these files to make an informed decision of which tags you want to use to sort the images by, then proceed to build a **whitelist.txt** file following the **whitelist_template.txt** file found in this repo, or by the following guidelines below:

The simplest way to whitelist tags is by renaming RAW_TAGS.txt to whitelist.txt and running the program again.

However, for finer control on what tags are converted, make a file with each tag separated by a line in this format:

  **MAIN TAG:SUB TAG 1:SUB TAG 2:SUB TAG 3:etc...**
  
Where the main tag is the tag images will be tagged as, and each sub tag will be renamed to the main tag.

Good uses of sub-tags are for things like the following:


 - League of Legends:League_of_Legends:LoL:lol:LOL:LeagueofLegends:leagueoflegends:league_of_legends:League_of_legends:LeagueOfLegends
 
 - Touhou Project:Touhou
 
 - Ahri:fox ears
 
 - IMAS:the idolmaster- cinderella girls
 
This provides consistent naming for related tags that are similar that you want renamed or representing the same tag. In those examples, all those different variations of League of Legends turn into just one tag called "*League of Legends*", all Touhou tags are renamed to "*Touhou Project*", and anything with fox ears becomes "*Ahri*".

**NOTE:** If you have a tag with colons in it, like *the idolmaster: cinderella girls* convert the colons to dashes like I did in the example above. All tags found by the tagger program with colons will automatically convert them to dashes!!!

These are all just basic examples.


When running the program with a **whitelist.txt** file in the same directory as the jar, the program will proceed to apply all the re-tagging.

This process can take some time on larger directories. It will close on its own when it's complete and run in the background. If you want something to indicate progress, then run it using a command prompt / Powershell window by opening up said window, dragging-and-dropping the file, then pressing enter to run it that way. You will get a more verbose output.

After the process is complete, if there were any items skipped due to being obsolete / deleted / invalid, a file called skippedItems.txt is created and stored in the same directory.

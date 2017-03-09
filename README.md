# SithAndroid
Werewolves (http://www.playwerewolf.co/rules) variant set in the Star Wars Universe!
This game takes the job of the narrator and keeps track of all the players and the full status of the game.
It's the Sith (Werewolves) against the Jedi (Civilians) together with other roles that can influence the flow of each game.

<img src="https://raw.githubusercontent.com/tristanvda/SithAndroid/master/resources/screen_home.png" width="360" height="640">

## Functionality
### Players
Start by adding global players to the app that can be used to create a new game. Only with 5 players or more, a new game can be created.

<img src="https://raw.githubusercontent.com/tristanvda/SithAndroid/master/resources/screen_players.png" width="300" height="534"> <img src="https://raw.githubusercontent.com/tristanvda/SithAndroid/master/resources/screen_players_edit.png" width="300" height="534">

### Prepare game
Choose the players to participate in the game and shuffle the cards!

<img src="https://raw.githubusercontent.com/tristanvda/SithAndroid/master/resources/screen_add_players.png" width="300" height="534"> <img src="https://raw.githubusercontent.com/tristanvda/SithAndroid/master/resources/screen_see_card.png" width="300" height="534">

### Day screen
Discuss who has to be killed during the day and start the next night round.

<img src="https://raw.githubusercontent.com/tristanvda/SithAndroid/master/resources/screen_day.png" width="300" height="534">

###  Play out the roles!
The narrator will tell the story and ask each role to do their part. Some examples in the screenshots below:

<img src="https://raw.githubusercontent.com/tristanvda/SithAndroid/master/resources/screen_select_card.png" width="300" height="534"> <img src="https://raw.githubusercontent.com/tristanvda/SithAndroid/master/resources/screen_bind_players.png" width="300" height="534">

<img src="https://raw.githubusercontent.com/tristanvda/SithAndroid/master/resources/screen_select_player.png" width="300" height="534"> <img src="https://raw.githubusercontent.com/tristanvda/SithAndroid/master/resources/screen_yes_no.png" width="300" height="534">

## Practical details about the project
This repo only contains the code for the app. The Images (shown in the picture) are not included in the project, because they are not my property.
The voice files (for the narrator) are also not included. These files are spoken in dutch language as well as the subtitles for the narration. Because of this, the string resources are mixed in English(app navigation) and Dutch(narration).. (Yes I know, I know..)
To use the app, you have to provide your own voice and image resources. If you want to create (free to use) resources for this game, feel free to do so!
Below a list of the resources that are not provided by this repo:

```
#Drawable resourses (mdpi, hdpi, xhdpi, xxhdpi, xxxhdpi)
card_bb8.png
card_boba_fett.png
card_chewbacca.png
card_darth_maul.png
card_darth_vader.png
card_finn.png
card_front.png
card_han_solo.png
card_kylo_ren.png
card_luke.png
card_maz_kanata.png
card_obi_wan.png
card_rey.png
card_the_emperor.png
card_yoda.png
card_general_grievous.png
day_background.png

#Raw resources
basis10_sithmogenteruggaanslapen.wav
basis11_bobafettwordtwakker.wav
basis12_bobafettrocketlauncher.wav
basis13_bobafettgaatterugslapen.wav
basis14_kylorenontwaakt.wav
basis16_dejedicouncilontwaakt.wav
basis1_intro.wav
basis2_hansolowordtwakker.wav
basis3_hansolomagteruggaanlslapen.wav
basis4_bb8wordtwakker.wav
basis5_1_bb8magteruggaanslapen.wav
basis5_2_bb8geliefdenkijkenelkaarliefdevolindeogen.wav
basis6_degeliefdenmogenteruggaanslapen.wav
basis7_mazkanatawordtwakker.wav
basis8_mazkanatamagteruggaanslapen.wav
basis9_sithwordenwakker.wav
bb8_1.mp3
bb8_2.mp3
bb8_3.mp3
bb8.wav
behalve.wav
boba_fett_1.mp3
boba_fett_2.mp3
boba_fett_3.mp3
boba_fett_4.mp3
boba_fett_5.mp3
boba_fett_6.mp3
boba_fett.wav
chewbacca.wav
darth_maul.wav
darth_vader.wav
finn.wav
general_grievous.wav
han_solo_1.mp3
han_solo_2.mp3
han_solo_3.mp3
han_solo_4.mp3
han_solo_5.mp3
han_solo.wav
kylo_ren.wav
luke.wav
maz_kanata_1.mp3
maz_kanata_2.mp3
maz_kanata_3.mp3
maz_kanata_4.mp3
maz_kanata_5.mp3
maz_kanata_6.mp3
maz_kanata.wav
obi_wan.wav
rey.wav
sith_1.mp3
sith_2.mp3
sith_3.mp3
sith_4.mp3
sith_5.mp3
sith_6.mp3
the_emperor.wav
yoda.wav
```
If you want to add random comments to the 'random-comment-flow' just add sound files to the raw folder, starting with "random_comment_", followed by the name of the file.
Example:

```
random_comment_1.wav
random_comment_2.wav

```

## References
https://github.com/PDDStudio/StarView

http://blog.bradcampbell.nz/mvp-presenters-that-survive-configuration-changes-part-1/

https://github.com/genzeb/flip

https://bitbucket.org/littlerobots/cupboard

## License
```
 The MIT License (MIT)

 Copyright (c) 2016 Tristan Vanderaerden

 Permission is hereby granted, free of charge, to any person
 obtaining a copy of this software and associated documentation
 files (the "Software"), to deal in the Software without
 restriction, including without limitation the rights to use,
 copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the
 Software is furnished to do so, subject to the following
 conditions:

 The above copyright notice and this permission notice shall be
 included in all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 OTHER DEALINGS IN THE SOFTWARE.
```
---
title: I3 finder
---

I3 finder is an open source node js script which allows you to focus and move windows
through the keyboard with a fuzzy logic search.

If you're familiar with quickswitch.py it is very similar, however it
includes a few features I couldn't live without. For example, marks
and workspaces are included in the list of selections, allowing you to easily
move/focus them without a separate command. Also i3finder allows you to to jump
back to a previous window focus (similar to alt tab in windows)

# Selections
I3 finder gives you a list of selections, which includes  all windows and
workspaces that currently exist in your i3 session. It utilizes  a launcher like
demnu to do so, and by default will assume dmenu is on your path. The
workspaces have a prefix, 'workspace: ', so workspace 1 would  appear in the list like
'workspace : 1'. IF a window has a mark that mark will also appear in the
selection, so that you can easily access tags in selections

# Focusing a selection
When using the focus action, the selections are shown, and the selection made
is then focused

# Moving a selection
when using the moving action, the selections are shown, and the selection made
is moved to your current location. If its a workspace, then all the windows in
that workspace are moved to your current location.

# Moving back to previous focus / state tracking
The back action allows you to move back to the last known focus state. This can
be used to provide something like alt tab in windows. It only goes to the last
window however, it doesn't cycle through the previous focuses. If you
repeatedly execute the command, you will bounce back between two states.

Also I say last known state because if i3 finder doesn't know about changes
unless it makes them. So if something other then an i3 finder command is used
to focus a window, then when you execute  back that change will be ignored.

The msg action however allows you to execute any i3 message through i3 finder,
and then it will properly track the change.

# Example configuration

```bash

# mod p brings up a list of windows/workspaces/tags to focus
bindsym $mod+p exec i3finder

# mod g brings up a list of windows/workspaces/tags to move to the current area
bindsym $mod+g exec i3finder -a move

# mod b triggers the back manuever
bindsym $mod+b exec i3finder -a back

# change focus on vim style keys, without the finder
# that way little motions dont mess up our history
bindsym $mod+h focus left
bindsym $mod+j focus down
bindsym $mod+k focus up
bindsym $mod+l focus right

# bind mod [num] to change to a workspace, but use msg parameter
# so that these motions are added to our history.
bindsym $mod+1 exec i3finder -a msg -i 'workspace 1'
bindsym $mod+2 exec i3finder -a msg -i 'workspace 2'
bindsym $mod+3 exec i3finder -a msg -i 'workspace 3'
bindsym $mod+4 exec i3finder -a msg -i 'workspace 4'
bindsym $mod+5 exec i3finder -a msg -i 'workspace 5'
bindsym $mod+6 exec i3finder -a msg -i 'workspace 6'
bindsym $mod+7 exec i3finder -a msg -i 'workspace 7'
bindsym $mod+8 exec i3finder -a msg -i 'workspace 8'
bindsym $mod+9 exec i3finder -a msg -i 'workspace 9'
bindsym $mod+0 exec i3finder -a msg -i 'workspace 10'
```

# Alternative launchers
I3 finder uses dmenu by default, but you can utilize any application launcher
that you wish. You can specify the command used to show selections via the dmenu 
paramter.

# Implementation
Utilizing the i3 msg api, I3 finder queries the current i3 tree, and then forms a 
list of selections. Originally I planned on using the i3-msg command only 
temporarily, but after finishing the implementation  I doubt much speed gains
will be achieved by utilizing unix sockets instead, and it simplifies the script
not to do so.



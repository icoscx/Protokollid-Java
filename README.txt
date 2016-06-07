Version 0.1.34a alpha, no automatic tests, very raw
Ivo Pure, Karl Mendelman, Kristjan Rõõm -> names also present in MainWindow.class (only)
Java x32 and x64 are required to run this.

Usage:
*Key folder
All public keys whom you want to connect to need to be in the /key folder
Public key has to be named as the 32bit hex ID. (FFFFFFFF) without ANY extension

*files folder
The files folder has to be present. For sending files the exact name has to be inserted.
File will be named randomly, please rename it after (use the chat :) to ask for the real filename and type)

*config ini
The name of your public key's file (which also has to be in key folder) has to be the value of myuid=FFFFFFFF
listeningport can be anything whats free, has to be above 1024 included

*cmd
there are no command line arguments

*startup
Open CMD -> java -Xms512m -Xmx1024m -jar <jarname>.jar
To exit, press the menu item exit (will close all threads)

*Running info
All errors, input errors and system errors will be displayed under debugger tab
Adding a wrong ip and port (when adding a neighbour) is not handled, please exit and start again.


Known bugs:
*Due to a design flaw, automatic authentication is not possible (it is semi-automatic). Both Peers have to add each other manually.
*Works only on windows because of built in windows alike static paths.
*Threads don't sleep. There is no thread management in this early version, this means one of the computer cores will be consumed 100 % (busy-wait).
This also means, it is not recommended to use this program on a computer with less than 4 logical cores.
*Routing table may not populate correctly or at all due to major flaws in the routing concept of the protocol itself
*Current maximum filesize to send is 1MB, sending files is unreliable!!!


Reflective feedback:

Poor design of authentication messages - most programming launguages (especially java, C#) do not support port spoofing (neither does the network environemnt),
this renders the theory to have only one port fort listening and receiving impossible. Therefore, authentication messages need to 
contain payloads sending the listening port to the potential added friend.

Unimplementable routing concept - I did not really understand the routing concept, It does not work - as described in Leonardos Scheme and in the document. Then they 
agreed on something new (undocumented, unconfirmed), which basically is a drop-unknown concept and it doesnt send routing info back to the 
origin. Well this actually means that the routing is alike multicast, traversing all nodes until TTL is dead and finding correct path to the
endpoint. But I still followed the official document: If a new node appeares and sends its routing table first, the routing it will break valid routes.
Another issue is, that if a node with key information about routing dissapeares all the routes get lost. **(Initiator into packet header.)

Stop and wait is a very bad concept and wont work without timers. There would have to be a wait timed for every packet sent and received.
I did some testing, and sending files as fast as possible, the order gets mixed and packets start to swap places, file gets corrupted.
Example: If one sends out 4 packets: 1=seq0, 2=seq1, 3=seq0 4=seq1 -> then this can most certanly happen by the receiveng party:
3=seq0 4=seq1, 1=seq0, 2=seq1
Protocol needs in header chunk id and sequence ID.



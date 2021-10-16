# WordsInChatProject
 Counts frequency of words in group chat for a given person.
 Creates 2 files, one for storing every word the person types, and one for every unique word, their frequency, and the total number of words written at the end.
 
 To run it in the terminal:
 java WordsInChat.java <chat file name.txt> <name of person, case sensitive> <number of leading characters>
 E.g. java WordsInChat.java my_chat.txt "John Doe" 23
 
 This program assumes 2 things
   1. Each message starts with the same number of leading characters, usually for the timestamp (this number is 23 for WhatsApp).
   2. Each will also include the name of the person sending the message followed by ':' before the actual message that was sent.
 

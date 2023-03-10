public class Game {
    private Room currentRoom;
    private Parser parser;

    private Player player;
    private Room circle;
    private Room spring;
    private Room summer;
    private Room winter;
    private Room fall;
    private Room pond;

    private boolean wantToQuit;
    Item tome;
    Item hat;

    Item glasses;

    Item blanket;

    public Game() {
        parser = new Parser();
        player = new Player();

    }

    public static void main(String[] args) {
        Game game = new Game();
        game.createRooms();
        game.play();
    }

    private void createRooms() {
        circle = new Room("You are standing in front of a low, dense hedge. You are surrounded by 4 large stones, one named after each season.",
                "You turn around in a circle, observing each stone and trying to figure out how to get out. They appear to be impenetrable stone walls with no apparent entry point.");
        winter = new Room(" You lean against the door labeled 'Winter' and it groans open, the sound of scraping stone fills the garden. You tumble into the darkness. The door clicks shut behind you.",
                "Your breath is visible in front of you as you breathe the frosty air. You become acutely aware that you are only wearing a Hard Rock Cafe T-Shirt and cargo pants two sizes too small. " +
                        "You wish you had invested in the Hard Rock sweatshirt and sweatpants. You wander about, rubbing your hands together and shivering. All you see is a pristine winter landscape, pine trees, snow " +
                        "covered hills. Then, your eye catches a glisten in the grey winter sunlight. A well worn leather bound tome is lying in the snow. It's gossamer pages glint brightly, inviting further investigation");
        summer = new Room("You push against the Stone labeled 'Summer' and it swings open freely, welcoming you into its warmth and brightness.", "Any lingering chill from your time in the cold of winter " +
                "dissipates instantly. Warmth seems to surround and envelop you. You feel sand beneath your feet and hear the distant sounds of the ocean. Lying in the sand by your feet is a tattered baseball hat. ");
        spring = new Room("You push gently on the stone labeled 'Spring', but it doesn't budge. You push harder, still nothing. You try one last time and suddenly the door springs open. You are surprised by this" +
                " sudden motion and fall headfirst into the room.",
                "You stand up and brush yourself off. You look up and take in your surroundings. You are greeted by the smell of a fresh rain and the grass beneath your feet is pleasantly damp. You wander for " +
                        "some time enjoying the pleasant weather. You come upon a picnic blanket laid out invitingly on a grassy slope.");
        fall = new Room("As you approach the stone labeled 'Fall' it swings outwards toward you and a cold breeze issues out of the room.",
                "You step boldly into the room and are affronted by a cutting wind. Despite the abrasive weather the scenery is beautiful. Autumnal colors ranging from deep browns to young oranges meet your eye " +
                        "everywhere you turn. Abandoned amongst the fallen leaves you see a pair of elegant reading glasses. The lenses glisten in the cool autumn light.");
        pond = new Room("You go through. You are standing by a pond. A peaceful calm comes over you.", "You lay out your blanket as a resting place. You put your hat on and are pleasantly shaded" +
                " from the sunbeams filtering through the trees. You put on your glasses and everything comes into focus. You sit down and take out your tome. This will be a very pleasant afternoon. You open the book and begin to read");


        winter.setExit("South", circle);
        fall.setExit("East", circle);
        spring.setExit("West", circle);
        summer.setExit("North", circle);
        circle.setExit("North", winter);


        tome = new Item();
        hat = new Item();
        glasses = new Item();
        blanket = new Item();

        winter.setItem("tome", tome);
        summer.setItem("hat", hat);
        spring.setItem("blanket", blanket);
        fall.setItem("glasses", glasses);
        currentRoom = circle;

    }

    public void play() {
        printWelcome();

        boolean finished = false;

        while (!finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Your presence as a guest of the manor was a delight, we hope you return soon.");
    }

    private boolean processCommand(Command command) {
        wantToQuit = false;

        CommandWord commandWord = command.getCommandWord();

        switch (commandWord) {
            case UNKNOWN:
                System.out.println("I am terribly sorry, but I do not understand this request.");
                break;
            case HELP:
                printHelp();
                break;
            case GO:
                goRoom(command);
                break;
            case QUIT:
                wantToQuit = true;
                break;
            case DROP:
                drop(command);
                break;
            case GRAB:
                grab(command);
                break;
            case LOOK:
                look(command);
                break;
            case REFLECT:
                reflect(command);
                break;

        }
        return wantToQuit;
    }

    private void look(Command command) {
        if (command.hasSecondWord() && currentRoom != pond) {
            System.out.println("You are already looking at " + command.getSecondWord() + ". You can't look at it again. Sorry.");
        }
         else {
            System.out.println(currentRoom.getLongDescription());
            System.out.println(player.getItemString());
        }
    }

    private void goRoom(Command command) {
        if (!command.hasSecondWord()) {
            System.out.println("Go where?");
            return;
        }
        String direction = command.getSecondWord();
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("You are not yet ready to travel in this direction. Reflect on what you can do now and return when you have gained new knowledge");
        } else {
            currentRoom = nextRoom;
            System.out.println(currentRoom.getShortDescription());
        }
        if (currentRoom == pond){
            look(command);
            System.out.println("You win!");
            wantToQuit = true;
        }
    }

    private void grab(Command command) {
        if (!command.hasSecondWord()) {
            System.out.println("Grab what?");
            return;
        }
        String itemName = command.getSecondWord();
        Item grabItem = currentRoom.getItem(itemName);

        if (grabItem == null) {
            System.out.println("I have never seen someone grab a " + itemName + ". That doesn't work. Sorry.");
        } else {
            player.setItem(itemName, grabItem);
            System.out.println("You pick up the " + itemName + " and add it to your knapsack.");
        }
        if (player.getInventory().containsKey("tome")) {
            circle.setExit("East", spring);
        }
            if (player.getInventory().containsKey("hat")) {
                circle.setExit("West", fall);
            }

                if (player.getInventory().containsKey("blanket")) {
                    circle.setExit("South", summer);
                }
        if (player.getInventory().containsKey("glasses")) {
            System.out.println("Sometimes to move forward, you must first seek help");
        }

        }


        private void drop (Command command){
            if (!command.hasSecondWord()) {
                System.out.println("Drop what?");
                return;
            }
            String itemName = command.getSecondWord();
            Item dropItem = player.getItem(itemName);

            if (dropItem == null) {
                System.out.println("I have never seen someone drop a " + itemName + ". That doesn't work. Sorry.");
            } else {
                currentRoom.setItem(itemName, dropItem);
            }
            if (!player.getInventory().containsKey("tome")) {
                circle.getExitMap().remove("South");
            }
            if (!player.getInventory().containsKey("hat")) {
                circle.getExitMap().remove("West");
                }
            if (!player.getInventory().containsKey("blanket"))   {
                circle.getExitMap().remove("South");
            }


            }

            private void reflect (Command command) {
                if (command.hasSecondWord()) {
                    System.out.println("Sorry, you are not ready yet. You lose. Don't overcomplicate things next time. Just reflect.");
                    wantToQuit = true;
                }
                else {
                    circle.setExit("Through", pond);
                    System.out.println("Return to the center and take a closer look.");
                }
            }



        private void printHelp () {
        System.out.println("Hello lost one! Your command words are:");
        if(player.getInventory().containsKey("glasses")){
            parser.showCommands();
        }
        else{
            parser.showFakeCommands();
        }
        }
        private boolean quit (Command command){
            if (command.hasSecondWord()) {
                System.out.println("Apologies, but I don't know how to quit" + command.getSecondWord());
                return false;
            }
            else {
                return true;
            }
        }
        private void printWelcome () {
            System.out.println();
            System.out.println("Welcome to my text adventure");
            System.out.println("Your command words are:");
            parser.showFakeCommands();
            System.out.println("If you ever forget them, just type help and you can see them all again.");
            System.out.println("");
            System.out.println(currentRoom.getShortDescription());
        }
    }

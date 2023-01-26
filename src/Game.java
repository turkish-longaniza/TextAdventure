public class Game {
    private Room currentRoom;
    private Parser parser;

    private Player player;
    Item obj;
    Item obj2;
    public Game(){
        parser = new Parser();
        player = new Player();

    }
    public static void main(String[]args){
        Game game = new Game();
        game.createRooms();
        game.play();
    }
    private void createRooms(){
        Room circle = new Room("You open your eyes standing in front of a low, dense hedge. You are surrounded by 4 large stones, one named after each season.",
                "You wander to each stone and inspect them. They appear to be impenetrable stone walls with no apparent entry point. You come upon the stone labeled 'Winter'. " +
                        "A cutting from a pine tree lays on the ground in front of the stone. You use it to brush away some dust on the stone. An outline of a door begins to appear." +
                        " You lean against the door and it groans open, the sound of scraping stone fills the garden. You tumble into the darkness. The door clicks shut behind you.");
        Room winter = new Room("Your breath is visible in front of you as you breathe the frosty air. You become acutely aware that you are only wearing a Hard Rock Cafe T-Shirt and cargo pants.","");
        Room fall = new Room("", "");
        Room spring = new Room("", "");
        Room summer = new Room("", "");
        Room pond = new Room("", "");


        winter.setExit("South", circle);
        fall.setExit("East", circle);
        spring.setExit("West", circle);
        summer.setExit("North", circle);
        circle.setExit("North", winter);
        circle.setExit("South", summer);
        circle.setExit("East", spring);
        circle.setExit("West", fall);
        circle.setExit("Down", pond);

       obj = new Item();
       obj2 = new Item();

        player.setItem("one", obj);
        currentRoom = circle;

    }
    public void play() {
        printWelcome();

        boolean finished = false;

        while(!finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Your presence as a guest of the manor was a delight, we hope you return soon.");
    }

    private boolean processCommand(Command command) {
        boolean wantToQuit = false;

        CommandWord commandWord = command.getCommandWord();

        switch(commandWord) {
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

        }
        return wantToQuit;
    }
    private void look (Command command) {
        if(command.hasSecondWord()) {
            System.out.println("You are already looking at " + command.getSecondWord() + ". You can't look at it again. Sorry.");
        }
        else{
            System.out.println(currentRoom.getLongDescription());
            System.out.println(player.getItemString());
        }
    }
    private void goRoom(Command command){
        if(!command.hasSecondWord()) {
            System.out.println("Go where?");
            return;
        }
        String direction = command.getSecondWord();
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null){
            System.out.println("There is no door!");
        }
        else{
            currentRoom = nextRoom;
        }
    }

    private void grab(Command command){
        if(!command.hasSecondWord()) {
            System.out.println("Grab what?");
            return;
        }
        String itemName = command.getSecondWord();
        Item grabItem = currentRoom.getItem(itemName);

        if (grabItem == null){
            System.out.println("I have never seen someone grab a " + itemName + ". That doesn't work. Sorry.");
        }
        else {
            player.setItem(itemName, grabItem);
        }
    }

    private void drop(Command command) {
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
    }

    private void printHelp() {
        System.out.println("You grow overwhelmed by the scent of hydrangeas, you grow faint from the overbearing stench.");
        System.out.println("When you awake you see a slip of paper has been placed by your head...");
        System.out.println("It reads...");
        System.out.println("Hello traveler! Your command words are:");
        parser.showCommands();
        System.out.println("signed, I.J.");

    }
    private boolean quit(Command command) {
        if (command.hasSecondWord()) {
            System.out.println("Apologies, but I don't know how to quit" + command.getSecondWord());
            return false;
        }
        else {
            return true;
        }
    }
    private void printWelcome(){
        System.out.println();
        System.out.println("Welcome to my text adventure");
        System.out.println("If you ever need help just ask. Type 'help' and help you shall receive.");
    }
}
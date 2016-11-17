import src.GameGUI;
import src.menu.MainMenu;

public class GameLauncher {
	
	public static void main(String[] args) throws Exception{

		try{
			String server = args[0];
			String username = args[1];
			int max = Integer.parseInt(args[2]);

			MainMenu.setClientVars(server, username, max);
			GameGUI game = new GameGUI("The Boss Fight");
        }catch(ArrayIndexOutOfBoundsException e){
            System.out.println("Usage: java GameLauncher <server ip> <username> <numberOfPlayers>");
        }
	}

}
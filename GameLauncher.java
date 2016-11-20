import src.GameGUI;
import src.menu.MainMenu;

public class GameLauncher {
	
	public static void main(String[] args) throws Exception{

		try{
			String server = args[0];
			int max = Integer.parseInt(args[1]);

			MainMenu.setClientVars(server, max);
			GameGUI game = new GameGUI("The Boss Fight");
        }catch(ArrayIndexOutOfBoundsException e){
            System.out.println("Usage: java GameLauncher <server ip> <numberOfPlayers>");
        }
	}

}
package csci331.team.red.shared;

import java.util.ArrayList;

/**
 * Wrapper class for words said by the players and actors. <br>
 * FIXME: <b>Stub</b>
 * 
 * @author ojourmel , lduperron
 */
public class Dialogue {
	public static final Dialogue GENERIC = new Dialogue("Ha Ha, that was funny" , "Diety");

	private String speaker;
	private String dialogue;
	private Enum<?> callbackCode;
	
	
	
	public Dialogue(String words , String speaker) 
	{
		this(words, speaker, null);
	}
	
	public Dialogue(String words , String speaker , Enum<?> c) 
	{
		this.dialogue = words;
		this.speaker = speaker;
		this.callbackCode = c;
	}
	
	public static Dialogue[] returnDialogArray(String[][] strings)
	{
		ArrayList<Dialogue> temp = new ArrayList<Dialogue>();
		
		for(int i = 0; i < strings.length; i++)
		{
			Dialogue tempDialog = new Dialogue(strings[i][0] , strings [i][1]);
			temp.add(tempDialog);
			
			
		}

		return temp.toArray(new Dialogue[0]);
		
	}
	
	public static Dialogue[] returnDialogArray(String[][] strings , Enum<?>[] callbackArray)
	{
		ArrayList<Dialogue> temp = new ArrayList<Dialogue>();
		
		for(int i = 0; i < strings.length; i++)
		{
			Dialogue tempDialog = null;
			if(callbackArray[i] != null)
			{
				tempDialog = new Dialogue(strings[i][0] , strings [i][1],  callbackArray [i]);
			}
			else
			{
				tempDialog = new Dialogue(strings[i][0] , strings [i][1]);
				
			}
			
			temp.add(tempDialog);
			
			
		}

		return temp.toArray(new Dialogue[0]);
		
	}
	
	

	public String getDialogue() {
		return dialogue;
	}
	
	public String getSpeaker() {
		return speaker;
	}
	public Enum<?> getCallbackCode()
	{
		return callbackCode;
	}
}

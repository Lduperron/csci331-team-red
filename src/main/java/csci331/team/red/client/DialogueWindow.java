package csci331.team.red.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
/**
 * Displays dialog.  Has an internal reference to another dialog window and to an Enum<?> called Callbackcode.
 * If one is not null, it's executed when the textbox is clicked.
 * @author Lduperron
 */

public class DialogueWindow extends TextButton {

	TextButton internalReference;
	TextButton Speaker;
	
	DialogueWindow nextWindow;
	
	Enum<?> Callbackcode;
	
	public DialogueWindow(String dialogue, String thespeaker, TextButtonStyle style , Stage parentStage) {
		this(dialogue, thespeaker, style, parentStage,  false , 0 , true, null , null, null);
	}
	
	public DialogueWindow(String dialogue, String thespeaker, TextButtonStyle style , Stage parentStage , DialogueWindow nextWindow) {
		this(dialogue, thespeaker, style, parentStage,  false , 0 , true,  null , null, null);
	}
	
	public DialogueWindow(String dialogue, String thespeaker, TextButtonStyle style , Stage parentStage, boolean useWordWrap , int extraPadding , Boolean displayNow , DialogueWindow nextWindow , Enum<?> c, final DialogueCallback<Callback> dcall) {
		this(dialogue, thespeaker, style , parentStage, useWordWrap , extraPadding , displayNow, nextWindow, c , Gdx.graphics.getHeight(), dcall);
	}
	

	public DialogueWindow(String dialogue, String thespeaker, TextButtonStyle style , Stage parentStage, boolean useWordWrap , int extraPadding , Boolean displayNow , DialogueWindow nextWindow , Enum<?> c , float yPosition, final DialogueCallback<Callback> dcall) {
		this(dialogue, thespeaker, style , parentStage, useWordWrap , extraPadding , displayNow , nextWindow , c , yPosition, dcall , -1) ;
			
	
	}
	
	public DialogueWindow(String dialogue, String thespeaker, TextButtonStyle style , Stage parentStage, boolean useWordWrap , int extraPadding , Boolean displayNow , DialogueWindow nextWindow , Enum<?> c , float yPosition, final DialogueCallback<Callback> dcall , float width) {
		super(dialogue, style , useWordWrap , extraPadding);
		
		internalReference = this;
		
		if(nextWindow != null)
		{
			this.nextWindow = nextWindow;
			
		}
		if(c != null )
		{
			this.Callbackcode = c;
			
		}
		
		this.setPosition(0 , yPosition - this.getHeight());
		
		
		if(width != -1)
		{
			this.setWidth(width);
			
		}

		Speaker = new TextButton(thespeaker, style , false , 0);
    	Speaker.setPosition(0, this.getY()-Speaker.getHeight() - 5);
    	
    	if(displayNow)
    	{
	    	parentStage.addActor(this);
	    	parentStage.addActor(Speaker);
    	}
		
    	this.addListener(new ClickListener() {
    		
    		
			@Override
    	    public void clicked(InputEvent event, float x, float y) 
    	    {
    	    	DialogueWindow thisActor = (DialogueWindow)event.getListenerActor();
    	    	
    	    	if(thisActor.nextWindow != null)
    	    	{
    	    		thisActor.getStage().addActor(thisActor.nextWindow);
    	    		thisActor.getStage().addActor(thisActor.nextWindow.Speaker);
    	    	}
    	    	if(thisActor.Callbackcode != null)
    	    	{
    	    		
    	    		
    	    		
    	    		dcall.call((Callback) thisActor.Callbackcode);
    	    		
    	    	}
    	    	
    	    	
    	    	thisActor.Speaker.remove();
    	    	thisActor.remove();
    	    	
    	    };
    		
    	});
		
	}

}

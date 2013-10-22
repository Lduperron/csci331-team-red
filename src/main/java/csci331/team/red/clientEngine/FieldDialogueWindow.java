package csci331.team.red.clientEngine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import csci331.team.red.clientEngine.DatabaseAgentScreen.DatabaseDialogCallbacks;
import csci331.team.red.clientEngine.FieldAgentScreen.fieldDialogCallbacks;
/**
 * @author Lduperron
 */

public class FieldDialogueWindow extends TextButton {

	TextButton internalReference;
	TextButton Speaker;
	
	FieldDialogueWindow nextWindow;
	
	fieldDialogCallbacks.callbacks dialogCallbackcode;
	
	public FieldDialogueWindow(String dialogue, String thespeaker, TextButtonStyle style , Stage parentStage) {
		this(dialogue, thespeaker, style, parentStage,  false , 0 , true, null , null);
	}
	
	public FieldDialogueWindow(String dialogue, String thespeaker, TextButtonStyle style , Stage parentStage , FieldDialogueWindow nextWindow) {
		this(dialogue, thespeaker, style, parentStage,  false , 0 , true,  null , null);
	}
	
	public FieldDialogueWindow(String dialogue, String thespeaker, TextButtonStyle style , Stage parentStage, boolean useWordWrap , int extraPadding , Boolean displayNow , FieldDialogueWindow nextWindow , fieldDialogCallbacks.callbacks c) {
		this(dialogue, thespeaker, style , parentStage, useWordWrap , extraPadding , displayNow, nextWindow, c , Gdx.graphics.getHeight());
	}
	
	public FieldDialogueWindow(String dialogue, String thespeaker, TextButtonStyle style , Stage parentStage, boolean useWordWrap , int extraPadding , Boolean displayNow , FieldDialogueWindow nextWindow , fieldDialogCallbacks.callbacks c , float yPosition) {
		super(dialogue, style , useWordWrap , extraPadding);
		
		internalReference = this;
		
		if(nextWindow != null)
		{
			this.nextWindow = nextWindow;
			
		}
		if(c != null )
		{
			this.dialogCallbackcode = c;
			
		}
		
		this.setPosition(0 , yPosition - this.getHeight());
		
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
    	    	FieldDialogueWindow thisActor = (FieldDialogueWindow)event.getListenerActor();
    	    	
    	    	if(thisActor.nextWindow != null)
    	    	{
    	    		thisActor.getStage().addActor(thisActor.nextWindow);
    	    		thisActor.getStage().addActor(thisActor.nextWindow.Speaker);
    	    	}
    	    	if(thisActor.dialogCallbackcode != null)
    	    	{
    	    		fieldDialogCallbacks.call(thisActor.dialogCallbackcode);
    	    	}
    	    	
    	    	
    	    	thisActor.Speaker.remove();
    	    	thisActor.remove();
    	    	
    	    };
    		
    	});
		
	}

}

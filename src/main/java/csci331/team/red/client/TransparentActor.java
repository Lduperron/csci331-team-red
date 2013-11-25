package csci331.team.red.client;

import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;

/**
 * Extention to contain an actor.  Handles transparent backgrounds.
 * @author Lduperron
 */



public class TransparentActor extends Actor 
{

	Pixmap pixmap;
	Texture texture;
	TweenManager tweenmanager;
	
	Vector2 localCords = null;
	


	public TransparentActor (Pixmap incomingPixmap) 
	{
		this(incomingPixmap, null);
	}
	
	public TransparentActor (Pixmap incomingPixmap, TweenManager incomingTweener) 
	{
		pixmap = incomingPixmap;
		tweenmanager = incomingTweener;
		
			
		texture = new Texture(pixmap);
		
		localCords = new Vector2();
			
		texture = new Texture(pixmap);
				
		setWidth(texture.getWidth());
		setHeight(texture.getHeight());
		this.setBounds(0, 0, getWidth() , getHeight());
				
		setTouchable(Touchable.enabled);
		
		this.addListener((new ClickListener()
		{
			
			public void clicked(InputEvent event, float x, float y)
			{
				event.getTarget().toFront();								
				
			}
			
		}));
	}
	
	public void setDragable ()
	{
		
		this.addListener((new DragListener() 
		{ 
			public void touchDragged (InputEvent event, float x, float y, int pointer) 
		 	{ 
					float dx = x-getTouchDownX(); 
					float dy = y-getTouchDownY(); 
					event.getTarget().setPosition(event.getTarget().getX() + dx, event.getTarget().getY() + dy); 
					event.getTarget().toFront();
			 }
		}));
	}
		
	 

	public void draw (SpriteBatch batch, float parentAlpha) 
	{
		Color color = getColor();
		batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
		
		batch.draw(texture, getX(), getY(), getOriginX(), getOriginY(), texture.getWidth(), texture.getHeight(), getScaleX(), getScaleY(), getRotation(), 0, 0, texture.getWidth(), texture.getHeight(), false,  false);
	}
	
	public Rectangle getBoundingRectangle()
	{
		
		return this.getBoundingRectangle();
		
	}
	
	@Override
	public Actor hit(float x, float y, boolean touchable) 
	{
		 if (touchable && getTouchable() != Touchable.enabled)
		 {
		 		return null;
		 }
		 
		 else
		 {
			if(x >= 0 && x < getWidth() && y >= 0 && y < getHeight())
			{
				 int pixel = pixmap.getPixel((int)x, (int)(getHeight() - y));
				 
				 if ((pixel & 0x000000ff) != 0) 
				 {
				 	return this;
				 }
				
				
			}
		 }

		 return null;
	}
}
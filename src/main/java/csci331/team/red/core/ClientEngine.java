package csci331.team.red.core;

import java.util.Iterator;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * 
 * @author duperrlc
 */
public class ClientEngine extends Game {
	SpriteBatch batch;
	BitmapFont font;
	
	AssetManager manager;
	
	mainGameScreen theGame;
	MainMenuScreen title;

	@Override
	public void create() {
		
		title = new MainMenuScreen(this);
		theGame = new mainGameScreen(this);
		
		
		batch = new SpriteBatch();
		// Use LibGDX's default Arial font.
		font = new BitmapFont();
		this.setScreen(title);
	    
	}

	@Override
	public void dispose() {
		
	    font.dispose();
	    batch.dispose();
		
		
	}

	@Override
	public void render() {
		super.render();
	    
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
	

	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		// System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
		cfg.title = "FinalProject";
		cfg.useGL20 = false;
		cfg.width = 1280;
		cfg.height = 1024;

		new LwjglApplication(new ClientEngine(), cfg);
	}
}
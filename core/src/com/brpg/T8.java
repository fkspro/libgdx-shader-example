package com.brpg;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class T8 extends ApplicationAdapter {
    Mesh mesh;
    ShaderProgram shaderProgram;
    OrthographicCamera cam;
    float aspectratio;
    Texture tank_tex;  // in case you want to render a tank
    
    @Override
    public void create () {
	// this is only used if you use the fragment shader
	tank_tex = new Texture(Gdx.files.internal("blue.png"));

	// Manually defining a mesh is painful and should be avoided
	float[] verts = new float[30];
        int i = 0;
        float x,y; // Mesh location in the world
        float width,height; // Mesh width and height

        x = y = 0f;
        width = Gdx.graphics.getWidth();
	height = Gdx.graphics.getHeight();

	aspectratio = height / width;

        //Top Left Vertex Triangle 1
        verts[i++] = x;   //X
        verts[i++] = y + height; //Y
        verts[i++] = 0;    //Z
        verts[i++] = 0f;   //U
        verts[i++] = 0f;   //V

        //Top Right Vertex Triangle 1
        verts[i++] = x + width;
        verts[i++] = y + height;
        verts[i++] = 0;
        verts[i++] = 1f;
        verts[i++] = 0f;

        //Bottom Left Vertex Triangle 1
        verts[i++] = x;
        verts[i++] = y;
        verts[i++] = 0;
        verts[i++] = 0f;
        verts[i++] = 1f;

        //Top Right Vertex Triangle 2
        verts[i++] = x + width;
        verts[i++] = y + height;
        verts[i++] = 0;
        verts[i++] = 1f;
        verts[i++] = 0f;

        //Bottom Right Vertex Triangle 2
        verts[i++] = x + width;
        verts[i++] = y;
        verts[i++] = 0;
        verts[i++] = 1f;
        verts[i++] = 1f;

        //Bottom Left Vertex Triangle 2
        verts[i++] = x;
        verts[i++] = y;
        verts[i++] = 0;
        verts[i++] = 0f;
        verts[i] = 1f;

        // Create a mesh out of two triangles rendered clockwise without indices
        mesh = new Mesh(true, 6, 0,
			 new VertexAttribute(VertexAttributes.Usage.Position,
					     3, ShaderProgram.POSITION_ATTRIBUTE ),
			 new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2,
					     ShaderProgram.TEXCOORD_ATTRIBUTE+"0" ) );
        mesh.setVertices(verts);

        shaderProgram = new ShaderProgram(   // vertex.glsl is the stock libgdx vertex shader
					  Gdx.files.internal("vertex.glsl").readString(),
					  // grid.glsl is in android/assets, and compiled at runtime
					  //Gdx.files.internal("grid.glsl").readString()
					  // alternate ultra simple texture mapping shader
					  Gdx.files.internal("simple-fragment.glsl").readString()
					  );
	if (!shaderProgram.isCompiled()) {
	    // these can be some pretty good mind puzzles
            throw new GdxRuntimeException(shaderProgram.getLog());
	}

	cam = new OrthographicCamera();
	cam.setToOrtho(false);  // i forget what this even does
	
        cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
	cam.zoom = 1.0f;
        cam.update();
    }
    
    // i never wrote a method to handle resizing but apparently the default
    // methods are dealing with it correctly

    @Override
	public void render() {
	Gdx.gl.glClearColor(1, 0, 0, 1);
	Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

	// textures are handled in a separate part of the gpu and must be
	// bound to texture mapping units before being used.  there aren't
	// a lot of mapping units which is why texture atlases are a big win
	// the texture is only used for the simple fragment shader
	tank_tex.bind(0);
	
	// Rendering directly to a mesh
	// the grid renderer doesn't use a texture at all
	shaderProgram.begin();
	shaderProgram.setUniformMatrix("u_projTrans", cam.combined);
	// for the simple shader we have to pass in the texture unit index
	// this will give an error if left in for the grid shader
	//shaderProgram.setUniformi("u_texture", 0);
	mesh.render(shaderProgram, GL20.GL_TRIANGLES);
	shaderProgram.end();
    }
    
}

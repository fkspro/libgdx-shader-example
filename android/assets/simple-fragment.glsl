#ifdef GL_ES
    precision mediump float;
#endif

// ultra simple fragment shader, a one-liner if you just want to draw a texture on a mesh
varying vec2 v_texCoords;
uniform sampler2D u_texture;
// for the normal batch() method of rendering the following are also required,
// but we don't use them in this code
//varying vec4 v_color;
//uniform mat4 u_projTrans;

void main() {
  // literally just mapping the texture 1:1
  // texture2D takes an index to a texture mapper and a
  // vector of x,y coordinates in the range 0-1 and returns
  // the color of the texture at that point
  //gl_FragColor = texture2D(u_texture, v_texCoords);

  // if you want to paint it gray
  vec3 color = texture2D(u_texture, v_texCoords).rgb;
  float gray = (color.r + color.g + color.b) / 3.0;
  gl_FragColor = vec4(gray, gray, gray, 1.0);

  // if you want to only show the green part
  //gl_FragColor = vec4(0.0, color.g, 0.0, 1.0);

  // if you want to paint it black
  // gl_FragColor = vec4(0.0, 0.0, 0.0, 1.0);
  
}

#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;

// shitty grid shader
void main() {
  // width of grid lines
  float width = 0.0005;
  // step size, a size of 0.1 means 1/0.1 = 10 grid squares
  vec2 step = vec2(0.2, 0.2);
  // use modulus to cycle input coordinates thru a range of 0 to step size, then center
  // grid lines draw in center of these ranges
  // these are all 2d vectors so the code does x and y at the same time
  vec2 relpos = mod(v_texCoords, step) - step / 2.0;
  // this is like looking at the gaussian distribution from the top down, satisfying
  vec2 grid = exp(-1.0 * relpos * relpos / width);
  // alternate hard edged grid that draws pixels until 0.0015 away from center line on both sides
  // vec2 grid = vec2(float(abs(relpos.x) < width), float(abs(relpos.y) < width));

  // figure out the final intensity of the pixel by combining the horizontal and vertical grid calculations
  // making grid intersections not look weird is surprisingly hard
  // apparently opengl calls pixels rendered onto a mesh "fragments", or something
  float frag = max(grid.x, grid.y);  // gives a weird X effect at intersections
  // float frag = min(1.0, grid.x + grid.y);  // overly bright spot at intersections
  //float frag = (grid.x + grid.y) / 2.0;   // averaging them sucks in the same manner as using min

  // fixed output color hardcoded here, could do a lot more
  // 1.0/frag is the lazy way to get an alpha of 1 and not have to put * frag next to each color
  // i'm hoping the compiler optimizes this out
  gl_FragColor = frag * vec4(0.1, 0.7, 0.5, 1.0/frag);

}

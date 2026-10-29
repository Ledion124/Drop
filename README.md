# Drop

My first game with [libGDX](https://libgdx.com/). Raindrops fall, you move the bucket and try to catch them. Simple, but it's mine.

I built it by following the official [A Simple Game](https://libgdx.com/wiki/start/a-simple-game) tutorial and went through every part until I actually understood it.

## Controls

- `A` / `D` to move left and right
- Or just click / drag with the mouse

## Run it

You need Java 17 or newer.

```
gradlew lwjgl3:run
```

## What I learned

This was my first time using libGDX, so pretty much everything was new:

- **The game loop.** libGDX calls `render()` around 60 times a second, and every frame I read input, update the game, then draw. Once that clicked, a lot of things made sense.
- **Why `create()` and not the constructor.** The constructor runs before the window and OpenGL exist, so loading textures there crashes. `create()` runs once everything is ready.
- **Viewports and world units.** My world is 8 by 5 units instead of pixels, and the `FitViewport` scales it to any window size without stretching. Kind of like percentages in CSS.
- **Delta time.** Without it, the bucket moves faster on a faster computer. Multiplying speed by delta makes it move the same distance per second everywhere.
- **Textures vs. sprites.** A texture is just the image, a sprite is the image plus its position and size. Lots of raindrops can share one texture.
- **Draw order.** Whatever gets drawn last ends up on top, so the background has to come first.
- **Coordinates.** x and y are the bottom-left corner, which is why I subtract the width when clamping the bucket and wait until a drop is `-dropHeight` below the screen before removing it.
- **Spawning with a timer.** Adding delta to a timer every frame and spawning a drop once it passes one second.
- **Removing from a list safely.** Looping backwards so removing a drop doesn't make me skip the next one.
- **Collision.** Putting a `Rectangle` around the bucket and each drop and checking `overlaps()`.
- **Sound and music.** Looping background music and playing a sound when a drop is caught.

I also learned that `core` is where the actual game lives and `lwjgl3` is just the desktop launcher. I found that out the hard way.

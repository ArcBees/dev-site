# Mixins documentation

## How to install {how-to-install}

Add `com/arcbees/gsss/mixin/client/mixin.gss` and your Gss file path in a `@Source` annotation.

```
@Source({"com/arcbees/gsss/mixin/client/mixins.gss", "style.gss"})
public Style style();
```

Add a `@require 'mixins';` at the beginning of your Gss file that uses the mixins. This will force the compiler to check at compile time that the source is well imported.

You can now use any mixins in your style.gss file.

## Border radius {border-radius}

### Params

*   @param TOP size of the top corners
*   @param BOTTOM size of the bottom corners
*   @param LEFT size of the left corners
*   @param RIGHT size of the right corners
*   @param TOP_RIGHT size of the top right corner
*   @param BOTTOM_RIGHT size of the bottom right corner
*   @param BOTTOM_LEFT size of the bottom left corner
*   @param TOP_LEFT size of the top left corner
*   @param SIZE size of all corners

### Mixins

#### borderradius(TOP_RIGHT, BOTTOM_RIGHT, BOTTOM_LEFT, TOP_LEFT)

```
@mixin borderradius(15px, 0, 30px, 5px);
```

$[gsss-mixins-1]

#### rounded(SIZE)

```
@mixin rounded(15px);
```

$[gsss-mixins-2]

#### borderradius_t(TOP)

```
@mixin borderradius_t(15px);
```

$[gsss-mixins-3]

#### borderradius_b(BOTTOM)

```
@mixin borderradius_b(15px);
```

$[gsss-mixins-4]

#### borderradius_l(LEFT)

```
@mixin borderradius_l(15px);
```

$[gsss-mixins-5]

#### borderradius_r(RIGHT)

```
@mixin borderradius_r(15px);
```

$[gsss-mixins-6]

#### borderradius_tr(TOP_RIGHT)

```
@mixin borderradius_tr(15px);
```

$[gsss-mixins-7]

#### borderradius_tl(TOP_LEFT)

```
@mixin borderradius_tl(15px);
```

$[gsss-mixins-8]

#### borderradius_br(BOTTOM_RIGHT)

```
@mixin borderradius_br(15px);
```

$[gsss-mixins-9]

#### borderradius_bl(BOTTOM_LEFT)

```
@mixin borderradius_bl(15px);
```

$[gsss-mixins-10]

## Box sizing {box-sizing}

### Mixins

#### boxsizing()

```
@mixin boxsizing();
```

## Gradient {gradient}

### Params

*   @param FIRST_COLOR first color
*   @param SECOND_COLOR second color
*   @param INNER_COLOR inner color
*   @param OUTER_COLOR outer color

### Mixins

#### gradient(FIRST_COLOR, SECOND_COLOR)

```
@mixin gradient(#ff22ee, #0044e1);
```

$[gsss-mixins-11]

#### gradient_h(FIRST_COLOR, SECOND_COLOR)

```
@mixin gradient_h(#ff22ee, #0044e1);
```

$[gsss-mixins-12]

#### gradient_tlbr(FIRST_COLOR, SECOND_COLOR)

```
@mixin gradient_tlbr(#ff22ee, #0044e1);
```

$[gsss-mixins-13]

#### gradient_bltr(FIRST_COLOR, SECOND_COLOR)

```
@mixin gradient_bltr(#ff22ee, #0044e1);
```

$[gsss-mixins-14]

#### gradient_circular(FIRST_COLOR, SECOND_COLOR)

```
@mixin gradient_circular(#ff22ee, #0044e1);
```

$[gsss-mixins-15]

## Opacity {opacity}

### Params

*   @param ALPHA shadow opacity (0 to 1)

### Mixins

#### opacity(ALPHA)

```
@mixin opacity(0.5);
```

$[gsss-mixins-16]

## Outline {outline}

### Params

*   @param SIZE size of the border outline
*   @param BORDER border style type
*   @param COLOR color of the outline
*   @param OFFSET offset from the element

### Mixins

#### outline(SIZE, BORDER, COLOR, OFFSET)

```
@mixin outline(2px, solid, #000, 10px);
```

$[gsss-mixins-17]

## Shadow {shadow}

### Params

*   @param HORIZONTAL position of the horizontal shadow
*   @param VERTICAL position of the vertical shadow
*   @param BLUR distance of the blur
*   @param SPREAD shadow size
*   @param COLOR shadow color
*   @param ALPHA shadow opacity (0 to 1)

### Mixins

#### shadow(HORIZONTAL, VERTICAL, BLUR, ALPHA)

```
@mixin shadow(5px, 5px, 3px, 0.3);
```

$[gsss-mixins-18]

#### shadow_color(HORIZONTAL, VERTICAL, BLUR, SPREAD, COLOR)

```
@mixin shadow_color(5px, 5px, 10px, 2px, #22ee22);
```

$[gsss-mixins-19]

#### shadow_inner(HORIZONTAL, VERTICAL, BLUR, ALPHA)

```
@mixin shadow_inner(0, 7px, 5px, 0.4);
```

$[gsss-mixins-20]

#### shadow_inner_color(HORIZONTAL, VERTICAL, BLUR, SPREAD, COLOR)

```
@mixin shadow_inner_color(0, 7px, 5px, 2px, rgba(0, 0, 255, 0.4));
```

$[gsss-mixins-21]

#### shadow_text(HORIZONTAL, VERTICAL, BLUR, ALPHA)

```
@mixin shadow_text(2px, 2px, 1px, 0.5);
```

$[gsss-mixins-22]

#### shadow_text_color(HORIZONTAL, VERTICAL, BLUR, COLOR)

```
@mixin shadow_text_color(0, 5px, 2px, #11ff66);
```

$[gsss-mixins-23]

#### shadow_none()

```
@mixin shadow_none();
```

## Transform {transform}

### Params

*   @param DEGREES angle of the rotation
*   @param RATIO_HORIZONTAL horizontal ratio (x) where 1 = no changes and 2 = twice the size
*   @param RATIO_VERTICAL vertical ratio (y) where 1 = no changes and 2 = twice the size
*   @param HORIZONTAL horizontal change (x) in pixels
*   @param VERTICAL vertical change (x) in pixels

### Flip

#### flip_horizontal()

```
@mixin flip_horizontal();
```

$[gsss-mixins-24]

#### flip_vertical()

```
@mixin flip_vertical();
```

$[gsss-mixins-25]

### Rotate

#### rotate(DEGREE)

```
@mixin rotate(45deg);
```

$[gsss-mixins-26]

#### rotate_180_cw()

```
@mixin rotate_180_cw();
```

$[gsss-mixins-27]

#### rotate_180_ccw()

```
@mixin rotate_180_ccw();
```

### Scale

#### scale(RATIO_HORIZONTAL, RATIO_VERTICAL)

```
@mixin scale(1.5, 1);
```

$[gsss-mixins-28]

### Translate

#### translate(HORIZONTAL, VERTICAL)

```
@mixin translate(10px, 20px);
```

$[gsss-mixins-29]

## Transition {transition}

### Params

*   @param PROPERTY name of the CSS property affected
*   @param DURATION time of the transition effect
*   @param EFFECT speed curve of the transition effect
*   @param DELAY time before the transition effect starts

### Mixins

#### transition(PROPERTY, DURATION)

```
@mixin transition(margin-left, 200);
```

#### transition_effect(PROPERTY, DURATION, EFFECT)

```
@mixin transition(all, 500, ease);
```

#### transition_delay(PROPERTY, DURATION, EFFECT, DELAY)

```
@mixin transition(width, 250, ease, 100);
```

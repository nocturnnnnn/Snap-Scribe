---
name: Espresso Night Journal
colors:
  surface: '#181210'
  surface-dim: '#181210'
  surface-bright: '#3f3835'
  surface-container-lowest: '#120d0b'
  surface-container-low: '#201a18'
  surface-container: '#251e1c'
  surface-container-high: '#2f2826'
  surface-container-highest: '#3b3331'
  on-surface: '#ede0dc'
  on-surface-variant: '#d7c3b1'
  inverse-surface: '#ede0dc'
  inverse-on-surface: '#362f2d'
  outline: '#a08e7d'
  outline-variant: '#524436'
  surface-tint: '#ffb86a'
  primary: '#ffb86a'
  on-primary: '#492900'
  primary-container: '#e89a3c'
  on-primary-container: '#5d3600'
  inverse-primary: '#895200'
  secondary: '#d2c4b2'
  on-secondary: '#372f22'
  secondary-container: '#51483a'
  on-secondary-container: '#c4b6a5'
  tertiary: '#ffb77a'
  on-tertiary: '#4c2700'
  tertiary-container: '#f1953d'
  on-tertiary-container: '#623300'
  error: '#ffb4ab'
  on-error: '#690005'
  error-container: '#93000a'
  on-error-container: '#ffdad6'
  primary-fixed: '#ffdcbc'
  primary-fixed-dim: '#ffb86a'
  on-primary-fixed: '#2c1700'
  on-primary-fixed-variant: '#683d00'
  secondary-fixed: '#efe0cd'
  secondary-fixed-dim: '#d2c4b2'
  on-secondary-fixed: '#221a0f'
  on-secondary-fixed-variant: '#4f4538'
  tertiary-fixed: '#ffdcc2'
  tertiary-fixed-dim: '#ffb77a'
  on-tertiary-fixed: '#2e1500'
  on-tertiary-fixed-variant: '#6d3a00'
  background: '#181210'
  on-background: '#ede0dc'
  surface-variant: '#3b3331'
typography:
  display-lg:
    fontFamily: Literata
    fontSize: 40px
    fontWeight: '600'
    lineHeight: 48px
    letterSpacing: -0.02em
  display-lg-mobile:
    fontFamily: Literata
    fontSize: 32px
    fontWeight: '600'
    lineHeight: 40px
    letterSpacing: -0.015em
  headline-lg:
    fontFamily: Literata
    fontSize: 28px
    fontWeight: '600'
    lineHeight: 36px
    letterSpacing: -0.01em
  headline-md:
    fontFamily: Literata
    fontSize: 22px
    fontWeight: '500'
    lineHeight: 30px
  headline-sm:
    fontFamily: Literata
    fontSize: 18px
    fontWeight: '500'
    lineHeight: 26px
  body-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
  body-md:
    fontFamily: Plus Jakarta Sans
    fontSize: 14px
    fontWeight: '400'
    lineHeight: 22px
  body-sm:
    fontFamily: Plus Jakarta Sans
    fontSize: 12px
    fontWeight: '400'
    lineHeight: 18px
  label-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 14px
    fontWeight: '600'
    lineHeight: 20px
    letterSpacing: 0.01em
  label-md:
    fontFamily: Plus Jakarta Sans
    fontSize: 12px
    fontWeight: '600'
    lineHeight: 16px
    letterSpacing: 0.02em
  label-sm:
    fontFamily: Plus Jakarta Sans
    fontSize: 11px
    fontWeight: '500'
    lineHeight: 14px
    letterSpacing: 0.03em
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  gutter: 1rem
  margin: 1.25rem
  space-xs: 0.25rem
  space-sm: 0.5rem
  space-md: 1rem
  space-lg: 1.5rem
  space-xl: 2.25rem
---

## Brand & Style

This design system blends **Tactile / Skeuomorphic Stationery** with **Warm Minimalism** tailored for evening reflection, journaling, and mindful capture. 

- **Audience & Context:** Thoughtful note-takers, creative diarists, and night-owls recording intimate memories in low-light environments.
- **Atmosphere:** An intimate, late-night café booth—rich roasted aroma, weathered wood, cream deckle-edge paper, and glowing amber ambient warmth.
- **Design Philosophy:** Avoid aggressive digital OLED blacks and clinical stark white typography. Rely on organic card surfaces, fine stationery-inspired borders, tactile paper highlights, and soothing thermal hues that protect circadian rhythms and foster an unhurried, human-centered journaling cadence.

## Colors

The palette simulates the layered sensory palette of rich roasted coffee beans, parchment paper, and warm amber lamplight:

- **Base Canvas (`#14100E` to `#1C1614`):** The deep roasted ground; anchors the interface in comfortable low-strain darkness without absolute black clipping.
- **Surface & Containers (`#261F1B` & `#322A25`):** Tactile cards and modal trays resembling warm coffee-stained leather covers and smooth desktop surfaces.
- **Accents — Amber & Caramel (`#E89A3C`, `#D9822B`):** Interactive highlights, active recording indicators, badges, and focus rings mimicking warm incandescent filaments.
- **Text & Parchment (`#F5E6D3` & `#A89B91`):** High-readability titles in creamy unbleached paper tone, balanced with soft earthy stone for body copy and metadata.
- **Borders & Dividers (`#3D322B`):** Warm, low-contrast separator lines mimicking subtle bookbinding rules and notebook margins.

## Typography

The typographic hierarchy pairs **Literata** (a warm, bookish, modern digital serif designed for long-form comfort) for entry headers and memorable highlights, with **Plus Jakarta Sans** (a friendly, rounded humanist sans-serif) for quick capture, metadata, navigation, and crisp microcopy.

- **Editorial Serifs (Literata):** Generous x-height and organic terminals bring the intimacy of personal printed essays and journal entries.
- **Readable Sans (Plus Jakarta Sans):** Soft geometry guarantees illegibility is eliminated across small device screens while maintaining warmth.
- **Night-Reading Balance:** High contrast is preserved between `#F5E6D3` and the `#1C1614` canvas without harsh glare; secondary body text in `#A89B91` ensures secondary thoughts fade gracefully into the background.

## Layout & Spacing

A mobile-first fluid grid optimized for thumb-reach and distraction-free daily logging.

- **Grid Architecture:** 4-column fluid layout on mobile viewports with `1.25rem` screen margins and `1rem` gutters; scales to 8 columns on tablet viewports and 12 columns in desktop journal viewing mode.
- **Vertical Rhythm:** 4px/8px baseline grid logic for all stacked text and media cards. Generous component padding (`1.25rem` to `1.5rem`) within cards gives entries the look and feel of spacious physical journal leaves.
- **Comfort Safe Margins:** Generous bottom clearance accommodates floating action buttons, audio wave capture bars, and toolbars without overlapping the final lines of evening thoughts.

## Elevation & Depth

Visual hierarchy does not use cold, synthetic drop shadows. Instead, it relies on **Tonal Layers** accented with fine **Warm Stationery Outlines** and subtle **Caramel Backlight Glows**:

- **Layer 0 (Canvas Base):** Grounded at `#14100E`.
- **Layer 1 (Cards & Journal Pages):** Elevated via `#261F1B` surface color, framed by a soft `1px solid #3D322B` boundary mimicking deckle paper edging.
- **Layer 2 (Modals & Sheet Trays):** Rendered in `#322A25`, accented with an ambient warm diffused shadow: `0 8px 24px -4px rgba(10, 7, 6, 0.65)`.
- **Active / Interactive Elements:** Important floating buttons or active audio recording states cast an amber ambient glow: `0 4px 16px rgba(232, 154, 60, 0.22)`.

## Shapes

The interface embraces organic, tactile curves reminiscent of leather-bound moleskines and smoothed river stones:

- **Cards & Journal Containers:** `rounded-lg` (16px) up to `rounded-xl` (24px) for expansive content blocks, journal prompts, and photo attachments.
- **Interactive Controls (Buttons, Inputs, Audio Pills):** Soft `0.5rem` to `0.75rem` radiuses for inputs, with contextual pills (`9999px`) reserved for status tags, mood chips, and audio playheads.

## Components

### Buttons
- **Primary (Amber Quill):** Solid `#E89A3C` background with deep `#14100E` espresso text, rounded-xl (12-16px) or pill, casting a soft amber ambient shadow. On press: `#D9822B`.
- **Secondary (Woven Paper):** Surface `#261F1B` with a `1px solid #3D322B` border and `#F5E6D3` text.
- **Ghost:** Transparent background with `#E89A3C` text, shifting to subtle `#261F1B` tint when pressed.

### Chips & Mood Selectors
- **Default State:** `#261F1B` fill with `#3D322B` border and `#A89B91` text.
- **Selected State:** Warm terracotta `#F5E6D3` surface with `#1C1614` text, or caramel tint `#3A271B` with `#E89A3C` border and amber text.

### Inputs & Journal Canvas
- **Text Fields:** `#1C1614` sunken field background framed by a `1px solid #3D322B` border. Placeholder text in muted `#74665E`. Active focus switches border to `#E89A3C` with an amber focus ring.
- **Journal Rich-Text Area:** Borderless, clean drafting space over `#261F1B` card surface with subtle, optional notebook ruling lines (`rgba(61, 50, 43, 0.3)`).

### Cards & Snap Containers
- **Journal Entry Card:** Surface in `#261F1B` with a `1px solid #3D322B` outline, generous `1.25rem` internal padding, and 20px corners. Includes embedded photo thumbnails framed with warm off-black borders.

### Checkboxes & Radio Toggles
- Custom rounded squares and circular check rings: `#261F1B` background, `#3D322B` stroke. Checked state transitions cleanly to `#E89A3C` with a rich `#14100E` checkmark glyph.

### Audio Voice Scribe Bar
- Distinctive tactile transcription component: Pill-shaped floating bar in `#322A25` with real-time waveform bars rendered in `#E89A3C` and live transcription snippets in `#F5E6D3`.
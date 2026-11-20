---
name: Warm Everyday Notebook
colors:
  surface: '#fff8f4'
  surface-dim: '#e0d9d4'
  surface-bright: '#fff8f4'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#faf2ed'
  surface-container: '#f4ece7'
  surface-container-high: '#eee7e2'
  surface-container-highest: '#e8e1dc'
  on-surface: '#1e1b18'
  on-surface-variant: '#58423c'
  inverse-surface: '#33302d'
  inverse-on-surface: '#f7efea'
  outline: '#8b716a'
  outline-variant: '#dfc0b7'
  surface-tint: '#a73918'
  primary: '#a43716'
  on-primary: '#ffffff'
  primary-container: '#c54f2c'
  on-primary-container: '#fffbff'
  inverse-primary: '#ffb5a0'
  secondary: '#376847'
  on-secondary: '#ffffff'
  secondary-container: '#b6edc2'
  on-secondary-container: '#3b6d4b'
  tertiary: '#655b45'
  on-tertiary: '#ffffff'
  tertiary-container: '#7e735c'
  on-tertiary-container: '#fffbff'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#ffdbd1'
  primary-fixed-dim: '#ffb5a0'
  on-primary-fixed: '#3b0900'
  on-primary-fixed-variant: '#862201'
  secondary-fixed: '#b9efc5'
  secondary-fixed-dim: '#9dd3aa'
  on-secondary-fixed: '#00210e'
  on-secondary-fixed-variant: '#1e5031'
  tertiary-fixed: '#f0e1c5'
  tertiary-fixed-dim: '#d3c5aa'
  on-tertiary-fixed: '#221b0a'
  on-tertiary-fixed-variant: '#4f4631'
  background: '#fff8f4'
  on-background: '#1e1b18'
  surface-variant: '#e8e1dc'
typography:
  headline-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 32px
    fontWeight: '700'
    lineHeight: 40px
    letterSpacing: -0.02em
  headline-lg-mobile:
    fontFamily: Plus Jakarta Sans
    fontSize: 26px
    fontWeight: '700'
    lineHeight: 34px
    letterSpacing: -0.01em
  headline-md:
    fontFamily: Plus Jakarta Sans
    fontSize: 22px
    fontWeight: '600'
    lineHeight: 30px
    letterSpacing: -0.01em
  headline-sm:
    fontFamily: Plus Jakarta Sans
    fontSize: 18px
    fontWeight: '600'
    lineHeight: 26px
  body-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 18px
    fontWeight: '400'
    lineHeight: 28px
  body-md:
    fontFamily: Plus Jakarta Sans
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
  body-sm:
    fontFamily: Plus Jakarta Sans
    fontSize: 14px
    fontWeight: '400'
    lineHeight: 20px
  label-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 16px
    fontWeight: '600'
    lineHeight: 24px
  label-md:
    fontFamily: Plus Jakarta Sans
    fontSize: 14px
    fontWeight: '600'
    lineHeight: 20px
  label-sm:
    fontFamily: Plus Jakarta Sans
    fontSize: 12px
    fontWeight: '600'
    lineHeight: 16px
    letterSpacing: 0.02em
rounded:
  sm: 0.5rem
  DEFAULT: 1rem
  md: 1.5rem
  lg: 2rem
  xl: 3rem
  full: 9999px
spacing:
  gutter: 1rem
  gutter-tablet: 1.5rem
  gutter-desktop: 2rem
  margin: 1rem
  margin-tablet: 2rem
  margin-desktop: 3rem
  space-xs: 0.25rem
  space-sm: 0.5rem
  space-md: 1rem
  space-lg: 1.5rem
  space-xl: 2rem
---

## Brand & Style

This design system embraces the tactile, reassuring presence of a physical paper journal, translating personal archiving into an intuitive, stress-free routine. Designed for an intergenerational audience—spanning tech-savvy students tracking study slides and expenses, to older parents filing warranties, bills, and physical receipts—the interface prioritizes legibility, generous physical proportions, and an unhurried, grounded feel.

Key attributes:
- **Tone:** Grounded, domestic, thoughtful, and clear.
- **Aesthetic Movement:** Tactile Warm Editorial with soft material cues. It purposefully avoids modern generative AI tropes—eschewing neon gradients, cyber palettes, glassmorphism, floating sparkle effects, and mechanical icons.
- **Interaction Ethos:** High tactile certainty. Every actionable element behaves like pressed stationery: substantial targets, definite borders, clear text indicators alongside iconic metaphors, and forgiving touch perimeters.

## Colors

The palette draws directly from physical ephemera: unbleached paper, baked earth, and botanical ink.

### Palette Architecture
- **Primary Accent (`#D95D39` - Terracotta Coral):** Drives the primary visual hierarchy. Used for high-intent actions, primary interactive triggers, active tab indicators, and critical focus states.
- **Secondary Support (`#4A7C59` - Sage Olive):** Communicates permanence, verified states, offline readiness, and non-destructive confirmations.
- **Tertiary Pastels (Categorical Paper Tags):**
  - Warm Honey: `#F5E6CA` (Bills, Financials, Urgent Slips)
  - Soft Sky: `#E3EDF7` (Study Notes, Slides, Readings)
  - Muted Sage: `#E3ECE4` (Groceries, Home, Health)
  - Soft Blush: `#FBE8E4` (Personal, Memories, Family)
- **Neutral & Canvas Surfaces:**
  - Base Canvas (Oat Milk): `#FAF7F2`
  - Elevated Cards / Page Surfaces: `#FFFFFF`
  - Editorial Border Line (Warm Sand): `#E8E2D9`
  - High-Contrast Copy (Deep Espresso): `#24211E`
  - Secondary Copy (Muted Warm Espresso): `#6B635B`
  - Disabled/Subtle Wash: `#F0EAE1`

## Typography

Type choices prioritize optical clarity across diverse visual acuities. **Plus Jakarta Sans** provides a friendly, humanist geometry with wide open apertures, rounded terminals, and clear character distinction (such as `1`, `l`, and `I`).

- **Sizing Floor:** No interactive or instructional text drops below 14px on mobile devices. 12px is restricted to auxiliary metadata tags and uppercase timestamps.
- **Vertical Rhythm:** Line heights are calibrated conservatively (1.4x–1.55x) to maintain clear word-grouping for readers skimming receipt totals or handwritten notes.
- **Hierarchy:** Weight steps move strictly from Regular (400) for body reading to Semi-Bold (600) and Bold (700) for headers and actions. Thin and extra-light styles are prohibited.

## Layout & Spacing

The layout uses a fluid structural grid that shifts smoothly across viewport thresholds:
- **Mobile (<600px):** 4 columns, 16px (`1rem`) margins and gutters. Single-column card stacks dominate to maintain wide scan lines.
- **Tablet (600px–1023px):** 8 columns, 32px (`2rem`) margins, 24px (`1.5rem`) gutters. Adapts to two-column receipt/image indexing layouts.
- **Desktop / Wide Tablet (1024px+):** 12 columns, maximum content width capped at 1120px to prevent uncomfortably long lines of captured text.

### Touch Target Standard
All clickable, tappable, and toggleable regions must satisfy a strict **minimum touch target of 48×48px**, regardless of visual icon size. Spacing tokens ensure actionable items maintain at least 8px (`space-sm`) of breathing room to avoid accidental taps.

## Elevation & Depth

Visual hierarchy uses physical sheet-like stacking rather than ambient dark glows or floating blurs.

- **Level 0 (Canvas):** The base `#FAF7F2` oat milk canvas. Flat, matte, texture-free background.
- **Level 1 (Card Sheets):** Pure white (`#FFFFFF`) card surfaces. Defined by a structural 1px solid border in `#E8E2D9` accompanied by a soft, warm paper shadow: `box-shadow: 0 2px 8px rgba(36, 33, 30, 0.05)`.
- **Level 2 (Popovers, Drawers, Floating Actions):** Pure white (`#FFFFFF`) elevated panels with an accentuated warm shadow: `box-shadow: 0 8px 24px rgba(36, 33, 30, 0.08)`, paired with the standard 1px `#E8E2D9` boundary line.
- **No Heavy Outlines or Dark Shadows:** Avoid cold pure black (`#000000`) drop shadows and harsh 2px+ borders. Depth must always read as natural ambient room light cast across physical stationery.

## Shapes

The design uses a 16px (`1rem`) radius baseline for structural surfaces and containers. This curvature conveys an approachable, handheld character without collapsing cards into circles.

- **Primary Cards & Modals:** 16px radius (`rounded-lg`).
- **Input Fields & Text Areas:** 16px radius (`rounded-lg`).
- **Interactive Buttons & Pill Badges:** Full pill-shaped radius (`rounded-xl` or `9999px`) to invite natural pressing.
- **Image & Receipt Crops:** 12px radius, preserving receipt geometry while softening sharp paper edges.

## Components

### Buttons
- **Primary Button:** Background `#D95D39`, text `#FFFFFF` (Font weight 600). Minimum height 52px. Pill radius (9999px). Substantial horizontal padding (24px).
- **Secondary Button:** Background `#FFFFFF`, border 1.5px solid `#E8E2D9`, text `#24211E`. Active/hover background `#FAF7F2`.
- **Icon + Label Pairing:** Interactive actions must always feature an explicit text label beside the icon (e.g., a capture camera icon is paired with "Snap Receipt"). Floating icon-only mystery meats are prohibited.

### Input Fields
- Minimum height 52px.
- Background `#FFFFFF`, border 1.5px solid `#E8E2D9`, corner radius 16px.
- In-focus state: Border shifts to `#D95D39` with a subtle outer ring (`box-shadow: 0 0 0 3px rgba(217, 93, 57, 0.15)`).
- Labels sit persistently above the field in `label-md` `#24211E`—never relying purely on disappearing placeholders.

### Category Pills & Status Badges
- **Status Badges (e.g., "Saved", "Indexed", "Offline Ready"):** Background `#E3ECE4`, text `#4A7C59`, leading dot indicator (6px circle). Height 32px, pill-shaped.
- **Folder / Tag Pills:** Solid fills derived from the pastel palette (Honey `#F5E6CA`, Sky `#E3EDF7`, Sage `#E3ECE4`, Blush `#FBE8E4`) paired with high-contrast `#24211E` typography. Padding: 6px 14px.

### Cards & Slips (Receipt/Screenshot Items)
- Background `#FFFFFF`, 1px border `#E8E2D9`, radius 16px, ambient drop shadow (`0 2px 8px rgba(36,33,30,0.05)`).
- Padding: 16px internal padding.
- Structure: Clear vertical stacking with thumbnail preview on the left/top, prominent title in `headline-sm`, date stamp in `body-sm` (`#6B635B`), and prominent category pill.

### Checkboxes & Radio Controls
- Minimum dimensions: 24×24px visual box centered in a 48×48px interactive touch area.
- Checkbox radius: 6px. Radio radius: 50%.
- Unchecked: 2px solid `#E8E2D9` on `#FFFFFF`.
- Checked: `#D95D39` solid fill with clear white tick or solid white dot.

### Specialized Component: Receipt Ledger Card
- A distinct paper-like component displaying extracted items (store name, date, total amount).
- Top edge features a subtle decorative stitched or notched border divider.
- Total currency values must always render in bold 20px `Plus Jakarta Sans` to ensure instant readability without pinch-to-zoom.
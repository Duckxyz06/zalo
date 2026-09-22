#!/usr/bin/env sh
set -eu

ROOT="${1:-app/build/intermediates}"

MANIFESTS=$(find "$ROOT" -type f -name AndroidManifest.xml -path '*playRelease*' 2>/dev/null || true)

if [ -z "$MANIFESTS" ]; then
  echo "ERROR: Could not find merged playRelease manifest."
  exit 1
fi

if printf '%s
' "$MANIFESTS" | xargs grep -H -E   'BIND_ACCESSIBILITY_SERVICE|android\.accessibilityservice\.AccessibilityService|isAccessibilityTool' ; then
  echo "ERROR: Accessibility capability leaked into playRelease."
  exit 1
fi

echo "OK: playRelease manifest contains no AccessibilityService capability."

# Version Management - Configuration Reference

This document provides all configuration options and customization points for the Fawords version management system.

---

## Workflow Configuration

### File Location
`.github/workflows/auto-version-tagging.yml`

### Trigger Configuration

#### 1. Automatic Triggers

```yaml
on:
  push:
    branches:
      - main      # Main production branch
      - master    # Alternative main branch
```

**Customization**:
- Add more branches: `- develop`, `- release/*`
- Exclude branches: Use `!branch` pattern
- Add path filters: `paths: ['pom.xml', 'src/**']`

#### 2. Manual Trigger (Workflow Dispatch)

```yaml
on:
  workflow_dispatch:
    inputs:
      version:
        description: 'Manual version override'
        required: false
        default: ''
      is_hotfix:
        description: 'Mark as hotfix release'
        required: false
        type: boolean
        default: false
```

**Customization**:
- Add more inputs (e.g., `release_notes`, `pre_release`)
- Change input types (`string`, `choice`, `environment`)
- Add default values

### Permissions Configuration

```yaml
permissions:
  contents: write    # Commit changes to repo
  tags: write        # Create git tags
```

**Minimal Setup** (GitHub requires):
- `contents: write` for commits
- `tags: write` for tag creation

**Extended Permissions** (optional):
- `pull-requests: read` - Read PR information
- `releases: write` - Create releases (usually auto with contents:write)

### Environment Variables

Current workflow uses:
```yaml
env:
  LATEST_TAG: ${{ steps.latest_tag.outputs.tag }}
  MANUAL_VERSION: ${{ github.event.inputs.version }}
  IS_HOTFIX: ${{ github.event.inputs.is_hotfix }}
  COMMIT_MSG: ${{ github.event.head_commit.message }}
```

### Customization: Version Increment Logic

**Current Implementation** (in `version-tag` job):
```bash
# Auto-increment patch version
PATCH=$((PATCH + 1))
NEW_VERSION="${MAJOR}.${MINOR}.${PATCH}"
```

**To increment minor on feature branches**:
```bash
if [[ "${{ github.ref }}" == "refs/heads/feature/*" ]]; then
  MINOR=$((MINOR + 1))
  PATCH=0
fi
```

**To increment major on release branches**:
```bash
if [[ "${{ github.ref }}" == "refs/heads/release/v*" ]]; then
  MAJOR=$((MAJOR + 1))
  MINOR=0
  PATCH=0
fi
```

---

## Script Configuration

### File Location
`scripts/version-management.sh`

### Configuration Variables

```bash
# File containing version (pom.xml, package.json, etc.)
VERSION_FILE="pom.xml"

# Prefix for git tags
TAG_PREFIX="v"

# Version pattern for regex matching
VERSION_PATTERN="[0-9]\+\.[0-9]\+\.[0-9]\+"
```

### Customization Examples

#### 1. Use Different Version File

```bash
# For Node.js project
VERSION_FILE="package.json"

# For Gradle project
VERSION_FILE="build.gradle"

# For Python project
VERSION_FILE="setup.py"
```

#### 2. Change Tag Prefix

```bash
# No prefix
TAG_PREFIX=""
# Result: 1.0.0

# Custom prefix
TAG_PREFIX="release-"
# Result: release-1.0.0

# Year-based prefix
TAG_PREFIX="v2026-"
# Result: v2026-1.0.0
```

#### 3. Custom Version Pattern

```bash
# With build metadata (e.g., 1.0.0+build123)
VERSION_PATTERN="[0-9]\+\.[0-9]\+\.[0-9]\+\(\+.*\)\?"

# With pre-release (e.g., 1.0.0-beta.1)
VERSION_PATTERN="[0-9]\+\.[0-9]\+\.[0-9]\+\(-[a-zA-Z0-9]*\)\?"

# Legacy 4-part version (e.g., 1.0.0.0)
VERSION_PATTERN="[0-9]\+\.[0-9]\+\.[0-9]\+\.[0-9]\+"
```

### Customization: Version Increment Strategy

**Current**: Auto-increment patch version

**Alternative 1: Calendar Versioning (CalVer)**
```bash
# Example: 2026.8.1 (Year.Month.Release)
NEW_VERSION="$(date +%Y).$(date +%-m).1"
```

**Alternative 2: Minor version per month**
```bash
MONTH=$(date +%m)
NEW_VERSION="1.${MONTH}.${PATCH}"
```

**Alternative 3: Custom formula**
```bash
# Increment only on specific conditions
if [[ "$COMMIT_MSG" =~ breaking ]]; then
  MAJOR=$((MAJOR + 1))
else
  PATCH=$((PATCH + 1))
fi
```

### Git Configuration

```bash
git config user.name "Release Automation"
git config user.email "release@fawords.local"
```

**Customization**:
- Change committer name/email
- Use environment variables: `${{ env.GITHUB_ACTOR }}`
- Different config per environment

---

## Project File Customization

### pom.xml Configuration

**Version Location** (currently searched):
```xml
<version>1.0.0</version>
```

**Update Pattern** (sed regex):
```bash
sed -i "s/<version>.*<\/version>/<version>$NEW_VERSION<\/version>/" pom.xml
```

**Customization for Multiple Versions**:
```bash
# Update parent version
sed -i "0,/<version>.*<\/version>/s/<version>.*<\/version>/<version>$NEW_VERSION<\/version>/" pom.xml

# Update specific module
sed -i "/<artifactId>module-name<\/artifactId>/,/<version>/s/<version>.*<\/version>/<version>$NEW_VERSION<\/version>/" pom.xml
```

### Alternative Project Files

#### Node.js (package.json)

```bash
# Update version field
jq ".version = \"$NEW_VERSION\"" package.json > package.json.tmp && mv package.json.tmp package.json
```

#### Python (setup.py)

```bash
# Update version
sed -i "s/version=['\"].*['\"]/version='$NEW_VERSION'/" setup.py
```

#### Gradle (build.gradle)

```bash
# Update version
sed -i "s/version = '[^']*'/version = '$NEW_VERSION'/" build.gradle
```

---

## Release Generation Configuration

### Current Release Template

```yaml
Release:
  name: v$VERSION
  body: |
    ## Release v$VERSION
    
    ### Changes
    (auto-generated changelog)
  draft: false
  prerelease: false
```

**Customization: Enhanced Release Notes**

```yaml
body: |
  ## 📦 Release v$VERSION
  
  ### ✨ Features
  (filtered commits)
  
  ### 🐛 Bug Fixes
  (filtered commits)
  
  ### 📝 Documentation
  (filtered commits)
  
  ### 🔥 Breaking Changes
  (if any)
  
  ### Contributors
  (list of contributors)
  
  **Release Date**: $(date)
  **Git Hash**: $GITHUB_SHA
```

### Changelog Generation

**Current Method**: Simple commit log

```bash
COMMIT_LOG=$(git log $LATEST_TAG..HEAD --oneline --pretty=format:"%h - %s")
```

**Alternative: Conventional Commits**

```bash
# Separate commits by type
FEATURES=$(git log $LATEST_TAG..HEAD --grep="^feat" --oneline)
FIXES=$(git log $LATEST_TAG..HEAD --grep="^fix" --oneline)
DOCS=$(git log $LATEST_TAG..HEAD --grep="^docs" --oneline)
```

**Alternative: Commit Range**

```bash
# Only since last major version
COMMIT_LOG=$(git log v${MAJOR}.0.0..HEAD --oneline)

# Only last N days
COMMIT_LOG=$(git log --since="7 days ago" --oneline)
```

---

## Hotfix Configuration

### Hotfix Detection Patterns

**Current Patterns** (in workflow):
```bash
if [[ "$COMMIT_MSG" =~ (hotfix|fix:|HOTFIX) ]]; then
  IS_HOTFIX_FINAL="true"
fi
```

**Customization: Add More Patterns**

```bash
# Additional patterns
if [[ "$COMMIT_MSG" =~ (hotfix|fix:|HOTFIX|urgent|critical|🔥) ]]; then
  IS_HOTFIX_FINAL="true"
fi

# Label-based detection
if [[ "$PR_LABELS" == *"hotfix"* ]]; then
  IS_HOTFIX_FINAL="true"
fi

# Branch-based detection
if [[ "${{ github.ref }}" == "refs/heads/hotfix/"* ]]; then
  IS_HOTFIX_FINAL="true"
fi
```

### Hotfix Release Markers

**Current**: Included in release body

```markdown
### 🔥 HOTFIX Release
```

**Customization: Add Severity Levels**

```bash
if [[ "$COMMIT_MSG" =~ "CRITICAL" ]]; then
  MARKER="🚨 CRITICAL HOTFIX"
elif [[ "$COMMIT_MSG" =~ "hotfix" ]]; then
  MARKER="🔥 Hotfix"
else
  MARKER="📦 Release"
fi
```

---

## Branch Configuration

### Monitored Branches

**Current**:
```yaml
branches:
  - main
  - master
```

**Customization: Multiple Branch Strategies**

```yaml
# Git Flow
branches:
  - main        # Production releases
  - develop     # Development releases
  - release/*   # Release candidates

# Trunk-based development
branches:
  - main        # Single branch

# Custom
branches:
  - main
  - stable
  - production
```

### Branch-Specific Actions

```bash
# Different increment for different branches
case "${{ github.ref }}" in
  "refs/heads/main")
    BUMP_TYPE="patch"  # Production patches
    ;;
  "refs/heads/develop")
    BUMP_TYPE="minor"  # Dev features
    ;;
  "refs/heads/release/"*)
    BUMP_TYPE="major"  # Release versions
    ;;
esac
```

---

## Security Configuration

### Token Scope

**Current** (GitHub-provided):
```yaml
GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
```

**Customization: Custom Token**

```yaml
# In workflow
env:
  GITHUB_TOKEN: ${{ secrets.CUSTOM_RELEASE_TOKEN }}

# In GitHub (Settings → Secrets):
# CUSTOM_RELEASE_TOKEN: (PAT with repo access)
```

### Commit Author

**Current** (GitHub Actions bot):
```bash
git config user.name "github-actions[bot]"
git config user.email "41898282+github-actions[bot]@users.noreply.github.com"
```

**Customization: Custom Author**

```bash
git config user.name "Release Automation"
git config user.email "automation@company.com"

# Or use environment variable
git config user.name "${{ env.RELEASE_AUTHOR }}"
```

---

## Notification Configuration

### Current: Built-in Summary

```yaml
- name: Release Summary
  run: |
    echo "### Release v${{ needs.version-tag.outputs.version }}"
```

### Customization: External Notifications

#### Slack Notification

```yaml
- name: Notify Slack
  uses: slackapi/slack-github-action@v1
  with:
    webhook-url: ${{ secrets.SLACK_WEBHOOK }}
    payload: |
      {
        "text": "Release v${{ needs.version-tag.outputs.version }} created"
      }
```

#### Discord Notification

```yaml
- name: Notify Discord
  uses: sarisia/actions-status-discord@v1
  with:
    webhook_url: ${{ secrets.DISCORD_WEBHOOK }}
    title: "Release v${{ needs.version-tag.outputs.version }}"
```

#### Email Notification

```yaml
- name: Notify Email
  uses: dawidd6/action-send-mail@v3
  with:
    server_address: ${{ secrets.MAIL_SERVER }}
    server_port: 587
    username: ${{ secrets.MAIL_USER }}
    password: ${{ secrets.MAIL_PASSWORD }}
    subject: "Release v${{ needs.version-tag.outputs.version }}"
    body: "New release available"
```

---

## Performance Tuning

### Checkout Optimization

```yaml
- uses: actions/checkout@v4
  with:
    fetch-depth: 0        # Full history (slower but needed)
    # OR
    fetch-depth: 50       # Partial history (faster)
```

### Cache Configuration

```yaml
- uses: actions/cache@v3
  with:
    path: ~/.m2/repository
    key: maven-${{ hashFiles('**/pom.xml') }}
    restore-keys: maven-
```

### Parallel Jobs

```yaml
jobs:
  version-tag:
    # Can run immediately
    
  update-version-file:
    needs: version-tag
    # Waits for version-tag
    
  notify:
    needs: [version-tag, update-version-file]
    # Waits for both
```

---

## Logging Configuration

### Workflow Debug Logging

```yaml
env:
  ACTIONS_STEP_DEBUG: true  # Enable step-level debugging
```

### Script Debug Mode

```bash
# In script
bash -x scripts/version-management.sh current

# Or add to script
set -x  # Enable debug output
```

---

## Error Handling Configuration

### Current Error Strategy

```yaml
# Fail on error
set -e

# Validate before proceeding
validate_version() { ... }
```

### Customization: Retry Logic

```bash
# Retry git operations
retry=0
max_retries=3
while [ $retry -lt $max_retries ]; do
  git push origin "v$NEW_VERSION" && break
  retry=$((retry + 1))
  sleep 5
done
```

### Custom Error Messages

```bash
# Current
echo "Failed to create git tag"

# Enhanced
echo "❌ ERROR: Failed to create git tag v$NEW_VERSION"
echo "   Reason: $(git tag 2>&1 | tail -1)"
echo "   Suggestion: Check git permissions"
```

---

## Testing Configuration

### Local Testing

```bash
# Test with --dry-run
./scripts/version-management.sh increment minor --dry-run

# Test without committing
./scripts/version-management.sh increment minor --no-commit

# Test without tagging
./scripts/version-management.sh increment minor --no-tag
```

### Workflow Testing

```yaml
# Test workflow file syntax
- uses: actions/workflow-runs-cleaner@v2

# Validate job configuration
- run: |
    echo "Jobs: $(yq '.jobs | keys' .github/workflows/auto-version-tagging.yml)"
```

---

## Maintenance Configuration

### Retention Policies

```bash
# Keep only last 10 tags
git tag -l | head -n -10 | xargs -d '\n' git tag -d

# Archive old releases
# Manual: GitHub Release archives (Settings → Auto-delete old releases)
```

### Backup Configuration

```bash
# Backup tags locally
git fetch origin 'refs/tags/*:refs/tags/*'

# Backup to separate branch
git push origin --tags
```

---

## Example: Complete Custom Configuration

### Example 1: CalVer + Slack Notifications

```bash
# In scripts/version-management.sh
VERSION_PATTERN="[0-9]{4}\.[0-9]\+\.[0-9]\+"
NEW_VERSION="$(date +%Y).$(date +%-m).1"

# In .github/workflows/auto-version-tagging.yml
- name: Notify Slack
  run: |
    curl -X POST ${{ secrets.SLACK_WEBHOOK }} \
      -d '{"text":"Release '$(echo $NEW_VERSION)' published"}'
```

### Example 2: Multi-file Version Update

```bash
# Update multiple files
./scripts/version-management.sh set 1.2.0
sed -i "s/VERSION=\"[^\"]*\"/VERSION=\"1.2.0\"/" Dockerfile
sed -i "s/version=[0-9.]\+/version=1.2.0/" build.gradle
```

### Example 3: Feature Branch Versioning

```bash
if [[ "${{ github.ref }}" == "refs/heads/feature/"* ]]; then
  NEW_VERSION="${MAJOR}.${MINOR}-$(git rev-parse --short HEAD)"
else
  NEW_VERSION="${MAJOR}.${MINOR}.${PATCH}"
fi
```

---

## Troubleshooting Configuration Issues

### Debug: Print Configuration

```bash
# Show current configuration
echo "VERSION_FILE: $VERSION_FILE"
echo "TAG_PREFIX: $TAG_PREFIX"
echo "VERSION_PATTERN: $VERSION_PATTERN"
```

### Debug: Test Regex

```bash
# Test version pattern matching
VERSION="1.0.0"
if [[ $VERSION =~ $VERSION_PATTERN ]]; then
  echo "✓ Pattern matches"
else
  echo "✗ Pattern doesn't match"
fi
```

### Debug: Validate Files

```bash
# Validate pom.xml
xmllint pom.xml > /dev/null && echo "✓ Valid XML"

# Validate workflow YAML
yamllint .github/workflows/auto-version-tagging.yml
```

---

**Configuration Reference v1.0**  
**Last Updated**: 2026-08-29

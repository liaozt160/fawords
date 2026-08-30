# Version Tagging & Release Management

This document describes the automatic and manual version tagging system for the Fawords project.

## Overview

The Fawords project uses **Semantic Versioning** (SemVer) with automated version tagging on pull request merges and manual override capabilities. The system automatically creates version tags, releases, and updates the project version in `pom.xml`.

### Version Format

```
MAJOR.MINOR.PATCH[-hotfix]

Example: 1.0.0, 1.0.1, 1.1.0, 2.0.0, 1.0.5-hotfix
```

- **MAJOR**: Significant changes, breaking compatibility (not auto-incremented)
- **MINOR**: New features, backward compatible (manual increment)
- **PATCH**: Bug fixes, hotfixes (auto-incremented)
- **-hotfix**: Optional suffix for critical hotfix releases

**Initial Version**: `1.0.0`

---

## Automatic Version Tagging

### Trigger: Pull Request Merge

When a pull request is merged into `main` or `master` branch:

1. ✅ Automatic version tag is created
2. ✅ Patch version is incremented (e.g., `1.0.0` → `1.0.1`)
3. ✅ Release is created with changelog
4. ✅ `pom.xml` is updated with new version
5. ✅ Changes are committed to main branch

### Workflow File

**Location**: `.github/workflows/auto-version-tagging.yml`

**Triggers**:
- `push` to `main` or `master` branch
- `workflow_dispatch` for manual control

### Automatic Hotfix Detection

The workflow automatically detects hotfix commits if the commit message contains:
- `hotfix`
- `fix:`
- `HOTFIX`

Hotfix releases are tagged with additional metadata in the release body.

---

## Manual Version Control

### Method 1: Workflow Dispatch (GitHub UI)

Manually trigger the workflow with custom version:

1. Go to **GitHub Repository** → **Actions**
2. Select **📦 Auto Version Tagging** workflow
3. Click **Run workflow**
4. Provide:
   - `version` (optional): e.g., `1.2.0` or `1.0.5`
   - `is_hotfix`: Check if this is a hotfix release
5. Click **Run workflow**

### Method 2: Command Line Script

Use the `version-management.sh` script for local version management:

```bash
# Show current version
./scripts/version-management.sh current

# Show next auto-incremented version
./scripts/version-management.sh next

# Increment minor version (new features)
./scripts/version-management.sh increment minor

# Increment major version (breaking changes)
./scripts/version-management.sh increment major

# Set specific version
./scripts/version-management.sh set 1.2.0

# Create hotfix version
./scripts/version-management.sh hotfix

# List all version tags
./scripts/version-management.sh list

# Dry run (preview changes)
./scripts/version-management.sh increment patch --dry-run

# Increment without committing
./scripts/version-management.sh set 1.5.0 --no-commit
```

### Script Options

- `--dry-run`: Show what would be done without making changes
- `--no-commit`: Don't commit version changes to git
- `--no-tag`: Don't create git tag

---

## Versioning Scenarios

### Scenario 1: Regular Bug Fix (Auto)

**Trigger**: PR merged to main
**Action**: Patch version auto-incremented
**Result**: `1.0.0` → `1.0.1`

```
Commit Message: Fix: authentication issue
→ Workflow runs automatically
→ Tag: v1.0.1 created
→ Release: v1.0.1 published
```

### Scenario 2: New Feature (Manual)

**Trigger**: Feature PR merged to main
**Action**: Manual workflow dispatch with `increment minor`
**Result**: `1.0.1` → `1.1.0`

```
Steps:
1. Merge feature PR to main
2. Go to Actions → Run workflow
3. Leave version field empty (or specify 1.1.0)
4. Click Run
→ Tag: v1.1.0 created
→ Release: v1.1.0 published
```

### Scenario 3: Critical Hotfix

**Trigger**: Hotfix PR merged to main
**Action**: Workflow auto-detects or manual dispatch with `is_hotfix` checked
**Result**: `1.0.1` → `1.0.2` (marked as hotfix)

```
Commit Message: hotfix: critical security patch
→ Workflow auto-detects hotfix
→ Tag: v1.0.2 created with 🔥 marker
→ Release: v1.0.2 published (marked as hotfix)
```

### Scenario 4: Major Version Release

**Trigger**: Manual workflow dispatch
**Action**: Set specific version `2.0.0`
**Result**: `1.X.X` → `2.0.0`

```
Steps:
1. Go to Actions → Run workflow
2. Set version: 2.0.0
3. Click Run
→ Tag: v2.0.0 created
→ Release: v2.0.0 published
```

---

## Workflow Details

### Job: `version-tag`

**Responsibilities**:
1. Fetch latest version tag from git
2. Determine next version (auto or manual)
3. Detect hotfix status
4. Generate changelog from commits
5. Create annotated git tag
6. Create GitHub Release with changelog

**Outputs**:
- `version`: Next version number
- `is_hotfix`: Hotfix status boolean
- `changelog`: Generated changelog

### Job: `update-version-file`

**Responsibilities**:
1. Update version in `pom.xml`
2. Commit changes with message `chore(release): vX.X.X`
3. Push to main branch

**Runs after**: `version-tag` job

### Job: `notify`

**Responsibilities**:
1. Summarize release information
2. Display changelog
3. Provide release link

---

## Release Notes & Changelog

### Automatic Changelog Generation

The workflow automatically generates changelog from commits since last tag:

```
## Release v1.0.1

### 📋 Changes
```
abc1234 - Fix: authentication issue (dev@example.com)
def5678 - Fix: null pointer exception (dev@example.com)
```
```

### Hotfix Release Marker

For hotfix releases, the changelog includes:

```
## Release v1.0.2

### 🔥 HOTFIX Release

### 📋 Changes
...
```

---

## Configuration

### Default Version Format

Currently configured for format: `X.X.X` (Semantic Versioning)

To customize:
- Edit `.github/workflows/auto-version-tagging.yml`
- Modify `VERSION_PATTERN` in `scripts/version-management.sh`
- Update version format logic in workflow

### Git Tag Prefix

- **Current**: `v` (e.g., `v1.0.0`)
- **Customizable**: Modify `TAG_PREFIX` in workflow or script

### Release Behavior

- **Draft**: `false` (releases published immediately)
- **Prerelease**: `false` for normal releases
- **Auto-publish**: Yes, every merge to main creates a release

---

## Best Practices

### Commit Messages

Use conventional commit format for clarity:

```
feat: add new authentication method
feat(api): add /auth/refresh endpoint
fix: resolve login timeout issue
fix(hotfix): critical security vulnerability
chore(release): v1.0.1
docs: update README
test: add authentication tests
```

### Branch Strategy

Recommended Git Flow:

```
main (stable releases)
  ↑
  └─ develop (integration branch)
      ↑
      └─ feature/*, hotfix/*, release/* (work branches)
```

### Hotfix Workflow

For critical production issues:

```
1. Create hotfix branch: git checkout -b hotfix/issue-description
2. Fix the issue
3. Create PR to main (mark with 🔥 emoji)
4. Merge PR
5. Workflow auto-detects hotfix and tags with hotfix marker
6. Merge back to develop when convenient
```

### Manual Version Increments

**When to use**:
- Major version bumps (breaking changes)
- Feature releases (new functionality)
- Scheduled releases
- Special versioning needs

**How**:
- Use workflow dispatch in GitHub UI
- Specify version in `version` field
- Workflow updates all files and creates tag

---

## Troubleshooting

### Issue: Workflow doesn't trigger on PR merge

**Cause**: Workflow file not in main branch yet

**Solution**:
1. Commit workflow file to main branch
2. Push to origin
3. Merge next PR

### Issue: Manual version dispatch doesn't work

**Cause**: Secrets or permissions not configured

**Solution**:
1. Check repo permissions: Settings → Actions → General
2. Ensure `contents: write` and `tags: write` permissions
3. Verify GitHub token has correct scopes

### Issue: pom.xml version not updating

**Cause**: File lock or regex pattern mismatch

**Solution**:
1. Check pom.xml is not locked
2. Verify version format matches: `<version>X.X.X</version>`
3. Run script locally with `--dry-run` to debug

### Issue: Git tag already exists

**Cause**: Manually creating tag with same version number

**Solution**:
1. Delete local tag: `git tag -d vX.X.X`
2. Delete remote tag: `git push --delete origin vX.X.X`
3. Run workflow again with incremented version

---

## Accessing Releases

### GitHub Releases

View all releases:
- **URL**: `https://github.com/USERNAME/fawords/releases`
- **Download**: Download source code, JAR, or assets

### Version History

View version tags:
- **Command**: `git tag -l --sort=-version:refname`
- **GitHub UI**: Repository → Tags

### Package Versions

Current version in `pom.xml`:
- **File**: `pom.xml`
- **Section**: `<version>...</version>`

---

## Integration with CI/CD

### Using Released Versions in Workflows

```yaml
- name: Deploy specific version
  run: |
    VERSION=$(git describe --tags --abbrev=0)
    echo "Deploying: $VERSION"
    # Deploy logic here
```

### Accessing Latest Release

```bash
# Get latest release version
LATEST=$(git describe --tags --abbrev=0)

# Get release assets
curl -s https://api.github.com/repos/USERNAME/fawords/releases/latest | jq -r '.tag_name'
```

---

## FAQ

**Q: Why does version start at 1.0.0 and not 0.1.0?**
A: Follows semantic versioning convention where 1.0.0 indicates first stable release.

**Q: Can I skip creating a tag for a merge?**
A: Yes, disable workflow in repository settings or use `--no-tag` with script.

**Q: How do I downgrade the version?**
A: Manually use workflow dispatch or script with specific version number.

**Q: What if I need to release multiple versions per day?**
A: Workflow supports unlimited releases. Each merge increments version.

**Q: Can I customize the release notes template?**
A: Yes, edit changelog generation in `version-tag` job in workflow file.

**Q: How do I mark a release as pre-release?**
A: Modify `prerelease: false` to `prerelease: true` in workflow file.

---

## Support

For issues or questions:
1. Check Troubleshooting section above
2. Review workflow logs in GitHub Actions
3. Verify git configuration locally
4. Test script with `--dry-run` flag

---

**Last Updated**: 2026-08-29  
**Workflow Version**: 1.0.0  
**Status**: Active and Maintained

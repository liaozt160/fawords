# Version Management Quick Reference

## 📋 Common Tasks

### View Current Version
```bash
./scripts/version-management.sh current
# Output: Current Version: 1.0.0
```

### View Next Version
```bash
./scripts/version-management.sh next
# Output: Next Version (patch): 1.0.1
```

### List All Versions
```bash
./scripts/version-management.sh list
# Output: v1.0.1, v1.0.0
```

---

## 🚀 Release Workflows

### Automatic Release (Default)

| Step | Action | Result |
|------|--------|--------|
| 1 | Merge PR to `main` | Workflow triggers |
| 2 | Auto-detect version | Patch increment |
| 3 | Create tag & release | GitHub Release created |
| ✅ | Complete | Version in pom.xml updated |

**Time**: < 2 minutes

---

### Manual Release (Via GitHub Actions UI)

1. Go to **Actions** tab
2. Select **📦 Auto Version Tagging**
3. Click **Run workflow**
4. Fill in (optional):
   - `version`: e.g., `1.1.0` or `1.0.5`
   - `is_hotfix`: Check if hotfix
5. Click **Run workflow**

**Time**: < 3 minutes

---

### Manual Release (Via Script)

```bash
# Set specific version
./scripts/version-management.sh set 1.2.0

# Increment minor (new features)
./scripts/version-management.sh increment minor

# Increment major (breaking changes)
./scripts/version-management.sh increment major

# Create hotfix
./scripts/version-management.sh hotfix
```

**Time**: < 5 minutes (includes git ops)

---

## 🏷️ Version Types

### Patch (Bug Fixes) - AUTOMATIC

```
1.0.0 → 1.0.1
```
- Triggered automatically on PR merge
- Use for: Bug fixes, patches, hotfixes
- Commit message: `fix:`, `hotfix:`

### Minor (New Features) - MANUAL

```
1.0.1 → 1.1.0
```
- Trigger: Workflow dispatch
- Use for: New features, enhancements
- Command: `./scripts/version-management.sh increment minor`

### Major (Breaking Changes) - MANUAL

```
1.1.0 → 2.0.0
```
- Trigger: Workflow dispatch
- Use for: Breaking changes, major releases
- Command: `./scripts/version-management.sh set 2.0.0`

### Hotfix (Critical Fixes) - AUTO/MANUAL

```
1.0.0 → 1.0.1 (marked as hotfix)
```
- Auto-detect: Commit message contains `hotfix`, `fix:`, `HOTFIX`
- Manual: Check `is_hotfix` in workflow dispatch
- Command: `./scripts/version-management.sh hotfix`

---

## 🔥 Hotfix Fast Track

### Production Hotfix (Critical)

```bash
# 1. Create hotfix branch
git checkout -b hotfix/critical-issue

# 2. Fix the issue
# ... make changes ...

# 3. Commit with hotfix marker
git commit -m "hotfix: critical security vulnerability"

# 4. Create PR to main
git push origin hotfix/critical-issue

# 5. Merge PR to main
# Workflow automatically:
# - Detects hotfix
# - Creates tag v1.0.2 (marked as hotfix)
# - Publishes release with 🔥 marker
```

**Total Time**: ~10 minutes

---

## 📊 Common Commands Reference

| Command | Purpose | Example |
|---------|---------|---------|
| `current` | Show current version | `./scripts/version-management.sh current` |
| `latest` | Show latest git tag | `./scripts/version-management.sh latest` |
| `next` | Show next patch version | `./scripts/version-management.sh next` |
| `increment` | Increment version | `./scripts/version-management.sh increment minor` |
| `set` | Set specific version | `./scripts/version-management.sh set 1.5.0` |
| `hotfix` | Create hotfix version | `./scripts/version-management.sh hotfix` |
| `list` | List all version tags | `./scripts/version-management.sh list` |

---

## 🛠️ Script Options

### Dry Run (Preview)
```bash
./scripts/version-management.sh increment minor --dry-run
# Shows what would happen without making changes
```

### No Commit
```bash
./scripts/version-management.sh set 1.5.0 --no-commit
# Updates files but doesn't commit to git
```

### No Tag
```bash
./scripts/version-management.sh increment patch --no-tag
# Increments version but doesn't create git tag
```

### Combine Options
```bash
./scripts/version-management.sh increment major --dry-run --no-commit
```

---

## 📈 Version History Examples

### Development with Regular Patches

```
v1.0.0 (Initial Release)
  ↓ (bug fix PR merged)
v1.0.1 (Auto-incremented)
  ↓ (bug fix PR merged)
v1.0.2 (Auto-incremented)
  ↓ (feature PR merged + manual increment)
v1.1.0 (Minor bump)
```

### Hotfix Scenario

```
v1.1.0 (Stable Release)
  ↓ (critical hotfix PR merged)
v1.1.1 (🔥 Hotfix Release)
  ↓ (development continues)
v1.1.2 (Bug fix)
  ↓ (feature PR merged)
v1.2.0 (Minor bump)
```

### Major Version Release

```
v1.9.5 (Stable)
  ↓ (breaking changes PR merged + manual set 2.0.0)
v2.0.0 (Major Release)
  ↓ (bug fix)
v2.0.1 (Auto-incremented)
```

---

## ⚙️ Configuration

### Where is the version stored?

1. **pom.xml**: `<version>1.0.0</version>`
   - Updated automatically on release
   - Source of truth for current version

2. **Git Tags**: `v1.0.0`, `v1.0.1`, etc.
   - Created automatically on release
   - Permanent version history

3. **GitHub Releases**: Published releases with changelog
   - Auto-generated from commit messages
   - Accessible via GitHub UI and API

### Customizing Version Format

To change version format (default: `MAJOR.MINOR.PATCH`):

1. Edit `.github/workflows/auto-version-tagging.yml`
2. Modify version parsing logic in steps
3. Edit `scripts/version-management.sh` `VERSION_PATTERN`

### Customizing Release Notes

Edit `.github/workflows/auto-version-tagging.yml`:
- `generate-changelog` step: Modify changelog template
- Release body format: Change release notes structure

---

## 🚨 Troubleshooting

### "No tags found"
- This is normal for new projects
- First merge will create v1.0.0

### Version doesn't update in pom.xml
```bash
# Check if pom.xml has correct format
grep "<version>" pom.xml

# Should show: <version>1.0.0</version>
```

### Can't create tag locally
```bash
# Check git config
git config user.name
git config user.email

# Set if missing
git config user.name "Your Name"
git config user.email "you@example.com"
```

### Script permission denied
```bash
# Make script executable
chmod +x scripts/version-management.sh
```

---

## 🔗 Related Resources

- [Full Documentation](VERSION_MANAGEMENT.md)
- [Semantic Versioning Spec](https://semver.org)
- [Conventional Commits](https://www.conventionalcommits.org)
- [GitHub Releases](https://docs.github.com/en/repositories/releasing-projects-on-github/about-releases)

---

**Last Updated**: 2026-08-29  
**Quick Reference v1.0**

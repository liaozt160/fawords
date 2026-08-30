# Version Management System - Architecture

## Overview

The Fawords version management system is a comprehensive, multi-layer approach to automatic and manual version tagging, release management, and version tracking.

---

## Components

### 1. GitHub Actions Workflow
**File**: `.github/workflows/auto-version-tagging.yml`

**Purpose**: Automated version tagging, release creation, and pom.xml updates

**Trigger Events**:
- `push` to `main` or `master` branch
- `workflow_dispatch` for manual control

**Architecture**:

```
┌─────────────────────────────────────────────────────────────┐
│           GitHub Actions Workflow                           │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  Job 1: version-tag                                         │
│  ├─ Checkout repository (full history)                      │
│  ├─ Get latest semantic version tag                         │
│  ├─ Determine next version (auto or manual)                 │
│  ├─ Detect hotfix status                                    │
│  ├─ Generate changelog from commits                         │
│  ├─ Create annotated git tag                                │
│  └─ Create GitHub Release                                   │
│                                                              │
│  Job 2: update-version-file (depends on Job 1)              │
│  ├─ Update version in pom.xml                               │
│  ├─ Commit changes                                          │
│  └─ Push to main branch                                     │
│                                                              │
│  Job 3: notify (depends on Jobs 1 & 2)                      │
│  └─ Display release summary                                 │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

**Key Functions**:
1. Determine version number
2. Handle manual overrides
3. Detect hotfix commits
4. Generate changelog
5. Create git tags
6. Create GitHub Releases
7. Update project files

---

### 2. Version Management Script
**File**: `scripts/version-management.sh`

**Purpose**: Local version management and scripting support

**Architecture**:

```
┌─────────────────────────────────────────────────────────────┐
│    Version Management Script (Bash)                         │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  Command Handler                                            │
│  ├─ current:   Get current version from pom.xml             │
│  ├─ latest:    Get latest git tag                           │
│  ├─ next:      Show next auto-incremented version           │
│  ├─ increment: Increment major/minor/patch                  │
│  ├─ set:       Set specific version                         │
│  ├─ hotfix:    Create hotfix version                        │
│  └─ list:      List all version tags                        │
│                                                              │
│  Core Functions                                             │
│  ├─ get_current_version()                                   │
│  ├─ get_latest_tag()                                        │
│  ├─ parse_version()                                         │
│  ├─ increment_version()                                     │
│  ├─ validate_version()                                      │
│  ├─ update_pom_version()                                    │
│  ├─ create_git_tag()                                        │
│  └─ commit_version_change()                                 │
│                                                              │
│  Options                                                    │
│  ├─ --dry-run:    Preview without changes                   │
│  ├─ --no-commit:  Skip git commit                           │
│  └─ --no-tag:     Skip tag creation                         │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

**Usage Patterns**:
- Local version management
- CI/CD integration
- Scripting and automation
- Version validation

---

### 3. Documentation
**Files**: 
- `docs/VERSION_MANAGEMENT.md` - Full guide
- `docs/VERSION_QUICK_REFERENCE.md` - Quick reference
- `README.md` - Overview

**Purpose**: User guidance and reference

**Structure**:
```
docs/
├─ VERSION_MANAGEMENT.md (Complete guide)
│  ├─ Overview
│  ├─ Automatic tagging
│  ├─ Manual control
│  ├─ Scenarios
│  ├─ Configuration
│  ├─ Best practices
│  ├─ Troubleshooting
│  └─ FAQ
│
└─ VERSION_QUICK_REFERENCE.md (Fast lookup)
   ├─ Common tasks
   ├─ Release workflows
   ├─ Version types
   ├─ Command reference
   ├─ Examples
   └─ Troubleshooting
```

---

### 4. Validation Script
**File**: `scripts/validate-version-system.sh`

**Purpose**: System health check and setup verification

**Checks**:
1. Workflow file exists
2. Script file exists and is executable
3. Git configuration complete
4. pom.xml format valid
5. Documentation files present
6. Script functionality
7. Git tag availability
8. README documentation

---

## Data Flow

### Automatic Release Flow

```
┌────────────────────────────────────────────────────────────┐
│ Developer creates and merges PR to main                    │
└─────────────────┬──────────────────────────────────────────┘
                  │
                  ▼
        ┌─────────────────────┐
        │ Git Push to main    │
        └─────────┬───────────┘
                  │
                  ▼
    ┌─────────────────────────────────────┐
    │ Workflow Triggered (auto-version)   │
    └─────────┬───────────────────────────┘
              │
              ▼
    ┌──────────────────────────────────────┐
    │ Job: version-tag                     │
    ├──────────────────────────────────────┤
    │ 1. Fetch latest tag (e.g., v1.0.0)   │
    │ 2. Parse version: 1.0.0              │
    │ 3. Increment patch: 1.0.1            │
    │ 4. Get commits since v1.0.0          │
    │ 5. Build changelog                   │
    │ 6. Create git tag v1.0.1             │
    │ 7. Create GitHub Release             │
    └─────────┬──────────────────────────┘
              │
              ▼
    ┌──────────────────────────────────────┐
    │ Job: update-version-file             │
    ├──────────────────────────────────────┤
    │ 1. Update pom.xml:                   │
    │    <version>1.0.1</version>          │
    │ 2. Commit: "chore(release): v1.0.1"  │
    │ 3. Push to main                      │
    └─────────┬──────────────────────────┘
              │
              ▼
    ┌──────────────────────────────────────┐
    │ Job: notify                          │
    ├──────────────────────────────────────┤
    │ Display release summary              │
    └──────────────────────────────────────┘
              │
              ▼
    ┌──────────────────────────────────────┐
    │ Release Complete                     │
    ├──────────────────────────────────────┤
    │ • Tag: v1.0.1                        │
    │ • Release: Published on GitHub       │
    │ • pom.xml: Updated                   │
    │ • Changelog: Auto-generated          │
    └──────────────────────────────────────┘
```

### Manual Release Flow

```
┌────────────────────────────────────────────────────────────┐
│ Developer triggers workflow manually                       │
│ (GitHub Actions UI or script)                             │
└─────────────────┬──────────────────────────────────────────┘
                  │
                  ▼
    ┌──────────────────────────────────────┐
    │ Provide version (optional)           │
    │ Example: 1.1.0                       │
    └─────────┬──────────────────────────┘
              │
              ▼
    ┌──────────────────────────────────────┐
    │ Workflow Triggered (manual-version)  │
    └─────────┬───────────────────────────┘
              │
              ▼
    ┌──────────────────────────────────────┐
    │ Job: version-tag                     │
    ├──────────────────────────────────────┤
    │ 1. Check manual version input        │
    │ 2. Use provided version: 1.1.0       │
    │ 3. (Skip auto-increment)             │
    │ 4. Build changelog                   │
    │ 5. Create git tag v1.1.0             │
    │ 6. Create GitHub Release             │
    └─────────┬──────────────────────────┘
              │
              ▼
    ┌──────────────────────────────────────┐
    │ Job: update-version-file             │
    ├──────────────────────────────────────┤
    │ 1. Update pom.xml: 1.1.0             │
    │ 2. Commit and push                   │
    └─────────┬──────────────────────────┘
              │
              ▼
    ┌──────────────────────────────────────┐
    │ Release Complete                     │
    └──────────────────────────────────────┘
```

---

## Version Storage & Tracking

### Primary Version Sources

```
┌─────────────────────────────────────┐
│ Version Information                 │
├─────────────────────────────────────┤
│                                     │
│ 1. pom.xml (Current)                │
│    └─ <version>1.0.1</version>      │
│                                     │
│ 2. Git Tags (History)               │
│    ├─ v1.0.0                        │
│    ├─ v1.0.1                        │
│    └─ v1.1.0                        │
│                                     │
│ 3. GitHub Releases (Publishing)     │
│    └─ Public release artifacts      │
│                                     │
│ 4. Build Artifacts                  │
│    └─ fawords-1.0.1.jar             │
│                                     │
└─────────────────────────────────────┘
```

### Version File Format

**pom.xml**:
```xml
<project>
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.fawords</groupId>
    <artifactId>fawords</artifactId>
    <version>1.0.1</version>  <!-- Single source of truth -->
    ...
</project>
```

---

## Semantic Versioning Implementation

### Version Components

```
MAJOR.MINOR.PATCH

Example: 1.2.3
         │ │ │
         │ │ └─ PATCH: Bug fixes, incremented automatically
         │ └─── MINOR: New features, incremented manually
         └───── MAJOR: Breaking changes, incremented manually
```

### Increment Rules

| Scenario | Current | Action | Result |
|----------|---------|--------|--------|
| Bug fix PR merged | 1.0.0 | Auto-increment patch | 1.0.1 |
| Feature added | 1.0.1 | Manual: `increment minor` | 1.1.0 |
| Breaking change | 1.1.0 | Manual: `increment major` | 2.0.0 |
| Hotfix commit | 1.2.3 | Auto-detect & patch | 1.2.4 (marked) |

### Hotfix Detection

```
Commit Message Analysis:
├─ Contains "hotfix"?    → Hotfix Release
├─ Contains "fix:"?      → Hotfix Release
├─ Contains "HOTFIX"?    → Hotfix Release
└─ Other patterns?       → Normal Release

Workflow Input:
├─ is_hotfix: true       → Hotfix Release
└─ is_hotfix: false      → Normal Release (default)
```

---

## Integration Points

### Git Workflow

```
main/master branch
    ▼
[Push event]
    ▼
GitHub Actions (auto-version-tagging.yml)
    ▼
Create Tag (v1.0.1)
    ▼
Create Release
    ▼
Update pom.xml
    ▼
Commit back to main
```

### CI/CD Integration

```
GitHub Actions
    ├─ Run tests
    ├─ Build package
    ├─ Version tagging
    └─ Create release

Other CI/CD Systems:
    ├─ Jenkins: Trigger on release webhook
    ├─ GitLab CI: Import workflow as pipeline
    └─ Travis CI: Custom integration
```

### Build System (Maven)

```
pom.xml
    ├─ <version>: Auto-updated on release
    ├─ <scm>: Git repository reference
    └─ <maven.compiler>: Java 21 target

Maven Lifecycle:
    clean → compile → test → package → install → deploy
```

---

## Error Handling & Edge Cases

### Scenario: Multiple Merges in Short Time

```
Time T0:  PR1 merge → workflow triggers
Time T1:  PR2 merge → workflow triggers (concurrent?)

Solution:
- Workflow has built-in queue
- T0 completes: creates v1.0.1
- T1 reads latest tag: v1.0.1
- T1 increments: v1.0.2
- No conflicts, sequential execution
```

### Scenario: Manual Override Collision

```
Condition: Manual version already exists as tag

Solution:
1. git tag -d v1.5.0      (delete local)
2. git push --delete origin v1.5.0  (delete remote)
3. Run workflow again with new version
```

### Scenario: pom.xml Parse Failure

```
Root Cause: Invalid version format in pom.xml

Solutions:
1. Fix pom.xml manually: <version>1.0.0</version>
2. Run script with --dry-run to validate
3. Check XML syntax
```

---

## Security Considerations

### Git Access

```
Permissions Required:
- contents: write       (commit changes)
- tags: write          (create tags)
- pull-requests: read  (read PR info)
```

### Token Management

```
GITHUB_TOKEN:
- Auto-provided by GitHub Actions
- Scoped to current repository
- Expires after workflow completes
- Not exposed in logs
```

### Branch Protection

```
Recommended:
- Require PR reviews before merge
- Require status checks (tests pass)
- Restrict force pushes
- Require signed commits (optional)
```

---

## Performance Characteristics

### Workflow Execution Time

| Component | Time |
|-----------|------|
| Checkout | ~30s |
| Get tags | ~5s |
| Determine version | ~2s |
| Generate changelog | ~10s |
| Create tag & release | ~20s |
| Update pom.xml | ~15s |
| Commit & push | ~10s |
| **Total** | **~90s** |

### Storage

```
Git Tags:        O(1) per tag (~100 bytes)
GitHub Releases: O(1) per release (~1-2 KB)
CI/CD Logs:      Retention per GitHub policy
```

---

## Maintenance & Monitoring

### Health Checks

Run validation script:
```bash
./scripts/validate-version-system.sh
```

Validates:
- All files in place
- Git configuration
- Script permissions
- pom.xml format
- Documentation presence
- Tag availability

### Common Issues

| Issue | Cause | Solution |
|-------|-------|----------|
| Workflow not triggering | Workflow file not in main | Push workflow to main branch |
| Script permission denied | File not executable | `chmod +x scripts/version-management.sh` |
| Git tag creation fails | Git config missing | Configure `user.name` and `user.email` |
| pom.xml not updating | Regex mismatch | Verify version tag format in XML |

---

## Future Enhancements

Potential improvements:

1. **Automated Changelog**: Use conventional commits for detailed changelog
2. **Release Templates**: Customizable release notes format
3. **Multi-language Support**: Version management scripts in Python/Go
4. **Integration Webhooks**: Notify Slack/Discord on release
5. **Version Constraints**: Enforce semantic versioning rules
6. **Rollback Support**: Easily rollback to previous version
7. **Database Migrations**: Track schema versions alongside code
8. **Docker/Container Support**: Auto-tag container images

---

## Conclusion

The Fawords version management system provides:

✅ **Automatic versioning** - Hands-free version increments  
✅ **Manual control** - Override when needed  
✅ **Hotfix support** - Fast-track critical releases  
✅ **Full traceability** - Git tags, releases, changelog  
✅ **Documentation** - Comprehensive guides and references  
✅ **Validation tools** - System health checks  

This architecture is scalable, maintainable, and follows industry best practices for semantic versioning and release management.

---

**Document Version**: 1.0  
**Last Updated**: 2026-08-29  
**Status**: Active

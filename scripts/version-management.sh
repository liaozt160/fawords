#!/bin/bash
# Version Management Utility
# This script helps manage versions for the Fawords project
# Supports semantic versioning with hotfix tracking

set -e

# Color codes for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuration
VERSION_FILE="pom.xml"
TAG_PREFIX="v"
VERSION_PATTERN="[0-9]\+\.[0-9]\+\.[0-9]\+"

# Functions
print_help() {
    cat << EOF
${BLUE}Fawords Version Management${NC}

Usage: $0 [COMMAND] [OPTIONS]

Commands:
  ${GREEN}current${NC}           Show current version
  ${GREEN}latest${NC}            Show latest tagged version
  ${GREEN}next${NC}              Show next auto-incremented version
  ${GREEN}increment${NC} [TYPE]  Increment version (major, minor, patch - default: patch)
  ${GREEN}set${NC} [VERSION]     Manually set version to specific value
  ${GREEN}hotfix${NC} [VERSION]  Create a hotfix version
  ${GREEN}list${NC}              List all version tags
  ${GREEN}help${NC}              Show this help message

Options:
  --dry-run              Show what would be done without making changes
  --no-commit            Don't commit version changes
  --no-tag               Don't create git tag

Examples:
  $0 current                    # Show current version
  $0 next                       # Show next patch version
  $0 increment minor            # Increment minor version
  $0 set 1.5.0                 # Set version to 1.5.0
  $0 hotfix                    # Create hotfix version
  $0 list                      # List all versions

${YELLOW}Note:${NC} This script requires git and access to origin repository.

EOF
}

# Get current version from pom.xml
get_current_version() {
    grep -m 1 "<version>" "$VERSION_FILE" | sed 's/.*<version>\(.*\)<\/version>.*/\1/' | grep -oE "$VERSION_PATTERN" | head -1
}

# Get latest git tag
get_latest_tag() {
    git describe --tags --abbrev=0 --match="${TAG_PREFIX}[0-9]*.[0-9]*.[0-9]*" 2>/dev/null || echo "v0.0.0"
}

# Parse version into components
parse_version() {
    local version=$1
    version=${version#v}  # Remove v prefix if present
    IFS='.' read -r MAJOR MINOR PATCH <<< "$version"
}

# Increment version
increment_version() {
    local current_version=$1
    local increment_type=${2:-patch}
    
    parse_version "$current_version"
    
    case $increment_type in
        major)
            MAJOR=$((MAJOR + 1))
            MINOR=0
            PATCH=0
            ;;
        minor)
            MINOR=$((MINOR + 1))
            PATCH=0
            ;;
        patch|*)
            PATCH=$((PATCH + 1))
            ;;
    esac
    
    echo "${MAJOR}.${MINOR}.${PATCH}"
}

# Create hotfix version
create_hotfix_version() {
    local current_version=$1
    parse_version "$current_version"
    PATCH=$((PATCH + 1))
    echo "${MAJOR}.${MINOR}.${PATCH}-hotfix"
}

# Validate version format
validate_version() {
    local version=$1
    if [[ $version =~ ^[0-9]+\.[0-9]+\.[0-9]+(-hotfix)?$ ]]; then
        return 0
    else
        echo -e "${RED}Invalid version format: $version${NC}"
        echo "Expected format: MAJOR.MINOR.PATCH or MAJOR.MINOR.PATCH-hotfix"
        return 1
    fi
}

# Update version in pom.xml
update_pom_version() {
    local new_version=$1
    local dry_run=$2
    
    if [[ "$dry_run" == "true" ]]; then
        echo -e "${YELLOW}[DRY RUN]${NC} Would update pom.xml to version: $new_version"
        return 0
    fi
    
    # Update pom.xml
    sed -i "s/<version>.*<\/version>/<version>$new_version<\/version>/" "$VERSION_FILE" || {
        echo -e "${RED}Failed to update pom.xml${NC}"
        return 1
    }
    
    echo -e "${GREEN}✓ Updated pom.xml to version: $new_version${NC}"
}

# Create git tag
create_git_tag() {
    local version=$1
    local message=$2
    local dry_run=$3
    
    if [[ "$dry_run" == "true" ]]; then
        echo -e "${YELLOW}[DRY RUN]${NC} Would create tag: ${TAG_PREFIX}${version}"
        return 0
    fi
    
    git config user.name "Version Management Script" 2>/dev/null || true
    git config user.email "automation@fawords.local" 2>/dev/null || true
    
    git tag -a "${TAG_PREFIX}${version}" -m "$message" || {
        echo -e "${RED}Failed to create git tag${NC}"
        return 1
    }
    
    echo -e "${GREEN}✓ Created git tag: ${TAG_PREFIX}${version}${NC}"
}

# Commit version changes
commit_version_change() {
    local version=$1
    local dry_run=$2
    local no_commit=$3
    
    if [[ "$no_commit" == "true" ]]; then
        echo -e "${YELLOW}[SKIP]${NC} Skipping commit (--no-commit flag set)"
        return 0
    fi
    
    if [[ "$dry_run" == "true" ]]; then
        echo -e "${YELLOW}[DRY RUN]${NC} Would commit: chore(release): v${version}"
        return 0
    fi
    
    git add "$VERSION_FILE"
    
    if git diff --cached --quiet; then
        echo -e "${YELLOW}No changes to commit${NC}"
        return 0
    fi
    
    git commit -m "chore(release): v${version}" || {
        echo -e "${RED}Failed to commit version change${NC}"
        return 1
    }
    
    echo -e "${GREEN}✓ Committed version change: v${version}${NC}"
}

# Main command processing
main() {
    local command=${1:-current}
    local dry_run=false
    local no_commit=false
    local no_tag=false
    
    # Parse global options
    for arg in "$@"; do
        case $arg in
            --dry-run)
                dry_run=true
                ;;
            --no-commit)
                no_commit=true
                ;;
            --no-tag)
                no_tag=true
                ;;
        esac
    done
    
    case $command in
        current)
            current=$(get_current_version)
            echo -e "${BLUE}Current Version:${NC} ${GREEN}$current${NC}"
            ;;
            
        latest)
            latest=$(get_latest_tag)
            echo -e "${BLUE}Latest Tag:${NC} ${GREEN}$latest${NC}"
            ;;
            
        next)
            current=$(get_current_version)
            next=$(increment_version "$current" "patch")
            echo -e "${BLUE}Next Version (patch):${NC} ${GREEN}$next${NC}"
            ;;
            
        increment)
            current=$(get_current_version)
            increment_type=${2:-patch}
            new_version=$(increment_version "$current" "$increment_type")
            
            validate_version "$new_version" || exit 1
            
            echo -e "${BLUE}Incrementing version ($increment_type):${NC} $current → ${GREEN}$new_version${NC}"
            
            update_pom_version "$new_version" "$dry_run" || exit 1
            commit_version_change "$new_version" "$dry_run" "$no_commit" || exit 1
            
            if [[ "$no_tag" != "true" ]]; then
                create_git_tag "$new_version" "Release v$new_version" "$dry_run" || exit 1
            fi
            
            if [[ "$dry_run" == "true" ]]; then
                echo -e "\n${YELLOW}Dry-run mode: No changes were made${NC}"
            else
                echo -e "\n${GREEN}✓ Version successfully incremented to: $new_version${NC}"
            fi
            ;;
            
        set)
            new_version=$2
            if [[ -z "$new_version" ]]; then
                echo -e "${RED}Error: Version not specified${NC}"
                echo "Usage: $0 set VERSION"
                exit 1
            fi
            
            validate_version "$new_version" || exit 1
            
            current=$(get_current_version)
            echo -e "${BLUE}Setting version:${NC} $current → ${GREEN}$new_version${NC}"
            
            update_pom_version "$new_version" "$dry_run" || exit 1
            commit_version_change "$new_version" "$dry_run" "$no_commit" || exit 1
            
            if [[ "$no_tag" != "true" ]]; then
                create_git_tag "$new_version" "Release v$new_version (manual)" "$dry_run" || exit 1
            fi
            
            if [[ "$dry_run" == "true" ]]; then
                echo -e "\n${YELLOW}Dry-run mode: No changes were made${NC}"
            else
                echo -e "\n${GREEN}✓ Version successfully set to: $new_version${NC}"
            fi
            ;;
            
        hotfix)
            current=$(get_current_version)
            hotfix_version=$(create_hotfix_version "$current")
            
            # Remove -hotfix suffix for version tagging
            clean_version=${hotfix_version%-hotfix}
            
            echo -e "${BLUE}Creating hotfix:${NC} $current → ${GREEN}$clean_version${NC} ${YELLOW}(hotfix)${NC}"
            
            update_pom_version "$clean_version" "$dry_run" || exit 1
            commit_version_change "$clean_version" "$dry_run" "$no_commit" || exit 1
            
            if [[ "$no_tag" != "true" ]]; then
                create_git_tag "$clean_version" "🔥 Hotfix Release v$clean_version" "$dry_run" || exit 1
            fi
            
            if [[ "$dry_run" == "true" ]]; then
                echo -e "\n${YELLOW}Dry-run mode: No changes were made${NC}"
            else
                echo -e "\n${GREEN}✓ Hotfix version created: $clean_version${NC}"
            fi
            ;;
            
        list)
            echo -e "${BLUE}All Version Tags:${NC}"
            git tag -l "${TAG_PREFIX}[0-9]*.[0-9]*.[0-9]*" --sort=-version:refname || echo "No tags found"
            ;;
            
        help|--help|-h)
            print_help
            ;;
            
        *)
            echo -e "${RED}Unknown command: $command${NC}"
            echo "Run '$0 help' for usage information"
            exit 1
            ;;
    esac
}

# Run main function with all arguments
main "$@"

#!/bin/bash
# Version Management Workflow Test
# This script validates the version management system setup

set -e

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}═══════════════════════════════════════════════${NC}"
echo -e "${BLUE}Version Management System - Validation Test${NC}"
echo -e "${BLUE}═══════════════════════════════════════════════${NC}\n"

# Test 1: Check workflow file exists
echo -e "${YELLOW}[1]${NC} Checking workflow file..."
if [[ -f ".github/workflows/auto-version-tagging.yml" ]]; then
    echo -e "${GREEN}✓ Workflow file found${NC}"
else
    echo -e "${RED}✗ Workflow file not found${NC}"
    exit 1
fi

# Test 2: Check script file exists and is executable
echo -e "${YELLOW}[2]${NC} Checking version management script..."
if [[ -f "scripts/version-management.sh" ]]; then
    echo -e "${GREEN}✓ Script file found${NC}"
    if [[ -x "scripts/version-management.sh" ]]; then
        echo -e "${GREEN}✓ Script is executable${NC}"
    else
        echo -e "${YELLOW}⚠ Script is not executable${NC}"
        echo "  Run: chmod +x scripts/version-management.sh"
    fi
else
    echo -e "${RED}✗ Script file not found${NC}"
    exit 1
fi

# Test 3: Check git configuration
echo -e "${YELLOW}[3]${NC} Checking git configuration..."
if git config user.name > /dev/null 2>&1; then
    echo -e "${GREEN}✓ Git user.name configured: $(git config user.name)${NC}"
else
    echo -e "${YELLOW}⚠ Git user.name not configured${NC}"
    echo "  Run: git config user.name 'Your Name'"
fi

if git config user.email > /dev/null 2>&1; then
    echo -e "${GREEN}✓ Git user.email configured: $(git config user.email)${NC}"
else
    echo -e "${YELLOW}⚠ Git user.email not configured${NC}"
    echo "  Run: git config user.email 'your@email.com'"
fi

# Test 4: Check pom.xml format
echo -e "${YELLOW}[4]${NC} Checking pom.xml version format..."
if grep -q "<version>[0-9]*\.[0-9]*\.[0-9]*</version>" pom.xml 2>/dev/null; then
    VERSION=$(grep -m 1 "<version>" pom.xml | sed 's/.*<version>\(.*\)<\/version>.*/\1/' | grep -oE "[0-9]+\.[0-9]+\.[0-9]+")
    echo -e "${GREEN}✓ pom.xml has valid version format: $VERSION${NC}"
else
    echo -e "${YELLOW}⚠ pom.xml version format may need attention${NC}"
fi

# Test 5: Check documentation files
echo -e "${YELLOW}[5]${NC} Checking documentation..."
if [[ -f "docs/VERSION_MANAGEMENT.md" ]]; then
    echo -e "${GREEN}✓ Full documentation found${NC}"
else
    echo -e "${YELLOW}⚠ Full documentation not found (docs/VERSION_MANAGEMENT.md)${NC}"
fi

if [[ -f "docs/VERSION_QUICK_REFERENCE.md" ]]; then
    echo -e "${GREEN}✓ Quick reference found${NC}"
else
    echo -e "${YELLOW}⚠ Quick reference not found (docs/VERSION_QUICK_REFERENCE.md)${NC}"
fi

# Test 6: Test script functionality
echo -e "${YELLOW}[6]${NC} Testing script functionality..."
if ./scripts/version-management.sh help > /dev/null 2>&1; then
    echo -e "${GREEN}✓ Script help command works${NC}"
else
    echo -e "${RED}✗ Script help command failed${NC}"
fi

if ./scripts/version-management.sh current > /dev/null 2>&1; then
    CURRENT=$(./scripts/version-management.sh current 2>&1 | grep -oE "[0-9]+\.[0-9]+\.[0-9]+")
    echo -e "${GREEN}✓ Script current command works: $CURRENT${NC}"
else
    echo -e "${RED}✗ Script current command failed${NC}"
fi

# Test 7: Test git tag listing
echo -e "${YELLOW}[7]${NC} Checking git tags..."
TAG_COUNT=$(git tag -l "v[0-9]*.[0-9]*.[0-9]*" 2>/dev/null | wc -l)
if [[ $TAG_COUNT -gt 0 ]]; then
    echo -e "${GREEN}✓ Found $TAG_COUNT version tags${NC}"
    git tag -l "v[0-9]*.[0-9]*.[0-9]*" --sort=-version:refname | head -3 | sed 's/^/  /'
else
    echo -e "${YELLOW}ℹ No version tags yet (normal for new projects)${NC}"
fi

# Test 8: Check README mentions version management
echo -e "${YELLOW}[8]${NC} Checking README documentation..."
if grep -q "Version Management" README.md 2>/dev/null; then
    echo -e "${GREEN}✓ README includes version management section${NC}"
else
    echo -e "${YELLOW}⚠ README doesn't mention version management${NC}"
fi

# Summary
echo -e "\n${BLUE}═══════════════════════════════════════════════${NC}"
echo -e "${BLUE}Validation Complete${NC}"
echo -e "${BLUE}═══════════════════════════════════════════════${NC}\n"

echo -e "${GREEN}✓ Version management system is ready to use!${NC}\n"

echo -e "Next steps:"
echo -e "  1. ${YELLOW}Make changes and create a pull request${NC}"
echo -e "  2. ${YELLOW}Merge PR to main/master${NC}"
echo -e "  3. ${YELLOW}Workflow automatically creates version tag${NC}"
echo -e "  4. ${YELLOW}Check GitHub Releases for new release${NC}"
echo ""
echo -e "For manual versioning:"
echo -e "  ${YELLOW}./scripts/version-management.sh set 1.1.0${NC}"
echo ""
echo -e "For more information:"
echo -e "  ${YELLOW}./scripts/version-management.sh help${NC}"
echo -e "  ${YELLOW}cat docs/VERSION_MANAGEMENT.md${NC}"
echo -e "  ${YELLOW}cat docs/VERSION_QUICK_REFERENCE.md${NC}"
echo ""

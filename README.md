# Fawords

Fawords is a small Spring Boot project (Java 21, Maven) that provides the starting point for the Fawords application.

## Status

Initial project skeleton: Spring Boot + Web MVC. No application-specific documentation yet — please add domain details and API/docs as features are implemented.

## Requirements

- Java 21 (JDK)
- Maven 3.8+

## Build

From the repository root:

```bash
mvn -U clean package
```

This produces a runnable JAR in `target/` (e.g. `fawords-0.0.1-SNAPSHOT.jar`).

## Run

Run with Maven (during development):

```bash
mvn spring-boot:run
```

Or run the packaged jar:

```bash
java -jar target/fawords-0.0.1-SNAPSHOT.jar
```

The application uses the Spring Web MVC starter. Add controllers and other components under `src/main/java`.

## Tests

Run unit tests with:

```bash
mvn test
```

## Project layout

- `src/main/java` — application source
- `src/test/java` — tests
- `pom.xml` — Maven project file (Java 21, Spring Boot 4.1.0)

## Dependencies

This project currently includes:

- `spring-boot-starter-webmvc`

See `pom.xml` for the full list.

## Contributing

Small, surgical changes preferred. If you add or change behavior, also add tests that verify the change.

When making changes:

1. Make the smallest change that satisfies the requirement.
2. Add or update tests to demonstrate the behavior.
3. Don’t touch unrelated files or refactor large sections without an explicit request.
## Version Management

Fawords uses **automatic semantic versioning** with manual override capabilities.

### Version Format

```
MAJOR.MINOR.PATCH

Example: 1.0.0, 1.0.1, 1.1.0, 2.0.0
```

### Automatic Versioning (Default)

- **Trigger**: Pull request merged to `main` or `master`
- **Action**: Patch version auto-incremented (1.0.0 → 1.0.1)
- **Workflow**: `.github/workflows/auto-version-tagging.yml`
- **Result**: GitHub Release created, `pom.xml` updated

### Manual Versioning

**Option 1: GitHub Actions UI**
- Go to **Actions** → **📦 Auto Version Tagging**
- Click **Run workflow**
- Specify version (e.g., `1.1.0` or `1.5.0`)
- Check `is_hotfix` if needed
- Click **Run workflow**

**Option 2: Command Line Script**
```bash
# Show current version
./scripts/version-management.sh current

# Increment minor version (new features)
./scripts/version-management.sh increment minor

# Set specific version (breaking changes)
./scripts/version-management.sh set 2.0.0

# Create hotfix version
./scripts/version-management.sh hotfix

# Dry run (preview changes)
./scripts/version-management.sh increment patch --dry-run

# List all versions
./scripts/version-management.sh list
```

### Hotfix Releases

For critical production issues:

1. Create hotfix branch: `git checkout -b hotfix/issue-description`
2. Fix the issue and commit: `git commit -m "hotfix: critical issue fixed"`
3. Create PR to main and merge
4. Workflow automatically detects hotfix and tags release

### Documentation

- **Full Guide**: [docs/VERSION_MANAGEMENT.md](docs/VERSION_MANAGEMENT.md)
- **Quick Reference**: [docs/VERSION_QUICK_REFERENCE.md](docs/VERSION_QUICK_REFERENCE.md)
This repository follows a conservative approach to changes: keep edits minimal and well-tested.

## License

No license is specified in `pom.xml`. Add a LICENSE file or update `pom.xml` with license information if you want to open-source this repository.

## Notes for maintainers

- The project is configured to use Java 21 in `pom.xml`.
- If you plan to publish or document APIs, add an `openapi`/`docs` folder and update this README with usage examples and endpoints.


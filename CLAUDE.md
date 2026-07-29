# Karpathy-Inspired Claude Code Guidelines for Fawords Project

> These guidelines are inspired by Andrej Karpathy's observations on LLM coding pitfalls, adapted for the `fawords` Spring Boot project. They aim to improve code quality, maintainability, and efficient development by guiding the AI agent's coding behavior.

## The Problems

LLMs often exhibit the following behaviors in coding:

*   Making wrong assumptions without checking.
*   Not managing confusion, not seeking clarifications, not surfacing inconsistencies, not presenting tradeoffs, not pushing back when they should.
*   Overcomplicating code and APIs, bloating abstractions, not cleaning up dead code.
*   Changing/removing comments and code they don't sufficiently understand as side effects, even if orthogonal to the task.

## The Solution: Four Principles for Fawords Project Development

These four principles directly address the above issues for the `fawords` project:

### 1. Think Before Coding

**Don't assume. Don't hide confusion. Surface tradeoffs.**

*   **State assumptions explicitly**: If uncertain, ask rather than guess. Always clarify ambiguous requirements related to `fawords` domain logic.
*   **Present multiple interpretations**: When ambiguity exists (e.g., API design choices, database schema decisions), don't pick silently. Present options and their tradeoffs.
*   **Push back when warranted**: If a simpler or more idiomatic Spring Boot/Java approach exists, suggest it and explain why.
*   **Stop when confused**: Name what's unclear about `fawords` requirements or existing code and ask for clarification.

### 2. Simplicity First

**Minimum code that solves the problem. Nothing speculative.**

*   **No features beyond what was asked**: Stick to the explicit requirements for `fawords` features.
*   **No abstractions for single-use code**: Avoid premature generalization. If a component is only used once in `fawords`, keep it simple.
*   **No "flexibility" or "configurability" that wasn't requested**: Only add configuration or flexible designs if explicitly required for the `fawords` project.
*   **No error handling for impossible scenarios**: Focus on realistic error conditions within the `fawords` application context.
*   **If 200 lines could be 50, rewrite it**: Always strive for the most concise and readable solution for `fawords` components.
*   **The test**: Would a senior engineer say this `fawords` code is overcomplicated? If yes, simplify.

### 3. Surgical Changes

**Touch only what you must. Clean up only your own mess.**

When editing existing `fawords` code:

*   **Don't "improve" adjacent code, comments, or formatting**: Focus strictly on the task at hand. Match the existing `fawords` code style.
*   **Don't refactor things that aren't broken**: Unless specifically requested, avoid drive-by refactoring.
*   **Match existing style**: Adhere to the established coding conventions in the `fawords` project, even if you prefer a different style.
*   **If you notice unrelated dead code, mention it — don't delete it**: Document potential improvements, but don't act on them without explicit instruction.

When your changes create orphans in `fawords`:

*   **Remove imports/variables/functions that YOUR changes made unused**: Clean up the direct consequences of your modifications.
*   **Don't remove pre-existing dead code unless asked**: Maintain the existing codebase's integrity.

*   **The test**: Every changed line in `fawords` should trace directly to the user's request.

### 4. Goal-Driven Execution

**Define success criteria. Loop until verified.**

Transform imperative tasks into verifiable goals for `fawords` development:

| Instead of...             | Transform to...                                       |
| :------------------------ | :---------------------------------------------------- |
| "Add validation"          | "Write tests for invalid inputs, then make them pass" |
| "Fix the bug"             | "Write a test that reproduces it, then make it pass"  |
| "Refactor X"              | "Ensure tests pass before and after"                  |
| "Implement feature Y"     | "Develop feature Y, then write integration tests to confirm functionality" |

For multi-step tasks for `fawords`, state a brief plan with verifiable steps:

```
1. [Step for fawords] → verify: [check specific outcome] 
2. [Step for fawords] → verify: [check specific outcome]
3. [Step for fawords] → verify: [check specific outcome]
```

**Key Insight**: LLMs are exceptionally good at looping until they meet specific goals. Don't tell it what to do, give it success criteria and watch it go.

## Tradeoff Note

These guidelines bias toward **caution over speed** for `fawords` project development. For trivial tasks (simple typo fixes, obvious one-liners), use judgment — not every change needs the full rigor. The goal is reducing costly mistakes on non-trivial work, not slowing down simple tasks.

package edu.advising.state;
import edu.advising.commands.Enrollment;
/**
 * Interface for the Enrollment State pattern.
 * Each concrete state implements only the transitions it allows.
 * All others throw IllegalStateException via the default methods below.
 */
public interface EnrollmentState {

    // ── Transitions ────────────────────────────────────────────────

    /** Confirms a PENDING enrollment → ENROLLED. */
    default void confirm(EnrollmentContext ctx) {
        throw new IllegalStateException(
                "confirm() not allowed in state: " + ctx.getEnrollment().getStatus());
    }

    /** Drops an ENROLLED enrollment → DROPPED. */
    default void drop(EnrollmentContext ctx, String reason) {
        throw new IllegalStateException(
                "drop() not allowed in state: " + ctx.getEnrollment().getStatus());
    }

    /** Withdraws an ENROLLED enrollment → WITHDRAWN (W grade recorded). */
    default void withdraw(EnrollmentContext ctx) {
        throw new IllegalStateException(
                "withdraw() not allowed in state: " + ctx.getEnrollment().getStatus());
    }

    /** Completes an ENROLLED enrollment → COMPLETED (final grade recorded). */
    default void complete(EnrollmentContext ctx, String finalGrade) {
        throw new IllegalStateException(
                "complete() not allowed in state: " + ctx.getEnrollment().getStatus());
    }

    /** Re-enrolls a DROPPED student → ENROLLED if section has capacity. */
    default void reenroll(EnrollmentContext ctx) {
        throw new IllegalStateException(
                "reenroll() not allowed in state: " + ctx.getEnrollment().getStatus());
    }

    // ── Guard methods (used by UI to show/hide buttons) ────────────

    /** Returns true if drop() is allowed in this state. */
    default boolean canDrop()     { return false; }

    /** Returns true if withdraw() is allowed in this state. */
    default boolean canWithdraw() { return false; }

    /** Returns true if complete() is allowed in this state. */
    default boolean canComplete() { return false; }

    /** Returns true if reenroll() is allowed in this state. */
    default boolean canReenroll() { return false; }
}
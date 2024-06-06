/**
 * (c) 2021, Micro:bit Educational Foundation and contributors
 *
 * SPDX-License-Identifier: MIT
 */
export class TimeoutError extends Error {
}

/**
 * Utility to time out an action after a delay.
 *
 * The action cannot be cancelled; it may still proceed after the timeout.
 */
export async function withTimeout<T>(
    actionPromise: Promise<T>,
    timeout: number
): Promise<T> {
    const timeoutPromise = new Promise((_, reject) => {
        setTimeout(() => {
            reject(new TimeoutError());
        }, timeout);
    });
    // timeoutPromise never resolves so result must be from action
    return Promise.race([actionPromise, timeoutPromise]) as Promise<T>;
}

import * as LOG from 'log';

/**
 * assertEq: assert that two objects are === w.r.t. to type and content,
 * otherwise LOG and throw an exception
 */
export function assertEq(expected: any, given: any): void {
    function internalCheck(expected, given): string {
        if (typeof expected === typeof given) {
            if (expected === given) {
                return null;
            } else {
                return 'Violation. Expected value: ' + expected + ', given: ' + given;
            }
        } else {
            return 'Violation. Expected type: ' + typeof expected + ', given: ' + typeof given;
        }
    }
    let msg: string = internalCheck(expected, given);
    if (msg !== null) {
        LOG.info(msg);
        throw msg;
    }
}

/**
 * assertTrue: assert that a condition holds, otherwise LOG and throw an
 * exception
 */
export function assertTrue(boolToTest: boolean, msg: string): void {
    if (!boolToTest) {
        LOG.info(msg);
        throw msg;
    }
}

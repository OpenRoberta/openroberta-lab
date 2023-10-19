/**
 * Rest calls to the server related to userGroup operations (create user, login ...)
 *
 * @module rest/program
 */

import * as COMM from 'comm';
import * as GUI from 'guiState.model';

/**
 * Retrieves userGroup with specified name from the server, if the currently logged in user is its owner
 *
 * @param groupName
 *            {String} - name of the userGroup
 * @param successFn
 *            {Function} - a callback that is called when the creation succeeds. Needs to take one parameter "data"
 */
export function loadUserGroup(groupName: string, successFn: Function): void {
    COMM.json(
        '/userGroup/getUserGroup',
        {
            cmd: 'getUserGroup',
            groupName: groupName
        },
        successFn,
        'Got all information of the usergroup "' + groupName + '" of the user "' + GUI.user.accountName + '" from the server.'
    );
}

/**
 * Retrieves all userGroups from the server, for which the currently logged in user is the owner.
 *
 * @param successFn
 *            {Function} - a callback that is called when the creation succeeds. Needs to take one parameter "data"
 */
export function loadUserGroupList(successFn: Function): void {
    COMM.json(
        '/userGroup/getUserGroupList',
        {
            cmd: 'getUserGroupList'
        },
        successFn,
        'Got the list of usergroups for the user "' + GUI.user.accountName + '" from the server.'
    );
}

/**
 * Retrieves all members of the usergroup of the provided name from the server, for which the currently logged in user is the owner.
 *
 * @param groupName
 *            {String} - name of the userGroup
 * @param successFn
 *            {Function} - a callback that is called when the creation succeeds. Needs to take one parameter "data"
 */
export function loadUserGroupMemberList(groupName: string, successFn: Function): void {
    COMM.json(
        '/userGroup/getUserGroupMemberList',
        {
            cmd: 'getUserGroupMemberList',
            groupName: groupName
        },
        successFn,
        'Got the list of members for the usergroup "' + groupName + '" of the user "' + GUI.user.accountName + '" from the server.'
    );
}

/**
 * Create a usergroup on the server.
 *
 * @param groupName
 *            {String} - name of the userGroup
 * @param successFn
 *            {Function} - a callback that is called when the creation succeeds. Needs to take one parameter "data"
 *
 */
export function createUserGroup(groupName: string, initialMembers, successFn: Function): void {
    COMM.json(
        '/userGroup/createUserGroup',
        {
            cmd: 'createUserGroup',
            groupName: groupName,
            groupMemberNames: initialMembers
        },
        function(data): void {
            successFn(data);
        },
        'Create usergroup "' + groupName + '" for user "' + GUI.user.accountName + '" on server.'
    );
}

export function deleteUserGroup(groupName: string, successFn: Function): void {
    COMM.json(
        '/userGroup/deleteUserGroups',
        {
            cmd: 'deleteUserGroups',
            groupNames: [groupName]
        },
        function(data): void {
            successFn(data);
        },
        'Delete usergroup "' + groupName + '" of user "' + GUI.user.accountName + '" on server.'
    );
}

export function deleteUserGroups(groupNames: string[], successFn: Function): void {
    COMM.json(
        '/userGroup/deleteUserGroups',
        {
            cmd: 'deleteUserGroups',
            groupNames: groupNames
        },
        function(data): void {
            successFn(data);
        },
        'Deleted "' + groupNames.length + '" user groups of user "' + GUI.user.accountName + '" on server.'
    );
}

export function addGroupMembers(groupName: string, newMemberNames: string[], successFn: Function): void {
    COMM.json(
        '/userGroup/addGroupMembers',
        {
            cmd: 'addGroupMembers',
            groupName: groupName,
            groupMemberNames: newMemberNames
        },
        function(data): void {
            successFn(data);
        },
        'Added ' + newMemberNames.length + ' members to usergroup "' + groupName + '" of user "' + GUI.user.accountName + '" on server.'
    );
}

export function deleteGroupMember(groupName: string, memberAccount: string, successFn: Function): void {
    COMM.json(
        '/userGroup/deleteGroupMembers',
        {
            cmd: 'deleteGroupMembers',
            groupName: groupName,
            groupMemberAccounts: [memberAccount]
        },
        function(data): void {
            successFn(data);
        },
        'Deleted member "' + memberAccount + '" of usergroup "' + groupName + '" of user "' + GUI.user.accountName + '" on server.'
    );
}

export function deleteGroupMembers(groupName: string, memberAccounts: string[], successFn: Function): void {
    COMM.json(
        '/userGroup/deleteGroupMembers',
        {
            cmd: 'deleteGroupMembers',
            groupName: groupName,
            groupMemberAccounts: memberAccounts
        },
        function(data): void {
            successFn(data);
        },
        'Deleted ' + memberAccounts.length + ' members of usergroup "' + groupName + '" of user "' + GUI.user.accountName + '" on server.'
    );
}

export function setUserGroupMemberDefaultPassword(userGroupName: string, memberId: string, successFn: Function): void {
    COMM.json(
        '/userGroup/setUserGroupMemberDefaultPasswords',
        {
            cmd: 'setUserGroupMemberDefaultPasswords',
            groupName: userGroupName,
            groupMemberAccounts: [memberId]
        },
        function(data): void {
            successFn(data);
        },
        'Reset the password of user "' + memberId + '" (member of "' + userGroupName + '") to default on server.'
    );
}

export function setUserGroupMemberDefaultPasswords(userGroupName: string, memberIds: string[], successFn: Function): void {
    COMM.json(
        '/userGroup/setUserGroupMemberDefaultPasswords',
        {
            cmd: 'setUserGroupMemberDefaultPasswords',
            groupName: userGroupName,
            groupMemberAccounts: memberIds
        },
        function(data): void {
            successFn(data);
        },
        'Reset the password of ' + memberIds.length + ' users of the group "' + userGroupName + '" to default value on server.'
    );
}

export function updateMemberAccount(account: string, groupName: string, newAccount: string, successFn: Function): void {
    COMM.json(
        '/userGroup/updateMemberAccount',
        {
            cmd: 'updateMemberAccount',
            groupName: groupName,
            currentGroupMemberAccount: account,
            newGroupMemberAccount: newAccount
        },
        function(data): void {
            successFn(data);
        },
        'Set new account name for ' + account + ' of the group "' + groupName + '" to "' + newAccount + '" on server.'
    );
}

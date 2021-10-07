/**
 * Rest calls to the server related to userGroup operations (create user, login ...)
 * 
 * @module rest/program
 */
define([ 'exports', 'comm', 'guiState.model' ], function(exports, COMM, GUI) {


    /**
     * Retrieves userGroup with specified name from the server, if the currently logged in user is its owner
     * 
     * @param groupName
     *            {String} - name of the userGroup
     * @param successFn
     *            {Function} - a callback that is called when the creation succeeds. Needs to take one parameter "data"
     */
    function loadUserGroup(groupName, successFn) {
        COMM.json("/userGroup/getUserGroup", {
            cmd : "getUserGroup",
            groupName : groupName,
        }, successFn, 'Got all information of the usergroup "' + groupName + '" of the user "' + GUI.user.accountName + '" from the server.');
    }

    exports.loadUserGroup = loadUserGroup;

    /**
     * Retrieves all userGroups from the server, for which the currently logged in user is the owner.
     * 
     * @param successFn
     *            {Function} - a callback that is called when the creation succeeds. Needs to take one parameter "data"
     */
    function loadUserGroupList(successFn) {
        COMM.json("/userGroup/getUserGroupList", {
            cmd : 'getUserGroupList'
        }, successFn, 'Got the list of usergroups for the user "' + GUI.user.accountName + '" from the server.');
    }
    exports.loadUserGroupList = loadUserGroupList;

    /**
     * Retrieves all members of the usergroup of the provided name from the server, for which the currently logged in user is the owner.
     * 
     * @param groupName
     *            {String} - name of the userGroup
     * @param successFn
     *            {Function} - a callback that is called when the creation succeeds. Needs to take one parameter "data"
     */
    function loadUserGroupMemberList(groupName, successFn) {
        COMM.json("/userGroup/getUserGroupMemberList", {
            cmd : 'getUserGroupMemberList',
            groupName : groupName,
        }, successFn, 'Got the list of members for the usergroup "' + groupName + '" of the user "' + GUI.user.accountName + '" from the server.');
    }
    exports.loadUserGroupMemberList = loadUserGroupMemberList;

    /**
     * Create a usergroup on the server.
     * 
     * @param groupName
     *            {String} - name of the userGroup
     * @param successFn
     *            {Function} - a callback that is called when the creation succeeds. Needs to take one parameter "data"
     * 
     */
    function createUserGroup(groupName, initialMembers, successFn) {
        COMM.json("/userGroup/createUserGroup", {
            cmd : 'createUserGroup',
            groupName : groupName,
            groupMemberNames: initialMembers,
        }, function (data) {successFn(data);}, 'Create usergroup "' + groupName + '" for user "' + GUI.user.accountName + '" on server.');
    }
    exports.createUserGroup = createUserGroup;

    function deleteUserGroup(groupName,  successFn) {
        COMM.json("/userGroup/deleteUserGroups", {
            cmd: 'deleteUserGroups',
            groupNames: [groupName],
        }, function (data) {successFn(data);}, 'Delete usergroup "' + groupName + '" of user "' + GUI.user.accountName + '" on server.');
    }
    exports.deleteUserGroup = deleteUserGroup;
    
    function deleteUserGroups(groupNames,  successFn) {
        COMM.json("/userGroup/deleteUserGroups", {
            cmd: 'deleteUserGroups',
            groupNames: groupNames,
        }, function (data) {successFn(data);}, 'Deleted "' + groupNames.length + '" user groups of user "' + GUI.user.accountName + '" on server.');
    }
    exports.deleteUserGroups = deleteUserGroups;
    
    function addGroupMembers(groupName, newMemberNames, successFn) {
        COMM.json("/userGroup/addGroupMembers", {
            cmd : 'addGroupMembers',
            groupName : groupName,
            groupMemberNames: newMemberNames,
        }, function (data) {successFn(data);}, 'Added ' + newMemberNames.length + ' members to usergroup "' + groupName + '" of user "' + GUI.user.accountName + '" on server.');
    }
    exports.addGroupMembers = addGroupMembers;
    
    function deleteGroupMember(groupName, memberAccount, successFn) {
        COMM.json("/userGroup/deleteGroupMembers", {
            cmd : 'deleteGroupMembers',
            groupName : groupName,
            groupMemberAccounts: [memberAccount],
        }, function (data) {successFn(data);}, 'Deleted member "' + memberAccount + '" of usergroup "' + groupName + '" of user "' + GUI.user.accountName + '" on server.');
    }
    exports.deleteGroupMember = deleteGroupMember;
    
    function deleteGroupMembers(groupName, memberAccounts, successFn) {
        COMM.json("/userGroup/deleteGroupMembers", {
            cmd : 'deleteGroupMembers',
            groupName : groupName,
            groupMemberAccounts: memberAccounts,
        }, function (data) {successFn(data);}, 'Deleted ' + memberAccounts.length + ' members of usergroup "' + groupName + '" of user "' + GUI.user.accountName + '" on server.');
    }
    exports.deleteGroupMembers = deleteGroupMembers;
    
    function setUserGroupMemberDefaultPassword(userGroupName, memberId, successFn) {
        COMM.json("/userGroup/setUserGroupMemberDefaultPasswords", {
            cmd: 'setUserGroupMemberDefaultPasswords',
            groupName: userGroupName,
            groupMemberAccounts: [memberId]
        }, function (data) {successFn(data);}, 'Reset the password of user "' + memberId + '" (member of "' + userGroupName + '") to default on server.');
    }
    exports.setUserGroupMemberDefaultPassword = setUserGroupMemberDefaultPassword;
    
    function setUserGroupMemberDefaultPasswords(userGroupName, memberIds, successFn) {
        COMM.json("/userGroup/setUserGroupMemberDefaultPasswords", {
            cmd: 'setUserGroupMemberDefaultPasswords',
            groupName: userGroupName,
            groupMemberAccounts: memberIds
        }, function (data) {successFn(data);}, 'Reset the password of ' + memberIds.length + ' users of the group "' + userGroupName + '" to default value on server.');
    }
    exports.setUserGroupMemberDefaultPasswords = setUserGroupMemberDefaultPasswords;

    function updateMemberAccount(account, groupName, newAccount, successFn) {
        COMM.json("/userGroup/updateMemberAccount", {
            cmd: 'updateMemberAccount',
            groupName: groupName,
            currentGroupMemberAccount: account,
            newGroupMemberAccount: newAccount
        }, function (data) {successFn(data);}, 'Set new account name for ' + account + ' of the group "' + groupName + '" to "' + newAccount + '" on server.');
    }
    exports.updateMemberAccount = updateMemberAccount;
});

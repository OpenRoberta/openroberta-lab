define(["require","exports","comm","guiState.model"],(function(e,r,o,u){Object.defineProperty(r,"__esModule",{value:!0}),r.updateMemberAccount=r.setUserGroupMemberDefaultPasswords=r.setUserGroupMemberDefaultPassword=r.deleteGroupMembers=r.deleteGroupMember=r.addGroupMembers=r.deleteUserGroups=r.deleteUserGroup=r.createUserGroup=r.loadUserGroupMemberList=r.loadUserGroupList=r.loadUserGroup=void 0,r.loadUserGroup=function(e,r){o.json("/userGroup/getUserGroup",{cmd:"getUserGroup",groupName:e},r,'Got all information of the usergroup "'+e+'" of the user "'+u.user.accountName+'" from the server.')},r.loadUserGroupList=function(e){o.json("/userGroup/getUserGroupList",{cmd:"getUserGroupList"},e,'Got the list of usergroups for the user "'+u.user.accountName+'" from the server.')},r.loadUserGroupMemberList=function(e,r){o.json("/userGroup/getUserGroupMemberList",{cmd:"getUserGroupMemberList",groupName:e},r,'Got the list of members for the usergroup "'+e+'" of the user "'+u.user.accountName+'" from the server.')},r.createUserGroup=function(e,r,s){o.json("/userGroup/createUserGroup",{cmd:"createUserGroup",groupName:e,groupMemberNames:r},(function(e){s(e)}),'Create usergroup "'+e+'" for user "'+u.user.accountName+'" on server.')},r.deleteUserGroup=function(e,r){o.json("/userGroup/deleteUserGroups",{cmd:"deleteUserGroups",groupNames:[e]},(function(e){r(e)}),'Delete usergroup "'+e+'" of user "'+u.user.accountName+'" on server.')},r.deleteUserGroups=function(e,r){o.json("/userGroup/deleteUserGroups",{cmd:"deleteUserGroups",groupNames:e},(function(e){r(e)}),'Deleted "'+e.length+'" user groups of user "'+u.user.accountName+'" on server.')},r.addGroupMembers=function(e,r,s){o.json("/userGroup/addGroupMembers",{cmd:"addGroupMembers",groupName:e,groupMemberNames:r},(function(e){s(e)}),"Added "+r.length+' members to usergroup "'+e+'" of user "'+u.user.accountName+'" on server.')},r.deleteGroupMember=function(e,r,s){o.json("/userGroup/deleteGroupMembers",{cmd:"deleteGroupMembers",groupName:e,groupMemberAccounts:[r]},(function(e){s(e)}),'Deleted member "'+r+'" of usergroup "'+e+'" of user "'+u.user.accountName+'" on server.')},r.deleteGroupMembers=function(e,r,s){o.json("/userGroup/deleteGroupMembers",{cmd:"deleteGroupMembers",groupName:e,groupMemberAccounts:r},(function(e){s(e)}),"Deleted "+r.length+' members of usergroup "'+e+'" of user "'+u.user.accountName+'" on server.')},r.setUserGroupMemberDefaultPassword=function(e,r,u){o.json("/userGroup/setUserGroupMemberDefaultPasswords",{cmd:"setUserGroupMemberDefaultPasswords",groupName:e,groupMemberAccounts:[r]},(function(e){u(e)}),'Reset the password of user "'+r+'" (member of "'+e+'") to default on server.')},r.setUserGroupMemberDefaultPasswords=function(e,r,u){o.json("/userGroup/setUserGroupMemberDefaultPasswords",{cmd:"setUserGroupMemberDefaultPasswords",groupName:e,groupMemberAccounts:r},(function(e){u(e)}),"Reset the password of "+r.length+' users of the group "'+e+'" to default value on server.')},r.updateMemberAccount=function(e,r,u,s){o.json("/userGroup/updateMemberAccount",{cmd:"updateMemberAccount",groupName:r,currentGroupMemberAccount:e,newGroupMemberAccount:u},(function(e){s(e)}),"Set new account name for "+e+' of the group "'+r+'" to "'+u+'" on server.')}}));
//# sourceMappingURL=userGroup.model.js.map
//# sourceMappingURL=userGroup.model.js.map

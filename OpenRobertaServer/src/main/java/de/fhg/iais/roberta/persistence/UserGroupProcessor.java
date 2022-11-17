package de.fhg.iais.roberta.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.fhg.iais.roberta.persistence.bo.AccessRight;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.bo.UserGroup;
import de.fhg.iais.roberta.persistence.dao.UserDao;
import de.fhg.iais.roberta.persistence.dao.UserGroupDao;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.basic.Pair;
import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * @author Philipp Maurer
 */
public class UserGroupProcessor extends AbstractProcessor {

    private final UserGroupDao userGroupDao;
    private final UserDao userDao;

    protected final Pattern groupNamePattern;

    protected static final int OWNER_GROUP_LIMIT = 100;
    protected static final int GROUP_STUDENT_LIMIT = 99;
    public static final String GROUP_NAME_DELIMITER = ":";

    private final boolean isPublicServer;

    public UserGroupProcessor(DbSession dbSession, int userId, boolean isPublicServer) {
        super(dbSession, userId);
        this.isPublicServer = isPublicServer;

        this.userGroupDao = new UserGroupDao(this.dbSession);
        this.userDao = new UserDao(this.dbSession);

        Pattern pattern;
        try {
            pattern = Pattern.compile("[^a-zA-Z0-9=+!?.,%#+&^@_\\- ]", Pattern.CASE_INSENSITIVE);
        } catch ( Exception e ) {
            //In case there is a bug we simply will not check the pattern
            pattern = null;
        }

        this.groupNamePattern = pattern;
    }

    /**
     * Returns a user group based on its name and its owner.
     *
     * @param groupName The name of the user group
     * @param groupOwner The owner of the user group
     * @param isLogin TODO
     * @return The user group, or null, if no such user group exists.
     */
    public UserGroup getGroup(String groupName, User groupOwner, boolean isLogin) {
        if ( groupOwner == null || groupName == null ) {
            this.setStatus(ProcessorStatus.FAILED, Key.GROUP_GET_ONE_ERROR, new HashMap<>());
            return null;
        }

        if ( !this.canOwnGroup(groupOwner) && !isLogin ) {
            this.setStatus(ProcessorStatus.FAILED, Key.GROUP_ERROR_MISSING_RIGHTS_TO_BE_GROUP_OWNER, new HashMap<>());
            return null;
        }

        if ( !this.isValidGroupName(groupName) ) {
            this.setStatus(ProcessorStatus.FAILED, Key.GROUP_ERROR_NAME_INVALID, new HashMap<>());
            return null;
        }

        UserGroup group = this.userGroupDao.load(groupName, groupOwner);

        if ( group == null ) {
            this.setStatus(ProcessorStatus.FAILED, Key.GROUP_GET_ONE_ERROR_NOT_FOUND, new HashMap<>());
            return null;
        }

        this.setStatus(ProcessorStatus.SUCCEEDED, Key.GROUP_GET_ONE_SUCCESS, new HashMap<>());
        return group;
    }

    /**
     * Returns all user groups a user owns
     *
     * @param groupOwner The user, who's user groups will be returned.
     * @return A list of all user groups that the specified user is the owner of. Returns null, if the specified user is null or is not allowed to own user
     *     groups.
     */
    public List<UserGroup> getGroupsByOwner(User groupOwner) {
        if ( groupOwner == null ) {
            this.setStatus(ProcessorStatus.FAILED, Key.GROUP_GET_ALL_ERROR, new HashMap<>());
            return null;
        }

        if ( !this.canOwnGroup(groupOwner) ) {
            this.setStatus(ProcessorStatus.FAILED, Key.GROUP_ERROR_MISSING_RIGHTS_TO_BE_GROUP_OWNER, new HashMap<>());
            return null;
        }

        List<UserGroup> groups = this.userGroupDao.loadAll(groupOwner);

        this.setStatus(ProcessorStatus.SUCCEEDED, Key.GROUP_GET_ALL_SUCCESS, new HashMap<>());
        return groups;
    }

    /**
     * Creates a new user group with the given name for the specified user and generates new members for the group.
     *
     * @param groupName The name the new user group shall have.
     * @param groupOwner The owner of the new user group.
     * @param initialMembers The number of members that shall be generated for the new user group. If a member can not be generated, he is omitted. The actually
     *     created group might have less members than specified therefore.
     * @return The newly created user group or null, if the user group can not be created
     */
    public UserGroup createGroup(String groupName, User groupOwner, List<String> initialMembers) {
        if ( groupOwner == null || groupName == null ) {
            this.setStatus(ProcessorStatus.FAILED, Key.GROUP_CREATE_ERROR, new HashMap<>());
            return null;
        }

        Map<String, String> processorParameters = new HashMap<>();
        processorParameters.put("USERGROUP_OWNER", groupOwner.getAccount());

        if ( !this.canOwnGroup(groupOwner) ) {
            this.setStatus(ProcessorStatus.FAILED, Key.GROUP_ERROR_MISSING_RIGHTS_TO_BE_GROUP_OWNER, processorParameters);
            return null;
        }
        if ( this.userGroupDao.getNumberOfGroupsOfOwner(groupOwner) >= UserGroupProcessor.OWNER_GROUP_LIMIT ) {
            this.setStatus(ProcessorStatus.FAILED, Key.GROUP_CREATE_ERROR_GROUP_LIMIT_REACHED, processorParameters);
            return null;
        }

        processorParameters.put("USERGROUP_NAME", groupName);

        if ( !this.isValidGroupName(groupName) ) {
            this.setStatus(ProcessorStatus.FAILED, Key.GROUP_ERROR_NAME_INVALID, processorParameters);
            return null;
        }

        if ( this.userGroupDao.load(groupName, groupOwner) != null ) {
            this.setStatus(ProcessorStatus.FAILED, Key.GROUP_CREATE_ERROR_GROUP_ALREADY_EXISTS, processorParameters);
            return null;
        }

        if ( initialMembers.size() > UserGroupProcessor.GROUP_STUDENT_LIMIT ) {
            this.setStatus(ProcessorStatus.FAILED, Key.GROUP_ADD_MEMBER_ERROR_LIMIT_REACHED, processorParameters);
            return null;
        }

        String accountPrefix = groupName + UserGroupProcessor.GROUP_NAME_DELIMITER;

        for ( String newMemberName : initialMembers ) {
            Matcher illegalCharacterMatcher = UserProcessor.ILLEGAL_USER_NAME_CHARACTER_PATTERN.matcher(newMemberName);
            if ( illegalCharacterMatcher.find() ) {
                this.setStatus(ProcessorStatus.FAILED, Key.USER_CREATE_ERROR_CONTAINS_SPECIAL_CHARACTERS, processorParameters);
                return null;
            }
        }

        Pair<Key, UserGroup> createStatus = this.userGroupDao.persistGroup(groupName, groupOwner, null); // data base changed
        UserGroup userGroup = createStatus.getSecond();

        boolean succeeded = createStatus.getFirst().equals(Key.GROUP_CREATE_SUCCESS) && userGroup != null;
        ProcessorStatus status = succeeded ? ProcessorStatus.SUCCEEDED : ProcessorStatus.FAILED;
        this.setStatus(status, createStatus.getFirst(), processorParameters);
        if ( succeeded ) {
            //TODO Currently only ADMIN_READ is supported. Implement other visibilities.
            userGroup.setAccessRight(AccessRight.ADMIN_READ);
            if ( initialMembers.size() > 0 ) {
                for ( String newMemberName : initialMembers ) {
                    String memberAccountName = accountPrefix + newMemberName;
                    try {
                        User member = this.userDao.persistNewUser(userGroup, memberAccountName, memberAccountName, "STUDENT"); // data base changed
                        Assert.notNull(member);
                        userGroup.addMember(member);
                    } catch ( Exception e ) {
                        this.setStatus(ProcessorStatus.FAILED, Key.SERVER_ERROR, processorParameters);
                        return null;
                    }
                }
            }
        }
        return userGroup;
    }

    /**
     * Deletes all specified user groups, that are owned by the specified user.
     *
     * @param groupNames A list of names of the user groups that shall be deleted.
     * @param groupOwner The owner of all user groups
     * @param deleteIfGroupHasMembers A flag that will prevent user groups that still have members from being deleted, if set to true.
     */
    public void deleteGroups(List<String> groupNames, User groupOwner, boolean deleteIfGroupHasMembers) {
        if ( groupOwner == null || groupNames == null || groupNames.size() == 0 ) {
            this.setStatus(ProcessorStatus.FAILED, Key.GROUP_DELETE_ERROR, new HashMap<>());
            return;
        }

        Map<String, String> processorParameters = new HashMap<>();
        processorParameters.put("USERGROUP_OWNER", groupOwner.getAccount());

        List<UserGroup> groupsToBeDeletedLater = new ArrayList<>();

        for ( String groupName : groupNames ) {
            UserGroup userGroup = this.userGroupDao.load(groupName, groupOwner);
            if ( userGroup == null ) {
                processorParameters.put("USERGROUP_NAME", groupName);
                this.setStatus(ProcessorStatus.FAILED, Key.GROUP_DELETE_ERROR_GROUP_DOES_NOT_EXISTS, processorParameters);
                return;
            }
            if ( !deleteIfGroupHasMembers && userGroup.getMembers().size() > 0 ) {
                this.setStatus(ProcessorStatus.FAILED, Key.GROUP_DELETE_ERROR_GROUP_HAS_MEMBERS, processorParameters);
                return;
            }
            groupsToBeDeletedLater.add(userGroup);
        }
        for ( UserGroup userGroup : groupsToBeDeletedLater ) {
            this.userGroupDao.delete(userGroup);
        }
        this.setStatus(ProcessorStatus.SUCCEEDED, Key.GROUP_DELETE_SUCCESS, processorParameters);
    }

    /**
     * Updates the account of a member to a new value.
     *
     * @param member The member of a group that shall get a new account name
     * @param newAccount The new account name, with or without the user group name prefix
     * @throws Exception
     */
    public void updateMemberAccount(User member, String newAccount) throws Exception {
        Map<String, String> processorParameters = new HashMap<>();

        if ( member == null || member.getUserGroup() == null ) {
            this.setStatus(ProcessorStatus.FAILED, Key.GROUP_GET_MEMBER_ERROR_NOT_FOUND, processorParameters);
            return;
        }

        processorParameters.put("CURRENT_MEMBER_ACCOUNT", member.getAccount());

        Matcher illegalCharacterMatcher = UserProcessor.ILLEGAL_USER_NAME_CHARACTER_PATTERN.matcher(newAccount);

        if ( illegalCharacterMatcher.find() ) {
            this.setStatus(ProcessorStatus.FAILED, Key.USER_CREATE_ERROR_CONTAINS_SPECIAL_CHARACTERS, processorParameters);
            return;
        }

        String newAccountFormatted = member.getUserGroup().getName() + UserGroupProcessor.GROUP_NAME_DELIMITER + newAccount;

        processorParameters.put("NEW_MEMBER_ACCOUNT", newAccountFormatted);

        UserDao userDao = new UserDao(this.dbSession);
        User duplicateMember = userDao.loadUser(member.getUserGroup(), newAccountFormatted);

        if ( duplicateMember != null ) {
            this.setStatus(ProcessorStatus.FAILED, Key.GROUP_MEMBER_ERROR_ALREADY_EXISTS, processorParameters);
            return;
        }

        if ( member.isPasswordCorrect(member.getAccount()) ) {
            member.setPassword(newAccountFormatted);
        }

        member.setAccount(newAccountFormatted);
        this.setStatus(ProcessorStatus.SUCCEEDED, Key.SERVER_SUCCESS, processorParameters);
    }

    /**
     * Checks for a given user if he can own a user group. Includes a null check.<br/>
     * A user can own a group, if he/she:<br/>
     * - is not a member of group <br/>
     * - is associated with a validated email address.
     *
     * @param owner The user to be tested
     * @return true, if the user can own a user group, false otherwise
     */
    protected boolean canOwnGroup(User owner) {
        if ( owner == null || owner.getUserGroup() != null ) {
            return false;
        } else if ( !this.isPublicServer ) { // test server allow not validated email addresses
            return true;
        } else {
            String email = owner.getEmail();
            email = email == null ? "" : email;
            return owner.isActivated() && !email.equals("");
        }
    }

    /**
     * Checks if a given string is a valid name for a user group. Includes a null check.<br/>
     * A valid group name can <b>not</b>:<br/>
     * - be empty, <br/>
     * - start or end with a space <br/>
     * - may not contain any other special chars than the ones defined in this classes pattern <br/>
     *
     * @param groupName The string that shall be checked
     * @return True, if the string can be used as a group name. False otherwise
     */
    protected boolean isValidGroupName(String groupName) {
        return groupName != null
            && !groupName.equals("")
            && groupName.trim().equals(groupName)
            && (this.groupNamePattern == null || !this.groupNamePattern.matcher(groupName).find());
    }

    /**
     * Checks whether or not the given string is a valid name of any user group member. Includes a null check. A string is a valid name of any user group
     * member, if it contains a colon ":", which is otherwise an illegal character for a user name.<br/>
     * Does <strong>not</strong> check if a user group member with the given name exists or not!
     *
     * @param userAccount The string that shall be checked.
     * @return True, if the string would be a valid name of a user group member. False otherwise
     */
    public static boolean isGroupMember(String userAccount) {
        return userAccount != null && userAccount.contains(GROUP_NAME_DELIMITER);
    }

    /**
     * Returns a list of all members of a specified user group, identified by its name and its owner
     *
     * @param groupName THe name of the user group
     * @param groupOwner The owner of the user group
     * @return A list of all members of the specified user group, or null, if no such user group exists.
     */
    public List<User> getUserGroupMembers(String groupName, User groupOwner) {
        if ( groupOwner == null || groupName == null ) {
            this.setStatus(ProcessorStatus.FAILED, Key.GROUP_DELETE_ERROR, new HashMap<>());
            return null;
        }

        Map<String, String> processorParameters = new HashMap<>();
        processorParameters.put("USERGROUP_OWNER", groupOwner.getAccount());
        processorParameters.put("USERGROUP_NAME", groupName);

        UserGroup userGroup = this.userGroupDao.load(groupName, groupOwner);

        if ( userGroup == null ) {
            this.setStatus(ProcessorStatus.FAILED, Key.GROUP_DELETE_ERROR_GROUP_DOES_NOT_EXISTS, processorParameters);
            return null;
        }

        List<User> userList = this.userDao.loadUsersOfGroup(userGroup);

        ProcessorStatus status = userList != null ? ProcessorStatus.SUCCEEDED : ProcessorStatus.FAILED;

        this.setStatus(status, Key.GROUP_GET_MEMBERS_SUCCESS, processorParameters);

        return userList;
    }

    /**
     * Generates the specified number of new members to the specified user group
     *
     * @param userGroup The user group, for which new members shall be generated.
     * @param newMemberCount The number of members, that shall be generated.
     */
    public void addMembersToUserGroup(UserGroup userGroup, List<String> newMemberNames) {
        Assert.notNull(userGroup);

        Map<String, String> processorParameters = new HashMap<>();
        processorParameters.put("USERGROUP_NAME", userGroup.getName());
        processorParameters.put("USERGROUP_SIZE", String.format("%d", userGroup.getMembers().size()));

        if ( newMemberNames == null || newMemberNames.size() == 0 ) {
            this.setStatus(ProcessorStatus.FAILED, Key.GROUP_ADD_MEMBER_ERROR_SMALLER_THAN_ONE, processorParameters);
            return;
        }

        processorParameters.put("USERGROUP_MEMBERS_TO_ADD", String.format("%d", newMemberNames.size()));

        if ( userGroup.getMembers().size() + newMemberNames.size() > UserGroupProcessor.GROUP_STUDENT_LIMIT ) {
            this.setStatus(ProcessorStatus.FAILED, Key.GROUP_ADD_MEMBER_ERROR_LIMIT_REACHED, processorParameters);
            return;
        }

        String accountPrefix = userGroup.getName() + UserGroupProcessor.GROUP_NAME_DELIMITER;
        String memberAccountName;
        UserDao userDao = new UserDao(this.dbSession);
        User member;
        for ( String newMemberName : newMemberNames ) {
            newMemberName = newMemberName.trim();
            if ( newMemberName.equals("") ) {
                this.setStatus(ProcessorStatus.FAILED, Key.USER_CREATE_ERROR_MISSING_REQ_FIELDS, processorParameters);
                return;
            }

            Matcher illegalCharacterMatcher = UserProcessor.ILLEGAL_USER_NAME_CHARACTER_PATTERN.matcher(newMemberName);
            if ( illegalCharacterMatcher.find() ) {
                this.setStatus(ProcessorStatus.FAILED, Key.USER_CREATE_ERROR_CONTAINS_SPECIAL_CHARACTERS, processorParameters);
                return;
            }

            member = userDao.loadUser(userGroup, accountPrefix + newMemberName);

            if ( member != null ) {
                this.setStatus(ProcessorStatus.FAILED, Key.GROUP_MEMBER_ERROR_ALREADY_EXISTS, processorParameters);
                return;
            }
        }

        for ( String newMemberName : newMemberNames ) {
            newMemberName = newMemberName.trim();
            memberAccountName = accountPrefix + newMemberName;
            try {
                member = this.userDao.persistNewUser(userGroup, memberAccountName, memberAccountName, "STUDENT");
                userGroup.addMember(member);
            } catch ( Exception e ) {
                this.setStatus(ProcessorStatus.FAILED, Key.SERVER_ERROR, processorParameters);
                return;
            }
        }
        this.setStatus(ProcessorStatus.SUCCEEDED, Key.GROUP_ADD_MEMBER_SUCCESS, processorParameters);
    }
}
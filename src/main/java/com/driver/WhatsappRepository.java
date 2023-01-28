package com.driver;

import org.springframework.stereotype.Repository;


import java.util.*;

@Repository
public class WhatsappRepository {

    private int count = 0;

    private int id = 0; // Maximum messages were present till now
    private int currentTotalMessages = 0; // current messages

    Map<String, User> users = new HashMap<>(); // All users
    Map<Group, List<User>> group_users = new HashMap<>(); // Group --> Users
    Map<Group, List<Message>> group_messages = new HashMap<>(); // Group --> Messages
    Map<Group, User> group_admin = new HashMap<>(); // Group --> Admin
    Map<User, List<Message>> user_messages = new HashMap<>(); // User --> Messages



    // 1.
    public String createUser(String name, String mobile) throws Exception {
        //If the mobile number exists in database, throw "User already exists" exception
        //Otherwise, create the user and return "SUCCESS"
        if(users.containsKey(mobile)){
            throw new Exception("User already exists");
        }
        User user = new User(name, mobile);
        users.put(mobile, user);
        return "SUCCESS";
    }


    // 2. Create Group
    public Group createGroup(List<User> users){
        // The list contains at least 2 users where the first user is the admin. A group has exactly one admin.
        // If there are only 2 users, the group is a personal chat and the group name should be kept as the name of the second user(other than admin)
        // If there are 2+ users, the name of group should be "Group count". For example, the name of first group would be "Group 1", second would be "Group 2" and so on.
        // Note that a personal chat is not considered a group and the count is not updated for personal chats.
        // If group is successfully created, return group.
        int size = users.size();
        Group group = null;
        if(size == 2){
            group = new Group(users.get(1).getName(), size);
        }
        else if(size > 2){
            count++;
            group = new Group("Group " + count, size);
        }
        group_users.put(group, users);
        group_admin.put(group, users.get(0));
        //For example: Consider userList1 = {Alex, Bob, Charlie}, userList2 = {Dan, Evan}, userList3 = {Felix, Graham, Hugh}.
        //If createGroup is called for these userLists in the same order, their group names would be "Group 1", "Evan", and "Group 2" respectively.
        return group;
    }


    // 3. Create message
    public int createMessage(String content){
        // The 'i^th' created message has message id 'i'.
        // Return the message id.
        Message message = new Message(++id, content);
        message.setTimestamp(new Date());
        currentTotalMessages++;
        return id;
    }

    // 4.
    public int sendMessage(Message message, User sender, Group group) throws Exception{
        //Throw "Group does not exist" if the mentioned group does not exist
        //Throw "You are not allowed to send message" if the sender is not a member of the group
        //If the message is sent successfully, return the final number of messages in that group.
        if(!group_users.containsKey(group)){
            throw new Exception("Group does not exist");
        }
        if(!group_users.get(group).contains(sender)){
            throw new Exception("You are not allowed to send message");
        }

        // Group --> List<Message>
        if(group_messages.get(group).size() == 0){
            group_messages.put(group, new ArrayList<>());
        }
        group_messages.get(group).add(message);

        // User --> Message
        if(user_messages.get(sender).size() == 0){
            user_messages.put(sender, new ArrayList<>());
        }
        user_messages.get(sender).add(message);

        return group_messages.get(group).size();
    }


    // 5. Change Admin
    public String changeAdmin(User approver, User user, Group group) throws Exception{
        //Throw "Group does not exist" if the mentioned group does not exist
        //Throw "Approver does not have rights" if the approver is not the current admin of the group
        //Throw "User is not a participant" if the user is not a part of the group
        //Change the admin of the group to "user" and return "SUCCESS". Note that at one time there is only one admin and the admin rights are transferred from approver to user.
        if(!group_users.containsKey(group)){
            throw new Exception("Group does not exist");
        }
        if(group_admin.get(group) != approver){
            throw new Exception("Approver does not have rights");
        }
        if(!group_users.get(group).contains(user)){
            throw new Exception("User is not a participant");
        }
        group_admin.put(group, user);
        return "SUCCESS";
    }

    // 6.
    public int removeUser(User user) throws Exception{
        //A user belongs to exactly one group
        //If user is not found in any group, throw "User not found" exception
        //If user is found in a group and it is the admin, throw "Cannot remove admin" exception
        //If user is not the admin, remove the user from the group, remove all its messages from all the databases, and update relevant attributes accordingly.
        //If user is removed successfully, return (the updated number of users in the group + the updated number of messages in group + the updated number of overall messages)
        boolean isUserPresent = false;
        for(Group group : group_users.keySet()){
            for(User user1 : group_users.get(group)){
                // User present in one of the group
                if(user1 == user){
                    isUserPresent = true;
                    // User = Admin
                    if(group_admin.get(group) == user){
                        throw new Exception("Cannot remove admin");
                    }
                    // User != Admin, So removing
                    for(Message message : user_messages.get(user)){
                        group_messages.get(group).remove(message);
                    }
                    currentTotalMessages -= user_messages.get(user).size();
                    users.remove(user1.getMobile()); // Remove User
                    user_messages.remove(user); // User messages removed
                    group_users.get(group).remove(user); //User removed from Group
                    return (group_users.get(group).size() + group_messages.get(group).size() + currentTotalMessages);
                }
            }
        }
        if(!isUserPresent){
            throw new Exception("User not found");
        }

        return -1;
    }


    // 7.
    public String findMessage(Date start, Date end, int K) throws Exception{
        // This is a bonus problem and does not contains any marks
        // Find the Kth latest message between start and end (excluding start and end)
        // If the number of messages between given time is less than K, throw "K is greater than the number of messages" exception
        return null;
    }

}

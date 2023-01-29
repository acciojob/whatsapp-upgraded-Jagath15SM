package com.driver;

import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.List;

@Service
public class WhatsappService {

    WhatsappRepository whatsappRepository = new WhatsappRepository();

    public String createUser(String name, String mobile) throws Exception {
        try{
            String result =  whatsappRepository.createUser(name, mobile);
            return result;
        }
        catch (Exception e){
            return e.getMessage();
        }
    }

    public Group createGroup(List<User> users){
        return whatsappRepository.createGroup(users);
    }


    public int createMessage(String content){
        return whatsappRepository.createMessage(content);
    }


    public int sendMessage(Message message, User sender, Group group) throws Exception{
        try{
            int result = whatsappRepository.sendMessage(message, sender, group);
            return result;
        }
        catch (Exception e){
            return 1;
        }
    }

    public String changeAdmin(User approver, User user, Group group) throws Exception{
        try{
            String result =  whatsappRepository.changeAdmin(approver, user, group);
            return result;
        }
        catch (Exception e){
            return e.getMessage();
        }
    }

    public int removeUser(User user) throws Exception{
        try{
            int result =  whatsappRepository.removeUser(user);
            return result;
        }
        catch (Exception e){
            return -5;
        }
    }

    public String findMessage(Date start, Date end, int K) throws Exception{
        return whatsappRepository.findMessage(start, end, K);
    }

}

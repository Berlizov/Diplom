import Structs.*;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * Created by Eugene Berlizov on 17.10.2014.
 */
class ClientConnector extends Thread implements UpdateInterface {
    final ArrayList<Packet> packetArrayList = new ArrayList<>();
    private final int maxTries = 3;

    private PrintWriter out;
    private UpdateProxy c;
    private SenderInterface sender;
    private String ip = "";
    private int port = 0;
    private  User user = new User();

    public Packet setup(String ip, int port, String login, String password, SenderInterface sender) throws IOException, JAXBException {
        this.ip = ip;
        this.port = port;
        user.setLogin(login);
        user.setPass(password);
        this.sender = sender;
        return connect();
    }


    Packet connect() throws IOException, JAXBException {

       /* Socket kkSocket = new Socket(ip, port);
        out = new PrintWriter(new OutputStreamWriter(kkSocket.getOutputStream()));
        c = new UpdateProxy(this, new BufferedReader(new InputStreamReader(kkSocket.getInputStream())));
        c.start();*/
        return sendMessage(new Packet(API.LOGIN, user.getLogin(), user.getPass()));
    }

    public Packet sendMessage(Packet packet) throws JAXBException, IOException {
        int tries = 0;
        while (tries < maxTries) {
          /*  try {
                System.out.println(packet.xmlGenerate());
                out.println(packet.xmlGenerate());
                out.flush();
                packet = readMessage(packet.func);
                return packet;*/
                return switcher(packet);
           /* } catch (IOException e) {
                if(packet.func!=API.LOGIN)
                    connect();
                tries++;
            }*/
        }
        throw new IOException();
    }


    private Packet readMessage(API func) throws IOException {
        Packet pack=new Packet(func);
        if (!c.isAlive())
            throw new IOException();
        try{
        synchronized (packetArrayList) {

                boolean f=true;
                for (int i = 0; i < packetArrayList.size(); i++) {
                    pack=packetArrayList.get(i);
                    if(pack.func == func){
                        packetArrayList.remove(pack);
                        f= false;
                        break;
                    }
                }
            if(f)
            {
                packetArrayList.wait(2000);
                for (int i = 0; i < packetArrayList.size(); i++) {
                    pack=packetArrayList.get(i);
                    if(pack.func == func){
                        packetArrayList.remove(pack);
                        f= false;
                        break;
                    }
                }
            }
            if(f)
                throw new IOException();
        }
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return pack;
    }

    public void update() {
        if (sender != null) {
            sender.update();
        }
    }

    public void close() {
        if (c != null)
            c.interrupt();
    }

    public Packet switcher(Packet pock){
        switch (pock.func) {
            case LOGIN:
                user.setLogin((String) pock.arguments[0]);
                user.setPass((String) pock.arguments[1]);
                user.setType(DBConnector.getUserType(user));
                pock.setArguments(user.getType());
                break;
            case ADD_USER:
                pock.setArguments(DBConnector.addUser((User) pock.arguments[0]));

                break;
            case CHANGE_USER_TYPE:
                User TempUser = (User) pock.arguments[0];
                pock.setArguments(DBConnector.changeUserType(TempUser));

                break;
            case CHANGE_USER_PASS:
                user.setPass((String) pock.arguments[0]);
                pock.setArguments(DBConnector.setNewPass(user));
                break;
            case GET_USERS_BY_TYPES:
                pock.setArguments(DBConnector.getUsersByType((UsersTypes) pock.arguments[0]));
                break;
            case GET_ALL_USERS_AND_TYPES:
                pock.setArguments(DBConnector.getAllUsersLoginsTypes());
                break;
            case GET_PROJECTS:
                pock.setArguments(DBConnector.getProjects(user));
                break;
            case ADD_PROJECTS:
                pock.setArguments(DBConnector.addProject((String) pock.arguments[0],
                        (String) pock.arguments[1]));

                break;
            case CHANGE_PROJECT_PRODUCT_OWNER:
                pock.setArguments(DBConnector.changeProjectProductOwner((String) pock.arguments[0],
                        (String) pock.arguments[1]));

                break;
            case GET_PROJECT_PRODUCT_OWNER:
                pock.setArguments(DBConnector.getProjectProductOwner((String) pock.arguments[0]));
                break;
            case GET_PROJECT_USERS:
                pock.setArguments(DBConnector.getProjectUsers((String) pock.arguments[0]));
                break;
            case GET_PROJECT_USERS_BY_TYPE:
                pock.setArguments(DBConnector.getProjectUsersByType((String) pock.arguments[0],(UsersTypes) pock.arguments[1]));
                break;
            case CHANGE_PROJECT_USERS:
                String[] s = pock.getArrayOfArgs(String[].class);
                pock.setArguments(DBConnector.changeProjectUsers(s[0], Arrays.copyOfRange(s, 1, s.length)));

                break;
            case ADD_PROJECT_TASK:
                pock.setArguments(DBConnector.addTask((Task) pock.arguments[0]));

                break;
            case GET_PROJECT_TASKS:
                pock.setArguments(DBConnector.getProjectTasks((String) pock.arguments[0]));
                break;
            case SET_PROJECT_TASK_COMPLEXITY:
                pock.setArguments(DBConnector.setTaskComplexity((Task) pock.arguments[0],user));

                break;
            case GET_PROJECT_USER_TASKS_COMPLEXITY:
                pock.setArguments(DBConnector.getProjectUserTasksComplexity((String) pock.arguments[0], user));
                break;
            case GET_TASK_SUBTASKS:
                pock.setArguments(DBConnector.getSubtasks((Task) pock.arguments[0]));
                break;
            case ADD_TASK_SUBTASK:
                pock.setArguments(DBConnector.addSubtask((Subtask) pock.arguments[0]));

                break;
            case ADD_TASK_DESCRIPTION:
                pock.setArguments(DBConnector.addTaskDescription((Task) pock.arguments[0],(String) pock.arguments[1]));

                break;
            case GET_TASK_DESCRIPTION:
                pock.setArguments(DBConnector.getTaskDescription((Task) pock.arguments[0]));
                break;
            case CHANGE_TASK_USERS:
                Object[] s1 = pock.arguments;
                pock.setArguments(DBConnector.changeTaskUsers((Task)s1[0], Arrays.copyOfRange(s1, 1, s1.length,String[].class)));

                break;
            case GET_TASK_USERS:
                pock.setArguments(DBConnector.getTaskUsers((Task) pock.arguments[0]));
                break;
            case CHANGE_SUBTASKS_COMPLETENESS:
                pock.setArguments(DBConnector.changeSubtaskCompleteness((Subtask) pock.arguments[0]));

                break;
        }
       return pock;
    }
}

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Scanner;
public class MultiThreadedTaskScheduler{
    public static void main(String[] args){
        Scanner scn = new Scanner(System.in);
        TaskQueue<Task> tasks = new TaskQueue<>();
        TaskWorker worker = new TaskWorker(tasks);
        int c = 0;
        int i =1;
        try{
            do{
                System.out.println("1. Add Email Task\n2. Add Backup Task\n3. Add Computation Task\n4. Start Task Execution\n5. Exit");
                c = scn.nextInt();
                switch(c){
                    case 1:
                        scn.nextLine();
                        System.out.println("Enter the sender email");
                        String sender= scn.nextLine();
                        System.out.println("Enter the receiver email:");
                        String receiver = scn.nextLine();
                        System.out.println("Enter the priority of task");
                        int priority = scn.nextInt();
                        scn.nextLine();
                        tasks.addTask(new EmailTask("T" + i,priority,new Email(sender),new Email(receiver)));
                        System.out.println("New task is added to queue");
                        i+=1;
                        break;
                    case 2:
                        scn.nextLine();
                        System.out.println("Enter the priority of task");
                        int priority2 = scn.nextInt();
                        scn.nextLine();
                        tasks.addTask(new DataBackUpTask("T" + i,priority2));
                        System.out.println("New task is added to queue");
                        i +=1;
                        break;
                    case 3:
                        scn.nextLine();
                        System.out.println("Enter the task's name");
                        String taskName = scn.nextLine();
                        System.out.println("Enter the task's priority");
                        int priority3 = scn.nextInt();
                        scn.nextLine();
                        tasks.addTask(new ComputationTask("T" +i,priority3,taskName));
                        System.out.println("New task is added to queue");
                        i+=1;
                        break;
                    case 4:
                        scn.nextLine();
                        System.out.println("Tasks are starting to execute");
                        worker.run();
                        System.out.println("Tasks are finished");
                        break;


                }
            }while(c!=5);
            System.out.println("Have a good day!");
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }
}
interface Executable{
    void execute();
}
class Email{
    private String email;
    private ArrayList<String> emailBox;
    public Email(String email){
        this.email = email;
        this.emailBox = new ArrayList<>();
    }
    public void getNewMail(String msg){
        emailBox.add(msg);
    }
    public String getEmail(){
        return email;
    }
}
abstract class Task implements Executable,Comparable<Task>{
    protected String taskId;
    protected int priority;
    public Task(String taskId,int priority){
        this.taskId = taskId;
        this.priority = priority;
    }
    public String getTaskId(){
        return taskId;
    }
    @Override
    public int compareTo(Task other){
        return Integer.compare(this.priority,other.priority);
    }
    public abstract void execute();
}
class EmailTask extends Task implements Executable{
    private Email sender;
    private Email receiver;
    public EmailTask(String taskId,int priority,Email sender,Email receiver){
        super(taskId,priority);
        this.sender = sender;
        this.receiver = receiver;
    }
    @Override
    public void execute(){
        Scanner scn = new Scanner(System.in);
        System.out.println("Enter the message to be sent");
        String msg = scn.nextLine();
        receiver.getNewMail(msg);
        System.out.println(sender.getEmail() + " send a mail to " + receiver.getEmail());
    }
}
class DataBackUpTask extends Task implements Executable{
    private ArrayList<Object> data;
    public DataBackUpTask(String taskId,int priority){
        super(taskId,priority);
        this.data = new ArrayList<>();
    }
    public DataBackUpTask(String taskId,int priority,ArrayList<Object> data){
        super(taskId,priority);
        this.data = data;
    }
    @Override
    public void execute(){
        Scanner scn = new Scanner(System.in);
        System.out.println("Enter the file name to store data:");
        String filename = scn.nextLine();
        try{
            FileOutputStream fileOut = new FileOutputStream(filename + ".ser");
            ObjectOutputStream oos = new ObjectOutputStream(fileOut);
            oos.writeObject(data);
            System.out.println("Data is stored at " + filename);
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }

    }

}
class ComputationTask extends Task implements Executable{
    private String taskName;
    public ComputationTask(String taskId,int priority,String taskName){
        super(taskId,priority);
        this.taskName = taskName;
    }
    @Override
    public void execute(){
        System.out.println("Task " + taskName + " is executed" );
    }
}
class TaskQueue<T extends Task>{
    private PriorityQueue<T> queue = new PriorityQueue<>();
    public void addTask(T task){
        queue.offer(task);
    }
    public T getNextTask(){
        return queue.poll();
    }
    public boolean isEmpty(){
        return queue.isEmpty();
    }
}
class TaskWorker extends Thread{
    private TaskQueue<? extends Task> taskQueue;
    public TaskWorker(TaskQueue<? extends Task> queue){
        this.taskQueue = queue;
    }
    public void run(){
        while(!taskQueue.isEmpty()){
            Task task = taskQueue.getNextTask();
            if(task != null){
                System.out.println("Executing task: " + task.getClass().getSimpleName());
                long x = System.currentTimeMillis();
                System.out.println("Task" + task.getTaskId() + " is starting to execute. Starting time: " + System.currentTimeMillis());
                task.execute();
                System.out.println("Task is executed. Finish time: " + System.currentTimeMillis() + ", Duration: " + (x-System.currentTimeMillis()));
            }
        }
    }
}
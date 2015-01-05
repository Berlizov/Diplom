package Structs;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Eugene Berlizov on 20.11.2014.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Subtask {
    private String taskname;
    private String project;
    private String name;
    Boolean finish=false;

    private Subtask(){}
    public Subtask(String taskname, String tsakID, String name,Boolean finish) {
        this.finish = finish;
        this.taskname = taskname;
        this.project = tsakID;
        this.name = name;
    }

    public Subtask(String name, String taskname, String tsakID) {
        this.name = name;
        this.taskname = taskname;
        this.project = tsakID;
    }

    public Boolean getFinish() {
        return finish;
    }

    public void setFinish(Boolean finish) {
        this.finish = finish;
    }

    public String getTaskname() {
        return taskname;
    }

    public String getProject() {
        return project;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "taskname='" + taskname + '\'' +
                ", project='" + project + '\'' +
                ", name='" + name + '\'' +
                ", finish=" + finish +
                '}';
    }
}

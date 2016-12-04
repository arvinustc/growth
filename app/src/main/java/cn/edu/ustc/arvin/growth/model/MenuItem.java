package cn.edu.ustc.arvin.growth.model;

import io.realm.RealmObject;

/**
 * Created by amazing on 2016/4/8.
 */
public class MenuItem extends RealmObject {
    private String name;
    private String file;
    private String icon;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "MenuItem{" +
                "name='" + name + '\'' +
                ", file='" + file + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }
}
